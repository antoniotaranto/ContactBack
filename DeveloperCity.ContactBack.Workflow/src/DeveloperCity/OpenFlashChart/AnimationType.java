package DeveloperCity.OpenFlashChart;

public enum AnimationType {

    Popup("pop-up"),
    Explode("explode"),
    MidSlide("mid-slide"),
    Drop("drop"),
    Fadein("fade-in"),
    Shrinkin("shrink-in");
    private String value;

    private AnimationType(String value) {
        this.value = value;
    }

    public String getStringValue() {
        return this.value;
    }

    public static AnimationType getFromString(String value) {
        if (value == null) {
            return null;
        }
        for (AnimationType d : AnimationType.values()) {
            if (d.getStringValue().equals(value)) {
                return d;
            }
        }
        return null;
    }
}
