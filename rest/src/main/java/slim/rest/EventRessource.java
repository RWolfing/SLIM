/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Robert
 */
@Path("events")
public interface EventRessource {

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @POST
    @Produces({MediaType.APPLICATION_XML})
    Response createEvent(@Context UriInfo uriInfo,
            @FormParam("name") String name,
            @FormParam("lattitude") long lattitude,
            @FormParam("longitude") String longitude,
            @FormParam("eventbegin") long eventBegin,
            @FormParam("eventend") long eventEnd,
            @FormParam("description") String description,
            @FormParam("organizerid") int idOrganizer
    );

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @PUT
    @Produces({MediaType.APPLICATION_XML})
    Response updateEvent(@Context UriInfo uriInfo,
            @PathParam("id") int eventId,
            @FormParam("name") String name,
            @FormParam("lattitude") long lattitude,
            @FormParam("longitude") String longitude,
            @FormParam("eventbegin") long eventBegin,
            @FormParam("eventend") long eventEnd,
            @FormParam("description") String description
    );

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    Response deleteEvent(int id);

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    Response fetchEventById(int id);

    @GET
    @Produces({MediaType.APPLICATION_XML})
    Response fetchAllEvents();

    @GET
    @Path("withuser/{id}")
    @Produces({MediaType.APPLICATION_XML})
    Response getEventsWithUser(int userId);

    @GET
    @Path("fromuser/{id}")
    @Produces({MediaType.APPLICATION_XML})
    Response getEventsFromUser(int userId);

    @GET
    @Produces({MediaType.APPLICATION_XML})
    Response getEventsWithinLocation(
            @PathParam("lattfrom") long lattitudeFrom,
            @PathParam("lattto") long lattiudeTo,
            @PathParam("longfrom") long longitudeFrom,
            @PathParam("longto") long longitudeTo);
    
    @GET
    @Produces({MediaType.APPLICATION_XML})
    Response getEventsWithinRange(@PathParam("minguests") int minGuests, @PathParam("maxguests") int maxGuests);
}
