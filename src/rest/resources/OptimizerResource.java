package rest.resources;

import ofar.generated.classes.conflicts.Anomalies;
import ofar.generated.classes.conflicts.AnomalyNames;
import ofar.generated.classes.conflicts.AnomalyType;
import ofar.generated.classes.input.ServiceInput;
import optimized.resolution.algorithm.classes.ConflictResolver;
import org.omg.CORBA.DATA_CONVERSION;
import rest.resources.DB.Database;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.xml.crypto.Data;
import java.net.URI;

@Path("/optimizer")
public class OptimizerResource {
    @POST
    @Path("/createResource")
    @Produces(MediaType.APPLICATION_XML)
    @Consumes(MediaType.APPLICATION_XML)
    public Response createOptimizer(ServiceInput serviceInput, @Context UriInfo uriInfo) {
        if(serviceInput == null){
            //throw new ForbiddenException("Empty Input");
        }
        ServiceInput savedInput = Database.insertEntry(serviceInput);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        URI u = builder.path(Long.toString(savedInput.getId())).build();
        return Response.created(u).entity(savedInput).build();
    }
    @GET
    @Path("/getConflictAnomalies/{id}")
    @Produces(MediaType.APPLICATION_XML)
    public Anomalies getUnresolvedAnomalies(@PathParam("id")int id){
        ServiceInput serviceInput= Database.getEntry(id);
        ConflictResolver conflictResolver = new ConflictResolver(serviceInput.getDefectedRules(),serviceInput.getAnomaliesList());
        return conflictResolver.getConflictAnomalies();
    }

}



