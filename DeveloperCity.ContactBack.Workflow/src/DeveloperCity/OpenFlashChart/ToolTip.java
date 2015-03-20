package DeveloperCity.OpenFlashChart;

import flexjson.JSON;
//{       
//        shadow:		true,
//
//
//      shadow:
//
//
//
//       true,
//        rounded:	1,
//        stroke:		2,
//        colour:		'#808080',
//        background:	'#f0f0f0',
//        title:		"color: #0000F0; font-weight: bold; font-size: 12;",
//        body:		"color: #000000; font-weight: normal; font-size: 12;",
//        mouse:		Tooltip.CLOSEST,
//        text:		"_default"
//}

public class ToolTip {

    private String text = "_default";
    private boolean shadow = true;
    private int rounded = 1;
    private int stroke = 2;
    private String colour;//= "#808080";
    private String background;//= "#f0f0f0";
    private String titlestyle;// = "color: #0000F0; font-weight: bold; font-size: 12;";
    private String bodystyle;//= "color: #000000; font-weight: normal; font-size: 12;";
    private ToolTipStyle mousestyle;//= ToolTipStyle.CLOSEST;
    public int mouse;

    public ToolTip(String text) {
        this.text = text;
    }

    @JSON(include = true)
    public String getText() {
        return text;
    }

    public void setText(String value) {
        text = value;
    }

    @JSON(include = true)
    public boolean getShadow() {
        return shadow;
    }

    public void setShadow(boolean value) {
        shadow = value;
    }

    @JSON(include = true)
    public int getRounded() {
        return rounded;
    }

    public void setRounded(int value) {
        rounded = value;
    }

    @JSON(include = true)
    public int getStroke() {
        return stroke;
    }

    public void setStroke(int value) {
        stroke = value;
    }

    @JSON(include = true)
    public String getColour() {
        return colour;
    }

    public void setColour(String value) {
        colour = value;
    }

    @JSON(include = true)
    public String getBackground() {
        return background;
    }

    public void setBackground(String value) {
        background = value;
    }

    @JSON(include = true)
    public String getTitle() {
        return titlestyle;
    }

    public void setTitle(String value) {
        titlestyle = value;
    }

    @JSON(include = true)
    public String getBody() {
        return bodystyle;
    }

    public void setBody(String value) {
        bodystyle = value;
    }

    @JSON(include = false)
    public ToolTipStyle getMouseStyle() {
        return mousestyle;
    }

    public void setMouseStyle(ToolTipStyle value) {
        mousestyle = value;
        mouse = value.getValue();
    }

    public void setProximity() {
        mouse = 1;
    }

    @Override
    public String toString() {
        return this.text;
    }
}
