package DeveloperCity.OpenFlashChart;

import flexjson.BasicType;
import flexjson.JSON;
import flexjson.TypeContext;
import flexjson.transformer.AbstractTransformer;
import java.util.ArrayList;
import java.util.List;

public class OpenFlashChart {

    private Title title;
    private List<ChartBase> elements;
    private XAxis x_axis;
    private YAxis y_axis;
    private YAxis y_axis_right;
    private Legend x_legend;
    private Legend y_legend;
    private Legend y2_legend;
    private String bgcolor;
    private RadarAxis radar_axis;
    private ToolTip tooltip;
    private Integer num_decimals;
    private Boolean is_fixed_num_decimals_forced;
    private Boolean is_decimal_separator_comma;
    private Boolean is_thousand_separator_disabled;

    public OpenFlashChart() {
        title = new Title("Chart Title");
        elements = new ArrayList<ChartBase>();
        //x_axis = new XAxis();
        //y_axis= new YAxis();
        //y_axis_right = new YAxis();
    }

    @JSON(include = true)
    public Title getTitle() {
        return this.title;
    }

    public OpenFlashChart setTitle(Title value) {
        this.title = value;
        return this;
    }

    @JSON(include = true)
    public XAxis getX_axis() {
        if (x_axis == null) {
            x_axis = new XAxis();
        }
        return this.x_axis;
    }

    public OpenFlashChart setX_axis(XAxis value) {
        this.x_axis = value;
        return this;
    }

    @JSON(include = true)
    public YAxis getY_axis() {
        if (y_axis == null) {
            y_axis = new YAxis();
        }
        return this.y_axis;
    }

    public OpenFlashChart setY_axis(YAxis value) {
        this.y_axis = value;
        return this;
    }

    @JSON(include = true)
    public YAxis getY_axis_right() {
        return y_axis_right;
    }

    public OpenFlashChart setY_axis_right(YAxis value) {
        y_axis_right = value;
        return this;
    }

    @JSON(include = true)
    public List<ChartBase> getElements() {
        return elements;
    }

    public OpenFlashChart setElements(List<ChartBase> value) {
        elements = value;
        return this;
    }

    public OpenFlashChart addElement(ChartBase chart) {
        this.elements.add(chart);
        //Y_Axis.SetRange(chart.GetMinValue(), chart.GetMaxValue());
        // X_Axis.SetRange(0,chart.GetValueCount());
        getX_axis().setSteps(1);
        return this;
    }

    @JSON(include = true)
    public Legend getX_legend() {
        return x_legend;
    }

    public OpenFlashChart setX_legend(Legend value) {
        x_legend = value;
        return this;
    }

    @JSON(include = true)
    public Legend getY_legend() {
        return y_legend;
    }

    public OpenFlashChart setY_legend(Legend value) {
        y_legend = value;
        return this;
    }

    @JSON(include = true)
    public Legend getY2_legend() {
        return y2_legend;
    }

    public OpenFlashChart setY2_legend(Legend value) {
        y2_legend = value;
        return this;
    }

    @JSON(include = true)
    public String getBg_colour() {
        return bgcolor;
    }

    public OpenFlashChart setBg_colour(String value) {
        bgcolor = value;
        return this;
    }

    @JSON(include = true)
    public ToolTip getTooltip() {
        return tooltip;
    }

    public OpenFlashChart setTooltip(ToolTip value) {
        tooltip = value;
        return this;
    }

    @JSON(include = true)
    public RadarAxis getRadar_axis() {
        return this.radar_axis;
    }

    public OpenFlashChart setRadar_axis(RadarAxis value) {
        this.radar_axis = value;
        return this;
    }

    @JSON(include = true)
    public Integer getNum_decimals() {
        return num_decimals;
    }

    public OpenFlashChart getNum_decimals(Integer value) {
        num_decimals = value;
        return this;
    }

    @JSON(include = true)
    public Boolean getIs_fixed_num_decimals_forced() {
        return is_fixed_num_decimals_forced;
    }

    public OpenFlashChart setIs_fixed_num_decimals_forced(Boolean value) {
        is_fixed_num_decimals_forced = value;
        return this;
    }

    @JSON(include = true)
    public Boolean getIs_decimal_separator_comma() {
        return is_decimal_separator_comma;
    }

    public OpenFlashChart setIs_decimal_separator_comma(Boolean value) {
        is_decimal_separator_comma = value;
        return this;
    }

    @JSON(include = true)
    public Boolean getIs_thousand_separator_disabled() {
        return is_thousand_separator_disabled;
    }

    public OpenFlashChart setIs_thousand_separator_disabled(Boolean value) {
        is_thousand_separator_disabled = value;
        return this;
    }

    @Override
    public String toString() {
        try {
            flexjson.JSONSerializer ser = new flexjson.JSONSerializer().transform(new AbstractTransformer() {

                @Override
                public Boolean isInline() {
                    return Boolean.TRUE;
                }

                public void transform(Object o) {
                    if (o == null) {
                        return;
                    }

                    boolean setContext = false;
                    TypeContext typeContext = getContext().peekTypeContext();
                    String propertyName = typeContext != null ? typeContext.getPropertyName() : "";
                    if (getContext().getObjectStack().size() > 1) {
                        Object father = getContext().getObjectStack().get(1);
                        for (java.lang.reflect.Method m : father.getClass().getMethods()) {
                            if (m.getReturnType() == o.getClass() && m.getName().equalsIgnoreCase("get" + propertyName)) {
                                JsonParameter p = m.getAnnotation(JsonParameter.class);
                                if (p != null && p.renameTo() != null && p.renameTo().length() > 0) {
                                    propertyName = p.renameTo();
                                }
                                break;
                            }
                        }
                    }
                    else {
                        flexjson.TransformerUtil.getDefaultTypeTransformers().getTransformer(o).transform(o);
                        return;
                    }

                    if (typeContext == null) {// || typeContext.getBasicType() != BasicType.OBJECT) {
                        typeContext = getContext().writeOpenObject();
                        setContext = true;
                    } else if (typeContext.getBasicType() != BasicType.OBJECT) {
                        typeContext.setFirst(true);
                    }
                    if (!typeContext.isFirst()) {
                        getContext().writeComma();
                    }
                    typeContext.setFirst(false);

                    if (propertyName != null) {
                        getContext().writeName(propertyName);
                    }

                    flexjson.TransformerUtil.getDefaultTypeTransformers().getTransformer(o).transform(o);

                    if (setContext) {
                        getContext().writeCloseObject();
                    }
                }
            }, Object.class, void.class);
            String retValue = ser.exclude("*.class").deepSerialize(this);
            return retValue;
        } catch (Exception e) {
            return null;
        }
    }
}
