package EveOnline.DataFetcher;

import Database.Connection.IConnection;
import EveOnline.Component.Quality;
import EveOnline.Ship.IShip;
import EveOnline.Ship.Ship;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * The ShipFetcher provides the logic for fetching eve online ships from the eve online database.
 * <p>
 * As this fetching is relevant to the eve online domain this class is packaged within the Eve Online layer of the project,
 * and implements a simple interface for public methods.
 */
public class ShipFetcher implements IShipFetcher {
    private final IConnection databaseConnection;

    /**
     * Initialize a new ShipFetcher providing it a database connection.
     *
     * @param connection the database connection provided.
     */
    public ShipFetcher(IConnection connection) {
        databaseConnection = connection;
    }

    @Override
    public IShip getShipByTypeID(int typeID) {
        String shipSql = getShipByTypeIDSQL(typeID);
        String typeNameSql = getTypeNameByTypeIDSQL(typeID);

        int numberOfHighModules = 0;
        int numberOfMediumModules = 0;
        int numberOfLowModules = 0;
        int numberOfRigs = 0;
        double cpu = 0;
        double power = 0;
        double calibration = 0;
        ArrayList<Quality> shipQualities = new ArrayList<>();

        IShip ship = null;

        try (Connection connection = databaseConnection.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(shipSql)
        ) {
            while (resultSet.next()) {
                String attributeName = resultSet.getString("attributeName");
                double value = resultSet.getDouble("value");

                switch (attributeName) {
                    case "lowSlots":
                        numberOfLowModules = (int) value;
                        break;
                    case "medSlots":
                        numberOfMediumModules = (int) value;
                        break;
                    case "hiSlots":
                        numberOfHighModules = (int) value;
                        break;
                    case "rigSlots":
                        numberOfRigs = (int) value;
                        break;
                    case "powerOutput":
                        power = value;
                        break;
                    case "cpuOutput":
                        cpu = value;
                        break;
                    case "upgradeCapacity":
                        calibration = value;
                        break;
                    default:
                        shipQualities.add(new Quality(attributeName, value));
                        break;
                }
            }

            ship = new Ship(numberOfRigs, numberOfHighModules, numberOfMediumModules, numberOfLowModules, power, cpu, calibration);
            for (Quality quality : shipQualities) {
                ship.addQuality(quality);
            }

            try (Connection connection2 = this.databaseConnection.connect();
                 Statement statement2 = connection2.createStatement();
                 ResultSet resultSet2 = statement2.executeQuery(typeNameSql)
            ) {

                ship.addTypeName(resultSet2.getString("typeName"));

            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        if (ship == null) {
            System.out.println("Issue with shipFetcher ship extraction, right there, right there");
        }

        return ship;
    }

    /**
     * Get the SQL Query for fetching a ship which maps to a specific type ID.
     *
     * @param typeID a Integer which represents the ship typeID.
     * @return a String which is formed as a SQL Query.
     */
    private String getShipByTypeIDSQL(int typeID) {
        return "/* ----------------------------------------------------------------- */\n" +
                "/* ----------------------------------------------------------------- */\n" +
                "/* Finds values of ship attributes */\n" +
                "/* Needed variables: value, attributeName */\n" +
                "SELECT attributes.attributeName, details.value FROM  (\n" +
                "\t/* Selects all dgmattribs for ship */\n" +
                "\tSELECT * FROM dgmattribs WHERE attributeID IN (\n" +
                "\t\tSELECT attributeID FROM dgmtypeattribs WHERE typeID = " + typeID + "\n" +
                "\t)\n" +
                ") AS attributes\n" +
                "/* Join dgmattribs with values */\n" +
                "INNER JOIN (\n" +
                "\t/* Selects values for modules */\n" +
                "\tSELECT value, attributeID FROM dgmtypeattribs WHERE typeID = " + typeID + "\n" +
                ")\tAS details\n" +
                "/* Join value from dgmtypeattribs with attributeName of dgmattribs */\n" +
                " ON attributes.attributeID = details.attributeID\n" +
                "/* ----------------------------------------------------------------- */\n" +
                "/* ----------------------------------------------------------------- */";
    }

    /**
     * Get the type name of a ship using a type ID
     *
     * @param typeID an database key
     * @return the type name which belongs to the provided type id
     */
    private String getTypeNameByTypeIDSQL(int typeID) {
        return "Select typeName from invtypes where typeID = " + typeID + ";";
    }
}
