/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Controller;
import DeveloperCity.ContactBack.DomainModel.Agent;
import DeveloperCity.ContactBack.Workflow.UItoProcess;
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
public class AgentTimeSheetController {
    private static final Logger log = Logger.getLogger(AgentTimeSheetController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/Agent/TimeSheet", method = RequestMethod.GET)
    public ModelAndView Get(HttpServletResponse response) {
        Agent user = (Agent) request.getSession(true).getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        DeveloperCity.ContactBack.Controller.LoginController.AccessType accessType = DeveloperCity.ContactBack.Controller.LoginController.HasAccess(user, "Agent/TimeSheet");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        ModelAndView mv = new ModelAndView("AgentTimeSheet");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);
        return mv;
    }

    @RequestMapping(value = "/Agent/TimeSheet", method = RequestMethod.POST)
    public ModelAndView Post(HttpServletResponse response) {
        String command = null;
        long breakID = 0;
        long breakTypeID = 0;
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        try {
            command = request.getParameter("command");
            if (request.getParameter("breakID") != null && request.getParameter("breakID").trim().length() > 0) {
                breakID = Long.valueOf(request.getParameter("breakID"));
            }
            if (request.getParameter("breakTypeID") != null && request.getParameter("breakTypeID").trim().length() > 0) {
                breakTypeID = Long.valueOf(request.getParameter("breakTypeID"));
            }
        } catch(Exception e) {}

        if (command == null) {
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", "false");
            mv.addObject("errorMessage", "Comando não reconhecido");
            return mv;
        }

        Agent user = (Agent) request.getSession(true).getAttribute("User");
        if (user == null) {
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", "false");
            mv.addObject("errorMessage", "Usuário não autenticado, faça novamente o seu login");
            return mv;
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "Agent/TimeSheet");
        if (accessType == LoginController.AccessType.None) {
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", "false");
            mv.addObject("errorMessage", "Usuário não possui permissão para realizar a tarefa solicitada");
            return mv;
        }

        UItoProcess uiToProcess = new UItoProcess();
        try {
            boolean success = false;
            if (command.equals("startBreak")) {
                if (breakTypeID > 0) {
                    success = uiToProcess.askBreakStart(user.getUserID(), breakTypeID);
                    if (success) {
                        user.setRequestingBreak(breakTypeID);
                        request.getSession(true).setAttribute("User", user);
                    }
                }
            }
            else if (command.equals("endBreak")) {
                if (breakID > 0) {
                    success = uiToProcess.askBreakEnd(user.getUserID(), breakID);
                    if (success) {
                        user.setRequestingBreak(0);
                        user.setRequestingLogoff(false);
                        request.getSession(true).setAttribute("User", user);
                    }
                }
            }
            else if (command.equals("restartSession")) {
                success = uiToProcess.askSessionRestart(user.getUserID(), breakTypeID);
                if (success) {
                    user.setRequestingBreak(0);
                    user.setRequestingLogoff(false);
                    request.getSession(true).setAttribute("User", user);
                }
            }
            else if (command.equals("startSession")) {
                success = uiToProcess.askSessionStart(user.getUserID());
                if (success) {
                    user.setRequestingBreak(0);
                    user.setRequestingLogoff(false);
                    request.getSession(true).setAttribute("User", user);
                }
            }
            else if (command.equals("endSession")) {
                success = uiToProcess.askSessionEnd(user.getUserID());
                if (success) {
                    user.setRequestingLogoff(true);
                    request.getSession(true).setAttribute("User", user);
                }
            }
            else if (command.equals("keepAlive")) {
                ModelAndView mv = new ModelAndView("AjaxResponse");
                mv.addObject("success", "true");
                mv.addObject("errorMessage", "");
                return mv;
            }

            if (success) {
                ModelAndView mv = new ModelAndView("AjaxResponse");
                mv.addObject("success", "true");
                mv.addObject("errorMessage", "");
                return mv;
            }
            else {
                ModelAndView mv = new ModelAndView("AjaxResponse");
                mv.addObject("success", "false");
                mv.addObject("errorMessage", "O servidor não pôde executar sua solicitação. <br />\r\nPor favor, verifique se o IP Communicator está ativo e se o seu usuário está com o status correto no sistema.");
                return mv;
            }
        } catch (Exception e) {
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", "false");
            mv.addObject("errorMessage", e.toString());
            return mv;
        }
    }
}
