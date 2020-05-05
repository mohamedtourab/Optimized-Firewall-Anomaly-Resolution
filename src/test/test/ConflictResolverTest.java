package test;

import data.serializer.DataMarshaller;
import data.serializer.DataUnmarshaller;
import ofar.generated.classes.conflicts.Anomalies;
import ofar.generated.classes.conflicts.AnomalyNames;
import ofar.generated.classes.conflicts.AnomalyType;
import ofar.generated.classes.conflicts.ObjectFactory;
import ofar.generated.classes.contradiction.ContradictionSolutionType;
import ofar.generated.classes.correlation.CorrelationSolutionType;
import ofar.generated.classes.rules.RuleType;
import ofar.generated.classes.rules.Rules;
import ofar.generated.classes.solveRequest.SolveRequest;
import optimized.resolution.algorithm.classes.ConflictResolver;
import optimized.resolution.algorithm.classes.DataGenerator;
import optimized.resolution.algorithm.classes.RemovedEntries;
import optimized.resolution.algorithm.classes.UnnecessaryAnomalyChecker;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.ws.rs.ForbiddenException;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.math.BigInteger;

import static org.junit.Assert.*;

public class ConflictResolverTest {

    @Test
    public void testGetRules() {
        ConflictResolver conflictResolver = new ConflictResolver(DataGenerator.createRules(), DataGenerator.createAnomalies());
        assertEquals(DataGenerator.createRules().getRule(), conflictResolver.getRules().getRule());
    }

    @Test
    public void testGetAnomalies() {
        ConflictResolver conflictResolver = new ConflictResolver(DataGenerator.createRules(), DataGenerator.createAnomalies());
        assertEquals(DataGenerator.createAnomalies().getAnomaly(), conflictResolver.getAnomalies().getAnomaly());
    }


    @Test
    public void testResolveAnomalies() {
        ConflictResolver conflictResolver = new ConflictResolver(DataGenerator.createRules(), DataGenerator.createAnomalies());
        RemovedEntries removedEntries = conflictResolver.resolveAnomalies();
        //Test based on the initial created data
        assertEquals(3, removedEntries.getRemovedRules().size());
        assertEquals(8, removedEntries.getRemovedAnomalies().size());

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

        assertThrows(ForbiddenException.class, () -> new ConflictResolver(null, null));
        //The next test's used to test that an exception will be throw if we sent an empty rules or anomalies
        assertThrows(ForbiddenException.class, () -> new ConflictResolver(rules, anomalies));


    }

    @Test
    public void testRemoveIrrelevanceAnomaly() {
        ConflictResolver conflictResolver = new ConflictResolver(DataGenerator.createRules(), DataGenerator.createAnomalies());
        RemovedEntries removedEntries = conflictResolver.removeIrrelevanceAnomaly();
        //Test based on the initial created data
        assertEquals(2, removedEntries.getRemovedRules().size());
        assertEquals(6, removedEntries.getRemovedAnomalies().size());

    }

    @Test
    public void testRemoveUnnecessaryAnomaly() {
        ConflictResolver conflictResolver = new ConflictResolver(DataGenerator.createRules(), DataGenerator.createAnomalies());
        RemovedEntries removedEntries = conflictResolver.removeUnnecessaryAnomaly();
        //Test based on the initial created data
        assertEquals(6, removedEntries.getRemovedRules().size());
        assertEquals(6, removedEntries.getRemovedAnomalies().size());
    }

    @Test
    public void testUnnecessaryAnomalyChecker() throws Exception {
        ConflictResolver conflictResolver = new ConflictResolver(DataGenerator.createRules(), DataGenerator.createAnomalies());
        UnnecessaryAnomalyChecker unnecessaryAnomalyChecker = new UnnecessaryAnomalyChecker(conflictResolver.getRules());
        int anomaliesListSize = conflictResolver.getAnomalies().getAnomaly().size();
        unnecessaryAnomalyChecker.checkForUnnecessaryAnomalies(conflictResolver.getAnomalies().getAnomaly().get(anomaliesListSize - 1).getAnomalyID().intValue());
    }

    @Test
    public void testRemoveDuplicationOrShadowingRedundancyAnomaly() {
        ConflictResolver conflictResolver = new ConflictResolver(DataGenerator.createRules(), DataGenerator.createAnomalies());
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
        ConflictResolver conflictResolver = new ConflictResolver(DataGenerator.createRules(), DataGenerator.createAnomalies());
        Anomalies unresolvedAnomalies = conflictResolver.getConflictAnomalies();
        ObjectFactory objectFactory = new ObjectFactory();
        JAXBElement<Anomalies> unresolved_anomalies = objectFactory.createAnomalies(unresolvedAnomalies);
        DataMarshaller.marshalData(unresolved_anomalies, "ofar.generated.classes.conflicts", "xsd/resulted_anomalies.xml", "xsd/conflict_schema.xsd");
        //Test based on the initial created data
        assertEquals(22, unresolvedAnomalies.getAnomaly().size());
    }

    @Test
    public void testExecuteSolveRequest() {
        ConflictResolver conflictResolver = new ConflictResolver(DataGenerator.createRules(), DataGenerator.createAnomalies());
        SolveRequest solveRequest = null;
        RemovedEntries removedEntries = conflictResolver.executeSolveRequest(solveRequest);
        try {
            solveRequest = DataUnmarshaller.unmarshallData("xsd/solve_request.xml", "xsd/solve_request.xsd", "ofar.generated.classes.solveRequest");
        } catch (SAXException | JAXBException e) {
            e.printStackTrace();
        }
        ContradictionSolutionType contradictionSolutionType = new ContradictionSolutionType();
        contradictionSolutionType.setAnomalyId(36);
        contradictionSolutionType.setRuleId(200);
        contradictionSolutionType.setToRemove(false);
        ContradictionSolutionType contradictionSolutionType1 = new ContradictionSolutionType();
        contradictionSolutionType1.setAnomalyId(37);
        contradictionSolutionType1.setRuleId(200);
        contradictionSolutionType1.setToRemove(true);
        solveRequest.getContradictionSolutions().add(contradictionSolutionType);
        solveRequest.getContradictionSolutions().add(contradictionSolutionType1);
        CorrelationSolutionType correlationSolutionType = new CorrelationSolutionType();
        RuleType ruleType = new RuleType();
        ruleType.setRuleID(BigInteger.valueOf(25));
        ruleType.setPriority(BigInteger.valueOf(25));
        ruleType.setIPsrc("10.10.10.*");
        ruleType.setPsrc("*");
        ruleType.setIPdst("10.10.10.*");
        ruleType.setPdst("*");
        ruleType.setProtocol("*");
        ruleType.setAction("DENY");

        correlationSolutionType.setAnomalyId(20);
        correlationSolutionType.setRuleId(23);
        correlationSolutionType.setUpdatedRule(ruleType);
        correlationSolutionType.setToChange(true);
        solveRequest.getCorrelationSolutions().add(correlationSolutionType);
        removedEntries = conflictResolver.executeSolveRequest(solveRequest);
        assertEquals(13, removedEntries.getRemovedAnomalies().size());
        SolveRequest newSolveRequest = new SolveRequest();
        removedEntries = conflictResolver.executeSolveRequest(newSolveRequest);
        assertNull(removedEntries);
    }

    @Test
    public void testAnomalyTypeToString() {
        final AnomalyType anomalyType = new AnomalyType();
        final RuleType ruleType = new RuleType();
        ruleType.setRuleID(BigInteger.valueOf(25));
        ruleType.setPriority(BigInteger.valueOf(25));
        ruleType.setIPsrc("10.10.10.*");
        ruleType.setPsrc("*");
        ruleType.setIPdst("10.10.10.*");
        ruleType.setPdst("*");
        ruleType.setProtocol("*");
        ruleType.setAction("DENY");
        anomalyType.setAnomalyID(BigInteger.valueOf(4));
        anomalyType.setAnomalyName(AnomalyNames.SHADOWING_REDUNDANCY);
        anomalyType.getRule().add(ruleType);
        anomalyType.getRule().add(ruleType);

        final String expectedString = "Anomaly 4\nType SHADOWING_REDUNDANCY\nRules Included [" + ruleType + ", " + ruleType + "]";
        assertEquals(expectedString, anomalyType.toString());
    }
}