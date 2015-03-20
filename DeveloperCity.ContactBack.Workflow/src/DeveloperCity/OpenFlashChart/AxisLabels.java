package DeveloperCity.OpenFlashChart;

import flexjson.JSON;
import java.util.ArrayList;
import java.util.List;

public class AxisLabels {

    private Integer steps;
    protected List<Object> labels;
    private String colour;
    private String rotate;
    private Integer fontsize;
    private Integer visiblesteps;
    private String formatstring;

    @JSON(include = true)
    public Integer getSteps() {
        if (this.steps == null) {
            return null;
        }
        return this.steps;
    }

    public AxisLabels setSteps(Integer value) {
        this.steps = value;
        return this;
    }

    @JSON(include = true)
    public List<Object> getLabels() {
        return labels;
    }

    public AxisLabels setLabels(List<Object> value) {
        this.labels = value;
        return this;
    }

    public AxisLabels setLabelsString(List<String> labelsvalue) {
        if (labels == null) {
            labels = new ArrayList<Object>();
        }
        for (String s : labelsvalue) {
            labels.add(s);
        }
        return this;
    }

    public AxisLabels add(AxisLabel label) {
        if (labels == null) {
            labels = new ArrayList<Object>();
        }
        labels.add(label);
        return this;
    }

    @JSON(include = true)
    public String getColour() {
        return this.colour;
    }

    public AxisLabels setColour(String colour) {
        this.colour = colour;
        return this;
    }

    @JSON(include = true)
    public String getRotate() {
        return rotate;
    }

    public AxisLabels setRotate(String rotate) {
        this.rotate = rotate;
        return this;
    }

    @JSON(include = false)
    public AxisLabels Vertical(boolean value) {
        if (value) {
            this.rotate = "vertical";
        }
        return this;
    }

    @JSON(include = true)
    public Integer getSize() {
        return fontsize;
    }

    public AxisLabels setSize(Integer fontsize) {
        this.fontsize = fontsize;
        return this;
    }

    @JSON(include = true)
    public String getText() {
        return formatstring;
    }

    public AxisLabels setText(String formatstring) {
        this.formatstring = formatstring;
        return this;
    }

    @JsonParameter(renameTo="visible-steps")
    @JSON(include = true)
    public Integer getVisiblesteps() {
        return visiblesteps;
    }

    public AxisLabels setVisiblesteps(Integer visiblesteps) {
        this.visiblesteps = visiblesteps;
        return this;
    }
}
