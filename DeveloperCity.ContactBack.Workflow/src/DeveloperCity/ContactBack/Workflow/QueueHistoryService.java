package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.DataAccess.HibernateSession;
import DeveloperCity.ContactBack.DataAccess.QueueHistoryDAO;
import DeveloperCity.ContactBack.DomainModel.QueueHistory;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class QueueHistoryService {

    private static Logger logger = Logger.getLogger(QueueHistoryService.class);

    public QueueHistoryService() {
    }

    // <editor-fold defaultstate="collapsed" desc="Get">
    public QueueHistory getByID(long id) {
        QueueHistory retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = QueueHistoryDAO.CreateInstance(s).getByID(id);
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="List">
    @SuppressWarnings("unchecked")
    public List<QueueHistory> getAll() {
        List<QueueHistory> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<QueueHistory>) QueueHistoryDAO.CreateInstance(s).getAll();
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<QueueHistory> getAll(int currentPage, int pageSize) {
        List<QueueHistory> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<QueueHistory>) QueueHistoryDAO.CreateInstance(s).getAll(currentPage, pageSize);
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
        retValue = QueueHistoryDAO.CreateInstance(s).CountAll();
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Save">
    @SuppressWarnings("unchecked")
    public QueueHistory setQueueHistory(QueueHistory queueHistory) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            queueHistory = ((QueueHistoryDAO<QueueHistory>) QueueHistoryDAO.CreateInstance(s)).setQueueHistory(queueHistory);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return queueHistory;
    }
    // </editor-fold>
}