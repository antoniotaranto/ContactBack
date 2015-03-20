package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public class HBarValue {

    private double left;
    private double right;
    private String tip;

    public HBarValue(double left, double right) {
        this.left = left;
        this.right = right;
    }

    public HBarValue(double left, double right, String tip) {
        this.left = left;
        this.right = right;
        this.tip = tip;
    }

    @JSON(include = true)
    public double getLeft() {
        return left;
    }

    public void setLeft(double left) {
        this.left = left;
    }

    @JSON(include = true)
    public double getRight() {
        return right;
    }

    public void setRight(double right) {
        this.right = right;
    }

    @JSON(include = true)
    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }
}
