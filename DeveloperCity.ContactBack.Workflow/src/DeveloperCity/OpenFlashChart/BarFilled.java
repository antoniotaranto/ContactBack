package DeveloperCity.OpenFlashChart;

public class BarFilled extends BarBase<BarFilledValue> {

    public BarFilled() {
        this.type = "bar_filled";
    }

    @Override
    public BarFilled add(BarFilledValue barFilledValue) {
        this.values.add(barFilledValue);
        return this;
    }
}
