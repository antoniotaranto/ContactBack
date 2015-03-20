package DeveloperCity.ContactBack.Controller;
import DeveloperCity.ContactBack.DomainModel.Queue;
import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.Workflow.QueueService;
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
public class CockPitQueueController {
    private static final Logger log = Logger.getLogger(CockPitQueueController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpSession session;

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/CockPit/Queue", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView Get() {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "CockPit/Queue");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        ModelAndView mv = new ModelAndView("CockPitQueue");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);
        return mv;
    }

    @RequestMapping(value = "/CockPit/Queue/List", method = { RequestMethod.POST })
    public ModelAndView List(HttpServletResponse response) {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "CockPit/Queue");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        QueueService queueService = new QueueService();
        List<Queue> queue = queueService.getInQueueAndCurrentCall();
        java.util.Collections.sort(queue);
        String queueJson = null;
        try {
            List<String> excludes = new ArrayList<String>();
            excludes.add("attendCall.agent.password");
            queueJson = DeveloperCity.ContactBack.Workflow.RealTimeData.hibernateToJson(queue, null, excludes);
        } catch(Exception e) {
            ModelAndView mv = new ModelAndView("TextPlain");
            mv.addObject("content",  e.toString());
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  queueJson );
        return mv;
    }

    @RequestMapping(value = "/CockPit/Queue/{queueID}", method = { RequestMethod.POST })
    public ModelAndView GetByID(@PathVariable("queueID") Long queueID, HttpServletResponse response) {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "CockPit/Queue");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        QueueService queueService = new QueueService();
        Queue queueItem = queueService.getByIDIncludeDetails(queueID);
        String queueJson = null;
        try {
            List<String> excludes = new ArrayList<String>();
            List<String> includes = new ArrayList<String>();
            excludes.add("attendCall.agent.password");
            includes.add("calls");
            includes.add("history");
            queueJson = DeveloperCity.ContactBack.Workflow.RealTimeData.hibernateToJson(queueItem, includes, excludes);
        } catch(Exception e) {
            ModelAndView mv = new ModelAndView("TextPlain");
            mv.addObject("content",  e.toString());
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  queueJson );
        return mv;
    }
}