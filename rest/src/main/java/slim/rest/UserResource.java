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
public interface UserResource {

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @POST
    @Produces({MediaType.APPLICATION_XML})
    Response createUser(@Context UriInfo uriInfo,
            @FormParam("nickname") String nickName,
            @FormParam("firstname") String firstName,
            @FormParam("lastname") String lastName,
            @FormParam("birthday") long birthday,
            @FormParam("about") String about,
            @FormParam("imageurl") String imageUrl);

    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @PUT
    @Path("id")
    @Produces({MediaType.APPLICATION_XML})
    Response updateUser(
            @PathParam("id") int userId,
            @FormParam("nickname") String nickName,
            @FormParam("birthday") long birthday,
            @FormParam("about") String about,
            @FormParam("imageurl") String imageUrl);

    @DELETE
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    Response deleteUser(int id);

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML})
    Response fetchUserById(int id);

    @GET
    @Produces({MediaType.APPLICATION_XML})
    Response fetchAllUsers();
    
    @GET
    @Produces({MediaType.APPLICATION_XML})
    Response doesHePartyWithMe(@PathParam("idyou") int idUser1, @PathParam("idhim") int idUser2);
}
