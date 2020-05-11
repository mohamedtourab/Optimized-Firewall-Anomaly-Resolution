package rest.resources.client;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/test")
@Api(value = "/test")
public class ClientResource {
    public UriInfo uriInfo;

    private final ServiceClient service = new ServiceClient();

    public ClientResource(@Context UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    @GET
    @ApiOperation(value = "testClient", notes = "Run Client perform different tests")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "OK")})
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response test() {
        service.runClient();
        return  Response.status(Response.Status.OK).entity("<test>TEST ENDED SUCCESSFULLY</test>").build();
    }
}
