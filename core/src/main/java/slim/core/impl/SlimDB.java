/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core.impl;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import slim.core.DatabaseConnection;
import slim.core.utils.Constants;
import slim.core.model.Event;
import slim.core.model.GuestList;
import slim.core.model.Location;
import slim.core.model.User;

/**
 *
 * @author Robert
 */
public class SlimDB implements DatabaseConnection {

    private final String mDatabaseName;

    protected ConnectionSource mConnectionSource;

    protected Dao<Event, Integer> mEventDao;
    protected Dao<GuestList, Integer> mGuestListDao;
    protected Dao<Location, Integer> mLocationDao;
    protected Dao<User, Integer> mUserDao;

    public SlimDB() {
        mDatabaseName = Constants.DB_NAME;
    }

    public SlimDB(String databaseName) {
        mDatabaseName = databaseName;
    }

    @Override
    public boolean open() {
        String slimDB = System.getProperty("user.dir") + "/" + mDatabaseName;
        String sJdbc = "jdbc:sqlite";
        String sDbUrl = sJdbc + ":" + slimDB;

        try {
            mConnectionSource = new JdbcConnectionSource(sDbUrl);
            setupDatabase(mConnectionSource);
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(SlimDB.class.getName()).log(Level.SEVERE, "Could not open database connection!", ex);
        }
        return false;
    }

    private void setupDatabase(ConnectionSource connectionSource) throws SQLException {
        mEventDao = DaoManager.createDao(connectionSource, Event.class);
        mGuestListDao = DaoManager.createDao(connectionSource, GuestList.class);
        mLocationDao = DaoManager.createDao(connectionSource, Location.class);
        mUserDao = DaoManager.createDao(connectionSource, User.class);

        TableUtils.createTableIfNotExists(connectionSource, Event.class);
        TableUtils.createTableIfNotExists(connectionSource, GuestList.class);
        TableUtils.createTableIfNotExists(connectionSource, Location.class);
        TableUtils.createTableIfNotExists(connectionSource, User.class);
    }

    @Override
    public boolean close() {
        if (mConnectionSource != null) {
            try {
                mConnectionSource.close();
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(SlimDB.class.getName()).log(Level.SEVERE, "Could not close database connection!", ex);
                return false;
            }
        }
        return false;
    }
}
