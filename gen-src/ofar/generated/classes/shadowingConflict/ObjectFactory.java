
package ofar.generated.classes.shadowingConflict;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the ofar.generated.classes.shadowingConflict package. 
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

    private final static QName _ShadowingConflictSolution_QNAME = new QName("", "ShadowingConflictSolution");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: ofar.generated.classes.shadowingConflict
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ShadowingConflictSolutionType }
     * 
     */
    public ShadowingConflictSolutionType createShadowingConflictSolutionType() {
        return new ShadowingConflictSolutionType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ShadowingConflictSolutionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "ShadowingConflictSolution")
    public JAXBElement<ShadowingConflictSolutionType> createShadowingConflictSolution(ShadowingConflictSolutionType value) {
        return new JAXBElement<ShadowingConflictSolutionType>(_ShadowingConflictSolution_QNAME, ShadowingConflictSolutionType.class, null, value);
    }

}
