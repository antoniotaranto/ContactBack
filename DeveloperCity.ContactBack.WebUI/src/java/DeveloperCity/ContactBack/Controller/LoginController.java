/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Controller;
import DeveloperCity.ContactBack.DomainModel.Customer;
import DeveloperCity.ContactBack.DomainModel.Portal;
import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.DomainModel.UserPermission;
import DeveloperCity.ContactBack.Workflow.UserService;
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
public class LoginController {
    private static final Logger log = Logger.getLogger(LoginController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/Login", method = RequestMethod.POST)
    public ModelAndView Post(HttpServletResponse response) {
        ModelAndView mv = null;
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");
        
        if (request.getParameter("txtLogin") == null || request.getParameter("txtLogin").isEmpty() || request.getParameter("txtPassword") == null || request.getParameter("txtPassword").isEmpty()) {
            mv = Get(response);
            String message = "";
            if (request.getParameter("txtLogin") == null || request.getParameter("txtLogin").isEmpty()) {
                message = "O campo login é obrigatório.";
            }
            if (request.getParameter("txtPassword") == null || request.getParameter("txtPassword").isEmpty()) {
                message = message.isEmpty() ? "O campo Password é obrigatório." : message + "<br />O campo Password é obrigatório.";
            }
            mv.addObject("errorMessage", message);
            return mv;
        }
        UserService userService = new UserService();
        User user = userService.getByAuthentication(request.getParameter("txtLogin"), request.getParameter("txtPassword"), true);
        if (user == null) {
            mv = Get(response);
            mv.addObject("errorMessage", "Usuário ou senha inválidos. Por favor verifique os dados e tente novamente.");
            return mv;
        }
        if (user.getPermissions().isEmpty()) {
            mv = Get(response);
            mv.addObject("errorMessage", "Usuário autenticado porém não possui acesso a nenhum módulo do portal, favor entrar em contato com o administrador do sistema.");
            return mv;
        }

        request.getSession().setAttribute("User", user);
        return new ModelAndView("redirect:/");

    }
    @RequestMapping(value = "/Login", method = RequestMethod.GET )
    public ModelAndView Get(HttpServletResponse response) {
        if (request.getSession(true).getAttribute("User") != null) {
            request.getSession().setAttribute("User", null);
        }
        ModelAndView mv = new ModelAndView("Login");
        return mv;
    }

    @RequestMapping(value = "/Mobile", method = RequestMethod.POST)
    public ModelAndView PostMobile(HttpServletResponse response) {
        ModelAndView mv = null;
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        if (request.getParameter("txtLogin") == null || request.getParameter("txtLogin").isEmpty() || request.getParameter("txtPassword") == null || request.getParameter("txtPassword").isEmpty()) {
            mv = GetMobile(response);
            String message = "";
            if (request.getParameter("txtLogin") == null || request.getParameter("txtLogin").isEmpty()) {
                message = "O campo login é obrigatório.";
            }
            if (request.getParameter("txtPassword") == null || request.getParameter("txtPassword").isEmpty()) {
                message = message.isEmpty() ? "O campo Password é obrigatório." : message + "<br />O campo Password é obrigatório.";
            }
            mv.addObject("errorMessage", message);
            return mv;
        }
        UserService userService = new UserService();
        User user = userService.getByAuthentication(request.getParameter("txtLogin"), request.getParameter("txtPassword"), true);
        if (user == null) {
            mv = GetMobile(response);
            mv.addObject("errorMessage", "Usuário ou senha inválidos. Por favor verifique os dados e tente novamente.");
            return mv;
        }
        if (user.getPermissions().isEmpty()) {
            mv = GetMobile(response);
            mv.addObject("errorMessage", "Usuário autenticado porém não possui acesso a nenhum módulo do portal, favor entrar em contato com o administrador do sistema.");
            return mv;
        }

        if (! (user instanceof Customer)) {
            mv = GetMobile(response);
            mv.addObject("errorMessage", "Apenas usuário do tipo \"Cliente\" pode realizar autenticação em dispositivos móveis.");
            return mv;
        }
        request.getSession().setAttribute("User", user);
        return new ModelAndView("redirect:/CockPit/WebQueueMobile");
    }
    @RequestMapping(value = "/Mobile", method = RequestMethod.GET )
    public ModelAndView GetMobile(HttpServletResponse response) {
        if (request.getSession(true).getAttribute("User") != null) {
            request.getSession().setAttribute("User", null);
        }
        ModelAndView mv = new ModelAndView("LoginMobile");
        return mv;
    }

    public static AccessType HasAccess(User user, String servlet) {
        for(UserPermission userPermission : user.getPermissions()) {
            if (userPermission.getModule().getServlet().equals(servlet)) {
                if (userPermission.getReadWritePermission().equals("rw")) {
                    return AccessType.Full;
                }
                else if (userPermission.getReadWritePermission().equals("r")) {
                    return AccessType.Read;
                }
            }
        }
        return AccessType.None;
    }
    public static boolean HasAccess(User user, Portal portal) {
        for(UserPermission userPermission : user.getPermissions()) {
            if (userPermission.getModule().getPortal() == portal) {
                return true;
            }
        }
        return false;
    }
    public enum AccessType {
        None, Read, Full
    }
}
