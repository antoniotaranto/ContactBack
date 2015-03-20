package DeveloperCity.OpenFlashChart;

public class LineHollow extends LineBase {

    public LineHollow() {
        //this.ChartType = "line_hollow";
        this.dotstyle.setHollow(true);
        this.dotstyle.setType(DotType.HOLLOW_DOT.getStringValue());
    }
}
