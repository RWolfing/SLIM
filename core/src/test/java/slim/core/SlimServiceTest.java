package slim.core;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import slim.core.model.Event;
import slim.core.model.User;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

/**
 * Class to test functionality of the SlimService methods that did not match any
 * model tests
 *
 * @author Robert Wolfinger
 */
public class SlimServiceTest extends BaseTest {

    @Before
    public void setupTest() throws SQLException {
        mSlimDatabase.clearAllTables();
    }

    @Test
    public void getEventsWithUserTest() throws SQLException {
        mSlimDatabase.clearAllTables();

        //Create the user which will be used a the search parameter
        User searchedUser = createRandomUser();

        //Creates some guestlists one with the user, one without him
        Collection<User> guestListWithUser = new ArrayList<>();
        guestListWithUser.add(searchedUser);
        guestListWithUser.add(createRandomUser());
        guestListWithUser.add(createRandomUser());

        Collection<User> guestListWithoutUser = new ArrayList();
        guestListWithoutUser.add(createRandomUser());
        guestListWithoutUser.add(createRandomUser());

        //Create a couple of events with the different guestlists
        Event event1 = createRandomEvent(guestListWithUser, null);
        Event event2 = createRandomEvent(guestListWithoutUser, null);
        Event event3 = createRandomEvent(guestListWithUser, null);
        Event event4 = createRandomEvent(guestListWithoutUser, null);

        //Retrieve all events with the user and check the result
        List<Event> result = mSlimService.getEventsWithUser(searchedUser.getmID());
        assertThat(result.contains(event1), is(true));
        assertThat(result.contains(event2), is(false));
        assertThat(result.contains(event3), is(true));
        assertThat(result.contains(event4), is(false));
    }

    @Test
    public void getEventsWithinGuestRange() throws SQLException {
        mSlimDatabase.clearAllTables();

        //Create a couple of random users
        User guest0 = createRandomUser();
        User guest1 = createRandomUser();
        User guest2 = createRandomUser();
        User guest3 = createRandomUser();

        //Create guestlist for the edge cases
        List<User> guestListMinRange = new ArrayList<>();
        guestListMinRange.add(guest0);

        List<User> guestListInRange = new ArrayList<>();
        guestListInRange.add(guest0);
        guestListInRange.add(guest2);

        List<User> guestListMaxRange = new ArrayList<>();
        guestListMaxRange.add(guest0);
        guestListMaxRange.add(guest1);
        guestListMaxRange.add(guest2);

        List<User> guestListOverRange = new ArrayList<>();
        guestListOverRange.add(guest0);
        guestListOverRange.add(guest1);
        guestListOverRange.add(guest2);
        guestListOverRange.add(guest3);

        //Create a couple of events for the edge cases
        Event eventOutOfRange = createRandomEvent(null, guest3);
        Event eventInMinRange = createRandomEvent(guestListMinRange, null);
        Event eventInRange = createRandomEvent(guestListInRange, null);
        Event eventInMaxRange = createRandomEvent(guestListMaxRange, null);
        Event eventOverMaxRange = createRandomEvent(guestListOverRange, null);

        //Test if getEventWithinGuestRange returns the right result
        List<Event> result = mSlimService.getEventsWithinGuestRange(1, 3);
        assertThat(result, notNullValue());
        assertThat(result.size(), is(3));
        assertThat(result.contains(eventInMinRange), is(true));
        assertThat(result.contains(eventInRange), is(true));
        assertThat(result.contains(eventInMaxRange), is(true));
    }

    @Test
    public void doesHePartyWithMe() throws SQLException {
        mSlimDatabase.clearAllTables();

        //Create testusers
        User userMe = createRandomUser();
        User userHim = createRandomUser();
        User user2 = createRandomUser();
        User user3 = createRandomUser();

        //Create a guestlist for success & failure
        List<User> guestListSuccess = new ArrayList<>();
        guestListSuccess.add(userMe);
        guestListSuccess.add(userHim);

        List<User> guestListFailure = new ArrayList<>();
        guestListSuccess.add(userMe);
        guestListSuccess.add(user2);

        //Create the events
        Event eventWithUsers = createRandomEvent(guestListSuccess, user3);
        createRandomEvent(guestListFailure, user3);

        //Fetch result doesHePartyWithMe
        boolean yesNo = mSlimService.doesHePartyWithMe(userMe.getmID(), userHim.getmID());
        assertThat(yesNo, is(true));

        //Delete event for success & check again
        mSlimService.deleteEvent(eventWithUsers.getmID());
        yesNo = mSlimService.doesHePartyWithMe(userMe.getmID(), userHim.getmID());
        assertThat(yesNo, is(false));
    }
}
