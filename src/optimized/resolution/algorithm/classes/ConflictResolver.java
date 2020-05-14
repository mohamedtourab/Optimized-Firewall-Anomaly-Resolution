package optimized.resolution.algorithm.classes;


import ofar.generated.classes.conflicts.Anomalies;
import ofar.generated.classes.conflicts.AnomalyNames;
import ofar.generated.classes.conflicts.AnomalyType;
import ofar.generated.classes.contradiction.ContradictionSolutionType;
import ofar.generated.classes.correlation.CorrelationSolutionType;
import ofar.generated.classes.rules.ObjectFactory;
import ofar.generated.classes.rules.RuleType;
import ofar.generated.classes.rules.Rules;
import ofar.generated.classes.shadowingConflict.ShadowingConflictSolutionType;
import ofar.generated.classes.solveRequest.SolveRequest;
import optimized.resolution.algorithm.interfaces.Resolver;

import javax.ws.rs.ForbiddenException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class ConflictResolver implements Resolver {
    private final Rules rules;
    private final Anomalies anomalies;

    public Rules getRules() {
        return rules;
    }

    public Anomalies getAnomalies() {
        return anomalies;
    }

    public ConflictResolver(Rules rules, Anomalies anomalies) {
        if (rules == null || anomalies == null || rules.getRule().size() == 0 || anomalies.getAnomaly().size() == 0) {
            throw new ForbiddenException("Invalid Input");
        }
        this.anomalies = anomalies;
        this.rules = rules;
    }

    @Override
    public RemovedEntries resolveAnomalies() {
        ObjectFactory rulesObjectFactory = new ObjectFactory();
        ofar.generated.classes.conflicts.ObjectFactory conflictsObjectFactory = new ofar.generated.classes.conflicts.ObjectFactory();
        final Rules removedRules = rulesObjectFactory.createRules();
        final Anomalies removedAnomalies = conflictsObjectFactory.createAnomalies();
        final RemovedEntries irrelevanceRemovedEntries = removeIrrelevanceAnomaly();
        final RemovedEntries duplicateRemovedEntries = removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.DUPLICATION);
        final RemovedEntries shadowingRedundancyRemovedEntries = removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.SHADOWING_REDUNDANCY);

        removedRules.getRule().addAll(Stream
                .of(irrelevanceRemovedEntries.getRemovedRules(), duplicateRemovedEntries.getRemovedRules(), shadowingRedundancyRemovedEntries.getRemovedRules())
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
        removedAnomalies.getAnomaly().addAll(Stream
                .of(irrelevanceRemovedEntries.getRemovedAnomalies(), duplicateRemovedEntries.getRemovedAnomalies(), shadowingRedundancyRemovedEntries.getRemovedAnomalies())
                .flatMap(Collection::stream)
                .collect(Collectors.toList()));
        return new RemovedEntries(new HashSet<>(removedRules.getRule()), new HashSet<>(removedAnomalies.getAnomaly()));
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
        return performRemoval(removedRules, removedAnomalies);
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
        ofar.generated.classes.conflicts.ObjectFactory conflictsObjectFactory = new ofar.generated.classes.conflicts.ObjectFactory();
        Anomalies returnData = conflictsObjectFactory.createAnomalies();
        returnData.getAnomaly().addAll(anomalies.getAnomaly().stream().filter(a -> {
            AnomalyNames anomalyName = a.getAnomalyName();
            return anomalyName.equals(AnomalyNames.CONTRADICTION) || anomalyName.equals(AnomalyNames.SHADOWING_CONFLICT) || anomalyName.equals(AnomalyNames.CORRELATION);
        }).collect(Collectors.toList()));
        return returnData;
    }

    @Override
    public RemovedEntries executeSolveRequest(SolveRequest solveRequest) {
        ObjectFactory rulesObjectFactory = new ObjectFactory();
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
                    .map(a -> getRuleUsingRuleID(rules.getRule(), a.getRuleId()))
                    .collect(Collectors.toSet()));

            //Perform the second solution to flip the order
            solveRequest.getShadowingConflictSolutions().stream().filter(ShadowingConflictSolutionType::isToChangeOrder).forEach(l -> {
                AnomalyType anomaly = getAnomalyUsingAnomalyID(anomalies.getAnomaly(), l.getAnomalyId());
                if (anomaly.getRule().size() == 2) {
                    /*Due to the change that happens in the rules list we cannot use the rule directly from the anomaly
                    by using 'anomaly.getRule().get(0)' but we should get the updated rule from the rules list by using it ID*/
                    RuleType rx = getRuleUsingRuleID(rules.getRule(), anomaly.getRule().get(0).getRuleID());
                    RuleType ry = getRuleUsingRuleID(rules.getRule(), anomaly.getRule().get(1).getRuleID());
                    int rxIndex = rules.getRule().indexOf(rx);

                    //Copy Ry information because it should be deleted from the current position
                    RuleType copyOfRy = rulesObjectFactory.createRuleType();
                    copyOfRy.setPriority(ry.getPriority());
                    copyOfRy.setRuleID(ry.getRuleID());
                    copyOfRy.setAction(ry.getAction());
                    copyOfRy.setIPdst(ry.getIPdst());
                    copyOfRy.setIPsrc(ry.getIPsrc());
                    copyOfRy.setPdst(ry.getPdst());
                    copyOfRy.setPsrc(ry.getPsrc());
                    copyOfRy.setProtocol(ry.getProtocol());

                    BigInteger rxPriority = rx.getPriority();
                    List<RuleType> rulesBelowNewPositionOfRy = getAllRulesBetweenTwoRules(rx, ry);
                    //remove ry from the table
                    rules.getRule().remove(ry);
                    //Set copyOfRy priority equal to the current priority of Rx
                    copyOfRy.setPriority(rxPriority);
                    //Inset Ry just before Rx at the table
                    rules.getRule().add(rxIndex, copyOfRy);
                    //Increment Rx priority and all the rules below
                    for (RuleType singleRule : rulesBelowNewPositionOfRy) {
                        singleRule.setPriority(singleRule.getPriority().add(BigInteger.ONE));
                    }
                }
            });
            //Update Removed Anomalies Collection
            removedAnomalies.addAll(solveRequest.getShadowingConflictSolutions().stream()
                    .map(a -> getAnomalyUsingAnomalyID(anomalies.getAnomaly(), a.getAnomalyId()))
                    .collect(Collectors.toSet()));

        }
        if (solveRequest.getCorrelationSolutions().size() > 0) {
            //Solve Correlation
            solveRequest.getCorrelationSolutions().stream().filter(CorrelationSolutionType::isToChange).forEach(a -> {
                RuleType rule = getRuleUsingRuleID(rules.getRule(), a.getRuleId());
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
                    .map(a -> getAnomalyUsingAnomalyID(anomalies.getAnomaly(), a.getAnomalyId()))
                    .collect(Collectors.toSet()));


        }

        return performRemoval(removedRules, removedAnomalies);
    }

    private List<RuleType> getAllRulesBetweenTwoRules(RuleType rx, RuleType ry) {
        List<RuleType> rulesList = new ArrayList<>();
        boolean startFilling = false;
        for (RuleType rule : rules.getRule()) {
            if (rule.equals(ry))
                return rulesList;
            if (startFilling)
                rulesList.add(rule);
            if (rule.equals(rx)) {
                startFilling = true;
                rulesList.add(rule);
            }
        }
        return rulesList;
    }

    private RemovedEntries performRemoval(Set<RuleType> removedRules, Set<AnomalyType> removedAnomalies) {
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
        ObjectFactory rulesObjectFactory = new ObjectFactory();
        RuleType r = rulesObjectFactory.createRuleType();
        for (RuleType rule : listOfRules) {
            if (rule.getRuleID().equals(ruleID))
                r = rule;
        }
        return r;
    }

    private AnomalyType getAnomalyUsingAnomalyID(List<AnomalyType> listOfAnomalies, BigInteger anomalyId) {
        ofar.generated.classes.conflicts.ObjectFactory conflictsObjectFactory = new ofar.generated.classes.conflicts.ObjectFactory();
        AnomalyType a = conflictsObjectFactory.createAnomalyType();
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
