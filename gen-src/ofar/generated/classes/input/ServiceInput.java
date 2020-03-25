
package ofar.generated.classes.input;

import ofar.generated.classes.conflicts.Anomalies;
import ofar.generated.classes.rules.Rules;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DefectedRules" type="{}Rules"/>
 *         &lt;element name="AnomaliesList" type="{}Anomalies"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "defectedRules",
    "anomaliesList"
})
@XmlRootElement(name = "ServiceInput")
public class ServiceInput {

    @XmlElement(name = "DefectedRules", required = true)
    protected Rules defectedRules;
    @XmlElement(name = "AnomaliesList", required = true)
    protected Anomalies anomaliesList;

    /**
     * Gets the value of the defectedRules property.
     * 
     * @return
     *     possible object is
     *     {@link Rules }
     *     
     */
    public Rules getDefectedRules() {
        return defectedRules;
    }

    /**
     * Sets the value of the defectedRules property.
     * 
     * @param value
     *     allowed object is
     *     {@link Rules }
     *     
     */
    public void setDefectedRules(Rules value) {
        this.defectedRules = value;
    }

    /**
     * Gets the value of the anomaliesList property.
     * 
     * @return
     *     possible object is
     *     {@link Anomalies }
     *     
     */
    public Anomalies getAnomaliesList() {
        return anomaliesList;
    }

    /**
     * Sets the value of the anomaliesList property.
     * 
     * @param value
     *     allowed object is
     *     {@link Anomalies }
     *     
     */
    public void setAnomaliesList(Anomalies value) {
        this.anomaliesList = value;
    }

}
