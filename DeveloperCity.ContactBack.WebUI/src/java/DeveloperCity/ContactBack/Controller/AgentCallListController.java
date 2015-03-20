/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Controller;
import DeveloperCity.ContactBack.DomainModel.Agent;
import DeveloperCity.ContactBack.DomainModel.Call;
import DeveloperCity.ContactBack.Workflow.CallService;
import DeveloperCity.ContactBack.Workflow.UItoProcess;
import java.util.Calendar;
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
public class AgentCallListController {
    private static final Logger log = Logger.getLogger(AgentCallListController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpSession session;

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/Agent/CallList", method = RequestMethod.GET)
    public ModelAndView Get(HttpServletResponse response) {
        Agent user = (Agent) request.getSession(true).getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        DeveloperCity.ContactBack.Controller.LoginController.AccessType accessType = DeveloperCity.ContactBack.Controller.LoginController.HasAccess(user, "Agent/CallList");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        ModelAndView mv = new ModelAndView("AgentCallList");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);
        return mv;
    }

    @RequestMapping(value = "/Agent/CallList/List/{lastItem}", method = RequestMethod.POST)
    public ModelAndView List(@PathVariable("lastItem") Long lastItem, HttpServletResponse response) {
        Agent user = (Agent)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "Agent/CallList");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        Calendar end = Calendar.getInstance();
        Calendar start = Calendar.getInstance();
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.SECOND, 0);
        start.set(Calendar.MILLISECOND, 0);
        CallService callService = new CallService();
        List<Call> calls = null;
        if (lastItem > 0) {
            calls = callService.getByAgentAndDates(user.getUserID(), start.getTime(), end.getTime(), lastItem);
        } else {
            calls = callService.getByAgentAndDates(user.getUserID(), start.getTime(), end.getTime());
        }
        String callsJson = null;
        try {
            callsJson = DeveloperCity.ContactBack.Workflow.RealTimeData.hibernateToJson(calls, null, null);
        } catch(Exception e) {
            log.error("Erro", e);
            ModelAndView mv = new ModelAndView("TextPlain");
            mv.addObject("content",  e.toString());
            return mv;
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content", callsJson);
        return mv;
    }

    @RequestMapping(value = "/Agent/CallList/Save/{callID}", method = RequestMethod.POST)
    public ModelAndView Save(@PathVariable("callID") Long callID, HttpServletResponse response) throws Exception {
        Agent user = (Agent)session.getAttribute("User");
        ModelAndView mv = new ModelAndView("TextPlain");
        if (user == null) {
            mv.addObject("content", "{ error: \"Não há permissão para executar essa operação. Consulte o administrador do sistema.\" }");
            return mv;
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "Agent/CallList");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            mv.addObject("content", "{ error: \"Não há permissão para executar essa operação. Consulte o administrador do sistema.\" }");
            return mv;
        }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        UItoProcess uiToProcess = new UItoProcess();
        try {
            uiToProcess.setMailBox(callID);
            mv.addObject("content", "{ error: \"\" }");
            return mv;
        } catch(Exception e) {
            mv.addObject("content", String.format("{ error: \"Ocorreu um erro: %s\" }", e.getMessage()));
            return mv;
        }
    }
}
