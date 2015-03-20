/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Controller;
import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.DomainModel.UserChart;
import DeveloperCity.ContactBack.Workflow.UserChartService;
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
public class CockPitChartController {
    private static final Logger log = Logger.getLogger(CockPitChartController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/CockPit/Chart", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView Get(HttpServletResponse response) {
        User user = (User)request.getSession(true).getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "CockPit/Chart");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        List<UserChart> charts = new UserChartService().getByUser(user);
        if (charts == null || charts.isEmpty()) {
            return new ModelAndView("redirect:/");
        }

        ModelAndView mv = new ModelAndView("CockPitChart");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("charts",  charts);
        mv.addObject("rootPath",  rootPath);
        return mv;
    }
}