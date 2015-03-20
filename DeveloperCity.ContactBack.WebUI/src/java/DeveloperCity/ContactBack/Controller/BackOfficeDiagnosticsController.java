package DeveloperCity.ContactBack.Controller;
import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.Workflow.UItoProcess;
import java.io.IOException;
import java.util.Properties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lbarbosa
 */
@Controller
public class BackOfficeDiagnosticsController {
    private static final Logger log = Logger.getLogger(BackOfficeDiagnosticsController.class);
    private static String StartupCommand;
    private String getStartupCommand() {
        if (StartupCommand == null || StartupCommand.length() == 0) {
            Properties p = new Properties();
            try {
                p.load(session.getServletContext().getResourceAsStream("/WEB-INF/classes/DeveloperCity/ContactBack/Controller/Config.properties"));
                StartupCommand = p.getProperty("StartupCommand");
            }
            catch(Exception e) { log.error(e); }
        }
        return StartupCommand;
    }
    private static Properties Manifest;
    private Properties getManifest() {
        if (Manifest == null || Manifest.isEmpty()) {
            Properties p = new Properties();
            try {
                p.load(session.getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF"));
                Manifest = p;
            }
            catch(Exception e) { log.error(e); }
        }
        return Manifest;
    }
    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpSession session;

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/BackOffice/Diagnostics", method = { RequestMethod.GET })
    public ModelAndView Get() throws IOException {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Diagnostics");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        Properties manifest = getManifest();

        ModelAndView mv = new ModelAndView("BackOfficeDiagnostics");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("webApplication", manifest.getProperty("Implementation-Vendor") + " " + manifest.getProperty("Implementation-Title")  + " " + manifest.getProperty("Implementation-Version"));
        mv.addObject("webBuild",  manifest.getProperty("date") + " por " + manifest.getProperty("user"));
        mv.addObject("webUser", System.getProperty("user.name") + " (" + System.getProperty("user.home") + ")");
        mv.addObject("webCurrentDirectory", System.getProperty("user.dir"));

        mv.addObject("rootPath",  rootPath);
        return mv;
    }
    @RequestMapping(value = "/BackOffice/Diagnostics", method = { RequestMethod.POST })
    public ModelAndView Post(HttpServletResponse response) throws IOException, Exception {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Diagnostics");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        String diagJson = "";

        try {
            UItoProcess uiToProcess = new UItoProcess();
            diagJson = uiToProcess.reqApplicationDiagnosticsJson();
        } catch(Exception e) { }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  diagJson );
        return mv;
    }
    @RequestMapping(value = "/BackOffice/Diagnostics/IsAlive", method = { RequestMethod.POST })
    public ModelAndView IsAliveApplication(HttpServletResponse response) throws IOException, Exception {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Diagnostics");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        boolean status = false;
        try {
            UItoProcess uiToProcess = new UItoProcess();
            status = uiToProcess.isAlive();
        } catch(Exception e) {
            status = false;
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  status ? "{ isAlive: true }" : "{ isAlive: false }" );
        return mv;
    }
    @RequestMapping(value = "/BackOffice/Diagnostics/Stop", method = { RequestMethod.POST })
    public ModelAndView StopApplication(HttpServletResponse response) throws IOException, Exception {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Diagnostics");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        boolean status = false;
        try {
            UItoProcess uiToProcess = new UItoProcess();
            status = uiToProcess.stopApplication();
        } catch(Exception e) {
            status = false;
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  status ? "Finalizando a aplicação com sucesso..." : "Erro ao finalizar aplicação..." );
        return mv;
    }
    @RequestMapping(value = "/BackOffice/Diagnostics/Start", method = { RequestMethod.POST })
    public ModelAndView StartApplication(HttpServletResponse response) throws IOException, Exception {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Diagnostics");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        boolean status = false;
        try {
            UItoProcess uiToProcess = new UItoProcess();
            status = uiToProcess.startApplication(getStartupCommand());
        } catch(Exception e) {
            status = false;
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  status ? "Iniciando aplicação com sucesso..." : "Erro ao iniciar..." );
        return mv;
    }
}
