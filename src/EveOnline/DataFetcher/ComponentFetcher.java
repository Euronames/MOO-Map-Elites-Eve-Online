package EveOnline.DataFetcher;

import Database.Connection.IConnection;
import EveOnline.Component.Component;
import EveOnline.Component.IComponent;
import EveOnline.Component.Quality;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * The ComponentFetcher provides the logic for fetching eve online components from the eve online database.
 * A component is defined as both high-, medium-, and low-powered modules while also Rigs within the Eve Online domain.
 * <p>
 * As this fetching is relevant to the eve online domain this class is packaged within the Eve Online layer of the project,
 * and implements a simple interface for public methods.
 */
public class ComponentFetcher implements IComponentFetcher {
    private final IConnection databaseConnection;

    /**
     * Initialize a ComponentFetcher with a database connection.
     *
     * @param databaseConnection a database connection for fetching the eve online components.
     */
    public ComponentFetcher(IConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    @Override
    public IComponent getComponentByTypeID(int typeID) {
        String typeNameSql = getTypeNameByTypeIDSQL(typeID);
        String componentSql = getComponentByTypeIDSSQL(typeID);

        IComponent component = new Component();

        try (Connection connection = this.databaseConnection.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(componentSql)
        ) {
            while (resultSet.next()) {
                String attributeName = resultSet.getString("attributeName");
                int value = resultSet.getInt("value");

                component.addQuality(new Quality(attributeName, value));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try (Connection connection = this.databaseConnection.connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(typeNameSql)
        ) {
            component.addTypeName(resultSet.getString("typeName"));

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return component;
    }


    /**
     * Get an SQL Query for fetching a single eve online component given a type ID.
     *
     * @param typeID an integer which represents the type ID of a component within the eve online database.
     * @return A String which is formatted as a SQL Query for fetching a eve online component.
     */
    private String getComponentByTypeIDSSQL(int typeID) {
        return "/* Joins the attributeName from dgmattribs table */\n" +
                "SELECT attributes.attributeName, a.value FROM dgmattribs as attributes INNER JOIN (\n" +
                "  /* Links values and attributeID, typeName and typeID for all modules */\n" +
                "  /* Joins the invtypes table with the dgmtypeattribs table */\n" +
                "  SELECT details.value, details.attributeID, moduleids.typeName, moduleids.typeID FROM dgmtypeattribs AS details INNER JOIN (\n" +
                "    /* Find all typeID and name of modules */\n" +
                "    SELECT typeID, typeName FROM invtypes INNER JOIN (\n" +
                "      SELECT groupID FROM invgroups WHERE categoryID = (\n" +
                "        SELECT categoryID FROM invcategories WHERE categoryName = \"Module\"\n" +
                "      )\n" +
                "    ) AS groups\n" +
                "      ON invtypes.groupID = groups.groupID\n" +
                "  ) AS moduleids\n" +
                "    ON details.typeID = moduleids.typeID\n" +
                ") AS a\n" +
                "  ON attributes.attributeID = a.attributeID AND a.typeID = " + typeID + ";";
    }

    /**
     * Get the type name of a component using a type ID
     *
     * @param typeID an database key
     * @return the type name which belongs to the provided type id
     */
    private String getTypeNameByTypeIDSQL(int typeID) {
        return "Select typeName from invtypes where typeID = " + typeID + ";";
    }
}
