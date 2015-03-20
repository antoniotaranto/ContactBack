package DeveloperCity.OpenFlashChart;

import flexjson.JSON;
import java.util.List;

public class YAxis extends Axis {

    private int tick_length;
    private YAxisLabels labels;

    @JsonParameter(renameTo = "tick-length")
    @JSON(include = true)
    public int getTickLength() {
        return tick_length;
    }

    public YAxis setTickLength(int value) {
        tick_length = value;
        return this;
    }

    public YAxis setRange(double min, double max, int step) {
        this.setMax(max);
        this.setMin(min);
        this.setSteps(step);
        return this;
    }

    @JSON(include = true)
    public YAxisLabels getLabels() {
        if (this.labels == null) {
            this.labels = new YAxisLabels();
        }
        return this.labels;
    }

    public YAxis setLabels(YAxisLabels value) {
        this.labels = value;
        return this;
    }

    public YAxis setLabels(List<String> labelsvalue) {
        getLabels().setLabelsString(labelsvalue);
        return this;
    }
}
