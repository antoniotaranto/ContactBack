package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.DataAccess.HibernateSession;
import DeveloperCity.ContactBack.DataAccess.BreakTypeDAO;
import DeveloperCity.ContactBack.DomainModel.BreakType;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class BreakTypeService {

    private static Logger logger = Logger.getLogger(BreakTypeService.class);

    public BreakTypeService() {
    }

    // <editor-fold defaultstate="collapsed" desc="Get">
    public BreakType getByID(long id) {
        BreakType retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = BreakTypeDAO.CreateInstance(s).getByID(id);
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="List">
    @SuppressWarnings("unchecked")
    public List<BreakType> getAllButSystemTypes() {
        List<BreakType> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<BreakType>) BreakTypeDAO.CreateInstance(s).getAllButSystemTypes();
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<BreakType> getAll() {
        List<BreakType> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<BreakType>) BreakTypeDAO.CreateInstance(s).getAll();
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<BreakType> getAll(int currentPage, int pageSize) {
        List<BreakType> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<BreakType>) BreakTypeDAO.CreateInstance(s).getAll(currentPage, pageSize);
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
        retValue = BreakTypeDAO.CreateInstance(s).CountAll();
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Save">
    @SuppressWarnings("unchecked")
    public BreakType setBreakType(BreakType breakType) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            breakType = ((BreakTypeDAO<BreakType>)BreakTypeDAO.CreateInstance(s)).setBreakType(breakType);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return breakType;
    }
    // </editor-fold>
}