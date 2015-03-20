package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public class ScatterValue {

    private double x;
    private double y;
    private Integer dotsize;
    private String dottype;
    private String onclick;

    public ScatterValue(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public ScatterValue(double x, double y, int dotsize) {
        this.x = x;
        this.y = y;
        if (dotsize > 0) {
            this.dotsize = dotsize;
        }
        //this.dottype = DotType.HOLLOW_DOT;
    }

    @JSON(include = true)
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    @JSON(include = true)
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @JsonParameter(renameTo="dot-size")
    @JSON(include = true)
    public Integer getDotsize() {
        if (dotsize == null) {
            return -1;
        }

        return dotsize;
    }

    public void setDotsize(Integer dotsize) {
        this.dotsize = dotsize;
    }

    public String getType() {
        return dottype;
    }

    public void setType(String dottype) {
        this.dottype = dottype;
    }

    @JsonParameter(renameTo="on-click")
    @JSON(include = true)
    public String getOnclick() {
        return onclick;
    }

    public void setOnclick(String onclick) {
        this.onclick = onclick;
    }
}
