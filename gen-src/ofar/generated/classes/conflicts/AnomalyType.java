
package ofar.generated.classes.conflicts;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AnomalyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AnomalyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ruleID" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="AnomalyName" use="required" type="{}AnomalyNames" />
 *       &lt;attribute name="AnomalyID" use="required" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AnomalyType", propOrder = {
    "ruleID"
})
public class AnomalyType {

    @XmlSchemaType(name = "positiveInteger")
    protected List<BigInteger> ruleID;
    @XmlAttribute(name = "AnomalyName", required = true)
    protected AnomalyNames anomalyName;
    @XmlAttribute(name = "AnomalyID", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger anomalyID;

    /**
     * Gets the value of the ruleID property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ruleID property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRuleID().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BigInteger }
     * 
     * 
     */
    public List<BigInteger> getRuleID() {
        if (ruleID == null) {
            ruleID = new ArrayList<BigInteger>();
        }
        return this.ruleID;
    }

    /**
     * Gets the value of the anomalyName property.
     * 
     * @return
     *     possible object is
     *     {@link AnomalyNames }
     *     
     */
    public AnomalyNames getAnomalyName() {
        return anomalyName;
    }

    /**
     * Sets the value of the anomalyName property.
     * 
     * @param value
     *     allowed object is
     *     {@link AnomalyNames }
     *     
     */
    public void setAnomalyName(AnomalyNames value) {
        this.anomalyName = value;
    }

    /**
     * Gets the value of the anomalyID property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAnomalyID() {
        return anomalyID;
    }

    /**
     * Sets the value of the anomalyID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAnomalyID(BigInteger value) {
        this.anomalyID = value;
    }

}
