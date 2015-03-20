/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Controller;
import DeveloperCity.ContactBack.DomainModel.User;
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
public class MainController {
    private static final Logger log = Logger.getLogger(MainController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView Get(HttpServletResponse response) {
        if (request.getSession(true).getAttribute("User") == null || ((User)request.getSession(true).getAttribute("User")) == null) {
            return new ModelAndView("redirect:/Login");
        }
        User loggedUser = (User)request.getSession(true).getAttribute("User");
        if ((loggedUser.hasBackOfficeAccess() || loggedUser.hasCockPitAccess() || loggedUser.hasAgentCockPitAccess()) == false) {
            return new ModelAndView("redirect:/Login");
        }
        if (loggedUser.hasBackOfficeAccess() && (!loggedUser.hasCockPitAccess()) && (!loggedUser.hasAgentCockPitAccess())) {
            return new ModelAndView("redirect:/BackOffice");
        }
        if ((!loggedUser.hasBackOfficeAccess()) && loggedUser.hasCockPitAccess() && (!loggedUser.hasAgentCockPitAccess())) {
            return new ModelAndView("redirect:/CockPit");
        }
        if ((!loggedUser.hasBackOfficeAccess()) && (!loggedUser.hasCockPitAccess()) && loggedUser.hasAgentCockPitAccess()) {
            return new ModelAndView("redirect:/Agent");
        }
        ModelAndView mv = new ModelAndView("Home");

        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);
        mv.addObject("hasBackOfficeAccess", loggedUser.hasBackOfficeAccess());
        mv.addObject("hasCockPitAccess", loggedUser.hasCockPitAccess());
        mv.addObject("hasAgentCockPitAccess", loggedUser.hasAgentCockPitAccess());
        return mv;
    }
}
