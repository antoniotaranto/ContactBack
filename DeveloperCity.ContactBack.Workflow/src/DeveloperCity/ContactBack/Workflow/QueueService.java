package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.DataAccess.HibernateSession;
import DeveloperCity.ContactBack.DataAccess.QueueDAO;
import DeveloperCity.ContactBack.DomainModel.*;
import DeveloperCity.ContactBack.DomainModel.IVR.PhoneNumber;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class QueueService {

    private static Logger logger = Logger.getLogger(QueueService.class);
    private QueueHistoryService sQueueHistory;

    public QueueService() {
        sQueueHistory = new QueueHistoryService();
    }

    // <editor-fold defaultstate="collapsed" desc="Get">
    public Queue getByID(long id) {
        Queue retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = QueueDAO.CreateInstance(s).getByID(id);
        tx.commit();
        s.close();

        return retValue;
    }
    public Queue getByIDIncludeDetails(long id) {
        Queue retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = QueueDAO.CreateInstance(s).getByIDIncludeDetails(id);
        tx.commit();
        s.close();

        return retValue;
    }
    public Queue getTopInQueueByCallBackNumber(String callBackNumber) {
        callBackNumber = PhoneNumber.fromBina(callBackNumber).getNumberToDial();
        Queue retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = QueueDAO.CreateInstance(s).getTopInQueueByCallBackNumber(callBackNumber);
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="List">
    @SuppressWarnings("unchecked")
    public List<Queue> getAll() {
        List<Queue> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Queue>) QueueDAO.CreateInstance(s).getAll();
        tx.commit();
        s.close();

        return retValue;
    }
    public int countInQueue() {
        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        int retValue = QueueDAO.CreateInstance(s).countInQueue();
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Queue> getAll(int currentPage, int pageSize) {
        List<Queue> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Queue>) QueueDAO.CreateInstance(s).getAll(currentPage, pageSize);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Queue> getInQueue() {
        List<Queue> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Queue>) QueueDAO.CreateInstance(s).getByQueueStatus(QueueStatus.InQueue);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Queue> getInQueueAndCurrentCall() {
        List<Queue> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Queue>) QueueDAO.CreateInstance(s).getByQueueStatus(QueueStatus.InQueue);
        for(Queue q : retValue) {
            if (q.getAttendCall() != null &&
                    q.getAttendCall().getCallID() != 0 &&
                    q.getAttendCall().getAgent() != null){
                String a = q.getAttendCall().getAgent().getUsername();
            }
        }
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Queue> getInQueue(int currentPage, int pageSize) {
        List<Queue> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Queue>) QueueDAO.CreateInstance(s).getByQueueStatus(currentPage, pageSize, QueueStatus.InQueue);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Queue> getInQueueByCallBackNumber(String callBackNumber) {
        callBackNumber = PhoneNumber.fromBina(callBackNumber).getNumberToDial();
        List<Queue> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Queue>) QueueDAO.CreateInstance(s).getByQueueStatusAndCallBackNumber(callBackNumber, QueueStatus.InQueue);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Queue> getInQueueByCallBackNumber(int currentPage, int pageSize, String callBackNumber) {
        callBackNumber = PhoneNumber.fromBina(callBackNumber).getNumberToDial();
        List<Queue> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Queue>) QueueDAO.CreateInstance(s).getByQueueStatusAndCallBackNumber(callBackNumber, currentPage, pageSize, QueueStatus.InQueue);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Queue> getAllByDates(Date from, Date to) {
        List<Queue> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Queue>) QueueDAO.CreateInstance(s).getByQueueStatusAndDates(from, to, null, QueueDAO.CallTime.asc());
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Queue> getAllByDates(Date from, Date to, int maxTries) {
        List<Queue> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Queue>) QueueDAO.CreateInstance(s).getByQueueStatusAndDates(from, to);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Queue> getByCallBackID(String callBackNumber) {
        List<Queue> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Queue>) QueueDAO.CreateInstance(s).getByQueueStatusAndCallBackNumber(callBackNumber);
        tx.commit();
        s.close();

        return retValue;
    }
    public HashMap<Integer, Integer> countByCallHour(Date from, Date to) {
        HashMap<Integer, Integer> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = QueueDAO.CreateInstance(s).countByCallHour(from, to);
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
        retValue = QueueDAO.CreateInstance(s).CountAll();
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Save">
    @SuppressWarnings("unchecked")
    public Queue setQueue(Queue queue) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            queue = ((QueueDAO<Queue>) QueueDAO.CreateInstance(s)).setQueue(queue);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return queue;
    }
    public Queue setQueueCallingAgain(Queue oldQueue, long callManagerCallID) throws Exception {
        Date now = new Date();

        QueueHistory queueHistory = new QueueHistory();
        queueHistory.setCallTime(now);
        queueHistory.setQueue(oldQueue);
        queueHistory = sQueueHistory.setQueueHistory(queueHistory);

        return oldQueue;
    }
    public Queue setQueueFirstTime(long callManagerCallID, String binaNumber, boolean validBinaNumber, String callBackNumber, CallBackNumberType callBackNumberType, Date EstimatedTimeToAttend, int entryPosition, short PriorityValue, Long customerID) throws Exception {
        Date now = new Date();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.SECOND, 20);

        Queue newQueue = (customerID == null || customerID == 0) ? new Queue() : new WebQueue();
        newQueue.setAttendCount(0);
        newQueue.setCallManagerCallID(callManagerCallID);
        newQueue.setCallTime(now);
        newQueue.setDontCallBefore(cal.getTime());
        newQueue.setBinaNumber(binaNumber == null ? "?" : binaNumber);
        newQueue.setValidBinaNumber(validBinaNumber);
        newQueue.setCallBackNumber(callBackNumber);
        newQueue.setCallBackNumberType(callBackNumberType);
        newQueue.setQueueStatus(QueueStatus.InQueue);
        newQueue.setScheduleTime(cal.getTime());
        newQueue.setEstimatedTimeToAttend(EstimatedTimeToAttend);
        newQueue.setEntryPosition(entryPosition);
        newQueue.setPriorityValue(PriorityValue);
        if (newQueue instanceof WebQueue) {
            Customer customer = new CustomerService().getByID(customerID.longValue());
            if (customer == null || customer.getUserStatus() != UserStatus.Active) {
                throw new Exception ("User don't have access to this operation");
            }
            ((WebQueue)newQueue).setCustomer( customer );
        }
        newQueue = setQueue(newQueue);

        QueueHistory queueHistory = new QueueHistory();
        queueHistory.setCallTime(now);
        queueHistory.setQueue(newQueue);
        queueHistory = sQueueHistory.setQueueHistory(queueHistory);

        return newQueue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="FetchChild">
    public void FetchAttendCall(Queue queue) {
        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        s.lock(queue, LockMode.NONE);
        org.hibernate.Hibernate.initialize(queue.getAttendCall());
        tx.commit();
        s.close();
    }
    public void FetchAttendCall(List<Queue> queue) {
        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        for (Queue q : queue) {
            s.lock(q, LockMode.NONE);
            org.hibernate.Hibernate.initialize(q.getAttendCall());
        }
        tx.commit();
        s.close();
    }
    public void FetchHistory(Queue queue) {
        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        s.lock(queue, LockMode.NONE);
        org.hibernate.Hibernate.initialize(queue.getHistory());
        tx.commit();
        s.close();
    }
    public void FetchHistory(List<Queue> queue) {
        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        for (Queue q : queue) {
            s.lock(q, LockMode.NONE);
            org.hibernate.Hibernate.initialize(q.getHistory());
        }
        tx.commit();
        s.close();
    }
    public void FetchCalls(Queue queue) {
        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        s.lock(queue, LockMode.NONE);
        org.hibernate.Hibernate.initialize(queue.getCalls());
        tx.commit();
        s.close();
    }
    public void FetchCalls(List<Queue> queue) {
        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        for (Queue q : queue) {
            s.lock(q, LockMode.NONE);
            org.hibernate.Hibernate.initialize(q.getCalls());
        }
        tx.commit();
        s.close();
    }
    // </editor-fold>
}
