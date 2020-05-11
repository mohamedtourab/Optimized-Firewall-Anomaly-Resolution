package optimized.resolution.algorithm.classes;

import ofar.generated.classes.conflicts.Anomalies;
import ofar.generated.classes.conflicts.AnomalyNames;
import ofar.generated.classes.conflicts.AnomalyType;
import ofar.generated.classes.correlation.CorrelationSolutionType;
import ofar.generated.classes.input.ServiceInput;
import ofar.generated.classes.rules.ObjectFactory;
import ofar.generated.classes.rules.RuleType;
import ofar.generated.classes.rules.Rules;
import ofar.generated.classes.shadowingConflict.ShadowingConflictSolutionType;
import ofar.generated.classes.solveRequest.SolveRequest;

import java.math.BigInteger;

public class DataGenerator {
    static RuleType ruleType1;
    static RuleType ruleType2;
    static RuleType ruleType3;
    static RuleType ruleType4;
    static RuleType ruleType5;
    static RuleType ruleType6;
    static RuleType ruleType7;
    static RuleType ruleType8;
    static RuleType ruleType9;
    static RuleType ruleType10;
    static RuleType ruleType11;
    static RuleType ruleType12;
    static RuleType ruleType13;
    static RuleType ruleType14;
    static RuleType ruleType15;
    static RuleType ruleType16;
    static RuleType ruleType17;
    static RuleType ruleType18;
    static RuleType ruleType19;
    static RuleType ruleType20;
    static RuleType ruleType21;
    static RuleType ruleType22;
    static RuleType ruleType23;

    public static Rules createRules() {
        ObjectFactory objectFactory = new ObjectFactory();
        Rules rules = objectFactory.createRules();
        //Rule 1
        ruleType1 = objectFactory.createRuleType();
        ruleType1.setRuleID(BigInteger.valueOf(1));
        ruleType1.setPriority(BigInteger.valueOf(1));
        ruleType1.setIPsrc("10.1.0.0/24");
        ruleType1.setPsrc("*");
        ruleType1.setIPdst("*");
        ruleType1.setPdst("*");
        ruleType1.setProtocol("*");
        ruleType1.setAction("DENY");
        rules.getRule().add(ruleType1);
        //Rule 2
        ruleType2 = objectFactory.createRuleType();
        ruleType2.setRuleID(BigInteger.valueOf(2));
        ruleType2.setPriority(BigInteger.valueOf(2));
        ruleType2.setIPsrc("10.1.0.0/24");
        ruleType2.setPsrc("*");
        ruleType2.setIPdst("*");
        ruleType2.setPdst("*");
        ruleType2.setProtocol("TCP");
        ruleType2.setAction("ALLOW");
        rules.getRule().add(ruleType2);
        //Rule 3
        ruleType3 = objectFactory.createRuleType();
        ruleType3.setRuleID(BigInteger.valueOf(3));
        ruleType3.setPriority(BigInteger.valueOf(3));
        ruleType3.setIPsrc("10.1.0.0/24");
        ruleType3.setPsrc("*");
        ruleType3.setIPdst("10.3.0.0/24");
        ruleType3.setPdst("*");
        ruleType3.setProtocol("TCP");
        ruleType3.setAction("DENY");
        rules.getRule().add(ruleType3);
        //Rule 4
        ruleType4 = objectFactory.createRuleType();
        ruleType4.setRuleID(BigInteger.valueOf(4));
        ruleType4.setPriority(BigInteger.valueOf(4));
        ruleType4.setIPsrc("10.1.0.0/24");
        ruleType4.setPsrc("*");
        ruleType4.setIPdst("10.3.1.0/24");
        ruleType4.setPdst("*");
        ruleType4.setProtocol("TCP");
        ruleType4.setAction("DENY");
        rules.getRule().add(ruleType4);
        //Rule 5
        ruleType5 = objectFactory.createRuleType();
        ruleType5.setRuleID(BigInteger.valueOf(5));
        ruleType5.setPriority(BigInteger.valueOf(5));
        ruleType5.setIPsrc("10.1.0.7");
        ruleType5.setPsrc("*");
        ruleType5.setIPdst("10.2.0.5");
        ruleType5.setPdst("80");
        ruleType5.setProtocol("TCP");
        ruleType5.setAction("ALLOW");
        rules.getRule().add(ruleType5);
        //Rule 6
        ruleType6 = objectFactory.createRuleType();
        ruleType6.setRuleID(BigInteger.valueOf(6));
        ruleType6.setPriority(BigInteger.valueOf(6));
        ruleType6.setIPsrc("10.2.0.5");
        ruleType6.setPsrc("80");
        ruleType6.setIPdst("10.1.0.7");
        ruleType6.setPdst("*");
        ruleType6.setProtocol("TCP");
        ruleType6.setAction("ALLOW");
        rules.getRule().add(ruleType6);
        //Rule 7
        ruleType7 = objectFactory.createRuleType();
        ruleType7.setRuleID(BigInteger.valueOf(7));
        ruleType7.setPriority(BigInteger.valueOf(7));
        ruleType7.setIPsrc("10.2.0.5");
        ruleType7.setPsrc("*");
        ruleType7.setIPdst("*");
        ruleType7.setPdst("*");
        ruleType7.setProtocol("*");
        ruleType7.setAction("DENY");
        rules.getRule().add(ruleType7);
        //Rule 8
        ruleType8 = objectFactory.createRuleType();
        ruleType8.setRuleID(BigInteger.valueOf(8));
        ruleType8.setPriority(BigInteger.valueOf(8));
        ruleType8.setIPsrc("*");
        ruleType8.setPsrc("*");
        ruleType8.setIPdst("10.2.0.5");
        ruleType8.setPdst("*");
        ruleType8.setProtocol("*");
        ruleType8.setAction("DENY");
        rules.getRule().add(ruleType8);
        //Rule 9
        ruleType9 = objectFactory.createRuleType();
        ruleType9.setRuleID(BigInteger.valueOf(9));
        ruleType9.setPriority(BigInteger.valueOf(9));
        ruleType9.setIPsrc("10.2.1.5");
        ruleType9.setPsrc("*");
        ruleType9.setIPdst("*");
        ruleType9.setPdst("*");
        ruleType9.setProtocol("*");
        ruleType9.setAction("DENY");
        rules.getRule().add(ruleType9);
        //Rule 10
        ruleType10 = objectFactory.createRuleType();
        ruleType10.setRuleID(BigInteger.valueOf(10));
        ruleType10.setPriority(BigInteger.valueOf(10));
        ruleType10.setIPsrc("*");
        ruleType10.setPsrc("*");
        ruleType10.setIPdst("10.2.1.5");
        ruleType10.setPdst("*");
        ruleType10.setProtocol("*");
        ruleType10.setAction("DENY");
        rules.getRule().add(ruleType10);
        //Rule 11
        ruleType11 = objectFactory.createRuleType();
        ruleType11.setRuleID(BigInteger.valueOf(11));
        ruleType11.setPriority(BigInteger.valueOf(11));
        ruleType11.setIPsrc("10.3.1.5");
        ruleType11.setPsrc("80");
        ruleType11.setIPdst("10.2.1.5");
        ruleType11.setPdst("8080");
        ruleType11.setProtocol("TCP");
        ruleType11.setAction("ALLOW");
        rules.getRule().add(ruleType11);
        //Rule 12
        ruleType12 = objectFactory.createRuleType();
        ruleType12.setRuleID(BigInteger.valueOf(12));
        ruleType12.setPriority(BigInteger.valueOf(12));
        ruleType12.setIPsrc("10.2.1.5");
        ruleType12.setPsrc("8080");
        ruleType12.setIPdst("10.3.1.5");
        ruleType12.setPdst("80");
        ruleType12.setProtocol("TCP");
        ruleType12.setAction("ALLOW");
        rules.getRule().add(ruleType12);
        //Rule 13
        ruleType13 = objectFactory.createRuleType();
        ruleType13.setRuleID(BigInteger.valueOf(13));
        ruleType13.setPriority(BigInteger.valueOf(13));
        ruleType13.setIPsrc("10.2.0.0/24");
        ruleType13.setPsrc("*");
        ruleType13.setIPdst("*");
        ruleType13.setPdst("*");
        ruleType13.setProtocol("*");
        ruleType13.setAction("DENY");
        rules.getRule().add(ruleType13);
        //Rule 14
        ruleType14 = objectFactory.createRuleType();
        ruleType14.setRuleID(BigInteger.valueOf(14));
        ruleType14.setPriority(BigInteger.valueOf(14));
        ruleType14.setIPsrc("*");
        ruleType14.setPsrc("*");
        ruleType14.setIPdst("10.2.0.0/24");
        ruleType14.setPdst("*");
        ruleType14.setProtocol("*");
        ruleType14.setAction("DENY");
        rules.getRule().add(ruleType14);
        //Rule 15
        ruleType15 = objectFactory.createRuleType();
        ruleType15.setRuleID(BigInteger.valueOf(15));
        ruleType15.setPriority(BigInteger.valueOf(15));
        ruleType15.setIPsrc("10.2.1.0/24");
        ruleType15.setPsrc("*");
        ruleType15.setIPdst("*");
        ruleType15.setPdst("*");
        ruleType15.setProtocol("*");
        ruleType15.setAction("DENY");
        rules.getRule().add(ruleType15);
        //Rule 16
        ruleType16 = objectFactory.createRuleType();
        ruleType16.setRuleID(BigInteger.valueOf(16));
        ruleType16.setPriority(BigInteger.valueOf(16));
        ruleType16.setIPsrc("*");
        ruleType16.setPsrc("*");
        ruleType16.setIPdst("10.2.1.0/24");
        ruleType16.setPdst("*");
        ruleType16.setProtocol("*");
        ruleType16.setAction("DENY");
        rules.getRule().add(ruleType16);
        //Rule 17
        ruleType17 = objectFactory.createRuleType();
        ruleType17.setRuleID(BigInteger.valueOf(17));
        ruleType17.setPriority(BigInteger.valueOf(17));
        ruleType17.setIPsrc("10.2.0.5");
        ruleType17.setPsrc("80");
        ruleType17.setIPdst("10.2.1.5");
        ruleType17.setPdst("8080");
        ruleType17.setProtocol("TCP");
        ruleType17.setAction("ALLOW");
        rules.getRule().add(ruleType17);
        //Rule 18
        ruleType18 = objectFactory.createRuleType();
        ruleType18.setRuleID(BigInteger.valueOf(18));
        ruleType18.setPriority(BigInteger.valueOf(18));
        ruleType18.setIPsrc("10.2.1.5");
        ruleType18.setPsrc("8080");
        ruleType18.setIPdst("10.2.0.5");
        ruleType18.setPdst("80");
        ruleType18.setProtocol("TCP");
        ruleType18.setAction("ALLOW");
        rules.getRule().add(ruleType18);
        //Rule 19
        ruleType19 = objectFactory.createRuleType();
        ruleType19.setRuleID(BigInteger.valueOf(19));
        ruleType19.setPriority(BigInteger.valueOf(19));
        ruleType19.setIPsrc("10.2.0.0/24");
        ruleType19.setPsrc("*");
        ruleType19.setIPdst("10.2.1.0/24");
        ruleType19.setPdst("*");
        ruleType19.setProtocol("*");
        ruleType19.setAction("ALLOW");
        rules.getRule().add(ruleType19);
        //Rule 20
        ruleType20 = objectFactory.createRuleType();
        ruleType20.setRuleID(BigInteger.valueOf(20));
        ruleType20.setPriority(BigInteger.valueOf(20));
        ruleType20.setIPsrc("10.2.1.0/24");
        ruleType20.setPsrc("*");
        ruleType20.setIPdst("10.2.0.0/24");
        ruleType20.setPdst("*");
        ruleType20.setProtocol("*");
        ruleType20.setAction("ALLOW");
        rules.getRule().add(ruleType20);
        //Rule 21
        ruleType21 = objectFactory.createRuleType();
        ruleType21.setRuleID(BigInteger.valueOf(21));
        ruleType21.setPriority(BigInteger.valueOf(21));
        ruleType21.setIPsrc("10.3.0.0/16");
        ruleType21.setPsrc("*");
        ruleType21.setIPdst("10.2.0.0/16");
        ruleType21.setPdst("*");
        ruleType21.setProtocol("*");
        ruleType21.setAction("DENY");
        rules.getRule().add(ruleType21);
        //Rule 22
        ruleType22 = objectFactory.createRuleType();
        ruleType22.setRuleID(BigInteger.valueOf(22));
        ruleType22.setPriority(BigInteger.valueOf(22));
        ruleType22.setIPsrc("10.2.0.0/16");
        ruleType22.setPsrc("*");
        ruleType22.setIPdst("10.3.0.0/16");
        ruleType22.setPdst("*");
        ruleType22.setProtocol("*");
        ruleType22.setAction("DENY");
        rules.getRule().add(ruleType22);
        //default Rule
        ruleType23 = objectFactory.createRuleType();
        ruleType23.setRuleID(BigInteger.valueOf(23));
        ruleType23.setPriority(BigInteger.valueOf(Integer.MAX_VALUE));
        ruleType23.setIPsrc("*");
        ruleType23.setPsrc("*");
        ruleType23.setIPdst("*");
        ruleType23.setPdst("*");
        ruleType23.setProtocol("*");
        ruleType23.setAction("DENY");
        rules.getRule().add(ruleType23);
        return rules;
    }

    public static Anomalies createAnomalies() {
        ofar.generated.classes.conflicts.ObjectFactory  objectFactory = new ofar.generated.classes.conflicts.ObjectFactory();
        Anomalies anomalies = objectFactory.createAnomalies();
        //Anomaly 1
        AnomalyType anomaly1 = objectFactory.createAnomalyType();
        anomaly1.setAnomalyID(BigInteger.valueOf(1));
        anomaly1.setAnomalyName(AnomalyNames.IRRELEVANCE);
        anomaly1.getRule().add(ruleType3);
        anomalies.getAnomaly().add(anomaly1);
        //Anomaly 2
        AnomalyType anomaly2 = objectFactory.createAnomalyType();
        anomaly2.setAnomalyID(BigInteger.valueOf(2));
        anomaly2.setAnomalyName(AnomalyNames.IRRELEVANCE);
        anomaly2.getRule().add(ruleType4);
        anomalies.getAnomaly().add(anomaly2);
        //Anomaly 3
        AnomalyType anomaly3 = objectFactory.createAnomalyType();
        anomaly3.setAnomalyID(BigInteger.valueOf(3));
        anomaly3.setAnomalyName(AnomalyNames.SHADOWING_REDUNDANCY);
        anomaly3.getRule().add(ruleType1);
        anomaly3.getRule().add(ruleType3);
        anomalies.getAnomaly().add(anomaly3);
        //Anomaly 4
        AnomalyType anomaly4 = objectFactory.createAnomalyType();
        anomaly4.setAnomalyID(BigInteger.valueOf(4));
        anomaly4.setAnomalyName(AnomalyNames.SHADOWING_REDUNDANCY);
        anomaly4.getRule().add(ruleType1);
        anomaly4.getRule().add(ruleType4);
        anomalies.getAnomaly().add(anomaly4);
        //Anomaly 5
        AnomalyType anomaly5 = objectFactory.createAnomalyType();
        anomaly5.setAnomalyID(BigInteger.valueOf(5));
        anomaly5.setAnomalyName(AnomalyNames.SHADOWING_REDUNDANCY);
        anomaly5.getRule().add(ruleType2);
        anomaly5.getRule().add(ruleType5);
        anomalies.getAnomaly().add(anomaly5);
        //Anomaly 6
        AnomalyType anomaly6 = objectFactory.createAnomalyType();
        anomaly6.setAnomalyID(BigInteger.valueOf(6));
        anomaly6.setAnomalyName(AnomalyNames.CORRELATION);
        anomaly6.getRule().add(ruleType7);
        anomaly6.getRule().add(ruleType19);
        anomalies.getAnomaly().add(anomaly6);
        //Anomaly 7
        AnomalyType anomaly7 = objectFactory.createAnomalyType();
        anomaly7.setAnomalyID(BigInteger.valueOf(7));
        anomaly7.setAnomalyName(AnomalyNames.CORRELATION);
        anomaly7.getRule().add(ruleType8);
        anomaly7.getRule().add(ruleType20);
        anomalies.getAnomaly().add(anomaly7);
        //Anomaly 8
        AnomalyType anomaly8 = objectFactory.createAnomalyType();
        anomaly8.setAnomalyID(BigInteger.valueOf(8));
        anomaly8.setAnomalyName(AnomalyNames.CORRELATION);
        anomaly8.getRule().add(ruleType9);
        anomaly8.getRule().add(ruleType20);
        anomalies.getAnomaly().add(anomaly8);
        //Anomaly 9
        AnomalyType anomaly9 = objectFactory.createAnomalyType();
        anomaly9.setAnomalyID(BigInteger.valueOf(9));
        anomaly9.setAnomalyName(AnomalyNames.CORRELATION);
        anomaly9.getRule().add(ruleType10);
        anomaly9.getRule().add(ruleType19);
        anomalies.getAnomaly().add(anomaly9);
        //Anomaly 10
        AnomalyType anomaly10 = objectFactory.createAnomalyType();
        anomaly10.setAnomalyID(BigInteger.valueOf(10));
        anomaly10.setAnomalyName(AnomalyNames.UNNECESSARY);
        anomaly10.getRule().add(ruleType7);
        anomaly10.getRule().add(ruleType13);
        anomalies.getAnomaly().add(anomaly10);
        //Anomaly 11
        AnomalyType anomaly11 = objectFactory.createAnomalyType();
        anomaly11.setAnomalyID(BigInteger.valueOf(11));
        anomaly11.setAnomalyName(AnomalyNames.UNNECESSARY);
        anomaly11.getRule().add(ruleType8);
        anomaly11.getRule().add(ruleType14);
        anomalies.getAnomaly().add(anomaly11);
        //Anomaly 12
        AnomalyType anomaly12 = objectFactory.createAnomalyType();
        anomaly12.setAnomalyID(BigInteger.valueOf(12));
        anomaly12.setAnomalyName(AnomalyNames.UNNECESSARY);
        anomaly12.getRule().add(ruleType17);
        anomaly12.getRule().add(ruleType19);
        anomalies.getAnomaly().add(anomaly12);
        //Anomaly 13
        AnomalyType anomaly13 = objectFactory.createAnomalyType();
        anomaly13.setAnomalyID(BigInteger.valueOf(13));
        anomaly13.setAnomalyName(AnomalyNames.UNNECESSARY);
        anomaly13.getRule().add(ruleType18);
        anomaly13.getRule().add(ruleType20);
        anomalies.getAnomaly().add(anomaly13);
        //Anomaly 14
        AnomalyType anomaly14 = objectFactory.createAnomalyType();
        anomaly14.setAnomalyID(BigInteger.valueOf(14));
        anomaly14.setAnomalyName(AnomalyNames.UNNECESSARY);
        anomaly14.getRule().add(ruleType21);
        anomaly14.getRule().add(ruleType23);
        anomalies.getAnomaly().add(anomaly14);
        //Anomaly 15
        AnomalyType anomaly15 = objectFactory.createAnomalyType();
        anomaly15.setAnomalyID(BigInteger.valueOf(15));
        anomaly15.setAnomalyName(AnomalyNames.UNNECESSARY);
        anomaly15.getRule().add(ruleType22);
        anomaly15.getRule().add(ruleType23);
        anomalies.getAnomaly().add(anomaly15);
        //Anomaly 16
        AnomalyType anomaly16 = objectFactory.createAnomalyType();
        anomaly16.setAnomalyID(BigInteger.valueOf(16));
        anomaly16.setAnomalyName(AnomalyNames.SHADOWING_CONFLICT);
        anomaly16.getRule().add(ruleType1);
        anomaly16.getRule().add(ruleType2);
        anomalies.getAnomaly().add(anomaly16);
        //Anomaly 17
        AnomalyType anomaly17 = objectFactory.createAnomalyType();
        anomaly17.setAnomalyID(BigInteger.valueOf(17));
        anomaly17.setAnomalyName(AnomalyNames.SHADOWING_CONFLICT);
        anomaly17.getRule().add(ruleType1);
        anomaly17.getRule().add(ruleType5);
        anomalies.getAnomaly().add(anomaly17);
        //Anomaly 18
        AnomalyType anomaly18 = objectFactory.createAnomalyType();
        anomaly18.setAnomalyID(BigInteger.valueOf(18));
        anomaly18.setAnomalyName(AnomalyNames.SHADOWING_CONFLICT);
        anomaly18.getRule().add(ruleType2);
        anomaly18.getRule().add(ruleType3);
        anomalies.getAnomaly().add(anomaly18);
        //Anomaly 19
        AnomalyType anomaly19 = objectFactory.createAnomalyType();
        anomaly19.setAnomalyID(BigInteger.valueOf(19));
        anomaly19.setAnomalyName(AnomalyNames.SHADOWING_CONFLICT);
        anomaly19.getRule().add(ruleType2);
        anomaly19.getRule().add(ruleType4);
        anomalies.getAnomaly().add(anomaly19);
        //Anomaly 20
        AnomalyType anomaly20 = objectFactory.createAnomalyType();
        anomaly20.setAnomalyID(BigInteger.valueOf(20));
        anomaly20.setAnomalyName(AnomalyNames.SHADOWING_CONFLICT);
        anomaly20.getRule().add(ruleType7);
        anomaly20.getRule().add(ruleType17);
        anomalies.getAnomaly().add(anomaly20);
        //Anomaly 21
        AnomalyType anomaly21 = objectFactory.createAnomalyType();
        anomaly21.setAnomalyID(BigInteger.valueOf(21));
        anomaly21.setAnomalyName(AnomalyNames.SHADOWING_CONFLICT);
        anomaly21.getRule().add(ruleType8);
        anomaly21.getRule().add(ruleType18);
        anomalies.getAnomaly().add(anomaly21);
        //Anomaly 22
        AnomalyType anomaly22 = objectFactory.createAnomalyType();
        anomaly22.setAnomalyID(BigInteger.valueOf(22));
        anomaly22.setAnomalyName(AnomalyNames.SHADOWING_CONFLICT);
        anomaly22.getRule().add(ruleType9);
        anomaly22.getRule().add(ruleType12);
        anomalies.getAnomaly().add(anomaly22);
        //Anomaly 23
        AnomalyType anomaly23 = objectFactory.createAnomalyType();
        anomaly23.setAnomalyID(BigInteger.valueOf(23));
        anomaly23.setAnomalyName(AnomalyNames.SHADOWING_CONFLICT);
        anomaly23.getRule().add(ruleType9);
        anomaly23.getRule().add(ruleType18);
        anomalies.getAnomaly().add(anomaly23);
        //Anomaly 24
        AnomalyType anomaly24 = objectFactory.createAnomalyType();
        anomaly24.setAnomalyID(BigInteger.valueOf(24));
        anomaly24.setAnomalyName(AnomalyNames.SHADOWING_CONFLICT);
        anomaly24.getRule().add(ruleType10);
        anomaly24.getRule().add(ruleType11);
        anomalies.getAnomaly().add(anomaly24);
        //Anomaly 25
        AnomalyType anomaly25 = objectFactory.createAnomalyType();
        anomaly25.setAnomalyID(BigInteger.valueOf(25));
        anomaly25.setAnomalyName(AnomalyNames.SHADOWING_CONFLICT);
        anomaly25.getRule().add(ruleType10);
        anomaly25.getRule().add(ruleType17);
        anomalies.getAnomaly().add(anomaly25);
        //Anomaly 26
        AnomalyType anomaly26 = objectFactory.createAnomalyType();
        anomaly26.setAnomalyID(BigInteger.valueOf(26));
        anomaly26.setAnomalyName(AnomalyNames.SHADOWING_CONFLICT);
        anomaly26.getRule().add(ruleType13);
        anomaly26.getRule().add(ruleType17);
        anomalies.getAnomaly().add(anomaly26);
        //Anomaly 27
        AnomalyType anomaly27 = objectFactory.createAnomalyType();
        anomaly27.setAnomalyID(BigInteger.valueOf(27));
        anomaly27.setAnomalyName(AnomalyNames.SHADOWING_CONFLICT);
        anomaly27.getRule().add(ruleType13);
        anomaly27.getRule().add(ruleType19);
        anomalies.getAnomaly().add(anomaly27);
        //Anomaly 28
        AnomalyType anomaly28 = objectFactory.createAnomalyType();
        anomaly28.setAnomalyID(BigInteger.valueOf(28));
        anomaly28.setAnomalyName(AnomalyNames.SHADOWING_CONFLICT);
        anomaly28.getRule().add(ruleType14);
        anomaly28.getRule().add(ruleType18);
        anomalies.getAnomaly().add(anomaly28);
        //Anomaly 29
        AnomalyType anomaly29 = objectFactory.createAnomalyType();
        anomaly29.setAnomalyID(BigInteger.valueOf(29));
        anomaly29.setAnomalyName(AnomalyNames.SHADOWING_CONFLICT);
        anomaly29.getRule().add(ruleType14);
        anomaly29.getRule().add(ruleType20);
        anomalies.getAnomaly().add(anomaly29);
        //Anomaly 30
        AnomalyType anomaly30 = objectFactory.createAnomalyType();
        anomaly30.setAnomalyID(BigInteger.valueOf(30));
        anomaly30.setAnomalyName(AnomalyNames.SHADOWING_CONFLICT);
        anomaly30.getRule().add(ruleType15);
        anomaly30.getRule().add(ruleType18);
        anomalies.getAnomaly().add(anomaly30);
        //Anomaly 31
        AnomalyType anomaly31 = objectFactory.createAnomalyType();
        anomaly31.setAnomalyID(BigInteger.valueOf(31));
        anomaly31.setAnomalyName(AnomalyNames.SHADOWING_CONFLICT);
        anomaly31.getRule().add(ruleType15);
        anomaly31.getRule().add(ruleType20);
        anomalies.getAnomaly().add(anomaly31);
        //Anomaly 32
        AnomalyType anomaly32 = objectFactory.createAnomalyType();
        anomaly32.setAnomalyID(BigInteger.valueOf(32));
        anomaly32.setAnomalyName(AnomalyNames.SHADOWING_CONFLICT);
        anomaly32.getRule().add(ruleType16);
        anomaly32.getRule().add(ruleType17);
        anomalies.getAnomaly().add(anomaly32);
        //Anomaly 33
        AnomalyType anomaly33 = objectFactory.createAnomalyType();
        anomaly33.setAnomalyID(BigInteger.valueOf(33));
        anomaly33.setAnomalyName(AnomalyNames.SHADOWING_CONFLICT);
        anomaly33.getRule().add(ruleType16);
        anomaly33.getRule().add(ruleType19);
        anomalies.getAnomaly().add(anomaly33);
        return anomalies;
    }

    public static SolveRequest createSolveRequest() {
        SolveRequest solveRequest = new SolveRequest();
        ShadowingConflictSolutionType shadowingConflictSolutionType = new ShadowingConflictSolutionType();
        shadowingConflictSolutionType.setAnomalyId(16);
        shadowingConflictSolutionType.setToRemove(false);
        shadowingConflictSolutionType.setToChangeOrder(true);
        solveRequest.getShadowingConflictSolutions().add(shadowingConflictSolutionType);
        shadowingConflictSolutionType = new ShadowingConflictSolutionType();
        shadowingConflictSolutionType.setAnomalyId(20);
        shadowingConflictSolutionType.setToRemove(false);
        shadowingConflictSolutionType.setToChangeOrder(true);
        solveRequest.getShadowingConflictSolutions().add(shadowingConflictSolutionType);
        shadowingConflictSolutionType = new ShadowingConflictSolutionType();
        shadowingConflictSolutionType.setAnomalyId(21);
        shadowingConflictSolutionType.setToRemove(false);
        shadowingConflictSolutionType.setToChangeOrder(true);
        solveRequest.getShadowingConflictSolutions().add(shadowingConflictSolutionType);
        shadowingConflictSolutionType = new ShadowingConflictSolutionType();
        shadowingConflictSolutionType.setAnomalyId(22);
        shadowingConflictSolutionType.setToRemove(false);
        shadowingConflictSolutionType.setToChangeOrder(true);
        solveRequest.getShadowingConflictSolutions().add(shadowingConflictSolutionType);
        shadowingConflictSolutionType = new ShadowingConflictSolutionType();
        shadowingConflictSolutionType.setAnomalyId(24);
        shadowingConflictSolutionType.setToRemove(false);
        shadowingConflictSolutionType.setToChangeOrder(true);
        solveRequest.getShadowingConflictSolutions().add(shadowingConflictSolutionType);
        shadowingConflictSolutionType = new ShadowingConflictSolutionType();
        shadowingConflictSolutionType.setAnomalyId(27);
        shadowingConflictSolutionType.setToRemove(false);
        shadowingConflictSolutionType.setToChangeOrder(true);
        solveRequest.getShadowingConflictSolutions().add(shadowingConflictSolutionType);
        shadowingConflictSolutionType = new ShadowingConflictSolutionType();
        shadowingConflictSolutionType.setAnomalyId(29);
        shadowingConflictSolutionType.setToRemove(false);
        shadowingConflictSolutionType.setToChangeOrder(true);
        solveRequest.getShadowingConflictSolutions().add(shadowingConflictSolutionType);
        CorrelationSolutionType correlationSolutionType = new CorrelationSolutionType();
        correlationSolutionType.setAnomalyId(6);
        correlationSolutionType.setToChange(false);
        solveRequest.getCorrelationSolutions().add(correlationSolutionType);
        correlationSolutionType = new CorrelationSolutionType();
        correlationSolutionType.setAnomalyId(7);
        correlationSolutionType.setToChange(false);
        solveRequest.getCorrelationSolutions().add(correlationSolutionType);
        correlationSolutionType = new CorrelationSolutionType();
        correlationSolutionType.setAnomalyId(8);
        correlationSolutionType.setToChange(false);
        solveRequest.getCorrelationSolutions().add(correlationSolutionType);
        correlationSolutionType = new CorrelationSolutionType();
        correlationSolutionType.setAnomalyId(9);
        correlationSolutionType.setToChange(false);
        solveRequest.getCorrelationSolutions().add(correlationSolutionType);
        return solveRequest;
    }

    public static ServiceInput createServiceInput() {
        ServiceInput serviceInput = new ServiceInput();
        serviceInput.setDefectedRules(DataGenerator.createRules());
        serviceInput.setAnomaliesList(DataGenerator.createAnomalies());
        return serviceInput;
    }


}
