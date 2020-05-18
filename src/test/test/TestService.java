package test;

import ofar.generated.classes.input.ServiceInput;
import ofar.generated.classes.solveRequest.SolveRequest;
import optimized.resolution.algorithm.classes.DataGenerator;
import org.junit.Assert;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestService {
    //CHANGE THE VALUE TO YOUR PROJECT NAME HERE
    private final String projectName = "testProject";

    private final String baseUrl = "http://localhost:8080/" + projectName + "/rest/optimizer";
    private static final Logger logger = Logger.getLogger(TestService.class.getName());
    private static final Client client = ClientBuilder.newClient();
    private final WebTarget target = client.target(baseUrl);

    @Test
    //Check that post request will correctly create a resource
    public void correct_post_request() {
        ServiceInput serviceInput = DataGenerator.createServiceInput();
        Assert.assertNotNull("The Generated Data is null", serviceInput);
        logger.log(Level.FINEST, "OFAR POST (correct_post_request) test [:Started]");
        // Perform a correct POST request with a correct body
        Response response = target.request(MediaType.APPLICATION_XML)
                .post(Entity.entity(serviceInput, MediaType.APPLICATION_XML));

        // Assert that correct status code is returned.
        Assert.assertEquals(201, response.getStatus());

        logger.log(Level.FINEST, "OFAR{post} test [:Ended]");
    }

    @Test
    //Check that the validator will throw 400 Bad request when the POST body doesn't validate against the schema
    public void wrong_post_request() {
        //Creating wrong object
        SolveRequest serviceInput = DataGenerator.createSolveRequest();
        Assert.assertNotNull("The Generated Data is null", serviceInput);
        logger.log(Level.INFO, "OFAR POST (correct_post_request) test [:Started]");
        // Perform a POST to create ServiceInput with a wrong type
        Response response = target.request(MediaType.APPLICATION_XML)
                .post(Entity.entity(serviceInput, MediaType.APPLICATION_XML));

        // Assert that correct status code is returned.
        Assert.assertEquals(400, response.getStatus());

        logger.log(Level.INFO, "OFAR POST test [:Ended]");
    }

    @Test
    //Check that the put request is performed correctly and the resource is updated
    public void correct_put_request() {
        ServiceInput serviceInput = DataGenerator.createServiceInput();
        // Perform a POST to create a resource
        ServiceInput postResult = target.request(MediaType.APPLICATION_XML)
                .post(Entity.entity(serviceInput, MediaType.APPLICATION_XML), ServiceInput.class);
        logger.log(Level.INFO, "OFAR PUT (correct_put_request) test [:Started]");
        //Creating new target for the newly created resource
        WebTarget itemTarget = target.path(postResult.getId().toString());
        //Creating a solve request  based on the description mentioned in the paper
        SolveRequest solveRequest = DataGenerator.createSolveRequest();
        Assert.assertNotNull("Generated SolveRequest is null", solveRequest);
        //Performing a put request using a correct solveRequest
        Response response = itemTarget.request().accept(MediaType.APPLICATION_XML).put(Entity.entity(solveRequest, MediaType.APPLICATION_XML), Response.class);
        // Assert that correct status code is returned.
        Assert.assertEquals(200, response.getStatus());
        logger.log(Level.INFO, "OFAR PUT test [:Ended]");
    }

    @Test
    //Check that validation throw a 400 bad request when the put body doesn't match the schema
    public void wrong_put_request() {
        logger.log(Level.INFO, "OFAR PUT (correct_put_request) test [:Started]");
        WebTarget itemTarget = target.path("0");
        //Creating a wrong solveRequest which has different type and schema
        ServiceInput solveRequest = DataGenerator.createServiceInput();
        Assert.assertNotNull("Generated SolveRequest is null", solveRequest);
        Response response = itemTarget.request().accept(MediaType.APPLICATION_XML).put(Entity.entity(solveRequest, MediaType.APPLICATION_XML), Response.class);
        Assert.assertNotNull("The received response is null", response);
        // Assert that correct status code is returned.
        Assert.assertEquals(400, response.getStatus());
        logger.log(Level.INFO, "OFAR PUT test [:Ended]");
    }

    @Test
    //Check that delete() method will throw 204 when the target to be deleted doesn't exist
    public void wrong_delete_request() {
        logger.log(Level.INFO, "OFAR DELETE (wrong_delete_request) test [:Started]");
        //Creating a wrong target
        WebTarget itemTarget = target.path("10000");
        //Performing delete request on the wrong target
        Response response = itemTarget.request(MediaType.APPLICATION_XML)
                .delete();
        Assert.assertEquals(204, response.getStatus());
        logger.log(Level.INFO, "OFAR DELETE test [:Ended]");

    }

    @Test
    //Check that delete() method will correctly delete the resource
    public void correct_delete_request() {
        ServiceInput serviceInput = DataGenerator.createServiceInput();
        logger.log(Level.INFO, "OFAR DELETE (correct_delete_request) test [:Started]");
        // Perform a POST to create a resource
        ServiceInput postResult = target.request(MediaType.APPLICATION_XML)
                .post(Entity.entity(serviceInput, MediaType.APPLICATION_XML), ServiceInput.class);
        //Creating new target for the newly created resource
        WebTarget itemTarget = target.path(postResult.getId().toString());
        //Performing a delete request for this target
        Response response = itemTarget.request(MediaType.APPLICATION_XML)
                .delete();
        Assert.assertEquals(200, response.getStatus());
        logger.log(Level.INFO, "OFAR DELETE test [:Ended]");

    }
}
