/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import slim.core.SlimDBService;
import slim.core.model.Event;
import slim.core.model.GuestEntry;
import slim.core.model.Location;
import slim.core.model.User;

/**
 * Implementation of the SlimDBService. This class contains all the logic for
 * read & write operations to the database. The slim database must be accessed
 * through this implementation to keep the data consistent.
 *
 * @see SlimDBService
 * @author Robert Wolfinger
 */
public class SlimDBServiceImpl extends SlimDB implements SlimDBService {

    /**
     * Default constructor
     */
    public SlimDBServiceImpl() {
        super();
    }

    /**
     * Constructor that connects to a database with the given databaseName
     *
     * @param databaseName name of the database
     */
    public SlimDBServiceImpl(String databaseName) {
        super(databaseName);
    }

    /**
     * Creates a user entry in the database from the given user
     *
     * @param user user that should be stored in the database
     * @return the stored user or null
     */
    @Override
    public User createUser(User user) {
        if (!open() || user == null) {
            return null;
        }
        User result = null;
        try {
            mUserDao.create(user);
            result = user;
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not create user; " + user, ex);
        } finally {
            close();
        }
        return result;
    }

    /**
     * Saves the given id by overwriting the name field of the location with the
     * same id. This method should only be used if the same location already
     * exists.
     *
     * @param location location that changed & should be saved
     * @return success
     */
    @Override
    public boolean saveLocation(Location location) {
        if (!open() || location == null) {
            return false;
        }

        boolean success = false;

        try {
            //Only update the name of a location
            Location existingLoc = mLocationDao.queryForId(location.getID());
            if (existingLoc != null) {
                existingLoc.setName(location.getName());
                mLocationDao.update(existingLoc);
                success = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not update name of the location!", ex);
        }
        return success;
    }

    /**
     * Saves the given user by overwriting the stored user with the same id.
     * This method should only be used if the same user already exists.
     *
     * @param user user that changed and should be saved
     * @return success
     */
    @Override
    public boolean saveUser(User user) {
        if (!open() || user == null) {
            return false;
        }

        boolean success = false;
        try {
            mUserDao.update(user);
            success = true;
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not save user " + user.getID(), ex);
        } finally {
            close();
        }
        return success;
    }

    /**
     * Retrieves a user with the given id
     *
     * @param id id of the user
     * @return a user or null
     */
    @Override
    public User getUserById(int id) {
        if (!open()) {
            return null;
        }
        User user = null;
        try {
            user = mUserDao.queryForId(id);
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not retrieve user with id: " + id, ex);
        } finally {
            close();
        }
        return user;
    }

    /**
     * Deletes the user with the given id in the database
     *
     * @param id id of the user
     * @return success
     */
    @Override
    public boolean deleteUserById(int id) {
        if (!open()) {
            return false;
        }

        try {
            mUserDao.deleteById(id);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not delete user with id " + id, ex);
        } finally {
            close();
        }
        return false;
    }

    /**
     * Retrieves a list of all users that are stored in the database
     *
     * @return a list of users
     */
    @Override
    public List<User> getAllUsers() {
        if (!open()) {
            return null;
        }
        List<User> result = null;
        try {
            result = mUserDao.queryForAll();
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not retrieve a list of all users!", ex);
        } finally {
            close();
        }
        return result;
    }

    /**
     * Creates a event entry in the database from the given event
     *
     * @param event that should be stored in the database
     * @return the created & stored event or null
     */
    @Override
    public Event createEvent(Event event) {
        if (!open() || event == null || event.getLocation() == null) {
            return null;
        }
        Event result = null;
        try {
            List<User> guests = event.getGuests();
            mEventDao.create(event);
            /**
             * As ormlite does not support many to many foreign collections. So
             * we have an extra table to store the connection of a guest (User)
             * and an event. For explanation and source @see
             * https://github.com/j256/ormlite-jdbc/tree/master/src/test/java/com/j256/ormlite/examples/manytomany
             */
            for (User guest : guests) {
                GuestEntry guestListEntry = new GuestEntry(event, guest);
                mGuestListDao.create(guestListEntry);
            }
            result = event;
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not create event!", ex);
        } finally {
            close();
        }
        return result;
    }

    /**
     * Saves the given event by overwriting the stored event with the same id.
     * This method should only be used if the event already exists in the
     * database.
     *
     * @param event event that changed and should be saved
     * @return success
     */
    @Override
    public boolean saveEvent(Event event) {
        if (!open() || event == null) {
            return false;
        }

        boolean success = false;
        try {
            mEventDao.update(event);
            /**
             * The guests may have changed, so we query for all guest entries
             * delete & recreated them with the new data.
             */
            List<GuestEntry> entries = mGuestListDao.queryBuilder().where().eq(GuestEntry.EVENT_ID_FIELD_NAME, event.getID()).query();
            mGuestListDao.delete(entries);
            for (User guest : event.getGuests()) {
                //Create the guest entries again
                addGuestToEvent(event.getID(), guest.getID());
            }
            success = true;
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } finally {
            close();
        }
        return success;
    }

    /**
     * Retrieves the event with the given id from the database
     *
     * @param id id of the event
     * @return the retrieved event or null
     */
    @Override
    public Event getEventById(int id) {
        if (!open()) {
            return null;
        }
        Event result = null;
        try {
            result = mEventDao.queryForId(id);
            if (result != null) {
                /**
                 * We have to query for all guests. For explanation @see
                 * getAllEvents()
                 */
                List<GuestEntry> guestEntries = mGuestListDao.queryBuilder().where().eq(GuestEntry.EVENT_ID_FIELD_NAME, result.getID()).query();
                if (guestEntries != null) {
                    for (GuestEntry entry : guestEntries) {
                        result.addGuest(entry.getGuest());
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not retrieve event with id " + id, ex);
        } finally {
            close();
        }
        return result;
    }

    /**
     * Deletes the event in the database with the given id. An id of an event
     * that does not exist in the database will be ignored. To keep the data
     * consistent all guest entries of the given event will be deleted too.
     *
     * @param id id of the event
     * @return success
     */
    @Override
    public boolean deleteEventById(int id) {
        if (!open()) {
            return false;
        }

        boolean success = false;
        try {
            Event eventToDelete = mEventDao.queryForId(id);
            if (eventToDelete != null) {
                /**
                 * We have to delete all guest list entries for this event in
                 * the database. Otherwise we will have dead data.
                 */
                List<GuestEntry> guestEntries = mGuestListDao.queryBuilder().where().eq(GuestEntry.EVENT_ID_FIELD_NAME, eventToDelete.getID()).query();
                if (guestEntries != null) {
                    mGuestListDao.delete(guestEntries);
                }
                mEventDao.deleteById(id);
                success = true;
            } else {
                success = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not delete event by the id " + id, ex);
        } finally {
            close();
        }
        return success;
    }

    /**
     * Retrieves a list of all events that are stored in the database
     *
     * @return a list of events
     */
    @Override
    public List<Event> getAllEvents() {
        if (!open()) {
            return null;
        }
        List<Event> result = null;
        try {
            result = mEventDao.queryForAll();
            if (result != null) {

                /**
                 * As ormlite does not support many to many foreign collections
                 * we have to query for all guests of every event and add them
                 * by hand.
                 */
                for (Event event : result) {
                    //Retrieve a list of all guestentries for this event
                    List<GuestEntry> guestEntries = mGuestListDao.queryBuilder().where().eq(GuestEntry.EVENT_ID_FIELD_NAME, event.getID()).query();
                    if (guestEntries != null) {
                        for (GuestEntry entry : guestEntries) {
                            //add every guest to the event
                            event.addGuest(entry.getGuest());
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not retrieve a list of all events!", ex);
        } finally {
            close();
        }
        return result;
    }

    /**
     * Adds the guest with the given id to the event with the given id.
     *
     * @param eventId id of the event
     * @param userId id of the user
     * @return the created guest entry or null
     */
    @Override
    public GuestEntry addGuestToEvent(int eventId, int userId) {
        if (!open()) {
            return null;
        }

        Event event = getEventById(eventId);
        User user = getUserById(userId);
        GuestEntry result = null;
        //Check if the event & user do exist in the database
        if (event != null && user != null) {
            try {
                //Check if the user is not already a guest of this event
                if (!event.getGuests().contains(user)) {
                    //Create the entry
                    GuestEntry guestListEntry = new GuestEntry(event, user);
                    mGuestListDao.create(guestListEntry);
                    result = guestListEntry;
                }
            } catch (SQLException ex) {
                Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not create guest list entry!", ex);
            }
        }
        return result;
    }

    /**
     * Adds a list of users with the given userIds to the event with the given
     * event id in the database
     *
     * @param eventId id of the event
     * @param userIds ids of the users
     *
     * @return a list of the created guest entries or null
     */
    @Override
    public List<GuestEntry> addGuestsToEvent(int eventId, int[] userIds) {
        if (!open()) {
            return null;
        }

        List<GuestEntry> guestListEntries = new ArrayList();
        for (int userId : userIds) {
            guestListEntries.add(addGuestToEvent(eventId, userId));
        }
        return guestListEntries;
    }

    /**
     * Retrieves all events from the database where the user with the given id
     * is a guest
     *
     * @param id id of the user
     * @return a list of events or null
     */
    @Override
    public List<Event> getEventsWithUser(int id) {
        if (!open()) {
            return null;
        }

        List<Event> result = null;
        User user = getUserById(id);

        //Check if the user exists in the database
        if (user != null) {
            result = new ArrayList<>();
            try {
                //Query for all guestentries of the given user
                List<GuestEntry> guestListEntries = mGuestListDao.queryBuilder().where().eq(GuestEntry.USER_ID_FIELD_NAME, user.getID()).query();
                if (guestListEntries != null) {
                    for (GuestEntry entry : guestListEntries) {
                        result.add(mEventDao.queryForId(entry.getEvent().getID()));
                    }
                    return result;
                }
            } catch (SQLException ex) {
                Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not retrieve guest entries for the user: " + id, ex);
                result = null;
            } finally {
                close();
            }
        }
        return result;
    }

    /**
     * Retrieves all events from the database where the user with the given id
     * is the organizer.
     *
     * @param id id of the user
     * @return a list of events or null
     */
    @Override
    public List<Event> getEventsFromUser(int id) {
        if (getUserById(id) == null) {
            return null;
        }

        List<Event> events = null;
        try {
            //Query for all events that have the user as an organizer
            events = mEventDao.queryBuilder().where().eq(Event.ORGANIZER_FIELD_NAME, id).query();
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not retrieve events from user!", ex);
        }
        return events;
    }

    /**
     * Creates a location entry in the database from the given location
     *
     * @param location location that should be stored
     * @return the created location entry or null
     */
    @Override
    public Location createLocation(Location location) {
        if (!open() || location == null) {
            return null;
        } else {
        }
        Location result = null;
        try {
            //Check if the location already exists
            Location existingLoc = retrieveLocation(location.getLattitude(), location.getLongitude());
            if (existingLoc != null) {
                result = existingLoc;
            } else {
                mLocationDao.create(location);
                result = location;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not create location", ex);
        } finally {
            close();
        }
        return result;
    }

    /**
     * Retrieves all locations from the database that fit in the given bounds
     *
     * @param lattiudeFrom lattitude from min
     * @param longitudeFrom longitude from min
     * @param lattitudeTo lattitude to max
     * @param longitudeTo longitude to max
     * @return a list of locations or null
     */
    @Override
    public List<Location> getLocationWithinBounds(long lattiudeFrom, long longitudeFrom, long lattitudeTo, long longitudeTo) {
        if (!open()) {
            return null;
        }

        List<Location> result = null;
        try {
            //Retrieve all locations that fit in the bounds
            result = mLocationDao.queryBuilder().where().between(Location.LATTITUDE_FIELD_NAME, lattiudeFrom, lattitudeTo).and().between(Location.LONGITUDE_FIELD_NAME, longitudeFrom, longitudeTo).query();
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not retrieve locations in bounds", ex);
        } finally {
            close();
        }
        return result;
    }

    /**
     * Retrieves the location with the given id from the database
     *
     * @param id id of the location
     * @return a location or null
     */
    @Override
    public Location getLocation(int id) {
        if (!open()) {
            return null;
        }

        Location result = null;
        try {
            result = mLocationDao.queryForId(id);
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not retrieve location with id " + id, ex);
        }
        return result;
    }

    /**
     * Deletes the location with the given id. The location will not be deleted
     * if it is part of an existing event.
     *
     * @param id id of the location
     * @return success
     */
    @Override
    public boolean deleteLocation(int id) {
        if (!open()) {
            return false;
        }

        boolean success = false;
        try {
            Location location = mLocationDao.queryForId(id);
            if (mLocationDao != null) {
                //Check if there is an event with the location
                List<Event> eventsWithLocation = mEventDao.queryBuilder().where().eq(Event.LOCATION_FIELD_NAME, location).query();
                if (eventsWithLocation == null || eventsWithLocation.isEmpty()) {
                    mLocationDao.delete(location);
                    success = true;
                } else {
                    success = false;
                    Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.INFO, "The location is still be used by events, canceling deletion...");
                }
            } else {
                success = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not delete location with id " + id, ex);
        } finally {
            close();
        }
        return success;
    }

    /**
     * Retrieves a location with the given lattitude & longitude
     *
     * @param lattitude lattitude of the location
     * @param longitude longitude of the location
     * @return a location or null
     */
    @Override
    public Location retrieveLocation(long lattitude, long longitude) {
        if (!open()) {
            return null;
        }
        Location result = null;
        try {
            //Query for the first location with the lattitude & longitude (there should be just one)
            result = mLocationDao.queryBuilder().where().eq(Location.LATTITUDE_FIELD_NAME, lattitude).and().eq(Location.LONGITUDE_FIELD_NAME, longitude).queryForFirst();
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
        return result;
    }
}
