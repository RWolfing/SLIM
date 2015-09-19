package slim.core;

import java.util.List;
import slim.core.model.User;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;

/**
 * Class to test the functionality of the user model and all connected service
 * write & reads
 * 
 * @author Robert Wolfinger
 */
public class UserTest extends BaseTest {

    private int mTestUserID;

    @Before
    public void setupDummyUser() {
        /**
         * Create dummy user
         */
        String nickName = getRandomName();
        String userName = getRandomName();
        String userLastname = getRandomName();
        String userAbout = getRandomName();
        User createdUser = mSlimService.createUser(nickName, userName, userLastname, 0, userAbout, "www.test.de");
        mTestUserID = createdUser.getID();
    }

    @Test
    public void createTestUser() {

        /**
         * Create & test user
         */
        String nickName = getRandomName();
        String userName = getRandomName();
        String userLastname = getRandomName();
        long userBirthday = 0;
        String userAbout = getRandomName();
        String imageUrl = "www.test.de";
        User createdUser = mSlimService.createUser(nickName, nickName, userName, userBirthday, userAbout, imageUrl);
        assertThat(createdUser, notNullValue());

        /**
         * Compare saved values
         */
        User fetchedUser = mSlimService.getUserById(createdUser.getID());
        assertThat(fetchedUser.getNickName(), is(equalTo(createdUser.getNickName())));
        assertThat(fetchedUser.getFirstName(), is(equalTo(createdUser.getFirstName())));
        assertThat(fetchedUser.getLastName(), is(equalTo(createdUser.getLastName())));
        assertThat(fetchedUser.getAbout(), is(equalTo(createdUser.getAbout())));
        assertThat(fetchedUser.getBirthday(), is((createdUser.getBirthday())));
        assertThat(fetchedUser.getImageUrl(), is(equalTo(createdUser.getImageUrl())));
    }

    @Test
    public void updateUserTest() {

        /**
         * Create user
         */
        String nickName = getRandomName();
        String userName = getRandomName();
        String userLastname = getRandomName();
        long userBirthday = 0;
        String userAbout = getRandomName();
        String imageUrl = "www.test.de";
        User createdUser = mSlimService.createUser(nickName, nickName, userName, userBirthday, userAbout, imageUrl);
        assertThat(createdUser, notNullValue());

        /**
         * Update user fields with new values
         */
        String newNickName = getRandomName();
        String newUserName = getRandomName();
        String newUserLastName = getRandomName();
        long newUserBday = 100;
        String newUserAbout = getRandomName();
        String newImageUrl = "www.test2.de";

        User user = mSlimDatabase.getUserById(createdUser.getID());
        assertThat(user, notNullValue());

        user.setNickName(newNickName);
        user.setFirstName(newUserName);
        user.setLastName(newUserLastName);
        user.setBirthday(newUserBday);
        user.setAbout(newUserAbout);
        user.setImageUrl(newImageUrl);

        boolean success = mSlimService.updateUser(user);
        assertThat(success, is(true));

        //Retrieve the user and compare fields
        user = mSlimDatabase.getUserById(createdUser.getID());
        assertThat(newNickName, is(equalTo(user.getNickName())));
        assertThat(newUserName, is(equalTo(user.getFirstName())));
        assertThat(newUserLastName, is(equalTo(user.getLastName())));
        assertThat(newUserAbout, is(equalTo(user.getAbout())));
        assertThat(newUserBday, is((user.getBirthday())));
        assertThat(newImageUrl, is(equalTo(user.getImageUrl())));
    }

    @Test
    public void deleteExistingUserByIdTest() {
        User user = mSlimDatabase.getUserById(mTestUserID);
        assertThat(user, notNullValue());
        //Delete the testuser
        mSlimService.deleteUser(user.getID());
        //Check if it really was deleted
        user = mSlimService.getUserById(user.getID());
        assertThat(user, is(nullValue()));
    }

    public void getAllUsersTest(){
        List<User> users = mSlimDatabase.getAllUsers();
        assertThat(users, notNullValue());
        //We should have one user in the database
        assertThat(users.size(), is(1));
    }
}
