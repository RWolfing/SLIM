package slim.rest.impl;

import java.util.List;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import slim.core.model.User;
import slim.core.model.UserList;
import slim.rest.UserResource;

/**
 * Implementation of the {@link UserResource}
 * 
 * @author Robert
 */
@Path("users")
public class UserResourceImpl extends SlimResource implements UserResource {

    @Override
    public Response createUser(UriInfo uriInfo, String nickName, String firstName, String lastName, long birthday, String about, String imageUrl) {
        if (firstName == null || firstName.equals("") || lastName == null || lastName.equals("")) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        //Create user
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

        //Retrieve user
        User user = mSlimService.getUserById(userId);
        //Does user exist
        if (user != null) {
            user.setNickName(nickName);
            user.setBirthday(birthday);
            user.setAbout(about);
            user.setImageUrl(imageUrl);

            //Update user
            if (mSlimService.updateUser(user)) {
                return Response.status(Status.OK).entity(user).build();
            } else {
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response deleteUser(int id) {
        if (id < 0) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        //Fetch user
        if (mSlimService.getUserById(id) != null) {
            //Delete user
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
        //Fetch user
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

        //Check if there are multiple users
        if (users != null && users.size() > 0) {
            UserList userList = new UserList();
            userList.setUsers(users);
            return Response.status(Status.OK).entity(userList).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response doesHePartyWithMe(int idUser1, int idUser2) {
        if (idUser1 < 0 || idUser2 < 0) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        if (mSlimService.doesHePartyWithMe(idUser1, idUser2)) {
            return Response.status(Status.OK).entity(true).build();
        } else {
            return Response.status(Status.OK).entity(false).build();
        }
    }
}
