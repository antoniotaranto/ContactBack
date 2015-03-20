package DeveloperCity.OpenFlashChart;

import java.util.ArrayList;
import java.util.Arrays;

public class PieAnimationSeries extends ArrayList<PieAnimation> {
    private static final long serialVersionUID = 1L;
    public PieAnimationSeries() { }
    public PieAnimationSeries (PieAnimation... animations) {
        this.addAll(Arrays.asList(animations));
    }
}
