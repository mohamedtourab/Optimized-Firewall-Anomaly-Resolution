
package ofar.generated.classes;

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
 *         &lt;element name="rule" type="{}ruleType" maxOccurs="unbounded" minOccurs="0"/>
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
    "rule"
})
public class AnomalyType {

    protected List<RuleType> rule;
    @XmlAttribute(name = "AnomalyName", required = true)
    protected AnomalyNames anomalyName;
    @XmlAttribute(name = "AnomalyID", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger anomalyID;

    /**
     * Gets the value of the rule property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rule property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRule().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RuleType }
     * 
     * 
     */
    public List<RuleType> getRule() {
        if (rule == null) {
            rule = new ArrayList<RuleType>();
        }
        return this.rule;
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
    @Override
    public String toString() {
        return "AnomalyType{" +
                "ruleID=" + rule +
                ", anomalyName=" + anomalyName +
                ", anomalyID=" + anomalyID +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null)return false;
        if(!(obj instanceof AnomalyType)) return false;
        AnomalyType other=(AnomalyType) obj;
        return other.anomalyName.equals(this.anomalyName)&&
                other.rule.equals(this.rule);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
