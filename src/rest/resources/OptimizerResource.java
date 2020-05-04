package rest.resources;

import ofar.generated.classes.conflicts.Anomalies;
import ofar.generated.classes.conflicts.AnomalyNames;
import ofar.generated.classes.input.ServiceInput;
import ofar.generated.classes.rules.ObjectFactory;
import ofar.generated.classes.rules.Rules;
import ofar.generated.classes.solveRequest.SolveRequest;
import optimized.resolution.algorithm.classes.ConflictResolver;
import optimized.resolution.algorithm.classes.UnnecessaryAnomalyChecker;
import rest.resources.DB.Database;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.xml.bind.JAXBElement;
import java.net.URI;
import java.util.Collection;


@Path("/optimizer")
public class OptimizerResource {

    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createOptimizer(ServiceInput serviceInput, @Context UriInfo uriInfo) {
        if (serviceInput == null) {
            throw new ForbiddenException("Empty Input");
        }
        ServiceInput savedInput = Database.insertEntry(serviceInput);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        URI u = builder.path(Long.toString(savedInput.getId())).build();
        return Response.created(u).entity(savedInput).build();
    }

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
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
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ServiceInput deleteOptimizer(@PathParam("id") int id) {
        return Database.dbHashMap.remove(id);
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public JAXBElement<Anomalies> getUnresolvedAnomalies(@PathParam("id") int id) {
        ofar.generated.classes.conflicts.ObjectFactory objectFactory = new ofar.generated.classes.conflicts.ObjectFactory();
        ConflictResolver conflictResolver;
        synchronized (Database.dbHashMap) {
            ServiceInput serviceInput = Database.getEntry(id);
            if (serviceInput == null) {
                throw new NotFoundException();
            }
            conflictResolver = new ConflictResolver(serviceInput.getDefectedRules(), serviceInput.getAnomaliesList());
            conflictResolver.removeIrrelevanceAnomaly();
            conflictResolver.removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.DUPLICATION);
            conflictResolver.removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.SHADOWING_REDUNDANCY);
        }
        return objectFactory.createAnomalies(conflictResolver.getConflictAnomalies());
    }

    @GET
    @Path("/solveIrrelevance/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public JAXBElement<Rules> solveIrrelevance(@PathParam("id") int id) {
        ObjectFactory objectFactory = new ObjectFactory();
        ConflictResolver conflictResolver;
        synchronized (Database.dbHashMap) {
            ServiceInput serviceInput = Database.getEntry(id);
            if (serviceInput == null) {
                throw new NotFoundException();
            }
            conflictResolver = new ConflictResolver(serviceInput.getDefectedRules(), serviceInput.getAnomaliesList());
            conflictResolver.removeIrrelevanceAnomaly();
        }
        return objectFactory.createRules(conflictResolver.getRules());
    }

    @GET
    @Path("/solveDuplication/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public JAXBElement<Rules> solveDuplication(@PathParam("id") int id) {
        ObjectFactory objectFactory = new ObjectFactory();
        ConflictResolver conflictResolver;
        synchronized (Database.dbHashMap) {
            ServiceInput serviceInput = Database.getEntry(id);
            if (serviceInput == null) {
                throw new NotFoundException();
            }
            conflictResolver = new ConflictResolver(serviceInput.getDefectedRules(), serviceInput.getAnomaliesList());
            conflictResolver.removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.DUPLICATION);
        }
        return objectFactory.createRules(conflictResolver.getRules());
    }

    @GET
    @Path("/solveShadowingRedundancy/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public JAXBElement<Rules> solveShadowingRedundancy(@PathParam("id") int id) {
        ObjectFactory objectFactory = new ObjectFactory();
        ConflictResolver conflictResolver;
        synchronized (Database.dbHashMap) {
            ServiceInput serviceInput = Database.getEntry(id);
            if (serviceInput == null) {
                throw new NotFoundException();
            }
            conflictResolver = new ConflictResolver(serviceInput.getDefectedRules(), serviceInput.getAnomaliesList());
            conflictResolver.removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.SHADOWING_REDUNDANCY);
        }
        return objectFactory.createRules(conflictResolver.getRules());
    }

    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public JAXBElement<Rules> solveConflicts(SolveRequest solveRequest, @PathParam("id") int id) throws Exception {
        ObjectFactory objectFactory = new ObjectFactory();
        ConflictResolver conflictResolver;
        UnnecessaryAnomalyChecker unnecessaryAnomalyChecker;
        synchronized (Database.dbHashMap) {
            ServiceInput serviceInput = Database.getEntry(id);
            if (serviceInput == null) {
                throw new NotFoundException();
            }
            conflictResolver = new ConflictResolver(serviceInput.getDefectedRules(), serviceInput.getAnomaliesList());
            conflictResolver.resolveAnomalies();
            conflictResolver.executeSolveRequest(solveRequest);
            unnecessaryAnomalyChecker = new UnnecessaryAnomalyChecker(conflictResolver.getRules());
            unnecessaryAnomalyChecker.checkForUnnecessaryAnomalies();
            conflictResolver.removeUnnecessaryAnomaly();
            serviceInput.setDefectedRules(conflictResolver.getRules());
            serviceInput.setAnomaliesList(conflictResolver.getAnomalies());

        }
        return objectFactory.createRules(conflictResolver.getRules());
    }
}



