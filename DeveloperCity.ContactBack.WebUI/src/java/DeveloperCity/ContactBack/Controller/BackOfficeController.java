/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Controller;
import DeveloperCity.ContactBack.DomainModel.Portal;
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
public class BackOfficeController {
    private static final Logger log = Logger.getLogger(BackOfficeController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/BackOffice", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView Get(HttpServletResponse response) {
        User user = (User)request.getSession(true).getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        if (!LoginController.HasAccess(user, Portal.BackOffice)) {
            return new ModelAndView("redirect:/");
        }

        ModelAndView mv = new ModelAndView("BackOffice");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);
        return mv;
    }
}
