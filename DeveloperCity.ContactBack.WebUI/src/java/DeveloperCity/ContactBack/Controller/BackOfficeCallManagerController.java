package DeveloperCity.ContactBack.Controller;

import DeveloperCity.ContactBack.DomainModel.DestinationType;
import DeveloperCity.ContactBack.DomainModel.CallManager;
import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.Workflow.CallManagerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BackOfficeCallManagerController {
    private static final Logger log = Logger.getLogger(BackOfficeCallManagerController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpSession session;

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    private boolean hasAccess() {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return false;
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/CallManager");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/BackOffice/CallManager", method = { RequestMethod.GET })
    public ModelAndView Get() {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        ModelAndView mv = new ModelAndView("BackOfficeCallManager");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);
        return mv;
    }

    @RequestMapping(value = "/BackOffice/CallManager/Json", method = { RequestMethod.GET })
    public ModelAndView List(HttpServletResponse response) {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        String callManagerJson = null;
        try {
            callManagerJson = DeveloperCity.ContactBack.Workflow.RealTimeData.hibernateToJson(CallManagerService.getFirst(), null, null);
        } catch(Exception e) {
            ModelAndView mv = new ModelAndView("TextPlain");
            mv.addObject("content",  e.toString());
            return mv;
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  callManagerJson );
        return mv;
    }

    @RequestMapping(value = "/BackOffice/CallManager/Save", method = { RequestMethod.POST })
    public ModelAndView Save(HttpServletResponse response) {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");
        String jtapiUsername = request.getParameter("jtapiUsername");
        String jtapiPassword = request.getParameter("jtapiPassword");
        String ip = request.getParameter("ip");

        try {
            CallManager ccm = CallManagerService.getFirst();
            ccm.setJtapiUsername(jtapiUsername);
            ccm.setJtapiPassword(jtapiPassword);
            ccm.setIP(ip);

            CallManagerService.setCallManagerStatic(ccm);

            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", "true");
            mv.addObject("errorMessage", "");
            return mv;
        } catch(Exception e) {
            log.error(e);
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", "false");
            mv.addObject("errorMessage", e.toString());
            return mv;
        }
    }
}