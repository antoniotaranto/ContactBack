package DeveloperCity.ContactBack.Controller;
import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.Workflow.ZipUtil;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Properties;
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
public class BackOfficeLogsController {
    private static final Logger log = Logger.getLogger(BackOfficeLogsController.class);
    private static String LogFolder;
    private String getLogFolder() {
        if (LogFolder == null || LogFolder.length() == 0) {
            Properties p = new Properties();
            try {
                p.load(session.getServletContext().getResourceAsStream("/WEB-INF/classes/DeveloperCity/ContactBack/Controller/Config.properties"));
                LogFolder = p.getProperty("LogFolder");
            }
            catch(Exception e) { log.error(e); }
        }
        return LogFolder;
    }
    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpSession session;

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/BackOffice/Logs", method = { RequestMethod.GET })
    public ModelAndView Get() {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Logs");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        ModelAndView mv = new ModelAndView("BackOfficeLogs");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);
        return mv;
    }
    @RequestMapping(value = "/BackOffice/Logs/Get/{fileName}", method = { RequestMethod.GET })
    public ModelAndView DownloadFile(@PathVariable("fileName") String fileName, HttpServletResponse response) throws IOException {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Logs");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        response.setContentType("application/octet-stream");
        ZipUtil.ZipFile(getLogFolder() + System.getProperty("file.separator") + fileName, response.getOutputStream());
        return null;
    }
    @RequestMapping(value = "/BackOffice/Logs/List", method = { RequestMethod.POST })
    public ModelAndView Post(HttpServletResponse response) throws IOException, Exception {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Logs");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        File logPath = new File(getLogFolder());
        File[] files = logPath.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                if (pathname.getName().endsWith(".log") ) {
                    return true;
                }
                return false;
            }
        });
        List<Log> logFiles = new ArrayList<Log>();
        for (File f : files) {
            logFiles.add(new Log(f.getName(), f.length(), new Date(f.lastModified()) ));
        }

        Collections.sort(logFiles, new Comparator<Log>() {
            public int compare(Log l1, Log l2) {
                if (l1 == null) {
                    return 1;
                } else if (l2 == null) {
                    return -1;
                } else {
                    return -l1.getLastModified().compareTo(l2.getLastModified());
                }
            }
        });

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content", DeveloperCity.Serialization.JSON.Serialize(logFiles, null, null));
        return mv;
    }
    public class Log implements Serializable {
        static final long serialVersionUID = 1L;
        private String FileName;
        private long Size;
        private Date LastModified;

        public Log() {
        }
        public Log(String FileName, long Size, Date LastModified) {
            this.FileName = FileName;
            this.Size = Size;
            this.LastModified = LastModified;
        }

        public Date getLastModified() {
            return LastModified;
        }
        private void setLastModified(Date LastModified) {
            this.LastModified = LastModified;
        }
        public String getFileName() {
            return FileName;
        }
        private void setFileName(String FileName) {
            this.FileName = FileName;
        }
        public long getSize() {
            return Size;
        }
        private void setSize(long Size) {
            this.Size = Size;
        }
    }
}
