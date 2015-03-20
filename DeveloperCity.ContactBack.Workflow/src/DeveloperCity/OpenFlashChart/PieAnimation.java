package DeveloperCity.OpenFlashChart;

import flexjson.JSON;

public class PieAnimation extends AnimationBase {

    private String type;
    private Integer distance;
    /// <summary>
    /// used in pie animation
    /// </summary>
    /// <param name="type"></param>
    /// <param name="distance"></param>

    public PieAnimation(String type, Integer distance) {
        this.type = type;
        this.distance = distance;
    }

    @JSON(include = true)
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @JSON(include = true)
    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }
}
