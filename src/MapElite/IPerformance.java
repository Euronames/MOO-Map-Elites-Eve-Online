package MapElite;

/**
 * The IPerformance defines the value of a single feature dimension. It is meant as a facade for interfacing with
 * the Map-Elite algorithm.
 * <p>
 * The IPerformance implements the comparable interface for comparing performances.
 */
public interface IPerformance extends Comparable<IPerformance> {
    /**
     * Get the score of the performance.
     *
     * @return a Double which defines the score of the performance.
     */
    double getScore();
}