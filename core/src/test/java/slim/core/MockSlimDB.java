/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package slim.core;

import java.sql.DriverManager;
import java.sql.SQLException;
import slim.core.impl.DatabaseHelper;
import slim.core.impl.SlimDB;

/**
 * Mockdatabase to test the real database features.
 * 
 * @author Robert
 */
public class MockSlimDB extends DatabaseHelper {

    public static final String MOCK_DB_NAME = "slim_mock_test.db";
    private static final String CLASS_NAME = MockSlimDB.class.getSimpleName();

    /**
     * Public default constructor
     *
     * Creates the mock database.
     */
    public MockSlimDB() {
//TODO /
        // prepare the statement to connect to the test database
        String slimDBPath = System.getProperty("user.dir") + "/" + MOCK_DB_NAME;
        String sJdbc = "jdbc:sqlite";
        String sDbUrl = sJdbc + ":" + slimDBPath;

        try {
            // connect to the created database
            Class.forName("org.sqlite.JDBC");
            mConnection = DriverManager.getConnection(sDbUrl);
            mStatement = mConnection.createStatement();

            mStatement.execute("DROP TABLE IF EXISTS " + SlimDB.USER_TABLE_NAME);
            mStatement.execute("DROP TABLE IF EXISTS " + SlimDB.EVENT_TABLE_NAME);
             
            // Create tables       
            String sqlQuery
                    = "CREATE TABLE " + SlimDB.USER_TABLE_NAME
                    + " (" + SlimDB.FIELD_USER_ID + " INTEGER PRIMARY KEY NOT NULL, "
                    + SlimDB.FIELD_USER_NICKNAME + " TEXT, "
                    + SlimDB.FIELD_USER_FIRSTNAME + " TEXT, "
                    + SlimDB.FIELD_USER_LASTNAME + " TEXT, "
                    + SlimDB.FIELD_USER_ABOUT + " TEXT, "
                    + SlimDB.FIELD_USER_BIRTHDAY + " TEXT"
                    + " );"
                    + "CREATE TABLE " + SlimDB.EVENT_TABLE_NAME
                    + " (" + SlimDB.FIELD_EVENT_ID + " INTEGER PRIMARY KEY NOT NULL, "
                    + SlimDB.FIELD_EVENT_NAME + " TEXT, "
                    + SlimDB.FIELD_EVENT_ORGANIZER + " TEXT, "
                    + SlimDB.FIELD_EVENT_DESCRIPTION + " TEXT, "
                    + SlimDB.FIELD_EVENT_BEGIN + " TEXT, "
                    + SlimDB.FIELD_EVENT_END + " TEXT, "
                    + SlimDB.FIELD_EVENT_LATTITUDE + " TEXT, "
                    + SlimDB.FIELD_EVENT_LONGITUDE + " TEXT"
                    + ");";
            mStatement.executeUpdate(sqlQuery);
         

        } catch (SQLException e) {
            System.err.println("SQLException in " + CLASS_NAME);
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("ClassNotFoundExeption in " + CLASS_NAME);
            System.err.println(e.getMessage());
        }

        setStatement(mConnection, mStatement);
    }

    /**
     * Close the connection to the database
     *
     * @return
     */
    @Override
    public boolean close() {
        return true;
    }

    /**
     * Open the connection to the database
     *
     * @return
     */
    @Override
    public boolean open() {
        return true;
    }

    /**
     * close the connection
     */
    public void closeConnection() {
        try {
            mConnection.close();
            mStatement.close();
        } catch (SQLException e) {
            System.err.println("Could not close connection in " + CLASS_NAME);
        }
    }

}
