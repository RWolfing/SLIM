/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core;

import slim.core.model.User;
import slim.core.model.Event;
import java.io.File;
import slim.core.impl.SlimServiceInMemory;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.After;
import org.junit.Before;
import slim.core.model.Location;

/**
 *
 * @author Robert
 */
public abstract class BaseTest {

    private static final String CLASS_NAME = BaseTest.class.getName();
    SlimService mSlimService;
    MockSlimDB mSlimDatabase;

    /**
     * Prepare the initial setup before testing
     */
    @Before
    public void buildService() {
        mSlimService = new SlimServiceInMemory();
        mSlimDatabase = new MockSlimDB();
    }

    /**
     * Tear down the created setup
     */
    @After
    public void tearDownService() {
        // Cleanup the mock database
        mSlimDatabase.closeConnection();
        File file = new File(System.getProperty("user.dir") + "/" + MockSlimDB.MOCK_DB_NAME);
        if (file.exists()) {
            file.delete();
        } else {
            System.err.println(CLASS_NAME + " could not delete mockup database. File does not exist!");
        }

    }

    public User createRandomUser(SlimService slimService) {
        return createUser(slimService, RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1));
    }

    public User createUser(SlimService slimService, String userName) {
        User user = new User("TestNickName", userName, "TestLastName", "21.3.1993", "Ich bin ein Testuser");
        slimService.createUser(user);
        return user;
    }

    public Event createRandomEvent(SlimService slimService) {
        String eventName = getRandomName();
        Location eventLocation = new Location(RandomUtils.nextLong(), RandomUtils.nextLong());
        String eventBegin = getRandomDateString();
        String eventEnd = getRandomDateString();
        String description = getRandomName();
        List<User> guestList = getRandomUserList(slimService, 3);
        User organizer = createRandomUser(slimService);

        return new Event(eventName, eventLocation, eventBegin, eventEnd, description, guestList, organizer);
    }

    protected String getRandomName() {
        return RandomStringUtils.randomAlphanumeric(RandomUtils.nextInt(10) + 1);
    }

    protected String getRandomUUIDString() {
        return String.valueOf(UUID.randomUUID());
    }

    protected String getRandomDateString() {
        Date date = new Date(Math.abs(System.currentTimeMillis() - RandomUtils.nextLong()));
        return date.toString();
    }

    protected List<User> getRandomUserList(SlimService slimService, int count) {
        List<User> userList = new ArrayList();
        for (int i = 0; i <= count; i++) {
            userList.add(createRandomUser(slimService));
        }
        return userList;
    }
}
