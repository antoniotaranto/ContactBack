/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.Controller.UserControl;

import DeveloperCity.ContactBack.DomainModel.Module;
import DeveloperCity.ContactBack.DomainModel.Portal;
import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.DomainModel.UserPermission;
import java.util.ArrayList;
import java.util.List;
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
public class MainMenuController {

    private static final Logger log = Logger.getLogger(MainMenuController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/MainMenuControl/{activeModule}", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView Get(@PathVariable("activeModule") String activeModule, HttpServletResponse response) throws Exception {
        return Get(activeModule, null, response);
    }

    @RequestMapping(value = "/MainMenuControl/{activeModule}/{currentAction}", method = {RequestMethod.GET, RequestMethod.POST})
    public ModelAndView Get(@PathVariable("activeModule") String activeModule, @PathVariable("currentAction") String currentAction, HttpServletResponse response) throws Exception {
        User user = (User) request.getSession(true).getAttribute("User");
        if (user == null) {
            throw new Exception("User not logged");
        }
        
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        ModelAndView mv = new ModelAndView("MainMenuControl");
        List<Module> modules = new ArrayList<Module>();
        for (UserPermission userPermission : user.getPermissions()) {
            if (userPermission.getModule().getPortal() == Portal.Agent && activeModule.equals("Agent")) {
                modules.add(userPermission.getModule());
            }
            else if (userPermission.getModule().getPortal() == Portal.BackOffice && activeModule.equals("BackOffice")) {
                modules.add(userPermission.getModule());
            }
            else if (userPermission.getModule().getPortal() == Portal.CockPit && activeModule.equals("CockPit")) {
                modules.add(userPermission.getModule());
            }
        }
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath", rootPath);
        mv.addObject("activeModule", activeModule);
        mv.addObject("currentAction", currentAction == null || currentAction.isEmpty() ? activeModule : activeModule + "/" + currentAction);
        mv.addObject("modules", modules);
        return mv;
    }
}
