package MapElite;

import java.util.Map;

/**
 * A IFeatureDescriptor maps the features of the feature space with performances. It is meant as a facade for
 * interfacing with the Map-Elite algorithm.
 * <p>
 * Essentially a IFeatureDescriptor defines a point in the feature space.
 * The IFeatureDescriptor is meant as an data wrapper which handles a single point in the feature space.
 * <p>
 * By mapping an IFeature with an IPerformance where the performance defines a value on a single feature dimension we
 * are able to define a multitude of features for one single point in space.
 * <p>
 * An IFeatureDescriptor extends the comparable interface for comparing points in space on all their feature dimensions.
 * An example of an implementation could be by only returning 1 of and only if all of the feature dimensions are larger
 * than the compared IFeatureDescriptors feature dimensions.
 */
public interface IFeatureDescriptor extends Comparable<IFeatureDescriptor> {
    /**
     * Get the feature descriptions which defines a point in the feature space.
     *
     * @return A Map between IFeature and IPerformance which defines a point in the feature space.
     */
    Map<IFeature, IPerformance> getFeatureDescriptions();

    /**
     * Compare a IFeatureDescriptors with this.
     *
     * @param o which is a object implementing the IFeatureDescribtor interface.
     * @return 1 if this solution is larger than the compared one, -1 if its smaller and 0 if they are equal.
     */
    int compareTo(IFeatureDescriptor o);
}