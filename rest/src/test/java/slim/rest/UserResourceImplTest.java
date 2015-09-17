/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.rest;

import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Before;
import slim.rest.impl.UserResourceImpl;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;

import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import slim.core.model.User;
import slim.core.model.UserList;

/**
 *
 * @author Robert
 */
public class UserResourceImplTest extends RestBaseTest {

    private UserResourceImpl mUserResource;
    private UriInfo mUriInfo;

    @Before
    public void prepareResourcesTest() {
        mUserResource = new UserResourceImpl();
        mUserResource.setSlimService(mSlimService);
    }

    @Before
    public void mockUriInfo() {
        mUriInfo = mock(UriInfo.class);
        final UriBuilder fromResource = UriBuilder.fromResource(UserResourceImpl.class);
        when(mUriInfo.getAbsolutePathBuilder()).thenAnswer((InvocationOnMock invocation) -> fromResource);
    }

    @Test
    public void createUserSuccess() {
        Response response = mUserResource.createUser(mUriInfo, "TestNickname", "TestFirstName", "TestLastName", 500000, "TestDescription", "www.test.de");
        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));

        User user = (User) response.getEntity();
        response = mUserResource.fetchUserById(user.getID());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        assertThat(user.getID(), is(((User) response.getEntity()).getID()));
    }

    @Test
    public void createUserFails() {
        Response response = mUserResource.createUser(mUriInfo, null, null, null, 500000, "TestDescription", "www.test.de");
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    @Test
    public void fetchUserById() {
        //Existing
        Response response = mUserResource.fetchUserById(mTestUser.getID());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        assertThat(mTestUser.getID(), is(((User) response.getEntity()).getID()));

        //Bad id
        response = mUserResource.fetchUserById(-10);
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));

        //Not existing user
        response = mUserResource.fetchUserById(100);
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    public void fetchAllUsers() {
        Response response = mUserResource.fetchAllUsers();
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        List<User> result = ((UserList) response.getEntity()).getUsers();
        assertThat(result.size(), is(SUM_USERS));
    }

    @Test
    public void updateUser() {
        //Test success
        Response response = mUserResource.updateUser(mTestUser.getID(), "maxi", mTestUser.getBirthday(), "new description", mTestUser.getImageUrl());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        User user = (User) response.getEntity();
        assertThat(user.getID(), is(mTestUser.getID()));
        assertThat(user.getNickName(), is("maxi"));
        assertThat(user.getAbout(), is("new description"));

        //Test failure bad id
        response = mUserResource.updateUser(-100, "maxi", mTestUser.getBirthday(), "new description", mTestUser.getImageUrl());
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));

        //Test failure not found
        response = mUserResource.updateUser(100, "maxi", mTestUser.getBirthday(), "new description", mTestUser.getImageUrl());
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    public void doesHePartyWithMe() {
        //Anna parties with gustav
        Response response = mUserResource.doesHePartyWithMe(mPartyFriend1.getID(), mPartyFriend2.getID());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        
        //Anna does not party with max
        response = mUserResource.doesHePartyWithMe(mPartyFriend1.getID(), mTestUser.getID());
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
        
        //Failure invalid ids
        response = mUserResource.doesHePartyWithMe(-100, mTestUser.getID());
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    @Test
    public void deleteUser() {
        //Test success
        Response response = mUserResource.deleteUser(mTestUser.getID());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        //Test bad id
        response = mUserResource.deleteUser(-100);
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));

        //Test not existing
        response = mUserResource.deleteUser(mTestUser.getID());
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }
}
