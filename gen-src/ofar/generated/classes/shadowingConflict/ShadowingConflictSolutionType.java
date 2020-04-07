
package ofar.generated.classes.shadowingConflict;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="toRemove" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="toChangeOrder" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
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

    protected Integer ruleId;
    protected int anomalyId;
    protected Boolean toRemove;
    protected Boolean toChangeOrder;

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
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isToRemove() {
        return toRemove;
    }

    /**
     * Sets the value of the toRemove property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setToRemove(Boolean value) {
        this.toRemove = value;
    }

    /**
     * Gets the value of the toChangeOrder property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isToChangeOrder() {
        return toChangeOrder;
    }

    /**
     * Sets the value of the toChangeOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setToChangeOrder(Boolean value) {
        this.toChangeOrder = value;
    }

}
