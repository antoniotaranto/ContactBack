package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public class Scatter extends Chart<ScatterValue> {

    private Integer dotsize;

    public Scatter() {
        this.type = "scatter";
    }

    public Scatter(String color, Integer dotsize) {
        this.type = "scatter";
        this.colour = color;
        this.dotsize = dotsize;
        this.getDotStyle().setType(DotType.SOLID_DOT.getStringValue());
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
