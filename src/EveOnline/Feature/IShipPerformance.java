package EveOnline.Feature;

import MapElite.IPerformance;

public interface IShipPerformance extends IPerformance {
    /**
     * Multiply the performance score with the provided score.
     *
     * @param score a Double which is multiplied with the performance score.
     */
    void addToScore(double score);

}
