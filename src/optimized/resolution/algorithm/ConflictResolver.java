package optimized.resolution.algorithm;

import ofar.generated.classes.conflicts.Anomalies;
import ofar.generated.classes.conflicts.AnomalyNames;
import ofar.generated.classes.conflicts.AnomalyType;
import ofar.generated.classes.rules.RuleType;
import ofar.generated.classes.rules.Rules;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ConflictResolver {
    private Rules rules;
    private Anomalies anomalies;


    public ConflictResolver(Rules rules, Anomalies anomalies) throws CloneNotSupportedException {
        this.anomalies = anomalies;
        this.rules = rules;
    }

    public void resolveAnomalies() {
        final RemovedEntries irrelevanceRemovedEntries = removeIrrelevanceAnomaly();
        final RemovedEntries duplicateRemovedEntries = removeDuplicationAnomaly();
        final Rules removedRules = new Rules();
        final Anomalies removedAnomalies = new Anomalies();

        removedRules.getRule().addAll(Stream
                .of(irrelevanceRemovedEntries.getRemovedRules(), duplicateRemovedEntries.getRemovedRules())
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
        removedAnomalies.getAnomaly().addAll(Stream
                .of(irrelevanceRemovedEntries.getRemovedAnomalies(), duplicateRemovedEntries.getRemovedAnomalies())
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
    }

    private RemovedEntries removeIrrelevanceAnomaly() {
        final Set<RuleType> removedRules = new HashSet<>();
        final Set<AnomalyType> removedAnomalies = new HashSet<>();
        //Remove Irrelevance Rules
        for (AnomalyType anomaly : anomalies.getAnomaly()) {
            if (anomaly.getAnomalyName().equals(AnomalyNames.IRRELEVANCE)) {
                BigInteger ruleID = anomaly.getRuleID().get(0);
                RuleType ruleToBeRemoved = getRuleUsingRuleID(rules.getRule(), ruleID);
                removedRules.add(ruleToBeRemoved);
                rules.getRule().remove(ruleToBeRemoved);
            }
        }

        //Remove all anomalies caused by the removed rules
        removedRules.forEach(oneRule -> {
            anomalies.getAnomaly().forEach(oneAnomaly -> {
                if (oneAnomaly.getRuleID().contains(oneRule.getRuleID()))
                    removedAnomalies.add(oneAnomaly);
            });
        });
        removedAnomalies.forEach(a -> {
            anomalies.getAnomaly().remove(a);
        });
        return new RemovedEntries(removedRules, removedAnomalies);
    }

    private RemovedEntries removeDuplicationAnomaly() {
        final Set<RuleType> removedRules = new HashSet<>();
        final Set<AnomalyType> removedAnomalies = new HashSet<>();
        final HashSet<AnomalyType> localRemovedAnomalies = new HashSet<>();
        final HashSet<RuleType> localRemovedRules = new HashSet<>();
        //Remove Irrelevance Rules
        for (AnomalyType anomaly : anomalies.getAnomaly()) {
            if (anomaly.getAnomalyName().equals(AnomalyNames.DUPLICATION)) {
                BigInteger firstRuleId = anomaly.getRuleID().get(0);
                BigInteger secondRuleId = anomaly.getRuleID().get(1);
                RuleType rule1 = getRuleUsingRuleID(rules.getRule(), firstRuleId);
                RuleType rule2 = getRuleUsingRuleID(rules.getRule(), secondRuleId);

                //Determine which rule to remove based on the priority
                if (rule1.getPriority().compareTo(rule2.getPriority()) > 0) {
                    localRemovedRules.add(rule1);
                    removedRules.add(rule1);
                } else {
                    localRemovedRules.add(rule2);
                    removedRules.add(rule2);
                }
            }
        }
        //Remove all anomalies caused by the removed rules
        localRemovedRules.forEach(oneRule -> {
            anomalies.getAnomaly().forEach(oneAnomaly -> {
                if (oneAnomaly.getRuleID().contains(oneRule.getRuleID())) {
                    removedAnomalies.add(oneAnomaly);
                    localRemovedAnomalies.add(oneAnomaly);
                }
            });
        });
        localRemovedAnomalies.forEach(a -> {
            anomalies.getAnomaly().remove(a);
        });
        localRemovedRules.forEach(r -> {
            rules.getRule().remove(r);
        });
        return new RemovedEntries(removedRules, removedAnomalies);
    }

    private RuleType getRuleUsingRuleID(List<RuleType> listOfRules, BigInteger ruleID) {
        RuleType r = new RuleType();
        for (RuleType rule : listOfRules) {
            if (rule.getRuleID().equals(ruleID))
                r = rule;
        }
        return r;
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
