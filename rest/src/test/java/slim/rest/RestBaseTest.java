/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.rest;

import java.io.File;
import javax.ws.rs.core.Context;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.After;
import org.junit.Before;
import slim.core.SlimService;
import slim.core.impl.SlimDBServiceImpl;
import slim.core.impl.SlimServiceInMemory;
import slim.core.model.Event;
import slim.core.model.Location;
import slim.core.model.User;
import slim.core.utils.Constants;

/**
 *
 * @author Robert
 */
public abstract class RestBaseTest {

    private static final String CLASS_NAME = RestBaseTest.class.getName();
    protected static final int SUM_USERS = 5;
    protected static final int SUM_EVENTS = 3;
    protected static final int TEST_USER_SUM_EVENTS_JOINED = 1;
    protected static final int TEST_USER_SUM_EVENTS_CREATED = 1;

    @Context
    protected SlimService mSlimService;
    private SlimDBServiceImpl mSlimDatabase;

    protected User mTestUser;
    protected User mTestUser2;
    protected User mPartyFriend1;
    protected User mPartyFriend2;

    protected Event mTestEvent;

    /**
     * Prepare the initial setup before testing
     */
    @Before
    public void buildService() {
        mSlimService = new SlimServiceInMemory();
        mSlimDatabase = new SlimDBServiceImpl(Constants.MOCK_DB_NAME);
        mSlimService.setDatabase(mSlimDatabase);

        User max = new User("Partyloewe", "Max", "Mustermann", 500000, "Spiel mit mir", "www.bilder.de/bild1");
        User anna = new User("Partyloewin", "Anna", "Branco", 600000, "Spiel mit mir", "www.bilder.de/bild3");
        User klaus = new User("klaus", "klaus", "schulz", 5000000, "Spiel mit mir", "www.bilder.de/bild4");
        User gustav = new User("gmueller", "Gustav", "Mueller", 1500000, "Spiel mit mir", "www.bilder.de/bild5");
        User bianca = new User("bschaber", "Bianca", "Schaber", 53420000, "Spiel mit mir", "www.bilder.de/bild6");

        mTestUser = max = mSlimDatabase.createUser(max);
        mPartyFriend1 = anna = mSlimDatabase.createUser(anna);
        mTestUser2 = klaus = mSlimDatabase.createUser(klaus);
        mPartyFriend2 = gustav = mSlimDatabase.createUser(gustav);
        bianca = mSlimDatabase.createUser(bianca);

        Location location0 = new Location(RandomUtils.nextLong(), RandomUtils.nextLong());
        Location location1 = new Location(RandomUtils.nextLong(), RandomUtils.nextLong());
        Location location2 = new Location(RandomUtils.nextLong(), RandomUtils.nextLong());

        location0 = mSlimDatabase.createLocation(location0);
        location1 = mSlimDatabase.createLocation(location1);
        location2 = mSlimDatabase.createLocation(location2);

        Event event0 = new Event("Event0", location0, 8000, 90000, "Event 0 ist toll", max);
        event0.addGuest(gustav);
        event0.addGuest(anna);
        mTestEvent = mSlimDatabase.createEvent(event0);

        Event event1 = new Event("Event1", location1, 123100, 9000000, "Event 1 ist toll", anna);
        event1.addGuest(gustav);
        mSlimDatabase.createEvent(event1);

        Event event2 = new Event("Event2", location2, 654616, 78951651, "Event 2 ist toll", bianca);
        event2.addGuest(klaus);
        event2.addGuest(max);
        event2.addGuest(gustav);
        mSlimDatabase.createEvent(event2);
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

    public SlimService getSlimService() {
        return mSlimService;
    }
}
