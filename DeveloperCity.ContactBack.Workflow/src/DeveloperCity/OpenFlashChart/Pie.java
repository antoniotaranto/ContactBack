package DeveloperCity.OpenFlashChart;

import flexjson.JSON;
import java.util.Arrays;
import java.util.List;

public class Pie extends Chart<PieValue> {

    private int border;
    private List<String> colours;
    private double alpha;
    private PieAnimationSeries animate;
    private double start_angle;
    private Boolean gradientfill;
    private Boolean nolabels;

    public Pie() {
        this.type = "pie";
        this.border = 2;
        this.colours = Arrays.asList(new String[] { "#d01f3c", "#356aa0", "#C79810" });
        this.alpha = 0.6;
        //this.animate = true;
        //gradientfill = true;

    }

    @JSON(include = true)
    public List<String> getColours() {
        return colours;
    }

    public Pie setColours(List<String> colours) {
        this.colours = colours;
        return this;
    }

    @JSON(include = true)
    public int getBorder() {
        return border;
    }

    public Pie setBorder(int border) {
        this.border = border;
        return this;
    }

    @JSON(include = true)
    @Override
    public Double getAlpha() {
        return alpha;
    }

    @Override
    public Pie setAlpha(Double value) {
        if (value < 0) {
            alpha = 0;
        }
        else if ((value >= 0) && (value <= 1)) {
            alpha = value;
        }
        else if ((value > 1) && (value <= 100)) {
            alpha = value / 100;
        }
        else {
            alpha = 1.0;
        }
        return this;
    }

    @JSON(include = true)
    public PieAnimationSeries getAnimate() {
        return animate;
    }

    public Pie setAnimate(PieAnimationSeries animate) {
        this.animate = animate;
        return this;
    }

    @JsonParameter(renameTo="start-angle")
    @JSON(include = true)
    public double getStart_angle() {
        return start_angle;
    }

    public Pie setStart_angle(double start_angle) {
        this.start_angle = start_angle;
        return this;
    }

    @JsonParameter(renameTo="gradient-fill")
    @JSON(include = true)
    public Boolean getGradientfill() {
        return gradientfill;
    }

    public Pie setGradientfill(Boolean gradientfill) {
        this.gradientfill = gradientfill;
        return this;
    }

    @JsonParameter(renameTo="no-labels")
    @JSON(include = true)
    public Boolean getNolabels() {
        return nolabels;
    }

    public Pie setNolabels(Boolean nolabels) {
        this.nolabels = nolabels;
        return this;
    }
}
