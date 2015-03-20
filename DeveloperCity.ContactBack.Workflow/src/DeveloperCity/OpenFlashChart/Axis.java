package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public class Axis {

    private Integer stroke;
    private String colour;
    private String gridColour;
    private Integer steps;
    private int _3D;
    private Double min;
    private Double max;
    private boolean offset;

    public Axis() {
        offset = true;
    }

    @JSON(include = true)
    public Integer getStroke() {
        return stroke;
    }

    public Axis setStroke(Integer stroke) {
        this.stroke = stroke;
        return this;
    }

    @JSON(include = true)
    public String getColour() {
        return colour;
    }

    public Axis setColour(String colour) {
        this.colour = colour;
        return this;
    }

    @JsonParameter(renameTo="grid-colour")
    @JSON(include = true)
    public String getGridColour() {
        return gridColour;
    }

    public Axis setGridColour(String gridColour) {
        this.gridColour = gridColour;
        return this;
    }

    @JSON(include = true)
    public Integer getSteps() {
        return steps;
    }

    public Axis setSteps(Integer steps) {
        this.steps = steps;
        return this;
    }

    public Axis setColors(String color, String gridcolor) {
        this.colour = color;
        this.gridColour = gridcolor;
        return this;
    }

    @JSON(include = true)
    public Double getMin() {
        return min;
    }

    public Axis setMin(Double min) {
        this.min = min;
        return this;
    }

    @JSON(include = true)
    public Double getMax() {
        return max;
    }

    public Axis setMax(Double max) {
        this.max = max;
        return this;
    }

    @JSON(include = true)
    public int get3D() {
        return _3D;
    }

    public Axis set3D(int _3D) {
        this._3D = _3D;
        return this;
    }

    public boolean getOffset() {
        return this.offset;
    }

    public Axis setOffset(boolean value) {
        this.offset = value;
        return this;
    }

    public Axis setRange(Double min, Double max) {
        this.max = max;
        this.min = min;
        return this;
    }
}
