/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author lbarbosa
 */
@Controller
public class SMSTesterController {
    private static final Logger log = Logger.getLogger(SMSTesterController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/SMSTester/{phone}/{msg}", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView SMSTester(@PathVariable("phone") String phone, @PathVariable("msg") String msg, HttpServletResponse response) {
        log.info("SMSTester");
        log.info(phone);
        log.info(msg);
        log.info(request.getMethod());
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content", "OK");
        return mv;
    }

}



