package test;

import ofar.generated.classes.conflicts.Anomalies;
import ofar.generated.classes.conflicts.AnomalyNames;
import ofar.generated.classes.conflicts.AnomalyType;
import ofar.generated.classes.rules.RuleType;
import ofar.generated.classes.rules.Rules;
import optimized.resolution.algorithm.classes.ConflictResolver;
import optimized.resolution.algorithm.classes.DataCreator;
import optimized.resolution.algorithm.classes.RemovedEntries;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.*;

public class ConflictResolverTest {

    @Test
    public void testGetRules() {
        ConflictResolver conflictResolver = new ConflictResolver(DataCreator.createRules(), DataCreator.createAnomalies());
        assertEquals(DataCreator.createRules().getRule(), conflictResolver.getRules().getRule());
    }

    @Test
    public void testGetAnomalies() {
        ConflictResolver conflictResolver = new ConflictResolver(DataCreator.createRules(), DataCreator.createAnomalies());
        assertEquals(DataCreator.createAnomalies().getAnomaly(), conflictResolver.getAnomalies().getAnomaly());
    }


    @Test
    public void testResolveAnomalies() {
        ConflictResolver conflictResolver = new ConflictResolver(DataCreator.createRules(), DataCreator.createAnomalies());
        RemovedEntries removedEntries = conflictResolver.resolveAnomalies();
        //Test based on the initial created data
        assertEquals(10, removedEntries.getRemovedRules().size());
        assertEquals(15, removedEntries.getRemovedAnomalies().size());

        //Creating fake data
        Rules rules = new Rules();
        RuleType ruleType = new RuleType();
        Anomalies anomalies = new Anomalies();
        AnomalyType anomalyType = new AnomalyType();

        ruleType.setRuleID(BigInteger.valueOf(25));
        ruleType.setPriority(BigInteger.valueOf(25));
        ruleType.setIPsrc("10.10.10.*");
        ruleType.setPsrc("*");
        ruleType.setIPdst("10.10.10.*");
        ruleType.setPdst("*");
        ruleType.setProtocol("*");
        ruleType.setAction("DENY");
        rules.getRule().add(ruleType);

        anomalyType.setAnomalyID(BigInteger.valueOf(4));
        anomalyType.setAnomalyName(AnomalyNames.SHADOWING_REDUNDANCY);
        anomalyType.getRule().add(ruleType);
        anomalyType.getRule().add(ruleType);
        anomalies.getAnomaly().add(anomalyType);


        ConflictResolver conflictResolver1 = new ConflictResolver(rules, anomalies);
        RemovedEntries removedEntries1 = conflictResolver1.resolveAnomalies();
        assertEquals(1, removedEntries1.getRemovedRules().size());
        assertEquals(1, removedEntries1.getRemovedAnomalies().size());

        assertThrows(IllegalArgumentException.class, () -> new ConflictResolver(null, null));
        //The next test's used to test that an exception will be throw if we sent an empty rules or anomalies
        assertThrows(IllegalArgumentException.class, () -> new ConflictResolver(rules, anomalies));


    }

    @Test
    public void testRemoveIrrelevanceAnomaly() {
        ConflictResolver conflictResolver = new ConflictResolver(DataCreator.createRules(), DataCreator.createAnomalies());
        RemovedEntries removedEntries = conflictResolver.removeIrrelevanceAnomaly();
        //Test based on the initial created data
        assertEquals(2, removedEntries.getRemovedRules().size());
        assertEquals(6, removedEntries.getRemovedAnomalies().size());

    }

    @Test
    public void testRemoveUnnecessaryAnomaly() {
        ConflictResolver conflictResolver = new ConflictResolver(DataCreator.createRules(), DataCreator.createAnomalies());
        RemovedEntries removedEntries = conflictResolver.removeUnnecessaryAnomaly();
        //Test based on the initial created data
        assertEquals(6, removedEntries.getRemovedRules().size());
        assertEquals(6, removedEntries.getRemovedAnomalies().size());
    }

    @Test
    public void testRemoveDuplicationOrShadowingRedundancyAnomaly() {
        ConflictResolver conflictResolver = new ConflictResolver(DataCreator.createRules(), DataCreator.createAnomalies());
        RemovedEntries removedEntries = conflictResolver.removeDuplicationOrShadowingRedundancyAnomaly(null);
        assertNull(removedEntries);

        //Creating fake data
        Rules rules = new Rules();
        RuleType ruleType = new RuleType();
        RuleType ruleType1 = new RuleType();

        Anomalies anomalies = new Anomalies();
        AnomalyType anomalyType = new AnomalyType();

        ruleType.setRuleID(BigInteger.valueOf(25));
        ruleType.setPriority(BigInteger.valueOf(25));
        ruleType.setIPsrc("10.10.10.*");
        ruleType.setPsrc("*");
        ruleType.setIPdst("10.10.10.*");
        ruleType.setPdst("*");
        ruleType.setProtocol("*");
        ruleType.setAction("DENY");
        rules.getRule().add(ruleType);

        ruleType1.setRuleID(BigInteger.valueOf(26));
        ruleType1.setPriority(BigInteger.valueOf(28));
        ruleType1.setIPsrc("10.10.10.*");
        ruleType1.setPsrc("*");
        ruleType1.setIPdst("10.10.10.*");
        ruleType1.setPdst("*");
        ruleType1.setProtocol("*");
        ruleType1.setAction("DENY");
        rules.getRule().add(ruleType1);

        anomalyType.setAnomalyID(BigInteger.valueOf(4));
        anomalyType.setAnomalyName(AnomalyNames.SHADOWING_REDUNDANCY);
        anomalyType.getRule().add(ruleType1);
        anomalyType.getRule().add(ruleType);
        anomalies.getAnomaly().add(anomalyType);
        conflictResolver = new ConflictResolver(rules, anomalies);
        RemovedEntries removedEntries2 = conflictResolver.removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.SHADOWING_REDUNDANCY);
        assertEquals(1, removedEntries2.getRemovedAnomalies().size());
        assertEquals(1, removedEntries2.getRemovedRules().size());
    }

    @Test
    public void testGetConflictAnomalies() {
        ConflictResolver conflictResolver = new ConflictResolver(DataCreator.createRules(), DataCreator.createAnomalies());
        Anomalies unresolvedAnomalies = conflictResolver.getConflictAnomalies();
        //Test based on the initial created data
        assertEquals(22, unresolvedAnomalies.getAnomaly().size());
    }

    @Test
    public void testExecuteSolveRequest() {
        //TODO create data unmarshaller to create solveRequest Object
        //TODO create validator to validate the data with respect to the schema

    }

}