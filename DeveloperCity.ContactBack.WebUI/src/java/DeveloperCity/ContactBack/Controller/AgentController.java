/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Controller;
import DeveloperCity.ContactBack.DomainModel.Portal;
import DeveloperCity.ContactBack.DomainModel.User;
import java.io.IOException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import DeveloperCity.ContactBack.Workflow.UItoProcess;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author lbarbosa
 */
@Controller
public class AgentController {
    private static final Logger log = Logger.getLogger(AgentController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/Agent", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView Get() {
        User user = (User)request.getSession(true).getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        if (!LoginController.HasAccess(user, Portal.Agent)) {
            return new ModelAndView("redirect:/");
        }

        ModelAndView mv = new ModelAndView("Agent");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);
        return mv;
    }

    @RequestMapping(value = "/Agent/QueueCount", method = { RequestMethod.POST })
    public ModelAndView AgentQueueCount(HttpServletResponse response) throws IOException, Exception {
        User user = (User)request.getSession(true).getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        if (!LoginController.HasAccess(user, Portal.Agent)) {
            return new ModelAndView("redirect:/");
        }
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        UItoProcess uiToProcess = new UItoProcess();
        String queueCount = uiToProcess.reqQueueCountJson();

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content", queueCount);
        return mv;
    }
}
