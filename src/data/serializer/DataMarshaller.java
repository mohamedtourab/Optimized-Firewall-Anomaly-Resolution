package data.serializer;


import ofar.generated.classes.conflicts.Anomalies;
import ofar.generated.classes.input.ServiceInput;
import ofar.generated.classes.rules.Rules;
import optimized.resolution.algorithm.classes.DataCreator;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

public class DataMarshaller {

    public static void marshalData(Object data, String contextPath, String outputFilePath, String schemaPath) {
        // create a Marshaller and marshal to a file
        try {
            //contextPath = "ofar.generated.classes.input"
            JAXBContext jc = JAXBContext.newInstance(contextPath);
            Marshaller m = jc.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            // set validation wrt schema using default validation handler (rises exception with non-valid files)
            SchemaFactory sf = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            Schema schema = sf.newSchema(new File(schemaPath));
            m.setSchema(schema);

            //"D:\\MohamedMamdouh\\Education\\PoliTo\\Projects\\Optimized-Firewall-Anomaly-Resolution\\src\\data\\serializer\\serviceInput.xml"
            // Store XML to File
            File file = new File(outputFilePath);
            m.marshal(data, file);
        } catch (JAXBException | SAXException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //create the data to be serialized
        Rules rules = DataCreator.createRules();
        Anomalies anomalies = DataCreator.createAnomalies();
        ServiceInput serviceInput = new ServiceInput();
        serviceInput.setAnomaliesList(anomalies);
        serviceInput.setDefectedRules(rules);
        String outputPath = "D:\\MohamedMamdouh\\Education\\PoliTo\\Projects\\Optimized-Firewall-Anomaly-Resolution\\src\\data\\serializer\\serviceInput.xml";
        marshalData(serviceInput, "ofar.generated.classes.input", outputPath,"xsd/webservice_input_schema.xsd");
    }
}
