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
public class DatabaseHelper extends SlimDB implements SlimDBService {

    public DatabaseHelper() {
        super();
    }

    public DatabaseHelper(String databaseName) {
        super(databaseName);
    }

    @Override
    public User getUserById(int id) {
        try {
            return mUserDao.queryForId(id);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } finally {
            close();
        }
        return success;
    }

    @Override
    public boolean deleteEventById(String id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<User> getAllUsers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Event> getAllEvents() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteUserById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Event getEventById(int id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
            success = false;
        } finally {
            close();
        }
        return success;
    }

}
