package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public class ChartElement {

    private String text;
    private String style;

    @JSON(include = true)
    public String getText() {
        return text;
    }

    public ChartElement setText(String text) {
        this.text = text;
        return this;
    }

    @JSON(include = true)
    public String getStyle() {
        if (style == null) {
            style = "{font-size: 20px; color:#0000ff; font-family: Verdana; text-align: center;}";
        }
        return this.style;
    }

    public ChartElement setStyle(String style) {
        this.style = style;
        return this;
    }
}
