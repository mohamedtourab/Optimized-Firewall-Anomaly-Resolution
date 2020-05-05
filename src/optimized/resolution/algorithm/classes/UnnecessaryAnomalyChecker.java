package optimized.resolution.algorithm.classes;

import ofar.generated.classes.conflicts.Anomalies;
import ofar.generated.classes.conflicts.AnomalyNames;
import ofar.generated.classes.conflicts.AnomalyType;
import ofar.generated.classes.rules.RuleType;
import ofar.generated.classes.rules.Rules;
import org.apache.commons.net.util.SubnetUtils;

import java.math.BigInteger;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnnecessaryAnomalyChecker {
    private final Rules rules;
    private static final Logger LOGGER = Logger.getLogger(UnnecessaryAnomalyChecker.class.getName());

    enum IP_TYPES {
        BOTH_IP,
        BOTH_SUBNET,
        ONE_SUBNET_AND_ONE_IP_ADDRESS
    }

    enum IP_TYPE {
        IP_ADDRESS,
        SUBNET_MASK,
    }

    public UnnecessaryAnomalyChecker(Rules rules) {
        this.rules = rules;
    }

    /**
     * @return Return the list of unnecessary anomalies found in the list of rules created when an instance of the
     * class is created
     * @throws Exception If the format of the IP address are incorrect or empty this exception is thrown
     */
    public Anomalies checkForUnnecessaryAnomalies(int anomalyListSize) throws Exception {
        Anomalies unnecessaryAnomalies = new Anomalies();
        for (int i = 0; i < rules.getRule().size(); i++) {
            RuleType rx = rules.getRule().get(i);
            for (int j = i + 1; j < rules.getRule().size(); j++) {
                RuleType ry = rules.getRule().get(j);
                if (haveUnnecessaryAnomaly(rx, ry)) {
                    AnomalyType anomalyType = new AnomalyType();
                    anomalyType.setAnomalyID(BigInteger.valueOf(anomalyListSize++));
                    anomalyType.setAnomalyName(AnomalyNames.UNNECESSARY);
                    anomalyType.getRule().add(rx);
                    anomalyType.getRule().add(ry);
                    unnecessaryAnomalies.getAnomaly().add(anomalyType);
                }
            }
        }
        unnecessaryAnomalies.getAnomaly().forEach(anomalyType -> LOGGER.log(Level.INFO, "Aunn(" + anomalyType.getRule().get(0).getRuleID() + ", " + anomalyType.getRule().get(1).getRuleID() + ")" + "ID->" + anomalyType.getAnomalyID()));
        return unnecessaryAnomalies;
    }

    /**
     * @param rx First Firewall rule
     * @param ry Second Firewall rule
     * @return true is there is unnecessary anomaly between these two rules else returns false.
     * @throws Exception If the format of the IP address are incorrect or empty this exception is thrown
     */
    private boolean haveUnnecessaryAnomaly(RuleType rx, RuleType ry) throws Exception {

        if (specifySameAction(rx, ry)) {
            if (rxPrecedesRy(rx, ry)) {
                if (allPacketsMatchedByRxAreMatchedByRy(rx, ry))
                    return !nonDisjointRuleExistBetweenRxAndRyWithOppositeActionToRx(rx, ry);
            }
        }
        return false;
    }

    private boolean specifySameAction(RuleType rx, RuleType ry) {
        return rx.getAction().equals(ry.getAction());
    }

    /**
     * @param rx First Firewall rule
     * @param ry Second Firewall rule
     * @return true the two rules are totally disjoint and specify the same action else returns false
     * @throws Exception If the format of the IP address are incorrect or empty this exception is thrown
     */
    private boolean nonDisjointRuleExistBetweenRxAndRyWithOppositeActionToRx(RuleType rx, RuleType ry) throws Exception {

        List<RuleType> ruleToCheck = getAllElementsInBetweenWithOppositeAction(rx, ry);
        for (RuleType rz : ruleToCheck) {
            if (!specifySameAction(rx, rz)) {
                if (!isTotallyDisjoint(rx, rz)) {
                    //There is a correlated rule that specify the different action
                    LOGGER.log(Level.WARNING, "Rule " + rx.getRuleID() + " and Rule " + rz.getRuleID() + " are specifying different action and correlated and Ry is " + ry.getRuleID());
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * This function can be used to retrieve all the rules that exists between two rules rx and ry in the firewall table
     *
     * @param rx First Firewall Rule
     * @param ry Second Firewall Rule
     * @return List of rules that are between the two input rules
     */
    private List<RuleType> getAllElementsInBetweenWithOppositeAction(RuleType rx, RuleType ry) {
        List<RuleType> rulesList = new ArrayList<>();
        boolean startFilling = false;
        for (RuleType rule : rules.getRule()) {
            if (rule.equals(ry))
                return rulesList;
            if (startFilling && !(rule.getAction().equals(rx.getAction())))
                rulesList.add(rule);
            if (rule.equals(rx))
                startFilling = true;
        }
        return rulesList;
    }

    /**
     * @param rx First Firewall rule
     * @param ry Second Firewall rule
     * @return true if the first rule precedes the second rule in the order of the firewall table
     */
    private boolean rxPrecedesRy(RuleType rx, RuleType ry) {
        return rx.getPriority().compareTo(ry.getPriority()) < 0;
    }

    /**
     * @param rx First Firewall rule
     * @param ry Second Firewall rule
     * @return true if all packet matched by the first rule are matched by the second rule too.
     */
    private boolean allPacketsMatchedByRxAreMatchedByRy(RuleType rx, RuleType ry) {
        return compareIP(rx.getIPsrc(), ry.getIPsrc()) &&
                compareIP(rx.getIPdst(), ry.getIPdst()) &&
                rxPortIncludedInRy(rx.getPsrc(), ry.getPsrc()) &&
                rxPortIncludedInRy(rx.getPdst(), ry.getPdst());
    }

    /**
     * @param r1 First Firewall rule
     * @param r2 Second Firewall rule
     * @return true if all the conditions for these two rule to be disjoint are met else returns false
     * @throws Exception If the format of the IP address are incorrect or empty this exception is thrown
     */
    private boolean isTotallyDisjoint(RuleType r1, RuleType r2) throws Exception {
        //Check all rules between R1 and R2 if Rz between R1 and R2 is not disjoint from R1 and have opposite action return false
        // check for src
        final boolean first = prepareDisjointCheck(
                r1.getIPsrc(),
                r2.getIPsrc(),
                getIpType(r1.getIPsrc()),
                getIpType(r2.getIPsrc()));
        final boolean second = prepareDisjointCheck(
                r1.getIPdst(),
                r2.getIPdst(),
                getIpType(r1.getIPdst()),
                getIpType(r2.getIPdst()));

        return (first || second) || (isPortDisjoint(r1.getPsrc(), r2.getPsrc()) || isPortDisjoint(r1.getPdst(), r2.getPdst()));
    }

    /**
     * This function is used to handle the calling of isDisjoint function as the Disjoint function can take as input (Subnet Mask,IP address)
     * in case it took as an argument one subnet mask and IP address the IP address must be the first argument and the second argument should be the subenet mask
     *
     * @param address1 First IP address or Subnet Mask
     * @param address2 Second IP address or Subtnet Mask
     * @param type1    First IP address type i.e (IP_TYPE.IP_ADDRESS,IP_TYPE.SUBNET_MASK)
     * @param type2    Second IP address type i.e (IP_TYPE.IP_ADDRESS,IP_TYPE.SUBNET_MASK)
     * @return True if both addresses are completely disjoint, otherwise returns false
     */
    private boolean prepareDisjointCheck(String address1, String address2, IP_TYPE type1, IP_TYPE type2) {
        if (type1 == IP_TYPE.IP_ADDRESS && type2 == IP_TYPE.SUBNET_MASK) {
            return isDisjoint(address1, address2, IP_TYPES.ONE_SUBNET_AND_ONE_IP_ADDRESS);
        } else if (type1 == IP_TYPE.SUBNET_MASK && type2 == IP_TYPE.IP_ADDRESS) {
            return isDisjoint(address2, address1, IP_TYPES.ONE_SUBNET_AND_ONE_IP_ADDRESS);
        } else if (type1 == IP_TYPE.SUBNET_MASK && type2 == IP_TYPE.SUBNET_MASK) {
            return isDisjoint(address1, address2, IP_TYPES.BOTH_SUBNET);
        } else {
            return !address1.equals(address2);
        }
    }

    /**
     * @param address1 The first IP address or subnet Mask.
     * @param address2 The second IP address or subnet Mask.
     * @param ipMode   This enum describes the values inside the address1 and address2 variables i.e (ONE_SUBNET_AND_ONE_IP_ADDRESS, BOTH_SUBNET)
     * @return true if both addresses are completely disjoint, otherwise returns false
     */
    private boolean isDisjoint(String address1, String address2, IP_TYPES ipMode) {
        if (address1.equals("*") || address2.equals("*"))
            return false;
        if (ipMode == IP_TYPES.ONE_SUBNET_AND_ONE_IP_ADDRESS) {
            final SubnetUtils utils = new SubnetUtils(address2);
            List<String> allIpsInSubnet = Arrays.asList(utils.getInfo().getAllAddresses());
            return !allIpsInSubnet.contains(address1);
        } else {
            final SubnetUtils utils1 = new SubnetUtils(address1);
            final SubnetUtils utils2 = new SubnetUtils(address2);

            final List<String> allIpsInSubnet1 = Arrays.asList(utils1.getInfo().getAllAddresses());
            final String[] allIpsInSubnet2 = utils2.getInfo().getAllAddresses();
            final Set<String> set = new HashSet<>(allIpsInSubnet1);

            for (String s : allIpsInSubnet2) {
                if (set.contains(s)) {
                    return false;
                }
            }
            return true;
        }
    }


    /**
     * @param rx The IP of the first firewall rule
     * @param ry The IP of the second firewall rule
     * @return True if Rx is included inside Ry or Rx == Ry
     */
    private boolean compareIP(String rx, String ry) {
        if (ry.equals("*")) return true;
        if (rx.equals("*")) return false;

        if (checkIPTypes(rx, ry) == IP_TYPES.BOTH_IP)
            return rx.equals(ry);
        else if (checkIPTypes(rx, ry) == IP_TYPES.BOTH_SUBNET) {

            SubnetUtils smallUtils = new SubnetUtils(rx);
            SubnetUtils largeUtils = new SubnetUtils(ry);

            List<String> allIpsInSubnet1 = Arrays.asList(smallUtils.getInfo().getAllAddresses());
            List<String> allIpsInSubnet2 = Arrays.asList(largeUtils.getInfo().getAllAddresses());
            return allIpsInSubnet2.containsAll(allIpsInSubnet1);

        } else {
            //checkIPTypes(rx, ry) == IP_TYPES.ONE_SUBNET_AND_ONE_IP_ADDRESS
            if (checkSingleIPType(ry) == IP_TYPE.IP_ADDRESS && checkSingleIPType(rx) == IP_TYPE.SUBNET_MASK) {
                //This means that rx is a range of addresses and ry is a single address
                return false;
            } else {
                /*This means that ry is a range of addresses and rx is a single address
                 * So we should check if rx is is inside ry*/
                HashSet<String> allIpsInSubnet3 = new HashSet<>(Arrays.asList(new SubnetUtils(ry).getInfo().getAllAddresses()));
                return allIpsInSubnet3.contains(rx);
            }
        }

    }


    /**
     * @param rx First Firewall Rule
     * @param ry Second Firewall Rule
     * @return True if rx port is inside ry range of ports or if rx port is equal to ry port
     */
    private boolean rxPortIncludedInRy(String rx, String ry) {
        if (rx.equals(ry))
            return true;
        else return ry.equals("*");
    }

    /**
     * @param ip Input Address whether it is an IP address or Subnet mask
     * @return the type of the address i.e (SUBNET_MASK,IP_ADDRESS)
     */
    private IP_TYPE checkSingleIPType(String ip) {
        if (ip.equals("*")) return IP_TYPE.SUBNET_MASK;
        String[] ipArr = ip.split("/");
        return ipArr.length == 2 ? IP_TYPE.SUBNET_MASK : IP_TYPE.IP_ADDRESS;
    }

    /**
     * @param px First port number. it could be "*" to specify all ports
     * @param pz Second port number. it could be "*" to specify all ports
     * @return true if both port numbers are disjoint else returns false
     */
    private boolean isPortDisjoint(String px, String pz) {
        if (px.equals("*") || pz.equals("*"))
            return false;
        else
            return !px.equals(pz);
    }

    /**
     * @param address The IP address that needed to be check if it's an IP address or a Subnet Mask
     * @return the type of the address i.e (IP_TYPE.SUBNET_MASK,IP_TYPE.IP_ADDRESS)
     * @throws Exception If the format of the IP address are incorrect or empty this exception is thrown
     */
    private IP_TYPE getIpType(String address) throws Exception {
        if (address == null || address.length() == 0) {
            throw new Exception("Empty IP Address");
        }

        if (address.length() == 1 && address.equals("*")) {
            return IP_TYPE.SUBNET_MASK;
        }

        final String base = "([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
        final String regexStringForSubnet = String.format("%s\\.%s\\.%s\\.%s/[1-32]", base, base, base, base);
        final String regexStringForIp = String.format("%s\\.%s\\.%s\\.%s", base, base, base, base);

        final Matcher subnet = Pattern.compile(regexStringForSubnet).matcher(address);
        final Matcher ip = Pattern.compile(regexStringForIp).matcher(address);
        if (subnet.find()) {
            return IP_TYPE.SUBNET_MASK;
        } else if (ip.find()) {
            return IP_TYPE.IP_ADDRESS;
        } else {
            throw new Exception("Incorrect IP Address format");
        }
    }

    /**
     * @param rx First IP address or Subnet Mask
     * @param ry Second IP address or Subnet Mask
     * @return The type of the received addresses ( BOTH_SUBNET,ONE_SUBNET_AND_ONE_IP_ADDRESS,BOTH_IP)
     */
    private IP_TYPES checkIPTypes(String rx, String ry) {
        IP_TYPES type;

        if (checkSingleIPType(rx) == IP_TYPE.SUBNET_MASK && checkSingleIPType(ry) == IP_TYPE.SUBNET_MASK) {
            type = IP_TYPES.BOTH_SUBNET;
            return type;
        } else if (checkSingleIPType(rx) == IP_TYPE.SUBNET_MASK || checkSingleIPType(ry) == IP_TYPE.SUBNET_MASK) {
            type = IP_TYPES.ONE_SUBNET_AND_ONE_IP_ADDRESS;
            return type;
        } else
            type = IP_TYPES.BOTH_IP;

        return type;
    }


    public static void main(String[] args) throws Exception {
        UnnecessaryAnomalyChecker unnecessaryAnomalyChecker = new UnnecessaryAnomalyChecker(DataGenerator.createRules());
        unnecessaryAnomalyChecker.checkForUnnecessaryAnomalies(DataGenerator.createAnomalies().getAnomaly().size());

    }
}
