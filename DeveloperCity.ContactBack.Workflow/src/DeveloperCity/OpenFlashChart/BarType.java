package DeveloperCity.OpenFlashChart;

public enum BarType {

    BAR("bar"),
    HBAR("hbar"),
    BAR_STACK("bar_stack"),
    BAR_SKETCH("bar_sketch"),
    BAR_GLASS("bar_glass"),
    BAR_CYLINDER("bar_cylinder"),
    BAR_CYLINDER_OUTLINE("bar_cylinder_outline"),
    BAR_DOME("bar_dome"),
    BAR_ROUND("bar_round"),
    BAR_ROUND_GLASS("bar_round_glass"),
    BAR_ROUND3D("bar_round3d"),
    BAR_FADE("bar_fade"),
    BAR_3D("bar_3d"),
    BAR_FILLED("bar_filled"),
    BAR_PLASTIC("bar_plastic"),
    BAR_PLASTIC_FLAT("bar_plastic_flat"),
    CANDLE("candle");
    private String value;

    private BarType(String value) {
        this.value = value;
    }

    public String getStringValue() {
        return this.value;
    }

    public static BarType getFromString(String value) {
        if (value == null) {
            return null;
        }
        for (BarType d : BarType.values()) {
            if (d.getStringValue().equals(value)) {
                return d;
            }
        }
        return null;
    }
}
