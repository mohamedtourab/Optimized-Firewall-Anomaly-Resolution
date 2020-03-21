
package ofar.generated.classes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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
 *         &lt;element name="ruleId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="anomalyId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="updatedRule" type="{}ruleType"/>
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

    protected boolean toChange;
    protected int ruleId;
    protected int anomalyId;
    @XmlElement(required = true)
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
     */
    public int getRuleId() {
        return ruleId;
    }

    /**
     * Sets the value of the ruleId property.
     *
     */
    public void setRuleId(int value) {
        this.ruleId = value;
    }

    /**
     * Gets the value of the anomalyId property.
     *
     */
    public int getAnomalyId() {
        return anomalyId;
    }

    /**
     * Sets the value of the anomalyId property.
     *
     */
    public void setAnomalyId(int value) {
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
