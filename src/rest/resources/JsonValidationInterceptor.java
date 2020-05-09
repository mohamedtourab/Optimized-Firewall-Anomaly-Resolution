package rest.resources;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.util.JAXBSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

@Provider
@Consumes({"application/json", "text/json"})
public class JsonValidationInterceptor implements ReaderInterceptor {
    private final String jaxbPackage = "ofar.generated.classes.input";
    private final String jaxbPackage2 = "ofar.generated.classes.solveRequest";
    private Schema schema;
    private Schema schemaSolveRequest;
    private JAXBContext jc;
    private JAXBContext jc2;
    private Logger logger;
    private String responseBodyTemplate;

    public JsonValidationInterceptor() {
        logger = Logger.getLogger(XmlValidationProvider.class.getName());

        try {
            URL schemaStream = getClass().getClassLoader().getResource("/xsd/webservice_input_schema.xsd");
            URL schemaStream2 = getClass().getClassLoader().getResource("/xsd/solve_request.xsd");
/*			InputStream schemaStream2 = XmlValidationProvider.class.getResourceAsStream("/xsd/firewall_rules.xsd");
			InputStream schemaStream3 = XmlValidationProvider.class.getResourceAsStream("/xsd/conflict_schema.xsd");*/
            if (schemaStream == null) {
                logger.log(Level.SEVERE, "xml schema file Not found.");
                throw new IOException();
            }
            SchemaFactory sf = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
            schema = sf.newSchema(schemaStream);
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
            reader.close();
            logger.log(Level.INFO, "JsonValidationInterceptor initialized successfully");
        } catch (Exception se) {
            logger.log(Level.SEVERE, "Error initializing JsonValidationInterceptor. Service will not work properly.", se);
        }
    }

    @Override
    public Object aroundReadFrom(ReaderInterceptorContext context) throws IOException, WebApplicationException {
        Object ret = context.proceed();
        validate(ret);
        return ret;
    }

    public void validate(Object item) {
        if (item == null) {
            BadRequestException bre = new BadRequestException("Request body body is empty");
            String validationErrorMessage = "Request body is empty";
            String responseBody = responseBodyTemplate.replaceFirst("___TO_BE_REPLACED___", validationErrorMessage);
            Response response = Response.fromResponse(bre.getResponse()).entity(responseBody).type("text/html").build();
            throw new BadRequestException("Request body is empty", response);
        }
        String postRequestClass = "ofar.generated.classes.input.ServiceInput";
        try {
            JAXBSource source;
            Validator v;
            if (item.getClass().getName().equals(postRequestClass)) {
                source = new JAXBSource(jc, item);
                v = schema.newValidator();
            } else {
                source = new JAXBSource(jc2, item);
                v = schemaSolveRequest.newValidator();
            }
            v.setErrorHandler(new MyErrorHandler());
            v.validate(source);

        } catch (SAXException e) {
            logger.log(Level.WARNING, "Request body validation error.", e);
            String validationErrorMessage = "Request body validation error";
            if (e.getMessage() != null)
                validationErrorMessage += ": " + e.getMessage();
            Throwable linked = e.getCause();
            while (linked != null) {
                if (linked instanceof SAXParseException && linked.getMessage() != null)
                    validationErrorMessage += ": " + linked.getMessage();
                linked = linked.getCause();
            }
            BadRequestException bre = new BadRequestException("Request body validation error");
            String responseBody = responseBodyTemplate.replaceFirst("___TO_BE_REPLACED___", validationErrorMessage);
            Response response = Response.fromResponse(bre.getResponse()).entity(responseBody).type("text/html").build();
            throw new BadRequestException("Request body validation error", response);
        } catch (Exception e) {
            throw new InternalServerErrorException();
        }
    }

}
