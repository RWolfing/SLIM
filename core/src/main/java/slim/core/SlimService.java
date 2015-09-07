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
public interface SlimService {

    void createEvent(Event event);

    void deleteEvent(String id);

    Event getEventById(String id);

    List<Event> getEvents();

    void createUser(User user);

    void deleteUser(String id);

    User getUserById(String id);

    List<User> getUsers();
}
