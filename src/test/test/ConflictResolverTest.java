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
import java.io.File;
import java.math.BigInteger;

import static org.junit.Assert.*;

public class ConflictResolverTest {
    /**
     * Check that getRules() correctly get rules
     */
    @Test
    public void testGetRules() {
        ConflictResolver conflictResolver = new ConflictResolver(DataGenerator.createRules(), DataGenerator.createAnomalies());
        assertNotNull("The created conflictResolver is null", conflictResolver);
        assertEquals("The two rules lists are different", DataGenerator.createRules().getRule(), conflictResolver.getRules().getRule());
    }

    /**
     * Check that getAnomalies() correctly get anomalies
     */
    @Test
    public void testGetAnomalies() {
        ConflictResolver conflictResolver = new ConflictResolver(DataGenerator.createRules(), DataGenerator.createAnomalies());
        assertNotNull("The created conflictResolver is null", conflictResolver);
        assertEquals("The two anomalies lists are different", DataGenerator.createAnomalies().getAnomaly(), conflictResolver.getAnomalies().getAnomaly());
    }

    /**
     * Check that resolveAnomalies() correctly removes all sub-optimization anomalies and the rules involved
     */
    @Test
    public void testResolveAnomalies() {
        ConflictResolver conflictResolver = new ConflictResolver(DataGenerator.createRules(), DataGenerator.createAnomalies());
        assertNotNull("The created conflictResolver is null", conflictResolver);
        //Remove all the anomalies and the rules that needs to be removed
        RemovedEntries removedEntries = conflictResolver.resolveAnomalies();
        //Tests based on the rules and anomalies
        assertEquals(3, removedEntries.getRemovedRules().size());
        assertEquals(8, removedEntries.getRemovedAnomalies().size());

        //Creating fake data to test that the function is correctly removing rules and anomalies
        //Creating a rules list
        Rules rules = new Rules();
        //Create a rule to add inside the list
        RuleType ruleType = new RuleType();
        //Create anomalies list
        Anomalies anomalies = new Anomalies();
        //Create anomaly to be added in the list
        AnomalyType anomalyType = new AnomalyType();
        //Updating the rule information
        ruleType.setRuleID(BigInteger.valueOf(25));
        ruleType.setPriority(BigInteger.valueOf(25));
        ruleType.setIPsrc("10.10.10.*");
        ruleType.setPsrc("*");
        ruleType.setIPdst("10.10.10.*");
        ruleType.setPdst("*");
        ruleType.setProtocol("*");
        ruleType.setAction("DENY");
        //adding rule inside the list
        rules.getRule().add(ruleType);

        //updating anomaly info
        anomalyType.setAnomalyID(BigInteger.valueOf(4));
        anomalyType.setAnomalyName(AnomalyNames.SHADOWING_REDUNDANCY);
        anomalyType.getRule().add(ruleType);
        anomalyType.getRule().add(ruleType);
        anomalies.getAnomaly().add(anomalyType);

        //Create a conflict resolver from the created fake data
        ConflictResolver conflictResolver1 = new ConflictResolver(rules, anomalies);
        //Remove the anomalies and the rules that are included in the anomalies
        RemovedEntries removedEntries1 = conflictResolver1.resolveAnomalies();
        //I added only 1 anomaly and 1 rules so the removed rule and anomalies should be 1
        assertEquals("Removed Rules are different from expected", 1, removedEntries1.getRemovedRules().size());
        assertEquals("Removed Anomalies are different from expected", 1, removedEntries1.getRemovedAnomalies().size());

        assertThrows("ConflictResolver cannot be created invalid input", ForbiddenException.class, () -> new ConflictResolver(null, null));
        //The next test is used to test that an exception will be throw if we sent an empty rules or anomalies
        assertThrows("ConflictResolver cannot be created invalid input", ForbiddenException.class, () -> new ConflictResolver(rules, anomalies));


    }

    /**
     * Check that removeIrrelevance() correctly remove irrelevance anomalies
     */
    @Test
    public void testRemoveIrrelevanceAnomaly() {
        //There are 2 irrelevant rules and 6 anomalies caused by these rules inside the data provided inside the paper
        ConflictResolver conflictResolver = new ConflictResolver(DataGenerator.createRules(), DataGenerator.createAnomalies());
        assertNotNull("The created conflictResolver is null", conflictResolver);
        //remove the irrelevanceAnomalies
        RemovedEntries removedEntries = conflictResolver.removeIrrelevanceAnomaly();
        //Test based on the initial created data
        assertEquals("Removed Rules are different from expected", 2, removedEntries.getRemovedRules().size());
        assertEquals("Removed Anomalies are different from expected", 6, removedEntries.getRemovedAnomalies().size());

    }

    /**
     * Check that removeUnnecessaryAnomaly() correctly removed all the unnecessary anomalies and the involved rules
     */
    @Test
    public void testRemoveUnnecessaryAnomaly() {
        //Create a conflict resolver that contains the list of rules and anomalies in the paper
        /*The data inside the paper contain 6 anomalies and 6 involved rules by applying the recommended solution
        the 6 rules should be removed and consequently the anomalies will be removed
         */
        ConflictResolver conflictResolver = new ConflictResolver(DataGenerator.createRules(), DataGenerator.createAnomalies());
        assertNotNull("The created conflictResolver is null", conflictResolver);
        //Remove the unnecessary rules from the list of anomalies
        RemovedEntries removedEntries = conflictResolver.removeUnnecessaryAnomaly();
        //Test based on the initial created data
        assertEquals("The removed data is not as expected", 6, removedEntries.getRemovedRules().size());
        assertEquals("The removed data is not as expected", 6, removedEntries.getRemovedAnomalies().size());
    }

    /**
     * Check that the unnecessaryAnomalyChecker correctly capture the unnecessary anomalies between rules
     *
     * @throws Exception If the format of the IP address are incorrect or empty this exception is thrown
     */
    @Test

    public void testUnnecessaryAnomalyChecker() throws Exception {
        //Create conflictResolver that contains the original list of rules
        ConflictResolver conflictResolver = new ConflictResolver(DataGenerator.createRules(), DataGenerator.createAnomalies());
        assertNotNull("The created conflictResolver is null", conflictResolver);
        //Create object from the unnecessaryAnomalyChecker
        UnnecessaryAnomalyChecker unnecessaryAnomalyChecker = new UnnecessaryAnomalyChecker(conflictResolver.getRules());
        //getting the size of anomalyList to be able to get the id of the last anomaly in the list
        int anomaliesListSize = conflictResolver.getAnomalies().getAnomaly().size();
        //Perform unnecessary anomaly check
        Anomalies newAnomalies = unnecessaryAnomalyChecker.checkForUnnecessaryAnomalies(conflictResolver.getAnomalies().getAnomaly().get(anomaliesListSize - 1).getAnomalyID().intValue());
        //In the paper there is a mistake, 2 unnecessary anomalies are missing so the total number of anomalies are 8
        assertEquals("Unnecessary anomaly numbers are different than expected ", 8, newAnomalies.getAnomaly().size());
    }

    /**
     * Check that removeDuplicationOrShadowingRedundancyAnomaly() correctly removes the desired type of anomalies based on the arguments sent
     */
    @Test
    public void testRemoveDuplicationOrShadowingRedundancyAnomaly() {
        ConflictResolver conflictResolver = new ConflictResolver(DataGenerator.createRules(), DataGenerator.createAnomalies());
        assertNotNull("The created conflictResolver is null", conflictResolver);
        //Check that function does't crash when send null and it returns null
        RemovedEntries removedEntries = conflictResolver.removeDuplicationOrShadowingRedundancyAnomaly(null);
        assertNull(removedEntries);

        //Creating fake rules and anomalies
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
        //Create a new conflictResolver from the fake data
        conflictResolver = new ConflictResolver(rules, anomalies);
        //remove shadowingRedundancy anomalies and the rule creating it.
        RemovedEntries removedEntries2 = conflictResolver.removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.SHADOWING_REDUNDANCY);
        assertEquals("The returned RemovedEntries doesn't match the expected one", 1, removedEntries2.getRemovedAnomalies().size());
        assertEquals("The returned RemovedEntries doesn't match the expected one", 1, removedEntries2.getRemovedRules().size());
    }

    /**
     * Check that getConflictAnomalies() correctly return the list of conflict anomalies that needs to be solved by network admin
     */
    @Test
    public void testGetConflictAnomalies() {
        ConflictResolver conflictResolver = new ConflictResolver(DataGenerator.createRules(), DataGenerator.createAnomalies());
        assertNotNull("The created conflictResolver is null", conflictResolver);
        //Getting the list of anomalies that cannot be resolved automatically
        Anomalies unresolvedAnomalies = conflictResolver.getConflictAnomalies();
        //Test based on the initial created data
        //Check the size of the returned list
        assertEquals(22, unresolvedAnomalies.getAnomaly().size());

        ObjectFactory objectFactory = new ObjectFactory();
        //Create JAXBElement<Anomalies> from the unresolvedAnomalies list to use it in marshalling data to file
        JAXBElement<Anomalies> unresolved_anomalies = objectFactory.createAnomalies(unresolvedAnomalies);
        assertNotNull("The JAXBElement is null", unresolved_anomalies);
        //Marshall the list of anomalies to xml file
        //This is just to test that the marshaller works correctly and the file is generated
        DataMarshaller.marshalData(unresolved_anomalies, "ofar.generated.classes.conflicts", "xsd/resulted_anomalies.xml", "xsd/conflict_schema.xsd");
        File generatedFile = new File("xsd/resulted_anomalies.xml");
        assertTrue("File doesn't Exist", generatedFile.exists());

    }

    /**
     * Check that executeSolveRequest is working properly and the solve request is applied correctly
     */
    @Test
    public void testExecuteSolveRequest() {
        ConflictResolver conflictResolver = new ConflictResolver(DataGenerator.createRules(), DataGenerator.createAnomalies());
        assertNotNull("The created conflictResolver is null", conflictResolver);
        SolveRequest solveRequest = null;
        RemovedEntries removedEntries;
        try {
            //Using data unmarshaller to read solve_request.xml written by the network administrator
            solveRequest = DataUnmarshaller.unmarshallSolveRequest("xsd/solve_request.xml", "xsd/solve_request.xsd", "ofar.generated.classes.solveRequest");
        } catch (SAXException | JAXBException e) {
            e.printStackTrace();
        }
        assertNotNull("solve_request.xml is not unmarshalled correctly. solveRequest is null", solveRequest);

        //Adding solution for contradiction solution to increase test coverage
        //Testing corner cases when trying to solve non existing anomalies with non existing rules
        ContradictionSolutionType contradictionSolutionType = new ContradictionSolutionType();
        contradictionSolutionType.setAnomalyId(36);
        contradictionSolutionType.setRuleId(200);
        contradictionSolutionType.setToRemove(false);
        assertNotNull("The created ContradictionSolution is null", contradictionSolutionType);
        ContradictionSolutionType contradictionSolutionType1 = new ContradictionSolutionType();
        contradictionSolutionType1.setAnomalyId(37);
        contradictionSolutionType1.setRuleId(200);
        contradictionSolutionType1.setToRemove(true);
        assertNotNull("The created Contradiction solution is null", contradictionSolutionType1);
        //adding the created solutions
        solveRequest.getContradictionSolutions().add(contradictionSolutionType);
        solveRequest.getContradictionSolutions().add(contradictionSolutionType1);

        //Check that the a rule is correctly re-written and updated
        //Rule re-writing is only possible for correlation anomaly solution
        CorrelationSolutionType correlationSolutionType = new CorrelationSolutionType();

        //creating rule
        RuleType ruleType = new RuleType();
        ruleType.setRuleID(BigInteger.valueOf(25));
        ruleType.setPriority(BigInteger.valueOf(25));
        ruleType.setIPsrc("10.10.10.*");
        ruleType.setPsrc("*");
        ruleType.setIPdst("10.10.10.*");
        ruleType.setPdst("*");
        ruleType.setProtocol("*");
        ruleType.setAction("DENY");
        assertNotNull("The created rule is null", ruleType);

        //updating the solution fields
        correlationSolutionType.setAnomalyId(BigInteger.valueOf(20));
        correlationSolutionType.setRuleId(BigInteger.valueOf(23));
        correlationSolutionType.setUpdatedRule(ruleType);
        correlationSolutionType.setToChange(true);

        //adding the solution to the list of solutions
        solveRequest.getCorrelationSolutions().add(correlationSolutionType);
        removedEntries = conflictResolver.executeSolveRequest(solveRequest);

        //The number of rules that should be removed in the paper is 13
        assertEquals(13, removedEntries.getRemovedAnomalies().size());
        SolveRequest newSolveRequest = new SolveRequest();
        //Check that could doesn't crash by sending empty solveRequest
        removedEntries = conflictResolver.executeSolveRequest(newSolveRequest);
        assertNull(removedEntries);
    }

    /**
     * Check that toString method is working correctly
     */
    @Test
    public void testAnomalyTypeToString() {
        //Creating anomaly
        final AnomalyType anomalyType = new AnomalyType();
        //creating rule
        final RuleType ruleType = new RuleType();
        assertNotNull("The created anomaly is null", anomalyType);
        assertNotNull("The created rule is null", ruleType);
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
        //Creating Expected String for the anomaly
        final String expectedString = "Anomaly 4\nType SHADOWING_REDUNDANCY\nRules Included [" +
                ruleType +
                ", " +
                ruleType + "]";
        assertEquals(expectedString, anomalyType.toString());
    }
}