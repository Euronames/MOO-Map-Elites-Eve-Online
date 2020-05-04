package MapElite;

/**
 * A IFeature which defines the feature of the feature space. It is meant as a facade for interfacing with the Map-Elite
 * algorithm.
 * <p>
 * A IFeature implementation is meant for comparing the vectors of the feature space and when mapped with a IPerformance
 * which defines a single point in one dimension of the feature space.
 */
public interface IFeature {
    /**
     * Get the description of the feature.
     *
     * @return a String which defines the feature description.
     */
    String getFeatureDescription();
    boolean equals(Object o);
    int hashCode();
}