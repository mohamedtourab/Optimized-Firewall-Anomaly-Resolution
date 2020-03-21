
package ofar.generated.classes;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ruleType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ruleType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="priority" type="{http://www.w3.org/2001/XMLSchema}positiveInteger"/>
 *         &lt;element name="IPsrc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Psrc" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="IPdst" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Pdst" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Protocol" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Action">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;enumeration value="ALLOW"/>
 *               &lt;enumeration value="DENY"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="ruleID" use="required" type="{http://www.w3.org/2001/XMLSchema}positiveInteger" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ruleType", propOrder = {
    "priority",
    "iPsrc",
    "psrc",
    "iPdst",
    "pdst",
    "protocol",
    "action"
})
public class RuleType {

    @XmlElement(required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger priority;
    @XmlElement(name = "IPsrc", required = true)
    protected String iPsrc;
    @XmlElement(name = "Psrc", required = true)
    protected String psrc;
    @XmlElement(name = "IPdst", required = true)
    protected String iPdst;
    @XmlElement(name = "Pdst", required = true)
    protected String pdst;
    @XmlElement(name = "Protocol", required = true)
    protected String protocol;
    @XmlElement(name = "Action", required = true)
    protected String action;
    @XmlAttribute(name = "ruleID", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger ruleID;

    /**
     * Gets the value of the priority property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPriority(BigInteger value) {
        this.priority = value;
    }

    /**
     * Gets the value of the iPsrc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPsrc() {
        return iPsrc;
    }

    /**
     * Sets the value of the iPsrc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPsrc(String value) {
        this.iPsrc = value;
    }

    /**
     * Gets the value of the psrc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPsrc() {
        return psrc;
    }

    /**
     * Sets the value of the psrc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPsrc(String value) {
        this.psrc = value;
    }

    /**
     * Gets the value of the iPdst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIPdst() {
        return iPdst;
    }

    /**
     * Sets the value of the iPdst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIPdst(String value) {
        this.iPdst = value;
    }

    /**
     * Gets the value of the pdst property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPdst() {
        return pdst;
    }

    /**
     * Sets the value of the pdst property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPdst(String value) {
        this.pdst = value;
    }

    /**
     * Gets the value of the protocol property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProtocol() {
        return protocol;
    }

    /**
     * Sets the value of the protocol property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProtocol(String value) {
        this.protocol = value;
    }

    /**
     * Gets the value of the action property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the value of the action property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAction(String value) {
        this.action = value;
    }

    /**
     * Gets the value of the ruleID property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRuleID() {
        return ruleID;
    }

    /**
     * Sets the value of the ruleID property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRuleID(BigInteger value) {
        this.ruleID = value;
    }
    @Override
    public String toString() {
        return "RuleType{" +
                "priority=" + priority +
                ", iPsrc='" + iPsrc + '\'' +
                ", psrc='" + psrc + '\'' +
                ", iPdst='" + iPdst + '\'' +
                ", pdst='" + pdst + '\'' +
                ", protocol='" + protocol + '\'' +
                ", action='" + action + '\'' +
                ", ruleID=" + ruleID +
                '}';
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof RuleType)) return false;
        RuleType other = (RuleType) obj;

        return other.getAction().equals(this.action) &&
                other.getPriority().equals(this.priority) &&
                other.getIPdst().equals(this.iPdst) &&
                other.getIPsrc().equals(this.iPsrc) &&
                other.getPsrc().equals(this.psrc) &&
                other.getPdst().equals(this.pdst) &&
                other.getProtocol().equals(this.protocol);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
