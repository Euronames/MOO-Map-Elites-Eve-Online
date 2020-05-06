package MapElite;

import EveOnline.Ship.IShip;

import java.util.List;

/**
 * The IElitesController is meant to be a facade for interfacing a controller with the Map-Elite algorithm.
 * <p>
 * The idea is that the class implementing this should have access to all data which the solutions are build upon
 * and its feature space.
 */
public interface IElitesController {
    /**
     * Get a random solution.
     *
     * @return returns a ISolution which i generated randomly.
     */
    ISolution generateRandomSolution(IShip ship);

    /**
     * Get a muted solution based on a parents randomly mutation.
     *
     * @param parent an ISolution parent which is about to get mutated.
     * @return a ISolution which is a child mutation of a single parent.
     */
    ISolution randomMutation(ISolution parent);

    /**
     * Get all features in the feature space.
     *
     * @return a List of all the features defined for the feature space.
     */
    List<IFeature> getFeatures();

    IShip getRandomShip();
}
