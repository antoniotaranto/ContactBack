/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DataAccess;

import DeveloperCity.ContactBack.Exception.MultipleOpenAgentSessionException;
import DeveloperCity.DataAccess.DAOGeneric;
import DeveloperCity.ContactBack.DomainModel.Agent;
import DeveloperCity.ContactBack.DomainModel.WorkTime;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;

public class WorkTimeDAO<T extends WorkTime> extends DAOGeneric<T> {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private static Logger logger = Logger.getLogger(WorkTimeDAO.class);
    public static final Property WorkTimeID = Property.forName("WorkTimeID");
    public static final Property LoginTime = Property.forName("LoginTime");
    public static final Property LogoutTime = Property.forName("LogoutTime");
    public static final Property Agent = Property.forName("Agent");
    public static final Property Breaks = Property.forName("Breaks");
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Enum">
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor and Override">
    @Override
    protected Order[] defaultOrder() {
        return new Order[] { LoginTime.desc() };
    }

    protected WorkTimeDAO(Session session, Class<T> type) {
        super(session, type);
    }

    public static WorkTimeDAO<? extends WorkTime> CreateInstance(Session session) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("CreateInstance(Session session)"));
        }
        WorkTimeDAO<WorkTime> retValue = new WorkTimeDAO<WorkTime>(session, WorkTime.class);
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
        Criteria query = getCriteria().add(WorkTimeID.eq(id));
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
    public T getByAgentUnfinished(Agent agent) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByAgentUnfinished(%s)", agent));
        }
        Criteria query = getCriteria()
                .add(Agent.eq(agent))
                .add(LogoutTime.isNull());
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
            if (retValue.isEmpty()) {
                return null;
            }
            else if (retValue.size() > 1) {
                throw new MultipleOpenAgentSessionException(agent);
            }
        } catch (Exception e) {
            logger.error("getByAgentUnfinished Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByAgentUnfinished(Agent) !");
        }
        return retValue.get(0);
    }
    public T getByAgentLastOne(Agent agent) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByAgentLastOne(%s)", agent));
        }
        Criteria query = getCriteria()
                .add(Agent.eq(agent))
                .addOrder(LoginTime.desc())
                .setMaxResults(1)
                .setFetchSize(1);
        T retValue = null;
        try {
            retValue = getIndividual(query);
        } catch (Exception e) {
            logger.error("getByAgentLastOne Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByAgentLastOne(Agent) !");
        }
        return retValue;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Get Collection">
    public List<T> getByAgent(Agent agent) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByAgent(%s)", agent));
        }
        Criteria query = getCriteria().add(Agent.eq(agent));
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
        } catch (Exception e) {
            logger.error("getByAgent Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByAgent(Agent) !");
        }
        return retValue;
    }
    public List<T> getByAgentAndDates(Agent agent, Date from, Date to) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByAgent(%s)", agent));
        }
        Criteria query = getCriteria().add(Agent.eq(agent)).add( LoginTime.between(from, to) );
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
        } catch (Exception e) {
            logger.error("getByAgent Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByAgent(Agent) !");
        }
        return retValue;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Change">
    public T setWorkTime(T item) throws Exception {
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