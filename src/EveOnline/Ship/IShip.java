package EveOnline.Ship;

import EveOnline.Component.ComponentName;
import EveOnline.Component.IComponent;
import EveOnline.Component.IQuality;
import EveOnline.Feature.IShipFeatureDescription;
import MapElite.ISolution;

import java.util.ArrayList;

/**
 * The IShip interface provides a common interface for handling solutions within the Eve Online layer.
 * <p>
 * The IShip extends the ISoultion interface as ships defines solutions at the boundary between the EveOnline layer and the MapElite layer.
 */
public interface IShip extends ISolution {
    /**
     * Get the IShipFeatureDescription of the ship for handling ship feature descriptions within the EveOnline layer.
     *
     * @return the IShipFeatureDescription which belongs to the ship.
     */
    IShipFeatureDescription getShipFeatureDescriptor();

    /**
     * Add a rig to the ship.
     *
     * @param rig an IComponent which represents a rig.
     * @return true if the rig was added, otherwise false.
     */
    boolean addRig(IComponent rig);

    /**
     * Add a high powered component to the ship.
     *
     * @param component an IComponent which represents a high powered component.
     * @return true if the component was added, otherwise false.
     */
    boolean addHighPoweredComponent(IComponent component);

    /**
     * Add a medium powered component to the ship.
     *
     * @param component an IComponent which represents a medium powered component.
     * @return true if the component was added, otherwise false.
     */
    boolean addMediumPoweredComponent(IComponent component);

    /**
     * Add a low powered component to the ship.
     *
     * @param component an IComponent which represents a low powered component.
     * @return true if the component was added, otherwise false.
     */
    boolean addLowPoweredComponent(IComponent component);

    /**
     * Remove a random component from the ship.
     */
    void removeRandomComponent();

    /**
     * Add a quality to the ship.
     *
     * @param newQuality a quality which defines the base attributes of a ship.
     */
    void addQuality(IQuality newQuality);

    ComponentName getEmptySlotName();

    /**
     * Get all rigs belonging to the ship.
     *
     * @return an Array of IComponent which represents all the rigs of the ship.
     */
    IComponent[] getRigs();

    /**
     * Get all high modules belonging to the ship.
     *
     * @return an Array of IComponent which represents all the high powered modules of the ship.
     */
    IComponent[] getHighModules();

    /**
     * Get all medium modules belonging to the ship.
     *
     * @return an Array of IComponent which represents all the medium powered modules of the ship.
     */
    IComponent[] getMediumModules();

    /**
     * Get all low modules of belonging to the ship.
     *
     * @return an Array of IComponents which represents all the low powered modules of the ship.
     */
    IComponent[] getLowModules();

    /**
     * Get all qualities of the ship.
     *
     * @return an ArrayList of IQualities which defines all the qualities of the ship.
     */
    ArrayList<IQuality> getQualities();

    /**
     * Adds a type name of the ship.
     *
     * @param typeName the name of the ship.
     */
    void addTypeName(String typeName);

    /**
     * Get the name of the ship
     *
     * @return a String name of the ship
     */
    String getShipName();
}