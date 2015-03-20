package DeveloperCity.OpenFlashChart;

import flexjson.JSON;
import java.util.List;

public class XAxis extends Axis {

    private String tick_height;
    private XAxisLabels labels;

    @JsonParameter(renameTo="tick-height")
    @JSON(include = true)
    public String getTickHeight() {
        return tick_height;
    }

    public XAxis setTickHeight(String value) {
        tick_height = value;
        return this;
    }

    @JSON(include = true)
    public XAxisLabels getLabels() {
        if (this.labels == null) {
            this.labels = new XAxisLabels();
        }
        return this.labels;
    }

    public XAxis setLabels(XAxisLabels value) {
        this.labels = value;
        return this;
    }

    public XAxis setLabels(List<String> labelsvalue) {
        getLabels().setLabelsString(labelsvalue);
        return this;
    }
}
