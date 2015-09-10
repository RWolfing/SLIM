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
import slim.core.model.GuestEntry;
import slim.core.model.Location;
import slim.core.model.User;

/**
 * Base class of the slim database. It provides methods that will be needed by
 * all childs.
 *
 * @author Robert Wolfinger
 */
public class SlimDB implements DatabaseConnection {

    private final String mDatabaseName;

    protected ConnectionSource mConnectionSource;

    //Ormlite daos for mapping
    protected Dao<Event, Integer> mEventDao;
    protected Dao<GuestEntry, Integer> mGuestListDao;
    protected Dao<Location, Integer> mLocationDao;
    protected Dao<User, Integer> mUserDao;

    /**
     * Default constructor that sets the default name of the database
     */
    public SlimDB() {
        mDatabaseName = Constants.DB_NAME;
    }

    /**
     * Constructor that sets the name of the database to the given databaseName
     *
     * @param databaseName name of the database
     */
    public SlimDB(String databaseName) {
        mDatabaseName = databaseName;
    }

    /**
     * Opens a connection to the database
     *
     * @return success
     */
    @Override
    public boolean open() {
        //Check if the connection is valid & open, if so return right away
        if (mConnectionSource != null && mConnectionSource.isOpen()) {
            return true;
        }
        //Else setup the connection
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

    /**
     * Initial setup of the database. The available ormlite daos will be
     * created. All tables that are necessary will be created if they do not
     * exist.
     *
     * @param connectionSource the connectionsource
     * @throws SQLException sql error
     */
    private void setupDatabase(ConnectionSource connectionSource) throws SQLException {
        mEventDao = DaoManager.createDao(connectionSource, Event.class);
        mGuestListDao = DaoManager.createDao(connectionSource, GuestEntry.class);
        mLocationDao = DaoManager.createDao(connectionSource, Location.class);
        mUserDao = DaoManager.createDao(connectionSource, User.class);

        TableUtils.createTableIfNotExists(connectionSource, Event.class);
        TableUtils.createTableIfNotExists(connectionSource, GuestEntry.class);
        TableUtils.createTableIfNotExists(connectionSource, Location.class);
        TableUtils.createTableIfNotExists(connectionSource, User.class);
    }

    /**
     * Deletes all tables of the database
     *
     * @throws SQLException sql error
     */
    public void clearAllTables() throws SQLException {
        if (mConnectionSource != null) {
            TableUtils.clearTable(mConnectionSource, Event.class);
            TableUtils.clearTable(mConnectionSource, GuestEntry.class);
            TableUtils.clearTable(mConnectionSource, Location.class);
            TableUtils.clearTable(mConnectionSource, User.class);
        }
    }

    /**
     * Closes the connection to the database
     *
     * @return success
     */
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
