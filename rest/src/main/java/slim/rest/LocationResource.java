/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Robert
 */
@Path("locations")
public interface LocationResource {

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response createLocation(@Context UriInfo uriInfo,
            @FormParam("name") String name,
            @FormParam("lattitude") long lattitude,
            @FormParam("longitude") long longitude);

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response updateLocation(
            @PathParam("id") int locationId,
            @QueryParam("name") @DefaultValue("") String name);

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    Response deleteLocation(@PathParam("id") int id);

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    Response fetchLocationById(@PathParam("id") int id);

    @GET
    @Produces({MediaType.APPLICATION_XML})
    Response fetchLocationByLongLat(
            @QueryParam("lattitude") long lattitude,
            @QueryParam("longitude") long longitude);

    //TODO fetch all
}
