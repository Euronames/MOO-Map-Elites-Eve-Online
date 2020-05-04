package Database;

import Database.Connection.IConnection;

/**
 * The IConnectionFactory defines a public Interface for the ConnectionFactory class of the database package.
 */
public interface IConnectionFactory {
    /**
     * Get the provided connection for the database.
     *
     * @return an IConnection which defines the public Interface for an connection.
     */
    IConnection getConnection();
}
