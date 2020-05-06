package EveOnline.Ship;

import EveOnline.Component.Component;
import EveOnline.Component.ComponentName;
import EveOnline.Component.IComponent;
import EveOnline.Component.IQuality;
import EveOnline.Feature.IShipFeatureDescription;
import EveOnline.Feature.ShipFeatureDescriptor;
import MapElite.IFeature;
import MapElite.IFeatureDescriptor;
import MapElite.ISolution;
import Seed.Seed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * The Ship class represents the eve online ships.
 * The Ship class keeps track of the components and qualities of the ship,
 * while updating the ships current power, calibration and cpu usage.
 */
public class Ship implements IShip {
    private IComponent[] rigs;
    private IComponent[] highModules;
    private IComponent[] mediumModules;
    private IComponent[] lowModules;

    private ArrayList<IQuality> qualities;
    private IShipFeatureDescription shipFeatureDescriptor;

    private String shipName;
    private double cpuTotal;
    private double powerTotal;
    private double calibrationTotal;
    private double cpuUsage;
    private double powerUsage;
    private double calibrationUsage;



    private Random random = new Random(Seed.seed);

    /**
     * Constructs a new Ship based on the number of rigs, high powered modules, medium powered modules and low powered modules, while also its total cpu, total power and total calibration.
     * The parameters provided defines the ship type.
     *
     * @param numberOfRigs          The amount of rigs the ship can contain.
     * @param numberOfHighModules   The amount of high powered modules the ship can contain.
     * @param numberOfMediumModules The amount of medium powered modules the chip can contain.
     * @param numberOfLowModules    The amount of low powered modules the ship can contain.
     * @param cpuTotal              The total CPU of the ship which works as a constraint on which type of modules the ship can fit
     * @param powerTotal            The total size of the power grid of the ship which works as a constraint on which types of modules the ship can fit.
     * @param calibrationTotal      The total size of calibration which works as a constraint on which types of rigs the ship can fit.
     */
    public Ship(int numberOfRigs, int numberOfHighModules, int numberOfMediumModules, int numberOfLowModules, double cpuTotal, double powerTotal, double calibrationTotal) {
        this.rigs = new Component[numberOfRigs];
        this.highModules = new Component[numberOfHighModules];
        this.mediumModules = new Component[numberOfMediumModules];
        this.lowModules = new Component[numberOfLowModules];
        this.qualities = new ArrayList<>();
        this.shipFeatureDescriptor = new ShipFeatureDescriptor();
        this.cpuTotal = cpuTotal;
        this.powerTotal = powerTotal;
        this.calibrationTotal = calibrationTotal;
        this.cpuUsage = 0;
        this.powerUsage = 0;
        this.calibrationUsage = 0;
    }

   
    /**
     * Construct a new ship based on a predefined ship. Used for copying the ship onto a new instance for ship mutation.
     *
     * @param ship A IShip which is copied into the new instance.
     */
    public Ship(IShip ship) {
        this.rigs = Arrays.copyOf(ship.getRigs(), ship.getRigs().length);
        this.highModules = Arrays.copyOf(ship.getHighModules(), ship.getHighModules().length);
        this.mediumModules = Arrays.copyOf(ship.getMediumModules(), ship.getMediumModules().length);
        this.lowModules = Arrays.copyOf(ship.getLowModules(), ship.getLowModules().length);
        this.qualities = new ArrayList<>(ship.getQualities());
        this.shipFeatureDescriptor = new ShipFeatureDescriptor();
        this.shipName = ship.getShipName();
        this.cpuTotal = ((Ship) ship).getCpuTotal();
        this.powerTotal = ((Ship) ship).getPowerTotal();
        this.calibrationTotal = ((Ship) ship).getCalibrationTotal();
        this.cpuUsage = ((Ship) ship).getCpuUsage();
        this.powerUsage = ((Ship) ship).getPowerUsage();
        this.calibrationUsage = ((Ship) ship).getCalibrationUsage();
    }

    @Override
    public IShipFeatureDescription getShipFeatureDescriptor() {
        return shipFeatureDescriptor;
    }

    @Override
    public IFeatureDescriptor getFeatureDescriptor() {
        return shipFeatureDescriptor;
    }

    @Override
    public boolean addRig(IComponent rig) {
        if (isRigAddable(rig)) {
            for (int i = 0; i < rigs.length; i++) {
                if (rigs[i] == null) {
                    rigs[i] = rig;
                    for (IQuality quality : rig.getQualities()) {
                        if (quality.getAttributeName().equals(AttributeName.CALIBRATION.getName())) {
                            calibrationUsage += quality.getScore();
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean addHighPoweredComponent(IComponent component) {
        return addComponent(component, highModules);
    }

    @Override
    public boolean addMediumPoweredComponent(IComponent component) {
        return addComponent(component, mediumModules);
    }

    @Override
    public boolean addLowPoweredComponent(IComponent component) {
        return addComponent(component, lowModules);
    }

    @Override
    public void removeRandomComponent() {
        int numberOfComponentTypes = 4;
        switch (random.nextInt(numberOfComponentTypes)) {
            case 0:
                if (highModules.length > 0) {
                    removeComponent(highModules, random.nextInt(highModules.length));
                }
                break;
            case 1:
                if (mediumModules.length > 0) {
                    removeComponent(mediumModules, random.nextInt(mediumModules.length));
                }
                break;
            case 2:
                if (lowModules.length > 0) {
                    removeComponent(lowModules, random.nextInt(lowModules.length));
                }
                break;
            case 3:
                if (rigs.length > 0) {
                    removeComponent(rigs, random.nextInt(rigs.length));
                }
                break;
        }
    }

    @Override
    public void addQuality(IQuality newQuality) {
        qualities.add(newQuality);
    }

    /**
     * Remove a element from the array of components at the provided index.
     *
     * @param components a Array of IComponents
     * @param index      the index at which a component is removed.
     */
    private void removeComponent(IComponent[] components, int index) {
        if (components[index] != null) {
            subtractComponentUsage(components[index]);
            components[index] = null;
        } else {
            removeRandomComponent();
        }
    }

    /**
     * Removes the cpu and power grid usage of the component.
     *
     * @param component the IComponent to which the usage has to be subtracted from the ships total usage.
     */
    private void subtractComponentUsage(IComponent component) {
        for (IQuality quality : component.getQualities()) {
            if (quality.getAttributeName().equals(AttributeName.CPU.getName())) {
                cpuUsage -= quality.getScore();
            } else if (quality.getAttributeName().equals(AttributeName.POWER.getName())) {
                powerUsage -= quality.getScore();
            }
        }

    }

    /**
     * Check whether a module is addable to the ship.
     * <p>
     * Checks the CPU and power grid usage of the module against the ships current usage to evaluate this.
     *
     * @param module the module which is checked.
     * @return true if the modules power and cpu usage is within the ships current and maximum usage, otherwise false.
     */
    private boolean isModuleAddable(IComponent module) {
        double componentCpu = 0;
        double componentPower = 0;

        for (IQuality quality : module.getQualities()) {
            if (quality.getAttributeName().equals(AttributeName.POWER.getName())) {
                componentPower = quality.getScore();
            } else if (quality.getAttributeName().equals(AttributeName.CPU.getName())) {
                componentCpu = quality.getScore();
            }
        }

        return (cpuTotal - cpuUsage) >= componentCpu && (powerTotal - powerUsage) >= componentPower;
    }

    /**
     * Check whether a rig is addable to the ship
     * <p>
     * Checks the current calibration usage of the ship against the rig calibration usage to evaluate this.
     *
     * @param rig the rig which is checked.
     * @return true if the rigs calibration usage is within the ships current and maximum calibration, otherwise false.
     */
    private boolean isRigAddable(IComponent rig) {
        double rigCalibrationCost = 0;

        for (IQuality quality : rig.getQualities()) {
            if (quality.getAttributeName().equals(AttributeName.CALIBRATION.getName())) {
                rigCalibrationCost = quality.getScore();
            }
        }

        return (calibrationTotal - calibrationUsage) >= rigCalibrationCost;
    }

    @Override
    public ComponentName getEmptySlotName() {
        ArrayList<ComponentName> componentNameList = new ArrayList<>();
        for (IComponent rigComponent : rigs) {
            if (rigComponent == null) {
                componentNameList.add(ComponentName.Rig);
                break;
            }
        }
        for (IComponent lowModule : lowModules) {
            if (lowModule == null) {
                componentNameList.add(ComponentName.LowPoweredModule);
                break;
            }
        }
        for (IComponent mediumModule : mediumModules) {
            if (mediumModule == null) {
                componentNameList.add(ComponentName.MediumPoweredModule);
                break;
            }
        }
        for (IComponent highModule : highModules) {
            if (highModule == null) {
                componentNameList.add(ComponentName.HighPoweredModule);
                break;
            }
        }
        return componentNameList.get(random.nextInt(componentNameList.size()));
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Ship name: ").append(shipName).append(System.getProperty("line.separator"));
        stringBuilder.append("No. of rigs: ").append(rigs.length).append(System.getProperty("line.separator"));
        stringBuilder.append("No. of high powered modules: ").append(highModules.length).append(System.getProperty("line.separator"));
        stringBuilder.append("No. of medium powered modules: ").append(mediumModules.length).append(System.getProperty("line.separator"));
        stringBuilder.append("No. of low powered modules: ").append(lowModules.length).append(System.getProperty("line.separator"));
        stringBuilder.append("Size of CPU: ").append(cpuTotal).append(System.getProperty("line.separator"));
        stringBuilder.append("Size of power: ").append(powerTotal).append(System.getProperty("line.separator"));
        stringBuilder.append("Size of calibration: ").append(calibrationTotal).append(System.getProperty("line.separator"));
        stringBuilder.append("Size of CPU usage: ").append(cpuUsage).append(System.getProperty("line.separator"));
        stringBuilder.append("Size of power usage: ").append(powerUsage).append(System.getProperty("line.separator"));
        stringBuilder.append("Size of calibration usage: ").append(calibrationUsage).append(System.getProperty("line.separator"));

        for (IQuality quality : qualities) {
            stringBuilder.append(quality.toString()).append(System.getProperty("line.separator"));
        }
        stringBuilder.append(System.getProperty("line.separator"));

        stringBuilder.append("All high powered components:").append(System.getProperty("line.separator"));
        for (IComponent component : highModules) {
            if (component != null) {
                stringBuilder.append(component.toString()).append(System.getProperty("line.separator"));
            }
        }
        stringBuilder.append(System.getProperty("line.separator"));

        stringBuilder.append("All medium powered components:").append(System.getProperty("line.separator"));
        for (IComponent component : mediumModules) {
            if (component != null) {
                stringBuilder.append(component.toString()).append(System.getProperty("line.separator"));
            }
        }
        stringBuilder.append(System.getProperty("line.separator"));

        stringBuilder.append("All low powered components:").append(System.getProperty("line.separator"));
        for (IComponent component : lowModules) {
            if (component != null) {
                stringBuilder.append(component.toString()).append(System.getProperty("line.separator"));
            }
        }
        stringBuilder.append(System.getProperty("line.separator"));

        stringBuilder.append("All rigs:").append(System.getProperty("line.separator"));
        for (IComponent component : rigs) {
            if (component != null) {
                stringBuilder.append(component.toString()).append(System.getProperty("line.separator"));
            }
        }
        stringBuilder.append(System.getProperty("line.separator"));

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (!(o instanceof Ship)) return false;
        if (o == this) return true;
        return
                Arrays.equals(this.getRigs(), ((Ship) o).getRigs()) &&
                        Arrays.equals(this.getLowModules(), ((Ship) o).getLowModules()) &&
                        Arrays.equals(this.getMediumModules(), ((Ship) o).getMediumModules()) &&
                        Arrays.equals(this.getHighModules(), ((Ship) o).getHighModules()) &&
                        Double.compare(this.getCalibrationTotal(), ((Ship) o).getCalibrationTotal()) == 0 &&
                        Double.compare(this.getPowerTotal(), ((Ship) o).getPowerTotal()) == 0 &&
                        Double.compare(this.getCpuTotal(), ((Ship) o).getCpuTotal()) == 0 &&
                        Double.compare(this.getCpuUsage(), ((Ship) o).getCpuUsage()) == 0 &&
                        Double.compare(this.getPowerUsage(), ((Ship) o).getPowerUsage()) == 0 &&
                        Double.compare(this.getCalibrationUsage(), ((Ship) o).getCalibrationUsage()) == 0 &&
                        this.getQualities().equals(((Ship) o).getQualities());
    }

    private boolean addComponent(IComponent component, IComponent[] modules) {
        if (isModuleAddable(component)) {
            for (int i = 0; i < modules.length; i++) {
                if (modules[i] == null) {
                    modules[i] = component;
                    for (IQuality quality : component.getQualities()) {
                        if (quality.getAttributeName().equals(AttributeName.CPU.getName())) {
                            cpuUsage += quality.getScore();
                        } else if (quality.getAttributeName().equals(AttributeName.POWER.getName())) {
                            powerUsage += quality.getScore();
                        }
                    }
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public IComponent[] getRigs() {
        return rigs;
    }

    @Override
    public IComponent[] getHighModules() {
        return highModules;
    }

    @Override
    public IComponent[] getMediumModules() {
        return mediumModules;
    }

    @Override
    public IComponent[] getLowModules() {
        return lowModules;
    }

    @Override
    public ArrayList<IQuality> getQualities() {
        return qualities;
    }

    @Override
    public void addTypeName(String typeName) {
        shipName = typeName;
    }

    @Override
    public String getShipName() {
        return shipName;
    }

    @Override
    public int compareTo(ISolution solution) {
        if (this.equals(solution)) {
            return 0;
        }
        boolean hasLargerFeature = false;
        boolean hasSmallerFeature = false;
        for (IFeature feature : shipFeatureDescriptor.getFeatureDescriptions().keySet()) {
            int comparison = shipFeatureDescriptor.getFeatureDescriptions().get(feature).compareTo(solution.getFeatureDescriptor().getFeatureDescriptions().get(feature));
            if (comparison > 0) {
                hasLargerFeature = true;
            } else if (comparison < 0) {
                hasSmallerFeature = true;
            }
        }

        if (hasLargerFeature) {
            return 1;
        } else if (hasSmallerFeature) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Get the total CPU of the ship.
     *
     * @return a double representing the total CPU of the ship.
     */
    private double getCpuTotal() {
        return cpuTotal;
    }

    /**
     * Get the total power grid of the ship.
     *
     * @return a double representing the total power grid of the ship.
     */
    private double getPowerTotal() {
        return powerTotal;
    }

    /**
     * Get the total calibration of the ship.
     *
     * @return a double representing the total calibration of the ship.
     */
    private double getCalibrationTotal() {
        return calibrationTotal;
    }

    /**
     * Get the total CPU usage of the ship.
     *
     * @return a double representing the total CPU usage of the ship.
     */
    private double getCpuUsage() {
        return cpuUsage;
    }

    /**
     * Get the total power grid usage of the ship.
     *
     * @return a double representing the total power grid usage of the ship.
     */
    private double getPowerUsage() {
        return powerUsage;
    }

    /**
     * Get the total calibration usage of the ship.
     *
     * @return a double representing the total calibration usage of the ship.
     */
    private double getCalibrationUsage() {
        return calibrationUsage;
    }
}