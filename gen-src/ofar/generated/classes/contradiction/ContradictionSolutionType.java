
package ofar.generated.classes.contradiction;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ContradictionSolutionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ContradictionSolutionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ruleId" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="toRemove" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ContradictionSolutionType", propOrder = {
    "ruleId",
    "toRemove"
})
public class ContradictionSolutionType {

    protected int ruleId;
    protected boolean toRemove;

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

}
