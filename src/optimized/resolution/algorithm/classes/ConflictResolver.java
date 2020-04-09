package optimized.resolution.algorithm.classes;


import ofar.generated.classes.conflicts.Anomalies;
import ofar.generated.classes.conflicts.AnomalyNames;
import ofar.generated.classes.conflicts.AnomalyType;
import ofar.generated.classes.contradiction.ContradictionSolutionType;
import ofar.generated.classes.correlation.CorrelationSolutionType;
import ofar.generated.classes.rules.RuleType;
import ofar.generated.classes.rules.Rules;
import ofar.generated.classes.shadowingConflict.ShadowingConflictSolutionType;
import ofar.generated.classes.solveRequest.SolveRequest;
import optimized.resolution.algorithm.interfaces.Resolver;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ConflictResolver implements Resolver {
    private Rules rules;
    private Anomalies anomalies;

    public Rules getRules() {
        return rules;
    }

    public Anomalies getAnomalies() {
        return anomalies;
    }

    public ConflictResolver(Rules rules, Anomalies anomalies) {
        if (rules == null || anomalies == null || rules.getRule().size() == 0 || anomalies.getAnomaly().size() == 0) {
            throw new IllegalArgumentException();
        }
        this.anomalies = anomalies;
        this.rules = rules;
    }

    @Override
    public RemovedEntries resolveAnomalies() {
        final Rules removedRules = new Rules();
        final Anomalies removedAnomalies = new Anomalies();
        final RemovedEntries irrelevanceRemovedEntries = removeIrrelevanceAnomaly();
        final RemovedEntries duplicateRemovedEntries = removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.DUPLICATION);
        final RemovedEntries shadowingRedundancyRemovedEntries = removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.SHADOWING_REDUNDANCY);
        final RemovedEntries unnecessaryRemovedEntries = removeUnnecessaryAnomaly();

        removedRules.getRule().addAll(Stream
                .of(irrelevanceRemovedEntries.getRemovedRules(), duplicateRemovedEntries.getRemovedRules(), shadowingRedundancyRemovedEntries.getRemovedRules(), unnecessaryRemovedEntries.getRemovedRules())
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
        removedAnomalies.getAnomaly().addAll(Stream
                .of(irrelevanceRemovedEntries.getRemovedAnomalies(), duplicateRemovedEntries.getRemovedAnomalies(), shadowingRedundancyRemovedEntries.getRemovedAnomalies(), unnecessaryRemovedEntries.getRemovedAnomalies())
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
        return new RemovedEntries(new HashSet<RuleType>(removedRules.getRule()), new HashSet<AnomalyType>(removedAnomalies.getAnomaly()));
    }

    @Override
    public RemovedEntries removeIrrelevanceAnomaly() {
        final Set<RuleType> removedRules = new HashSet<>();
        final Set<AnomalyType> removedAnomalies = new HashSet<>();
        //Remove Irrelevance Rules
        for (AnomalyType anomaly : anomalies.getAnomaly()) {
            if (anomaly.getAnomalyName().equals(AnomalyNames.IRRELEVANCE)) {
                removedRules.add(anomaly.getRule().get(0));
            }
        }
        //Remove rule and anomalies caused by the removed rules
        return getRemovedEntries(removedRules, removedAnomalies);
    }

    @Override
    public RemovedEntries removeUnnecessaryAnomaly() {
        final HashSet<AnomalyType> localAnomaliesToBeRemoved = new HashSet<>();

        final HashSet<RuleType> localRulesToBeRemoved = anomalies.getAnomaly().stream().filter(s -> s.getAnomalyName().equals(AnomalyNames.UNNECESSARY)).map(anomalyType -> {
            if (anomalyType.getRule().size() == 2) {
                RuleType rule1 = anomalyType.getRule().get(0);
                RuleType rule2 = anomalyType.getRule().get(1);
                localAnomaliesToBeRemoved.add(anomalyType);
                if (rule1.getPriority().compareTo(rule2.getPriority()) < 0) {
                    return rule1;
                } else {
                    return rule2;
                }
            }
            return null;
        }).collect(Collectors.toCollection(HashSet::new));

        //Update the original list of rules
        localRulesToBeRemoved.forEach(rule -> rules.getRule().remove(rule));
        //update the original list of anomalies
        localAnomaliesToBeRemoved.forEach(anomaly -> anomalies.getAnomaly().remove(anomaly));

        return new RemovedEntries(localRulesToBeRemoved, localAnomaliesToBeRemoved);
    }

    @Override
    public RemovedEntries removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames anomalyName) {
        if (anomalyName == null || !(anomalyName.equals(AnomalyNames.DUPLICATION) || anomalyName.equals(AnomalyNames.SHADOWING_REDUNDANCY))) {
            return null;
        }
        final HashSet<AnomalyType> localRemovedAnomalies = new HashSet<>();
        final HashSet<RuleType> localRemovedRules = new HashSet<>();

        for (AnomalyType anomaly : anomalies.getAnomaly()) {
            if (anomaly.getAnomalyName().equals(anomalyName)) {
                if (anomaly.getRule().size() == 2) {
                    RuleType rule1 = anomaly.getRule().get(0);
                    RuleType rule2 = anomaly.getRule().get(1);
                    //Determine which rule to remove based on the priority
                    if (rule1.getPriority().compareTo(rule2.getPriority()) > 0) {
                        localRemovedRules.add(rule1);
                    } else {
                        localRemovedRules.add(rule2);
                    }
                }
            }
        }
        //Remove all anomalies caused by the removed rules
        localRemovedRules.forEach(oneRule -> anomalies.getAnomaly().forEach(oneAnomaly -> {
            if (oneAnomaly.getRule().contains(oneRule)) {
                localRemovedAnomalies.add(oneAnomaly);
            }
        }));
        localRemovedAnomalies.forEach(a -> anomalies.getAnomaly().remove(a));
        localRemovedRules.forEach(r -> rules.getRule().remove(r));
        return new RemovedEntries(localRemovedRules, localRemovedAnomalies);
    }

    @Override
    public Anomalies getConflictAnomalies() {
        Anomalies returnData = new Anomalies();
        returnData.getAnomaly().addAll(anomalies.getAnomaly().stream().filter(a -> {
            AnomalyNames anomalyName = a.getAnomalyName();
            return anomalyName.equals(AnomalyNames.CONTRADICTION) || anomalyName.equals(AnomalyNames.SHADOWING_CONFLICT) || anomalyName.equals(AnomalyNames.CORRELATION);
        }).collect(Collectors.toList()));
        return returnData;
    }

    @Override
    public RemovedEntries executeSolveRequest(SolveRequest solveRequest) {
        if (solveRequest == null || isEmpty(solveRequest)) {
            return null;
        }
        final Set<RuleType> removedRules = new HashSet<>();
        final Set<AnomalyType> removedAnomalies = new HashSet<>();

        //Solve Contradiction
        if (solveRequest.getContradictionSolutions().size() > 0) {
            removedRules.addAll(solveRequest.getContradictionSolutions().stream()
                    .filter(ContradictionSolutionType::isToRemove)
                    .map(a -> getRuleUsingRuleID(rules.getRule(), BigInteger.valueOf(a.getRuleId())))
                    .collect(Collectors.toSet()));
            //Update Removed Anomalies Collection
            removedAnomalies.addAll(solveRequest.getContradictionSolutions().stream()
                    .map(a -> getAnomalyUsingAnomalyID(anomalies.getAnomaly(), BigInteger.valueOf(a.getAnomalyId())))
                    .collect(Collectors.toSet()));
        }

        //Solve Shadowing Conflict
        if (solveRequest.getShadowingConflictSolutions().size() > 0) {
            //Perform the first solution to get the rules to be deleted
            removedRules.addAll(solveRequest.getShadowingConflictSolutions().stream()
                    .filter(ShadowingConflictSolutionType::isToRemove)
                    .map(a -> getRuleUsingRuleID(rules.getRule(), BigInteger.valueOf(a.getRuleId())))
                    .collect(Collectors.toSet()));

            //Perform the second solution to flip the order
            solveRequest.getShadowingConflictSolutions().stream().filter(ShadowingConflictSolutionType::isToChangeOrder).forEach(l -> {
                AnomalyType anomaly = getAnomalyUsingAnomalyID(anomalies.getAnomaly(), BigInteger.valueOf(l.getAnomalyId()));
                if (anomaly.getRule().size() == 2) {
                    int rule2Index = rules.getRule().indexOf(anomaly.getRule().get(0));
                    int rule1Index = rules.getRule().indexOf(anomaly.getRule().get(1));
                    BigInteger temp = anomaly.getRule().get(0).getPriority();
                    anomaly.getRule().get(0).setPriority(anomaly.getRule().get(1).getPriority());
                    anomaly.getRule().get(1).setPriority(temp);
                    Collections.swap(rules.getRule(), rule1Index, rule2Index);
                }
            });
            //Update Removed Anomalies Collection
            removedAnomalies.addAll(solveRequest.getShadowingConflictSolutions().stream()
                    .map(a -> getAnomalyUsingAnomalyID(anomalies.getAnomaly(), BigInteger.valueOf(a.getAnomalyId())))
                    .collect(Collectors.toSet()));

        }
        if (solveRequest.getCorrelationSolutions().size() > 0) {
            //Solve Correlation
            solveRequest.getCorrelationSolutions().stream().filter(CorrelationSolutionType::isToChange).forEach(a -> {
                RuleType rule = getRuleUsingRuleID(rules.getRule(), BigInteger.valueOf(a.getRuleId()));
                rule.setPriority(a.getUpdatedRule().getPriority());
                rule.setAction(a.getUpdatedRule().getAction());
                rule.setIPdst(a.getUpdatedRule().getIPdst());
                rule.setIPsrc(a.getUpdatedRule().getIPsrc());
                rule.setPdst(a.getUpdatedRule().getPdst());
                rule.setProtocol(a.getUpdatedRule().getProtocol());
                rule.setPsrc(a.getUpdatedRule().getPsrc());
                rule.setRuleID(a.getUpdatedRule().getRuleID());
            });

            //Update Removed Anomalies Collection
            removedAnomalies.addAll(solveRequest.getCorrelationSolutions().stream()
                    .map(a -> getAnomalyUsingAnomalyID(anomalies.getAnomaly(), BigInteger.valueOf(a.getAnomalyId())))
                    .collect(Collectors.toSet()));


        }

        return getRemovedEntries(removedRules, removedAnomalies);
    }

    private RemovedEntries getRemovedEntries(Set<RuleType> removedRules, Set<AnomalyType> removedAnomalies) {
        removedRules.forEach(r -> rules.getRule().remove(r));
        removedRules.forEach(oneRule -> anomalies.getAnomaly().forEach(oneAnomaly -> {
            if (oneAnomaly.getRule().contains(oneRule)) {
                removedAnomalies.add(oneAnomaly);
            }
        }));
        removedAnomalies.forEach(a -> anomalies.getAnomaly().remove(a));
        return new RemovedEntries(removedRules, removedAnomalies);
    }

    private boolean isEmpty(SolveRequest solveRequest) {
        return solveRequest.getShadowingConflictSolutions().size() == 0
                && solveRequest.getCorrelationSolutions().size() == 0
                && solveRequest.getContradictionSolutions().size() == 0;
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

    /*public static void main(String[] args) {

        ConflictResolver conflictResolver = new ConflictResolver(DataCreator.createRules(), DataCreator.createAnomalies());
        conflictResolver.resolveAnomalies();
        Anomalies anomalylist = conflictResolver.getConflictAnomalies();
        SolveRequest solveRequest = new SolveRequest();
        ShadowingConflictSolutionType svSol = new ShadowingConflictSolutionType();
        svSol.setAnomalyId(16);
        svSol.setToRemove(false);
        svSol.setToChangeOrder(true);
        solveRequest.getShadowingConflictSolutions().add(svSol);
        conflictResolver.executeSolveRequest(solveRequest);
        anomalylist = conflictResolver.getConflictAnomalies();

    }*/

}
