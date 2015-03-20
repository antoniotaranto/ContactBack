package DeveloperCity.ContactBack.Controller;

import DeveloperCity.ContactBack.DomainModel.DestinationType;
import DeveloperCity.ContactBack.DomainModel.Setup;
import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.Workflow.SetupService;
import DeveloperCity.ContactBack.Workflow.UItoProcess;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BackOfficeSetupController {
    private static final Logger log = Logger.getLogger(BackOfficeSetupController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpSession session;

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    private boolean hasAccess() {
        User user = (User)session.getAttribute("User");
        if (user == null) {
            return false;
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "BackOffice/Setup");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return false;
        }
        return true;
    }

    @RequestMapping(value = "/BackOffice/Setup", method = { RequestMethod.GET })
    public ModelAndView Get() {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        ModelAndView mv = new ModelAndView("BackOfficeSetup");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);
        return mv;
    }

    @RequestMapping(value = "/BackOffice/Setup/Json", method = { RequestMethod.GET })
    public ModelAndView List(HttpServletResponse response) {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        SetupService setupService = null;

        String setupJson = null;
        try {
            setupService = new SetupService();
            setupJson = DeveloperCity.ContactBack.Workflow.RealTimeData.hibernateToJson(setupService.getSetup(), null, null);
        } catch(Exception e) {
            ModelAndView mv = new ModelAndView("TextPlain");
            mv.addObject("content",  e.toString());
            return mv;
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  setupJson );
        return mv;
    }

    @RequestMapping(value = "/BackOffice/Setup/Save", method = { RequestMethod.POST })
    public ModelAndView Save(HttpServletResponse response) {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");
        String timeBetweenCallBacks = request.getParameter("timeBetweenCallBacks");
        String lateCallBackAfter = request.getParameter("lateCallBackAfter");
        String lateCallBackTime = request.getParameter("lateCallBackTime");
        String endOfQueue = request.getParameter("endOfQueue");
        String maxCallBacks = request.getParameter("maxCallBacks");
        String cti_DeviceName = request.getParameter("cti_DeviceName");
        String prefixDial = request.getParameter("prefixDial");
        String shiftWeekdayStart = request.getParameter("shiftWeekdayStart");
        String shiftWeekdayEnd = request.getParameter("shiftWeekdayEnd");
        String shiftSaturdayStart = request.getParameter("shiftSaturdayStart");
        String shiftSaturdayEnd = request.getParameter("shiftSaturdayEnd");
        String internalExtensionDestination = request.getParameter("internalExtensionDestination");
        String mobilePhoneDestination = request.getParameter("mobilePhoneDestination");
        String landLineDestination = request.getParameter("landLineDestination");
        String invalidNumberDestination = request.getParameter("invalidNumberDestination");
        String queueSuccessDevice = request.getParameter("queueSuccessDevice");
        String queueAlreadyDevice = request.getParameter("queueAlreadyDevice");
        String queueInvalidNumberDevice = request.getParameter("queueInvalidNumberDevice");
        String queueNotInShiftTimeDevice = request.getParameter("queueNotInShiftTimeDevice");
        String smsUrl = request.getParameter("smsUrl");
        String smsMessage = request.getParameter("smsMessage");
        String smsAccount = request.getParameter("smsAccount");
        String smsCode = request.getParameter("smsCode");
        String smsFrom = request.getParameter("smsFrom");
        String defaultPassword = request.getParameter("defaultPassword");
        String proxyIP = request.getParameter("proxyIP");
        Integer proxyPort = null;
        try {
            proxyPort = Integer.parseInt(request.getParameter("proxyPort"));
        } catch (Exception e) {
            proxyPort = null;
        }
        String ivr_DeviceName = request.getParameter("ivr_DeviceName");
        String voiceFolder = request.getParameter("voiceFolder");

        try {
            SetupService sSetup = new SetupService();
            Setup setup = sSetup.getSetup();

            setup.setTimeBetweenCallBacks(Integer.parseInt(timeBetweenCallBacks));
            setup.setLateCallBackAfter(Integer.parseInt(lateCallBackAfter));
            setup.setLateCallBackTime(Integer.parseInt(lateCallBackTime));
            setup.setEndOfQueue(Integer.parseInt(endOfQueue));
            setup.setMaxCallBacks(Integer.parseInt(maxCallBacks));
            setup.setCTI_DeviceName(cti_DeviceName);
            setup.setPrefixDial(prefixDial);
            setup.setShiftWeekdayStartHour(Short.parseShort(shiftWeekdayStart.split(":")[0]));
            setup.setShiftWeekdayStartMinute(Short.parseShort(shiftWeekdayStart.split(":")[1]));
            setup.setShiftWeekdayEndHour(Short.parseShort(shiftWeekdayEnd.split(":")[0]));
            setup.setShiftWeekdayEndMinute(Short.parseShort(shiftWeekdayEnd.split(":")[1]));
            setup.setShiftSaturdayStartHour(Short.parseShort(shiftSaturdayStart.split(":")[0]));
            setup.setShiftSaturdayStartMinute(Short.parseShort(shiftSaturdayStart.split(":")[1]));
            setup.setShiftSaturdayEndHour(Short.parseShort(shiftSaturdayEnd.split(":")[0]));
            setup.setShiftSaturdayEndMinute(Short.parseShort(shiftSaturdayEnd.split(":")[1]));
            setup.setInternalExtensionDestination(DestinationType.valueOf(internalExtensionDestination));
            setup.setMobilePhoneDestination(DestinationType.valueOf(mobilePhoneDestination));
            setup.setLandLineDestination(DestinationType.valueOf(landLineDestination));
            setup.setInvalidNumberDestination(DestinationType.valueOf(invalidNumberDestination));
            setup.setQueueSuccessDevice(queueSuccessDevice);
            setup.setQueueAlreadyDevice(queueAlreadyDevice);
            setup.setQueueInvalidNumberDevice(queueInvalidNumberDevice);
            setup.setQueueNotInShiftTimeDevice(queueNotInShiftTimeDevice);
            setup.setSMSUrl(smsUrl);
            setup.setSMSMessage(smsMessage);
            setup.setSMSAccount(smsAccount);
            setup.setSMSCode(smsCode);
            setup.setSMSFrom(smsFrom);
            setup.setDefaultPassword(defaultPassword);
            setup.setProxyIP(proxyIP);
            setup.setProxyPort(proxyPort);
            setup.setIVR_DeviceName(ivr_DeviceName);
            setup.setVoiceFolder(voiceFolder);

            setup = sSetup.setSetup(setup);

            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", "true");
            mv.addObject("errorMessage", "");
            return mv;
        } catch(Exception e) {
            log.error(e);
            ModelAndView mv = new ModelAndView("AjaxResponse");
            mv.addObject("success", "false");
            mv.addObject("errorMessage", e.toString());
            return mv;
        }
    }
    @RequestMapping(value = "/BackOffice/Setup/Refresh", method = { RequestMethod.POST })
    public ModelAndView Refresh(HttpServletResponse response) {
        if (!hasAccess()) { return new ModelAndView("redirect:/"); }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        UItoProcess uiToProcess = new UItoProcess();
        try {
            boolean success = uiToProcess.reloadSetup();
            if (success) {
                ModelAndView mv = new ModelAndView("AjaxResponse");
                mv.addObject("success", "true");
                mv.addObject("errorMessage", "");
                return mv;
            }
            else {
                ModelAndView mv = new ModelAndView("AjaxResponse");
                mv.addObject("success", "false");
                mv.addObject("errorMessage", "Ocorreu um erro ao executar sua solicitação. Por gentileza aguarde alguns instantes e tente novamente.");
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