/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core;

import slim.core.model.User;
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

    private long mTestUserID;

    @Before
    public void setupUser() {
        /**
         * Nutzer erzeugen
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
         * Nutzer erzeugen
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
         * Gespeicherten Nutzer laden & vergleichen
         */
        User fetchedUser = mSlimService.getUserById(createdUser.getID());
        assertThat(fetchedUser.getmNickName(), is(equalTo(createdUser.getmNickName())));
        assertThat(fetchedUser.getmFirstName(), is(equalTo(createdUser.getmFirstName())));
        assertThat(fetchedUser.getmLastName(), is(equalTo(createdUser.getmLastName())));
        assertThat(fetchedUser.getmAbout(), is(equalTo(createdUser.getmAbout())));
        assertThat(fetchedUser.getmBirthday(), is((createdUser.getmBirthday())));
        assertThat(fetchedUser.getmImageUrl(), is(equalTo(createdUser.getmImageUrl())));
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
