package DeveloperCity.OpenFlashChart;

public enum ToolTipStyle {

    CLOSEST(0),
    FOLLOW(1),
    NORMAL(2);

    private int value;
    ToolTipStyle(int value) {
        this.value = value;
    }

    public int getValue() { return value; }
    public ToolTipStyle getFromValue(int value) {
        for (ToolTipStyle tts : ToolTipStyle.values()) {
            if (tts.getValue() == value) {
                return tts;
            }
        }
        return null;
    }
}
