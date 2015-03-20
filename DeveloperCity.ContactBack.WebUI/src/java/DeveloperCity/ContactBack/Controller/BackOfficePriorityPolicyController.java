package DeveloperCity.ContactBack.Controller;
import DeveloperCity.ContactBack.DomainModel.MatchMode;
import DeveloperCity.ContactBack.DomainModel.Priority;
import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.DomainModel.Weekdays;
import DeveloperCity.ContactBack.Workflow.PriorityService;
import DeveloperCity.ContactBack.Workflow.UItoProcess;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public class BackOfficePriorityPolicyController {
    private static final Logger log = Logger.getLogger(BackOfficePriorityPolicyController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpSession session;

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    private boolean hasAccess() {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return false;
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/PriorityPolicy");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/BackOffice/PriorityPolicy", method = { RequestMethod.GET })
    public ModelAndView Get() {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        ModelAndView mv = new ModelAndView("BackOfficePriorityPolicy");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);
        return mv;
    }

    @RequestMapping(value = "/BackOffice/PriorityPolicy/List", method = { RequestMethod.GET })
    public ModelAndView List(HttpServletResponse response) {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        PriorityService priorityService = new PriorityService();
        List<Priority> priority = priorityService.getAll();

        String priorityJson = null;
        try {
            List<String> includes = new ArrayList<String>();
            includes.add("weekdays");
            priorityJson = DeveloperCity.ContactBack.Workflow.RealTimeData.hibernateToJson(priority, includes, null);
        } catch(Exception e) {
            ModelAndView mv = new ModelAndView("TextPlain");
            mv.addObject("content",  e.toString());
            return mv;
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  priorityJson );
        return mv;
    }

    @RequestMapping(value = "/BackOffice/PriorityPolicy/List", method = { RequestMethod.POST })
    public ModelAndView Refresh(HttpServletResponse response) {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        UItoProcess uiToProcess = new UItoProcess();
        try {
            boolean success = uiToProcess.reloadPriorityPolicy();
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

    @RequestMapping(value = "/BackOffice/PriorityPolicy/{priorityID}", method = { RequestMethod.GET })
    public ModelAndView GetByID(@PathVariable("priorityID") Long priorityID, HttpServletResponse response) {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        PriorityService priorityService = new PriorityService();
        Priority priorityItem = priorityService.getByID(priorityID);
        String priorityJson = null;
        try {
            List<String> includes = new ArrayList<String>();
            includes.add("weekdays");
            priorityJson = DeveloperCity.ContactBack.Workflow.RealTimeData.hibernateToJson(priorityItem, includes, null);
        } catch(Exception e) {
            ModelAndView mv = new ModelAndView("TextPlain");
            mv.addObject("content",  e.toString());
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  priorityJson );
        return mv;
    }

    @RequestMapping(value = "/BackOffice/PriorityPolicy/{priorityID}", method = { RequestMethod.POST })
    public ModelAndView Save(@PathVariable("priorityID") Long priorityID, HttpServletResponse response) {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        PriorityService priorityService = new PriorityService();
        Priority priorityItem = null;
        if (priorityID == 0) {
            // new
            priorityItem = new Priority();
        } else {
            // update
            priorityItem = priorityService.getByID(priorityID);
        }

        Date startTime = null;
        Date endTime = null;
        String number = null;
        MatchMode matchMode = null;
        short priorityValue = 0;
        boolean sunday = false;
        boolean monday = false;
        boolean tuesday = false;
        boolean wednesday = false;
        boolean thurday = false;
        boolean friday = false;
        boolean saturday = false;
        List<Weekdays> weekdays = new ArrayList<Weekdays>();

        try {
            startTime = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/2000 " + request.getParameter("startTime"));
            endTime = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse("01/01/2000 " + request.getParameter("endTime"));
            number = request.getParameter("number");
            String REGEX = "\\D";
            Pattern pattern = Pattern.compile(REGEX);
            Matcher matcher = pattern.matcher(number);
            if(matcher.find()) {
                throw new Exception("Invalid parameter 'number'");
            }
            matchMode = MatchMode.valueOf(request.getParameter("matchMode"));
            priorityValue = Short.parseShort(request.getParameter("priorityValue"));
            sunday = Boolean.parseBoolean(request.getParameter("sunday"));
            monday = Boolean.parseBoolean(request.getParameter("monday"));
            tuesday = Boolean.parseBoolean(request.getParameter("tuesday"));
            wednesday = Boolean.parseBoolean(request.getParameter("wednesday"));
            thurday = Boolean.parseBoolean(request.getParameter("thurday"));
            friday = Boolean.parseBoolean(request.getParameter("friday"));
            saturday = Boolean.parseBoolean(request.getParameter("saturday"));
            if (sunday) { weekdays.add(Weekdays.Sunday); }
            if (monday) { weekdays.add(Weekdays.Monday); }
            if (tuesday) { weekdays.add(Weekdays.Tuesday); }
            if (wednesday) { weekdays.add(Weekdays.Wednesday); }
            if (thurday) { weekdays.add(Weekdays.Thurday); }
            if (friday) { weekdays.add(Weekdays.Friday); }
            if (saturday) { weekdays.add(Weekdays.Saturday); }
        } catch(Exception e) {
            log.error(e);
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", "false");
            mv.addObject("errorMessage", e.toString());
            return mv;
        }

        priorityItem.setStartTime(startTime);
        priorityItem.setEndTime(endTime);
        priorityItem.setNumber(number);
        priorityItem.setMatchMode(matchMode);
        priorityItem.setPriorityValue(priorityValue);
        priorityItem.setWeekdays(weekdays);

        try { priorityService.setPriority(priorityItem); }
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

    @RequestMapping(value = "/BackOffice/PriorityPolicy/Delete/{priorityID}", method = { RequestMethod.POST })
    public ModelAndView Delete(@PathVariable("priorityID") Long priorityID, HttpServletResponse response) {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        PriorityService priorityService = new PriorityService();
        if (priorityID == 0) {
            // new
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", "false");
            mv.addObject("errorMessage", "Não é possível excluir esse item pois ele ainda não foi salvo.");
            return mv;
        }

        try { priorityService.deletePriority(priorityID); }
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