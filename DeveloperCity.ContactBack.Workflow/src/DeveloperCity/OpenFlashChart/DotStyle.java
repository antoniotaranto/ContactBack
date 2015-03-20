package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public class DotStyle {

    private String type;
    private Integer sides;
    private Double alpha;
    private Boolean hollow;
    private String background_colour;
    private Double background_alpha;
    private Integer width;
    private String tip;
    private String colour;
    private Integer dotsize;
    private String onclick;
    private Animation onshow = new Animation();

    @JsonParameter(renameTo="on-show")
    @JSON(include = true)
    public Animation getOnshow() {
        return onshow;
    }

    public void setOnshow(Animation onshow) {
        this.onshow = onshow;
    }

    @JSON(include = true)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JSON(include = true)
    public Integer getSides() {
        return sides;
    }

    public void setSides(Integer sides) {
        this.sides = sides;
    }

    @JSON(include = true)
    public Double getAlpha() {
        return alpha;
    }

    public void setAlpha(Double alpha) {
        this.alpha = alpha;
    }

    @JSON(include = true)
    public Boolean getHollow() {
        return hollow;
    }

    public void setHollow(Boolean hollow) {
        this.hollow = hollow;
    }

    @JsonParameter(renameTo="background-colour")
    @JSON(include = true)
    public String getBackground_colour() {
        return background_colour;
    }

    public void setBackground_colour(String background_colour) {
        this.background_colour = background_colour;
    }

    @JsonParameter(renameTo="background-alpha")
    @JSON(include = true)
    public Double getBackground_alpha() {
        return background_alpha;
    }

    public void setBackground_alpha(Double background_alpha) {
        this.background_alpha = background_alpha;
    }

    @JSON(include = true)
    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
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
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    @JsonParameter(renameTo="dot-size")
    @JSON(include = true)
    public Integer getDotsize() {
        return dotsize;
    }

    public void setDotsize(Integer dotsize) {
        this.dotsize = dotsize;
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
