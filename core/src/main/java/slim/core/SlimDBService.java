/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core;

import slim.core.model.User;
import slim.core.model.Event;
import java.util.List;
import slim.core.model.GuestEntry;

/**
 *
 * @author Robert
 */
public interface SlimDBService {
    
    User getUserById(int id);
    User createUser(User user);
    boolean saveUser(User user);
    boolean deleteUserById(int id);
    List<User> getAllUsers();
    
    Event getEventById(int id);
    Event createEvent(Event event);
    boolean saveEvent(Event event);
    GuestEntry addGuestToEvent(int eventId, int userId);
    List<GuestEntry> addGuestsToEvent(int eventId, int[] userIds);
    boolean deleteEventById(int id);
    List<Event> getAllEvents();
    
    List<Event> getEventsWithUser(int id);
}
