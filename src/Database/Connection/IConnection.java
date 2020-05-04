package Database.Connection;

import java.sql.Connection;

/**
 * The IConnection defines a public interface for the database connection.
 */
public interface IConnection {
    /**
     * Connect to the database and return the Connection.
     *
     * @return A Connection which is the object used for connecting to the database.
     */
    Connection connect();
}
