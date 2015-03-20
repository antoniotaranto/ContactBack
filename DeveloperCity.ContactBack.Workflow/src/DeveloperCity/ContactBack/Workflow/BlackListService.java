package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.DataAccess.HibernateSession;
import DeveloperCity.ContactBack.DataAccess.BlackListDAO;
import DeveloperCity.ContactBack.DomainModel.BlackList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class BlackListService {

    private static Logger logger = Logger.getLogger(BlackListService.class);

    public BlackListService() {
    }

    // <editor-fold defaultstate="collapsed" desc="Get">
    public BlackList getByID(long id) {
        BlackList retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = BlackListDAO.CreateInstance(s).getByID(id);
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="List">
    @SuppressWarnings("unchecked")
    public List<BlackList> getAll() {
        List<BlackList> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<BlackList>) BlackListDAO.CreateInstance(s).getAll();
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<BlackList> getAll(int currentPage, int pageSize) {
        List<BlackList> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<BlackList>) BlackListDAO.CreateInstance(s).getAll(currentPage, pageSize);
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
        retValue = BlackListDAO.CreateInstance(s).CountAll();
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Save">
    @SuppressWarnings("unchecked")
    public BlackList setBlackList(BlackList blackList) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            blackList = ((BlackListDAO<BlackList>) BlackListDAO.CreateInstance(s)).setBlackList(blackList);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return blackList;
    }
    @SuppressWarnings("unchecked")
    public void deleteBlackList(long id) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            BlackListDAO<BlackList> dao = (BlackListDAO<BlackList>) BlackListDAO.CreateInstance(s);
            BlackList blackList = dao.getByID(id);
            if (blackList == null) { throw new Exception("Invalid black list id to delete"); }
            dao.deleteBlackList(blackList);
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
