package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public class AxisLabel {

    private String colour;
    private String text;
    private int size;
    private String rotate;
    private boolean visible = true;

    public AxisLabel() {
        this.visible = true;
        size = 12;
    }

    public AxisLabel(String text) {
        this.text = text;
        this.visible = true;
        size = 12;
    }

    public static AxisLabel create(String text) {
        return new AxisLabel(text);
    }

    public AxisLabel(String text, String colour, int size, String rotate) {
        this.text = text;
        this.colour = colour;
        this.size = size;
        this.rotate = rotate;
        this.visible = true;
    }

    @JSON(include = true)
    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    @JSON(include = true)
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @JSON(include = true)
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @JSON(include = true)
    public String getRotate() {
        return rotate;
    }

    public void setRotate(String rotate) {
        this.rotate = rotate;
    }

    @JSON(include = false)
    public void setVertical(boolean value) {
        if (value) {
            this.rotate = "vertical";
        }
    }

    @JSON(include = true)
    public boolean getVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
