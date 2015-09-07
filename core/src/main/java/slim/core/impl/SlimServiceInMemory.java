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
import slim.core.model.User;

/**
 *
 * @author Robert
 */
public class SlimServiceInMemory implements SlimService {

    private DatabaseHelper mDataBaseHelper = new DatabaseHelper();
    private List<User> mUserList = new ArrayList();
    private List<Event> mEventList = new ArrayList();

    @Override
    public void createEvent(Event event) {
        if (event != null) {
            mDataBaseHelper.saveEvent(event);
        }
    }

    @Override
    public void deleteEvent(String id) {
        return;
    }

    @Override
    public void createUser(User user) {
        if (user != null) {
            mUserList.add(user);
        }
    }

    @Override
    public void deleteUser(String id) {
        return;
    }

    @Override
    public List<User> getUsers() {
        return mUserList;
    }

    @Override
    public List<Event> getEvents() {
        return mEventList;
    }

    @Override
    public User getUserById(String id) {
        return null;
    }

    @Override
    public Event getEventById(String id) {
        return null;
    }

}
