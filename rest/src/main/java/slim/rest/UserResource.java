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
import slim.core.model.User;

/**
 * @author Robert Wolfinger
 */
@Path("users")
public interface UserResource {

    /**
     * Creates a {@link User} with the given parameters
     *
     * @param uriInfo uriInfo
     * @param nickName nickname of the user
     * @param firstName firstname of the user
     * @param lastName lastname of the user
     * @param birthday birthday of the user in timemillis
     * @param about about of the user
     * @param imageUrl imageurl of the user
     * @return 201 CREATED If the user was created, 400 If there were missing
     * invalid parameter, Else the appropriate status code
     */
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @POST
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response createUser(@Context UriInfo uriInfo,
            @FormParam("nickname") String nickName,
            @FormParam("firstname") String firstName,
            @FormParam("lastname") String lastName,
            @FormParam("birthday") long birthday,
            @FormParam("about") String about,
            @FormParam("imageurl") String imageUrl);

    /**
     * Updates a {@link User} with the given id
     *
     * @param userId id of the user
     * @param nickName new nickname of the user
     * @param birthday new birthday of the user
     * @param about new about of the user
     * @param imageUrl new imageurl of the user
     * @return 200 OK If the user was updated, 404 If the user was not found,
     * Else the appropriate status code
     */
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @PUT
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response updateUser(
            @PathParam("id") int userId,
            @QueryParam("nickname") @DefaultValue("") String nickName,
            @QueryParam("birthday") long birthday,
            @QueryParam("about") @DefaultValue("") String about,
            @QueryParam("imageurl") @DefaultValue("") String imageUrl);

    /**
     * Deletes a {@link User} with the given id
     *
     * @param id id of the user
     * @return 200 OK If the user was deleted, 404 If the user was not found,
     * Else the appropriate status code
     */
    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response deleteUser(@PathParam("id") int id);

    /**
     * Fetches a {@link User} with the given id
     *
     * @param id id of the user
     * @return 200 OK If the user was retrieved, 404 If the user was not found,
     * Else the appropriate status code
     */
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response fetchUserById(@PathParam("id") int id);

    /**
     * Retrieves a list of all {@link User}s
     *
     * @return 200 OK If the users were retrieved, 404 If no users exist
     */
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response fetchAllUsers();

    /**
     * Check if the given {@link User}s are in one or more events together
     *
     * @param idUser1 id of one user
     * @param idUser2 id of another user
     * @return 200 OK if the result is true, 404 if the result is false
     * code
     */
    @GET
    @Path("{idyou}/{idhim}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response doesHePartyWithMe(@PathParam("idyou") int idUser1, @PathParam("idhim") int idUser2);
}
