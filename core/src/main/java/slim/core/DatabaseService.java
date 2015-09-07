/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core;

import slim.core.model.User;
import slim.core.model.Event;
import java.util.List;

/**
 *
 * @author Robert
 */
public interface DatabaseService {
    
    User getUserById(int id);
    boolean saveUser(User user);
    boolean deleteUserById(int id);
    List<User> getAllUsers();
    
    Event getEventById(int id);
    boolean saveEvent(Event event);
    boolean deleteEventById(String id);
    List<Event> getAllEvents();
}
