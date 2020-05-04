package EveOnline.DataFetcher;

import Database.Connection.IConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * The TypeIDFetcher provides the logic for fetching type ID's from the eve online database.
 * <p>
 * As this fetching is relevant to the eve online domain this class is packaged within the Eve Online layer of the project,
 * and implements a simple interface for public methods.
 */
public class TypeIDFetcher implements ITypeIDFetcher {
    private final IConnection databaseConnection;

    @Override
    public ArrayList<Integer> getHighPoweredTypeIDs() {
        final int highPoweredEffectID = 12; // The database effect ID of high powered modules
        return getTypeIDsFromComponent(highPoweredEffectID);
    }

    @Override
    public ArrayList<Integer> getMediumPoweredTypeIDs() {
        final int mediumPoweredEffectID = 13; // The database effect ID of medium powered modules
        return getTypeIDsFromComponent(mediumPoweredEffectID);
    }

    @Override
    public ArrayList<Integer> getLowPoweredTypeIDs() {
        final int lowPoweredEffectID = 11; // The database effect ID of low powered modules
        return getTypeIDsFromComponent(lowPoweredEffectID);
    }

    @Override
    public ArrayList<Integer> getRigTypeIDs() {
        final int rigEffectID = 2663; // The database effect ID of rig components
        return getTypeIDsFromComponent(rigEffectID);
    }

    @Override
    public ArrayList<Integer> getShipTypeIDs() {
        String sqlQuery = getShipTypeIDsSQL();
        return fetchTypeIDsFromDatabase(sqlQuery);
    }

    /**
     * Initialize a new TypeIDFetcher provided a database connection.
     *
     * @param connection the database connection the fetcher should use.
     */
    public TypeIDFetcher(IConnection connection) {
        databaseConnection = connection;
    }

    /**
     * Get the Type ID belonging to a component providing its effect ID.
     * Components are meant as both High-, medium- and low-modules while also rigs.
     *
     * @param effectID an Integer representation of the database effect ID.
     * @return an ArrayList of Integers which represents all the type ID belonging to a component.
     */
    private ArrayList<Integer> getTypeIDsFromComponent(int effectID) {
        String sqlQuery = getComponentTypeIDsFromEffectID(effectID);
        return fetchTypeIDsFromDatabase(sqlQuery);
    }

    /**
     * Get the database type ID's which are used for fetching ships and components.
     *
     * @param sqlQuery an String formed as a SQL Query which fetches a list of type ID's
     * @return An ArrayList of type ID's.
     */
    private ArrayList<Integer> fetchTypeIDsFromDatabase(String sqlQuery) {
        ArrayList<Integer> typeIDList = new ArrayList<>();
        try (Connection connection = databaseConnection.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sqlQuery)
        ) {
            while (resultSet.next()) {
                typeIDList.add(resultSet.getInt("typeID"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return typeIDList;
    }

    /**
     * Get a SQL Query for fetching component type ID's providing the database effect ID.
     * Components are meant as both High-, medium- and low-modules while also rigs.
     *
     * @param effectID an Integer which represents the database effect ID
     * @return a String formed as a SQL query for fetching the needed database component type ID's.
     */
    private String getComponentTypeIDsFromEffectID(int effectID) {
        IFeatureStringFactory featureStringFactory = new FeatureStringFactory();
        //Here be dragons
        return "/* Joins the attribute name, value, type name and effect name for all possible modules*/\n" +
                "SELECT DISTINCT c.typeID FROM dgmeffects INNER JOIN (\n" +
                "\t/* Joins the effectID from junction table */\n" +
                "\tSELECT b.attributeName, b.value, b.typeName, b.typeID, b.attributeID, dgmtypeeffects.effectID FROM dgmtypeeffects INNER JOIN (\n" +
                "\t\t/* Joins the attributeName from dgmattribs table */\n" +
                "\t\tSELECT attributes.attributeName, a.value, a.typeName, a.typeID, a.attributeID FROM dgmattribs as attributes INNER JOIN (\n" +
                "\t\t\t/* Links values and attributeID, typeName and typeID for all modules */\n" +
                "\t\t\t/* Joins the invtypes table with the dgmtypeattribs table */\n" +
                "\t\t\tSELECT details.value, details.attributeID, moduleids.typeName, moduleids.typeID FROM dgmtypeattribs AS details INNER JOIN (\n" +
                "\t\t\t\t/* Find all typeID and name of modules */\n" +
                "\t\t\t\tSELECT typeID, typeName FROM invtypes INNER JOIN (\n" +
                "\t\t\t\t\tSELECT groupID FROM invgroups WHERE categoryID = (\n" +
                "\t\t\t\t\t\tSELECT categoryID FROM invcategories WHERE categoryName = \"Module\"\n" +
                "\t\t\t\t\t)\n" +
                "\t\t\t\t) AS groups\n" +
                "\t\t\t\tON invtypes.groupID = groups.groupID\n" +
                "\t\t\t) AS moduleids\n" +
                "\t\t\tON details.typeID = moduleids.typeID\n" +
                "\t\t) AS a\n" +
                "\t\tON attributes.attributeID = a.attributeID\n" +
                featureStringFactory.fetchFeatureData() +
                "\t) AS b\n" +
                "\tON dgmtypeeffects.typeID = b.typeID\n" +
                ") AS c\n" +
                "ON dgmeffects.effectID = c.effectID AND c.typeID IN (SELECT dgmtypeeffects.typeID FROM dgmtypeeffects WHERE effectID = " + effectID + ");";
    }

    /**
     * Get a SQL Query for fetching the database ship type ID's.
     *
     * @return a String formed as a SQL Query for fetching the needed database ship type ID's.
     */
    private String getShipTypeIDsSQL() {
        // The database category ID of the ship category
        int shipCategoryID = 6;
        return "SELECT invtypes.typeID FROM invtypes WHERE invtypes.groupID IN (\n" +
                "SELECT invgroups.groupID FROM invgroups WHERE categoryID = " + shipCategoryID + ");";
    }
}
