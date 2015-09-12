package slim.core;

import java.util.List;
import slim.core.model.User;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;

/**
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
        mTestUserID = createdUser.getmID();
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
        User fetchedUser = mSlimService.getUserById(createdUser.getmID());
        assertThat(fetchedUser.getmNickName(), is(equalTo(createdUser.getmNickName())));
        assertThat(fetchedUser.getmFirstName(), is(equalTo(createdUser.getmFirstName())));
        assertThat(fetchedUser.getmLastName(), is(equalTo(createdUser.getmLastName())));
        assertThat(fetchedUser.getmAbout(), is(equalTo(createdUser.getmAbout())));
        assertThat(fetchedUser.getmBirthday(), is((createdUser.getmBirthday())));
        assertThat(fetchedUser.getmImageUrl(), is(equalTo(createdUser.getmImageUrl())));
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

        User user = mSlimDatabase.getUserById(createdUser.getmID());
        assertThat(user, notNullValue());

        user.setmNickName(newNickName);
        user.setmFirstName(newUserName);
        user.setmLastName(newUserLastName);
        user.setmBirthday(newUserBday);
        user.setmAbout(newUserAbout);
        user.setmImageUrl(newImageUrl);

        boolean success = mSlimService.updateUser(user);
        assertThat(success, is(true));

        //Retrieve the user and compare fields
        user = mSlimDatabase.getUserById(user.getmID());
        assertThat(newNickName, is(equalTo(user.getmNickName())));
        assertThat(newUserName, is(equalTo(user.getmFirstName())));
        assertThat(newUserLastName, is(equalTo(user.getmLastName())));
        assertThat(newUserAbout, is(equalTo(user.getmAbout())));
        assertThat(newUserBday, is((user.getmBirthday())));
        assertThat(newImageUrl, is(equalTo(user.getmImageUrl())));
    }

    @Test
    public void deleteExistingUserByIdTest() {
        User user = mSlimDatabase.getUserById(mTestUserID);
        assertThat(user, notNullValue());
        //Delete the testuser
        mSlimService.deleteUser(user.getmID());
        //Check if it really was deleted
        user = mSlimService.getUserById(user.getmID());
        assertThat(user, is(nullValue()));
    }

    public void getAllUsersTest(){
        List<User> users = mSlimDatabase.getAllUsers();
        assertThat(users, notNullValue());
        //We should have one user in the database
        assertThat(users.size(), is(1));
    }
}
