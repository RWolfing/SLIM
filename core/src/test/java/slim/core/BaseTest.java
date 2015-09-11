/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import slim.core.impl.SlimServiceInMemory;
import java.util.Date;
import java.util.UUID;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.After;
import org.junit.Before;
import slim.core.impl.SlimDBServiceImpl;
import slim.core.model.Event;
import slim.core.model.Location;
import slim.core.model.User;
import slim.core.utils.Constants;

/**
 *
 * @author Robert
 */
public abstract class BaseTest {

    private static final String CLASS_NAME = BaseTest.class.getName();
    SlimService mSlimService;
    SlimDBServiceImpl mSlimDatabase;

    /**
     * Prepare the initial setup before testing
     */
    @Before
    public void buildService() {
        mSlimService = new SlimServiceInMemory();
        mSlimDatabase = new SlimDBServiceImpl(Constants.MOCK_DB_NAME);
        mSlimService.setDatabase(mSlimDatabase);
    }

    /**
     * Tear down the created setup
     */
    @After
    public void tearDownService() {
        // Cleanup the mock database
        mSlimDatabase.close();
        File file = new File(System.getProperty("user.dir") + "/" + Constants.MOCK_DB_NAME);
        if (file.exists()) {
            file.delete();
        } else {
            System.err.println(CLASS_NAME + " could not delete mockup database. File does not exist!");
        }
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

    protected Event createRandomEvent(Collection<User> guests, User organizer) {
        long eventBegin = Math.abs(RandomUtils.nextLong());
        long eventEnd = Math.abs(eventBegin + RandomUtils.nextLong());

        Event event;
        Location location = createRandomLocation();
        if (organizer != null) {
            event = mSlimDatabase.createEvent(new Event(getRandomName(), location, eventBegin, eventEnd, getRandomName(), organizer));
        } else {
            event = mSlimDatabase.createEvent(new Event(getRandomName(), location, eventBegin, eventEnd, getRandomName(), createRandomUser()));
        }

        if (guests != null) {
            event.setGuests(new ArrayList<>(guests));
            mSlimDatabase.saveEvent(event);
        }
        return event;
    }

    protected User createRandomUser() {
        return mSlimDatabase.createUser(new User(getRandomName(), getRandomName(), getRandomName(), 10000, getRandomName(), "www.imageurl.de"));
    }

    protected Location createRandomLocation() {
        return mSlimDatabase.createLocation(new Location("auto-generated", RandomUtils.nextLong(), RandomUtils.nextLong()));
    }
}
