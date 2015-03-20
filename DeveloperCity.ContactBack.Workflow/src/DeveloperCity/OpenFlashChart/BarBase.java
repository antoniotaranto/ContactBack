package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public abstract class BarBase<T> extends Chart<T> {

    private Animation onshow = new Animation();

    protected BarBase() {
        this.type = "bar";
    }

    @JsonParameter(renameTo="on-show")
    @JSON(include = true)
    public Animation getOnshow() {
        return onshow;
    }

    public BarBase<T> setOnshow(Animation onshow) {
        this.onshow = onshow;
        return this;
    }
}
