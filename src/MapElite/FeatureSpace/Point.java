package MapElite.FeatureSpace;

import MapElite.IFeature;

import java.util.HashMap;
import java.util.Map;

/**
 * Used for converting a solution to a point in the feature space.
 */
public class Point implements IPoint {
    private Map<IFeature, Double> coordinate;

    /**
     * Create a empty point.
     * Expects to be filled up with values before use.
     */
    Point() {
        coordinate = new HashMap<>();
    }

    @Override
    public boolean insertValue(IFeature feature, double value) {
        if (!coordinate.containsKey(feature)) {
            coordinate.put(feature, value);
            return true;
        }
        return false;
    }

    @Override
    public double getFeaturePoint(IFeature feature) {
        return coordinate.get(feature);
    }
}
