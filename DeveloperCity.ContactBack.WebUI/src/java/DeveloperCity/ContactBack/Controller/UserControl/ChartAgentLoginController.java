package DeveloperCity.ContactBack.Controller.UserControl;
import DeveloperCity.ContactBack.DomainModel.Agent;
import DeveloperCity.ContactBack.DomainModel.WorkTime;
import DeveloperCity.ContactBack.Workflow.AgentService;
import DeveloperCity.ContactBack.Workflow.WorkTimeService;
import DeveloperCity.OpenFlashChart.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lbarbosa
 */
@Controller
public class ChartAgentLoginController {
    private static final Logger log = Logger.getLogger(ChartAgentLoginController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/ChartAgentLoginControl", method = { RequestMethod.GET })
    public ModelAndView Get(HttpServletResponse response) throws Exception {
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        ModelAndView mv = new ModelAndView("ChartAgentLoginControl");
        mv.addObject("today", new SimpleDateFormat("dd/MM/yyyy").format(new Date()) );
        return mv;
    }

    @RequestMapping(value = "/ChartAgentLoginControl/JSON", method = { RequestMethod.GET })
    public ModelAndView GetData(HttpServletResponse response) throws Exception {
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        AgentService sAgent = new AgentService();
        WorkTimeService sWorkTime = new WorkTimeService();
        List<Agent> allAgents = sAgent.getUserActive();
        Collections.sort(allAgents, new Comparator<Agent>() {

            public int compare(Agent o1, Agent o2) {
                if (o1 == null) { return 1; }
                if (o2 == null) { return -1; }
                return o1.getName().compareTo(o2.getName());
            }

        });
        List<String> agentNames = new ArrayList<String>();
        List<BarGlassValue> loginValues = new ArrayList<BarGlassValue>();
        List<BarGlassValue> availableValues = new ArrayList<BarGlassValue>();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date startDate = sdf.parse(request.getParameter("startDate") + " 00:00:00");
        Date endDate = sdf.parse(request.getParameter("endDate") + " 23:59:59");

        double maxLogin = 0D;
        for (Agent a : allAgents) {
            agentNames.add(a.getUsername());
            double loginTotal = 0D;
            double availableTotal = 0D;
            List<WorkTime> workTimes = sWorkTime.getByAgentAndDates(a, startDate, endDate);
            sWorkTime.FetchBreaks(workTimes);

            for(WorkTime w : workTimes) {
                loginTotal += (w.getSessionDuration() / 3600 );
                availableTotal += ((w.getSessionDuration() - w.getBreakDuration()) / 3600);
            }
            maxLogin = Math.max(maxLogin, loginTotal);
            loginTotal = loginTotal < 0 ? 0 : loginTotal;
            availableTotal = availableTotal < 0 ? 0 : availableTotal;
            loginValues.add( (BarGlassValue) new BarGlassValue(loginTotal).setTip( String.format("%s (Login = %d)", a.getName(), Math.round(loginTotal)) ));
            availableValues.add( (BarGlassValue) new BarGlassValue(availableTotal).setTip( String.format("%s (Disponibilidade = %d)", a.getName(), Math.round(availableTotal)) ));
        }

        ModelAndView mv = new ModelAndView("TextPlain");

        OpenFlashChart chart =
            new OpenFlashChart()
                .setTitle( (Title) new Title("Login de Agentes")
                    .setStyle("{font-size:20px;color:#0000ff;font-family:Verdana;text-align:center;}") )
                .setY_legend( (Legend) new Legend("De " + request.getParameter("startDate") + " até " +  request.getParameter("endDate"))
                    .setStyle("{color:#736AFF;font-size:12px;}") )
                .addElement( new BarGlass()
                    .setOnshow(new Animation("pop", 1D, 0D))
                    .setValues(loginValues)
                    .setAlpha(0.70)
                    .setColour("#9999bb")
                    .setText("Login")
                    .setFontSize(10D))
                .addElement( new BarGlass()
                    .setOnshow(new Animation("pop", 1D, 0D))
                    .setValues(availableValues)
                    .setAlpha(0.70)
                    .setColour("#44cc66")
                    .setText("Disponibilidade")
                    .setFontSize(10D) )
                .setX_axis( (XAxis) new XAxis()
                    .setLabels( (XAxisLabels) new XAxisLabels().setLabelsString(agentNames).Vertical(true) )
                    .setTickHeight("10")
                    .setColour("#d000d0")
                    .setGridColour("#00ff00")
                    .setStroke(1))
                .setY_axis( (YAxis) new YAxis()
                    .setTickLength( 10 )
                    .setStroke(4)
                    .setColour("#d000d0")
                    .setGridColour("#00ff00")
                    .setOffset(false)
                    .setMax( Math.ceil(maxLogin) * 1.1D + 1) );

        mv.addObject("content", chart.toString());
        return mv;
    }
}


//        OpenFlashChart chart =
//            new OpenFlashChart()
//                .setTitle( (Title) new Title("Manydatalines")
//                    .setStyle("{font-size:20px;color:#0000ff;font-family:Verdana;text-align:center;}") )
//                .setY_legend( (Legend) new Legend("OpenFlashChart")
//                    .setStyle("{color:#736AFF;font-size:12px;}") )
//                .addElement( new Bar<BarValue>()
//                    .setValues(Arrays.asList(
//                        new BarValue(9D),
//                        new BarValue(6D),
//                        new BarValue(7D),
//                        new BarValue(9D),
//                        new BarValue(5D),
//                        new BarValue(7D),
//                        new BarValue(6D),
//                        new BarValue(9D),
//                        new BarValue(7D)))
//                    .setAlpha(0.5)
//                    .setColour("#9933CC")
//                    .setText("Pageviews")
//                    .setFontSize(10D) )
//                .addElement( new Bar<BarValue>()
//                    .setValues(Arrays.asList(
//                        new BarValue(6D),
//                        new BarValue(7D),
//                        new BarValue(9D),
//                        new BarValue(5D),
//                        new BarValue(7D),
//                        new BarValue(6D),
//                        new BarValue(9D),
//                        new BarValue(7D),
//                        new BarValue(3D)))
//                    .setAlpha(0.5)
//                    .setColour("#CC9933")
//                    .setText("Pageviews2")
//                    .setFontSize(10D) )
//                .setX_axis( (XAxis) new XAxis()
//                    .setLabels( Arrays.asList("January", "February", "March", "April", "May", "June", "July", "August", "September") )
//                    .setTickHeight("10")
//                    .setColour("#d000d0")
//                    .setGridColour("#00ff00")
//                    .setStroke(1))
//                .setY_axis( (YAxis) new YAxis()
//                    .setTickLength(3)
//                    .setStroke(4)
//                    .setColour("#d000d0")
//                    .setGridColour("#00ff00")
//                    .setOffset(false)
//                    .setMax(20D) );

//        System.out.println("Primeiro:");
//        System.out.println(chart.toString());

//        String json = "{\"title\":{\"text\":\"Manydatalines\",\"style\":\"{font-size:20px;color:#0000ff;font-family:Verdana;text-align:center;}\"},\"y_legend\":{\"text\":\"OpenFlashChart\",\"style\":\"{color:#736AFF;font-size:12px;}\"},\"elements\":[{\"type\":\"bar\",\"alpha\":0.5,\"colour\":\"#9933CC\",\"text\":\"Pageviews\",\"font-size\":10,\"values\":[9,6,7,9,5,7,6,9,7]},{\"type\":\"bar\",\"alpha\":0.5,\"colour\":\"#CC9933\",\"text\":\"Pageviews2\",\"font-size\":10,\"values\":[6,7,9,5,7,6,9,7,3]}],\"x_axis\":{\"stroke\":1,\"tick_height\":10,\"colour\":\"#d000d0\",\"grid_colour\":\"#00ff00\",\"labels\":{\"labels\":[\"January\",\"February\",\"March\",\"April\",\"May\",\"June\",\"July\",\"August\",\"Spetember\"]}},\"y_axis\":{\"stroke\":4,\"tick_length\":3,\"colour\":\"#d000d0\",\"grid_colour\":\"#00ff00\",\"offset\":0,\"max\":20}}";
//        System.out.println("Segundo:");
//        System.out.println(json);
