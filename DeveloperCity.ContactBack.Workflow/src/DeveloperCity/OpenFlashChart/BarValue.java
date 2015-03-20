package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public class BarValue {

    protected Double bottom;
    protected Double top;
    protected String color;
    protected String tip;
    private String onclick;

    public BarValue() {
    }

    public BarValue(double top) {
        this.top = top;
    }

    public BarValue(double top, double bottom) {
        this.bottom = bottom;
        this.top = top;
    }

    @JSON(include = true)
    public Double getBottom() {
        return bottom;
    }

    public BarValue setBottom(Double bottom) {
        this.bottom = bottom;
        return this;
    }

    @JSON(include = true)
    public Double getTop() {
        return top;
    }

    public BarValue setTop(Double top) {
        this.top = top;
        return this;
    }

    @JSON(include = true)
    public String getColour() {
        return color;
    }

    public BarValue setColour(String color) {
        this.color = color;
        return this;
    }

    @JSON(include = true)
    public String getTip() {
        return tip;
    }

    public BarValue setTip(String tip) {
        this.tip = tip;
        return this;
    }

    @JsonParameter(renameTo="on-click")
    @JSON(include = true)
    public String getOnclick() {
        return onclick;
    }

    public BarValue setOnclick(String onclick) {
        this.onclick = onclick;
        return this;
    }
}
