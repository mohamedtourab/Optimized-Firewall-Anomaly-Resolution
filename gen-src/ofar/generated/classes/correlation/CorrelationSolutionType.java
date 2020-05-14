
package ofar.generated.classes.correlation;

import ofar.generated.classes.rules.RuleType;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CorrelationSolutionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CorrelationSolutionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="toChange" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="ruleId" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" minOccurs="0"/>
 *         &lt;element name="anomalyId" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *         &lt;element name="updatedRule" type="{}ruleType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CorrelationSolutionType", propOrder = {
    "toChange",
    "ruleId",
    "anomalyId",
    "updatedRule"
})
public class CorrelationSolutionType {

    @XmlElement(defaultValue = "false")
    protected boolean toChange;
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger ruleId;
    @XmlElement(required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger anomalyId;
    protected RuleType updatedRule;

    /**
     * Gets the value of the toChange property.
     * 
     */
    public boolean isToChange() {
        return toChange;
    }

    /**
     * Sets the value of the toChange property.
     * 
     */
    public void setToChange(boolean value) {
        this.toChange = value;
    }

    /**
     * Gets the value of the ruleId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRuleId() {
        return ruleId;
    }

    /**
     * Sets the value of the ruleId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRuleId(BigInteger value) {
        this.ruleId = value;
    }

    /**
     * Gets the value of the anomalyId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAnomalyId() {
        return anomalyId;
    }

    /**
     * Sets the value of the anomalyId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAnomalyId(BigInteger value) {
        this.anomalyId = value;
    }

    /**
     * Gets the value of the updatedRule property.
     * 
     * @return
     *     possible object is
     *     {@link RuleType }
     *     
     */
    public RuleType getUpdatedRule() {
        return updatedRule;
    }

    /**
     * Sets the value of the updatedRule property.
     * 
     * @param value
     *     allowed object is
     *     {@link RuleType }
     *     
     */
    public void setUpdatedRule(RuleType value) {
        this.updatedRule = value;
    }

}
