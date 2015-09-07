/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core;

import slim.core.model.User;
import java.util.List;
import junit.framework.Assert;
import static org.hamcrest.CoreMatchers.*;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;

/**
 *
 * @author Robert
 */
public class UserTest extends BaseTest {

    private String mTestUserID;

    @Before
    public void setupUser() {
        /**
         * Nutzer erzeugen
         */
        String nickName = getRandomName();
        String userName = getRandomName();
        String userLastname = getRandomName();
        String userBirthday = getRandomDateString();
        String userAbout = getRandomName();
        User user = new User(nickName, userName, userLastname, userBirthday, userAbout);
        mSlimDatabase.saveUser(user);
    }

    @Test
    public void createTestUser() {

        /**
         * Nutzer erzeugen
         */
        String id = getRandomUUIDString();
        String nickName = getRandomName();
        String userName = getRandomName();
        String userLastname = getRandomName();
        String userBirthday = getRandomDateString();
        String userAbout = getRandomName();
        User user = new User(nickName, userName, userLastname, userBirthday, userAbout);
        boolean success = mSlimDatabase.saveUser(user);
        assertThat(success, is(true));

        /**
         * Gespeicherten Nutzer laden & vergleichen
         */
        User fetchedUser = mSlimDatabase.getUserById(user.getID());
        assertThat(fetchedUser.getmNickName(), is(equalTo(user.getmNickName())));
        assertThat(fetchedUser.getmFirstName(), is(equalTo(user.getmFirstName())));
        assertThat(fetchedUser.getmLastName(), is(equalTo(user.getmLastName())));
        assertThat(fetchedUser.getmAbout(), is(equalTo(user.getmAbout())));
        assertThat(fetchedUser.getmBirthday(), is(equalTo(user.getmBirthday())));
    }

    public void updateUserTest() {
        User user = mSlimDatabase.getUserById(0);
        Assert.assertNotNull(user);

        boolean success = mSlimDatabase.saveUser(user);
        assertThat(success, is(true));
    }

    /**
     * Nutzer löschen
     */
    public void deleteExistingUser() {
        User user = mSlimDatabase.getUserById(0);
        //löschen
        mSlimDatabase.deleteUserById(user.getID());
        //prüfung
        user = mSlimDatabase.getUserById(user.getID());
        assertThat(user, is(nullValue()));
    }

}
