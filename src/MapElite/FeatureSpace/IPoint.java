package MapElite.FeatureSpace;

import MapElite.IFeature;

/**
 * The IPoint defines a point in the n-dimensional feature space.
 * <p>
 * The concept is used for finding a matching feature soace
 */
public interface IPoint {

    /**
     * Get a point mapped by a feature
     *
     * @param feature the feature which maps the value
     * @return a double which represents a point mapped by a feature.
     */
    double getFeaturePoint(IFeature feature);

    /**
     * Insert a value mapped with a feature into this point
     *
     * @param feature the feature which maps the value
     * @param value   the value which is mapped by a feature
     * @return true if the value is successfully inserted otherwise false
     */
    boolean insertValue(IFeature feature, double value);
}
