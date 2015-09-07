/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import slim.core.DatabaseService;
import slim.core.model.Event;
import slim.core.ResultSetMapper;
import slim.core.model.User;

/**
 *
 * @author Robert
 */
public class DatabaseHelper extends SlimDB implements DatabaseService {

    @Override
    public User getUserById(int id) {
        if (!open()) {
            return null;
        }

        User user = null;
        try {

            String sql = "SELECT * FROM " + USER_TABLE_NAME + " WHERE id = '" + id + "';";
            mPreparedStatement = mConnection.prepareStatement(sql);
            ResultSet resultSet = mPreparedStatement.executeQuery();
            ResultSetMapper<User> mapper = new ResultSetMapper<>();
            List<User> result = mapper.mapResultSetToPojo(resultSet, User.class);
            if (result != null) {
                user = result.get(0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, "Could not retrieve user by id!", ex);
        }
        close();
        return user;
    }

    @Override
    public boolean saveUser(User user) {
        if (!open() || user == null) {
            return false;
        }
        String message = "test";
        boolean success = false;
        if (getUserById(user.getID()) != null) {
            //Update the user
            String sql = "UPDATE " + USER_TABLE_NAME + " SET "
                    + FIELD_USER_NICKNAME + "=?, "
                    + FIELD_USER_FIRSTNAME + "=?, "
                    + FIELD_USER_LASTNAME + "=?, "
                    + FIELD_USER_BIRTHDAY + "=?, "
                    + FIELD_USER_ABOUT + "=? "
                    + "WHERE " + FIELD_USER_ID + "=?;";
            try {
                mPreparedStatement = mConnection.prepareStatement(sql);
                mPreparedStatement.setString(1, user.getmNickName());
                mPreparedStatement.setString(2, user.getmFirstName());
                mPreparedStatement.setString(3, user.getmLastName());
                mPreparedStatement.setString(4, user.getmBirthday());
                mPreparedStatement.setString(5, user.getmAbout());
                mPreparedStatement.setInt(6, user.getID());
                mPreparedStatement.executeUpdate();
                success = true;
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, "SQL Exception while updating user!", ex);
            }

        } else {
            //Create the user
            try {
                String sql = "INSERT INTO " + USER_TABLE_NAME + " ("
           
                        + FIELD_USER_NICKNAME + ", "
                        + FIELD_USER_FIRSTNAME + ", "
                        + FIELD_USER_LASTNAME + ", "
                        + FIELD_USER_BIRTHDAY + ", "
                        + FIELD_USER_ABOUT + ") VALUES (?, ?, ?, ?, ?);";
                mPreparedStatement = mConnection.prepareStatement(sql);
 
                mPreparedStatement.setString(1, user.getmNickName());
                mPreparedStatement.setString(2, user.getmFirstName());
                mPreparedStatement.setString(3, user.getmLastName());
                mPreparedStatement.setString(4, user.getmBirthday());
                mPreparedStatement.setString(5, user.getmAbout());
               success = mPreparedStatement.execute();
   
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, "SQL Exception while saving user!", ex);
                message = ex.getMessage();
            }
        }

        close();
        return success;

    }

    @Override
    public boolean deleteUserById(int id
    ) {
        if (!open()) {
            return false;
        }
        boolean success = false;
        try {
            String sql = "DELETE FROM " + USER_TABLE_NAME + " WHERE " + FIELD_USER_ID + " = '" + id + "';";
            mPreparedStatement = mConnection.prepareStatement(sql);
            mPreparedStatement.execute();
            success = true;
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        close();
        return success;
    }

    @Override
    public Event getEventById(int id
    ) {
        if (!open()) {
            return null;
        }
        Event event = null;
        try {
            String sql = "SELECT "
                    + FIELD_EVENT_NAME + ", "
                    + FIELD_EVENT_DESCRIPTION + ", "
                    + FIELD_EVENT_BEGIN + ", "
                    + FIELD_EVENT_END + ", "
                    + FIELD_EVENT_LATTITUDE + ", "
                    + FIELD_EVENT_LONGITUDE
                    + " FROM " + EVENT_TABLE_NAME
                    + " WHERE " + FIELD_EVENT_ID + " = '" + id + "';";
            mPreparedStatement = mConnection.prepareStatement(sql);
            ResultSet resultSet = mPreparedStatement.executeQuery();
            ResultSetMapper<Event> mapper = new ResultSetMapper<>();
            List<Event> list = mapper.mapResultSetToPojo(resultSet, Event.class);
            if (list != null) {
                event = list.get(0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        close();
        return event;
    }

    //TODO Organizer
    @Override
    public boolean saveEvent(Event event
    ) {
        if (!open() || event == null || event.getmOrganizer() == null) {
            return false;
        }

        boolean success = false;
        if (getEventById(event.getmID()) != null) {
            //Update the event
            String sql = "UPDATE " + EVENT_TABLE_NAME + " SET "
                    + FIELD_EVENT_NAME + "=?, "
                    + FIELD_EVENT_BEGIN + "=?, "
                    + FIELD_EVENT_END + "=?, "
                    + FIELD_EVENT_DESCRIPTION + "=?, "
                    + FIELD_EVENT_ORGANIZER + "=? "
                    + "WHERE " + FIELD_EVENT_ID + "=?;";
            try {
                mPreparedStatement = mConnection.prepareStatement(sql);
                mPreparedStatement.setString(1, event.getmName());
                mPreparedStatement.setString(2, event.getmEventBegin());
                mPreparedStatement.setString(3, event.getmEventEnd());
                mPreparedStatement.setString(4, event.getmDescription());
                mPreparedStatement.setInt(5, event.getmOrganizer().getID());
                mPreparedStatement.setInt(6, event.getmID());
                mPreparedStatement.executeUpdate();
                success = true;
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, "SQL Exception while updating event!", ex);
            }
        } else {
            //Create the event
            try {
                String sql = "INSERT INTO " + EVENT_TABLE_NAME + " ("
                        + FIELD_EVENT_ID + ", "
                        + FIELD_EVENT_NAME + ", "
                        + FIELD_EVENT_BEGIN + ", "
                        + FIELD_EVENT_END + ", "
                        + FIELD_EVENT_DESCRIPTION + ", "
                        + FIELD_EVENT_ORGANIZER + ") VALUES (?, ?, ?, ?, ?, ?);";
                mPreparedStatement = mConnection.prepareStatement(sql);

                mPreparedStatement.setInt(1, event.getmID());
                mPreparedStatement.setString(2, event.getmName());
                mPreparedStatement.setString(3, event.getmEventBegin());
                mPreparedStatement.setString(4, event.getmEventEnd());
                mPreparedStatement.setString(5, event.getmDescription());
                mPreparedStatement.setInt(6, event.getmOrganizer().getID());
                success = mPreparedStatement.execute();
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, "SQL Exception while creatin event!", ex);
            }
        }
        close();
        return success;
    }

    @Override
    public boolean deleteEventById(String id
    ) {
        if (!open() || id == null) {
            return false;
        }

        boolean success = false;
        try {
            String sql = "DELETE FROM " + EVENT_TABLE_NAME + " WHERE id = '" + id + "'";
            mPreparedStatement = mConnection.prepareStatement(sql);
            success = mPreparedStatement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, "SQL Exception while deleting event!", ex);
        }

        close();
        return success;
    }

    @Override
    public List<User> getAllUsers() {
        if (!open()) {
            return null;
        }

        List<User> users = null;

        String sql = "SELECT * FROM " + USER_TABLE_NAME + ";";
        try {
            mPreparedStatement = mConnection.prepareStatement(sql);
            ResultSetMapper<User> mapper = new ResultSetMapper<>();
            ResultSet result = mPreparedStatement.executeQuery();
            users = mapper.mapResultSetToPojo(result, User.class);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        close();
        return users;
    }

    @Override
    public List<Event> getAllEvents() {
        if (!open()) {
            return null;
        }

        List<Event> events = null;

        String sql = "SELECT * FROM " + EVENT_TABLE_NAME + ";";
        try {
            mPreparedStatement = mConnection.prepareStatement(sql);
            ResultSetMapper<Event> mapper = new ResultSetMapper<>();
            ResultSet result = mPreparedStatement.executeQuery();
            events = mapper.mapResultSetToPojo(result, Event.class);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseHelper.class.getName()).log(Level.SEVERE, null, ex);
        }
        close();
        return events;
    }

}
