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

    private HashSet<AnomalyType> removedAnomalies;
    private HashSet<RuleType> removedRules;

    private Rules rules;
    private Anomalies anomalies;
    private Rules clonedRules;
    private Anomalies clonedAnomalies;


    public ConflictResolver(Rules rules, Anomalies anomalies) throws CloneNotSupportedException {
        this.anomalies = anomalies;
        this.rules = rules;
        clonedAnomalies =(Anomalies) this.anomalies.clone();
        clonedRules =(Rules) this.rules.clone();
        removedAnomalies = new HashSet<>();
        removedRules = new HashSet<>();
    }

    public void resolveAnomalies() {
        removeIrrelevanceAnomaly();
        //removeDuplicationAnomaly();
        rules.getRule().forEach(System.out::println);
        anomalies.getAnomaly().forEach(System.out::println);

        System.out.println("\nThe removed rules");
        removedRules.forEach(System.out::println);
        System.out.println("The removed anomalies are: ");
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
        removedRules.forEach(oneRule -> {
            clonedAnomalies.getAnomaly().forEach(oneAnomaly -> {
                if (oneAnomaly.getRuleID().contains(oneRule.getRuleID())){
                    anomalies.getAnomaly().remove(oneAnomaly);
                    removedAnomalies.add(oneAnomaly);
                }

            });
        });
//        //Remove all anomalies caused the removed rules
//        removedRules.forEach(oneRule -> {
//            anomalies.getAnomaly().forEach(oneAnomaly -> {
//                if (oneAnomaly.getRuleID().contains(oneRule.getRuleID()))
//                    removedAnomalies.add(oneAnomaly);
//            });
//        });
//        removedAnomalies.forEach(a -> {
//            anomalies.getAnomaly().remove(a);
//        });
    }

    private void removeDuplicationAnomaly() {
        //Remove Irrelevance Rules
        for (AnomalyType anomaly : anomalies.getAnomaly()) {
            if (anomaly.getAnomalyName().equals(AnomalyNames.DUPLICATION)) {
                int ruleID = anomaly.getRuleID().get(0).intValue();
                RuleType ruleToBeRemoved = getRuleUsingRuleID(rules.getRule(), ruleID);
                if (ruleToBeRemoved != null) {
                    removedRules.add(ruleToBeRemoved);
                }
                rules.getRule().remove(ruleToBeRemoved);
            }
        }

        //Remove all anomalies caused the removed rules
        removedRules.forEach(oneRule -> {
            anomalies.getAnomaly().forEach(oneAnomaly -> {
                if (oneAnomaly.getRuleID().contains(oneRule.getRuleID()))
                    removedAnomalies.add(oneAnomaly);
            });
        });
        removedAnomalies.forEach(a -> {
            anomalies.getAnomaly().remove(a);
        });
    }



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


    public static void main(String[] args) throws CloneNotSupportedException {

        ConflictResolver conflictResolver = new ConflictResolver(DataCreator.createRules(), DataCreator.createAnomalies());
        conflictResolver.resolveAnomalies();
    }


}


