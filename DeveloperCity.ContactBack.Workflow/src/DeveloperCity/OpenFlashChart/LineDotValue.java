package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public class LineDotValue {

    private Double val;
    private String tip;
    private String color;
    private Integer sides;
    private Integer rotation;
    private String type;
    private Boolean isHollow;
    private Integer dotsize;

    public LineDotValue() {
    }

    public LineDotValue(double val) {
        this.val = val;
    }

    public LineDotValue(double val, String tip, String color) {
        this.val = val;
        this.tip = tip;
        this.color = color;
    }

    public LineDotValue(double val, String color) {
        this.val = val;
        this.color = color;
    }

    @JSON(include = true)
    public Double getValue() {
        return val;
    }

    public void setValue(Double val) {
        this.val = val;
    }

    @JSON(include = true)
    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    @JSON(include = true)
    public String getColour() {
        return color;
    }

    public void setColour(String color) {
        this.color = color;
    }

    @JSON(include = true)
    public Integer getSides() {
        return sides;
    }

    public void setSides(Integer sides) {
        this.sides = sides;
    }

    @JSON(include = true)
    public Integer getRotation() {
        return rotation;
    }

    public void setRotation(Integer rotation) {
        this.rotation = rotation;
    }

    @JSON(include = true)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JSON(include = true)
    public Boolean getHollow() {
        return isHollow;
    }

    public void setHollow(Boolean isHollow) {
        this.isHollow = isHollow;
    }

    @JsonParameter(renameTo="dot-size")
    @JSON(include = true)
    public Integer getDotsize() {
        return dotsize;
    }

    public void setDotsize(Integer dotsize) {
        this.dotsize = dotsize;
    }
}
