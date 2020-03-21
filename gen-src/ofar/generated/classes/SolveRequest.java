
package ofar.generated.classes;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="contradictionSolutions" type="{}ContradictionSolutionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="shadowingConflictSolutions" type="{}ShadowingConflictSolutionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="correlationSolutions" type="{}CorrelationSolutionType" maxOccurs="unbounded" minOccurs="0"/>
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
    "contradictionSolutions",
    "shadowingConflictSolutions",
    "correlationSolutions"
})
@XmlRootElement(name = "SolveRequest")
public class SolveRequest {

    protected List<ContradictionSolutionType> contradictionSolutions;
    protected List<ShadowingConflictSolutionType> shadowingConflictSolutions;
    protected List<CorrelationSolutionType> correlationSolutions;

    /**
     * Gets the value of the contradictionSolutions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contradictionSolutions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContradictionSolutions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ContradictionSolutionType }
     * 
     * 
     */
    public List<ContradictionSolutionType> getContradictionSolutions() {
        if (contradictionSolutions == null) {
            contradictionSolutions = new ArrayList<ContradictionSolutionType>();
        }
        return this.contradictionSolutions;
    }

    /**
     * Gets the value of the shadowingConflictSolutions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the shadowingConflictSolutions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getShadowingConflictSolutions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ShadowingConflictSolutionType }
     * 
     * 
     */
    public List<ShadowingConflictSolutionType> getShadowingConflictSolutions() {
        if (shadowingConflictSolutions == null) {
            shadowingConflictSolutions = new ArrayList<ShadowingConflictSolutionType>();
        }
        return this.shadowingConflictSolutions;
    }

    /**
     * Gets the value of the correlationSolutions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the correlationSolutions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCorrelationSolutions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CorrelationSolutionType }
     * 
     * 
     */
    public List<CorrelationSolutionType> getCorrelationSolutions() {
        if (correlationSolutions == null) {
            correlationSolutions = new ArrayList<CorrelationSolutionType>();
        }
        return this.correlationSolutions;
    }

}
