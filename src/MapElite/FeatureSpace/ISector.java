package MapElite.FeatureSpace;

import MapElite.ISolution;

import java.util.ArrayList;

/**
 * Provides an interface for handling the sector logic.
 * <p>
 * A Sector knows nothing about the span its mapped towards.
 */
public interface ISector {
    /**
     * Insert a new solution into the ISector.
     *
     * @param solution a ISolution which defines the solution inserted into the sector.
     */
    void insertIntoSector(ISolution solution);

    /**
     * Get the solutions stored in the ISector.
     *
     * @return an ArrayList of ISolutions which defines the solutions stored in the sector.
     */
    ArrayList<ISolution> getParetoFront();

    /**
     * Get a random solution from the ISector.
     *
     * @return a ISolution which is a randomly picked solution.
     */
    ISolution getRandomSolution();

    /**
     * Check whether a ISolution is already stored in the ISector.
     *
     * @param solution a ISolution which is matched towards all the other solutions stored in the sector.
     * @return true if and only if the ISolution is contained within the ISector.
     */
    boolean isSolutionContained(ISolution solution);
}
