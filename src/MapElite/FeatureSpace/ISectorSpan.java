package MapElite.FeatureSpace;

import MapElite.IFeature;

import java.util.Map;

/**
 * Provides a main interface for handling sector span logic.
 * The Sector span concept defines a area in the n-dimensional feature space.
 */
public interface ISectorSpan {
    /**
     * Check if a point is within the current sector span.
     *
     * @param point a Map<IFeature, Double> which defines a point in the feature space.
     * @return true if all the features of the point is within the sector span, otherwise false.
     */
    boolean isPointsWithinSectorSpan(IPoint point);

    /**
     * Get a span belonging to a feature of the sector span.
     *
     * @param feature the IFeature which maps to the wanted Span.
     * @return a Span which maps to the provided IFeature.
     */
    ISpan getFeatureSpan(IFeature feature);

    /**
     * Add a new span to the sector span.
     *
     * @param feature a IFeature which defines the dimension of the new feature span.
     * @param span    the Span which is inserted.
     */
    void addNewFeatureSpan(IFeature feature, ISpan span);

    /**
     * Get the spans of the sectorSpans.
     *
     * @return the Map<IFeature, Span> which defines the spans of a sector in the feature space.
     */
    Map<IFeature, ISpan> getSectorSpans();

    boolean equals(Object o);
    int hashCode();
}
