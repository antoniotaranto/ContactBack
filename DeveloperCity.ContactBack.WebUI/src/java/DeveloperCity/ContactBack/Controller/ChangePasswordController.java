/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.Controller;

import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.Exception.AuthenticationFailException;
import DeveloperCity.ContactBack.Exception.PasswordArgumentException;
import DeveloperCity.ContactBack.Exception.UserNotFoundException;
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
public class ChangePasswordController {

    private static final Logger log = Logger.getLogger(ChangePasswordController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/ChangePassword", method = RequestMethod.GET)
    public ModelAndView Get(HttpServletResponse response) {
        User user = (User) request.getSession(true).getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        ModelAndView mv = new ModelAndView("ChangePassword");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath", rootPath);
        mv.addObject("lastPage", request.getHeader("referer"));
        return mv;
    }

    @RequestMapping(value = "/ChangePassword", method = RequestMethod.POST)
    public ModelAndView Post(HttpServletResponse response) {
        User user = (User) request.getSession(true).getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        String lastPage = request.getParameter("hdnLastPage");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";

        String currentPassword = request.getParameter("txtCurrentPassword");
        String newPassword = request.getParameter("txtNewPassword");
        String confirmPassword = request.getParameter("txtConfirmPassword");
        String errorMessage = "";

        boolean success = true;
        if (currentPassword == null || currentPassword.trim().length() < 5) {
            success = false;
            errorMessage = "A senha atual deve possuir no mínimo 5 caracteres e é obrigatória para a realização da troca de senha.";
        }
        else if (newPassword == null || newPassword.trim().length() < 5) {
            success = false;
            errorMessage = "A nova senha deverá ter no mínimo 5 caracteres.";
        }
        else if (!newPassword.equals(confirmPassword)) {
            success = false;
            errorMessage = "A nova senha informada e sua confirmação estão diferentes.";
        }


        if (success) {
            UserService userService = new UserService();
            try {
                userService.setUserPassword(user.getUserID(), currentPassword, newPassword, confirmPassword);
            } catch (PasswordArgumentException ex) {
                success = false;
                errorMessage = "Parâmetros incorretos foram informados. Detalhes: " + ex.toString();
                log.error(ex);
            } catch (UserNotFoundException ex) {
                success = false;
                errorMessage = "O usuário informado não foi encontrado no banco de dados. Favor entrar em contato com o administrador do sistema.";
                log.error(ex);
            } catch (AuthenticationFailException ex) {
                success = false;
                errorMessage = "A senha atual está incorreta, por favor verifique os dados informados ou entre em contato com o administrador do sistema.";
                log.error(ex);
            } catch (Exception ex) {
                success = false;
                errorMessage = "Erro ao salvar a nova senha. Detalhes: " + ex.toString();
                log.error(ex);
            }
        }

        if (success) {
            if (lastPage == null || lastPage.isEmpty() || lastPage.contains("ChangePassword")) {
                lastPage = "/";
            }
            ModelAndView mv = new ModelAndView("redirect:" + lastPage);
            return mv;
        }
        else {
            ModelAndView mv = new ModelAndView("ChangePassword");
            mv.addObject("rootPath", rootPath);
            mv.addObject("errorMessage", errorMessage);
            mv.addObject("lastPage", lastPage);
            return mv;
        }
    }
}
