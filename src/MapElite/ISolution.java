package MapElite;

/**
 * A solution is described by its feature descriptor in the feature space. It is meant as a facade for interfacing with
 * the Map-Elite algorithm.
 * <p>
 * The Map-Elite algorithm uses ISolution as a central access point for fetching its feature descriptor.
 * The idea is that a solution is a facade class for accessing the feature descriptor.
 * <p>
 * The ISolution implements the comparable interface for comparing solutions in the feature space.
 */
public interface ISolution extends Comparable<ISolution> {
    /**
     * Get the feature descriptor which defines where in the feature space the solution is located.
     *
     * @return an IFeatureDescriptor which maps the features of the space with performances.
     */
    IFeatureDescriptor getFeatureDescriptor();
}