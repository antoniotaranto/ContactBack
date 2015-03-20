/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Controller.UserControl;
import DeveloperCity.ContactBack.DomainModel.Agent;
import DeveloperCity.ContactBack.DomainModel.Break;
import DeveloperCity.ContactBack.DomainModel.WorkTime;
import DeveloperCity.ContactBack.DomainModel.Utils;
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
public class AgentTimeSheetMenuController {
    private static final Logger log = Logger.getLogger(HeaderController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/AgentTimeSheetMenuControl", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView Get(HttpServletResponse response) throws Exception {
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        Agent user = (Agent)request.getSession(true).getAttribute("User");
        if (user == null) {
            throw new Exception("User not logged");
        }

        DeveloperCity.ContactBack.Controller.LoginController.AccessType accessType = DeveloperCity.ContactBack.Controller.LoginController.HasAccess(user, "Agent/TimeSheet");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }
        ModelAndView mv = new ModelAndView("AgentTimeSheetMenu");
        mv.addObject("agent", user);
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);

        WorkTimeService sWorkTime = new WorkTimeService();
        WorkTime currentWorkTime = sWorkTime.getByAgentUnfinished(user);

        if (currentWorkTime != null) {
            // Atualmente logado
            return CurrentlyLogged(mv, currentWorkTime, sWorkTime, user);
        } else {
            // Atualmente deslogado
            return CurrentlyNotLogged(mv, user, currentWorkTime, sWorkTime);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Logged">
    private ModelAndView CurrentlyLogged(ModelAndView mv, WorkTime currentWorkTime, WorkTimeService sWorkTime, Agent agent) {
        sWorkTime.FetchBreaks(currentWorkTime);
        boolean inBreak = false;
        boolean inSystemBreak = false;
        for (Break br : currentWorkTime.getBreaks()) {
            if (br.getBreakEnd() == null) {
                if (br.isSystemBreak()) {
                    inSystemBreak = true;
                }
                else {
                    inBreak = true;
                }
            }
        }
        mv.addObject("inBreak", inBreak);
        mv.addObject("inSystemBreak", inSystemBreak);

        if (inBreak) {
            // Em pausa
            mv.addObject("Status", "Break");
            mv.addObject("message", "Você está atualmente em uma sessão porém em pausa. Para retornar ao atendimento, finalize a pausa na listagem abaixo, ou para encerrar os atendimentos de hoje, finalize a sessão.");
            mv.addObject("currentWorkTime", currentWorkTime);
            return mv;
        }
        else if (inSystemBreak) {
            // Em pausa de sistema
            mv.addObject("Status", "SystemBreak");
            mv.addObject("message", "Você está atualmente em uma sessão porém seu ramal está bloqueado. Para retornar ao atendimento, libere seu ramal para atendimento, ou para encerrar os atendimentos de hoje, finalize a sessão.");
            mv.addObject("currentWorkTime", currentWorkTime);
            return mv;
        }
        else {
            // Ativo
            mv.addObject("Status", "Online");
            mv.addObject("message", "Você está atualmente ativo no sistema. Para entrar em pausa, escolha o tipo de pausa e inicie, ou para encerrar os atendimentos de hoje, finalize a sessão.");
            mv.addObject("requestingBreak", agent.getRequestingBreak());
            mv.addObject("requestingLogoff", agent.getRequestingLogoff());
            mv.addObject("currentWorkTime", currentWorkTime);
            return mv;
        }
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Not logged">
    private ModelAndView CurrentlyNotLogged(ModelAndView mv, Agent agent, WorkTime currentWorkTime, WorkTimeService sWorkTime) {
        currentWorkTime = sWorkTime.getByAgentLastOne(agent);
        if (currentWorkTime == null) {
            // Nunca se logou
            mv.addObject("Status", "NeverLogged");
            mv.addObject("message", "Bem-vindo ao seu <b>primeiro login</b> no atendimento do CAC Danone! Para iniciar o atendimento, basta iniciar uma sessão.");
            return mv;
        }
        else if (Utils.isToday(currentWorkTime.getLoginTime())) {
            // Já se logou hoje
            mv.addObject("Status", "AlreadyLoggedToday");
            sWorkTime.FetchBreaks(currentWorkTime);
            mv.addObject("todayWorkTime", currentWorkTime);
            mv.addObject("message", "A sua sessão de atendimento de hoje já foi encerrada. Porém você poderá reabri-la informando qual o tipo de pausa ocorreu desde o encerramento da sessão até agora.");
            return mv;
        }
        else {
            // Ainda não se logou hoje
            mv.addObject("Status", "NotLoggedToday");
            mv.addObject("message", "Bem-vindo ao sistema de login para atendimento do CAC Danone. Para iniciar o atendimento, basta iniciar uma sessão.");
            return mv;
        }
    }
    // </editor-fold>
}