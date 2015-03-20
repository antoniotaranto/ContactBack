package DeveloperCity.ContactBack.Controller;
import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.DomainModel.Customer;
import DeveloperCity.ContactBack.DomainModel.Module;
import DeveloperCity.ContactBack.DomainModel.UserPermission;
import DeveloperCity.ContactBack.DomainModel.UserStatus;
import DeveloperCity.ContactBack.Exception.AuthenticationFailException;
import DeveloperCity.ContactBack.Exception.PasswordArgumentException;
import DeveloperCity.ContactBack.Exception.UserNotFoundException;
import DeveloperCity.ContactBack.Workflow.CustomerService;
import DeveloperCity.ContactBack.Workflow.ModuleService;
import DeveloperCity.ContactBack.Workflow.UserPermissionService;
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
public class BackOfficeCustomersController {
    private static final Logger log = Logger.getLogger(BackOfficeCustomersController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpSession session;

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/BackOffice/Customers", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView Get(HttpServletResponse response) {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Customers");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        ModelAndView mv = new ModelAndView("BackOfficeCustomers");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);
        return mv;
    }
    @RequestMapping(value = "/BackOffice/Customers/List", method = { RequestMethod.POST })
    public ModelAndView List(HttpServletResponse response) {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Customers");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        CustomerService customerService = new CustomerService();
        List<Customer> customers = customerService.getAll();
        java.util.Collections.sort(customers, new Comparator<User>() {
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
        String customersJson = null;
        try {
            List<String> excludes = new ArrayList<String>();
            excludes.add("*.password");
            customersJson = DeveloperCity.ContactBack.Workflow.RealTimeData.hibernateToJson(customers, null, excludes);
        } catch(Exception e) {
            ModelAndView mv = new ModelAndView("TextPlain");
            mv.addObject("content",  e.toString());
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  customersJson.replace("\"class\"", "\"type\"") );
        return mv;
    }
    @RequestMapping(value = "/BackOffice/Customers/{userID}", method = { RequestMethod.GET })
    public ModelAndView GetByID(@PathVariable("userID") Long userID, HttpServletResponse response) {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Customers");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        CustomerService customerService = new CustomerService();
        Customer customerEdit = customerService.getByID(userID);

        String customerJson = null;
        try {
            List<String> excludes = new ArrayList<String>();
            excludes.add("*.password");
            customerJson = DeveloperCity.ContactBack.Workflow.RealTimeData.hibernateToJson(customerEdit, null, excludes);
        } catch(Exception e) {
            ModelAndView mv = new ModelAndView("TextPlain");
            mv.addObject("content", e.toString());
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  customerJson.replace("\"class\"", "\"type\"") );
        return mv;
    }
    @RequestMapping(value = "/BackOffice/Customers/{userID}", method = { RequestMethod.POST })
    public ModelAndView Save(@PathVariable("userID") Long userID, HttpServletResponse response) throws UnsupportedEncodingException {
        // <editor-fold defaultstate="collapsed" desc="Access Permission">
        User user = (User) session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Customers");
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
        String mobilePhone = request.getParameter("mobilePhone");
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Read customer from database">
        CustomerService customerService = new CustomerService();
        String defaultPassword = null;
        try { defaultPassword = new DeveloperCity.ContactBack.Workflow.SetupService().getSetup().getDefaultPassword();
        } catch(Exception e) { defaultPassword = "dc123"; }
        Customer customerEdit = null;
        if (userID == 0) {
            customerEdit = new Customer();
            customerEdit.setPassword(DeveloperCity.Security.Crypto.EncryptForever(defaultPassword));
        }
        else {
            customerEdit = customerService.getByID(userID);
        }
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Change properties">
        if (customerEdit.getUsername() == null || (!customerEdit.getUsername().equals( username ))) {
            // Verify duplicity
            Customer verify = customerService.getByUsername(username, true);
            if (verify != null && verify.getUserID() != userID) {
                ModelAndView mv = new ModelAndView("AjaxResponse");
                mv.addObject("success", "false");
                mv.addObject("errorMessage", "O nome-de-usuário escolhido já está em uso. Favor escolher outro.");
                return mv;
            }
            customerEdit.setUsername(username);
        }
        customerEdit.setName(name);
        customerEdit.setBirthday(birthday);
        customerEdit.setEmail(email);
        customerEdit.setUserStatus(UserStatus.valueOf(userStatus));
        customerEdit.setMobilePhone(mobilePhone);
        // </editor-fold>
        // <editor-fold defaultstate="collapsed" desc="Save">
        try {
            customerEdit = customerService.setCustomer(customerEdit);
        } catch(Exception e) {
            log.error(e);
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", "false");
            mv.addObject("errorMessage", "Ocorreu um erro ao salvar o usuário.");
            return mv;
        }
        
        UserPermissionService sUserPermission = new UserPermissionService();
        ModuleService sModule = new ModuleService();
        List<Module> modules = sModule.getByServlet("CockPit/WebQueue");
        Long[] moduleIds = new Long[modules.size()];
        for (int i = 0; i < modules.size(); i++) { moduleIds[i] = modules.get(i).getModuleID(); }
        try {
            sUserPermission.setUserPermission(customerEdit.getUserID(), Arrays.asList(moduleIds));
        } catch(Exception e) {
            log.error(e);
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", "false");
            mv.addObject("errorMessage", "Ocorreu um erro ao salvar as permissões do usuário.");
            return mv;
        }
        ModelAndView mv = new ModelAndView("AjaxResponse");
        mv.addObject("success", "true");
        mv.addObject("errorMessage", "");
        return mv;
        // </editor-fold>
    }

    @RequestMapping(value = "/BackOffice/Customers/ResetPassword/{userID}", method = { RequestMethod.POST })
    public ModelAndView ResetPassword(@PathVariable("userID") Long userID, HttpServletResponse response) {
        // <editor-fold defaultstate="collapsed" desc="Access Permission">
        User user = (User) session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Customers");
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

    @RequestMapping(value = "/BackOffice/Customers/Permissions/{userID}", method = { RequestMethod.GET })
    public ModelAndView Permissions(@PathVariable("userID") Long userID, HttpServletResponse response) {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Customers");
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
}
