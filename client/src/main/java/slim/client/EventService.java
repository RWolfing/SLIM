/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import slim.core.model.Event;

/**
 *
 * @author Robert
 */
public class EventService extends SlimService {

    public EventService(String serviceUrl, MediaType type) {
        super(serviceUrl + "/" + "events", type);
    }

    public SlimResult<Event> createEvent(String name, long lattitude, long longitude, long eventBegin, long eventEnd, String description, int organizerID) {
        PostMethod postMethod = new PostMethod(mServiceBaseURI);
        postMethod.addParameter("name", name);
        postMethod.addParameter("lattitude", String.valueOf(lattitude));
        postMethod.addParameter("longitude", String.valueOf(longitude));
        postMethod.addParameter("eventbegin", String.valueOf(eventBegin));
        postMethod.addParameter("eventend", String.valueOf(eventEnd));
        postMethod.addParameter("description", description);
        postMethod.addParameter("organizerid", String.valueOf(organizerID));
        postMethod.addRequestHeader("Accept", mMediaType.getValue());

        try {
            int status = mHttpClient.executeMethod(postMethod);
            Event event = (Event) unmarshall(postMethod.getResponseBodyAsString(), Event.class);
            return new SlimResult<>(status, null);
        } catch (IOException ex) {
            Logger.getLogger(EventService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            Logger.getLogger(EventService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public SlimResult<Event> updateEvent(int eventId, String name, long eventBegin, long eventEnd, String description) {
        PutMethod putMethod = new PutMethod(mServiceBaseURI + "/" + eventId);
        NameValuePair pairName = new NameValuePair("name", name);
        NameValuePair pairBegin = new NameValuePair("eventbegin", String.valueOf(eventBegin));
        NameValuePair pairEnd = new NameValuePair("eventend", String.valueOf(eventEnd));
        NameValuePair pairDescr = new NameValuePair("description", description);
        putMethod.setQueryString(new NameValuePair[]{pairName, pairBegin, pairEnd, pairDescr});
        putMethod.setRequestHeader("Accept", mMediaType.getValue());

        try {
            int status = mHttpClient.executeMethod(putMethod);
            Event event = (Event) unmarshall(putMethod.getResponseBodyAsString(), Event.class);
            return new SlimResult<>(status, event);
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "IO Exception", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "JAXB Exception", ex);
        }
        return null;
    }

    public SlimResult deleteEvent(int eventId) {
        DeleteMethod deleteMethod = new DeleteMethod(mServiceBaseURI + "/" + eventId);
        try {
            int status = mHttpClient.executeMethod(deleteMethod);
            return new SlimResult(status, null);
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public SlimResult<Event> fetchEventById(int eventId) {
        GetMethod getMethod = new GetMethod(mServiceBaseURI + "/" + eventId);
        getMethod.setRequestHeader("Accept", mMediaType.getValue());
        try {
            int status = mHttpClient.executeMethod(getMethod);
            Event event = (Event) unmarshall(getMethod.getResponseBodyAsString(), Event.class);
            return new SlimResult<>(status, event);
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "IO Exception", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "JAXB Exception", ex);
        }
        return null;
    }

    public SlimResult<List<Event>> fetchAllEvents() {
        return null;
    }

    public SlimResult<List<Event>> getEventsWithUser(int userID) {
        return null;
    }

    public SlimResult<List<Event>> getEventsFromUser(int userId) {
        return null;
    }

    public SlimResult<List<Event>> getEventsWithinLocation(long lattitudeFrom, long lattitudeTo, long longitudeFrom, long longitudeTo) {
        return null;
    }

    public SlimResult<List<Event>> getEventsWithinGuestRange(int minGuests, int maxGuests) {
        return null;
    }

    public SlimResult<Event> addGuestToEvent(int eventId, int userId) {
        PutMethod putMethod = new PutMethod(mServiceBaseURI + "/" + "addguest" + "/" + eventId);
        NameValuePair pairName = new NameValuePair("userId", String.valueOf(userId));
        putMethod.setQueryString(new NameValuePair[]{pairName});
        putMethod.setRequestHeader("Accept", mMediaType.getValue());

        try {
            int status = mHttpClient.executeMethod(putMethod);
            Event event = (Event) unmarshall(putMethod.getResponseBodyAsString(), Event.class);
            return new SlimResult<>(status, event);
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "IO Exception", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "JAXB Exception", ex);
        }
        return null;
    }

    public SlimResult<Event> addGuestsToEvent(int eventId, List<Integer> userIds) {
        if (userIds == null) {
            throw new NullPointerException("Userids must not be null!");
        }
        PutMethod putMethod = new PutMethod(mServiceBaseURI + "/" + "addguests" + "/" + eventId);
        ArrayList<NameValuePair> pairs = new ArrayList<>();
        for (Integer id : userIds) {
            pairs.add(new NameValuePair("userIds", String.valueOf(id)));
        }

        putMethod.setQueryString(pairs.toArray(new NameValuePair[pairs.size()]));
        putMethod.setRequestHeader("Accept", mMediaType.getValue());

        try {
            int status = mHttpClient.executeMethod(putMethod);
            Event event = (Event) unmarshall(putMethod.getResponseBodyAsString(), Event.class);
            return new SlimResult<>(status, event);
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "IO Exception", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "JAXB Exception", ex);
        }
        return null;
    }

    public SlimResult<Event> removeGuestFromEvent(int eventId, int userId) {
        DeleteMethod deleteMethod = new DeleteMethod(mServiceBaseURI + "/" + "removeguest" + "/" + eventId);
        NameValuePair userIdPair = new NameValuePair("userId", String.valueOf(userId));
        deleteMethod.setQueryString(new NameValuePair[]{userIdPair});
        try {
            int status = mHttpClient.executeMethod(deleteMethod);
            Event event = (Event) unmarshall(deleteMethod.getResponseBodyAsString(), Event.class);
            return new SlimResult<>(status, event);
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "IO Exception", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(EventService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public SlimResult<Event> removeGuestsFromEvent(int eventId, List<Integer> userIds) {
        if (userIds == null) {
            throw new NullPointerException("Userids must not be null!");
        }
        PutMethod putMethod = new PutMethod(mServiceBaseURI + "/" + "removeguests" + "/" + eventId);
        ArrayList<NameValuePair> pairs = new ArrayList<>();
        for (Integer id : userIds) {
            pairs.add(new NameValuePair("userIds", String.valueOf(id)));
        }

        putMethod.setQueryString(pairs.toArray(new NameValuePair[pairs.size()]));
        putMethod.setRequestHeader("Accept", mMediaType.getValue());

        try {
            int status = mHttpClient.executeMethod(putMethod);
            Event event = (Event) unmarshall(putMethod.getResponseBodyAsString(), Event.class);
            return new SlimResult<>(status, event);
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "IO Exception", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, "JAXB Exception", ex);
        }
        return null;
    }
}
