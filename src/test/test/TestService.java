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
    private final String projectName = "Optimized_Firewall_Anomaly_Resolution_war_exploded";

    private final String baseUrl = "http://localhost:8080/" + projectName + "/rest/optimizer";
    private static final Logger logger = Logger.getLogger(TestService.class.getName());
    private static final Client client = ClientBuilder.newClient();
    private final WebTarget target = client.target(baseUrl);

    @Test
    public void correct_post_request() {
        ServiceInput serviceInput = DataGenerator.createServiceInput();
        logger.log(Level.FINEST, "OFAR POST (correct_post_request) test [:Started]");
        // Perform a POST http request using Json
        Response response = target.request(MediaType.APPLICATION_XML)
                .post(Entity.entity(serviceInput, MediaType.APPLICATION_XML));

        // Assert that correct status code is returned.
        Assert.assertEquals(201, response.getStatus());

        logger.log(Level.FINEST, "OFAR{post} test [:Ended]");
    }

    @Test
    public void wrong_post_request() {
        //Creating wrong object
        SolveRequest serviceInput = DataGenerator.createSolveRequest();
        logger.log(Level.INFO, "OFAR POST (correct_post_request) test [:Started]");
        // Perform a POST http request using Json
        Response response = target.request(MediaType.APPLICATION_XML)
                .post(Entity.entity(serviceInput, MediaType.APPLICATION_XML));

        // Assert that correct status code is returned.
        Assert.assertEquals(400, response.getStatus());

        logger.log(Level.INFO, "OFAR POST test [:Ended]");
    }

    @Test
    public void correct_put_request() {
        ServiceInput serviceInput = DataGenerator.createServiceInput();
        // Perform a POST
        target.request(MediaType.APPLICATION_XML)
                .post(Entity.entity(serviceInput, MediaType.APPLICATION_XML));
        logger.log(Level.INFO, "OFAR PUT (correct_put_request) test [:Started]");
        WebTarget itemTarget = target.path("1");
        SolveRequest solveRequest = DataGenerator.createSolveRequest();
        Response response = itemTarget.request().accept(MediaType.APPLICATION_XML).put(Entity.entity(solveRequest, MediaType.APPLICATION_XML), Response.class);
        // Assert that correct status code is returned.
        Assert.assertEquals(200, response.getStatus());
        logger.log(Level.INFO, "OFAR PUT test [:Ended]");
    }

    @Test
    public void wrong_put_request() {
        logger.log(Level.INFO, "OFAR PUT (correct_put_request) test [:Started]");
        WebTarget itemTarget = target.path("0");
        ServiceInput solveRequest = DataGenerator.createServiceInput();
        Response response = itemTarget.request().accept(MediaType.APPLICATION_XML).put(Entity.entity(solveRequest, MediaType.APPLICATION_XML), Response.class);
        // Assert that correct status code is returned.
        Assert.assertEquals(400, response.getStatus());
        logger.log(Level.INFO, "OFAR PUT test [:Ended]");
    }

    @Test
    public void wrong_delete_request() {
        logger.log(Level.INFO, "OFAR DELETE (wrong_delete_request) test [:Started]");
        WebTarget itemTarget = target.path("1000");
        Response response = itemTarget.request(MediaType.APPLICATION_XML)
                .delete();
        Assert.assertEquals(204, response.getStatus());
        logger.log(Level.INFO, "OFAR DELETE test [:Ended]");

    }
}
