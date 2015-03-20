package DeveloperCity.ContactBack.Controller;
import DeveloperCity.ContactBack.DomainModel.Holiday;
import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.Workflow.HolidayService;
import DeveloperCity.ContactBack.Workflow.UItoProcess;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lbarbosa
 */
@Controller
public class BackOfficeHolidaysController {
    private static final Logger log = Logger.getLogger(BackOfficeHolidaysController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpSession session;

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    private boolean hasAccess() {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return false;
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Holidays");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/BackOffice/Holidays", method = { RequestMethod.GET })
    public ModelAndView Get() {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        ModelAndView mv = new ModelAndView("BackOfficeHolidays");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);
        return mv;
    }

    @RequestMapping(value = "/BackOffice/Holidays/List", method = { RequestMethod.GET })
    public ModelAndView List(HttpServletResponse response) {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        HolidayService holidayService = new HolidayService();
        List<Holiday> holiday = holidayService.getAll();

        String holidayJson = null;
        try {
            List<String> includes = new ArrayList<String>();
            includes.add("weekdays");
            holidayJson = DeveloperCity.ContactBack.Workflow.RealTimeData.hibernateToJson(holiday, includes, null);
        } catch(Exception e) {
            ModelAndView mv = new ModelAndView("TextPlain");
            mv.addObject("content",  e.toString());
            return mv;
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  holidayJson );
        return mv;
    }

    @RequestMapping(value = "/BackOffice/Holidays/List", method = { RequestMethod.POST })
    public ModelAndView Refresh(HttpServletResponse response) {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        UItoProcess uiToProcess = new UItoProcess();
        try {
            boolean success = uiToProcess.reloadHolidays();
            if (success) {
                ModelAndView mv = new ModelAndView("AjaxResponse");
                mv.addObject("success", "true");
                mv.addObject("errorMessage", "");
                return mv;
            }
            else {
                ModelAndView mv = new ModelAndView("AjaxResponse");
                mv.addObject("success", "false");
                mv.addObject("errorMessage", "Ocorreu um erro ao executar sua solicitação. Por gentileza aguarde alguns instantes e tente novamente.");
                return mv;
            }
        } catch (Exception e) {
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", "false");
            mv.addObject("errorMessage", e.toString());
            return mv;
        }
    }

    @RequestMapping(value = "/BackOffice/Holidays/{holidayID}", method = { RequestMethod.GET })
    public ModelAndView GetByID(@PathVariable("holidayID") Long holidayID, HttpServletResponse response) {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        HolidayService holidayService = new HolidayService();
        Holiday holidayItem = holidayService.getByID(holidayID);
        String holidayJson = null;
        try {
            List<String> includes = new ArrayList<String>();
            includes.add("weekdays");
            holidayJson = DeveloperCity.ContactBack.Workflow.RealTimeData.hibernateToJson(holidayItem, includes, null);
        } catch(Exception e) {
            ModelAndView mv = new ModelAndView("TextPlain");
            mv.addObject("content",  e.toString());
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  holidayJson );
        return mv;
    }

    @RequestMapping(value = "/BackOffice/Holidays/{holidayID}", method = { RequestMethod.POST })
    public ModelAndView Save(@PathVariable("holidayID") Long holidayID, HttpServletResponse response) {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        HolidayService holidayService = new HolidayService();
        Holiday holidayItem = null;
        if (holidayID == 0) {
            // new
            holidayItem = new Holiday();
        } else {
            // update
            holidayItem = holidayService.getByID(holidayID);
        }

        Date day = null;
        String name = null;

        try {
            day = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("day"));
            name = request.getParameter("holidayName");
            if (name == null) { throw new Exception("Campo obrigatório: nome do feriado"); }
        } catch(Exception e) {
            log.error(e);
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", "false");
            mv.addObject("errorMessage", e.toString());
            return mv;
        }

        holidayItem.setDay(day);
        holidayItem.setHolidayName(name);

        try { holidayService.setHoliday(holidayItem); }
        catch(Exception e) {
            log.error(e);
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", "false");
            mv.addObject("errorMessage", e.toString());
            return mv;
        }

        ModelAndView mv = new ModelAndView("AjaxResponse");
        mv.addObject("success", "true");
        mv.addObject("errorMessage", "");
        return mv;
    }

    @RequestMapping(value = "/BackOffice/Holidays/Delete/{holidayID}", method = { RequestMethod.POST })
    public ModelAndView Delete(@PathVariable("holidayID") Long holidayID, HttpServletResponse response) {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        HolidayService holidayService = new HolidayService();
        if (holidayID == 0) {
            // new
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", "false");
            mv.addObject("errorMessage", "Não é possível excluir esse item pois ele ainda não foi salvo.");
            return mv;
        }

        try { holidayService.deleteHoliday(holidayID); }
        catch(Exception e) {
            log.error(e);
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", "false");
            mv.addObject("errorMessage", e.toString());
            return mv;
        }

        ModelAndView mv = new ModelAndView("AjaxResponse");
        mv.addObject("success", "true");
        mv.addObject("errorMessage", "");
        return mv;
    }
}