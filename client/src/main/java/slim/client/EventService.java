/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.client;

import java.util.List;
import slim.core.model.Event;

/**
 *
 * @author Robert
 */
public class EventService extends SlimService {
    
    
    public static SlimResult<Event> createEvent(String name, long lattitude, long longitude, long eventBegin, long eventEnd, String description, int organizerID){
        return null;
    }
    
    public static SlimResult<Event> updateEvent(int eventId, String name, long eventBegin, long eventEnd, String description){
        return null;
    }
    
    public static SlimResult deleteEvent(int eventId){
        return null;
    }
    
    public static SlimResult<Event> fetchEventById(int eventId){
        return null;
    }
    
    public static SlimResult<List<Event>> fetchAllEvents(){
        return null;
    }
    
    public static SlimResult<List<Event>> getEventsWithUser(int userID){
        return null;
    }
    
    public static SlimResult<List<Event>> getEventsFromUser(int userId){
        return null;
    }
    
    public static SlimResult<List<Event>> getEventsWithinLocation(long lattitudeFrom, long lattitudeTo, long longitudeFrom, long longitudeTo){
        return null;
    }
    
    public static SlimResult<List<Event>> getEventsWithinGuestRange(int minGuests, int maxGuests){
        return null;
    }
    
    public static SlimResult<Event> addGuestToEvent(int eventId, int userId){
        return null;
    }
    
    public static SlimResult<Event> addGuestsToEvent(int eventId, List<Integer> userIds){
        return null;
    }
    
    public static SlimResult<Event> removeGuestFromEvent(int eventId, int userId){
        return null;
    }
    
    public static SlimResult<Event> removeGuestsFromEvent(int eventId, List<Integer> userIds){
        return null;
    }
}
