package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public abstract class AreaBase extends Chart<Double> {

    private Integer width;
    private Double dotsize;
    private Double halosize;
    private boolean loop;
    private String fillcolour;
    private Animation onshow = new Animation();

    protected AreaBase() {
        this.type = "area";
    }

    @JSON(include = true)
    public String getFill() {
        return fillcolour;
    }

    public void setFill(String fillcolour) {
        this.fillcolour = fillcolour;
    }

    @JSON(include = true)
    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    @JsonParameter(renameTo="dot-size")
    @JSON(include = true)
    public Double getDotsize() {
        return dotsize;
    }

    public void setDotsize(Double dotsize) {
        this.dotsize = dotsize;
    }

    @JSON(include = true)
    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    @JsonParameter(renameTo="halo-size")
    @JSON(include = true)
    public Double getHalosize() {
        return halosize;
    }

    public void setHalosize(Double halosize) {
        this.halosize = halosize;
    }

    @JsonParameter(renameTo="on-show")
    @JSON(include = true)
    public Animation getOnshow() {
        return onshow;
    }

    public void setOnshow(Animation onshow) {
        this.onshow = onshow;
    }
}
