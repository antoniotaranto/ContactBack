package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public class Bar<X extends BarValue> extends BarBase<X> {

    public Bar() {
        this.type = "bar";
    }
    /// <summary>
    ///
    /// </summary>

    @JSON(include = false)
    public String getBarType() {
        return this.type;
    }

    public Bar<X> setBarType(String value) {
        this.type = value;
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Bar<X> add(BarValue barValue) {
        this.values.add((X)barValue);
        return this;
    }
}
