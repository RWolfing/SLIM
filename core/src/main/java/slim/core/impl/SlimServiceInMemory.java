/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core.impl;

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
    public Event createEvent(String name, long lattitude, long longitude, long eventBegin, long eventEnd, String description, User organizer) {
        Location location = new Location(lattitude, longitude);
        Event event = new Event(name, location, eventBegin, eventEnd, description, organizer);
        return mDataBaseHelper.createEvent(event);
    }

    //TODO return
    @Override
    public void deleteEvent(int id) {
        mDataBaseHelper.deleteEventById(id);
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
}
