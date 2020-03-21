package optimized.resolution.algorithm;

import ofar.generated.classes.*;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ConflictResolver {
    private Rules rules;
    private Anomalies anomalies;


    public ConflictResolver(Rules rules, Anomalies anomalies) {
        this.anomalies = anomalies;
        this.rules = rules;
    }

    public void resolveAnomalies() {
        final RemovedEntries irrelevanceRemovedEntries = removeIrrelevanceAnomaly();
        final RemovedEntries duplicateRemovedEntries = removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.DUPLICATION);
        final RemovedEntries shadowingRedundancyRemovedEntries = removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.SHADOWING_REDUNDANCY);

        final Rules removedRules = new Rules();
        final Anomalies removedAnomalies = new Anomalies();

        removedRules.getRule().addAll(Stream
                .of(irrelevanceRemovedEntries.getRemovedRules(), duplicateRemovedEntries.getRemovedRules(), shadowingRedundancyRemovedEntries.getRemovedRules())
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
        removedAnomalies.getAnomaly().addAll(Stream
                .of(irrelevanceRemovedEntries.getRemovedAnomalies(), duplicateRemovedEntries.getRemovedAnomalies(), shadowingRedundancyRemovedEntries.getRemovedAnomalies())
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
    }

    private RemovedEntries removeIrrelevanceAnomaly() {
        final Set<RuleType> removedRules = new HashSet<>();
        final Set<AnomalyType> removedAnomalies = new HashSet<>();
        //Remove Irrelevance Rules
        for (AnomalyType anomaly : anomalies.getAnomaly()) {
            if (anomaly.getAnomalyName().equals(AnomalyNames.IRRELEVANCE)) {
                RuleType rule = anomaly.getRule().get(0);
                removedRules.add(rule);
                rules.getRule().remove(rule);
            }
        }

        //Remove all anomalies caused by the removed rules
        removedRules.forEach(oneRule -> anomalies.getAnomaly().forEach(oneAnomaly -> {
            if (oneAnomaly.getRule().contains(oneRule))
                removedAnomalies.add(oneAnomaly);
        }));
        removedAnomalies.forEach(a -> anomalies.getAnomaly().remove(a));
        return new RemovedEntries(removedRules, removedAnomalies);
    }

    private RemovedEntries removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames anomalyName) {
        final Set<RuleType> removedRules = new HashSet<>();
        final Set<AnomalyType> removedAnomalies = new HashSet<>();
        final HashSet<AnomalyType> localRemovedAnomalies = new HashSet<>();
        final HashSet<RuleType> localRemovedRules = new HashSet<>();

        //Remove Irrelevance Rules
        for (AnomalyType anomaly : anomalies.getAnomaly()) {
            if (anomaly.getAnomalyName().equals(anomalyName)) {
                RuleType rule1 = anomaly.getRule().get(0);
                RuleType rule2 = anomaly.getRule().get(1);
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
        localRemovedRules.forEach(oneRule -> anomalies.getAnomaly().forEach(oneAnomaly -> {
            if (oneAnomaly.getRule().contains(oneRule)) {
                removedAnomalies.add(oneAnomaly);
                localRemovedAnomalies.add(oneAnomaly);
            }
        }));
        localRemovedAnomalies.forEach(a -> anomalies.getAnomaly().remove(a));
        localRemovedRules.forEach(r -> rules.getRule().remove(r));
        return new RemovedEntries(removedRules, removedAnomalies);
    }

    private List<AnomalyType> getConflictAnomalies() {
        return anomalies.getAnomaly().stream().filter(a -> {
            AnomalyNames anomalyName = a.getAnomalyName();
            return anomalyName.equals(AnomalyNames.CONTRADICTION) || anomalyName.equals(AnomalyNames.SHADOWING_CONFLICT) || anomalyName.equals(AnomalyNames.CORRELATION);
        }).collect(Collectors.toList());
    }

    private void executeSolveRequest(SolveRequest solveRequest) {
        final Set<RuleType> removedRules = new HashSet<>();
        final Set<AnomalyType> removedAnomalies = new HashSet<>();

        //Solve Contradiction
        removedRules.addAll(solveRequest.getContradictionSolutions().stream().filter(ContradictionSolutionType::isToRemove).map(a -> getRuleUsingRuleID(rules.getRule(), BigInteger.valueOf(a.getRuleId()))).collect(Collectors.toSet()));

        //Solve Shadowing Conflict
        //Perform the first solution to get the rules to be deleted
        removedRules.addAll(solveRequest.getShadowingConflictSolutions().stream().filter(ShadowingConflictSolutionType::isToRemove).map(a -> getRuleUsingRuleID(rules.getRule(), BigInteger.valueOf(a.getRuleId()))).collect(Collectors.toSet()));
        //Perform the second solution to flip the order
        solveRequest.getShadowingConflictSolutions().stream().filter(ShadowingConflictSolutionType::isToChangeOrder).forEach(l -> {
            AnomalyType anomaly = getAnomalyUsingAnomalyID(anomalies.getAnomaly(), BigInteger.valueOf(l.getAnomalyId()));
            int rule2Index=rules.getRule().indexOf(anomaly.getRule().get(0));
            int rule1Index=rules.getRule().indexOf(anomaly.getRule().get(1));
            BigInteger temp = anomaly.getRule().get(0).getPriority();
            anomaly.getRule().get(0).setPriority(anomaly.getRule().get(1).getPriority());
            anomaly.getRule().get(1).setPriority(temp);
            Collections.swap(rules.getRule(),rule1Index,rule2Index);
        });

        //Solve Correlation
        solveRequest.getCorrelationSolutions().stream().filter(CorrelationSolutionType::isToChange).forEach(a->{
            RuleType rule = getRuleUsingRuleID(rules.getRule(),BigInteger.valueOf(a.getRuleId()));
            rule.setPriority(a.getUpdatedRule().getPriority());
            rule.setAction(a.getUpdatedRule().getAction());
            rule.setIPdst(a.getUpdatedRule().getIPdst());
            rule.setIPsrc(a.getUpdatedRule().getIPsrc());
            rule.setPdst(a.getUpdatedRule().getPdst());
            rule.setProtocol(a.getUpdatedRule().getProtocol());
            rule.setPsrc(a.getUpdatedRule().getPsrc());
            rule.setRuleID(a.getUpdatedRule().getRuleID());
        });

    }

    private RuleType getRuleUsingRuleID(List<RuleType> listOfRules, BigInteger ruleID) {
        RuleType r = new RuleType();
        for (RuleType rule : listOfRules) {
            if (rule.getRuleID().equals(ruleID))
                r = rule;
        }
        return r;
    }

    private AnomalyType getAnomalyUsingAnomalyID(List<AnomalyType> listOfAnomalies, BigInteger anomalyId) {
        AnomalyType a = new AnomalyType();
        for (AnomalyType anomaly : listOfAnomalies) {
            if (anomaly.getAnomalyID().equals(anomalyId))
                a = anomaly;
        }
        return a;
    }

    public static void main(String[] args) {

        ConflictResolver conflictResolver = new ConflictResolver(DataCreator.createRules(), DataCreator.createAnomalies());
        conflictResolver.resolveAnomalies();
        conflictResolver.getConflictAnomalies();
    }

}
