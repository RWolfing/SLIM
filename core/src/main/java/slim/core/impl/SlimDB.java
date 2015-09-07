/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import slim.core.DatabaseConnection;

/**
 *
 * @author Robert
 */
public class SlimDB implements DatabaseConnection {

    protected Connection mConnection;
    protected Statement mStatement;
    protected PreparedStatement mPreparedStatement;
    protected ResultSet mResultSet;

     /**
     * *** TABLES ***********
     */
    public static final String USER_TABLE_NAME = "user";
    public static final String EVENT_TABLE_NAME = "event";

    /**
     * *** USER TABLE FIELDS ***
     */
    public static final String FIELD_USER_ID = "id";
    public static final String FIELD_USER_NICKNAME = "nickname";
    public static final String FIELD_USER_FIRSTNAME = "firstname";
    public static final String FIELD_USER_LASTNAME = "lastname";
    public static final String FIELD_USER_BIRTHDAY = "birthday";
    public static final String FIELD_USER_ABOUT = "about";

    /**
     * *** EVENT TABLE FIELDS ***
     */
    public static final String FIELD_EVENT_ID = "id";
    public static final String FIELD_EVENT_NAME = "name";
    public static final String FIELD_EVENT_LATTITUDE = "lattitude";
    public static final String FIELD_EVENT_LONGITUDE = "longitude";
    public static final String FIELD_EVENT_BEGIN = "eventbegin";
    public static final String FIELD_EVENT_END = "eventend";
    public static final String FIELD_EVENT_DESCRIPTION = "description";
    public static final String FIELD_EVENT_ORGANIZER = "organizer";

    public SlimDB() {

    }

    public void setStatement(Connection connection, Statement statement) {
        mConnection = connection;
        mStatement = statement;
    }

    @Override
    public boolean open() {
        String slimDB = System.getProperty("user.dir") + "/slim.db";
        String sJdbc = "jdbc:sqlite";
        String sDbUrl = sJdbc + ":" + slimDB;

        try {
            Class.forName("org.sqlite.JDBC");
            mConnection = DriverManager.getConnection(sDbUrl);

            if (mConnection != null) {
                mStatement = mConnection.createStatement();
                return true;
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SlimDB.class.getName()).log(Level.SEVERE, "Class *org.sqlite.JDBC' not found!", ex);
        } catch (SQLException ex) {
            Logger.getLogger(SlimDB.class.getName()).log(Level.SEVERE, "SQL Exception while opening connection!", ex);
        }
        return false;
    }

    @Override
    public boolean close() {
        try {
            if (mStatement != null) {
                mStatement.close();
            }
            if(mPreparedStatement != null){
                mPreparedStatement.close();
            }
            if(mResultSet != null){
                mResultSet.close();
            }
            if(mConnection != null){
                mConnection.close();
            }
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SlimDB.class.getName()).log(Level.SEVERE, "Error while trying to close DB connection!", ex);
        }
        return false;
    }
}
