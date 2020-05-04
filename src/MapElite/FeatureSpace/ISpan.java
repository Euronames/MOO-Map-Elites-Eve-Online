package MapElite.FeatureSpace;

/**
 * The ISpan defines the span of a sector within the feature space.
 * <p>
 * The span essentially consists of a max and min value in the feature space.
 */
public interface ISpan {
    /**
     * Check whether a point in the feature space is within the span.
     *
     * @param point a double which defines a point in the feature space.
     * @return true if the point is within the feature space otherwise false.
     */
    boolean isWithinSpan(double point);

    /**
     * Get the maximum value of the span.
     *
     * @return a double which represents the maximum value of the span.
     */
    double getMax();

    /**
     * Get the minimum value of the span.
     *
     * @return a double which represents the minimum value of the span.
     */
    double getMin();

    boolean isSmallerThan(double point);

    boolean equals(Object o);
    int hashCode();
}