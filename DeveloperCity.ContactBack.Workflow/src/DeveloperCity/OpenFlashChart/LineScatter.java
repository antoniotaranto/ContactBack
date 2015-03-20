package DeveloperCity.OpenFlashChart;
    public class LineScatter extends Scatter
    {
        public LineScatter()
        {
            this.type = "scatter_line";
            this.dotstyle.setType(DotType.SOLID_DOT.getStringValue());
        }
    }
