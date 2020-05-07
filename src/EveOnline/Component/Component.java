package EveOnline.Component;

import java.util.ArrayList;

/**
 * A component defines a module or rig which belongs to a ship.
 * The component implements the IComponent interface for references beyond this package.
 */
public class Component implements IComponent {
    private String componentName;

    public String getComponentName() {
        return componentName;
    }

    private ArrayList<IQuality> qualities = new ArrayList<>();

    public void addQualities(ArrayList<IQuality> qualities) {
        this.qualities = qualities;
    }

    @Override
    public void addQuality(IQuality newQuality) {
        qualities.add(newQuality);
    }

    @Override
    public ArrayList<IQuality> getQualities() {
        return qualities;
    }

    @Override
    public void addTypeName(String typeName) {
        componentName = typeName;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Component: ").append(componentName).append(System.getProperty("line.separator"));
        stringBuilder.append("Qualities in component").append(System.getProperty("line.separator"));
        for (IQuality quality : qualities) {
            stringBuilder.append(quality).append(System.getProperty("line.separator"));

        }

        return stringBuilder.toString();
    }
}
