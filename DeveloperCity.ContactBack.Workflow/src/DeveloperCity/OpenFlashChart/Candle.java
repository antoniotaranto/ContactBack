package DeveloperCity.OpenFlashChart;

public class Candle extends BarBase<CandleValue> {

    public Candle() {
        this.type = "candle";
    }

    @Override
    public Candle add(CandleValue candleValue) {
        this.values.add(candleValue);
        return this;
    }
}
