package EveOnline.Feature;

import MapElite.IPerformance;

/**
 * The ShipPerformance class implements the IPerformance which extends the IPerformance.
 * The idea is that the ShipPerformance defines the performance of the ship features.
 */
public class ShipPerformance implements IShipPerformance {
    private double score;

    /**
     * Instantiate a new ShipPerformance with the value provided.
     *
     * @param score the score in which the performance is measured.
     */
    ShipPerformance(double score) {
        this.score = score;
    }

    @Override
    public void addToScore(double score) {
        this.score += score;
    }

    @Override
    public double getScore() {
        return score;
    }

    @Override
    public int compareTo(IPerformance o) {
        if (this.score > o.getScore()) {
            return 1;
        } else if (this.score < o.getScore()) {
            return -1;
        }

        return 0;
    }
}
