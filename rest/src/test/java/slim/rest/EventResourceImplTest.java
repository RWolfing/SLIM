/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.rest;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import org.apache.commons.lang.math.RandomUtils;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import slim.core.model.Event;
import slim.rest.impl.EventResourceImpl;

/**
 *
 * @author Robert
 */
public class EventResourceImplTest extends RestBaseTest {

    private EventResourceImpl mEventResource;
    private UriInfo mUriInfo;

    @Before
    public void prepareResourcesTest() {
        mEventResource = new EventResourceImpl();
        mEventResource.setmSlimService(mSlimService);
    }

    @Before
    public void mockUriInfo() {
        mUriInfo = mock(UriInfo.class);
        final UriBuilder fromResource = UriBuilder.fromResource(EventResourceImpl.class);
        when(mUriInfo.getAbsolutePathBuilder()).thenAnswer(new Answer<UriBuilder>() {

            @Override
            public UriBuilder answer(InvocationOnMock invocation) throws Throwable {
                return fromResource;
            }
        });
    }

    @Test
    public void createEventTest() {
        //Test success
        Response response = mEventResource.createEvent(mUriInfo, "TestEvent", RandomUtils.nextLong(), RandomUtils.nextLong(), 1000, 10000, "event description", mTestUser.getmID());
        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));

        Event event = (Event) response.getEntity();
        response = mEventResource.fetchEventById(event.getmID());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        assertThat(event.getmID(), is(((Event) response.getEntity()).getmID()));

        //Test failure name missing
        response = mEventResource.createEvent(mUriInfo, null, RandomUtils.nextLong(), RandomUtils.nextLong(), 1000, 10000, "event description", mTestUser.getmID());
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));

        //Test failure bad time
        response = mEventResource.createEvent(mUriInfo, "TestEvent", RandomUtils.nextLong(), RandomUtils.nextLong(), 5000, 1000, "event description", mTestUser.getmID());
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));

        //Test failure organizer does not exist
        response = mEventResource.createEvent(mUriInfo, "TestEvent", RandomUtils.nextLong(), RandomUtils.nextLong(), 1000, 5000, "event description", 100);
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    public void updateEventTest() {
        //Test success
        Response response = mEventResource.updateEvent(mUriInfo, mTestEvent.getmID(), "updated name", 2000, 3000, "new description");
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        Event event = (Event) response.getEntity();
        assertThat(event.getmID(), is(mTestEvent.getmID()));
        assertThat(event.getmName(), is("updated name"));
        assertThat(event.getmEventBegin(), is(2000));
        assertThat(event.getmEventEnd(), is(3000));
        assertThat(event.getmDescription(), is("new description"));

        //Test failure bad request name null
        response = mEventResource.updateEvent(mUriInfo, mTestEvent.getmID(), null, 2000, 3000, "new description");
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));

        //Test failure bad request bad time
        response = mEventResource.updateEvent(mUriInfo, mTestEvent.getmID(), "name", 3000, 2000, "new description");
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

    @Test
    public void deleteEventTest() {
        Response response = mEventResource.deleteEvent(mTestEvent.getmID());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void fetchAllEventsTest() {
        Response response = mEventResource.fetchAllEvents();
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        List<Event> result = (List<Event>) response.getEntity();
        assertThat(result.size(), is(SUM_EVENTS));
    }

    @Test
    public void getEventsWithUserTest() {
        //Test success
        Response response = mEventResource.getEventsWithUser(mTestUser.getmID());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        List<Event> result = (List<Event>) response.getEntity();
        assertThat(result.size(), is(TEST_USER_SUM_EVENTS_JOINED));

        //Test failure not found
        response = mEventResource.getEventsWithUser(100);
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    public void getEventsFromUserTest() {
        //Test success
        Response response = mEventResource.getEventsFromUser(mTestUser.getmID());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        List<Event> result = (List<Event>) response.getEntity();
        assertThat(result.size(), is(TEST_USER_SUM_EVENTS_CREATED));

        //Test not found
        response = mEventResource.getEventsFromUser(100);
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    public void getEventsWithinLocation() {
        Response response = mEventResource.getEventsWithinLocation(RandomUtils.nextLong(), RandomUtils.nextLong(), RandomUtils.nextLong(), RandomUtils.nextLong());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void getEventsWithinGuestRangeTest() {
        Response response = mEventResource.getEventsWithinGuestRange(0, 5);
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void changeGuestTest() {
        //First add guest to event 
        Response response = mEventResource.addGuestToEvent(mUriInfo, mTestEvent.getmID(), mTestUser.getmID());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        Event event = (Event) response.getEntity();
        assertThat(event.getGuests().contains(mTestUser), is(true));

        //Remove guest from event
        response = mEventResource.removeGuestFromEvent(mUriInfo, mTestEvent.getmID(), mTestUser.getmID());
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        event = (Event) response.getEntity();
        assertThat(event.getGuests().contains(mTestUser), is(false));

        //Test add failure event not found
        response = mEventResource.addGuestToEvent(mUriInfo, 100, mTestUser.getmID());
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));

        //Test add failure user not found
        response = mEventResource.addGuestToEvent(mUriInfo, mTestEvent.getmID(), 100);
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));

        //Test add failure event not found
        response = mEventResource.removeGuestFromEvent(mUriInfo, 100, mTestUser.getmID());
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));

        //Test add failure event not found
        response = mEventResource.removeGuestFromEvent(mUriInfo, mTestEvent.getmID(), 100);
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    public void changeGuestsTest() {
        //Add guests klaus & max to event
        final int prevGuestSize = mTestEvent.getGuests().size();
        List<Integer> userIds = new ArrayList<>();
        userIds.add(mTestUser.getmID());
        userIds.add(mTestUser2.getmID());
        userIds.add(1000);
        Response response = mEventResource.addGuestsToEvent(mUriInfo, mTestEvent.getmID(), userIds);
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        Event event = (Event) response.getEntity();
        assertThat(event.getGuests().contains(mTestUser), is(true));
        assertThat(event.getGuests().contains(mTestUser2), is(true));
        assertThat(event.getGuests().size(), is(prevGuestSize + 2));

        //Remove klaus & max from event
        response = mEventResource.removeGuestsFromEvent(mUriInfo, mTestEvent.getmID(), userIds);
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));

        event = (Event) response.getEntity();
        assertThat(event.getGuests().contains(mTestUser), is(false));
        assertThat(event.getGuests().contains(mTestUser2), is(false));
        assertThat(event.getGuests().size(), is(prevGuestSize));

        //Test failure if event does not exist - add
        response = mEventResource.addGuestsToEvent(mUriInfo, 100, userIds);
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));

        //Test failure if no user ids are given - add
        response = mEventResource.addGuestsToEvent(mUriInfo, mTestEvent.getmID(), null);
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));

        //Test failure if event does not exist - remove
        response = mEventResource.removeGuestsFromEvent(mUriInfo, 100, userIds);
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));

        //Test failure if no user ids are given -remove
        response = mEventResource.removeGuestsFromEvent(mUriInfo, mTestEvent.getmID(), null);
        assertThat(response.getStatus(), is(Response.Status.BAD_REQUEST.getStatusCode()));
    }

}
