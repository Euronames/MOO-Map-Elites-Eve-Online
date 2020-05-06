package Printer;

public enum Features {

    ARMOR("armor"),
    SHIELD("shield"),
    DRONERANGE("droneRange"),
    VELOCITY("velocity");


    private final String feature;

    Features(String feature) {
        this.feature = feature;
    }

    public String getFeature() {
        return feature;
    }
}