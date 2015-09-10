/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.rest.impl;

import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import slim.core.SlimService;
import slim.core.model.User;
import slim.rest.UserResource;

/**
 *
 * @author Robert
 */
public class UserResourceImpl extends SlimResource implements UserResource {

    @Override
    public Response createUser(UriInfo uriInfo, String nickName, String firstName, String lastName, long birthday, String about, String imageUrl) {
        if (firstName == null || firstName.equals("") || lastName == null || lastName.equals("")) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        User createdUser = mSlimService.createUser(nickName, firstName, lastName, birthday, about, imageUrl);
        if (createdUser != null) {
            return Response.status(Status.CREATED).entity(createdUser).build();
        } else {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public Response updateUser(int userId, String nickName, long birthday, String about, String imageUrl) {
        if (userId < 0) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        User user = mSlimService.getUserById(userId);
        if (user != null) {
            user.setmNickName(nickName);
            user.setmBirthday(birthday);
            user.setmAbout(about);
            user.setmImageUrl(imageUrl);

            if (mSlimService.updateUser(user)) {
                return Response.status(Status.OK).entity(user).build();
            } else {
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    //TODO not found
    @Override
    public Response deleteUser(int id) {
        if (id < 0) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        if (mSlimService.getUserById(id) != null) {
            mSlimService.deleteUser(id);
            return Response.status(Status.OK).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }

    }

    @Override
    public Response fetchUserById(int id) {
        if (id < 0) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        User user = mSlimService.getUserById(id);
        if (user != null) {
            return Response.status(Status.OK).entity(user).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response fetchAllUsers() {
        List<User> users = mSlimService.getUsers();

        if (users != null && users.size() > 0) {
            return Response.status(Status.OK).entity(users).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response doesHePartyWithMe(int idUser1, int idUser2) {
       if(idUser1 < 0 || idUser2 < 0){
           return Response.status(Status.BAD_REQUEST).build();
       }
       
       if(mSlimService.doesHePartyWithMe(idUser1, idUser2)){
           return Response.status(Status.OK).entity(true).build();
       } else {
           return Response.status(Status.OK).entity(false).build();
       }
    }

    @Override
    public void setService(SlimService service) {
       mSlimService = service;
    }
}
