package Database;

import Database.Connection.IConnection;
import Database.Connection.SQLiteJDBCDriverConnection;

/**
 * The ConnectionFactory defines an factory pattern for the creation of database connections.
 * This pattern is used for improving the modifyability of the package.
 */
public class ConnectionFactory implements IConnectionFactory {
    private IConnection connection;

    /**
     * Construct a new ConnectionFactory.
     */
    public ConnectionFactory() {
        connection = new SQLiteJDBCDriverConnection();
    }

    @Override
    public IConnection getConnection() {
        return this.connection;
    }

}
