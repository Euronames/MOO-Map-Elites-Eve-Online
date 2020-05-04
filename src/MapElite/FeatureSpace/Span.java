package MapElite.FeatureSpace;

import MapElite.IFeature;

/**
 * The main concern of a span is to hold a maximum and a minimum which is used for defining the span of a feature when mapped.
 */
public class Span implements ISpan {
    private double max;
    private double min;

    /**
     * Construct a featureSpan which max and min defines its size.
     *
     * @param max the maximum value of the feature span
     * @param min the minimum value of the feature span
     */
    Span(double max, double min) {
        this.max = max;
        this.min = min;
    }

    @Override
    public double getMax() {
        return max;
    }

    @Override
    public double getMin() {
        return min;
    }

    @Override
    public boolean isWithinSpan(double point) {
        // points are allowed to be zero but only if min is 0
        if (point <= this.max && point > this.min) {
            return true;
        } else return point <= this.max && this.min == 0 && point == 0;
    }

    public boolean isSmallerThan(double point) {
        if (point > this.max) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Span)) return false;
        Span span = (Span) o;

        return this.max == span.getMax() && this.min == span.getMin();
    }

    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += 31 * this.max;
        hashCode += 31 * this.min;

        return hashCode;
    }
}
