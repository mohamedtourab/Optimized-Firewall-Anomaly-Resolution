package rest.resources;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXParseException;

@Provider
@Consumes({"application/xml", "text/xml"})
public class XmlValidationProvider<T> implements MessageBodyReader<T> {
    private final String jaxbPackage = "ofar.generated.classes.input";
    private final String jaxbPackage2 = "ofar.generated.classes.solveRequest";
    private Schema schemaServiceInput;
    private Schema schemaSolveRequest;
    private JAXBContext jc;
    private JAXBContext jc2;
    private final Logger logger;
    private String responseBodyTemplate;


    public XmlValidationProvider() {
        logger = Logger.getLogger(XmlValidationProvider.class.getName());
        logger.log(Level.SEVERE, "--- XML validator ---");

        try {
            URL schemaStream = getClass().getClassLoader().getResource("/xsd/webservice_input_schema.xsd");
            URL schemaStream2 = getClass().getClassLoader().getResource("/xsd/solve_request.xsd");

            /*InputStream schemaStream2 = XmlValidationProvider.class.getResourceAsStream("/xsd/firewall_rules.xsd");
            InputStream schemaStream3 = XmlValidationProvider.class.getResourceAsStream("/xsd/conflict_schema.xsd");*/

            if (schemaStream == null) {
                logger.log(Level.SEVERE, "xml schema file Not found.");
                throw new IOException();
            }
            SchemaFactory sf = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            schemaServiceInput = sf.newSchema(schemaStream);
            schemaSolveRequest = sf.newSchema(schemaStream2);
            jc = JAXBContext.newInstance(jaxbPackage);
            jc2 = JAXBContext.newInstance(jaxbPackage2);

            InputStream templateStream = XmlValidationProvider.class.getResourceAsStream("/html/BadRequestBodyTemplate.html");
            if (templateStream == null) {
                logger.log(Level.SEVERE, "html template file Not found.");
                throw new IOException();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(templateStream));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            responseBodyTemplate = out.toString();

            logger.log(Level.INFO, "XmlProvider initialized successfully");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error initializing XmlProvider. Service will not work properly.", e);
        }
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return jaxbPackage.equals(type.getPackage().getName()) || jaxbPackage2.equals(type.getPackage().getName());
    }

    @Override
    public T readFrom(Class<T> type, Type genericType, Annotation[] annotations, MediaType mediaType,
                      MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws WebApplicationException {
        Unmarshaller unmarshaller;
        String postRequestClass = "ofar.generated.classes.input.ServiceInput";
        String putRequestClass = "ofar.generated.classes.solveRequest.SolveRequest";

        try {
            jc = JAXBContext.newInstance(jaxbPackage);
            jc2 = JAXBContext.newInstance(jaxbPackage2);
            if (type.getName().equals(postRequestClass)) {
                unmarshaller = jc.createUnmarshaller();
                unmarshaller.setSchema(schemaServiceInput);
            } else {
                unmarshaller = jc2.createUnmarshaller();
                unmarshaller.setSchema(schemaSolveRequest);
            }
            try {
                Object obj = unmarshaller.unmarshal(entityStream);
                if (obj.getClass().equals(type))
                    return (T) obj;
                else {
                    logger.log(Level.WARNING, "Request body validation error. Wrong Type.");
                    BadRequestException bre = new BadRequestException("Request body validation error. Wrong Type.");
                    String responseBody = responseBodyTemplate.replaceFirst("___TO_BE_REPLACED___", "Request body validation error. Wrong Type.");
                    Response response = Response.fromResponse(bre.getResponse()).entity(responseBody).type("text/html").build();
                    throw new BadRequestException("Request body validation error", response);
                }
            } catch (JAXBException ex) {
                logger.log(Level.WARNING, "Request body validation error.", ex);
                Throwable linked = ex.getLinkedException();
                String validationErrorMessage = "Request body validation error";
                if (linked instanceof SAXParseException)
                    validationErrorMessage += ": " + linked.getMessage();
                BadRequestException bre = new BadRequestException("Request body validation error");
                String responseBody = responseBodyTemplate.replaceFirst("___TO_BE_REPLACED___", validationErrorMessage);
                Response response = Response.fromResponse(bre.getResponse()).entity(responseBody).type("text/html").build();
                throw new BadRequestException("Request body validation error", response);
            }
        } catch (JAXBException e) {
            logger.log(Level.INFO, "Unable to initialize unmarshaller.", e);
            throw new InternalServerErrorException();
        }

    }
}
