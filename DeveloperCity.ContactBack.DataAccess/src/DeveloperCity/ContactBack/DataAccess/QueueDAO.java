/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DataAccess;

import DeveloperCity.DataAccess.DAOGeneric;
import DeveloperCity.ContactBack.DomainModel.Queue;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.type.Type;

public class QueueDAO<T extends Queue> extends DAOGeneric<T> {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private static Logger logger = Logger.getLogger(QueueDAO.class);
    public static final Property QueueID = Property.forName("QueueID");
    public static final Property BinaNumber = Property.forName("BinaNumber");
    public static final Property ValidBinaNumber = Property.forName("ValidBinaNumber");
    public static final Property CallBackNumber = Property.forName("CallBackNumber");
    public static final Property CallBackNumberType = Property.forName("CallBackNumberType");
    public static final Property CallTime = Property.forName("CallTime");
    public static final Property DontCallBefore = Property.forName("DontCallBefore");
    public static final Property ScheduleTime = Property.forName("ScheduleTime");
    public static final Property EstimatedTimeToAttend = Property.forName("EstimatedTimeToAttend");
    public static final Property EntryPosition = Property.forName("EntryPosition");
    public static final Property AttendCall = Property.forName("AttendCall");
    public static final Property QueueStatus = Property.forName("QueueStatus");
    public static final Property CallManagerCallID = Property.forName("CallManagerCallID");
    public static final Property AttendCount = Property.forName("AttendCount");
    public static final Property PriorityValue = Property.forName("PriorityValue");
    public static final Property Calls = Property.forName("Calls");
    public static final Property History = Property.forName("History");
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Enum">
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor and Override">
    @Override
    protected Order[] defaultOrder() {
        return new Order[] { ScheduleTime.asc() };
    }

    protected QueueDAO(Session session, Class<T> type) {
        super(session, type);
    }

    public static QueueDAO<? extends Queue> CreateInstance(Session session) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("CreateInstance(Session session)"));
        }
        QueueDAO<Queue> retValue = new QueueDAO<Queue>(session, Queue.class);
        if (logger.isInfoEnabled()) {
            logger.info(String.format("CreateInstance(Session session) !"));
        }
        return retValue;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Get Individual">
    public int countInQueue() {
        if (logger.isInfoEnabled()) {
            logger.info("countInQueue()");
        }
        Criteria query = getCriteria().add(QueueStatus.eq(DeveloperCity.ContactBack.DomainModel.QueueStatus.InQueue));
        query.setProjection(Projections.rowCount());
        int retValue = 0;
        try {
            retValue = getScalar(query, Integer.class);
        } catch (Exception e) {
            logger.error("countInQueue Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("countInQueue() !");
        }
        return retValue;
    }
    public T getByID(long id) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByID(%d)", id));
        }
        Criteria query = getCriteria().add(QueueID.eq(id));
        T retValue = null;
        try {
            retValue = getIndividual(query);
        } catch (Exception e) {
            logger.error("getByID Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByID(long) !");
        }
        return retValue;
    }
    public T getByIDIncludeDetails(long id) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByID(%d)", id));
        }
        Criteria query = getCriteria()
                .add(QueueID.eq(id));
        T retValue = null;
        try {
            retValue = getIndividual(query);
            Object o = retValue.getCalls().toArray();
            o = retValue.getHistory().toArray();
        } catch (Exception e) {
            logger.error("getByID Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByID(long) !");
        }
        return retValue;
    }
    public T getTopInQueueByCallBackNumber(String callbackNumber) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getTopInQueueByCallBackNumber(%s)", callbackNumber));
        }
        Criteria query = getCriteria()
                .add( CallBackNumber.eq(callbackNumber) )
                .add( QueueStatus.eq( DeveloperCity.ContactBack.DomainModel.QueueStatus.InQueue ) )
                .setMaxResults( 1 )
                .setFetchSize( 1 );
        T retValue = null;
        try {
            retValue = getIndividual(query);
        } catch (Exception e) {
            logger.error("getTopInQueueByCallBackNumber Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getTopInQueueByCallBackNumber(String) !");
        }
        return retValue;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Get Collection">
    public List<T> getByQueueStatus(DeveloperCity.ContactBack.DomainModel.QueueStatus... status) {
        return getByQueueStatus(status, defaultOrder());
    }
    public List<T> getByQueueStatus(DeveloperCity.ContactBack.DomainModel.QueueStatus[] status, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByQueueStatus(%s, %s)", status.toString(), order.toString()));
        }
        Criteria query = getCriteria()
                .add( QueueStatus.in(status) );
        if (order != null) {
            for (Order o : order) {
                query = query.addOrder(o);
            }
        }
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
        } catch (Exception e) {
            logger.error("getByQueueStatus Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByQueueStatus(QueueStatus[], Order...) !");
        }
        return retValue;
    }
    public List<T> getByQueueStatus(int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.QueueStatus... status) {
        return getByQueueStatus(currentPage, pageSize, status, defaultOrder());
    }
    public List<T> getByQueueStatus(int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.QueueStatus[] status, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByQueueStatus(%d, %d, %s, %s)", currentPage, pageSize, status.toString(), order.toString()));
        }
        Criteria query = getCriteria()
                .add(QueueStatus.in(status));
        query = query
                .setFetchSize(pageSize)
                .setMaxResults(pageSize)
                .setFirstResult(currentPage * pageSize);
        if (order != null) {
            for (Order o : order) {
                query = query.addOrder(o);
            }
        }
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
        } catch (Exception e) {
            logger.error("getByQueueStatus Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByQueueStatus(int, int, QueueStatus[], Order...) !");
        }
        return retValue;
    }

    public List<T> getByNotQueueStatus(DeveloperCity.ContactBack.DomainModel.QueueStatus... status) {
        return getByNotQueueStatus(status, defaultOrder());
    }
    public List<T> getByNotQueueStatus(DeveloperCity.ContactBack.DomainModel.QueueStatus[] status, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByNotQueueStatus(%s, %s)", status.toString(), order.toString()));
        }
        Criteria query = getCriteria();
        for (DeveloperCity.ContactBack.DomainModel.QueueStatus s : status) {
            query = query
                .add( QueueStatus.ne(s) );
        }
        if (order != null) {
            for (Order o : order) {
                query = query.addOrder(o);
            }
        }
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
        } catch (Exception e) {
            logger.error("getByNotQueueStatus Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByNotQueueStatus(QueueStatus[], Order...) !");
        }
        return retValue;
    }
    public List<T> getByNotQueueStatus(int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.QueueStatus... status) {
        return getByNotQueueStatus(currentPage, pageSize, status, defaultOrder());
    }
    public List<T> getByNotQueueStatus(int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.QueueStatus[] status, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByNotQueueStatus(%d, %d, %s, %s)", currentPage, pageSize, status.toString(), order.toString()));
        }
        Criteria query = getCriteria();
        for (DeveloperCity.ContactBack.DomainModel.QueueStatus s : status) {
            query = query
                .add( QueueStatus.ne(s) );
        }
        query = query
                .setFetchSize(pageSize)
                .setMaxResults(pageSize)
                .setFirstResult(currentPage * pageSize);
        if (order != null) {
            for (Order o : order) {
                query = query.addOrder(o);
            }
        }
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
        } catch (Exception e) {
            logger.error("getByNotQueueStatus Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByNotQueueStatus(int, int, QueueStatus[], Order...) !");
        }
        return retValue;
    }

    public List<T> getByQueueStatusAndDates(Date from, Date to, DeveloperCity.ContactBack.DomainModel.QueueStatus... status) {
        return getByQueueStatusAndDates(from, to, status, defaultOrder());
    }
    public List<T> getByQueueStatusAndDates(Date from, Date to, DeveloperCity.ContactBack.DomainModel.QueueStatus[] status, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByQueueStatusAndDates(%s, %s, %s, %s)", from, to, status != null ? status.toString() : "null", order.toString()));
        }
        Criteria query = getCriteria();

        if (status != null && status.length > 0) {
            query = query.add(QueueStatus.in(status));
        }
        if (from != null && to != null) {
            query = query.add(CallTime.between(from, to));
        }
        else if (from != null) {
            query = query.add(CallTime.ge(from));
        }
        else if (to != null) {
            query = query.add(CallTime.le(to));
        }

        if (order != null) {
            for (Order o : order) {
                query = query.addOrder(o);
            }
        }
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
        } catch (Exception e) {
            logger.error("getByQueueStatusAndDates Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByQueueStatusAndDates(Date, Date, QueueStatus[], Order...) !");
        }
        return retValue;
    }
    public List<T> getByQueueStatusAndDates(Date from, Date to, int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.QueueStatus... status) {
        return getByQueueStatusAndDates(from, to, currentPage, pageSize, status, defaultOrder());
    }
    @SuppressWarnings("unchecked")
    public HashMap<Integer, Integer> countByCallHour(Date from, Date to) {
        DetachedCriteria query = DetachedCriteria.forClass(Queue.class)
                .add( Property.forName("CallTime").isNotNull())
                .add( Property.forName("CallTime").between(from, to))
                .setProjection( Projections.projectionList()
                    .add( Projections.rowCount(), "countCalls" )
                    .add( Projections.sqlGroupProjection("hour({alias}.CallTime) as hour", "hour({alias}.CallTime)", new String[]{"hour"}, new Type[]{Hibernate.INTEGER} ) )
                );
        List<Object[]> results = null;
        HashMap<Integer, Integer> retValue = new HashMap<Integer, Integer>();
        try {
            results = (List<Object[]>) query.getExecutableCriteria(session).list();
        } catch(Exception e) {
            logger.error("countByCallHour Error", e);
        }
        for (Object[] o : results) {
            Object[] item = o;
            Integer hour = (Integer) item[1];
            Integer count = (Integer) item[0];
            if (hour != null && count != null) {
                retValue.put(hour, count);
            }
        }

        return retValue;
    }
    public List<T> getByQueueStatusAndDates(Date from, Date to, int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.QueueStatus[] status, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByQueueStatusAndDates(%s, %s, %d, %d, %s, %s)", from, to, currentPage, pageSize, status != null ? status.toString() : "null", order.toString()));
        }
        Criteria query = getCriteria();

        if (status != null && status.length > 0) {
            query = query.add(QueueStatus.in(status));
        }

        if (from != null && to != null) {
            query = query.add(CallTime.between(from, to));
        }
        else if (from != null) {
            query = query.add(CallTime.ge(from));
        }
        else if (to != null) {
            query = query.add(CallTime.le(to));
        }
        
        query = query
                .setFetchSize(pageSize)
                .setMaxResults(pageSize)
                .setFirstResult(currentPage * pageSize);
        if (order != null) {
            for (Order o : order) {
                query = query.addOrder(o);
            }
        }
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
        } catch (Exception e) {
            logger.error("getByQueueStatusAndDates Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByQueueStatusAndDates(Date, Date, int, int, QueueStatus[], Order...) !");
        }
        return retValue;
    }

    public List<T> getByQueueStatusAndCallBackNumber(String callBackNumber, DeveloperCity.ContactBack.DomainModel.QueueStatus... status) {
        return getByQueueStatusAndCallBackNumber(callBackNumber, status, defaultOrder());
    }
    public List<T> getByQueueStatusAndCallBackNumber(String callBackNumber, DeveloperCity.ContactBack.DomainModel.QueueStatus[] status, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByQueueStatusAndCallBackNumber(%s, %s, %s)", callBackNumber, status.toString(), order.toString()));
        }
        Criteria query = getCriteria()
                .add(CallBackNumber.eq(callBackNumber))
                .add(QueueStatus.in(status));
        if (order != null) {
            for (Order o : order) {
                query = query.addOrder(o);
            }
        }
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
        } catch (Exception e) {
            logger.error("getByQueueStatusAndCallBackNumber Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByQueueStatusAndCallBackNumber(String, QueueStatus[], Order...) !");
        }
        return retValue;
    }
    public List<T> getByQueueStatusAndCallBackNumber(String callBackNumber, int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.QueueStatus... status) {
        return getByQueueStatusAndCallBackNumber(callBackNumber, currentPage, pageSize, status, defaultOrder());
    }
    public List<T> getByQueueStatusAndCallBackNumber(String callBackNumber, int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.QueueStatus[] status, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByQueueStatusAndCallBackNumber(%s, %d, %d, %s, %s)", callBackNumber, currentPage, pageSize, status.toString(), order.toString()));
        }
        Criteria query = getCriteria()
                .add(CallBackNumber.eq(callBackNumber))
                .add(QueueStatus.in(status));
        query = query
                .setFetchSize(pageSize)
                .setMaxResults(pageSize)
                .setFirstResult(currentPage * pageSize);
        if (order != null) {
            for (Order o : order) {
                query = query.addOrder(o);
            }
        }
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
        } catch (Exception e) {
            logger.error("getByQueueStatusAndCallBackNumber Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByQueueStatusAndCallBackNumber(String, int, int, QueueStatus[], Order...) !");
        }
        return retValue;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Change">
    public T setQueue(T item) throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("set(%s)", item));
        }
        try {
            super.session.saveOrUpdate(item);
        } catch (Exception e) {
            logger.error("set Error", e);
            throw e;
        }
        if (logger.isInfoEnabled()) {
            logger.info("set(T) !");
        }
        return item;
    }
    // </editor-fold>
}