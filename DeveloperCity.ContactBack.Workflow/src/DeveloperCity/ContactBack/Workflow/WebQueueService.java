package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.DataAccess.HibernateSession;
import DeveloperCity.ContactBack.DataAccess.WebQueueDAO;
import DeveloperCity.ContactBack.DomainModel.IVR.PhoneNumber;
import DeveloperCity.ContactBack.DomainModel.WebQueue;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class WebQueueService {

    private static Logger logger = Logger.getLogger(WebQueueService.class);
    private QueueHistoryService sQueueHistory;

    public WebQueueService() {
        sQueueHistory = new QueueHistoryService();
    }

    // <editor-fold defaultstate="collapsed" desc="Get">
    public WebQueue getByID(long id) {
        WebQueue retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = WebQueueDAO.CreateInstance(s).getByID(id);
        tx.commit();
        s.close();

        return retValue;
    }
    public WebQueue getByCustomer(long userId) {
        WebQueue retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = WebQueueDAO.CreateInstance(s).getByCustomer(userId);
        tx.commit();
        s.close();

        return retValue;
    }
    public WebQueue getByIDIncludeDetails(long id) {
        WebQueue retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = WebQueueDAO.CreateInstance(s).getByIDIncludeDetails(id);
        tx.commit();
        s.close();

        return retValue;
    }
    public WebQueue getTopInWebQueueByCallBackNumber(String callBackNumber) {
        callBackNumber = PhoneNumber.fromBina(callBackNumber).getNumberToDial();
        WebQueue retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = WebQueueDAO.CreateInstance(s).getTopInQueueByCallBackNumber(callBackNumber);
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="List">
    @SuppressWarnings("unchecked")
    public List<WebQueue> getAll() {
        List<WebQueue> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<WebQueue>) WebQueueDAO.CreateInstance(s).getAll();
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<WebQueue> getAll(int currentPage, int pageSize) {
        List<WebQueue> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<WebQueue>) WebQueueDAO.CreateInstance(s).getAll(currentPage, pageSize);
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Scalar">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Save">
    @SuppressWarnings("unchecked")
    public WebQueue setWebQueue(WebQueue queue) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            queue = ((WebQueueDAO<WebQueue>) WebQueueDAO.CreateInstance(s)).setQueue(queue);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return queue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="FetchChild">
    // </editor-fold>
}
