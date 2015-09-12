package slim.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 * @author Robert
 */
@Path("locations")
public interface LocationResource {

    /**
     * Creates a new {@link Location} with the given parameters
     * 
     * @param uriInfo uriInfo
     * @param name name of the location
     * @param lattitude lattitude of the location
     * @param longitude longitude of the location
     * @return 201 CREATED if the location was created, Else the appropriate
     * status code
     */
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response createLocation(@Context UriInfo uriInfo,
            @FormParam("name") String name,
            @FormParam("lattitude") long lattitude,
            @FormParam("longitude") long longitude);

    /**
     * Updates a {@link Location} with the given id to the given parameters
     * 
     * @param locationId id of the location
     * @param name new name of the location
     * @return 200 OK If the location was updated, 404 If the location was not found, 
     * Else the appropriate status code
     */
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response updateLocation(
            @PathParam("id") int locationId,
            @QueryParam("name") @DefaultValue("") String name);

    /**
     * Deletes the {@link Location} with the given id
     * 
     * @param id id of the location
     * @return 200 OK If the location was deleted, Else the appropriate status code
     */
    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response deleteLocation(@PathParam("id") int id);

    /**
     * Retrieves a {@link Location} with the given id
     * 
     * @param id id of the location
     * @return 200 OK If the location was retrieved, 404 If the location was not found
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response fetchLocationById(@PathParam("id") int id);

    /**
     * Retrieves a {@link Location} with the given lattitude & longitude
     * 
     * @param lattitude lattitude of the location
     * @param longitude longitude of the location
     * @return 200 OK If the location was retrieved, 404 If the location was not found
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response fetchLocationByLongLat(
            @QueryParam("lattitude") long lattitude,
            @QueryParam("longitude") long longitude);
}
