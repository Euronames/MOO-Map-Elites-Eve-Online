package EveOnline.DataFetcher;

import java.util.ArrayList;

/**
 * The ITypeIDFetcher provides a common interface for accessing methods to get the eve online database type ID's.
 * <p>
 * This type ID fetching is meant for fetching high-, medium- and low-powered modules while also Rigs and Ships from the
 * eve online database.
 * <p>
 * As this fetching is relevant to the eve online domain this class is packaged within the Eve Online layer of the project,
 * and implements a simple interface for public methods.
 */
public interface ITypeIDFetcher {
    /**
     * Get all the high powered type IDs from the eve online database.
     *
     * @return an ArrayList<Integer> which represents all the type IDs of all the high powered modules.
     */
    ArrayList<Integer> getHighPoweredTypeIDs();

    /**
     * Get all the medium powered type IDs from the eve online database.
     *
     * @return an ArrayList<Integer> which represents all the type IDs of all the medium powered modules.
     */
    ArrayList<Integer> getMediumPoweredTypeIDs();

    /**
     * Get all the low powered type IDs from the eve online database.
     *
     * @return an ArrayList<Integer> which represents all the type IDs of all the low powered modules.
     */
    ArrayList<Integer> getLowPoweredTypeIDs();

    /**
     * Get all the rig type IDs from the eve online database.
     *
     * @return an ArrayList<Integer> which represents all the type IDs of all the rigs.
     */
    ArrayList<Integer> getRigTypeIDs();

    /**
     * Get all the ship type IDs from the eve online database.
     *
     * @return an ArrayList<Integer> which represents all the type IDs of all the ships.
     */
    ArrayList<Integer> getShipTypeIDs();
}
