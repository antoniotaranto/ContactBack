package DeveloperCity.ContactBack.Controller.UserControl;
import DeveloperCity.ContactBack.DomainModel.Queue;
import DeveloperCity.ContactBack.DomainModel.QueueStatus;
import DeveloperCity.ContactBack.Workflow.QueueService;
import DeveloperCity.OpenFlashChart.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
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
public class ChartSuccessCallBackController {
    private static final Logger log = Logger.getLogger(ChartAgentLoginController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/ChartSuccessCallBackControl", method = { RequestMethod.GET })
    public ModelAndView Get(HttpServletResponse response) throws Exception {
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        ModelAndView mv = new ModelAndView("ChartSuccessCallBackControl");
        mv.addObject("today", new SimpleDateFormat("dd/MM/yyyy").format(new Date()) );
        return mv;
    }

    @RequestMapping(value = "/ChartSuccessCallBackControl/JSON", method = { RequestMethod.GET })
    public ModelAndView GetData(HttpServletResponse response) throws Exception {
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date startDate = sdf.parse(request.getParameter("startDate") + " 00:00:00");
        Date endDate = sdf.parse(request.getParameter("endDate") + " 23:59:59");

        Calendar calStartPlus31 = Calendar.getInstance();
        calStartPlus31.setTime(startDate);
        calStartPlus31.add(Calendar.DATE, 31);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(endDate);
        if (calStartPlus31.before(calEnd)) {
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", false);
            mv.addObject("errorMessage", "O intervalo máximo neste relatório é de 1 mês.");
            return mv;
        }

        QueueService sQueue = new QueueService();
        List<Queue> queue = sQueue.getAllByDates(startDate, endDate);

        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yy");
        HashMap<Date, Integer[]> xAxis = new HashMap<Date, Integer[]>();

        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        for (; c.getTime().before(endDate); c.add(Calendar.DATE, 1)) {
            xAxis.put(c.getTime() , new Integer[] { 0, 0 } );
        }

        for (Queue q : queue) {
            Calendar queueDate = Calendar.getInstance();
            queueDate.setTime(q.getCallTime());
            queueDate.set(Calendar.HOUR_OF_DAY, 0);
            queueDate.set(Calendar.MINUTE, 0);
            queueDate.set(Calendar.SECOND, 0);
            queueDate.set(Calendar.MILLISECOND, 0);

            Integer[] totals = xAxis.get( queueDate.getTime() );
            totals[0]++;
            if (q.getQueueStatus() == QueueStatus.Complete) {
                totals[1]++;
            }
            xAxis.put(queueDate.getTime(), totals);
        }

        List<String> dates = new ArrayList<String>();
        List<BarGlassValue> callValues = new ArrayList<BarGlassValue>();
        List<BarGlassValue> successValues = new ArrayList<BarGlassValue>();

        double maxCalls = 0D;
        Date[] dateSet = xAxis.keySet().toArray(new Date[0]);
        Arrays.sort(dateSet);

        for (Date d : dateSet) {
            Integer[] totals = xAxis.get(d);
            dates.add(f.format(d));
            maxCalls = Math.max(maxCalls, totals[0]);
            callValues.add( (BarGlassValue) new BarGlassValue(totals[0]).setTip( String.format("%s (Contatos = %d)", f.format(d), totals[0]) ));
            successValues.add( (BarGlassValue) new BarGlassValue(totals[1]).setTip( String.format("%s (Retornos = %d)", f.format(d), totals[1]) ));
        }

        ModelAndView mv = new ModelAndView("TextPlain");

        OpenFlashChart chart =
            new OpenFlashChart()
                .setTitle( (Title) new Title("Retornos com Sucesso")
                    .setStyle("{font-size:20px;color:#0000ff;font-family:Verdana;text-align:center;}") )
                .setY_legend( (Legend) new Legend("De " + request.getParameter("startDate") + " até " +  request.getParameter("endDate"))
                    .setStyle("{color:#736AFF;font-size:12px;}") )
                .addElement( new BarGlass()
                    .setOnshow(new Animation("pop", 1D, 0D))
                    .setValues(callValues)
                    .setAlpha(0.70)
                    .setColour("#9999bb")
                    .setText("Login")
                    .setFontSize(10D))
                .addElement( new BarGlass()
                    .setOnshow(new Animation("pop", 1D, 0D))
                    .setValues(successValues)
                    .setAlpha(0.70)
                    .setColour("#44cc66")
                    .setText("Disponibilidade")
                    .setFontSize(10D) )
                .setX_axis( (XAxis) new XAxis()
                    .setLabels( (XAxisLabels) new XAxisLabels().setLabelsString(dates).Vertical(true) )
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

