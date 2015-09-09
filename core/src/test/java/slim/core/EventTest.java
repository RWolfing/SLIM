/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core;

import java.sql.SQLException;
import java.util.List;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Test;
import slim.core.model.Event;
import slim.core.model.User;

/**
 *
 * @author Robert
 */
public class EventTest extends BaseTest {

    @Test
    public void createEventTest() throws SQLException {
        mSlimDatabase.clearAllTables();
        /**
         * Neuen Event erzeugen
         */
        User organzier = createRandomUser();

        Event event = createRandomEvent(null, organzier);
        assertThat(event, notNullValue());
        assertThat(event.getmLocation(), notNullValue());
        assertThat(event.getmOrganizer(), notNullValue());

        Event fetchedEvent = mSlimDatabase.getEventById(event.getmID());
        assertThat(fetchedEvent.getmName(), is(equalTo(event.getmName())));
        assertThat(fetchedEvent.getmLocation(), notNullValue());
        assertThat(fetchedEvent.getmLocation().getmID(), is(event.getmLocation().getmID()));
        assertThat(fetchedEvent.getmEventBegin(), is(event.getmEventBegin()));
        assertThat(fetchedEvent.getmEventEnd(), is(event.getmEventEnd()));
        assertThat(fetchedEvent.getGuests(), notNullValue());
        assertThat(fetchedEvent.getGuests().size(), is(0));
        assertThat(fetchedEvent.getmDescription(), is(equalTo(event.getmDescription())));
        assertThat(fetchedEvent.getmOrganizer(), notNullValue());
        assertThat(fetchedEvent.getmOrganizer().getmID(), is(event.getmOrganizer().getmID()));
    }

    @Test
    public void updateEventTest() throws SQLException {
        mSlimDatabase.clearAllTables();
        /**
         * Neuen Event erzeugen
         */
        User organzier = createRandomUser();
        User guest = createRandomUser();

        Event event = createRandomEvent(null, organzier);
        assertThat(event, notNullValue());

        String newDescription = "new description";
        String newName = "new name";
        long newEventBegin = 1000;
        long newEventEnd = 500000000;

        event.setmDescription(newDescription);
        event.setmEventBegin(newEventBegin);
        event.setmEventEnd(newEventEnd);
        event.setmName(newName);
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
    public void deleteEventById() throws SQLException {
        mSlimDatabase.clearAllTables();
        /**
         * Neuen Event erzeugen
         */
        User organzier = createRandomUser();

        Event event = createRandomEvent(null, organzier);
        assertThat(event, notNullValue());

        mSlimService.deleteEvent(event.getmID());
        event = mSlimService.getEventById(event.getmID());
        assertThat(event, nullValue());
    }

    @Test
    public void getAllEvents() throws SQLException {
        mSlimDatabase.clearAllTables();

        User organzier = createRandomUser();

        Event event1 = createRandomEvent(null, organzier);
        assertThat(event1, notNullValue());

        Event event2 = createRandomEvent(null, organzier);
        assertThat(event2, notNullValue());

        Event event3 = createRandomEvent(null, organzier);
        assertThat(event3, notNullValue());

        List<Event> allEvents = mSlimDatabase.getAllEvents();
        assertThat(allEvents, notNullValue());
        assertThat(allEvents.size(), is(3));

    }
}
