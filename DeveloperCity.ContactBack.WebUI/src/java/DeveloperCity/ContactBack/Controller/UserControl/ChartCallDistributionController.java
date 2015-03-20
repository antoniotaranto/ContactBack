package DeveloperCity.ContactBack.Controller.UserControl;
import DeveloperCity.ContactBack.Workflow.CallService;
import DeveloperCity.ContactBack.Workflow.QueueService;
import DeveloperCity.OpenFlashChart.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
public class ChartCallDistributionController {
    private static final Logger log = Logger.getLogger(ChartCallDistributionController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/ChartCallDistributionControl", method = { RequestMethod.GET })
    public ModelAndView Get(HttpServletResponse response) throws Exception {
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        ModelAndView mv = new ModelAndView("ChartCallDistributionControl");
        mv.addObject("today", new SimpleDateFormat("dd/MM/yyyy").format(new Date()) );
        return mv;
    }

    @RequestMapping(value = "/ChartCallDistributionControl/JSON", method = { RequestMethod.GET })
    public ModelAndView GetData(HttpServletResponse response) throws Exception {
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date startDate = sdf.parse(request.getParameter("startDate") + " 00:00:00");
        Date endDate = sdf.parse(request.getParameter("endDate") + " 23:59:59");

        QueueService sQueue = new QueueService();
        CallService sCall = new CallService();
        HashMap<Integer, Integer> queue = sQueue.countByCallHour(startDate, endDate);
        HashMap<Integer, Integer> calls = sCall.countByAnswerHour(startDate, endDate);

        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yy");
        
        List<String> hours = new ArrayList<String>();
        List<BarGlassValue> queueValues = new ArrayList<BarGlassValue>();
        List<BarGlassValue> callValues = new ArrayList<BarGlassValue>();

        double maxCalls = 0D;

        for (int i = 0; i < 24; i++) {
            String startHour = String.format("%d:00", i);
            String endHour = String.format("%d:00", (i == 23 ? 0 : i + 1));
            String interval = String.format("%s - %s", startHour, endHour);
            hours.add(interval);
            if (queue.get(i) != null) { maxCalls = Math.max(maxCalls, queue.get(i)); }
            if (calls.get(i) != null) { maxCalls = Math.max(maxCalls, calls.get(i)); }
            queueValues.add((BarGlassValue)new BarGlassValue(queue.get(i) != null ? queue.get(i) : 0).setTip(String.format("#top# contatos (%s)", interval)));
            callValues.add((BarGlassValue)new BarGlassValue(calls.get(i) != null ? calls.get(i) : 0).setTip(String.format("#top# retornos (%s)", interval )));
        }

        ModelAndView mv = new ModelAndView("TextPlain");

        OpenFlashChart chart =
            new OpenFlashChart()
                .setTitle( (Title) new Title("Distribuição de chamadas")
                    .setStyle("{font-size:20px;color:#0000ff;font-family:Verdana;text-align:center;}") )
                .setY_legend( (Legend) new Legend("De " + request.getParameter("startDate") + " até " +  request.getParameter("endDate"))
                    .setStyle("{color:#736AFF;font-size:12px;}") )
                .addElement( new BarGlass()
                    .setOnshow(new Animation("pop", 1D, 0D))
                    .setValues(queueValues)
                    .setAlpha(0.70)
                    .setColour("#9999bb")
                    .setText("Fila")
                    .setFontSize(10D))
                .addElement( new BarGlass()
                    .setOnshow(new Animation("pop", 1D, 0D))
                    .setValues(callValues)
                    .setAlpha(0.70)
                    .setColour("#44cc66")
                    .setText("Retornos")
                    .setFontSize(10D) )
                .setX_axis( (XAxis) new XAxis()
                    .setLabels( (XAxisLabels) new XAxisLabels().setLabelsString(hours).Vertical(true) )
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
                    .setMax( Math.ceil(maxCalls) * 1.1D + 1) );

        mv.addObject("content", chart.toString());
        return mv;
    }
}

