package slim.core;

import slim.core.model.User;
import slim.core.model.Event;
import java.util.List;
import slim.core.impl.SlimDBServiceImpl;

/**
 * Interface that defines all available service operations.
 * 
 * @see SlimDBServiceImpl
 * 
 * @author Robert Wolfinger
 */
public interface SlimService {

    Event createEvent(String name, long lattitude, long longitude, long eventBegin, long eventEnd, String description, int organizerID);

    boolean updateEvent(Event event);
    
    boolean deleteEvent(int id);

    Event getEventById(int id);

    List<Event> getEvents();

    User createUser(String nickName, String firstName, String lastName, long birthday, String about, String imageUrl);

    boolean updateUser(User user);
    
    void deleteUser(int id);

    User getUserById(int id);

    List<User> getUsers();
    
    List<Event> getEventsWithUser(int id);
    
    List<Event> getEventsFromUser(int id);
    
    List<Event> getEventsWithinLocation(long lattitudeFrom, long lattitudeTo, long longitudeFrom, long longitudeTo);
    
    List<Event> getEventsWithinGuestRange(int fromMin, int toMax);
    
    boolean doesHePartyWithMe(int idMe, int idHim);

    void setDatabase(SlimDBServiceImpl mSlimDatabase);
}
