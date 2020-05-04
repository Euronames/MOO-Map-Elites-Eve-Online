package EveOnline.Component;

/**
 * The Quality is mapped with Components and Ships and defines attributes with their associated scores.
 * <p>
 * Within the Eve Online
 */
public class Quality implements IQuality {
    private String attributeName;
    private double value;

    /**
     * Construct a Quality with the provided name and value.
     *
     * @param attributeName a name of the Quality.
     * @param value         a quantified score of the created Quality.
     */
    public Quality(String attributeName, double value) {
        this.attributeName = attributeName;
        this.value = value;
    }

    @Override
    public String getAttributeName() {
        return attributeName;
    }

    @Override
    public double getScore() {
        return this.value;
    }

    @Override
    public String toString() {
        return attributeName + " - " + value;
    }
}