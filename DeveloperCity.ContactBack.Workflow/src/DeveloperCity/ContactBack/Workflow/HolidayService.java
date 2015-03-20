package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.DataAccess.HibernateSession;
import DeveloperCity.ContactBack.DataAccess.HolidayDAO;
import DeveloperCity.ContactBack.DomainModel.Holiday;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class HolidayService {

    private static Logger logger = Logger.getLogger(HolidayService.class);

    public HolidayService() {
    }

    // <editor-fold defaultstate="collapsed" desc="Get">
    public Holiday getByID(long id) {
        Holiday retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = HolidayDAO.CreateInstance(s).getByID(id);
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="List">
    @SuppressWarnings("unchecked")
    public List<Holiday> getAll() {
        List<Holiday> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Holiday>) HolidayDAO.CreateInstance(s).getAll();
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Holiday> getAll(int currentPage, int pageSize) {
        List<Holiday> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Holiday>) HolidayDAO.CreateInstance(s).getAll(currentPage, pageSize);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Holiday> getFutureHolidays() {
        List<Holiday> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Holiday>) HolidayDAO.CreateInstance(s).getFutureHolidays();
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
        retValue = HolidayDAO.CreateInstance(s).CountAll();
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Save">
    @SuppressWarnings("unchecked")
    public Holiday setHoliday(Holiday holiday) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            holiday = ((HolidayDAO<Holiday>)HolidayDAO.CreateInstance(s)).setHoliday(holiday);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return holiday;
    }
    @SuppressWarnings("unchecked")
    public void deleteHoliday(long id) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            HolidayDAO<Holiday> dao = (HolidayDAO<Holiday>) HolidayDAO.CreateInstance(s);
            Holiday holiday = dao.getByID(id);
            if (holiday == null) { throw new Exception("Invalid holiday id to delete"); }
            dao.deleteHoliday(holiday);
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