package rest.resources;

import io.swagger.annotations.*;
import ofar.generated.classes.conflicts.Anomalies;
import ofar.generated.classes.conflicts.AnomalyNames;
import ofar.generated.classes.conflicts.AnomalyType;
import ofar.generated.classes.input.ServiceInput;
import ofar.generated.classes.rules.ObjectFactory;
import ofar.generated.classes.rules.RuleType;
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
import java.util.logging.Level;
import java.util.logging.Logger;


@Path("/optimizer")
@Api(value = "/optimizer")
public class OptimizerResource {
    Logger logger = Logger.getLogger(OptimizerResource.class.getName());

    @POST
    @ApiOperation(value = "createoptimizer", notes = "create a new optimizer that takes ServiceInput", response = ServiceInput.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "OK", response = ServiceInput.class),
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createOptimizer(ServiceInput serviceInput, @Context UriInfo uriInfo) {
        if (serviceInput == null) {
            throw new ForbiddenException("Empty Input");
        }
        logger.log(Level.INFO, "Creating a new ServiceInput");
        logger.log(Level.INFO, "Saving the new resource into the Database");
        ServiceInput savedInput = Database.insertEntry(serviceInput);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        URI u = builder.path(Long.toString(savedInput.getId())).build();
        logger.log(Level.INFO, "Returning the response object   ");
        return Response.created(u).entity(savedInput).build();
    }

    @GET
    @ApiOperation(value = "getAllServiceInput", notes = "read all service input resources"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ServiceInput.class),
    })
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Collection<ServiceInput> getAllServiceInput() {
        return Database.dbHashMap.values();
    }

    @DELETE
    @Path("{id}")
    @ApiOperation(value = "deleteOptimizer", notes = "delete a single optimizer"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "No content"),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public synchronized ServiceInput deleteOptimizer(@ApiParam("The id of the optimizer") @PathParam("id") int id) {
        return Database.dbHashMap.remove(id);
    }

    @GET
    @ApiOperation(value = "getSingleServiceInput", notes = "Get a single ServiceInput resource after doing some actions " +
            "based on the QueryParam resolutionType.\nPossible values are:\n1-\"solveIrrelevance\"\n " +
            "2-\"solveDuplication\"\n 3-\"solveShadowingRedundancy\"\n 3-\"solveSub-optimization\"\n" +
            "4-\"\" which is the default value if you didn't send a QueryParam. if you used this case " +
            "you will get the original ServiceInput resource without any edits "
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = ServiceInput.class),
            @ApiResponse(code = 404, message = "Not Found"),

    })
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public ServiceInput getSingleServiceInput(
            @ApiParam("The id of the optimizer") @PathParam("id") int id,
            @ApiParam("The keyword to be used for selecting an action to be performed on the ServiceInput resource by default no action is done")
            @QueryParam("resolutionType")
            @DefaultValue("") String requestedTypeOfResolution) {
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
                logger.log(Level.INFO, "Get request received");
                logger.log(Level.INFO, "Getting Requested item");
                if (Database.getEntry(id) == null)
                    throw new NotFoundException();
                else
                    return Database.getEntry(id);
            }
        }
    }

    private ServiceInput solveSubOptimizationAnomalies(int id) {
        ofar.generated.classes.input.ObjectFactory serviceInputObjectFactory = new ofar.generated.classes.input.ObjectFactory();
        ConflictResolver conflictResolver;
        if (Database.getEntry(id) == null) {
            throw new NotFoundException();
        } else {
            logger.log(Level.INFO, "Get request received");
            logger.log(Level.INFO, "Performing Sub-Optimization resolution");
            ServiceInput serviceInput = createServiceInput(id);
            if (serviceInput.getAnomaliesList().getAnomaly().size() == 0) {
                logger.log(Level.WARNING, "No Anomalies Found. These rules are already optimized");
                logger.log(Level.INFO, "Returning Rules");
                return serviceInput;
            }
            conflictResolver = new ConflictResolver(serviceInput.getDefectedRules(), serviceInput.getAnomaliesList());
            conflictResolver.removeIrrelevanceAnomaly();
            conflictResolver.removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.DUPLICATION);
            conflictResolver.removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.SHADOWING_REDUNDANCY);
            ServiceInput returnedServiceInput = serviceInputObjectFactory.createServiceInput();
            returnedServiceInput.setId(id);
            returnedServiceInput.setAnomaliesList(conflictResolver.getAnomalies());
            returnedServiceInput.setDefectedRules(conflictResolver.getRules());
            logger.log(Level.INFO, "Returning the list new list of rules and the remaining anomalies");
            return returnedServiceInput;
        }

    }

    private ServiceInput solveIrrelevance(int id) {
        ConflictResolver conflictResolver;
        if (Database.getEntry(id) == null) {
            throw new NotFoundException();
        } else {
            logger.log(Level.INFO, "Get request received");
            logger.log(Level.INFO, "Performing irrelevance resolution");
            ServiceInput serviceInput = createServiceInput(id);
            if (serviceInput.getAnomaliesList().getAnomaly().size() == 0) {
                logger.log(Level.WARNING, "No Anomalies Found. These rules are already optimized");
                logger.log(Level.INFO, "Returning Rules");
                return serviceInput;
            }
            conflictResolver = new ConflictResolver(serviceInput.getDefectedRules(), serviceInput.getAnomaliesList());
            conflictResolver.removeIrrelevanceAnomaly();
            logger.log(Level.INFO, "Irrelevance anomalies removed successfully");
            ServiceInput returnedServiceInput = new ServiceInput();
            returnedServiceInput.setId(id);
            returnedServiceInput.setAnomaliesList(conflictResolver.getAnomalies());
            returnedServiceInput.setDefectedRules(conflictResolver.getRules());
            logger.log(Level.INFO, "Returning the list new list of rules and the remaining anomalies");
            return returnedServiceInput;
        }

    }


    private ServiceInput solveDuplication(int id) {

        ConflictResolver conflictResolver;
        if (Database.getEntry(id) == null) {
            throw new NotFoundException();
        } else {
            logger.log(Level.INFO, "Get request received");
            logger.log(Level.INFO, "Performing duplication resolution");
            ServiceInput serviceInput = createServiceInput(id);
            if (serviceInput.getAnomaliesList().getAnomaly().size() == 0) {
                logger.log(Level.WARNING, "No Anomalies Found. These rules are already optimized");
                logger.log(Level.INFO, "Returning Rules");
                return serviceInput;
            }
            conflictResolver = new ConflictResolver(serviceInput.getDefectedRules(), serviceInput.getAnomaliesList());
            conflictResolver.removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.DUPLICATION);
            logger.log(Level.INFO, "Duplication anomalies removed successfully");
            ServiceInput returnedServiceInput = new ServiceInput();
            returnedServiceInput.setId(id);
            returnedServiceInput.setAnomaliesList(conflictResolver.getAnomalies());
            returnedServiceInput.setDefectedRules(conflictResolver.getRules());
            logger.log(Level.INFO, "Returning the list new list of rules and the remaining anomalies");
            return returnedServiceInput;
        }
    }


    private ServiceInput solveShadowingRedundancy(int id) {
        ConflictResolver conflictResolver;
        if (Database.getEntry(id) == null) {
            throw new NotFoundException();
        } else {
            logger.log(Level.INFO, "Get request received");
            logger.log(Level.INFO, "Performing shadowing redundancy resolution");
            ServiceInput serviceInput = createServiceInput(id);
            if (serviceInput.getAnomaliesList().getAnomaly().size() == 0) {
                logger.log(Level.WARNING, "No Anomalies Found. These rules are already optimized");
                logger.log(Level.INFO, "Returning Rules");
                return serviceInput;
            }
            conflictResolver = new ConflictResolver(serviceInput.getDefectedRules(), serviceInput.getAnomaliesList());
            conflictResolver.removeDuplicationOrShadowingRedundancyAnomaly(AnomalyNames.SHADOWING_REDUNDANCY);
            logger.log(Level.INFO, "Shadowing Redundancy anomalies removed successfully");
            ServiceInput returnedServiceInput = new ServiceInput();
            returnedServiceInput.setId(id);
            returnedServiceInput.setAnomaliesList(conflictResolver.getAnomalies());
            returnedServiceInput.setDefectedRules(conflictResolver.getRules());
            logger.log(Level.INFO, "Returning the list new list of rules and the remaining anomalies");
            return returnedServiceInput;
        }
    }

    private ServiceInput createServiceInput(int id) {
        ofar.generated.classes.input.ObjectFactory serviceInputObjectFactory = new ofar.generated.classes.input.ObjectFactory();
        ServiceInput serviceInput = serviceInputObjectFactory.createServiceInput();
        serviceInput.setId(id);
        serviceInput.setAnomaliesList(cloneAnomalies(Database.getEntry(id).getAnomaliesList()));
        serviceInput.setDefectedRules(cloneRules(Database.getEntry(id).getDefectedRules()));
        return serviceInput;
    }

    private Anomalies cloneAnomalies(Anomalies anomalies) {
        ofar.generated.classes.conflicts.ObjectFactory conflictsObjectFactory = new ofar.generated.classes.conflicts.ObjectFactory();

        Anomalies clonedAnomalies = conflictsObjectFactory.createAnomalies();
        for (AnomalyType anomalyType : anomalies.getAnomaly()) {
            clonedAnomalies.getAnomaly().add(anomalyType);
        }
        return clonedAnomalies;
    }

    private Rules cloneRules(Rules rules) {
        ObjectFactory rulesObjectFactory = new ObjectFactory();
        Rules clonedRules = rulesObjectFactory.createRules();
        for (RuleType ruleType : rules.getRule()) {
            clonedRules.getRule().add(ruleType);
        }
        return clonedRules;
    }

    @PUT
    @Path("{id}")
    @ApiOperation(value = "solveConflicts", notes = "update a single item by applying a solution written by network administrator"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = Rules.class),
            @ApiResponse(code = 400, message = "Bad Request"),
            @ApiResponse(code = 404, message = "Not Found"),
    })
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public JAXBElement<Rules> solveConflicts(SolveRequest solveRequest, @ApiParam("The id of the optimizer") @PathParam("id") int id) throws Exception {
        ObjectFactory objectFactory = new ObjectFactory();
        ConflictResolver conflictResolver;
        UnnecessaryAnomalyChecker unnecessaryAnomalyChecker;
        synchronized (Database.dbHashMap) {
            ServiceInput serviceInput = Database.getEntry(id);
            if (serviceInput == null) {
                throw new NotFoundException();
            }
            if (serviceInput.getAnomaliesList().getAnomaly().size() == 0) {
                logger.log(Level.WARNING, "No Anomalies Found. These rules are already optimized");
                logger.log(Level.INFO, "Returning Rules");
                return objectFactory.createRules(serviceInput.getDefectedRules());
            }
            logger.log(Level.INFO, "Put request received");
            logger.log(Level.INFO, "Getting Data from Database");
            conflictResolver = new ConflictResolver(serviceInput.getDefectedRules(), serviceInput.getAnomaliesList());
            logger.log(Level.INFO, "Resolving all sub-optimization anomalies");
            conflictResolver.resolveAnomalies();
            logger.log(Level.INFO, "Sub-optimization anomalies resolved");

            logger.log(Level.INFO, "Applying solve request");
            conflictResolver.executeSolveRequest(solveRequest);
            logger.log(Level.INFO, "Solve request applied");

            unnecessaryAnomalyChecker = new UnnecessaryAnomalyChecker(conflictResolver.getRules());
            int anomalyListSize = conflictResolver.getAnomalies().getAnomaly().size();
            logger.log(Level.INFO, "Checking Unnecessary Anomalies");
            //conflictResolver.getAnomalies().getAnomaly().get(anomalyListSize - 1).getAnomalyID().intValue() -> here i get the anomaly ID of the last element in the anomalies list
            Anomalies newlyCreatedAnomalies = unnecessaryAnomalyChecker.checkForUnnecessaryAnomalies(conflictResolver.getAnomalies().getAnomaly().get(anomalyListSize - 1).getAnomalyID().intValue());
            if (newlyCreatedAnomalies.getAnomaly().size() == 0) {
                logger.log(Level.INFO, "Unnecessary Anomalies Detected");

            } else {
                logger.log(Level.INFO, "No unnecessary anomalies found");

            }

            //Add the newly created anomalies the list of the anomalies to be solved
            conflictResolver.getAnomalies().getAnomaly().addAll(newlyCreatedAnomalies.getAnomaly());
            logger.log(Level.INFO, "Resolving unnecessary anomalies");
            //Resolve the unnecessary anomalies
            conflictResolver.removeUnnecessaryAnomaly();
            logger.log(Level.INFO, "Updating Database...");

            //Update the database with the new list of rules and anomalies
            serviceInput.setDefectedRules(conflictResolver.getRules());
            serviceInput.setAnomaliesList(new Anomalies());

        }
        logger.log(Level.INFO, "Returning the updated list of rules");
        return objectFactory.createRules(conflictResolver.getRules());
    }
}



