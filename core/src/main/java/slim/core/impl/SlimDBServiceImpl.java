/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import slim.core.SlimDBService;
import slim.core.model.Event;
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
        try {
            return mUserDao.queryForId(id);
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
        return null;
    }

    @Override
    public User createUser(User user) {
        if (!open() || user == null) {
            return null;
        }
        try {
            mUserDao.create(user);
            return user;
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
        return null;
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
            success = false;
        } finally {
            close();
        }
        return success;
    }

    @Override
    public boolean deleteEventById(int id) {
        if(!open()){
            return false;
        }
        
        boolean success = false;
        try {
            mEventDao.deleteById(id);
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not delete event by the id " + id, ex);
        } finally {
            close();
        }
        return success;
    }

    @Override
    public List<User> getAllUsers() {
        if(!open()){
            return null;
        }
        
        try {
            return mUserDao.queryForAll();
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not retrieve a list of all users!", ex);
        } finally {
            close();
        }
        return null;
    }

    @Override
    public List<Event> getAllEvents() {
        if(!open()){
            return null;
        }
        
        try {
            return mEventDao.queryForAll();
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not retrieve a list of all events!", ex);
        } finally {
            close();
        }
        return null;
    }

    @Override
    public boolean deleteUserById(int id) {
       if(!open()){
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
        if(!open()){
            return null;
        }
        
        try {
            return mEventDao.queryForId(id);
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, "Could not retrieve event with id " + id, ex);
        } finally {
            close();
        }
        return null;
    }

    @Override
    public Event createEvent(Event event) {
        if (!open() || event == null) {
            return null;
        }

        try {
            mEventDao.create(event);
            return event;
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
        return null;
    }

    @Override
    public boolean saveEvent(Event event) {
        if (!open() || event == null) {
            return false;
        }

        boolean success = false;
        try {
            mEventDao.createOrUpdate(event);
            success = true;
        } catch (SQLException ex) {
            Logger.getLogger(SlimDBServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } finally {
            close();
        }
        return success;
    }

}
