package slim.core;

import slim.core.model.User;
import slim.core.model.Event;
import java.util.List;
import slim.core.impl.SlimDBServiceImpl;
import slim.core.model.GuestEntry;
import slim.core.model.Location;

/**
 * Interface that defines all available database operations for the slim
 * database.
 *
 * @see SlimDBServiceImpl
 *
 * @author Robert Wolfinger
 */
public interface SlimDBService {

    User getUserById(int id);

    User createUser(User user);

    boolean saveUser(User user);

    boolean deleteUserById(int id);

    List<User> getAllUsers();

    Location createLocation(Location location);
    
    boolean saveLocation(Location location);

    List<Location> getLocationWithinBounds(long lattiudeFrom, long longitudeFrom, long lattitudeTo, long longitudeTo);
    
    Location getLocation(int id);
    
    Location retrieveLocation(long lattitude, long longitude);

    boolean deleteLocation(int id);

    Event getEventById(int id);

    Event createEvent(Event event);

    boolean saveEvent(Event event);

    GuestEntry addGuestToEvent(int eventId, int userId);

    List<GuestEntry> addGuestsToEvent(int eventId, int[] userIds);

    boolean deleteEventById(int id);

    List<Event> getAllEvents();

    List<Event> getEventsWithUser(int id);

    List<Event> getEventsFromUser(int id);
}
