
package ofar.generated.classes.conflicts;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AnomalyNames.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="AnomalyNames">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="Irrelevance"/>
 *     &lt;enumeration value="Duplication"/>
 *     &lt;enumeration value="ShadowingRedundancy"/>
 *     &lt;enumeration value="Unnecessary"/>
 *     &lt;enumeration value="Contradiction"/>
 *     &lt;enumeration value="ShadowingConflict"/>
 *     &lt;enumeration value="Correlation"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "AnomalyNames")
@XmlEnum
public enum AnomalyNames {

    @XmlEnumValue("Irrelevance")
    IRRELEVANCE("Irrelevance"),
    @XmlEnumValue("Duplication")
    DUPLICATION("Duplication"),
    @XmlEnumValue("ShadowingRedundancy")
    SHADOWING_REDUNDANCY("ShadowingRedundancy"),
    @XmlEnumValue("Unnecessary")
    UNNECESSARY("Unnecessary"),
    @XmlEnumValue("Contradiction")
    CONTRADICTION("Contradiction"),
    @XmlEnumValue("ShadowingConflict")
    SHADOWING_CONFLICT("ShadowingConflict"),
    @XmlEnumValue("Correlation")
    CORRELATION("Correlation");
    private final String value;

    AnomalyNames(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static AnomalyNames fromValue(String v) {
        for (AnomalyNames c: AnomalyNames.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
