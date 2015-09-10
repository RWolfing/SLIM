
package slim.core;

/**
 * Interface to open & close a database connection.
 * 
 * @author Robert Wolfinger
 */
public interface DatabaseConnection {
    boolean open();
    boolean close();
}
