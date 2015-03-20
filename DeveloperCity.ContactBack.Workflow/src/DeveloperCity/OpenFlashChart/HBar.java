package DeveloperCity.OpenFlashChart;

public class HBar extends BarBase<HBarValue> {

    public HBar() {
        this.type = "hbar";
    }

    @Override
    public HBar add(HBarValue hBarValue) {
        this.values.add(hBarValue);
        return this;
    }
}
