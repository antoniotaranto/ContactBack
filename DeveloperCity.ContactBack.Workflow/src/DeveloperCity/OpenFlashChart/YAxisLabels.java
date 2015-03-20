package DeveloperCity.OpenFlashChart;

import java.util.ArrayList;
import java.util.List;

public class YAxisLabels extends AxisLabels {

    @Override
    public YAxisLabels setLabelsString(List<String> labelsvalue) {
        int pos = 0;
        if (labels == null) {
            labels = new ArrayList<Object>();
        }
        for (String s : labelsvalue) {
            labels.add(s);
            pos++;
        }
        return this;
    }
}
