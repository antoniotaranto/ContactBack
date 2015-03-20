package DeveloperCity.ContactBack.Controller;
import DeveloperCity.ContactBack.DomainModel.Agent;
import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.Workflow.AgentService;
import DeveloperCity.ContactBack.Workflow.UItoProcess;
import java.io.IOException;
import java.util.ArrayList;
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
public class CockPitAgentController {
    private static final Logger log = Logger.getLogger(CockPitQueueController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpSession session;

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/CockPit/Agent", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView Get() {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "CockPit/Agent");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        ModelAndView mv = new ModelAndView("CockPitAgent");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);
        return mv;
    }

    @RequestMapping(value = "/CockPit/Agent/List", method = { RequestMethod.POST })
    public ModelAndView List(HttpServletResponse response) throws IOException, Exception {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "CockPit/Agent");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        UItoProcess uiToProcess = new UItoProcess();
        String agents = uiToProcess.reqAgentsJson();

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  agents );
        return mv;
    }

    @RequestMapping(value = "/CockPit/Agent/{userID}", method = { RequestMethod.POST })
    public ModelAndView GetByID(@PathVariable("userID") Long userID, HttpServletResponse response) {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "CockPit/Agent");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }
        
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        AgentService agentService = new AgentService();
        Agent agent = agentService.getByIDIncludeLastWorkTime(userID);
        
        String agentJson = null;
        try {
            List<String> includes = new ArrayList<String>();
            includes.add("workTimes.breaks.breakType");
            agentJson = DeveloperCity.ContactBack.Workflow.RealTimeData.hibernateToJson(agent, includes, null);
        } catch(Exception e) {
            ModelAndView mv = new ModelAndView("TextPlain");
            mv.addObject("content",  e.toString());
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content", agentJson );
        return mv;
    }
}