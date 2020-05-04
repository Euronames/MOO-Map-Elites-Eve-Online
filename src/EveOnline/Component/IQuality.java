package EveOnline.Component;

/**
 * The IQuality defines a public interface to represent the Quality.
 */
public interface IQuality {
    /**
     * Get the attribute name of the Quality.
     *
     * @return A String which represents the attribute name of the Quality.
     */
    String getAttributeName();

    /**
     * Get the score of the Quality.
     *
     * @return an double which represents a quantified score of the Quality.
     */
    double getScore();
}
