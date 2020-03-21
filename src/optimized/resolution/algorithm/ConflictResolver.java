package optimized.resolution.algorithm;

import ofar.generated.classes.*;

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

    public static void main(String[] args) {

        ConflictResolver conflictResolver = new ConflictResolver(DataCreator.createRules(), DataCreator.createAnomalies());
        conflictResolver.resolveAnomalies();
    }

}
