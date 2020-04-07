package rest.resources;

import ofar.generated.classes.conflicts.Anomalies;
import ofar.generated.classes.conflicts.AnomalyNames;
import ofar.generated.classes.input.ServiceInput;
import ofar.generated.classes.rules.Rules;
import ofar.generated.classes.solveRequest.SolveRequest;
import optimized.resolution.algorithm.classes.ConflictResolver;
import rest.resources.DB.Database;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.Collection;


@Path("/optimizer")
public class OptimizerResource {

    @POST
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Response createOptimizer(ServiceInput serviceInput, @Context UriInfo uriInfo) {
        if (serviceInput == null) {
            //throw new ForbiddenException("Empty Input");
        }
        ServiceInput savedInput = Database.insertEntry(serviceInput);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        URI u = builder.path(Long.toString(savedInput.getId())).build();
        return Response.created(u).entity(savedInput).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Collection<ServiceInput> getAllServiceInput() {
        return Database.dbHashMap.values();
    }


    @GET
    @Path("hello")
    @Produces("text/plain")
    public String getHello() {
        return "Hello";
    }

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public ServiceInput deleteOptimizer(@PathParam("id") int id) {
        return Database.dbHashMap.remove(id);
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Anomalies getUnresolvedAnomalies(@PathParam("id") int id) {
        ServiceInput serviceInput = Database.getEntry(id);
        ConflictResolver conflictResolver = new ConflictResolver(serviceInput.getDefectedRules(), serviceInput.getAnomaliesList());
        conflictResolver.removeIrrelevanceAnomaly();
        conflictResolver.removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.DUPLICATION);
        conflictResolver.removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.SHADOWING_REDUNDANCY);
        return conflictResolver.getConflictAnomalies();
    }

    @GET
    @Path("/solveIrrelevance/{id}")
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Rules solveIrrelevance(@PathParam("id") int id) {
        ServiceInput serviceInput = Database.getEntry(id);
        ConflictResolver conflictResolver = new ConflictResolver(serviceInput.getDefectedRules(), serviceInput.getAnomaliesList());
        conflictResolver.removeIrrelevanceAnomaly();
        serviceInput.setDefectedRules(conflictResolver.getRules());
        serviceInput.setAnomaliesList(conflictResolver.getAnomalies());
        Database.dbHashMap.put(id, serviceInput);
        return conflictResolver.getRules();
    }

    @GET
    @Path("/solveDuplication/{id}")
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Rules solveDuplication(@PathParam("id") int id) {
        ServiceInput serviceInput = Database.getEntry(id);
        ConflictResolver conflictResolver = new ConflictResolver(serviceInput.getDefectedRules(), serviceInput.getAnomaliesList());
        conflictResolver.removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.DUPLICATION);
        serviceInput.setDefectedRules(conflictResolver.getRules());
        serviceInput.setAnomaliesList(conflictResolver.getAnomalies());
        Database.dbHashMap.put(id, serviceInput);
        return conflictResolver.getRules();
    }

    @GET
    @Path("/solveShadowingRedundancy/{id}")
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Rules solveShadowingRedundancy(@PathParam("id") int id) {
        ServiceInput serviceInput = Database.getEntry(id);
        ConflictResolver conflictResolver = new ConflictResolver(serviceInput.getDefectedRules(), serviceInput.getAnomaliesList());
        conflictResolver.removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.SHADOWING_REDUNDANCY);
        serviceInput.setDefectedRules(conflictResolver.getRules());
        serviceInput.setAnomaliesList(conflictResolver.getAnomalies());
        Database.dbHashMap.put(id, serviceInput);
        return conflictResolver.getRules();
    }

    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
    public Rules solveConflicts(SolveRequest solveRequest, @PathParam("id") int id) {
        ServiceInput serviceInput = Database.getEntry(id);
        ConflictResolver conflictResolver = new ConflictResolver(serviceInput.getDefectedRules(), serviceInput.getAnomaliesList());
        conflictResolver.executeSolveRequest(solveRequest);
        serviceInput.setDefectedRules(conflictResolver.getRules());
        serviceInput.setAnomaliesList(conflictResolver.getAnomalies());
        Database.dbHashMap.put(id, serviceInput);
        return conflictResolver.getRules();
    }
}



