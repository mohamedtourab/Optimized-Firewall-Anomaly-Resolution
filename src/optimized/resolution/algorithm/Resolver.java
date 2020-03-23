package optimized.resolution.algorithm;

import ofar.generated.classes.conflicts.Anomalies;
import ofar.generated.classes.conflicts.AnomalyNames;
import ofar.generated.classes.solveRequest.SolveRequest;

public interface Resolver {
    /**
     * This Method doesn't return anything it performs the first 3 anomaly types i.e {Irrelevant,Duplicate,Shadowing Redundancy}
     * This method uses the other two methods removeIrrelevanceAnomaly(),removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames anomalyName)
     * so If you used this function you don't have to use the two previously mentioned methods
     */
    void resolveAnomalies();

    /**
     * This function removes the irrelevant rules and the anomalies caused by these rules
     * This function doesn't receive any arguments as the need arguments are provided in the constructor of the main class(conflictResolver)
     * @return an object of type RemovedEntries which contains 2 sets i.e {set<RuleType>removedRules,set<AnomalyType>removedAnomalies}
     * which contains all the anomalies and rules removed by this function.
     *
     */
    RemovedEntries removeIrrelevanceAnomaly();

    /**
     * This function removes the Duplicated rules and the anomalies caused by these rules or rules causing shadowing redundancy anomaly
     * @param anomalyName this parameter is used to specify which operation you want to do either(AnomalyNames.DUPLICATION or
     *                    AnomalyNames.SHADOWING_REDUNDANCY)
     * @return an object of type RemovedEntries which contains 2 sets i.e {set<RuleType>removedRules,set<AnomalyType>removedAnomalies}
     * which contains all the anomalies and rules removed by this function.
     *
     */
    RemovedEntries removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames anomalyName);

    /**
     * This function return all the anomalies of the following types (AnomalyNames.CONTRADICTION,AnomalyNames.SHADOWING_CONFLICT,
     * AnomalyNames.CORRELATION) in a list so that the administrator can check this list and reply with a possible solution
     * for these anomalies
     * @return an object of type Anomalies which contains a List of all the conflict anomalies in the rules provided
     */
    Anomalies getConflictAnomalies();

    /**
     * This function solve the conflict anomalies by performing the commands sent by the network administrator by filling xml
     * file that satisfy the schemas defined in the folder XSD.
     * @param solveRequest Contain three list each on of them contains the solution corresponding to the three conflict
     *                    anomaly types.
     * @return an object of type RemovedEntries which contains 2 sets i.e {set<RuleType>removedRules,set<AnomalyType>removedAnomalies}
     * which contains all the anomalies and rules removed by this function.
     */
    RemovedEntries executeSolveRequest(SolveRequest solveRequest);

    }
