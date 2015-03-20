package DeveloperCity.ContactBack.Controller;
import DeveloperCity.ContactBack.DomainModel.Agent;
import DeveloperCity.ContactBack.DomainModel.AgentStatus;
import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.DomainModel.UserChart;
import DeveloperCity.ContactBack.DomainModel.UserPermission;
import DeveloperCity.ContactBack.DomainModel.UserReport;
import DeveloperCity.ContactBack.DomainModel.UserStatus;
import DeveloperCity.ContactBack.Exception.AgentInUseException;
import DeveloperCity.ContactBack.Exception.AuthenticationFailException;
import DeveloperCity.ContactBack.Exception.InvalidDirectoryNumberException;
import DeveloperCity.ContactBack.Exception.InvalidTerminalException;
import DeveloperCity.ContactBack.Exception.PasswordArgumentException;
import DeveloperCity.ContactBack.Exception.UserNotFoundException;
import DeveloperCity.ContactBack.Workflow.UItoProcess;
import DeveloperCity.ContactBack.Workflow.UserChartService;
import DeveloperCity.ContactBack.Workflow.UserPermissionService;
import DeveloperCity.ContactBack.Workflow.UserReportService;
import DeveloperCity.ContactBack.Workflow.UserService;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lbarbosa
 */
@Controller
public class BackOfficeUsersController {
    private static final Logger log = Logger.getLogger(BackOfficeUsersController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpSession session;

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/BackOffice/Users", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView Get(HttpServletResponse response) {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Users");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        ModelAndView mv = new ModelAndView("BackOfficeUsers");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);
        return mv;
    }
    @RequestMapping(value = "/BackOffice/Users/List", method = { RequestMethod.POST })
    public ModelAndView List(HttpServletResponse response) {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Users");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        UserService userService = new UserService();
        List<User> users = userService.getNotCustomer();
        java.util.Collections.sort(users, new Comparator<User>() {
            public int compare(User o1, User o2) {
                if (o1 == null) {
                    return 1;
                } else if (o2 == null) {
                    return -1;
                } else {
                    return o1.getUsername().compareTo(o2.getUsername());
                }
            }
        });
        String usersJson = null;
        try {
            List<String> excludes = new ArrayList<String>();
            excludes.add("*.password");
            usersJson = DeveloperCity.ContactBack.Workflow.RealTimeData.hibernateToJson(users, null, excludes);
        } catch(Exception e) {
            ModelAndView mv = new ModelAndView("TextPlain");
            mv.addObject("content",  e.toString());
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  usersJson.replace("\"class\"", "\"type\"") );
        return mv;
    }
    @RequestMapping(value = "/BackOffice/Users/{userID}", method = { RequestMethod.GET })
    public ModelAndView GetByID(@PathVariable("userID") Long userID, HttpServletResponse response) {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Users");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        UserService userService = new UserService();
        User userEdit = userService.getByID(userID);

        String userJson = null;
        try {
            List<String> excludes = new ArrayList<String>();
            excludes.add("*.password");
            userJson = DeveloperCity.ContactBack.Workflow.RealTimeData.hibernateToJson(userEdit, null, excludes);
        } catch(Exception e) {
            ModelAndView mv = new ModelAndView("TextPlain");
            mv.addObject("content", e.toString());
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  userJson.replace("\"class\"", "\"type\"") );
        return mv;
    }
    @RequestMapping(value = "/BackOffice/Users/{userID}", method = { RequestMethod.POST })
    public ModelAndView Save(@PathVariable("userID") Long userID, HttpServletResponse response) throws UnsupportedEncodingException {
        // <editor-fold defaultstate="collapsed" desc="Access Permission">
        User user = (User) session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Users");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        request.setCharacterEncoding("ISO-8859-1");
        response.setHeader("Expires", "Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis());
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache,must-revalidate,max-age=0");
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Read POST parameters">
        String username = request.getParameter("username");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String pbirth = request.getParameter("birthday");
        Date birthday = null;
        
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");

        if (pbirth != null && pbirth.length() > 0) {
            try {
                birthday = dateFormatter.parse(pbirth);
            } catch (Exception e) {
                birthday = null;
            }
        }
        String userStatus = request.getParameter("userStatus");
        boolean isAgent = request.getParameter("isAgent").equals("true");
        String directoryNumber = request.getParameter("directoryNumber");
        String terminal = request.getParameter("terminal");
        String selectedModules = request.getParameter("selectedModules");
        String selectedReports = request.getParameter("selectedReports");
        String selectedCharts = request.getParameter("selectedCharts");
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Read agent from database">
        UserService userService = new UserService();
        String defaultPassword = null;
        try { defaultPassword = new DeveloperCity.ContactBack.Workflow.SetupService().getSetup().getDefaultPassword();
        } catch(Exception e) { defaultPassword = "dc123"; }
        User userEdit = null;
        if (userID == 0) {
            if (isAgent) {
                userEdit = new Agent();
            }
            else {
                userEdit = new User();
            }
            userEdit.setPassword(DeveloperCity.Security.Crypto.EncryptForever(defaultPassword));
        }
        else {
            userEdit = userService.getByID(userID);
            if (userEdit instanceof Agent && ((Agent) userEdit).getAgentStatus() != AgentStatus.NotLogged) {
                ModelAndView mv = new ModelAndView("AjaxResponse");
                mv.addObject("success", "false");
                mv.addObject("errorMessage", "Para alterar um agente, ele precisa estar offline.");
                return mv;
            }
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Change properties">
        if (userEdit.getUsername() == null || (!userEdit.getUsername().equals( username ))) {
            // Verify duplicity
            User verify = userService.getByUsername(username, true);
            if (verify != null && verify.getUserID() != userID) {
                ModelAndView mv = new ModelAndView("AjaxResponse");
                mv.addObject("success", "false");
                mv.addObject("errorMessage", "O nome-de-usuário escolhido já está em uso. Favor escolher outro.");
                return mv;
            }
            userEdit.setUsername(username);
        }
        userEdit.setName(name);
        userEdit.setBirthday(birthday);
        userEdit.setEmail(email);
        userEdit.setUserStatus(UserStatus.valueOf(userStatus));
        if (userEdit instanceof Agent) {
            Agent agentEdit = (Agent)userEdit;
            agentEdit.setDirectoryNumber(directoryNumber);
            agentEdit.setTerminal(terminal);
            agentEdit.setAgentStatus(AgentStatus.NotLogged);
            userEdit = agentEdit;
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Save">
        if (userEdit instanceof Agent) {
            // <editor-fold defaultstate="collapsed" desc="Save through console application (Server Agent)">
            try {
                UItoProcess uiToProcess = new UItoProcess();
                uiToProcess.saveAgent((Agent)userEdit);
            } catch(InvalidTerminalException e) {
                ModelAndView mv = new ModelAndView("AjaxResponse");
                mv.addObject("success", "false");
                mv.addObject("errorMessage", "O telefone indicado está em uso.");
                return mv;
            } catch(InvalidDirectoryNumberException e) {
                ModelAndView mv = new ModelAndView("AjaxResponse");
                mv.addObject("success", "false");
                mv.addObject("errorMessage", "O telefone indicado está incorreto.");
                return mv;
            } catch(AgentInUseException e) {
                ModelAndView mv = new ModelAndView("AjaxResponse");
                mv.addObject("success", "false");
                mv.addObject("errorMessage", "O agente está incorreto ou em atendimento. Solicite logoff.");
                return mv;
            } catch(Exception e) {
                log.error(e);
                ModelAndView mv = new ModelAndView("AjaxResponse");
                mv.addObject("success", "false");
                mv.addObject("errorMessage", "Ocorreu um erro ao salvar o agente.");
                return mv;
            }
            // </editor-fold>
        } else {
            // <editor-fold defaultstate="collapsed" desc="Save through database">
            try {
                userEdit = userService.setUser(userEdit);
            } catch(Exception e) {
                log.error(e);
                ModelAndView mv = new ModelAndView("AjaxResponse");
                mv.addObject("success", "false");
                mv.addObject("errorMessage", "Ocorreu um erro ao salvar o usuário.");
                return mv;
            }
            // </editor-fold>
        }
        if (selectedModules != null && !selectedModules.isEmpty()) {
            String[] modString = selectedModules.split("\\|");
            Long[] modules = new Long[modString.length];
            for (int i = 0; i < modString.length; i++) { modules[i] = Long.parseLong(modString[i]); }
            UserPermissionService sUserPermission = new UserPermissionService();
            try {
                sUserPermission.setUserPermission(userEdit.getUserID(), Arrays.asList(modules));
            } catch(Exception e) {
                log.error(e);
                ModelAndView mv = new ModelAndView("AjaxResponse");
                mv.addObject("success", "false");
                mv.addObject("errorMessage", "Ocorreu um erro ao salvar as permissões do usuário.");
                return mv;
            }
        }
        if (selectedReports != null && !selectedReports.isEmpty()) {
            String[] rptString = selectedReports.split("\\|");
            Long[] reports = new Long[rptString.length];
            for (int i = 0; i < rptString.length; i++) { reports[i] = Long.parseLong(rptString[i]); }
            UserReportService sUserReport = new UserReportService();
            try {
                sUserReport.setUserReport(userEdit.getUserID(), Arrays.asList(reports));
            } catch(Exception e) {
                log.error(e);
                ModelAndView mv = new ModelAndView("AjaxResponse");
                mv.addObject("success", "false");
                mv.addObject("errorMessage", "Ocorreu um erro ao associar relatórios ao usuário.");
                return mv;
            }
        }
        if (selectedCharts != null && !selectedCharts.isEmpty()) {
            String[] chaString = selectedCharts.split("\\|");
            Long[] charts = new Long[chaString.length];
            for (int i = 0; i < chaString.length; i++) { charts[i] = Long.parseLong(chaString[i]); }
            UserChartService sUserChart = new UserChartService();
            try {
                sUserChart.setUserChart(userEdit.getUserID(), Arrays.asList(charts));
            } catch(Exception e) {
                log.error(e);
                ModelAndView mv = new ModelAndView("AjaxResponse");
                mv.addObject("success", "false");
                mv.addObject("errorMessage", "Ocorreu um erro ao associar gráficos ao usuário.");
                return mv;
            }
        }
        ModelAndView mv = new ModelAndView("AjaxResponse");
        mv.addObject("success", "true");
        mv.addObject("errorMessage", "");
        return mv;
        // </editor-fold>
    }
    
    @RequestMapping(value = "/BackOffice/Users/ResetPassword/{userID}", method = { RequestMethod.POST })
    public ModelAndView ResetPassword(@PathVariable("userID") Long userID, HttpServletResponse response) {
        // <editor-fold defaultstate="collapsed" desc="Access Permission">
        User user = (User) session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Users");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        response.setHeader("Expires", "Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis());
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache,must-revalidate,max-age=0");
        // </editor-fold>

        String defaultPassword = null;
        try { defaultPassword = new DeveloperCity.ContactBack.Workflow.SetupService().getSetup().getDefaultPassword();
        } catch(Exception e) { defaultPassword = "dc123"; }
        
        boolean success = false;
        String errorMessage = "";
        UserService userService = new UserService();
        try {
            userService.setUserPassword(userID, defaultPassword);
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

        if (success) {
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", "true");
            mv.addObject("errorMessage", "");
            return mv;
        }
        else {
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", "false");
            mv.addObject("errorMessage", errorMessage);
            return mv;
        }
    }

    @RequestMapping(value = "/BackOffice/Users/Permissions/{userID}", method = { RequestMethod.GET })
    public ModelAndView Permissions(@PathVariable("userID") Long userID, HttpServletResponse response) {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Users");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        UserPermissionService userPermissionService = new UserPermissionService();
        List<UserPermission> userPermissions = userPermissionService.getByUserID(userID);
        java.util.Collections.sort(userPermissions, new Comparator<UserPermission>() {
            public int compare(UserPermission o1, UserPermission o2) {
                if (o1 == null || o1.getModule() == null) {
                    return 1;
                } else if (o2 == null || o2.getModule() == null) {
                    return -1;
                } else {
                    int first = o1.getModule().getPortal().toString().compareTo(o2.getModule().getPortal().toString());
                    return (first == 0) ? o1.getModule().getDescription().compareTo(o2.getModule().getDescription()) : first;
                }
            }
        });
        String userPermissionsJson = null;
        try {
            List<String> excludes = new ArrayList<String>();
            excludes.add("*.user");
            userPermissionsJson = DeveloperCity.ContactBack.Workflow.RealTimeData.hibernateToJson(userPermissions, null, excludes);
        } catch(Exception e) {
            ModelAndView mv = new ModelAndView("TextPlain");
            mv.addObject("content",  e.toString());
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  userPermissionsJson );
        return mv;
    }

    @RequestMapping(value = "/BackOffice/Users/Reports/{userID}", method = { RequestMethod.GET })
    public ModelAndView Reports(@PathVariable("userID") Long userID, HttpServletResponse response) {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Users");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        UserReportService userReportService = new UserReportService();
        List<UserReport> userReports = userReportService.getByUserID(userID);
        java.util.Collections.sort(userReports, new Comparator<UserReport>() {
            public int compare(UserReport o1, UserReport o2) {
                if (o1 == null || o1.getReport() == null) {
                    return 1;
                } else if (o2 == null || o2.getReport() == null) {
                    return -1;
                } else {
                    return o1.getReport().getReportDescription().compareTo(o2.getReport().getReportDescription());
                }
            }
        });
        String userReportsJson = null;
        try {
            List<String> excludes = new ArrayList<String>();
            excludes.add("*.user");
            userReportsJson = DeveloperCity.ContactBack.Workflow.RealTimeData.hibernateToJson(userReports, null, excludes);
        } catch(Exception e) {
            ModelAndView mv = new ModelAndView("TextPlain");
            mv.addObject("content",  e.toString());
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  userReportsJson );
        return mv;
    }

    @RequestMapping(value = "/BackOffice/Users/Charts/{userID}", method = { RequestMethod.GET })
    public ModelAndView Charts(@PathVariable("userID") Long userID, HttpServletResponse response) {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Users");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        UserChartService userChartService = new UserChartService();
        List<UserChart> userCharts = userChartService.getByUserID(userID);
        java.util.Collections.sort(userCharts, new Comparator<UserChart>() {
            public int compare(UserChart o1, UserChart o2) {
                if (o1 == null || o1.getChart() == null) {
                    return 1;
                } else if (o2 == null || o2.getChart() == null) {
                    return -1;
                } else {
                    return o1.getChart().getChartDescription().compareTo(o2.getChart().getChartDescription());
                }
            }
        });
        String userChartsJson = null;
        try {
            List<String> excludes = new ArrayList<String>();
            excludes.add("*.user");
            userChartsJson = DeveloperCity.ContactBack.Workflow.RealTimeData.hibernateToJson(userCharts, null, excludes);
        } catch(Exception e) {
            ModelAndView mv = new ModelAndView("TextPlain");
            mv.addObject("content",  e.toString());
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  userChartsJson );
        return mv;
    }
}
