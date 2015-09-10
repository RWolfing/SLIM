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
import org.mockito.stubbing.Answer;

import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import slim.core.model.User;

/**
 *
 * @author Robert
 */
public class UserResourceImplTest extends RestBaseTest {

    private UserResource mUserResource;
    private UriInfo mUriInfo;

    @Before
    public void prepareResourcesTest() {
        mUserResource = new UserResourceImpl();
        mUserResource.setService(mSlimService);
    }

    @Before
    public void mockUriInfo() {
        mUriInfo = mock(UriInfo.class);
        final UriBuilder fromResource = UriBuilder.fromResource(UserResource.class);
        when(mUriInfo.getAbsolutePathBuilder()).thenAnswer(new Answer<UriBuilder>() {

            @Override
            public UriBuilder answer(InvocationOnMock invocation) throws Throwable {
                return fromResource;
            }
        });
    }

    @Test
    public void createUserSuccess() {
        Response response = mUserResource.createUser(mUriInfo, "TestNickname", "TestFirstName", "TestLastName", 500000, "TestDescription", "www.test.de");
        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));

        User user = (User) response.getEntity();
        response = mUserResource.fetchUserById(user.getmID());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        assertThat(user.getmID(), is(((User) response.getEntity()).getmID()));
    }

    @Test
    public void createUserFails() {
        Response response = mUserResource.createUser(mUriInfo, null, null, null, 500000, "TestDescription", "www.test.de");
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    @Test
    public void fetchUserById() {
        //Existing
        Response response = mUserResource.fetchUserById(mTestUser.getmID());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        assertThat(mTestUser.getmID(), is(((User) response.getEntity()).getmID()));

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
        List<User> result = (List<User>) response.getEntity();
        assertThat(result.size(), is(SUM_USERS));
    }

    @Test
    public void updateUser() {
        //Test success
        Response response = mUserResource.updateUser(mTestUser.getmID(), "maxi", mTestUser.getmBirthday(), "new description", mTestUser.getmImageUrl());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        User user = (User) response.getEntity();
        assertThat(user.getmID(), is(mTestUser.getmID()));
        assertThat(user.getmNickName(), is("maxi"));
        assertThat(user.getmAbout(), is("new description"));

        //Test failure bad id
        response = mUserResource.updateUser(-100, "maxi", mTestUser.getmBirthday(), "new description", mTestUser.getmImageUrl());
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));

        //Test failure not found
        response = mUserResource.updateUser(100, "maxi", mTestUser.getmBirthday(), "new description", mTestUser.getmImageUrl());
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    public void doesHePartyWithMe() {
        //Anna parties with gustav
        Response response = mUserResource.doesHePartyWithMe(mPartyFriend1.getmID(), mPartyFriend2.getmID());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        assertThat(response.getEntity(), is(true));
        
        //Anna does not party with max
        response = mUserResource.doesHePartyWithMe(mPartyFriend1.getmID(), mTestUser.getmID());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        assertThat(response.getEntity(), is(false));
        
        //Failure invalid ids
        response = mUserResource.doesHePartyWithMe(-100, mTestUser.getmID());
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    @Test
    public void deleteUser() {
        //Test success
        Response response = mUserResource.deleteUser(mTestUser.getmID());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        //Test bad id
        response = mUserResource.deleteUser(-100);
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));

        //Test not existing
        response = mUserResource.deleteUser(mTestUser.getmID());
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }
}
