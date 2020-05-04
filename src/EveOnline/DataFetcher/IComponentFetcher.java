package EveOnline.DataFetcher;

import EveOnline.Component.IComponent;

/**
 * The IComponentFetcher provides a common interface for fetching eve online components.
 * Components are defines as both high-, medium-, and low-powered modules while also Rigs.
 * <p>
 * As this fetching is relevant to the eve online domain this class is packaged within the Eve Online layer of the project,
 * and implements a simple interface for public methods.
 */
public interface IComponentFetcher {
    /**
     * Get the eve online component which maps to the provided database type ID.
     *
     * @param typeID an Integer which represents a type ID within the eve online database.
     * @return an IComponent which consists of all data mapped to a given component within eve online.
     */
    IComponent getComponentByTypeID(int typeID);
}
