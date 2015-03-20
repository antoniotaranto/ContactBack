/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DataAccess;

import DeveloperCity.DataAccess.DAOGeneric;
import DeveloperCity.ContactBack.DomainModel.Agent;
import DeveloperCity.ContactBack.DomainModel.Break;
import DeveloperCity.ContactBack.DomainModel.WorkTime;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;

public class BreakDAO<T extends Break> extends DAOGeneric<T> {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private static Logger logger = Logger.getLogger(BreakDAO.class);
    public static final Property BreakID = Property.forName("BreakID");
    public static final Property WorkTime = Property.forName("WorkTime");
    public static final Property BreakType = Property.forName("BreakType");
    public static final Property BreakStart = Property.forName("BreakStart");
    public static final Property BreakEnd = Property.forName("BreakEnd");
    public static final Property SystemBreak = Property.forName("SystemBreak");
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Enum">
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor and Override">
    @Override
    protected Order[] defaultOrder() {
        return new Order[] { BreakStart.asc(), BreakEnd.asc() };
    }

    protected BreakDAO(Session session, Class<T> type) {
        super(session, type);
    }

    public static BreakDAO<? extends Break> CreateInstance(Session session) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("CreateInstance(Session session)"));
        }
        BreakDAO<Break> retValue = new BreakDAO<Break>(session, Break.class);
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
        Criteria query = getCriteria().add(BreakID.eq(id));
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
    public List<T> getByAgentUnfinished(Agent agent) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByAgentUnfinished(%s)", agent));
        }
        Criteria query = getCriteria()
                .createAlias(WorkTime.getPropertyName(), "w", org.hibernate.sql.JoinFragment.INNER_JOIN)
                .add(Property.forName("w." + WorkTimeDAO.Agent.getPropertyName()).eq(agent))
                .add(Property.forName("w." + WorkTimeDAO.LogoutTime.getPropertyName()).isNull())
                .add(BreakEnd.isNull())
                .addOrder(BreakStart.asc());
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
        } catch (Exception e) {
            logger.error("getByAgentUnfinished Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByAgentUnfinished(Agent) !");
        }
        return retValue;
    }
    public List<T> getByWorkTimeUnfinished(WorkTime currentWorkTime) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByWorkTimeUnfinished(%s)", currentWorkTime));
        }
        Criteria query = getCriteria()
                .add(WorkTime.eq(currentWorkTime))
                .add(BreakEnd.isNull())
                .addOrder(BreakStart.asc());
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
        } catch (Exception e) {
            logger.error("getByWorkTimeUnfinished Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByWorkTimeUnfinished(WorkTime) !");
        }
        return retValue;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Change">
    public T setBreak(T item) throws Exception {
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