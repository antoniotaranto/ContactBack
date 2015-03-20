package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public class LineBase extends Chart<LineDotValue> {

    private int width;
    private int dotsize;
    private int halosize;
    private String onclick;
    private boolean loop;
    private Animation onshow = new Animation();

    public LineBase() {
        this.type = "line";
        this.getDotStyle().setType(DotType.SOLID_DOT.getStringValue());
    }

    public void setOnClickFunction(String func) {
        this.getDotStyle().setOnclick(func);
        this.onclick = func;
    }

    @JSON(include = true)
    public String getOnClick() {
        return this.onclick;
    }

    public void setOnClick(String value) {
        setOnClickFunction(value);
    }

    @JSON(include = true)
    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @JsonParameter(renameTo="dot-size")
    @JSON(include = true)
    public int getDotsize() {
        return dotsize;
    }

    public void setDotsize(int dotsize) {
        this.dotsize = dotsize;
    }

    @JsonParameter(renameTo="halo-size")
    @JSON(include = true)
    public int getHalosize() {
        return halosize;
    }

    public void setHalosize(int halosize) {
        this.halosize = halosize;
    }

    @JSON(include = true)
    public boolean isLoop() {
        return loop;
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
    }

    @JsonParameter(renameTo="on-show")
    @JSON(include = true)
    public Animation getOnShow() {
        return onshow;
    }

    public void setOnShow(Animation onshow) {
        this.onshow = onshow;
    }
}
