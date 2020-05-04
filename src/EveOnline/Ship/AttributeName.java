package EveOnline.Ship;

public enum AttributeName {
    CPU("cpu"), POWER("power"), CALIBRATION("upgradeCost");

    private String name;

    AttributeName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
