package slim.core;

import java.sql.SQLException;
import java.util.List;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import slim.core.model.Event;
import slim.core.model.User;

/**
 * Class to test the functionality of the event model and all connected service
 * write & reads
 *
 * @author Robert Wolfinger
 */
public class EventTest extends BaseTest {

    @Test
    public void createEventTest() throws SQLException {
        /**
         * Create new organizer
         */
        User organzier = createRandomUser();

        /**
         * Create new event
         */
        Event event = createRandomEvent(null, organzier);
        assertThat(event, notNullValue());
        assertThat(event.getLocation(), notNullValue());
        assertThat(event.getOrganizer(), notNullValue());

        /**
         * Read the event from the db & check every field
         */
        Event fetchedEvent = mSlimDatabase.getEventById(event.getID());
        assertThat(fetchedEvent.getName(), is(equalTo(event.getName())));
        assertThat(fetchedEvent.getLocation(), notNullValue());
        assertThat(fetchedEvent.getLocation().getID(), is(event.getLocation().getID()));
        assertThat(fetchedEvent.getEventBegin(), is(event.getEventBegin()));
        assertThat(fetchedEvent.getEventEnd(), is(event.getEventEnd()));
        assertThat(fetchedEvent.getGuests(), notNullValue());
        assertThat(fetchedEvent.getGuests().size(), is(0));
        assertThat(fetchedEvent.getDescription(), is(equalTo(event.getDescription())));
        assertThat(fetchedEvent.getOrganizer(), notNullValue());
        assertThat(fetchedEvent.getOrganizer().getID(), is(event.getOrganizer().getID()));
    }

    @Test
    public void updateEventTest() throws SQLException {
        //Create a organizer
        User organzier = createRandomUser();
        //Create a guest
        User guest = createRandomUser();

        //Create a event
        Event createdEvent = createRandomEvent(null, organzier);
        assertThat(createdEvent, notNullValue());

        //Update the event with new values
        String newDescription = "new description";
        String newName = "new name";
        long newEventBegin = 1000;
        long newEventEnd = 500000000;

        Event event = mSlimService.getEventById(createdEvent.getID());
        event.setDescription(newDescription);
        event.setEventBegin(newEventBegin);
        event.setEventEnd(newEventEnd);
        event.setName(newName);
        event.addGuest(guest);

        /**
         * As ormlite does the update, we do not have to check if every field
         * was really updated just if it worked
         */
        boolean success = mSlimService.updateEvent(event);
        assertThat(success, is(true));
        assertThat(event.getGuests().size(), is(1));
    }

    @Test
    public void guestsOfEventTest() throws SQLException {
        //Create a events
        Event event = createRandomEvent(null, null);
        //Create guests
        User guest1 = createRandomUser();
        User guest2 = createRandomUser();
        //Add the guest & save the event
        event.addGuest(guest1);
        event.addGuest(guest2);
        boolean success = mSlimDatabase.saveEvent(event);
        assertThat(success, is(true));

        //Fetch event from db and check if guests match
        event = mSlimDatabase.getEventById(event.getID());
        assertThat(event.getGuests().contains(guest1), is(true));
        assertThat(event.getGuests().contains(guest2), is(true));
        assertThat(event.getGuests().size(), is(2));

        //Remove guest from event
        event.removeGuest(guest1);
        success = mSlimDatabase.saveEvent(event);
        assertThat(success, is(true));

        //Fetch event from db and check if guests match
        event = mSlimDatabase.getEventById(event.getID());
        assertThat(event.getGuests().contains(guest1), is(false));
        assertThat(event.getGuests().contains(guest2), is(true));
    }

    @Test
    public void deleteEventById() throws SQLException {
        //Create event organizer
        User organzier = createRandomUser();

        //Create event
        Event event = createRandomEvent(null, organzier);
        assertThat(event, notNullValue());

        //Delete event
        mSlimService.deleteEvent(event.getID());
        event = mSlimService.getEventById(event.getID());
        assertThat(event, nullValue());
    }

    @Test
    public void getAllEvents() throws SQLException {
        mSlimDatabase.clearAllTables();
        //Create organizer
        User organzier = createRandomUser();

        //Create 3 events
        Event event1 = createRandomEvent(null, organzier);
        assertThat(event1, notNullValue());

        Event event2 = createRandomEvent(null, organzier);
        assertThat(event2, notNullValue());

        Event event3 = createRandomEvent(null, organzier);
        assertThat(event3, notNullValue());

        //Retrieve all events and check if the size is 3
        List<Event> allEvents = mSlimDatabase.getAllEvents();
        assertThat(allEvents, notNullValue());
        assertThat(allEvents.size(), is(3));
        assertThat(allEvents.contains(event1), is(true));
        assertThat(allEvents.contains(event2), is(true));
        assertThat(allEvents.contains(event3), is(true));
    }
}
