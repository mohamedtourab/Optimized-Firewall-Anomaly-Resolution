package optimized.resolution.algorithm.classes;

import ofar.generated.classes.conflicts.Anomalies;
import ofar.generated.classes.conflicts.AnomalyNames;
import ofar.generated.classes.conflicts.AnomalyType;
import ofar.generated.classes.rules.RuleType;
import ofar.generated.classes.rules.Rules;
import org.apache.commons.net.util.SubnetUtils;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class UnnecessaryAnomalyChecker {
    private final Rules rules;

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

    public Anomalies checkForUnnecessaryAnomalies() {
        Anomalies unnecessaryAnomalies = new Anomalies();
        for (int i = 0; i < rules.getRule().size(); i++) {
            RuleType rx = rules.getRule().get(i);
            for (int j = i + 1; j < rules.getRule().size(); j++) {
                RuleType ry = rules.getRule().get(j);
                if (haveUnnecessaryAnomaly(rx, ry)) {
                    AnomalyType anomalyType = new AnomalyType();
                    anomalyType.setAnomalyID(BigInteger.valueOf(1));
                    anomalyType.setAnomalyName(AnomalyNames.UNNECESSARY);
                    anomalyType.getRule().add(rx);
                    anomalyType.getRule().add(ry);
                    unnecessaryAnomalies.getAnomaly().add(anomalyType);
                }
            }
        }
        unnecessaryAnomalies.getAnomaly().forEach(anomalyType -> {
            System.out.println("Aunn(" + anomalyType.getRule().get(0).getRuleID() + ", " + anomalyType.getRule().get(1).getRuleID() + ")");
        });
        return unnecessaryAnomalies;
    }

    private boolean haveUnnecessaryAnomaly(RuleType rx, RuleType ry) {

        if (specifySameAction(rx, ry)) {
            if (rxPrecedesRy(rx, ry)) {
                return allPacketsMatchedByRxAreMatchedByRy(rx, ry);
            }
        }
        return false;
    }

    private boolean specifySameAction(RuleType rx, RuleType ry) {
        return rx.getAction().equals(ry.getAction());
    }

    private boolean ruleExistBetweenRxAndRy(RuleType rx, RuleType ry) {
        return false;
    }

    private boolean isDisjoint(RuleType r1, RuleType r2) {
        return false;
    }

    private boolean rxPrecedesRy(RuleType rx, RuleType ry) {
        return rx.getPriority().compareTo(ry.getPriority()) < 0;
    }

    private boolean allPacketsMatchedByRxAreMatchedByRy(RuleType rx, RuleType ry) {
        return compareIP(rx.getIPsrc(), ry.getIPsrc()) &&
                compareIP(rx.getIPdst(), ry.getIPdst()) &&
                rxPortIncludedInRy(rx.getPsrc(), ry.getPsrc()) &&
                rxPortIncludedInRy(rx.getPdst(), ry.getPdst());
    }

    /**
     * @param rx The IP of the first firewall rule
     * @param ry The IP of the second firewall rule
     * @return True if Rx is included inside Ry or Rx == Ry
     */
    private boolean compareIP(String rx, String ry) {
        if (ry.equals("*")) return true;
        if (rx.equals("*")) return false;

        if(checkIPTypes(rx, ry) == IP_TYPES.BOTH_IP)
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
     *
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
     *
     * @param ip Input Address whether it is an IP address or Subnet mask
     * @return the type of the address i.e (SUBNET_MASK,IP_ADDRESS)
     */
    private IP_TYPE checkSingleIPType(String ip) {
        if (ip.equals("*")) return IP_TYPE.SUBNET_MASK;
        String[] ipArr = ip.split("/");
        return ipArr.length == 2 ? IP_TYPE.SUBNET_MASK : IP_TYPE.IP_ADDRESS;
    }

    public static void main(String[] args) {
        UnnecessaryAnomalyChecker unnecessaryAnomalyChecker = new UnnecessaryAnomalyChecker(DataGenerator.createRules());
        unnecessaryAnomalyChecker.checkForUnnecessaryAnomalies();

    }
}
