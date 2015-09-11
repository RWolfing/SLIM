/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.rest.impl;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import slim.core.model.Event;
import slim.core.model.User;
import slim.rest.EventRessource;

/**
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
        if (mSlimService.getUserById(idOrganizer) != null) {
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

        Event event = mSlimService.getEventById(eventId);
        if (event != null) {
            event.setmName(name);
            event.setmEventBegin(eventBegin);
            event.setmEventEnd(eventEnd);
            event.setmDescription(description);
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
        Event event = mSlimService.getEventById(id);
        if (event != null) {
            return Response.ok(event).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response fetchAllEvents() {
        return Response.ok(mSlimService.getEvents()).build();
    }

    @Override
    public Response getEventsWithUser(int userId) {
        List<Event> result = mSlimService.getEventsWithUser(userId);
        if (result != null) {
            return Response.ok(result).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response getEventsFromUser(int userId) {
        List<Event> result = mSlimService.getEventsFromUser(userId);
        if (result != null) {
            return Response.ok(result).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @Override
    public Response getEventsWithinLocation(long lattitudeFrom, long lattiudeTo, long longitudeFrom, long longitudeTo) {
        return Response.ok(mSlimService.getEventsWithinLocation(lattitudeFrom, lattiudeTo, longitudeFrom, longitudeTo)).build();
    }

    @Override
    public Response getEventsWithinGuestRange(int minGuests, int maxGuests) {
        return Response.ok(mSlimService.getEventsWithinGuestRange(minGuests, maxGuests)).build();
    }

    @Override
    public Response addGuestToEvent(UriInfo uriInfo, int eventId, int userId) {
        if (uriInfo == null) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        Event event = mSlimService.getEventById(eventId);
        User user = mSlimService.getUserById(userId);

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
        Event event = mSlimService.getEventById(eventId);
        List<User> guests = new ArrayList<>();
        for (Integer id : userids) {
            if (id != null) {
                User user = mSlimService.getUserById(id);
                if (user != null) {
                    guests.add(user);
                }
            }
        }
        if (event != null) {
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

        Event event = mSlimService.getEventById(eventId);
        User user = mSlimService.getUserById(userid);

        if (event != null && user != null) {
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

        Event event = mSlimService.getEventById(eventId);
        List<User> guestsToRemove = new ArrayList<>();
        for (Integer id : userids) {
            if (id != null) {
                User user = mSlimService.getUserById(id);
                if (user != null) {
                    guestsToRemove.add(user);
                }
            }
        }
        if (event != null) {
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
