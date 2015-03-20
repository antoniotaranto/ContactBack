package DeveloperCity.OpenFlashChart;

import java.util.List;

public class BarStack<X extends BarStackValue> extends BarBase<X> {

    public BarStack() {
        this.type = "bar_stack";
    }

    @SuppressWarnings("unchecked") @Override
    public BarStack<X> add(BarStackValue barStackValue) {
        this.values.add((X)barStackValue);
        return this;
    }

    @SuppressWarnings("unchecked")
    public BarStack<X> addStack(List<BarStackValue> barStackValues) {
        for (BarStackValue b : barStackValues) {
            this.values.add((X)b);
        }
        return this;
    }
}
