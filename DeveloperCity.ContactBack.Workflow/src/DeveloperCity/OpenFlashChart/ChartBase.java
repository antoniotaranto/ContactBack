package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public abstract class ChartBase {

    protected String type;
    protected double fillalpha;
    protected Double alpha;
    protected Double fontsize;
    protected String colour;
    protected String text;
    protected String tooltip;
    protected DotStyle dotstyle;
    protected boolean attachtorightaxis;

    protected ChartBase() {
        fillalpha = 0.35;
        colour = "#CC3399";
        attachtorightaxis = false;
    }

    public ChartBase AttachToRightAxis(boolean attach) {
        attachtorightaxis = attach;
        return this;
    }

    @JSON(include = true)
    public String getAxis() {
        if (attachtorightaxis) {
            return "right";
        }
        return null;
    }

    public ChartBase setAxis(String attachtorightaxis) {
        this.attachtorightaxis = attachtorightaxis.equalsIgnoreCase("right");
        return this;
    }

    @JSON(include = true)
    public String getColour() {
        return this.colour;
    }

    public ChartBase setColour(String value) {
        this.colour = value;
        return this;
    }

    @JSON(include = true)
    public String getTip() {
        return this.tooltip;
    }

    public ChartBase setTip(String value) {
        this.tooltip = value;
        return this;
    }

    @JsonParameter(renameTo="font-size")
    @JSON(include = true)
    public Double getFontSize() {
        return fontsize;
    }

    public ChartBase setFontSize(Double value) {
        fontsize = value;
        return this;
    }

    @JSON(include = true)
    public String getText() {
        return text;
    }

    public ChartBase setText(String text) {
        this.text = text;
        return this;
    }

    @JsonParameter(renameTo="fill-alpha")
    @JSON(include = true)
    public double getFillAlpha() {
        return fillalpha;
    }

    public ChartBase setFillAlpha(double fillalpha) {
        this.fillalpha = fillalpha;
        return this;
    }

    @JSON(include = true)
    public Double getAlpha() {
        return alpha;
    }

    public ChartBase setAlpha(Double alpha) {
        this.alpha = alpha;
        return this;
    }

    @JSON(include = true)
    public String getType() {
        return type;
    }

    public ChartBase setType(String type) {
        this.type = type;
        return this;
    }

    @JSON(include = true)
    @JsonParameter(renameTo="dot-style")
    public DotStyle getDotStyle() {
        if (dotstyle == null) {
            dotstyle = new DotStyle();
        }
        return dotstyle;
    }

    public ChartBase setDotStyle(DotStyle dotstyle) {
        if (dotstyle == null) {
            dotstyle = new DotStyle();
        }
        this.dotstyle = dotstyle;
        return this;
    }

    public ChartBase setKey(String key, double font_size) {
        this.text = key;
        this.fontsize = font_size;
        return this;
    }

}
