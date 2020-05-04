package EveOnline.DataFetcher;

import EveOnline.Ship.IShip;

/**
 * The IShipFetcher provides a common interface for fetching eve online ships.
 * <p>
 * As this fetching is relevant to the eve online domain this class is packaged within the Eve Online layer of the project,
 * and implements a simple interface for public methods.
 */
public interface IShipFetcher {
    /**
     * Get a Eve Online Ship which maps to the provided database type ID.
     *
     * @param typeID an Integer which represents a type ID of a Ship in the database
     * @return an IShip with all the data from the database associated.
     */
    IShip getShipByTypeID(int typeID);
}
