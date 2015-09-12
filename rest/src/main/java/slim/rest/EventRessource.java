package slim.rest;

import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
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
 * @author Robert Wolfinger
 */
@Path("events")
public interface EventRessource {

    /**
     * Creates a new {@link Event] with the given parameters.
     *
     * @param uriInfo uruInfo
     * @param name name of the event
     * @param lattitude lattitude of the event
     * @param longitude longitude of the event
     * @param eventBegin begin in timemillis of the event
     * @param eventEnd end in timemillis of the event
     * @param description description of the event
     * @param idOrganizer id of the user that organizes that event
     * @return 201 Created if the user was created 404 If the organizer was not
     * found Else appropriate status code
     */
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response createEvent(@Context UriInfo uriInfo,
            @FormParam("name") String name,
            @FormParam("lattitude") long lattitude,
            @FormParam("longitude") long longitude,
            @FormParam("eventbegin") long eventBegin,
            @FormParam("eventend") long eventEnd,
            @FormParam("description") String description,
            @FormParam("organizerid") int idOrganizer
    );

    /**
     * Updates a {@link Event] with the given values
     *
     * @param uriInfo uriInfo
     * @param eventId id of the event to update
     * @param name new name of the event
     * @param eventBegin new time of the eventbegin
     * @param eventEnd new time of the eventend
     * @param description description of the event
     * @return 200 OK if the event was updated, 404 If the event was not found,
     * Else appropriate status code
     */
    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response updateEvent(@Context UriInfo uriInfo,
            @PathParam("id") int eventId,
            @QueryParam("name") String name,
            @QueryParam("eventbegin") long eventBegin,
            @QueryParam("eventend") long eventEnd,
            @QueryParam("description") String description
    );

    /**
     * Deletes the {@link Event} with the given id
     *
     * @param id id of the event
     * @return 200 OK if the user was deleted Else appropriate status code
     */
    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response deleteEvent(@PathParam("id") int id);

    /**
     * Fetches the {@link Event} with the given id
     *
     * @param id id of the event
     * @return 200 OK if the event was found, 404 If the event was not found,
     * Else appropriate status code
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response fetchEventById(@PathParam("id") int id);

    /**
     * Fetches all events
     *
     * @return 200 OK If events were found, 404 If no events were found
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response fetchAllEvents();

    /**
     * Fetches event where the {@link User} with the given id is a guest
     *
     * @param userId id of the user
     * @return 200 OK If events were found, 404 If no events were found
     */
    @GET
    @Path("withuser/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response getEventsWithUser(@PathParam("id") int userId);

    /**
     * Fetches event where the {@link User} with the given id is the organizer
     *
     * @param userId id of the user
     * @return 200 OK If events were found, 404 If no events were found
     */
    @GET
    @Path("fromuser/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response getEventsFromUser(@PathParam("id") int userId);

    /**
     * Retrieves all {@link Event]s where the {@link Location} fits the given bounds
     *
     * @param lattitudeFrom min latttitude
     * @param lattiudeTo max lattitude
     * @param longitudeFrom min longitude
     * @param longitudeTo max longitude
     * @return 200 OK If events were found, 404 If no events were found
     */
    @GET
    @Path("location/")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response getEventsWithinLocation(
            @QueryParam("lattfrom") long lattitudeFrom,
            @QueryParam("lattto") long lattiudeTo,
            @QueryParam("longfrom") long longitudeFrom,
            @QueryParam("longto") long longitudeTo);

    /**
     * Retrieves all {@link Event}s where the size of the guestlist fits the
     * given range
     *
     * @param minGuests min guest count
     * @param maxGuests max guest count
     * @return 200 OK If events were found, 404 If no events were found
     */
    @GET
    @Path("guestrange/")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response getEventsWithinGuestRange(
            @QueryParam("minguests") int minGuests,
            @QueryParam("maxguests") int maxGuests);

    /**
     * Adds a {@link User} with the given id as a guest to the {@link Event}
     * with the given id
     *
     * @param uriInfo uriInfo
     * @param eventId id of the event
     * @param userId id of the user
     * @return 200 OK If the user was added, 404 If the user or event was not
     * found, Else the appropriate status code
     */
    @PUT
    @Path("addguest/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response addGuestToEvent(@Context UriInfo uriInfo,
            @PathParam("eventId") int eventId,
            @QueryParam("userId") int userId);

    /**
     * Adds a list of {@link User}s with the given ids to the {@link Event} with
     * the given id
     *
     * @param uriInfo uriInfo
     * @param eventId id of the event
     * @param userids ids of the users
     * @return 200 OK If the users were added, 404 If the event was not found,
     * Else the appropriate status code
     */
    @PUT
    @Path("addguests/{eventId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response addGuestsToEvent(@Context UriInfo uriInfo,
            @PathParam("eventId") int eventId,
            @QueryParam("userIds") final List<Integer> userids);

    /**
     * Removes the {@link User} with the given id from the {@link Event} with
     * the given id
     *
     * @param uriInfo uriInfo
     * @param eventId id of the event
     * @param userid id of the user
     * @return 200 OK If the user was removed, 404 If the event or user was not
     * found, Else the appropriate status code
     */
    @DELETE
    @Path("removeguest/{eventId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response removeGuestFromEvent(@Context UriInfo uriInfo,
            @PathParam("eventId") int eventId,
            @QueryParam("userId") int userid);

    /**
     * Removes a list of {@link User}s with the given ids from the {@link Event}
     * with the given id
     *
     * @param uriInfo uriInfo
     * @param eventId id of the event
     * @param userids ids of the users
     * @return 200 OK If the users were removed, 404 If the event was not found,
     * Else the appropriate status code
     */
    @DELETE
    @Path("removeguests/{eventId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response removeGuestsFromEvent(@Context UriInfo uriInfo,
            @PathParam("eventId") int eventId,
            @QueryParam("userIds") final List<Integer> userids);
}
