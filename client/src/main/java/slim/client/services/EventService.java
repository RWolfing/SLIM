/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.client.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import static slim.client.services.SlimService.ERROR_JAXB;
import slim.core.model.Event;

/**
 *
 * @author Robert
 */
public class EventService extends SlimService {

    private static final String ERROR_JAXB_LOCATION = ERROR_JAXB + Event.class.getName();

    public EventService(String serviceUrl, MediaType type) {
        super(serviceUrl + "/" + "events", type);
    }

    public SlimResult<Event> createEvent(String name, long lattitude, long longitude, long eventBegin, long eventEnd, String description, int organizerID) {
        SlimResult result = new SlimResult(null);
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
            result.setStatus(status);
            if (status == HttpStatus.SC_CREATED) {
                Event event = (Event) unmarshall(postMethod.getResponseBodyAsString(), Event.class);
                result.setResultContent(event);
            }
        } catch (IOException ex) {
            Logger.getLogger(EventService.class.getName()).log(Level.SEVERE, ERROR_IO + "create event", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(EventService.class.getName()).log(Level.SEVERE, ERROR_JAXB_LOCATION, ex);
        }
        whatAmIDoing(result, postMethod.getPath());
        return result;
    }

    public SlimResult<Event> updateEvent(int eventId, String name, long eventBegin, long eventEnd, String description) {
        SlimResult result = new SlimResult(null);
        PutMethod putMethod = new PutMethod(mServiceBaseURI + "/" + eventId);
        NameValuePair pairName = new NameValuePair("name", name);
        NameValuePair pairBegin = new NameValuePair("eventbegin", String.valueOf(eventBegin));
        NameValuePair pairEnd = new NameValuePair("eventend", String.valueOf(eventEnd));
        NameValuePair pairDescr = new NameValuePair("description", description);
        putMethod.setQueryString(new NameValuePair[]{pairName, pairBegin, pairEnd, pairDescr});
        putMethod.setRequestHeader("Accept", mMediaType.getValue());

        try {
            int status = mHttpClient.executeMethod(putMethod);
            result.setStatus(status);
            if (status == HttpStatus.SC_OK) {
                Event event = (Event) unmarshall(putMethod.getResponseBodyAsString(), Event.class);
                result.setResultContent(event);
            }
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_IO + "update event", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_JAXB_LOCATION, ex);
        }
          whatAmIDoing(result, putMethod.getPath());
        return result;
    }

    public SlimResult deleteEvent(int eventId) {
        SlimResult result = new SlimResult(null);
        DeleteMethod deleteMethod = new DeleteMethod(mServiceBaseURI + "/" + eventId);
        try {
            int status = mHttpClient.executeMethod(deleteMethod);
            result.setStatus(status);
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, null, ex);
        }
         whatAmIDoing(result, deleteMethod.getPath());
        return result;
    }

    public SlimResult<Event> fetchEventById(int eventId) {
        SlimResult result = new SlimResult(null);
        GetMethod getMethod = new GetMethod(mServiceBaseURI + "/" + eventId);
        getMethod.setRequestHeader("Accept", mMediaType.getValue());
        try {
            int status = mHttpClient.executeMethod(getMethod);
            result.setStatus(status);
            if (status == HttpStatus.SC_OK) {
                Event event = (Event) unmarshall(getMethod.getResponseBodyAsString(), Event.class);
                result.setResultContent(event);
            }
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_IO + "fetch event by id", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_JAXB_LOCATION, ex);
        }
        whatAmIDoing(result, getMethod.getPath());
        return result;
    }

    public SlimResult<List<Event>> fetchAllEvents() {
        SlimResult result = new SlimResult(null);
        return result;
    }

    public SlimResult<List<Event>> getEventsWithUser(int userID) {
        SlimResult result = new SlimResult(null);
        whatAmIDoing(result, "");
        return result;
    }

    public SlimResult<List<Event>> getEventsFromUser(int userId) {
        SlimResult result = new SlimResult(null);
        whatAmIDoing(result, "");
        return result;
    }

    public SlimResult<List<Event>> getEventsWithinLocation(long lattitudeFrom, long lattitudeTo, long longitudeFrom, long longitudeTo) {
        SlimResult result = new SlimResult(null);
        whatAmIDoing(result, "");
        return result;
    }

    public SlimResult<List<Event>> getEventsWithinGuestRange(int minGuests, int maxGuests) {
        SlimResult result = new SlimResult(null);
        whatAmIDoing(result, "");
        return result;
    }

    public SlimResult<Event> addGuestToEvent(int eventId, int userId) {
        SlimResult result = new SlimResult(null);
        PutMethod putMethod = new PutMethod(mServiceBaseURI + "/" + "addguest" + "/" + eventId);
        NameValuePair pairName = new NameValuePair("userId", String.valueOf(userId));
        putMethod.setQueryString(new NameValuePair[]{pairName});
        putMethod.setRequestHeader("Accept", mMediaType.getValue());

        try {
            int status = mHttpClient.executeMethod(putMethod);
            result.setStatus(status);
            if (status == HttpStatus.SC_OK) {
                Event event = (Event) unmarshall(putMethod.getResponseBodyAsString(), Event.class);
                result.setResultContent(event);
            }
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_IO + "add guest to event", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_JAXB_LOCATION, ex);
        }
        whatAmIDoing(result, putMethod.getPath());
        return result;
    }

    public SlimResult<Event> addGuestsToEvent(int eventId, List<Integer> userIds) {
        SlimResult result = new SlimResult(null);
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
            result.setStatus(status);
            if (status == HttpStatus.SC_OK) {
                Event event = (Event) unmarshall(putMethod.getResponseBodyAsString(), Event.class);
                result.setResultContent(event);
            }
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_IO + "add guests to event", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_JAXB_LOCATION, ex);
        }
         whatAmIDoing(result, putMethod.getPath());
        return result;
    }

    public SlimResult<Event> removeGuestFromEvent(int eventId, int userId) {
        SlimResult result = new SlimResult(null);
        DeleteMethod deleteMethod = new DeleteMethod(mServiceBaseURI + "/" + "removeguest" + "/" + eventId);
        NameValuePair userIdPair = new NameValuePair("userId", String.valueOf(userId));
        deleteMethod.setQueryString(new NameValuePair[]{userIdPair});
        try {
            int status = mHttpClient.executeMethod(deleteMethod);
            result.setStatus(status);
            if (status == HttpStatus.SC_OK) {
                Event event = (Event) unmarshall(deleteMethod.getResponseBodyAsString(), Event.class);
                result.setResultContent(event);
            }
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_IO + "remove guest from event", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(EventService.class.getName()).log(Level.SEVERE, ERROR_JAXB_LOCATION, ex);
        }
         whatAmIDoing(result, deleteMethod.getPath());
        return result;
    }

    public SlimResult<Event> removeGuestsFromEvent(int eventId, List<Integer> userIds) {
        if (userIds == null) {
            throw new NullPointerException("Userids must not be null!");
        }

        SlimResult result = new SlimResult(null);
        DeleteMethod deleteMethod = new DeleteMethod(mServiceBaseURI + "/" + "removeguests" + "/" + eventId);
        ArrayList<NameValuePair> pairs = new ArrayList<>();
        for (Integer id : userIds) {
            pairs.add(new NameValuePair("userIds", String.valueOf(id)));
        }

        deleteMethod.setQueryString(pairs.toArray(new NameValuePair[pairs.size()]));
        deleteMethod.setRequestHeader("Accept", mMediaType.getValue());

        try {
            int status = mHttpClient.executeMethod(deleteMethod);
            result.setStatus(status);
            if (status == HttpStatus.SC_OK) {
                Event event = (Event) unmarshall(deleteMethod.getResponseBodyAsString(), Event.class);
                result.setResultContent(event);
            }
        } catch (IOException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_IO + "remove guests from event", ex);
        } catch (JAXBException ex) {
            Logger.getLogger(UserService.class.getName()).log(Level.SEVERE, ERROR_JAXB_LOCATION, ex);
        }
         whatAmIDoing(result, deleteMethod.getPath());
        return result;
    }
}
