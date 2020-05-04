package MapElite;

import MapElite.FeatureSpace.IFeatureSpace;

import java.util.LinkedList;
import java.util.Map;

/**
 * The IMapElitesFacade is the main facade of the MepElite layer.
 */
public interface IMapElitesFacade {
    /**
     * Commend the illumination of the search space.
     *
     * @param iterations      an integer which defines how many iterations the illumination algorithm should run.
     * @param solutions       an integer which defines how large the initial population of solutions should be.
     * @param storeDuplicates boolean which defines if the illumination should keep duplicate solutions.
     */
    void illuminateElites(int iterations, int solutions, boolean storeDuplicates);

    /**
     * Get the illuminated feature space.
     *
     * @return an IFeatureSpace which is the facade for handling the feature space.
     */
    IFeatureSpace getIlluminatedFeatureSpace();

    /**
     * Get a map which maps all generations of solutions to their children.
     *
     * @return an Map which links solutions to their children
     */
    Map<ISolution, LinkedList<ISolution>> getGenerationsTree();
}
