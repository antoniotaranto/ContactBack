package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public class Animation extends AnimationBase {

    private String type;
    private Double cascade;
    private Double delay;

    public Animation() {
    }

    public Animation(String type, Double cascade, Double delay) {
        this.type = type;
        this.cascade = cascade;
        this.delay = delay;
    }

    @JSON(include = true)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JSON(include = true)
    public Double getCascade() {
        return cascade;
    }

    public void setCascade(Double cascade) {
        this.cascade = cascade;
    }

    @JSON(include = true)
    public Double getDelay() {
        return delay;
    }

    public void setDelay(Double delay) {
        this.delay = delay;
    }
}
