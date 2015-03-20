package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.DataAccess.HibernateSession;
import DeveloperCity.ContactBack.DataAccess.PriorityDAO;
import DeveloperCity.ContactBack.DomainModel.Priority;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class PriorityService {

    private static Logger logger = Logger.getLogger(PriorityService.class);

    public PriorityService() {
    }

    // <editor-fold defaultstate="collapsed" desc="Get">
    public Priority getByID(long id) {
        Priority retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = PriorityDAO.CreateInstance(s).getByID(id);
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="List">
    @SuppressWarnings("unchecked")
    public List<Priority> getAll() {
        List<Priority> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Priority>) PriorityDAO.CreateInstance(s).getAll();
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Priority> getAll(int currentPage, int pageSize) {
        List<Priority> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Priority>) PriorityDAO.CreateInstance(s).getAll(currentPage, pageSize);
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
        retValue = PriorityDAO.CreateInstance(s).CountAll();
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Save">
    @SuppressWarnings("unchecked")
    public Priority setPriority(Priority priority) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            priority = ((PriorityDAO<Priority>) PriorityDAO.CreateInstance(s)).setPriority(priority);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return priority;
    }
    @SuppressWarnings("unchecked")
    public void deletePriority(long id) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            PriorityDAO<Priority> dao = (PriorityDAO<Priority>) PriorityDAO.CreateInstance(s);
            Priority priority = dao.getByID(id);
            if (priority == null) { throw new Exception("Invalid priority id to delete"); }
            dao.deletePriority(priority);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
    }
    // </editor-fold>
}
