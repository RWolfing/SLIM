/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.rest;

import java.util.List;
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
public interface EventRessource {

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @POST
    @Produces({MediaType.APPLICATION_XML})
    Response createEvent(@Context UriInfo uriInfo,
            @FormParam("name") String name,
            @FormParam("lattitude") long lattitude,
            @FormParam("longitude") long longitude,
            @FormParam("eventbegin") long eventBegin,
            @FormParam("eventend") long eventEnd,
            @FormParam("description") String description,
            @FormParam("organizerid") int idOrganizer
    );

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    Response updateEvent(@Context UriInfo uriInfo,
            @PathParam("id") int eventId,
            @FormParam("name") String name,
            @FormParam("eventbegin") long eventBegin,
            @FormParam("eventend") long eventEnd,
            @FormParam("description") String description
    );

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    Response deleteEvent(@PathParam("id") int id);

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    Response fetchEventById(@PathParam("id") int id);

    @GET
    @Produces({MediaType.APPLICATION_XML})
    Response fetchAllEvents();

    @GET
    @Path("withuser/{id}")
    @Produces({MediaType.APPLICATION_XML})
    Response getEventsWithUser(@PathParam("id") int userId);

    @GET
    @Path("fromuser/{id}")
    @Produces({MediaType.APPLICATION_XML})
    Response getEventsFromUser(@PathParam("id") int userId);

    @GET
    @Path("location/")
    @Produces({MediaType.APPLICATION_XML})
    Response getEventsWithinLocation(
            @QueryParam("lattfrom") long lattitudeFrom,
            @QueryParam("lattto") long lattiudeTo,
            @QueryParam("longfrom") long longitudeFrom,
            @QueryParam("longto") long longitudeTo);
    
    @GET
    @Path("guestrange/")
    @Produces({MediaType.APPLICATION_XML})
    Response getEventsWithinGuestRange(
            @QueryParam("minguests") int minGuests,
            @QueryParam("maxguests") int maxGuests);
    
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @POST
    @Path("addguest/{id}")
    @Produces({MediaType.APPLICATION_XML})
    Response addGuestToEvent(@Context UriInfo uriInfo,
            @PathParam("eventId") int eventId,
            @QueryParam("userId") int userId);
    
    @POST
    @Path("addguests/{eventId}")
    @Produces({MediaType.APPLICATION_XML})
    Response addGuestsToEvent(@Context UriInfo uriInfo,
            @PathParam("eventId") int eventId,
            @QueryParam("userIds") final List<Integer> userids);
    
    @DELETE
    @Path("removeguest/{eventId}")
    @Produces({MediaType.APPLICATION_XML})
    Response removeGuestFromEvent(@Context UriInfo uriInfo,
            @PathParam("eventId") int eventId,
            @QueryParam("userId") int userid);
    
    @DELETE
    @Path("removeguests/{eventId}")
    @Produces({MediaType.APPLICATION_XML})
    Response removeGuestsFromEvent(@Context UriInfo uriInfo,
            @PathParam("eventId") int eventId,
            @QueryParam("userIds") final List<Integer> userids);
}
