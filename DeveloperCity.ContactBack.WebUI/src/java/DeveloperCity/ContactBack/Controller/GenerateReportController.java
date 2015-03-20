/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Controller;
import DeveloperCity.DataAccess.HibernateSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRTextExporter;
import net.sf.jasperreports.engine.export.JRTextExporterParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;
import net.sf.jasperreports.engine.export.JRXmlExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lbarbosa
 */
@Controller
public class GenerateReportController {
    private static final Logger log = Logger.getLogger(GenerateReportController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/CockPit/GenerateReport", method = { RequestMethod.GET, RequestMethod.POST })
    @SuppressWarnings("unchecked")
    public ModelAndView Post(HttpServletResponse response) throws JRException, IOException, Exception {
        if(log.isInfoEnabled()) {
            log.info("Post(HttpServletResponse response)");
        }
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");
        
        ModelAndView mv = null;
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        String Format = request.getParameter("Format");
        String Report = request.getParameter("Report");
        if (Format.equals("html")) {
            mv = new ModelAndView("GenerateReport");
            mv.addObject("rootPath",  rootPath);

            String[][] params = new String[request.getParameterMap().values().size()][2];
            Enumeration<String> pNames = request.getParameterNames();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < params.length; i++) {
                params[i][0] = pNames.nextElement().toString();
                params[i][1] = request.getParameter(params[i][0]);
                if (!params[i][0].equals("Format")) {
                    sb.append("&");
                    sb.append(params[i][0]);
                    sb.append("=");
                    sb.append(params[i][1]);
                }
            }
            mv.addObject("paramsCount", params.length);
            mv.addObject("params", params);
            mv.addObject("parString", sb.toString());
        }


        JasperPrint reportPrint = null;
        String error = "";
        if (Report.equals("queueDetails")) {
            try {
                reportPrint = queueDetails(Format.contains("xls"));
            } catch(Exception ex) { error = ex.toString(); reportPrint = null; log.error("Report error", ex); }
        } else if (Report.equals("dailyResume")) {
            try {
                reportPrint = dailyResume(Format.contains("xls"));
            } catch(Exception ex) { error = ex.toString(); reportPrint = null; log.error("Report error", ex); }
        } else if (Report.equals("contactTimeResume")) {
            try {
                reportPrint = contactTimeResume(Format.contains("xls"));
            } catch(Exception ex) { error = ex.toString(); reportPrint = null; log.error("Report error", ex); }
        } else if (Report.equals("returnTimeResume")) {
            try {
                reportPrint = returnTimeResume(Format.contains("xls"));
            } catch(Exception ex) { error = ex.toString(); reportPrint = null; log.error("Report error", ex); }
        } else if (Report.equals("agentLogin")) {
            try {
                reportPrint = agentLogin(Format.contains("xls"));
            } catch(Exception ex) { error = ex.toString(); reportPrint = null; log.error("Report error", ex); }
        } else if (Report.equals("agentAllCalls")) {
            try {
                reportPrint = agentAllCalls(Format.contains("xls"));
            } catch(Exception ex) { error = ex.toString(); reportPrint = null; log.error("Report error", ex); }
        } else if (Report.equals("agentCall")) {
            try {
                reportPrint = agentCall(Format.contains("xls"));
            } catch(Exception ex) { error = ex.toString(); reportPrint = null; log.error("Report error", ex); }
        } else if (Report.equals("agentCallResume")) {
            try {
                reportPrint = agentCallResume(Format.contains("xls"));
            } catch(Exception ex) { error = ex.toString(); reportPrint = null; log.error("Report error", ex); }
        }

        if (reportPrint == null) {
            mv = new ModelAndView("redirect:Report");
            //mv.addObject("rootPath",  rootPath);
            //mv.addObject("html", error);
            //mv.addObject("errorMessage", error);
            if(log.isInfoEnabled()) {
                log.info("Post(HttpServletResponse response) !");
            }
            return mv;
        }

        if (Format.equals("pdf")) {
            if (request.getMethod().equals("POST")) {
                response.setHeader("Content-disposition", "attachment;filename=Report_" + (new java.text.SimpleDateFormat("dd-MM-yyyy_HH-mm-ss")).format(new Date()) + ".pdf");
            }
            response.setContentType("application/pdf; charset=ISO-8859-1");
            JRPdfExporter exp = new JRPdfExporter();
            exp.setParameter(JRExporterParameter.JASPER_PRINT, reportPrint);
            exp.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
            exp.exportReport();
        } else if (Format.equals("xls")) {
            response.setHeader("Content-disposition", "attachment;filename=Report_" + (new java.text.SimpleDateFormat("dd-MM-yyyy_HH-mm-ss")).format(new Date()) + ".xls");
            response.setContentType("application/vnd.ms-excel");
            JRXlsExporter exp = new JRXlsExporter();

            exp.setParameter(JRExporterParameter.JASPER_PRINT, reportPrint);
            exp.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
            exp.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);
            exp.setParameter(JRXlsExporterParameter.IS_DETECT_CELL_TYPE, Boolean.TRUE);
            exp.setParameter(JRXlsExporterParameter.IS_WHITE_PAGE_BACKGROUND, Boolean.FALSE);
            exp.setParameter(JRXlsExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
            exp.setParameter(JRXlsExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
            exp.setParameter(JRXlsExporterParameter.IS_FONT_SIZE_FIX_ENABLED, Boolean.TRUE);
            exp.exportReport();
        } else if (Format.equals("rtf")) {
            response.setHeader("Content-disposition", "attachment;filename=Report_" + (new java.text.SimpleDateFormat("dd-MM-yyyy_HH-mm-ss")).format(new Date()) + ".rtf");
            response.setContentType("application/rtf; charset=ISO-8859-1");
            JRRtfExporter exp = new JRRtfExporter();
            exp.setParameter(JRExporterParameter.JASPER_PRINT, reportPrint);
            exp.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
            exp.exportReport();
        } else if (Format.equals("txt")) {
            response.setContentType("text/plain; charset=ISO-8859-1");
            JRTextExporter exp = new JRTextExporter();
            exp.setParameter(JRTextExporterParameter.PAGE_WIDTH, 300);
            exp.setParameter(JRTextExporterParameter.CHARACTER_HEIGHT, 10f);
            exp.setParameter(JRTextExporterParameter.CHARACTER_ENCODING, "ISO-8859-1");
            exp.setParameter(JRExporterParameter.JASPER_PRINT, reportPrint);
            exp.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
            exp.exportReport();
        } else if (Format.equals("csv")) {
            response.setContentType("text/comma-separated-values; charset=ISO-8859-1");
            JRCsvExporter exp = new JRCsvExporter();
            exp.setParameter(JRExporterParameter.JASPER_PRINT, reportPrint);
            exp.setParameter(JRTextExporterParameter.CHARACTER_ENCODING, "ISO-8859-1");
            exp.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
            exp.exportReport();
        } else if (Format.equals("xml")) {
            response.setContentType("text/xml; charset=ISO-8859-1");
            JRXmlExporter exp = new JRXmlExporter();
            exp.setParameter(JRExporterParameter.JASPER_PRINT, reportPrint);
            exp.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
            exp.exportReport();
        } else if (Format.equals("docx")) {
            response.setHeader("Content-disposition", "attachment;filename=Report_" + (new java.text.SimpleDateFormat("dd-MM-yyyy_HH-mm-ss")).format(new Date()) + ".docx");
            response.setContentType("application/msword; charset=ISO-8859-1");
            JRDocxExporter exp = new JRDocxExporter();
            exp.setParameter(JRExporterParameter.JASPER_PRINT, reportPrint);
            exp.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
            exp.exportReport();
        } else if (Format.equals("xlsx")) {
            response.setHeader("Content-disposition", "attachment;filename=Report_" + (new java.text.SimpleDateFormat("dd-MM-yyyy_HH-mm-ss")).format(new Date()) + ".xlsx");
            response.setContentType("application/msexcel; charset=ISO-8859-1");
            JRXlsxExporter exp = new JRXlsxExporter();
            exp.setParameter(JRExporterParameter.JASPER_PRINT, reportPrint);
            exp.setParameter(JRExporterParameter.OUTPUT_STREAM, response.getOutputStream());
            exp.exportReport();
        }
        if(log.isInfoEnabled()) {
            log.info("Post(HttpServletResponse response) !");
        }
        return mv;
    }

    private JasperPrint queueDetails(boolean ignorePagination) throws ParseException, Exception {
        Date StartDate = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("StartDate"));
        Date EndDate = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("EndDate"));
        if (StartDate.after(EndDate)) {
            throw new Exception("A data de término deverá ser posterior à data de início.");
        }

	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("StartDate", StartDate);
	parameters.put("EndDate", EndDate);
        parameters.put("IS_IGNORE_PAGINATION", ignorePagination);

        return getJasperPrint(request.getSession().getServletContext().getRealPath("/resources/Reports/QueueDetails.jasper"), parameters);
    }
    private JasperPrint dailyResume(boolean ignorePagination) throws ParseException, Exception {
        Date StartDate = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("StartDate"));
        Date EndDate = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("EndDate"));
        if (StartDate.after(EndDate)) {
            throw new Exception("A data de término deverá ser posterior à data de início.");
        }

	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("StartDate", StartDate);
	parameters.put("EndDate", EndDate);
        parameters.put("IS_IGNORE_PAGINATION", ignorePagination);

        return getJasperPrint(request.getSession().getServletContext().getRealPath("/resources/Reports/DailyResume.jasper"), parameters);
    }
    private JasperPrint contactTimeResume(boolean ignorePagination) throws ParseException, Exception {
        Date StartDate = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("StartDate"));
        Date EndDate = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("EndDate"));
        if (StartDate.after(EndDate)) {
            throw new Exception("A data de término deverá ser posterior à data de início.");
        }

	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("StartDate", StartDate);
	parameters.put("EndDate", EndDate);
        parameters.put("IS_IGNORE_PAGINATION", ignorePagination);

        return getJasperPrint(request.getSession().getServletContext().getRealPath("/resources/Reports/ContactTimeResume.jasper"), parameters);
    }
    private JasperPrint returnTimeResume(boolean ignorePagination) throws ParseException, Exception {
        Date StartDate = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("StartDate"));
        Date EndDate = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("EndDate"));
        if (StartDate.after(EndDate)) {
            throw new Exception("A data de término deverá ser posterior à data de início.");
        }

	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("StartDate", StartDate);
	parameters.put("EndDate", EndDate);
        parameters.put("IS_IGNORE_PAGINATION", ignorePagination);

        return getJasperPrint(request.getSession().getServletContext().getRealPath("/resources/Reports/ReturnTimeResume.jasper"), parameters);
    }
    private JasperPrint agentLogin(boolean ignorePagination) throws ParseException, Exception {
        Date StartDate = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("StartDate"));
        Date EndDate = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("EndDate"));
        if (StartDate.after(EndDate)) {
            throw new Exception("A data de término deverá ser posterior à data de início.");
        }

	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("StartDate", StartDate);
	parameters.put("EndDate", EndDate);
        parameters.put("IS_IGNORE_PAGINATION", ignorePagination);

        return getJasperPrint(request.getSession().getServletContext().getRealPath("/resources/Reports/AgentLogin.jasper"), parameters);
    }
    private JasperPrint agentAllCalls(boolean ignorePagination) throws ParseException, Exception {
        Date StartDate = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("StartDate"));
        Date EndDate = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("EndDate"));
        if (StartDate.after(EndDate)) {
            throw new Exception("A data de término deverá ser posterior à data de início.");
        }

	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("StartDate", StartDate);
	parameters.put("EndDate", EndDate);
        parameters.put("IS_IGNORE_PAGINATION", ignorePagination);

        return getJasperPrint(request.getSession().getServletContext().getRealPath("/resources/Reports/AgentAllCalls.jasper"), parameters);
    }
    private JasperPrint agentCall(boolean ignorePagination) throws ParseException, Exception {
        Date StartDate = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("StartDate"));
        Date EndDate = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("EndDate"));
        if (StartDate.after(EndDate)) {
            throw new Exception("A data de término deverá ser posterior à data de início.");
        }

	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("StartDate", StartDate);
	parameters.put("EndDate", EndDate);
        parameters.put("IS_IGNORE_PAGINATION", ignorePagination);

        return getJasperPrint(request.getSession().getServletContext().getRealPath("/resources/Reports/AgentCall.jasper"), parameters);
    }
    private JasperPrint agentCallResume(boolean ignorePagination) throws ParseException, Exception {
        Date StartDate = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("StartDate"));
        Date EndDate = new java.text.SimpleDateFormat("dd/MM/yyyy").parse(request.getParameter("EndDate"));
        if (StartDate.after(EndDate)) {
            throw new Exception("A data de término deverá ser posterior à data de início.");
        }

	Map<String, Object> parameters = new HashMap<String, Object>();
	parameters.put("StartDate", StartDate);
	parameters.put("EndDate", EndDate);
        parameters.put("IS_IGNORE_PAGINATION", ignorePagination);

        return getJasperPrint(request.getSession().getServletContext().getRealPath("/resources/Reports/AgentCallResume.jasper"), parameters);
    }

    @SuppressWarnings("deprecation")
    private JasperPrint getJasperPrint(String reportFileName, Map<String, Object> parameters) throws Exception {
	File reportFile = new File(reportFileName);
        if (!reportFile.exists()) {
            throw new FileNotFoundException(reportFileName + " was not found in resources folder");
        }
        Session s = HibernateSession.getSessionFactory().openSession();
	JasperPrint jasperPrint =
		JasperFillManager.fillReport(
			reportFileName,
			parameters,
                        s.connection()
			);
        s.close();
        jasperPrint.setName("Danone");
        return jasperPrint;
    }
}