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

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized ServiceInput deleteOptimizer(@PathParam("id") int id) {
        return Database.dbHashMap.remove(id);
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ServiceInput getUnresolvedAnomalies(
            @PathParam("id") int id,
            @QueryParam("resolutionType") @DefaultValue("") String requestedTypeOfResolution) {
        switch (requestedTypeOfResolution) {
            case "solveIrrelevance": {
                return solveIrrelevance(id);
            }
            case "solveDuplication": {
                return solveDuplication(id);
            }
            case "solveShadowingRedundancy": {
                return solveShadowingRedundancy(id);
            }
            case "solveSub-optimization": {
                return solveSubOptimizationAnomalies(id);
            }
            default: {
                return Database.getEntry(id);
            }
        }
    }

    private ServiceInput solveSubOptimizationAnomalies(int id) {
        ConflictResolver conflictResolver;
        ServiceInput serviceInput = Database.getEntry(id);
        if (serviceInput == null) {
            throw new NotFoundException();
        }
        conflictResolver = new ConflictResolver(serviceInput.getDefectedRules(), serviceInput.getAnomaliesList());
        conflictResolver.removeIrrelevanceAnomaly();
        conflictResolver.removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.DUPLICATION);
        conflictResolver.removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.SHADOWING_REDUNDANCY);

        ServiceInput returnedServiceInput = new ServiceInput();
        returnedServiceInput.setId(id);
        returnedServiceInput.setAnomaliesList(conflictResolver.getAnomalies());
        returnedServiceInput.setDefectedRules(conflictResolver.getRules());
        return returnedServiceInput;
    }

    private ServiceInput solveIrrelevance(int id) {
        ConflictResolver conflictResolver;
        ServiceInput serviceInput = Database.getEntry(id);
        if (serviceInput == null) {
            throw new NotFoundException();
        }
        conflictResolver = new ConflictResolver(serviceInput.getDefectedRules(), serviceInput.getAnomaliesList());
        conflictResolver.removeIrrelevanceAnomaly();
        ServiceInput returnedServiceInput = new ServiceInput();
        returnedServiceInput.setId(id);
        returnedServiceInput.setAnomaliesList(conflictResolver.getAnomalies());
        returnedServiceInput.setDefectedRules(conflictResolver.getRules());
        return returnedServiceInput;
    }


    private ServiceInput solveDuplication(int id) {
        ConflictResolver conflictResolver;
        ServiceInput serviceInput = Database.getEntry(id);
        if (serviceInput == null) {
            throw new NotFoundException();
        }
        conflictResolver = new ConflictResolver(serviceInput.getDefectedRules(), serviceInput.getAnomaliesList());
        conflictResolver.removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.DUPLICATION);

        ServiceInput returnedServiceInput = new ServiceInput();
        returnedServiceInput.setId(id);
        returnedServiceInput.setAnomaliesList(conflictResolver.getAnomalies());
        returnedServiceInput.setDefectedRules(conflictResolver.getRules());
        return returnedServiceInput;
    }


    private ServiceInput solveShadowingRedundancy(int id) {
        ConflictResolver conflictResolver;
        ServiceInput serviceInput = Database.getEntry(id);
        if (serviceInput == null) {
            throw new NotFoundException();
        }
        conflictResolver = new ConflictResolver(serviceInput.getDefectedRules(), serviceInput.getAnomaliesList());
        conflictResolver.removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.SHADOWING_REDUNDANCY);
        ServiceInput returnedServiceInput = new ServiceInput();
        returnedServiceInput.setId(id);
        returnedServiceInput.setAnomaliesList(conflictResolver.getAnomalies());
        returnedServiceInput.setDefectedRules(conflictResolver.getRules());
        return returnedServiceInput;
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
            int anomalyListSize = conflictResolver.getAnomalies().getAnomaly().size();
            //conflictResolver.getAnomalies().getAnomaly().get(anomalyListSize - 1).getAnomalyID().intValue() -> here i get the anomaly ID of the last element in the anomalies list
            Anomalies newlyCreatedAnomalies = unnecessaryAnomalyChecker.checkForUnnecessaryAnomalies(conflictResolver.getAnomalies().getAnomaly().get(anomalyListSize - 1).getAnomalyID().intValue());
            //Add the newly created anomalies the list of the anomalies to be solved
            conflictResolver.getAnomalies().getAnomaly().addAll(newlyCreatedAnomalies.getAnomaly());
            //Resolve the unnecessary anomalies
            conflictResolver.removeUnnecessaryAnomaly();
            //Update the database with the new list of rules and anomalies
            serviceInput.setDefectedRules(conflictResolver.getRules());
            serviceInput.setAnomaliesList(conflictResolver.getAnomalies());

        }
        return objectFactory.createRules(conflictResolver.getRules());
    }
}



