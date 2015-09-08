/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.rest.impl;

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
public class EventResourceImpl extends SlimResource implements EventRessource {

    @Override
    public Response createEvent(UriInfo uriInfo, String name, long lattitude, String longitude, long eventBegin, long eventEnd, String description, int idOrganizer) {
        if (uriInfo == null || name == null || name.equals("") || eventBegin < 0 || eventEnd < eventBegin || idOrganizer < 0) {
            return Response.status(Status.BAD_REQUEST).build();
        }
        User organizer = mSlimService.getUserById(idOrganizer);
        if (organizer != null) {
            Event event = mSlimService.createEvent(name, lattitude, lattitude, eventBegin, eventEnd, description, null);
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
    public Response updateEvent(UriInfo uriInfo, int eventId, String name, long lattitude, String longitude, long eventBegin, long eventEnd, String description) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response deleteEvent(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response fetchEventById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response fetchAllEvents() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response getEventsWithUser(int userId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response getEventsFromUser(int userId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response getEventsWithinLocation(long lattitudeFrom, long lattiudeTo, long longitudeFrom, long longitudeTo) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response getEventsWithinRange(int minGuests, int maxGuests) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
