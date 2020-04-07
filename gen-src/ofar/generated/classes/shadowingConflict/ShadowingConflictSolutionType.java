
package ofar.generated.classes.shadowingConflict;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ShadowingConflictSolutionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ShadowingConflictSolutionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ruleId" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="anomalyId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="toRemove" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="toChangeOrder" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ShadowingConflictSolutionType", propOrder = {
    "ruleId",
    "anomalyId",
    "toRemove",
    "toChangeOrder"
})
public class ShadowingConflictSolutionType {

    @XmlElement(defaultValue = "-1")
    protected Integer ruleId;
    protected int anomalyId;
    @XmlElement(defaultValue = "false")
    protected boolean toRemove;
    @XmlElement(defaultValue = "false")
    protected boolean toChangeOrder;

    /**
     * Gets the value of the ruleId property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getRuleId() {
        return ruleId;
    }

    /**
     * Sets the value of the ruleId property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setRuleId(Integer value) {
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
     * Gets the value of the toRemove property.
     * 
     */
    public boolean isToRemove() {
        return toRemove;
    }

    /**
     * Sets the value of the toRemove property.
     * 
     */
    public void setToRemove(boolean value) {
        this.toRemove = value;
    }

    /**
     * Gets the value of the toChangeOrder property.
     * 
     */
    public boolean isToChangeOrder() {
        return toChangeOrder;
    }

    /**
     * Sets the value of the toChangeOrder property.
     * 
     */
    public void setToChangeOrder(boolean value) {
        this.toChangeOrder = value;
    }

}
