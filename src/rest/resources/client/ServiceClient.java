package rest.resources.client;

import ofar.generated.classes.input.ServiceInput;
import ofar.generated.classes.rules.Rules;
import ofar.generated.classes.solveRequest.SolveRequest;
import optimized.resolution.algorithm.classes.DataGenerator;

import rest.resources.OptimizerResource;

import javax.ws.rs.ForbiddenException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServiceClient {
    //CHANGE THE VALUE TO YOUR PROJECT NAME HERE
    private static final String projectName = "testProject";

    Logger logger = Logger.getLogger(OptimizerResource.class.getName());
    private static Client client;
    private final WebTarget target;        // the WebTarget of the main resource
    private final Map<URI, ServiceInput> map;    // a local map to access created resources

    public ServiceClient() {
        // create the Client object
        client = ClientBuilder.newClient();
        // create a web target for the main URI
        target = client.target(getBaseURI());
        // create the map
        map = new HashMap<>();

    }

    private static URI getBaseURI() {
        return UriBuilder.fromUri("http://localhost:8080/" + projectName + "/rest/optimizer/").build();
    }

    public void runClient() {
        ServiceInput serviceInput = DataGenerator.createServiceInput();
        URI uri1 = performPost(serviceInput);
        URI uri2 = performPost(serviceInput);
        performGetAllResources();
        performGetSingleResource(uri1, "");
        performPutOnSingleResource(uri1);
        performGetSingleResource(uri1, "");
        performGetSingleResource(uri1, "solveIrrelevance");
        performDelete(uri2);
        performGetAllResources();
        logger.log(Level.INFO, "Successful: Test Ended Successfully");

    }

    public URI performPost(ServiceInput serviceInput) {
        logger.log(Level.INFO, "--- Performing a Post --- \n");
        // Perform a POST to create a resource
        Response response = target.request(MediaType.APPLICATION_XML)
                .post(Entity.entity(serviceInput, MediaType.APPLICATION_XML));
        if (response.getStatus() == 201) {
            logger.log(Level.INFO, "--- Post executed correctly --- \n");
            // convert response entity to Negotiate object
            ServiceInput receivedServiceInput = response.readEntity(ServiceInput.class);
            URI uri = response.getLocation();
            logger.log(Level.INFO, "Received URI: " + uri);
            map.put(uri, receivedServiceInput);
            return uri;
        } else {
            logger.log(Level.INFO, "Post failed with status " + response.getStatus());
            client.close();
            throw new ForbiddenException("POST request cannot be performed");
        }
    }

    public void performDelete(URI uri) {
        logger.log(Level.INFO, "--- Performing a DELETE Request --- \n");
        WebTarget itemTarget = client.target(uri);
        ServiceInput response = itemTarget.request(MediaType.APPLICATION_XML)
                .delete(ServiceInput.class);
        logger.log(Level.INFO, "---DELETE Request Executed Successfully--- \n");

        logger.log(Level.INFO, "--- Performing a DELETE Request --- \n");
        logger.log(Level.INFO, "--- ServiceInput resource deleted ID --- \n" + response.getId());
        logger.log(Level.INFO, "--- ServiceInput resource deleted rules --- \n" + response.getDefectedRules().getRule());
        logger.log(Level.INFO, "--- ServiceInput resource deleted anomalies --- \n" + response.getAnomaliesList().getAnomaly());


    }

    public void performGetSingleResource(URI uri, String action) {
        WebTarget itemTarget;
        ServiceInput serviceInput = null;
        logger.log(Level.INFO, " --- Performing GET operations on the single resource --- ");
        if (action.equals("")) {
            itemTarget = client.target(uri);
            serviceInput = itemTarget.request().accept(MediaType.APPLICATION_XML)
                    .get(ServiceInput.class);
        } else {
            if (action.equals("solveIrrelevance") ||
                    action.equals("solveDuplication") ||
                    action.equals("solveShadowingRedundancy") ||
                    action.equals("solveSub-optimization")) {
                itemTarget = client.target(uri).queryParam(action);
                serviceInput = itemTarget.request().accept(MediaType.APPLICATION_XML)
                        .get(ServiceInput.class);
            } else {
                logger.log(Level.SEVERE, "Incorrect GET Action");
            }
        }

        if (serviceInput == null) {
            logger.log(Level.INFO, "This item is not found");
        } else {
            logger.log(Level.INFO, serviceInput.getId().toString());
        }

    }

    public void performPutOnSingleResource(URI uri) {
        ServiceInput serviceInput = map.get(uri);
        if (serviceInput == null) {
            logger.log(Level.INFO, " --- Cannot Perform PUT operations ServiceInput resource not found --- ");
            return;
        }
        logger.log(Level.INFO, " --- Performing PUT operations on the single resource --- ");
        WebTarget itemTarget = client.target(uri);
        SolveRequest solveRequest = DataGenerator.createSolveRequest();
        Rules rules = itemTarget.request().accept(MediaType.APPLICATION_XML).put(Entity.entity(solveRequest, MediaType.APPLICATION_XML), Rules.class);
        logger.log(Level.INFO, rules.getRule().toString());

    }

    public void performGetAllResources() {

        logger.log(Level.INFO, " --- Performing GET operations on the main resource --- ");
        List<ServiceInput> xmlResponse = target.request().accept(MediaType.APPLICATION_XML)
                .get(new GenericType<List<ServiceInput>>() {
                });
        if (xmlResponse.size() == 0) {
            logger.log(Level.INFO, " LIST IS EMPTY ");

        } else {
            logger.log(Level.INFO, " --- Response of GET to all resources in the web service --- ");
            for (ServiceInput n : xmlResponse) {
                logger.log(Level.INFO, n.getId().toString());
                logger.log(Level.INFO, n.getDefectedRules().toString());
                logger.log(Level.INFO, n.getAnomaliesList().toString());
            }
        }


    }

    public static void main(String[] args) {
        ServiceClient serviceClient = new ServiceClient();
        serviceClient.runClient();
    }


}
