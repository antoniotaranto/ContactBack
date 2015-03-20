package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.DataAccess.HibernateSession;
import DeveloperCity.ContactBack.DataAccess.WorkTimeDAO;
import DeveloperCity.ContactBack.DomainModel.Agent;
import DeveloperCity.ContactBack.DomainModel.WorkTime;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class WorkTimeService {

    private static Logger logger = Logger.getLogger(WorkTimeService.class);

    public WorkTimeService() {
    }

    // <editor-fold defaultstate="collapsed" desc="Get">
    public WorkTime getByID(long id) {
        WorkTime retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = WorkTimeDAO.CreateInstance(s).getByID(id);
        tx.commit();
        s.close();

        return retValue;
    }
    public WorkTime getByAgentUnfinished(Agent agent) {
        WorkTime retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = WorkTimeDAO.CreateInstance(s).getByAgentUnfinished(agent);
        tx.commit();
        s.close();

        return retValue;
    }
    public WorkTime getByAgentLastOne(Agent agent) {
        WorkTime retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = WorkTimeDAO.CreateInstance(s).getByAgentLastOne(agent);
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="List">
    @SuppressWarnings("unchecked")
    public List<WorkTime> getAll() {
        List<WorkTime> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<WorkTime>) WorkTimeDAO.CreateInstance(s).getAll();
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<WorkTime> getAll(int currentPage, int pageSize) {
        List<WorkTime> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<WorkTime>) WorkTimeDAO.CreateInstance(s).getAll(currentPage, pageSize);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<WorkTime> getByAgent(Agent agent) {
        List<WorkTime> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<WorkTime>) WorkTimeDAO.CreateInstance(s).getByAgent(agent);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<WorkTime> getByAgentAndDates(Agent agent, Date from, Date to) {
        List<WorkTime> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<WorkTime>) WorkTimeDAO.CreateInstance(s).getByAgentAndDates(agent, from, to);
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
        retValue = WorkTimeDAO.CreateInstance(s).CountAll();
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Save">
    @SuppressWarnings("unchecked")
    public WorkTime setWorkTime(WorkTime workTime) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            workTime = ((WorkTimeDAO<WorkTime>) WorkTimeDAO.CreateInstance(s)).setWorkTime(workTime);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return workTime;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="FetchChild">
    public void FetchBreaks(WorkTime workTime) {
        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        s.lock(workTime, LockMode.NONE);
        boolean i = workTime.getBreaks().isEmpty();
        tx.commit();
        s.close();
    }
    public void FetchBreaks(List<WorkTime> workTimes) {
        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        boolean i;
        for (WorkTime workTime : workTimes) {
            s.lock(workTime, LockMode.NONE);
            i = workTime.getBreaks().isEmpty();
        }
        tx.commit();
        s.close();
    }
    // </editor-fold>
}
