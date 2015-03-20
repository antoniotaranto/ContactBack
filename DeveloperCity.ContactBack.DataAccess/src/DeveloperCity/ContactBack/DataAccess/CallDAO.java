/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DataAccess;

import DeveloperCity.DataAccess.DAOGeneric;
import DeveloperCity.ContactBack.DomainModel.Call;
import DeveloperCity.ContactBack.DomainModel.QueueStatus;
import java.util.ArrayList;
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

public class CallDAO<T extends Call> extends DAOGeneric<T> {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private static Logger logger = Logger.getLogger(CallDAO.class);
    public static final Property CallID = Property.forName("CallID");
    public static final Property Queue = Property.forName("Queue");
    public static final Property StartTime = Property.forName("StartTime");
    public static final Property AnswerTime = Property.forName("AnswerTime");
    public static final Property EndTime = Property.forName("EndTime");
    public static final Property CallStatus = Property.forName("CallStatus");
    public static final Property CallManagerCallID = Property.forName("CallManagerCallID");
    public static final Property Agent = Property.forName("Agent");
    public static final Property Records = Property.forName("Records");
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Enum">
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor and Override">
    @Override
    protected Order[] defaultOrder() {
        return new Order[] { StartTime.asc() };
    }

    protected CallDAO(Session session, Class<T> type) {
        super(session, type);
    }

    public static CallDAO<? extends Call> CreateInstance(Session session) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("CreateInstance(Session session)"));
        }
        CallDAO<Call> retValue = new CallDAO<Call>(session, Call.class);
        if (logger.isInfoEnabled()) {
            logger.info(String.format("CreateInstance(Session session) !"));
        }
        return retValue;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Get Individual">
    public T getByID(long id) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByID(%d)", id));
        }
        Criteria query = getCriteria().add(CallID.eq(id));
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Get Collection">
    public List<T> getByCallStatus(DeveloperCity.ContactBack.DomainModel.CallStatus... status) {
        return getByCallStatus(status, defaultOrder());
    }
    public List<T> getByCallStatus(DeveloperCity.ContactBack.DomainModel.CallStatus[] status, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByCallStatus(%s, %s)", status.toString(), order.toString()));
        }
        Criteria query = getCriteria()
                .add( CallStatus.in(status) );
        if (order != null) {
            for (Order o : order) {
                query = query.addOrder(o);
            }
        }
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
        } catch (Exception e) {
            logger.error("getByCallStatus Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByCallStatus(CallStatus[], Order...) !");
        }
        return retValue;
    }
    public List<T> getByCallStatus(int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.CallStatus... status) {
        return getByCallStatus(currentPage, pageSize, status, defaultOrder());
    }
    public List<T> getByCallStatus(int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.CallStatus[] status, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByCallStatus(%d, %d, %s, %s)", currentPage, pageSize, status.toString(), order.toString()));
        }
        Criteria query = getCriteria()
                .add(CallStatus.in(status));
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
            logger.error("getByCallStatus Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByCallStatus(int, int, CallStatus[], Order...) !");
        }
        return retValue;
    }

    public List<T> getByCallStatusAndNotFinished(DeveloperCity.ContactBack.DomainModel.CallStatus... status) {
        return getByCallStatusAndNotFinished(status, defaultOrder());
    }
    public List<T> getByCallStatusAndNotFinished(DeveloperCity.ContactBack.DomainModel.CallStatus[] status, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByCallStatusAndNotFinished(%s, %s)", status.toString(), order.toString()));
        }
        Criteria query = getCriteria()
                .add( EndTime.isNull() )
                .add( CallStatus.in(status) );
        if (order != null) {
            for (Order o : order) {
                query = query.addOrder(o);
            }
        }
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
        } catch (Exception e) {
            logger.error("getByCallStatusAndNotFinished Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByCallStatusAndNotFinished(CallStatus[], Order...) !");
        }
        return retValue;
    }
    public List<T> getByCallStatusAndNotFinished(int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.CallStatus... status) {
        return getByCallStatusAndNotFinished(currentPage, pageSize, status, defaultOrder());
    }
    public List<T> getByCallStatusAndNotFinished(int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.CallStatus[] status, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByCallStatusAndNotFinished(%d, %d, %s, %s)", currentPage, pageSize, status.toString(), order.toString()));
        }
        Criteria query = getCriteria()
                .add( EndTime.isNull() )
                .add(CallStatus.in(status));
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
            logger.error("getByCallStatusAndNotFinished Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByCallStatusAndNotFinished(int, int, CallStatus[], Order...) !");
        }
        return retValue;
    }

    public List<T> getByNotCallStatus(DeveloperCity.ContactBack.DomainModel.CallStatus... status) {
        return getByNotCallStatus(status, defaultOrder());
    }
    public List<T> getByNotCallStatus(DeveloperCity.ContactBack.DomainModel.CallStatus[] status, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByNotCallStatus(%s, %s)", status.toString(), order.toString()));
        }
        Criteria query = getCriteria();
        for (DeveloperCity.ContactBack.DomainModel.CallStatus s : status) {
            query = query
                .add( CallStatus.ne(s) );
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
            logger.error("getByNotCallStatus Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByNotCallStatus(CallStatus[], Order...) !");
        }
        return retValue;
    }
    public List<T> getByNotCallStatus(int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.CallStatus... status) {
        return getByNotCallStatus(currentPage, pageSize, status, defaultOrder());
    }
    public List<T> getByNotCallStatus(int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.CallStatus[] status, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByNotCallStatus(%d, %d, %s, %s)", currentPage, pageSize, status.toString(), order.toString()));
        }
        Criteria query = getCriteria();
        for (DeveloperCity.ContactBack.DomainModel.CallStatus s : status) {
            query = query
                .add( CallStatus.ne(s) );
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
            logger.error("getByNotCallStatus Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByNotCallStatus(int, int, CallStatus[], Order...) !");
        }
        return retValue;
    }
    public List<T> getByCallStatusAndDates(Date from, Date to, DeveloperCity.ContactBack.DomainModel.CallStatus... status) {
        return getByCallStatusAndDates(from, to, status, defaultOrder());
    }
    public List<T> getByCallStatusAndDates(Date from, Date to, DeveloperCity.ContactBack.DomainModel.CallStatus[] status, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByCallStatusAndDates(%s, %s, %s, %s)", from, to, status != null ? status.toString() : "null", order.toString()));
        }
        Criteria query = getCriteria();

        if (status != null && status.length > 0) {
            query = query.add(CallStatus.in(status));
        }

        if (from != null && to != null) {
            query = query.add(StartTime.between(from, to));
        }
        else if (from != null) {
            query = query.add(StartTime.ge(from));
        }
        else if (to != null) {
            query = query.add(StartTime.le(to));
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
            logger.error("getByCallStatusAndDates Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByCallStatusAndDates(Date, Date, CallStatus[], Order...) !");
        }
        return retValue;
    }
    public List<T> getByCallStatusAndDates(Date from, Date to, int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.CallStatus... status) {
        return getByCallStatusAndDates(from, to, currentPage, pageSize, status, defaultOrder());
    }
    public List<T> getByCallStatusAndDates(Date from, Date to, int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.CallStatus[] status, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByCallStatusAndDates(%s, %s, %d, %d, %s, %s)", from, to, currentPage, pageSize, status.toString(), order.toString()));
        }
        Criteria query = getCriteria()
                .add(CallStatus.in(status));
        if (from != null && to != null) {
            query = query.add(StartTime.between(from, to));
        }
        else if (from != null) {
            query = query.add(StartTime.ge(from));
        }
        else if (to != null) {
            query = query.add(StartTime.le(to));
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
            logger.error("getByCallStatusAndDates Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByCallStatusAndDates(Date, Date, int, int, CallStatus[], Order...) !");
        }
        return retValue;
    }
    public List<T> getByAgentAndDates(long agentID, Date from, Date to) {
        return getByAgentAndDates(agentID, 0, from, to, 0, 0, defaultOrder());
    }
    public List<T> getByAgentAndDates(long agentID, Date from, Date to, Order... order) {
        return getByAgentAndDates(agentID, 0, from, to, 0, 0, order);
    }
    public List<T> getByAgentAndDates(long agentID, Date from, Date to, int currentPage, int pageSize, Order... order) {
        return getByAgentAndDates(agentID, 0, from, to, currentPage, pageSize, order);
    }
    public List<T> getByAgentAndDates(long agentID, long sinceItem,Date from, Date to) {
        return getByAgentAndDates(agentID, sinceItem, from, to, 0, 0, defaultOrder());
    }
    public List<T> getByAgentAndDates(long agentID, long sinceItem, Date from, Date to, Order... order) {
        return getByAgentAndDates(agentID, sinceItem, from, to, 0, 0, order);
    }
    public List<T> getByAgentAndDates(long agentID, long sinceItem, Date from, Date to, int currentPage, int pageSize, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByAgentDates(%d, %d, %s, %s, %d, %d, %s)", agentID, sinceItem, from, to, currentPage, pageSize, order.toString()));
        }
        Criteria query = getCriteria()
                .add(Property.forName(Agent.getPropertyName() + "." + AgentDAO.UserID.getPropertyName()).eq(agentID));
        if (from != null && to != null) {
            query = query.add(StartTime.between(from, to));
        }
        else if (from != null) {
            query = query.add(StartTime.ge(from));
        }
        else if (to != null) {
            query = query.add(StartTime.le(to));
        }

        if (sinceItem > 0) {
            query = query.add(CallID.gt(sinceItem));
        }

        if (pageSize > 0) {
            query = query
                .setFetchSize(pageSize)
                .setMaxResults(pageSize)
                .setFirstResult(currentPage * pageSize);
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
            logger.error("getByAgentDates Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByAgentDates(long, long, Date, Date, int, int, Order...) !");
        }
        return retValue;
    }

    @SuppressWarnings("unchecked")
    public HashMap<Integer, Integer> countByAnswerHour(Date from, Date to) {
        DetachedCriteria query = DetachedCriteria.forClass(Call.class)
                .add( Property.forName("AnswerTime").isNotNull())
                .add( Property.forName("AnswerTime").between(from, to) )
                .setProjection( Projections.projectionList()
                    .add( Projections.rowCount(), "countCalls" )
                    .add( Projections.sqlGroupProjection("hour({alias}.AnswerTime) as hour", "hour({alias}.AnswerTime)", new String[]{"hour"}, new Type[]{Hibernate.INTEGER} ) )
                );
        List<Object[]> results = null;
        HashMap<Integer, Integer> retValue = new HashMap<Integer, Integer>();
        try {
            results = (List<Object[]>) query.getExecutableCriteria(session).list();
        } catch(Exception e) {
            logger.error("countByAnswerHour Error", e);
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
    @SuppressWarnings("unchecked")
    public List<Date[]> getContactAndReturnByDates(Date from, Date to) {
        DetachedCriteria query = DetachedCriteria.forClass(Call.class, "C")
                .setProjection( Projections.projectionList()
                    .add(Projections.property( "Q." + QueueDAO.CallTime.getPropertyName() ) )
                    .add( Projections.property( AnswerTime.getPropertyName() ) )
                )
                .createAlias("Queue", "Q", org.hibernate.sql.JoinFragment.INNER_JOIN)
                .add( Property.forName("Q." + QueueDAO.QueueStatus.getPropertyName()).eq( QueueStatus.Complete ) )
                .add( AnswerTime.isNotNull() )
                .add( AnswerTime.between(from, to) )
                .add( CallID.eqProperty( Property.forName( "Q." + QueueDAO.AttendCall.getPropertyName() ) ) );
        List<Object[]> results = null;
        List<Date[]> retValue = new ArrayList<Date[]>();
        try {
            results = (List<Object[]>) query.getExecutableCriteria(session).list();
        } catch(Exception e) {
            logger.error("countByAnswerHour Error", e);
        }
        for (Object[] o : results) {
            Object[] item = o;
            Date callTime = (Date) item[0];
            Date answerTime = (Date) item[1];
            if (callTime != null && answerTime != null) {
                retValue.add(new Date[] { callTime, answerTime });
            }
        }

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<T> getFirstCallbacksByDates(Date from, Date to) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getFirstCallbacksByDates(%s, %s)", from, to));
        }
        Criteria query = getCriteria();
        if (from != null && to != null) {
            query = query.add(StartTime.between(from, to));
        }
        else if (from != null) {
            query = query.add(StartTime.ge(from));
        }
        else if (to != null) {
            query = query.add(StartTime.le(to));
        }

        query = query.addOrder(Queue.asc());
        query = query.addOrder(CallID.asc());
        List<T> all = null;
        try {
            all = getCollection(query);
        } catch (Exception e) {
            logger.error("getFirstCallbacksByDates Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getFirstCallbacksByDates(Date, Date) !");
        }
        HashMap<Long, T> map = new HashMap<Long, T>();
        for(T t : all) {
            if (!map.containsKey( t.getQueue().getQueueID() )) {
                map.put(t.getQueue().getQueueID(), t);
            }
        }
        List<T> retValue = new ArrayList<T>();
        retValue.addAll(map.values());
        return retValue;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Change">
    public T setCall(T item) throws Exception {
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