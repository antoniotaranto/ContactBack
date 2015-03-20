/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Controller;
import DeveloperCity.ContactBack.DomainModel.Portal;
import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.Workflow.QueueService;
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
public class CockPitController {
    private static final Logger log = Logger.getLogger(CockPitController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/CockPit", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView Get(HttpServletResponse response) {
        User user = (User)request.getSession(true).getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        if (!LoginController.HasAccess(user, Portal.CockPit)) {
            return new ModelAndView("redirect:/");
        }

        QueueService queueService = new QueueService();
        int inQueue = queueService.countInQueue();

        ModelAndView mv = new ModelAndView("CockPit");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);
        mv.addObject("inQueue", inQueue);
        return mv;
    }
    @RequestMapping(value = "/CockPit/QueueCount", method = { RequestMethod.POST })
    public ModelAndView Post(HttpServletResponse response) throws IOException, Exception {
        User user = (User)request.getSession(true).getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        if (!LoginController.HasAccess(user, Portal.CockPit)) {
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
