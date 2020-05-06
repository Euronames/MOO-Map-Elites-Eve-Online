package Database.Connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The SQLiteJDBCDriverConnection defines a class using the SQLite library for connecting to the database.
 */
public class SQLiteJDBCDriverConnection implements IConnection {

    @Override
    public Connection connect() {
        Connection connection = null;
        try {
            // db parameters
            //String url = "jdbc:sqlite:pyfa_eve.db";
            String url = "jdbc:sqlite:/Users/xrosby/Desktop/Git/MOO-Map-Elites-Eve-Online_FUNK/pyfa_eve.db";
            // create a connection to the database
            connection = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return connection;
    }

}