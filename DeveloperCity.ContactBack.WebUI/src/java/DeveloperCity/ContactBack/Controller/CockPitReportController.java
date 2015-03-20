/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Controller;
import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.DomainModel.UserReport;
import DeveloperCity.ContactBack.Workflow.UserReportService;
import java.util.List;
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
public class CockPitReportController {
    private static final Logger log = Logger.getLogger(CockPitReportController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/CockPit/Report", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView Get(HttpServletResponse response) {
        User user = (User)request.getSession(true).getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "CockPit/Report");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        List<UserReport> reports = new UserReportService().getByUser(user);
        if (reports == null || reports.isEmpty()) {
            return new ModelAndView("redirect:/");
        }

        ModelAndView mv = new ModelAndView("CockPitReport");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("reports",  reports);
        mv.addObject("rootPath",  rootPath);
        return mv;
    }
}