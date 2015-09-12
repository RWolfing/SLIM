package slim.rest.impl;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import slim.core.model.Event;
import slim.core.model.EventList;
import slim.core.model.User;
import slim.rest.EventRessource;

/**
 * Implementation of the {@link EventRessource}
 * 
 * @author Robert
 */
@Path("events")
public class EventResourceImpl extends SlimResource implements EventRessource {

    @Override
    public Response createEvent(UriInfo uriInfo, String name, long lattitude, long longitude, long eventBegin, long eventEnd, String description, int idOrganizer) {
        if (uriInfo == null || name == null || name.equals("") || eventBegin < 0 || eventEnd < eventBegin || idOrganizer < 0) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        //Check if organizer exists
        if (mSlimService.getUserById(idOrganizer) != null) {
            //Create event
            Event event = mSlimService.createEvent(name, lattitude, lattitude, eventBegin, eventEnd, description, idOrganizer);
            if (event != null) {
                return Response.status(Status.CREATED).entity(event).build();
            } else {
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response updateEvent(UriInfo uriInfo, int eventId, String name, long eventBegin, long eventEnd, String description) {
        if (uriInfo == null || name == null || name.equals("") || eventBegin < 0 || eventEnd < eventBegin) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        //Retrieve event
        Event event = mSlimService.getEventById(eventId);
        //Check if event exists
        if (event != null) {
            event.setmName(name);
            event.setmEventBegin(eventBegin);
            event.setmEventEnd(eventEnd);
            event.setmDescription(description);
            //Update event
            if (mSlimService.updateEvent(event)) {
                return Response.ok(event).build();
            } else {
                return Response.serverError().build();
            }
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response deleteEvent(int id) {
        if (mSlimService.deleteEvent(id)) {
            return Response.status(Status.OK).build();
        } else {
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public Response fetchEventById(int id) {
        //Retrieve event
        Event event = mSlimService.getEventById(id);
        //Check if event exists
        if (event != null) {
            return Response.ok(event).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response fetchAllEvents() {
        //Retrieve all events
        List<Event> events = mSlimService.getEvents();
        //Check if there are events
        if (events != null && events.size() > 0) {
            EventList eventList = new EventList();
            eventList.setEvents(events);
            return Response.ok(eventList).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response getEventsWithUser(int userId) {
        //Retrieve all events with the user
        List<Event> events = mSlimService.getEventsWithUser(userId);
        //Check if there are events
        if (events != null && events.size() > 0) {
            EventList eventList = new EventList();
            eventList.setEvents(events);
            return Response.ok(eventList).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response getEventsFromUser(int userId) {
        //Retrieve all events from the user
        List<Event> events = mSlimService.getEventsFromUser(userId);
        //Check if there are events
        if (events != null && events.size() > 0) {
            EventList eventList = new EventList();
            eventList.setEvents(events);
            return Response.ok(eventList).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response getEventsWithinLocation(long lattitudeFrom, long lattiudeTo, long longitudeFrom, long longitudeTo) {
        //Retrieve all events with the location in the given bounds
        List<Event> events = mSlimService.getEventsWithinLocation(lattitudeFrom, lattiudeTo, longitudeFrom, longitudeTo);
        //Check if there are events
        if (events != null && events.size() > 0) {
            EventList eventList = new EventList();
            eventList.setEvents(events);
            return Response.ok(eventList).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response getEventsWithinGuestRange(int minGuests, int maxGuests) {
        //Retrieve all events with the given guest range
        List<Event> events = mSlimService.getEventsWithinGuestRange(minGuests, maxGuests);
        //Check if there are events
        if (events != null && events.size() > 0) {
            EventList eventList = new EventList();
            eventList.setEvents(events);
            return Response.ok(eventList).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response addGuestToEvent(UriInfo uriInfo, int eventId, int userId) {
        if (uriInfo == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        //Retrieve the event
        Event event = mSlimService.getEventById(eventId);
        //Retrieve the user
        User user = mSlimService.getUserById(userId);
        //Check if event & user exist
        if (user != null && event != null) {
            event.addGuest(user);
            if (mSlimService.updateEvent(event)) {
                return Response.ok(event).build();
            } else {
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response addGuestsToEvent(UriInfo uriInfo, int eventId, List<Integer> userids) {
        if (uriInfo == null || userids == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        //Retrieve the event
        Event event = mSlimService.getEventById(eventId);

        //Fill the guest list
        List<User> guests = new ArrayList<>();
        for (Integer id : userids) {
            if (id != null) {
                User user = mSlimService.getUserById(id);
                if (user != null) {
                    guests.add(user);
                }
            }
        }
        //Check if event exists
        if (event != null) {
            //Add guests
            for (User guest : guests) {
                event.addGuest(guest);
                mSlimService.updateEvent(event);
            }
            return Response.ok(event).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response removeGuestFromEvent(UriInfo uriInfo, int eventId, int userid) {
        if (uriInfo == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        //Retrieve event
        Event event = mSlimService.getEventById(eventId);
        //Retrieve user
        User user = mSlimService.getUserById(userid);

        //Check if event & user exist
        if (event != null && user != null) {
            //Remove guest
            event.removeGuest(user);
            if (mSlimService.updateEvent(event)) {
                return Response.ok(event).build();
            } else {
                return Response.status(Status.INTERNAL_SERVER_ERROR).build();
            }
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response removeGuestsFromEvent(UriInfo uriInfo, int eventId, List<Integer> userids) {
        if (uriInfo == null || userids == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }

        //Retrieve event
        Event event = mSlimService.getEventById(eventId);

        //Fill guest list to remove
        List<User> guestsToRemove = new ArrayList<>();
        for (Integer id : userids) {
            if (id != null) {
                User user = mSlimService.getUserById(id);
                if (user != null) {
                    guestsToRemove.add(user);
                }
            }
        }
        //Check if event exists
        if (event != null) {
            //Remove all guests
            for (User guest : guestsToRemove) {
                event.removeGuest(guest);
                mSlimService.updateEvent(event);
            }
            return Response.ok(event).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
}
