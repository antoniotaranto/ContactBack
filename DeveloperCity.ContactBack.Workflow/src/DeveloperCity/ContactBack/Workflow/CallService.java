package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.DataAccess.HibernateSession;
import DeveloperCity.ContactBack.DataAccess.AgentDAO;
import DeveloperCity.ContactBack.DataAccess.CallDAO;
import DeveloperCity.ContactBack.DataAccess.QueueDAO;
import DeveloperCity.ContactBack.DomainModel.Call;
import DeveloperCity.ContactBack.DomainModel.Agent;
import DeveloperCity.ContactBack.DomainModel.Queue;
import DeveloperCity.ContactBack.DomainModel.CallStatus;
import DeveloperCity.ContactBack.DomainModel.Utils;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CallService {

    private static Logger logger = Logger.getLogger(CallService.class);
    // <editor-fold defaultstate="collapsed" desc="Fields">
    private CallManagerService callManagerService;
    // </editor-fold>

    public CallService() {
    }

    // <editor-fold defaultstate="collapsed" desc="Get">
    public Call getByID(long id) {
        Call retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = CallDAO.CreateInstance(s).getByID(id);
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="List">
    @SuppressWarnings("unchecked")
    public List<Call> getAll() {
        List<Call> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Call>) CallDAO.CreateInstance(s).getAll();
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Call> getCompletedByDates(Date from, Date to) {
        List<Call> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Call>) CallDAO.CreateInstance(s).getByCallStatusAndDates(from, to, CallStatus.Complete, CallStatus.Busy, CallStatus.Error);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Call> getSuccessfulByDates(Date from, Date to) {
        List<Call> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Call>) CallDAO.CreateInstance(s).getByCallStatusAndDates(from, to, CallStatus.Complete);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Call> getFirstCallbackByDates(Date from, Date to) {
        List<Call> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Call>) CallDAO.CreateInstance(s).getFirstCallbacksByDates(from, to);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Call> getAll(int currentPage, int pageSize) {
        List<Call> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Call>) CallDAO.CreateInstance(s).getAll(currentPage, pageSize);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Call> getByAgentAndDates(long agentID, Date from, Date to) {
        List<Call> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Call>) CallDAO.CreateInstance(s).getByAgentAndDates(agentID, from, to, CallDAO.StartTime.desc());
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Call> getByAgentAndDates(long agentID, Date from, Date to, long sinceItem) {
        List<Call> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Call>) CallDAO.CreateInstance(s).getByAgentAndDates(agentID, sinceItem, from, to, CallDAO.StartTime.desc());
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Call> getTalking() {
        List<Call> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Call>) CallDAO.CreateInstance(s).getByCallStatusAndNotFinished(CallStatus.Talking);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Call> getTalking(int currentPage, int pageSize) {
        List<Call> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Call>) CallDAO.CreateInstance(s).getByCallStatus(currentPage, pageSize, CallStatus.Talking);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Call> getLocked() {
        List<Call> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Call>) CallDAO.CreateInstance(s).getByCallStatus(
                //CallStatus.Busy,
                //CallStatus.Complete,
                CallStatus.Dialing,
                //CallStatus.NoAnswer,
                //CallStatus.None,
                CallStatus.Planning,
                CallStatus.Ringing,
                CallStatus.Talking
                );
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Call> getLocked(int currentPage, int pageSize) {
        List<Call> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Call>) CallDAO.CreateInstance(s).getByCallStatus(currentPage, pageSize, CallStatus.Talking);
        tx.commit();
        s.close();

        return retValue;
    }
    public HashMap<Integer, Integer> countByAnswerHour(Date from, Date to) {
        HashMap<Integer, Integer> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = CallDAO.CreateInstance(s).countByAnswerHour(from, to);
        tx.commit();
        s.close();

        return retValue;
    }
    public List<Date[]> getContactAndReturnByDates(Date from, Date to) {
        List<Date[]> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = CallDAO.CreateInstance(s).getContactAndReturnByDates(from, to);
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Scalar">
    public int countAll() {
        int retValue = 0;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = CallDAO.CreateInstance(s).CountAll();
        tx.commit();
        s.close();

        return retValue;
    }
    public String getAverageTalkTime(Date date) {
        Calendar dateStart = Calendar.getInstance(), dateEnd = Calendar.getInstance();
        dateStart.setTime(date);
        dateEnd.setTime(date);

        dateStart.set(Calendar.HOUR_OF_DAY, 0);
        dateStart.set(Calendar.MINUTE, 0);
        dateStart.set(Calendar.SECOND, 0);
        dateStart.set(Calendar.MILLISECOND, 0);

        dateEnd.set(Calendar.HOUR_OF_DAY, 0);
        dateEnd.set(Calendar.MINUTE, 0);
        dateEnd.set(Calendar.SECOND, 0);
        dateEnd.set(Calendar.MILLISECOND, 0);
        dateEnd.set(Calendar.DATE, dateEnd.get(Calendar.DATE) + 1);

        return getAverageTalkTime(dateStart.getTime(), dateEnd.getTime());
    }
    public String getAverageTalkTime(Date from, Date to) {
        return getAverageTalkTime(getSuccessfulByDates(from, to));
    }
    public String getAverageTalkTime() {
        return getAverageTalkTime(getTalking());
    }
    public String getAverageTalkTime(List<Call> calls) {
        if (calls == null || calls.isEmpty()) {
            return "00\"";
        }
        double totalDuration = 0;
        for (Call c : calls) {
            totalDuration += c.getDuration();
        }
        return Utils.TimeDuration(totalDuration / calls.size());
    }

    public String getAverageWaitTime(Date date) {
        Calendar dateStart = Calendar.getInstance(), dateEnd = Calendar.getInstance();
        dateStart.setTime(date);
        dateEnd.setTime(date);

        dateStart.set(Calendar.HOUR_OF_DAY, 0);
        dateStart.set(Calendar.MINUTE, 0);
        dateStart.set(Calendar.SECOND, 0);
        dateStart.set(Calendar.MILLISECOND, 0);

        dateEnd.set(Calendar.HOUR_OF_DAY, 0);
        dateEnd.set(Calendar.MINUTE, 0);
        dateEnd.set(Calendar.SECOND, 0);
        dateEnd.set(Calendar.MILLISECOND, 0);
        dateEnd.set(Calendar.DATE, dateEnd.get(Calendar.DATE) + 1);

        return getAverageWaitTime(dateStart.getTime(), dateEnd.getTime());
    }
    public String getAverageWaitTime(Date from, Date to) {
        return getAverageWaitTime(getFirstCallbackByDates(from, to));
    }
    public String getAverageWaitTime() {
        return getAverageWaitTime(getTalking());
    }
    public String getAverageWaitTime(List<Call> calls) {
        if (calls == null || calls.isEmpty()) {
            return "00\"";
        }
        double totalWait = 0;
        for (Call c : calls) {
            totalWait += c.getWaitTime();
        }
        return Utils.TimeDuration(totalWait / calls.size());
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Save">
    @SuppressWarnings("unchecked")
    public Call setCall(Call call) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            call = ((CallDAO<Call>) CallDAO.CreateInstance(s)).setCall(call);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return call;
    }
    @SuppressWarnings("unchecked")
    public Call setCall(Call call, Agent agent, Queue queue, boolean saveAgent) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        CallDAO<Call> callDAO = (CallDAO<Call>) CallDAO.CreateInstance(s);
        AgentDAO<Agent> agentDAO = (AgentDAO<Agent>) AgentDAO.CreateInstance(s);
        QueueDAO<Queue> queueDAO = (QueueDAO<Queue>) QueueDAO.CreateInstance(s);
        try {
            call = callDAO.setCall(call);
            //agent.setCurrentCall(call);
            if (saveAgent) {
                Agent currAgent = agentDAO.getByID(agent.getUserID());
                currAgent.setLastCallTime(agent.getLastCallTime());
                agent = agentDAO.setAgent(currAgent);
            }
            queue = queueDAO.setQueue(queue);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        call.setAgent(agent);
        call.setQueue(queue);
        return call;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="FetchChild">
    public void FetchRecords(Call call) {
        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        s.lock(call, LockMode.NONE);
        org.hibernate.Hibernate.initialize(call.getRecords());
        tx.commit();
        s.close();
    }
    public void FetchRecords(List<Call> calls) {
        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        for (Call call : calls) {
            s.lock(call, LockMode.NONE);
            org.hibernate.Hibernate.initialize(call.getRecords());
        }
        tx.commit();
        s.close();
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Properties">
    public CallManagerService getCallManagerService() {
        return callManagerService;
    }

    public void setCallManagerService(CallManagerService callManagerService) {
        this.callManagerService = callManagerService;
    }
    // </editor-fold>
}
