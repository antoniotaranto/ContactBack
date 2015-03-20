package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public class BarFilledValue extends BarValue {

    private String outline_color;

    public BarFilledValue() {
        super();
    }

    public BarFilledValue(double top, double bottom) {
        super(top, bottom);
    }

    @JsonParameter(renameTo="outline-colour")
    @JSON(include = true)
    public String getOutline_color() {
        return outline_color;
    }

    public void setOutline_color(String outline_color) {
        this.outline_color = outline_color;
    }
}
