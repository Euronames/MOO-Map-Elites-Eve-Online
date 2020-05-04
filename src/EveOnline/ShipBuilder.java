package EveOnline;

import Database.ConnectionFactory;
import Database.IConnectionFactory;
import EveOnline.Component.ComponentName;
import EveOnline.Component.IComponent;
import EveOnline.DataFetcher.*;
import EveOnline.Feature.Features;
import EveOnline.Ship.IShip;
import EveOnline.Ship.Ship;

import MapElite.IFeature;
import MapElite.ISolution;
import Seed.Seed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * The ShipBuilder provides functionality to the IShipBuilder facade for the Eve Online layer.
 * <p>
 * The Idea is that the ShipBuilder provides ships to its consumer while keeping track of all the components in which a ship can mount
 * and the possible ships which are possible to modify.
 */
public class ShipBuilder implements IShipBuilder {
    private Random random = new Random(Seed.seed);
    private ArrayList<IShip> ships;
    private ArrayList<IComponent> rigs;
    private ArrayList<IComponent> highPoweredModules;
    private ArrayList<IComponent> mediumPoweredModules;
    private ArrayList<IComponent> lowPoweredModules;

    /**
     * Construct shipbuilder with base components fetched from the open source database.
     */
    public ShipBuilder() {
        IConnectionFactory connectionFactory = new ConnectionFactory();

        ITypeIDFetcher typeIDFetcher = new TypeIDFetcher(connectionFactory.getConnection());
        IShipFetcher shipFetcher = new ShipFetcher(connectionFactory.getConnection());
        IComponentFetcher componentFetcher = new ComponentFetcher(connectionFactory.getConnection());
        ships = new ArrayList<>();

        // FETCHING ONLY THE STABBER TYPE SHIP
        ships.add(shipFetcher.getShipByTypeID(622));

        //FETCHING A SINGLE RANDOM SHIP TYPE
        /*
        for (int typeID : typeIDFetcher.getShipTypeIDs()) {
            ships.add(shipFetcher.getShipByTypeID(typeID));
        }
        */

        rigs = new ArrayList<>();
        for (int rigTypeID : typeIDFetcher.getRigTypeIDs()) {
            rigs.add(componentFetcher.getComponentByTypeID(rigTypeID));
        }
        highPoweredModules = new ArrayList<>();
        for (int highPoweredModuleTypeID : typeIDFetcher.getHighPoweredTypeIDs()) {
            highPoweredModules.add(componentFetcher.getComponentByTypeID(highPoweredModuleTypeID));
        }
        mediumPoweredModules = new ArrayList<>();
        for (int mediumPoweredModuleTypeID : typeIDFetcher.getMediumPoweredTypeIDs()) {
            mediumPoweredModules.add(componentFetcher.getComponentByTypeID(mediumPoweredModuleTypeID));
        }
        lowPoweredModules = new ArrayList<>();
        for (int lowPoweredModuleTypeID : typeIDFetcher.getLowPoweredTypeIDs()) {
            lowPoweredModules.add(componentFetcher.getComponentByTypeID(lowPoweredModuleTypeID));
        }
    }

    @Override
    public List<IFeature> getFeatures() {
        return new ArrayList<>(Arrays.asList(Features.values()));
    }

    @Override
    public ISolution generateRandomSolution() {
        IShip randomShip = new Ship(getRandomShip());
        int repetitions = 200;

        for (int i = 0; i < repetitions; i++) {
            this.addRandomComponent(randomShip);
        }

        randomShip.getShipFeatureDescriptor().updateShipFeatureDescriptions(randomShip);
        return randomShip;
    }

    @Override
    public ISolution randomMutation(ISolution parent) {
        IShip childShip = new Ship((IShip) parent);

        childShip.removeRandomComponent();

        ComponentName componentName = childShip.getEmptySlotName();

        switch (componentName) {
            case Rig:
                childShip.addRig(getRandomRig());
                break;
            case LowPoweredModule:
                childShip.addLowPoweredComponent(getRandomLowPoweredModule());
                break;
            case MediumPoweredModule:
                childShip.addLowPoweredComponent(getRandomMediumPoweredModule());
                break;
            case HighPoweredModule:
                childShip.addLowPoweredComponent(getRandomHighPoweredModule());
                break;
        }
        childShip.getShipFeatureDescriptor().updateShipFeatureDescriptions(childShip);
        return childShip;
    }

    /**
     * Add a random component to a ship.
     *
     * @param ship the ship the component is added to.
     * @return true if the component addition was successful otherwise false.
     */
    private boolean addRandomComponent(IShip ship) {
        int numberOfComponentTypes = 4;
        boolean hasBeenAdded = false;
        switch (random.nextInt(numberOfComponentTypes)) {
            case 0:
                hasBeenAdded = ship.addLowPoweredComponent(getRandomLowPoweredModule());
                break;
            case 1:
                hasBeenAdded = ship.addMediumPoweredComponent(getRandomMediumPoweredModule());
                break;
            case 2:
                hasBeenAdded = ship.addHighPoweredComponent(getRandomHighPoweredModule());
                break;
            case 3:
                hasBeenAdded = ship.addRig(getRandomRig());
                break;
            default:
                break;
        }

        if (hasBeenAdded) {
            ship.getShipFeatureDescriptor().updateShipFeatureDescriptions(ship);
        }

        return hasBeenAdded;
    }

    /**
     * Get a random high-powered component.
     *
     * @return an IComponent which is a random chosen high powered component.
     */
    private IComponent getRandomHighPoweredModule() {
        return highPoweredModules.get(random.nextInt(highPoweredModules.size()));
    }

    /**
     * Get a random medium powered component.
     *
     * @return an IComponent which is a random chosen medium powered component.
     */
    private IComponent getRandomMediumPoweredModule() {
        return mediumPoweredModules.get(random.nextInt(mediumPoweredModules.size()));
    }

    /**
     * Ger a random low powered component.
     *
     * @return an IComponent which is a random chosen low powered component.
     */
    private IComponent getRandomLowPoweredModule() {
        return lowPoweredModules.get(random.nextInt(lowPoweredModules.size()));
    }

    /**
     * Get a random rig component.
     *
     * @return an IComponent which is a random chosen rig component.
     */
    private IComponent getRandomRig() {
        return rigs.get(random.nextInt(rigs.size()));
    }

    /**
     * Get a random ship.
     *
     * @return an IShip which is a random chosen ship.
     */
    private IShip getRandomShip() {
        return ships.get(random.nextInt(ships.size()));
    }

}
