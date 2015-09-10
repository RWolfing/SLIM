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
 *
 * @author Robert
 */
public class SlimDBServiceImpl extends SlimDB implements SlimDBService {

    public SlimDBServiceImpl() {
        super();
    }

    public SlimDBServiceImpl(String databaseName) {
        super(databaseName);
    }

    @Override
    public User getUserById(int id) {
        if (!open()) {
            return null;
        }
        User user = null;
        try {
            user = mUserDao.queryForId(id);
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
        return user;
    }

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
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
        return result;
    }

    @Override
    public boolean saveUser(User user) {
        if (!open() || user == null) {
            return false;
        }

        boolean success = false;
        try {
            mUserDao.createOrUpdate(user);
            success = true;
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not save user " + user.getmID(), ex);
        } finally {
            close();
        }
        return success;
    }

    @Override
    public boolean deleteEventById(int id) {
        if (!open()) {
            return false;
        }

        boolean success = false;
        try {
            mEventDao.deleteById(id);
            success = true;
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not delete event by the id " + id, ex);
        } finally {
            close();
        }
        return success;
    }

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

    @Override
    public List<Event> getAllEvents() {
        if (!open()) {
            return null;
        }
        List<Event> result = null;
        try {
            result = mEventDao.queryForAll();
            if (result != null) {
                for (Event event : result) {
                    List<GuestEntry> guestEntries = mGuestListDao.queryBuilder().where().eq(GuestEntry.EVENT_ID_FIELD_NAME, event.getmID()).query();
                    if (guestEntries != null) {
                        for (GuestEntry entry : guestEntries) {
                            event.addGuest(entry.getmGuest());
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

    @Override
    public Event getEventById(int id) {
        if (!open()) {
            return null;
        }
        Event result = null;
        try {
            result = mEventDao.queryForId(id);
            if (result != null) {
                List<GuestEntry> guestEntries = mGuestListDao.queryBuilder().where().eq(GuestEntry.EVENT_ID_FIELD_NAME, result.getmID()).query();
                if (guestEntries != null) {
                    for (GuestEntry entry : guestEntries) {
                        result.addGuest(entry.getmGuest());
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

    @Override
    public Event createEvent(Event event) {
        if (!open() || event == null || event.getmLocation() == null) {
            return null;
        }
        Event result = null;
        try {
            List<User> guests = event.getGuests();
            mEventDao.create(event);
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

    @Override
    public boolean saveEvent(Event event) {
        if (!open() || event == null) {
            return false;
        }

        boolean success = false;
        try {
            mEventDao.createOrUpdate(event);
            List<GuestEntry> entries = mGuestListDao.queryBuilder().where().eq(GuestEntry.EVENT_ID_FIELD_NAME, event.getmID()).query();
            mGuestListDao.delete(entries);
            for (User guest : event.getGuests()) {
                addGuestToEvent(event.getmID(), guest.getmID());
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

    @Override
    public GuestEntry addGuestToEvent(int eventId, int userId) {
        if (!open()) {
            return null;
        }

        Event event = getEventById(eventId);
        User user = getUserById(userId);
        GuestEntry result = null;
        if (event != null || user != null) {
            try {
                GuestEntry guestListEntry = new GuestEntry(event, user);
                mGuestListDao.create(guestListEntry);
                result = guestListEntry;
            } catch (SQLException ex) {
                Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not create guest list entry!", ex);
            }
        }
        return result;
    }

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

    @Override
    public List<Event> getEventsWithUser(int id) {
        if (!open()) {
            return null;
        }

        List<Event> result = new ArrayList<>();
        User user = getUserById(id);

        if (user != null) {
            try {
                List<GuestEntry> guestListEntries = mGuestListDao.queryBuilder().where().eq(GuestEntry.USER_ID_FIELD_NAME, user.getmID()).query();
                if (guestListEntries != null && guestListEntries.size() > 0) {
                    for (GuestEntry entry : guestListEntries) {
                        result.add(mEventDao.queryForId(entry.getmEvent().getmID()));
                    }
                    return result;
                }
            } catch (SQLException ex) {
                Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            } finally {
                close();
            }
        }
        return result;
    }

    @Override
    public Location createLocation(Location location) {
        if (!open() || location == null) {
            return null;
        } else {
        }
        Location result = null;
        try {
            Location existingLoc = retrieveLocation(location.getmLattitude(), location.getmLongitude());
            if (existingLoc != null) {
                return existingLoc;
            } else {
                mLocationDao.create(location);
                result = location;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
        return result;
    }

    @Override
    public List<Location> getLocationWithinBounds(long lattiudeFrom, long longitudeFrom, long lattitudeTo, long longitudeTo) {
        if (!open()) {
            return null;
        }

        List<Location> result = null;
        try {
            result = mLocationDao.queryBuilder().where().between(Location.LATTITUDE_FIELD_NAME, lattiudeFrom, lattitudeTo).and().between(Location.LONGITUDE_FIELD_NAME, longitudeFrom, longitudeTo).query();
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
        return result;
    }

    @Override
    public Location getLocation(int id) {
        if (!open()) {
            return null;
        }

        Location result = null;
        try {
            result = mLocationDao.queryForId(id);
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }

    @Override
    public boolean deleteLocation(int id) {
        if (!open()) {
            return false;
        }

        boolean success = false;
        try {
            Location location = mLocationDao.queryForId(id);
            if (mLocationDao != null) {
                List<Event> eventsWithLocation = mEventDao.queryBuilder().where().eq(Event.LOCATION_FIELD_NAME, location).query();
                if (eventsWithLocation == null || eventsWithLocation.isEmpty()) {
                    mLocationDao.delete(location);
                    success = true;
                } else {
                    success = false;
                    Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.INFO, "The location is still be used by events, canceling deletion...");
                }
            } else {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
        return success;
    }

    private Location retrieveLocation(long lattitude, long longitude) {
        if (!open()) {
            return null;
        }
        Location result = null;
        try {
            result = mLocationDao.queryBuilder().where().eq(Location.LATTITUDE_FIELD_NAME, lattitude).and().eq(Location.LONGITUDE_FIELD_NAME, longitude).queryForFirst();
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
        return result;
    }
}
