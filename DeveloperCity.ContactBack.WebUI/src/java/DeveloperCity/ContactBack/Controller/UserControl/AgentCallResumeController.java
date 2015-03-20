/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Controller.UserControl;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class AgentCallResumeController {
    private static final Logger log = Logger.getLogger(AgentCallResumeController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/AgentCallResumeControl", method = { RequestMethod.GET })
    public ModelAndView Get(HttpServletResponse response) throws Exception {
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        ModelAndView mv = new ModelAndView("AgentCallResumeControl");
        mv.addObject("today", new SimpleDateFormat("dd/MM/yyyy").format(new Date()) );
        return mv;
    }

    @RequestMapping(value = "/AgentCallResumeControl", method = { RequestMethod.POST })
    public ModelAndView Post(HttpServletResponse response) throws Exception {
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        if (startDate == null || endDate == null || startDate.trim().length() == 0 || endDate.trim().length() == 0) {
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", false);
            mv.addObject("errorMessage", "As datas de início e fim são obrigatórias.");
            return mv;
        }

        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        Date validStart = null;
        Date validEnd = null;
        try {
            validStart = dateFormatter.parse(startDate);
        } catch(Exception ex) {
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", false);
            mv.addObject("errorMessage", "A data de início não é válida. Favor digitar no padrão dia/mês/ano.");
            return mv;
        }
        try {
            validEnd = dateFormatter.parse(endDate);
        } catch(Exception ex) {
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", false);
            mv.addObject("errorMessage", "A data de fim não é válida. Favor digitar no padrão dia/mês/ano.");
            return mv;
        }
        if (validStart == null || validEnd == null) {
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", false);
            mv.addObject("errorMessage", "As datas de início e fim são obrigatórias. Favor digitar no padrão dia/mês/ano.");
            return mv;
        }

        if (validStart.after(validEnd)) {
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", false);
            mv.addObject("errorMessage", "A data de início não deve ser posterior à data de fim.");
            return mv;
        }

        ModelAndView mv = new ModelAndView("AjaxResponse");
        mv.addObject("success", true);
        mv.addObject("errorMessage", "");
        return mv;
    }
}