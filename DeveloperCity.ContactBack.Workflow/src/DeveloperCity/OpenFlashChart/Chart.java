package DeveloperCity.OpenFlashChart;

import flexjson.JSON;
import java.util.ArrayList;
import java.util.List;

public class Chart<T> extends ChartBase {
    protected List<T> values;

    public Chart() {
        this.values = new ArrayList<T>();
        this.fillalpha = 0.35;
        //fontsize = 20;
    }

    //public override double GetMaxValue()
    //{
    //    if (values.Count == 0)
    //        return 0;
    //    double max = double.MinValue;
    //    Type valuetype = typeof(T);
    //    if (!valuetype.IsValueType)
    //        return 0;
    //    foreach (T d in values)
    //    {
    //        double temp = double.Parse(d.ToString());
    //        if (temp > max)
    //            max = temp;
    //    }
    //    return max;
    //}
    //public override double GetMinValue()
    //{
    //    if (values.Count == 0)
    //        return 0;
    //    double min = double.MaxValue;
    //    Type valuetype = typeof (T);
    //    if (!valuetype.IsValueType)
    //        return 0;
    //    foreach (T d in values)
    //    {
    //        double temp = double.Parse(d.ToString());
    //        if(temp<min)
    //            min = temp;
    //    }
    //    return min;
    //}
    public Chart<T> add(T v) {
        this.values.add(v);
        return this;
    }
    public int getValueCount() {
        return values.size();
    }
    @JSON(include = true)
    public List<T> getValues() {
        return this.values;
    }

    public Chart<T> setValues(List<T> value) {
        this.values = value;
        return this;
    }
}
