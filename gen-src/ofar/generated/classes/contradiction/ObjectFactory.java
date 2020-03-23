
package ofar.generated.classes.contradiction;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ofar.generated.classes.contradiction package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _ContradictionSolution_QNAME = new QName("", "ContradictionSolution");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ofar.generated.classes.contradiction
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ContradictionSolutionType }
     * 
     */
    public ContradictionSolutionType createContradictionSolutionType() {
        return new ContradictionSolutionType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ContradictionSolutionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ContradictionSolution")
    public JAXBElement<ContradictionSolutionType> createContradictionSolution(ContradictionSolutionType value) {
        return new JAXBElement<ContradictionSolutionType>(_ContradictionSolution_QNAME, ContradictionSolutionType.class, null, value);
    }

}
