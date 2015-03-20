package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public class RadarAxis extends XAxis {

    private XAxisLabels spokelabels;

    public RadarAxis(double max) {
        this.setMax(max);
    }

    public RadarAxis() {
    }

    @JsonParameter(renameTo="spoke-labels")
    @JSON(include = true)
    public XAxisLabels getSpokeLabels() {
        return spokelabels;
    }

    public void setSpokeLabels(XAxisLabels value) {
        spokelabels = value;
    }
}
