package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public class BarSketch extends BarBase<BarValue> {

    private String outlinecolour;
    private Integer offset;

    public BarSketch(String colour, String outlinecolor, Integer offset) {
        this.type = "bar_sketch";
        this.colour = colour;
        this.outlinecolour = outlinecolor;
        this.offset = offset;
    }

    @JSON(include = true)
    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @JsonParameter(renameTo="outline-colour")
    @JSON(include = true)
    public String getOutlinecolour() {
        return outlinecolour;
    }

    public void setOutlinecolour(String outlinecolour) {
        this.outlinecolour = outlinecolour;
    }
}
