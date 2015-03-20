package DeveloperCity.OpenFlashChart;

public enum DotType {

    SOLID_DOT("solid-dot"),
    HOLLOW_DOT("hollow-dot"),
    ANCHOR("anchor"),
    STAR("star"),
    BOW("bow"),
    DOT("dot");
    private String value;

    DotType(String value) {
        this.value = value;
    }

    public String getStringValue() {
        return this.value;
    }

    public static DotType getFromString(String value) {
        if (value == null) {
            return null;
        }
        for (DotType d : DotType.values()) {
            if (d.getStringValue().equals(value)) {
                return d;
            }
        }
        return null;
    }
}
