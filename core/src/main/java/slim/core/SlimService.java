/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core;

import slim.core.model.User;
import slim.core.model.Event;
import java.util.List;
import slim.core.impl.SlimDBServiceImpl;

/**
 *
 * @author Robert
 */
public interface SlimService {

    Event createEvent(String name, long lattitude, long longitude, long eventBegin, long eventEnd, String description, User organizer);

    void deleteEvent(int id);

    Event getEventById(int id);

    List<Event> getEvents();

    User createUser(String nickname, String firstName, String lastName, long birthday, String about, String imageUrl);

    void deleteUser(int id);

    User getUserById(int id);

    List<User> getUsers();

    void setDatabase(SlimDBServiceImpl mSlimDatabase);
}
