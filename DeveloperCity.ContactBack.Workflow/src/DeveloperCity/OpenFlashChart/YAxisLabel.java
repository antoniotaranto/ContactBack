package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public class YAxisLabel extends AxisLabel {

    private Integer y;

    @JSON(include = true)
    public Integer getY() {
        return y;
    }

    public void setY(Integer value) {
        y = value;
    }

    public YAxisLabel(String text, int ypos) {
        super(text);
        y = ypos;
    }
}
