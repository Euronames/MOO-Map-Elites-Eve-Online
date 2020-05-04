package MapElite.FeatureSpace;

import MapElite.IFeature;

import java.util.HashMap;
import java.util.Map;

/**
 * The sector span main concern is to map features with spans.
 * When the sector span is mapped with a sector then it is used for defining the span of the sector within the feature space.
 * Another responsibility is to check if a point in the feature space is within the sector span.
 */
public class SectorSpan implements ISectorSpan {
    private HashMap<IFeature, ISpan> sectorSpans;

    /**
     * Construct a base SectorSpan.
     * The base sector span is collapsed at the center of 0, and needs recalculation to actually map the span of a sector.
     *
     * @param features a List consisting of all features in the feature space.
     */
    SectorSpan(IFeature[] features) {
        sectorSpans = new HashMap<>();
        for (IFeature feature : features) {
            sectorSpans.put(feature, new Span(0, 0));
        }
    }

    /**
     * Construct a new SpectorSpan based on another SectorSpan.
     *
     * @param sectorSpan the ISectorSpan which the new SectorSpan is based upon.
     */
    SectorSpan(ISectorSpan sectorSpan) {
        sectorSpans = new HashMap<>();
        for (IFeature feature : sectorSpan.getSectorSpans().keySet()) {
            this.sectorSpans.put(feature, sectorSpan.getSectorSpans().get(feature));
        }
    }

    @Override
    public boolean isPointsWithinSectorSpan(IPoint point) {
        boolean isWithinSpan = true;
        for (IFeature feature : sectorSpans.keySet()) {
            if (!sectorSpans.get(feature).isWithinSpan(point.getFeaturePoint(feature))) {
                isWithinSpan = false;
            }
        }

        return isWithinSpan;
    }

    @Override
    public void addNewFeatureSpan(IFeature feature, ISpan span) {
        sectorSpans.replace(feature, span);
    }

    @Override
    public ISpan getFeatureSpan(IFeature feature) {
        return sectorSpans.get(feature);
    }

    @Override
    public Map<IFeature, ISpan> getSectorSpans() {
        return sectorSpans;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof SectorSpan)) return false;
        SectorSpan sectorSpan = (SectorSpan) o;

        return sectorSpan.getSectorSpans().equals(((SectorSpan) o).getSectorSpans());
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        for (IFeature feature : sectorSpans.keySet()) {
            hashCode += (int) (31 * sectorSpans.get(feature).getMax() + sectorSpans.get(feature).getMin());
        }

        return hashCode;
    }
}
