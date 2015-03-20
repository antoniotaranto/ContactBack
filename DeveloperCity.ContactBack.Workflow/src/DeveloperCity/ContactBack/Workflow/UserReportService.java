/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.DataAccess.HibernateSession;
import DeveloperCity.ContactBack.DataAccess.ReportDAO;
import DeveloperCity.ContactBack.DataAccess.UserDAO;
import DeveloperCity.ContactBack.DataAccess.UserReportDAO;
import DeveloperCity.ContactBack.DomainModel.Report;
import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.DomainModel.UserReport;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserReportService {

    private static Logger logger = Logger.getLogger(UserReportService.class);

    public UserReportService() {
    }

    // <editor-fold defaultstate="collapsed" desc="Get">
    public UserReport getByID(long id) {
        UserReport retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = UserReportDAO.CreateInstance(s).getByID(id);
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="List">
    @SuppressWarnings("unchecked")
    public List<UserReport> getAll() {
        List<UserReport> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<UserReport>) UserReportDAO.CreateInstance(s).getAll();
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<UserReport> getByUser(User user) {
        List<UserReport> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<UserReport>) UserReportDAO.CreateInstance(s).getByUser(user);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<UserReport> getAll(int currentPage, int pageSize) {
        List<UserReport> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<UserReport>) UserReportDAO.CreateInstance(s).getAll(currentPage, pageSize);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<UserReport> getByUserID(long userID) {
        List<UserReport> retValue = new ArrayList<UserReport>();

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();

        User u = null;
        if (userID > 0) {
            u = UserDAO.CreateInstance(s).getByID(userID);
            for (UserReport up : u.getReports()) {
                String a = up.getReport().getReportDescription();
                retValue.add(up);
            }
        } else { u = new User(); }

        List<Report> reports = (List<Report>) ReportDAO.CreateInstance(s).getAll();
        for (Report m : reports) {
            boolean hasAccess = false;
            for (UserReport up : retValue) {
                if (up.getReport().getReportID() == m.getReportID()) {
                    hasAccess = true;
                    break;
                }
            }
            if (!hasAccess) {
                retValue.add(new UserReport(0, u, m, false));
            }
        }

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
        retValue = UserReportDAO.CreateInstance(s).CountAll();
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Save">
    @SuppressWarnings("unchecked")
    public UserReport setUserReport(UserReport userReport) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            userReport = ((UserReportDAO<UserReport>) UserReportDAO.CreateInstance(s)).setUserReport(userReport);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return userReport;
    }
    @SuppressWarnings("unchecked")
    public List<UserReport> setUserReport(Long userID, List<Long> reports) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        List<UserReport> retValue = null;
        try {
            List<UserReport> removed = new ArrayList<UserReport>();
            User u = UserDAO.CreateInstance(s).getByID(userID);
            ReportDAO<Report> mDao = (ReportDAO<Report>) ReportDAO.CreateInstance(s);
            List<Long> notChangedIds = new ArrayList<Long>();
            UserReportDAO<UserReport> daoUP = (UserReportDAO<UserReport>) UserReportDAO.CreateInstance(s);
            retValue = daoUP.getByUserID(userID.longValue());
            for(UserReport p : retValue) {
                if (!reports.contains(p.getReport().getReportID())) {
                    removed.add(p);
                    s.delete(p);
                } else {
                    notChangedIds.add( reports.get(reports.indexOf(p.getReport().getReportID())) );
                }
            }
            retValue.removeAll(removed);
            if (reports.size() > 0) {
                for(long m : reports) {
                    if (m > 0 && !notChangedIds.contains(m)) {
                        Report newMod = mDao.getByID(m);
                        UserReport up = new UserReport();
                        up.setReport(newMod);
                        up.setUser(u);
                        up = daoUP.setUserReport(up);
                        retValue.add(up);
                    }
                }
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return retValue;
    }
    // </editor-fold>
}