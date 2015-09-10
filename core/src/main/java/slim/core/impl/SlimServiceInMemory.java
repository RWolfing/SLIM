/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core.impl;

import java.util.ArrayList;
import java.util.List;
import slim.core.model.Event;
import slim.core.SlimService;
import slim.core.model.Location;
import slim.core.model.User;

/**
 *
 * @author Robert
 */
public class SlimServiceInMemory implements SlimService {

    private SlimDBServiceImpl mDataBaseHelper;

    public SlimServiceInMemory() {
        mDataBaseHelper = new SlimDBServiceImpl();
    }

    @Override
    public Event createEvent(String name, long lattitude, long longitude, long eventBegin, long eventEnd, String description, int organizerId) {
        User organizer = mDataBaseHelper.getUserById(organizerId);

        if (organizer != null) {
            Location location = mDataBaseHelper.createLocation(new Location(lattitude, longitude));
            if (location != null) {
                Event event = new Event(name, location, eventBegin, eventEnd, description, organizer);
                return mDataBaseHelper.createEvent(event);
            }
        }
        return null;
    }

    @Override
    public boolean deleteEvent(int id) {
        return mDataBaseHelper.deleteEventById(id);
    }

    @Override
    public Event getEventById(int id) {
        return mDataBaseHelper.getEventById(id);
    }

    @Override
    public List<Event> getEvents() {
        return mDataBaseHelper.getAllEvents();
    }

    @Override
    public User createUser(String nickname, String firstName, String lastName, long birthday, String about, String imageUrl) {
        User user = new User(nickname, firstName, lastName, birthday, about, imageUrl);
        return mDataBaseHelper.createUser(user);
    }

    @Override
    public void deleteUser(int id) {
        mDataBaseHelper.deleteUserById(id);
    }

    @Override
    public User getUserById(int id) {
        return mDataBaseHelper.getUserById(id);
    }

    @Override
    public List<User> getUsers() {
        return mDataBaseHelper.getAllUsers();
    }

    @Override
    public void setDatabase(SlimDBServiceImpl mSlimDatabase) {
        mDataBaseHelper = mSlimDatabase;
    }

    @Override
    public boolean updateEvent(Event event) {
        return mDataBaseHelper.saveEvent(event);
    }

    @Override
    public boolean updateUser(User user) {
        return mDataBaseHelper.saveUser(user);
    }

    @Override
    public List<Event> getEventsFromUser(int id) {
        return mDataBaseHelper.getEventsFromUser(id);
    }

    @Override
    public List<Event> getEventsWithinLocation(long lattitudeFrom, long lattitudeTo, long longitudeFrom, long longitudeTo) {
        List<Location> locations = mDataBaseHelper.getLocationWithinBounds(lattitudeFrom, longitudeFrom, lattitudeTo, longitudeTo);
        List<Event> result = new ArrayList<>();
        if (locations != null) {
            for (Location location : locations) {
                result.addAll(location.getmEvents());
            }
            return result;
        }
        return null;
    }

    @Override
    public List<Event> getEventsWithinGuestRange(int fromMin, int toMax) {
        List<Event> events = mDataBaseHelper.getAllEvents();
        List<Event> result = new ArrayList<>();
        if (events != null) {
            for (Event event : events) {
                if (event.getGuests().size() <= toMax && event.getGuests().size() >= fromMin) {
                    result.add(event);
                }
            }
            return result;
        }
        return null;
    }

    @Override
    public boolean doesHePartyWithMe(int idMe, int idHim) {
        List<Event> myEvents = mDataBaseHelper.getEventsWithUser(idMe);
        List<Event> hisEvents = mDataBaseHelper.getEventsWithUser(idHim);
        if (myEvents != null && hisEvents != null) {

            myEvents.retainAll(hisEvents);
            return myEvents.size() > 0;
        }
        return false;
    }

    @Override
    public List<Event> getEventsWithUser(int id) {
        return mDataBaseHelper.getEventsWithUser(id);
    }
}
