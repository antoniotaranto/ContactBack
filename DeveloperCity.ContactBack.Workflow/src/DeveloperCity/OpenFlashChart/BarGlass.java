package DeveloperCity.OpenFlashChart;

public class BarGlass extends BarBase<BarGlassValue> {

    public BarGlass() {
        this.type = "bar_glass";
    }

    @Override
    public BarGlass add(BarGlassValue barGlassValue) {
        this.values.add(barGlassValue);
        return this;
    }
}
