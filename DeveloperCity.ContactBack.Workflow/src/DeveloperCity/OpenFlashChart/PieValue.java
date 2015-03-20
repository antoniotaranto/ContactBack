package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public class PieValue {

    private double val;
    private String text;
    private String click;

    public PieValue(double val) {
        this.val = val;
    }

    public static PieValue create(double val) {
        return new PieValue(val, "");
    }

    public PieValue(double val, String text) {
        this.val = val;
        this.text = text;
    }

    @JSON(include = true)
    public double getValue() {
        return val;
    }

    public void setValue(double val) {
        this.val = val;
    }

    @JSON(include = true)
    public String getLabel() {
        return text;
    }

    public void setLabel(String text) {
        this.text = text;
    }

    @JSON(include = true)
    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }
}
