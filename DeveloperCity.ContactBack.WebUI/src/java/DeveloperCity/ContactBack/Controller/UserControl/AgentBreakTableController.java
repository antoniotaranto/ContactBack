/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Controller.UserControl;
import DeveloperCity.ContactBack.DomainModel.Agent;
import DeveloperCity.ContactBack.DomainModel.Utils;
import DeveloperCity.ContactBack.DomainModel.WorkTime;
import DeveloperCity.ContactBack.Workflow.WorkTimeService;
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
public class AgentBreakTableController {
    private static final Logger log = Logger.getLogger(AgentBreakTableController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/AgentBreakTableControl", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView Get(HttpServletResponse response) throws Exception {
        Agent user = (Agent) request.getSession(true).getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        DeveloperCity.ContactBack.Controller.LoginController.AccessType accessType = DeveloperCity.ContactBack.Controller.LoginController.HasAccess(user, "Agent/TimeSheet");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        ModelAndView mv = new ModelAndView("AgentBreakTableControl");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);

        WorkTimeService sWorkTime = new WorkTimeService();
        WorkTime currentWorkTime = sWorkTime.getByAgentUnfinished(user);

        if (currentWorkTime == null) {
            currentWorkTime = sWorkTime.getByAgentLastOne(user);
            if (currentWorkTime == null || !Utils.isToday(currentWorkTime.getLoginTime()) ) {
                return mv;
            }
        }

        sWorkTime.FetchBreaks(currentWorkTime);
        for(DeveloperCity.ContactBack.DomainModel.Break b : currentWorkTime.getBreaks()) {
            log.info(b);
        }
        mv.addObject("currentWorkTime", currentWorkTime);
        mv.addObject("rowCount", currentWorkTime.getBreaks().size());
        return mv;
    }
}
