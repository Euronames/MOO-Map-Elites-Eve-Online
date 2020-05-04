package EveOnline.Component;

import java.util.ArrayList;

/**
 * The IComponent provides a public interface for accessing the methods implemented within a Component.
 */
public interface IComponent {
    /**
     * Add a quality to the component.
     *
     * @param newQuality the provided quality which defines a quality within the component.
     */
    void addQuality(IQuality newQuality);

    /**
     * Get the qualities of the component
     *
     * @return an ArrayList of qualities which defines all of the components qualities.
     */
    ArrayList<IQuality> getQualities();

    /**
     * Add the type name of the component.
     *
     * @param typeName A String which represents the type name of the component.
     */
    void addTypeName(String typeName);
}
