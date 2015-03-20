/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.DataAccess.HibernateSession;
import DeveloperCity.ContactBack.DataAccess.ChartDAO;
import DeveloperCity.ContactBack.DataAccess.UserChartDAO;
import DeveloperCity.ContactBack.DataAccess.UserDAO;
import DeveloperCity.ContactBack.DomainModel.Chart;
import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.DomainModel.UserChart;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserChartService {

    private static Logger logger = Logger.getLogger(UserChartService.class);

    public UserChartService() {
    }

    // <editor-fold defaultstate="collapsed" desc="Get">
    public UserChart getByID(long id) {
        UserChart retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = UserChartDAO.CreateInstance(s).getByID(id);
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="List">
    @SuppressWarnings("unchecked")
    public List<UserChart> getAll() {
        List<UserChart> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<UserChart>) UserChartDAO.CreateInstance(s).getAll();
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<UserChart> getByUser(User user) {
        List<UserChart> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<UserChart>) UserChartDAO.CreateInstance(s).getByUser(user);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<UserChart> getAll(int currentPage, int pageSize) {
        List<UserChart> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<UserChart>) UserChartDAO.CreateInstance(s).getAll(currentPage, pageSize);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<UserChart> getByUserID(long userID) {
        List<UserChart> retValue = new ArrayList<UserChart>();

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();

        User u = null;
        if (userID > 0) {
            u = UserDAO.CreateInstance(s).getByID(userID);
            for (UserChart up : u.getCharts()) {
                String a = up.getChart().getChartDescription();
                retValue.add(up);
            }
        } else { u = new User(); }

        List<Chart> charts = (List<Chart>) ChartDAO.CreateInstance(s).getAll();
        for (Chart m : charts) {
            boolean hasAccess = false;
            for (UserChart up : retValue) {
                if (up.getChart().getChartID() == m.getChartID()) {
                    hasAccess = true;
                    break;
                }
            }
            if (!hasAccess) {
                retValue.add(new UserChart(0, u, m, false));
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
        retValue = UserChartDAO.CreateInstance(s).CountAll();
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Save">
    @SuppressWarnings("unchecked")
    public UserChart setUserChart(UserChart userChart) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            userChart = ((UserChartDAO<UserChart>) UserChartDAO.CreateInstance(s)).setUserChart(userChart);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return userChart;
    }
    @SuppressWarnings("unchecked")
    public List<UserChart> setUserChart(Long userID, List<Long> charts) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        List<UserChart> retValue = null;
        try {
            List<UserChart> removed = new ArrayList<UserChart>();
            User u = UserDAO.CreateInstance(s).getByID(userID);
            ChartDAO<Chart> mDao = (ChartDAO<Chart>) ChartDAO.CreateInstance(s);
            List<Long> notChangedIds = new ArrayList<Long>();
            UserChartDAO<UserChart> daoUP = (UserChartDAO<UserChart>) UserChartDAO.CreateInstance(s);
            retValue = daoUP.getByUserID(userID.longValue());
            for(UserChart p : retValue) {
                if (!charts.contains(p.getChart().getChartID())) {
                    removed.add(p);
                    s.delete(p);
                } else {
                    notChangedIds.add( charts.get(charts.indexOf(p.getChart().getChartID())) );
                }
            }
            retValue.removeAll(removed);
            if (charts.size() > 0) {
                for(long m : charts) {
                    if (m > 0 && !notChangedIds.contains(m)) {
                        Chart newMod = mDao.getByID(m);
                        UserChart up = new UserChart();
                        up.setChart(newMod);
                        up.setUser(u);
                        up = daoUP.setUserChart(up);
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