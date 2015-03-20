package DeveloperCity.ContactBack.Controller;
import DeveloperCity.ContactBack.DomainModel.Customer;
import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.DomainModel.WebQueue;
import DeveloperCity.ContactBack.Workflow.UItoProcess;
import DeveloperCity.ContactBack.Workflow.WebQueueService;
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
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lbarbosa
 */
@Controller
public class CockPitWebQueueController {
    private static final Logger log = Logger.getLogger(CockPitWebQueueController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpSession session;

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    private boolean hasAccess() {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return false;
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "CockPit/WebQueue");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/CockPit/WebQueue", method = { RequestMethod.GET })
    public ModelAndView Get() {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        ModelAndView mv = new ModelAndView("CockPitWebQueue");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);
        mv.addObject("sessionUser", ((User)session.getAttribute("User")).getName() );
        String mobilePhone = "";
        Customer c = session.getAttribute("User") instanceof Customer ? ((Customer)session.getAttribute("User")) : null;
        if (c != null) { mobilePhone = c.getMobilePhone(); }
        mv.addObject("sessionMobilePhone", mobilePhone);
        return mv;
    }
    @RequestMapping(value = "/CockPit/WebQueueMobile", method = { RequestMethod.GET })
    public ModelAndView GetMobile(HttpServletResponse response) {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        Customer c = session.getAttribute("User") instanceof Customer ? ((Customer)session.getAttribute("User")) : null;
        if (c == null) { return new ModelAndView("redirect:/"); }
        WebQueue webQueue = new WebQueueService().getByCustomer(c.getUserID());
        String mobilePhone = c.getMobilePhone();

        ModelAndView mv = new ModelAndView("CockPitWebQueueMobile");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);
        mv.addObject("sessionUser", ((User)session.getAttribute("User")).getName() );
        mv.addObject("sessionMobilePhone", mobilePhone);
        mv.addObject("webQueue", webQueue);
        if (request.getParameter("errorMessage") != null && (!request.getParameter("errorMessage").isEmpty()) ) {
            mv.addObject("errorMessage", request.getParameter("errorMessage"));
        }
        return mv;
    }
    @RequestMapping(value = "/CockPit/WebQueue/Json", method = { RequestMethod.GET })
    public ModelAndView List(HttpServletResponse response) {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        String webQueueJson = null;
        try {
            webQueueJson = DeveloperCity.ContactBack.Workflow.RealTimeData.hibernateToJson(new WebQueueService().getByCustomer(((User)session.getAttribute("User")).getUserID()), null, null);
        } catch(Exception e) {
            ModelAndView mv = new ModelAndView("TextPlain");
            mv.addObject("content",  e.toString());
            return mv;
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  webQueueJson );
        return mv;
    }
    @RequestMapping(value = "/CockPit/WebQueueMobile", method = { RequestMethod.POST })
    public ModelAndView SaveMobile(HttpServletResponse response) {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        try {
            Save(request.getParameter("txtCallbackNumber"));
            return new ModelAndView("redirect:/CockPit/WebQueueMobile");
        } catch(Exception e) {
            log.error(e);
            ModelAndView mv = GetMobile(response);
            mv.addObject("sessionMobilePhone", request.getParameter("txtCallbackNumber"));
            mv.addObject("errorMessage", e.getMessage());
            return mv;
        }
    }
    @RequestMapping(value = "/CockPit/WebQueue", method = { RequestMethod.POST })
    public ModelAndView SaveAjax(HttpServletResponse response) {
        if (!hasAccess()) {
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", "false");
            mv.addObject("errorMessage", "Usuário não possui permissão para executar essa ação.");
            return mv;
        }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");
        try {
            Save(request.getParameter("callbackNumber"));
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", "true");
            mv.addObject("errorMessage", "");
            return mv;
        } catch(Exception e) {
            log.error(e);
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", "false");
            mv.addObject("errorMessage", e.getMessage());
            return mv;
        }
    }
    private void Save(String callbackNumber) throws Exception {
        Customer c = session.getAttribute("User") instanceof Customer ? ((Customer)session.getAttribute("User")) : null;
        if (c == null) {
            throw new Exception("Para criar um agendamento, seu usuário deverá ter perfil de cliente.");
        }
        if (callbackNumber == null || callbackNumber.length() != 10) {
            throw new Exception("O número para retorno não é válido.");
        }
        String REGEX = "\\D";
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(callbackNumber);
        if(matcher.find() || callbackNumber.substring(0, 1).equals("0") || callbackNumber.substring(2, 3).equals("1") || callbackNumber.substring(2, 3).equals("0")) {
            throw new Exception("O número para retorno não é válido.");
        }

        DeveloperCity.ContactBack.Workflow.WebQueueService wqService = new DeveloperCity.ContactBack.Workflow.WebQueueService();
        WebQueue queueItem = wqService.getByCustomer(c.getUserID());
        if (queueItem != null) {
            throw new Exception("Seu usuário já estava agendado para retorno, favor aguardar.");
        }

        UItoProcess uiToProcess = new UItoProcess();
        uiToProcess.webQueueAdd(c.getUserID(), callbackNumber);
    }
}