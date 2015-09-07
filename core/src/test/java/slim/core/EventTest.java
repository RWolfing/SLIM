/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.Test;
import slim.core.model.Event;
import slim.core.model.GuestList;
import slim.core.model.User;

/**
 *
 * @author Robert
 */
public class EventTest extends BaseTest {

    private String mName;
    private long mLattitude = 500;
    private long mLongitude = 500;
    private long mEventBegin = 0;
    private long mEventEnd = 100000;
    private String mDescription;
    private GuestList mGuestList;
    private User mOrganizer;

    @Before
    public void setupTest() {
        mName = getRandomName();
        mLattitude = 500;
        mLongitude = 500;
        mEventBegin = 0;
        mEventEnd = 100000;
        mDescription = getRandomName();
        mGuestList = new GuestList();
        mOrganizer = createOrganizer();
    }

    public void getEventByIdTest() {

    }

    @Test
    public void createEventTest() {
        /**
         * Neuen Event erzeugen
         */
        Event event = mSlimService.createEvent(mName, mLattitude, mLongitude, mEventBegin, mEventEnd, mDescription, mOrganizer);
        assertThat(event, notNullValue());
        assertThat(event.getmLocation(), notNullValue());
        assertThat(event.getmGuestList(), notNullValue());
        assertThat(event.getmOrganizer(), notNullValue());

        Event fetchedEvent = mSlimDatabase.getEventById(event.getmID());
        assertThat(fetchedEvent.getmName(), is(equalTo(event.getmName())));
        assertThat(fetchedEvent.getmLocation(), notNullValue());
        assertThat(fetchedEvent.getmLocation().getmID(), is(event.getmLocation().getmID()));
        assertThat(fetchedEvent.getmEventBegin(), is(event.getmEventBegin()));
        assertThat(fetchedEvent.getmEventEnd(), is(event.getmEventEnd()));
        assertThat(fetchedEvent.getmDescription(), is(equalTo(event.getmDescription())));
        assertThat(fetchedEvent.getmGuestList(), notNullValue());
        assertThat(fetchedEvent.getmGuestList().getmID(), is(event.getmGuestList().getmID()));
        assertThat(fetchedEvent.getmOrganizer(), notNullValue());
        assertThat(fetchedEvent.getmOrganizer().getmID(), is(event.getmOrganizer().getmID()));
    }

    @Test
    public void updateEventTest() {
        /**
         * Neuen Event erzeugen
         */
        Event event = mSlimService.createEvent(mName, mLattitude, mLongitude, mEventBegin, mEventEnd, mDescription, mOrganizer);
        assertThat(event, notNullValue());
        assertThat(event.getmLocation(), notNullValue());
        assertThat(event.getmGuestList(), notNullValue());
        assertThat(event.getmOrganizer(), notNullValue());

        String newDescription = "new description";
        String newName = "new name";
        long newEventBegin = 1000;
        long newEventEnd = 500000000;

        event.setmDescription(newDescription);
        event.setmEventBegin(newEventBegin);
        event.setmEventEnd(newEventEnd);
        event.setmName(newName);

        /**
         * As ormlite does the update, we do not have to check if every field
         * was really updated just if it worked
         */
        boolean success = mSlimService.updateEvent(event);
        assertThat(success, is(true));
    }

    @Test
    public void deleteEventById() {
        /**
         * Neuen Event erzeugen
         */
        Event event = mSlimService.createEvent(mName, mLattitude, mLongitude, mEventBegin, mEventEnd, mDescription, mOrganizer);
        assertThat(event, notNullValue());
        assertThat(event.getmLocation(), notNullValue());
        assertThat(event.getmGuestList(), notNullValue());
        assertThat(event.getmOrganizer(), notNullValue());

        mSlimService.deleteEvent(event.getmID());
        event = mSlimService.getEventById(event.getmID());
        assertThat(event, nullValue());
    }

    //TODO
    public void getAllEvents() {

    }

    /**
     * Für die Events benötigten Organizer erzeugen
     *
     * @return ein Nutzer
     */
    private User createOrganizer() {
        String nickName = getRandomName();
        String userName = getRandomName();
        String userLastname = getRandomName();
        String userAbout = getRandomName();
        return mSlimService.createUser(nickName, userName, userLastname, 0, userAbout, "www.test.de");
    }
}
