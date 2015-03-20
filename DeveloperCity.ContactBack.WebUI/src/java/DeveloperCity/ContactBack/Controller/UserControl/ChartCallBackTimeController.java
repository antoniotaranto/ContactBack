package DeveloperCity.ContactBack.Controller.UserControl;
import DeveloperCity.ContactBack.Workflow.CallService;
import DeveloperCity.OpenFlashChart.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
public class ChartCallBackTimeController {
    private static final Logger log = Logger.getLogger(ChartCallBackTimeController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/ChartCallBackTimeControl", method = { RequestMethod.GET })
    public ModelAndView Get(HttpServletResponse response) throws Exception {
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        ModelAndView mv = new ModelAndView("ChartCallBackTimeControl");
        mv.addObject("today", new SimpleDateFormat("dd/MM/yyyy").format(new Date()) );
        return mv;
    }

    @RequestMapping(value = "/ChartCallBackTimeControl/JSON", method = { RequestMethod.GET })
    public ModelAndView GetData(HttpServletResponse response) throws Exception {
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date startDate = sdf.parse(request.getParameter("startDate") + " 00:00:00");
        Date endDate = sdf.parse(request.getParameter("endDate") + " 23:59:59");

        CallService sCall = new CallService();
        List<Date[]> contactXcallback = sCall.getContactAndReturnByDates(startDate, endDate);

        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yy");

        HashMap<Integer, Integer> returnTimeCount = new HashMap<Integer, Integer>();
        returnTimeCount.put(30, 0);
        returnTimeCount.put(45, 0);
        returnTimeCount.put(60, 0);
        returnTimeCount.put(90, 0);
        returnTimeCount.put(120, 0);
        returnTimeCount.put(240, 0);
        returnTimeCount.put(360, 0);
        returnTimeCount.put(480, 0);
        returnTimeCount.put(720, 0);
        returnTimeCount.put(1200, 0);
        returnTimeCount.put(1800, 0);
        returnTimeCount.put(2400, 0);
        returnTimeCount.put(3600, 0);
        returnTimeCount.put(100000, 0);
        List<String> labels = new ArrayList<String>();
        List<PieValue> timeValues = new ArrayList<PieValue>();
        Integer[] times = returnTimeCount.keySet().toArray(new Integer[0]);
        Arrays.sort(times);

        for (Date[] dates : contactXcallback) {
            Date contactTime = dates[0];
            Date callbackTime = dates[1];
            int diff = (int) Math.floor( (callbackTime.getTime() - contactTime.getTime()) / 1000 );
            boolean found = false;
            for (Integer i : times) {
                if (diff <= i.intValue()) {
                    returnTimeCount.put(i, returnTimeCount.get(i) + 1);
                    found = true;
                    break;
                }
            }
            if (!found) { returnTimeCount.put(100000, returnTimeCount.get(100000) + 1); }
        }

        for (Integer t : times) {
            String exp = t.intValue() == 100000 ? "+3600s" : String.format("Até %ds", t);
            timeValues.add(new PieValue(returnTimeCount.get(t.intValue()), exp));
        }

        ModelAndView mv = new ModelAndView("TextPlain");

        OpenFlashChart chart =
            new OpenFlashChart()
                .setTitle( (Title) new Title("Tempo de retorno")
                    .setStyle("{font-size:20px;color:#0000ff;font-family:Verdana;text-align:center;}") )
                .setY_legend( (Legend) new Legend("De " + request.getParameter("startDate") + " até " +  request.getParameter("endDate"))
                    .setStyle("{color:#736AFF;font-size:12px;}") )
                .addElement( new Pie()
                    .setAnimate(new PieAnimationSeries(new PieAnimation("fade", 0), new PieAnimation("bounce", 4)))
                    .setGradientfill(true)
                    .setNolabels(false)
                    .setBorder(1)
                    .setColours(Arrays.asList(new String[] { "#00FF00", "#2DFF00", "#57FF00", "#81FF00", "#ABFF00", "#D5FF00", "#FFFF00", "#FFDB00", "#FFB700", "#FF9300", "#FF6F00", "#FF4B00", "#FF2700", "#FF0000" }))
                    .setValues(timeValues)
                    .setTip("#val# de #total#<br>#percent# de 100%")
                    .setAlpha(0.80)
                    .setFontSize(9D));

        mv.addObject("content", chart.toString());
        return mv;
    }
}

