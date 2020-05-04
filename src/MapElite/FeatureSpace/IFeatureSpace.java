package MapElite.FeatureSpace;

import MapElite.ISolution;

import java.util.Map;

/**
 * An interface defined for interaction with the featurespace package
 */
public interface IFeatureSpace {
    /**
     * Used for fetching a random solution from the feature space.
     *
     * @return a random solution from the feature space.
     */
    ISolution getRandomSolution();

    /**
     * Insert a solution into the feature space.
     * This method takes care of checking if the provided solution is within the currently defined space.
     * If the solution is beyond the currently defined feature space it will make sure that the feature space gets recalculated to handle this new elite.
     *
     * @param solution the solution which are inserted into the feature space,
     */
    void insertSolution(ISolution solution);

    /**
     * Used for getting the feature space.
     *
     * @return a Map which maps sector spans with sectors, which defines the whole feature space.
     */
    Map<ISectorSpan, ISector> getFeatureSpace();

    /**
     * Set whether the handler should keep duplicate solutions in the feature space.
     *
     * @param storeDuplicates a boolean which specifies if the handler should store duplicates.
     */
    void setStoreDuplicates(boolean storeDuplicates);

    /**
     * Get the amount of features within the feature space
     *
     * @return an integer which represents the amount of features in the feature space
     */
    int getAmountOfFeatures();

    /**
     * Get the amount of recalculations which have been done in the past run.
     *
     * @return an integer which represents the amount of recalculations counted by one run.
     */
    int getRecalculation();
}
