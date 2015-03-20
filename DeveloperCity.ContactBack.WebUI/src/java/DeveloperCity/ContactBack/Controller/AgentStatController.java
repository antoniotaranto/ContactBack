/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Controller;
import DeveloperCity.ContactBack.DomainModel.Agent;
import DeveloperCity.ContactBack.Workflow.UItoProcess;
import java.io.IOException;
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
public class AgentStatController {
    private static final Logger log = Logger.getLogger(AgentStatController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/Agent/Stat", method = RequestMethod.GET)
    public ModelAndView Get(HttpServletResponse response) {
        Agent user = (Agent) request.getSession(true).getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        DeveloperCity.ContactBack.Controller.LoginController.AccessType accessType = DeveloperCity.ContactBack.Controller.LoginController.HasAccess(user, "Agent/Stat");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        ModelAndView mv = new ModelAndView("AgentStat");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);

        return mv;
    }

    @RequestMapping(value = "/Agent/Stat/RealTime", method = RequestMethod.POST)
    public ModelAndView Post(HttpServletResponse response) throws IOException, Exception {
        Agent user = (Agent) request.getSession(true).getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        DeveloperCity.ContactBack.Controller.LoginController.AccessType accessType = DeveloperCity.ContactBack.Controller.LoginController.HasAccess(user, "Agent/Stat");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }
        
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        UItoProcess uiToProcess = new UItoProcess();
        String agent = uiToProcess.reqAgentJson(user.getUserID());

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content", agent);
        return mv;
    }
}
