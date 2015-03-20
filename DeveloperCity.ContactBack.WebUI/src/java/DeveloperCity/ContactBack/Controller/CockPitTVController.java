/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Controller;
import DeveloperCity.ContactBack.DomainModel.Agent;
import DeveloperCity.ContactBack.DomainModel.AgentStatus;
import DeveloperCity.ContactBack.DomainModel.Call;
import DeveloperCity.ContactBack.DomainModel.Queue;
import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.DomainModel.Utils;
import DeveloperCity.ContactBack.Workflow.AgentService;
import DeveloperCity.ContactBack.Workflow.CallService;
import DeveloperCity.ContactBack.Workflow.QueueService;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
public class CockPitTVController {
    private static final Logger log = Logger.getLogger(CockPitTVController.class);

    @Autowired @SuppressWarnings("PackageVisibleField")
    HttpServletRequest request;

    @RequestMapping(value = "/CockPit/TV", method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView Get(HttpServletResponse response) {
        User user = (User)request.getSession(true).getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "CockPit/TV");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        ModelAndView mv = new ModelAndView("CockPitTV");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);
        return mv;
    }
    @RequestMapping(value = "/CockPit/TV/FullScreen", method = { RequestMethod.GET })
    public ModelAndView GetFullScreen(HttpServletResponse response) {
        User user = (User)request.getSession(true).getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "CockPit/TV");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        ModelAndView mv = new ModelAndView("CockPitTVFullScreen");
        String rootPath = request.getContextPath().endsWith("/") ? request.getContextPath() : request.getContextPath() + "/";
        mv.addObject("rootPath",  rootPath);
        return mv;
    }
    @RequestMapping(value = "/CockPit/TV/Agent", method = { RequestMethod.GET })
    public ModelAndView GetAgent(HttpServletResponse response) {
        User user = (User)request.getSession(true).getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "CockPit/TV");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        StringBuilder json = new StringBuilder();
        try {
            int offline = 0, inbreak = 0, incall = 0, available = 0;
            List<Agent> agents = new AgentService().getUserActive();
            CallService callService = new CallService();
            List<Call> calls = callService.getTalking();
            for (Agent a : agents) {
                if (a.getAgentStatus() == AgentStatus.NotLogged) {
                    offline++;
                } else if (a.getAgentStatus() == AgentStatus.Break || a.getAgentStatus() == AgentStatus.NotReady) {
                    inbreak++;
                } else {
                    final Agent activeAgent = a;
                    boolean isInCall = DeveloperCity.Collections.contains(calls, new DeveloperCity.Collections.Predicate<Call>() {
                        public boolean apply(Call call) { return call.getAgent().getUserID() == activeAgent.getUserID(); }
                    });
                    if (isInCall) { incall++; } else { available++; }
                }
            }

            String talkTime = callService.getAverageTalkTime(new Date());
            String waitTime = callService.getAverageWaitTime(new Date());

            json.append("{availablecount:");
            json.append( DeveloperCity.Serialization.JSON.Serialize(available) );
            json.append(",incallcount:");
            json.append( DeveloperCity.Serialization.JSON.Serialize(incall) );
            json.append(",inbreakcount:");
            json.append( DeveloperCity.Serialization.JSON.Serialize(inbreak) );
            json.append(",offlinecount:");
            json.append( DeveloperCity.Serialization.JSON.Serialize(offline) );
            json.append(",avgtalktime:");
            json.append( DeveloperCity.Serialization.JSON.Serialize(talkTime) );
            json.append(",avgwaittime:");
            json.append( DeveloperCity.Serialization.JSON.Serialize(waitTime) );
            json.append("}");
        } catch(Exception e) {
            ModelAndView mv = new ModelAndView("TextPlain");
            mv.addObject("content",  e.toString());
            return mv;
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  json.toString() );
        return mv;
    }
    @RequestMapping(value = "/CockPit/TV/Queue", method = { RequestMethod.GET })
    public ModelAndView GetQueue(HttpServletResponse response) {
        User user = (User)request.getSession(true).getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "CockPit/TV");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }

        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        StringBuilder json = new StringBuilder();
        try {
            Date now = new Date();
            int queuecount = 0, frozencount = 0, incallcount = 0, areacode1count = 0, areacode2count = 0;
            long longestwaittimeNumber = 0, totalWaitTime = 0;
            String areacode1 = "", areacode2 = "", longestwaittime = "", longestwaitfrom = "", avgwaittime = "";

            QueueService queueService = new QueueService();
            CallService callService = new CallService();
            List<Queue> queue = queueService.getInQueue();
            List<Call> calls = callService.getTalking();
            HashMap<String, Integer> countAreaCode = new HashMap<String, Integer>();
            for (Queue q : queue) {
                String areaCode = q.getCallBackNumber().substring(0, 2);
                long waitTime = now.getTime() - q.getCallTime().getTime();
                totalWaitTime += waitTime;
                if (waitTime > longestwaittimeNumber) {
                    longestwaittimeNumber = waitTime;
                    longestwaittime = Utils.TimeDuration( ((double)waitTime) / 1000D);
                    longestwaitfrom = q.getCallBackNumber();
                }

                if (countAreaCode.containsKey(areaCode)) { countAreaCode.put(areaCode, countAreaCode.get(areaCode) + 1); }
                else { countAreaCode.put(areaCode, 1); }
                if (q.getDontCallBefore().getTime() > now.getTime()) {
                    frozencount++;
                } else {
                    queuecount++;
                }
            }
            incallcount = calls.size();
            avgwaittime = Utils.TimeDuration(((double)totalWaitTime / 1000D) / ((double) queue.size()));

            for (String area : countAreaCode.keySet()) {
                int qtty = countAreaCode.get(area);
                if (qtty > areacode1count) {
                    areacode2count = areacode1count;
                    areacode2 = areacode1;
                    areacode1count = qtty;
                    areacode1 = area;
                } else if (qtty > areacode2count) {
                    areacode2count = qtty;
                    areacode2 = area;
                }
            }
            json.append("{queuecount:");
            json.append( DeveloperCity.Serialization.JSON.Serialize(queuecount) );
            json.append(",frozencount:");
            json.append( DeveloperCity.Serialization.JSON.Serialize(frozencount) );
            json.append(",incallcount:");
            json.append( DeveloperCity.Serialization.JSON.Serialize(incallcount) );
            json.append(",areacode1count:");
            json.append( DeveloperCity.Serialization.JSON.Serialize(areacode1count) );
            json.append(",areacode2count:");
            json.append( DeveloperCity.Serialization.JSON.Serialize(areacode2count) );
            json.append(",areacode1:");
            json.append( DeveloperCity.Serialization.JSON.Serialize(areacode1) );
            json.append(",areacode2:");
            json.append( DeveloperCity.Serialization.JSON.Serialize(areacode2) );
            json.append(",longestwaittime:");
            json.append( DeveloperCity.Serialization.JSON.Serialize(longestwaittime) );
            json.append(",longestwaitfrom:");
            json.append( DeveloperCity.Serialization.JSON.Serialize(longestwaitfrom) );
            json.append(",avgwaittime:");
            json.append( DeveloperCity.Serialization.JSON.Serialize(avgwaittime) );
            json.append("}");
        } catch(Exception e) {
            ModelAndView mv = new ModelAndView("TextPlain");
            mv.addObject("content",  e.toString());
            return mv;
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  json.toString() );
        return mv;
    }
    @RequestMapping(value = "/CockPit/TV/Call", method = { RequestMethod.GET })
    public ModelAndView GetCall(HttpServletResponse response) {
        User user = (User)request.getSession(true).getAttribute("User");
        if (user == null) {
            return new ModelAndView("redirect:/");
        }

        LoginController.AccessType accessType = LoginController.HasAccess(user, "CockPit/TV");
        if (accessType == DeveloperCity.ContactBack.Controller.LoginController.AccessType.None) {
            return new ModelAndView("redirect:/");
        }
        
        response.setHeader("Expires","Thu, 01 Dec 1994 16:00:00 GMT");
        response.setDateHeader("Last-Modified", System.currentTimeMillis() );
        response.setHeader("Pragma","no-cache");
        response.setHeader("Cache-Control","no-cache,must-revalidate,max-age=0");

        StringBuilder json = new StringBuilder();
        try {
            Date now = new Date();

            int incallcount = 0;
            long longestwaittimeNumber = 0, totalWaitTime = 0, totalTalkTime = 0;
            String longestwaittime = "", longestwaitfrom = "", avgtalktime = "", avgwaittime = "";

            CallService callService = new CallService();
            List<Call> calls = callService.getTalking();
            for (Call c : calls) {
                String areaCode = c.getQueue().getCallBackNumber().substring(0, 2);
                long waitTime = c.getStartTime().getTime() - c.getQueue().getCallTime().getTime();
                long talkTime = (c.getEndTime() != null ? c.getEndTime().getTime() : now.getTime()) - c.getAnswerTime().getTime();
                totalWaitTime += waitTime;
                totalTalkTime += talkTime;
                if (waitTime > longestwaittimeNumber) {
                    longestwaittimeNumber = waitTime;
                    longestwaittime = Utils.TimeDuration( ((double)waitTime) / 1000D);
                    longestwaitfrom = c.getQueue().getCallBackNumber();
                }
            }
            incallcount = calls.size();
            avgwaittime = Utils.TimeDuration(((double)totalWaitTime / 1000D) / ((double) calls.size()));
            avgtalktime = Utils.TimeDuration(((double)totalTalkTime / 1000D) / ((double) calls.size()));

            json.append("{incallcount:");
            json.append( DeveloperCity.Serialization.JSON.Serialize(incallcount) );
            json.append(",longestwaittime:");
            json.append( DeveloperCity.Serialization.JSON.Serialize(longestwaittime) );
            json.append(",longestwaitfrom:");
            json.append( DeveloperCity.Serialization.JSON.Serialize(longestwaitfrom) );
            json.append(",avgwaittime:");
            json.append( DeveloperCity.Serialization.JSON.Serialize(avgwaittime) );
            json.append(",avgtalktime:");
            json.append( DeveloperCity.Serialization.JSON.Serialize(avgtalktime) );
            json.append("}");
        } catch(Exception e) {
            ModelAndView mv = new ModelAndView("TextPlain");
            mv.addObject("content",  e.toString());
            return mv;
        }

        ModelAndView mv = new ModelAndView("TextPlain");
        mv.addObject("content",  json.toString() );
        return mv;
    }
}