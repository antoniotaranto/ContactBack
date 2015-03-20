/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Controller.UserControl;
import DeveloperCity.ContactBack.DomainModel.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lbarbosa
 */
@Controller
public class HeaderController {
    private static final Logger log = Logger.getLogger(HeaderController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/HeaderControl/{activeModule}", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView Get(@PathVariable("activeModule") String activeModule, HttpServletResponse response) throws Exception {
        User user = (User)request.getSession(true).getAttribute("User");
        if (user == null) {
            throw new Exception("User not logged");
        }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        ModelAndView mv = new ModelAndView("HeaderControl");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);
        mv.addObject("user", user);
        mv.addObject("activeModule", activeModule);
        return mv;
    }
}
