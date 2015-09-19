package slim.core.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import slim.core.model.Event;
import slim.core.SlimService;
import slim.core.model.Location;
import slim.core.model.User;

/**
 * Implementation of the SlimService
 *
 * @see SlimService
 * @author Robert Wolfinger
 */
public class SlimServiceInMemory implements SlimService {

    //Service to access the database
    private SlimDBServiceImpl mSlimDatabase;

    /**
     * Default constructor
     */
    public SlimServiceInMemory() {
        mSlimDatabase = new SlimDBServiceImpl();
    }

    /**
     * Creates a event from the given parameters and saves it into the database
     *
     * @param name name of the event
     * @param lattitude lattitude of the event
     * @param longitude longitude of the event
     * @param eventBegin begin of the event
     * @param eventEnd end of the event
     * @param description description of the event
     * @param organizerId id of the organizer of the event
     * @return the created event or null
     */
    @Override
    public Event createEvent(String name, long lattitude, long longitude, long eventBegin, long eventEnd, String description, int organizerId) {
        User organizer = mSlimDatabase.getUserById(organizerId);

        //Organizer must exsit in the 
        if (organizer != null) {
            Location location = mSlimDatabase.createLocation(new Location("auto-generated", lattitude, longitude));
            if (location != null) {
                Event event = new Event(name, location, eventBegin, eventEnd, description, organizer);
                return mSlimDatabase.createEvent(event);
            }
        }
        return null;
    }

    /**
     * Updates the given event in the database
     *
     * @param event event to be updated
     * @return success
     */
    @Override
    public boolean updateEvent(Event event) {
        return mSlimDatabase.saveEvent(event);
    }

    /**
     * Deletes the event in the databse with the given id
     *
     * @param id id of the event to delete
     * @return success
     */
    @Override
    public boolean deleteEvent(int id) {
        return mSlimDatabase.deleteEventById(id);
    }

    /**
     * Retrieves a event from the database with the given id
     *
     * @param id id of the event
     * @return the event or null
     */
    @Override
    public Event getEventById(int id) {
        return mSlimDatabase.getEventById(id);
    }

    /**
     * Retrieves a list of all events in the database
     *
     * @return a list of events
     */
    @Override
    public List<Event> getEvents() {
        return mSlimDatabase.getAllEvents();
    }

    /**
     * Creates a new user in the database with the given parameters
     *
     * @param nickname nickname of the user
     * @param firstName firstname of the user
     * @param lastName lastname of the user
     * @param birthday birthday of the user in timemillis
     * @param about about of the user
     * @param imageUrl imageurl of the user
     * @return the created user or null
     */
    @Override
    public User createUser(String nickname, String firstName, String lastName, long birthday, String about, String imageUrl) {
        User user = new User(nickname, firstName, lastName, birthday, about, imageUrl);
        return mSlimDatabase.createUser(user);
    }

    /**
     * Updates the given user in the database
     *
     * @param user user to be updated
     * @return success
     */
    @Override
    public boolean updateUser(User user) {
        return mSlimDatabase.saveUser(user);
    }

    /**
     * Deletes the user with the given id in the database
     *
     * @param id id of the user
     * @return success
     */
    @Override
    public boolean deleteUser(int id) {
        return mSlimDatabase.deleteUserById(id);
    }

    /**
     * Retrieves the user with the given id from the database
     *
     * @param id id of the user
     * @return the user or null
     */
    @Override
    public User getUserById(int id) {
        return mSlimDatabase.getUserById(id);
    }

    /**
     * Retrieves a list of all users in the database
     *
     * @return a list of users
     */
    @Override
    public List<User> getUsers() {
        return mSlimDatabase.getAllUsers();
    }

    /**
     * Creates a location with the given parameters and saves it into the
     * database
     *
     * @param locationName name of the location
     * @param lattitude lattitude of the location
     * @param longitude longitude of the location
     * @return the created location or null
     */
    @Override
    public Location createLocation(String locationName, long lattitude, long longitude) {
        return mSlimDatabase.createLocation(new Location(locationName, lattitude, longitude));
    }

    /**
     * Updates the given location in the database
     *
     * @param location location to be updated
     * @return success
     */
    @Override
    public boolean updateLocation(Location location) {
        return mSlimDatabase.saveLocation(location);
    }

    /**
     * Retrieves a location in the database with the given id
     *
     * @param id id of the location
     * @return the location or null
     */
    @Override
    public Location getLocationById(int id) {
        return mSlimDatabase.getLocation(id);
    }

    /**
     * Retrieves the location with the given lattitude and longitude from the
     * database
     *
     * @param lattitude lattitude of the location
     * @param longitude longitude of the location
     * @return a location
     */
    @Override
    public Location retrieveLocationByLongLat(long lattitude, long longitude) {
        return mSlimDatabase.retrieveLocation(lattitude, longitude);
    }

    /**
     * Deletes the location in the databse with the given id
     *
     * @param locationId id of the location to delete
     * @return success
     */
    @Override
    public boolean deleteLocation(int locationId) {
        return mSlimDatabase.deleteLocation(locationId);
    }

    /**
     * Retrieves a list of events where the user with the given id is a guest
     *
     * @param id id of the user
     * @return a list of events
     */
    @Override
    public List<Event> getEventsWithUser(int id) {
        return mSlimDatabase.getEventsWithUser(id);
    }

    /**
     * Retrieves all events from the database that have the user with the given
     * id as an organizer
     *
     * @param id id of the user
     * @return a list of events
     */
    @Override
    public List<Event> getEventsFromUser(int id) {
        return mSlimDatabase.getEventsFromUser(id);
    }

    /**
     * Retrieves all events from the database that have a location which matches
     * the given bounds
     *
     * @param lattitudeFrom min lattitude
     * @param lattitudeTo max lattitude
     * @param longitudeFrom min longitude
     * @param longitudeTo max longitude
     * @return a list of events
     */
    @Override
    public List<Event> getEventsWithinLocation(long lattitudeFrom, long lattitudeTo, long longitudeFrom, long longitudeTo) {
        //Retrieve all locations that match the bounds
        List<Location> locations = mSlimDatabase.getLocationWithinBounds(lattitudeFrom, longitudeFrom, lattitudeTo, longitudeTo);
        List<Event> result = new ArrayList<>();
        if (locations != null) {
            //Retrieve all events of the found locations
            for (Location location : locations) {
                result.addAll(location.getEvents());
            }
            return result;
        }
        return null;
    }

    /**
     * Retrieves a list of events from the database that have guests from the
     * given fromMin to the given toMax
     *
     * @param fromMin min guests
     * @param toMax max guests
     * @return a list of events
     */
    @Override
    public List<Event> getEventsWithinGuestRange(int fromMin, int toMax) {
        List<Event> events = mSlimDatabase.getAllEvents();
        List<Event> result = new ArrayList<>();
        if (events != null) {
            for (Event event : events) {
                //Check if guest size matches
                if (event.getGuests().size() <= toMax && event.getGuests().size() >= fromMin) {
                    result.add(event);
                }
            }
            return result;
        }
        return null;
    }

    /**
     * Checks if the user with the given idMe is a guest in one or more events
     * that match events in which the user with idHim is a guest
     *
     * @param idMe id of the user1
     * @param idHim id of the user2
     * @return are they guest in the same event
     */
    @Override
    public boolean doesHePartyWithMe(int idMe, int idHim) {
        List<Event> myEvents = mSlimDatabase.getEventsWithUser(idMe);
        List<Event> hisEvents = mSlimDatabase.getEventsWithUser(idHim);
        if (myEvents != null && hisEvents != null) {

            myEvents.retainAll(hisEvents);
            return myEvents.size() > 0;
        }
        return false;
    }

    /**
     * Sets the database service to use
     *
     * @param slimDatabase the service
     */
    @Override
    public void setDatabase(SlimDBServiceImpl slimDatabase) {
        mSlimDatabase = slimDatabase;
    }
    
    /**
     * Helper method for debug purposes only, to drop the database
     * 
     * @return success
     */
    @Override
    public boolean dropAllTables() {
        try {
            mSlimDatabase.clearAllTables();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SlimServiceInMemory.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}
