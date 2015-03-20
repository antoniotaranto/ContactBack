package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public class CandleValue extends BarValue {

    protected Double high;
    protected Double low;

    @JSON(include = true)
    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    @JSON(include = true)
    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public CandleValue() {
    }

    public CandleValue(double high, double top, double bottom, double low) {
        this.high = high;
        this.top = top;
        this.bottom = bottom;
        this.low = low;
    }
}
