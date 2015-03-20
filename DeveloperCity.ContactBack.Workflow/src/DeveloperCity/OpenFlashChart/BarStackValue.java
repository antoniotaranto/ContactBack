package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public class BarStackValue {

    private String colour;
    private double val;

    public BarStackValue(double val, String color) {
        this.colour = color;
        this.val = val;
    }

    public BarStackValue(double val) {
        this.val = val;
    }

    public BarStackValue() {
    }

    public static BarStackValue create(double val) {
        return new BarStackValue(val);
    }

    @JSON(include = true)
    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    @JSON(include = true)
    public double getVal() {
        return val;
    }

    public void setVal(double val) {
        this.val = val;
    }
}
