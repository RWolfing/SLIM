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
import slim.core.SlimService;

/**
 *
 * @author Robert
 */
@Path("users")
public interface UserResource {

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

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response deleteUser(@PathParam("id") int id);

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response fetchUserById(@PathParam("id") int id);

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response fetchAllUsers();
    
    @GET
    @Path("{idyou}/{idhim}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    Response doesHePartyWithMe(@PathParam("idyou") int idUser1, @PathParam("idhim") int idUser2);
    
    void setService(SlimService service);
}
