package EveOnline.Feature;


/**
 * The Features defines the features captured for the ships.
 * It is defined as a enum which consist of an array of different definitions of the same feature.
 * <p>
 * The vast amount of definitions are Eve Online specific and by storing them all as a enum of different string values
 * it is then possible to match the features in a broad range of definitions which relaxes the amount of switch statements
 * needed within the controlling code to handle all of these different definitions.
 */
public enum Features implements IFeatures {

    ARMOR("armor,armorHPBonusAdd,armorHPBonus,armorHP"),
    SHIELD("shield,overloadShieldBonus,capacityBonus,shieldCapacity,shieldCapacityBonus,shieldBonus"),
    DRONERANGE("droneRange,droneBandwidth,droneRangeBonus"),
    VELOCITY("maxVelocity,velocityBonus,maxVelocityBonus");

    private final String[] featureDescriptions;

    /**
     * Initialize a Features by providing it with a string of comma separated definitions which all maps to the same shipFeature.
     *
     * @param featureDescription a string of comma separated feature definitions which all maps to the same feature definition.
     */
    Features(String featureDescription) {
        featureDescriptions = featureDescription.split(",");
    }

    @Override
    public boolean contains(String description) {
        for (String featureDescription : featureDescriptions) {
            if (featureDescription.equals(description)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getFeatureDescription() {
        return this.featureDescriptions[0];
    }

    public String[] getFeatureDescriptions() {
        return this.featureDescriptions;
    }

}