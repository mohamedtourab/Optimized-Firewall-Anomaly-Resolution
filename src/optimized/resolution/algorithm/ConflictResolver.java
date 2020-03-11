package optimized.resolution.algorithm;

import ofar.generated.classes.conflicts.Anomalies;
import ofar.generated.classes.conflicts.AnomalyNames;
import ofar.generated.classes.conflicts.AnomalyType;
import ofar.generated.classes.rules.RuleType;
import ofar.generated.classes.rules.Rules;

import javax.xml.crypto.Data;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


public class ConflictResolver {

    private List<AnomalyType> removedAnomalies;
    private List<RuleType> removedRules;
    private HashSet<BigInteger> removedRulesIDs;

    private Rules rules;
    private Anomalies anomalies;


    public ConflictResolver(Rules rules, Anomalies anomalies) {
        this.anomalies = anomalies;
        this.rules = rules;
        removedAnomalies = new ArrayList<>();
        removedRules = new ArrayList<>();
        removedRulesIDs = new HashSet<>();
    }

    public void resolveAnomalies() {
        removeIrrelevanceAnomaly();
        //removeDuplicationAnomaly();
        rules.getRule().forEach(System.out::println);
        //anomalies.getAnomaly().forEach(System.out::println);

        System.out.println("\nThe removed rules and anomalies");
        removedRules.forEach(System.out::println);
        removedAnomalies.forEach(System.out::println);
    }


    private void removeIrrelevanceAnomaly() {

        //Remove Irrelevance Rules
        for (AnomalyType anomaly : anomalies.getAnomaly()) {
            if (anomaly.getAnomalyName().equals(AnomalyNames.IRRELEVANCE)) {
                int ruleID = anomaly.getRuleID().get(0).intValue();
                RuleType ruleToBeRemoved = getRuleUsingRuleID(rules.getRule(), ruleID);
                if (ruleToBeRemoved != null) {
                    removedRules.add(ruleToBeRemoved);
                }
                rules.getRule().remove(ruleToBeRemoved);
            }
        }
        //Remove irrelevance anomalies
        List<AnomalyType> upatedAnomalies = anomalies.getAnomaly().stream()
                .filter(anomaly -> {
                    anomaly.getRuleID().stream().filter(ruleId->{
                        
                    });


                })
                .collect(Collectors.toList());
        upatedAnomalies.stream().forEach(System.out::println);


/*

        checkRule(anomaly, rulesIdsToRemove){
            List<BigInteger> rulesInAnomaly = anomaly.getRuleId();
            for(BigInteger i : rulesInAnomay){
                if(rulesIds.contains(i)){
                    return true;
                }
            }
            return false;
        }

        List<AnomalyType> upatedAnomalies = listOfAnomalies.stream()
                .filter(anomaly -> checkRule(anomaly, rulesIdsToRemove))
                .collect(Collectors.toList());

  */


/*
        for (RuleType item : removedRules) {
            for (AnomalyType anomaly : listOfAnomalies) {
                for (BigInteger ruleID : anomaly.getRuleID()) {
                    if (ruleID.equals(item.getRuleID())) {
                        removedAnomalies.add(anomaly);
                        anomalies.getAnomaly().remove(anomaly);
                    }
                }
            }
        }
*/
    }

    /*private void removeDuplicationAnomaly(){
        listOfAnomalies = new ArrayList<>(anomalies.getAnomaly());
        //Remove Irrelevance Anomalies
        for (AnomalyType anomaly : listOfAnomalies) {
            if (anomaly.getAnomalyName().equals(AnomalyNames.DUPLICATION)) {
                int firstRuleID = anomaly.getRuleID().get(0).intValue();
                int secondRuleID = anomaly.getRuleID().get(1).intValue();
                System.out.println(firstRuleID);
                System.out.println(secondRuleID );
                RuleType firstRuleToBeRemoved = getRuleUsingRuleID(rules.getRule(), firstRuleID);
                RuleType secondRuleToBeRemoved = getRuleUsingRuleID(rules.getRule(), secondRuleID);

                if( firstRuleToBeRemoved.getPriority().intValue()>secondRuleToBeRemoved.getPriority().intValue() ){
                    System.out.println(firstRuleToBeRemoved.getPriority().intValue());
                    System.out.println(secondRuleToBeRemoved.getPriority().intValue());
                    removedRules.add(secondRuleToBeRemoved);
                    rules.getRule().remove(secondRuleToBeRemoved);
                }else{
                    removedRules.add(firstRuleToBeRemoved);
                    rules.getRule().remove(firstRuleToBeRemoved);
                }
                removedAnomalies.add(anomaly);
                anomalies.getAnomaly().remove(anomaly);
            }
        }
    }
*/
    private RuleType getRuleUsingRuleID(List<RuleType> listOfRules, int ruleID) {
        for (RuleType rule : listOfRules) {
            if (rule.getRuleID().intValue() == ruleID)
                return rule;
        }
        return null;
    }


    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }

    public Anomalies getAnomalies() {
        return anomalies;
    }

    public void setAnomalies(Anomalies anomalies) {
        this.anomalies = anomalies;
    }


    public static void main(String[] args) {

        ConflictResolver conflictResolver = new ConflictResolver(DataCreator.createRules(), DataCreator.createAnomalies());
        conflictResolver.resolveAnomalies();
    }


}


