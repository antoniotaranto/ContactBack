/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DataAccess;

import DeveloperCity.ContactBack.DomainModel.Agent;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;

public class AgentDAO<T extends Agent> extends UserDAO<T> {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private static Logger logger = Logger.getLogger(AgentDAO.class);
    public static final Property DirectoryNumber = Property.forName("DirectoryNumber");
    public static final Property Terminal = Property.forName("Terminal");
    public static final Property AgentStatus = Property.forName("AgentStatus");
    public static final Property CurrentCall = Property.forName("CurrentCall");
    public static final Property LastCallTime = Property.forName("LastCallTime");
    public static final Property WorkTimes = Property.forName("WorkTimes");
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Enum">
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor and Override">
    @Override
    protected Order[] defaultOrder() {
        return new Order[] { Name.asc() };
    }

    protected AgentDAO(Session session, Class<T> type) {
        super(session, type);
    }

    @SuppressWarnings("unchecked")
    public static AgentDAO<? extends Agent> CreateInstance(Session session) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("CreateInstance(Session session)"));
        }
        AgentDAO<Agent> retValue = new AgentDAO<Agent>(session, Agent.class);
        if (logger.isInfoEnabled()) {
            logger.info(String.format("CreateInstance(Session session) !"));
        }
        return retValue;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Get Individual">
    @Override
    public T getByID(long id) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByID(%d)", id));
        }
        Criteria query = getCriteria()
                .add(UserID.eq(id));
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
    public T getByDirectoryNumber(String directoryNumber) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByDirectoryNumber(%s)", directoryNumber));
        }
        Criteria query = getCriteria()
                .add(DirectoryNumber.like(directoryNumber))
                .add(UserStatus.eq(DeveloperCity.ContactBack.DomainModel.UserStatus.Active));

        T retValue = null;
        try {
            retValue = getIndividual(query);
        } catch (Exception e) {
            logger.error("getByDirectoryNumber Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByDirectoryNumber(String) !");
        }
        return retValue;
    }
    public T getByIDIncludeLastWorkTime(long id) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByID(%d)", id));
        }
        Criteria query = getCriteria()
                .add(UserID.eq(id))
                .createAlias(WorkTimes.getPropertyName(), "w", org.hibernate.sql.JoinFragment.LEFT_OUTER_JOIN)
                .setMaxResults(1)
                .addOrder(Property.forName("w." + WorkTimeDAO.WorkTimeID.getPropertyName()).desc());
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
    public List<T> getByNotAgentStatus(DeveloperCity.ContactBack.DomainModel.AgentStatus... status) {
        return getByNotAgentStatus(status, defaultOrder());
    }
    public List<T> getByNotAgentStatus(DeveloperCity.ContactBack.DomainModel.AgentStatus[] status, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByNotAgentStatus(%s, %s)", status.toString(), order.toString()));
        }

        Criteria query = getCriteria()
                .add(UserStatus.eq(DeveloperCity.ContactBack.DomainModel.UserStatus.Active));

        for (DeveloperCity.ContactBack.DomainModel.AgentStatus s : status) {
            query = query
                .add( AgentStatus.ne(s) );
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
            logger.error("getByNotAgentStatus Error", e);
        }

        if (logger.isInfoEnabled()) {
            logger.info("getByNotAgentStatus(AgentStatus[], Order...) !");
        }
        return retValue;
    }
    public List<T> getByNotAgentStatus(int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.AgentStatus... status) {
        return getByNotAgentStatus(currentPage, pageSize, status, defaultOrder());
    }
    public List<T> getByNotAgentStatus(int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.AgentStatus[] status, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByNotAgentStatus(%d, %d, %s, %s)", currentPage, pageSize, status.toString(), order.toString()));
        }

        Criteria query = getCriteria()
                .add(UserStatus.eq(DeveloperCity.ContactBack.DomainModel.UserStatus.Active));
        for (DeveloperCity.ContactBack.DomainModel.AgentStatus s : status) {
            query = query
                .add( AgentStatus.ne(s) );
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
            logger.error("getByNotAgentStatus Error", e);
        }

        if (logger.isInfoEnabled()) {
            logger.info("getByNotAgentStatus(AgentStatus[], Order...) !");
        }
        return retValue;
    }

    public List<T> getByAgentStatus(DeveloperCity.ContactBack.DomainModel.AgentStatus... status) {
        return getByAgentStatus(status, defaultOrder());
    }
    public List<T> getByAgentStatus(DeveloperCity.ContactBack.DomainModel.AgentStatus[] status, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByAgentStatus(%s, %s)", status.toString(), order.toString()));
        }

        Criteria query = getCriteria()
                .add( AgentStatus.in(status) )
                .add(UserStatus.eq(DeveloperCity.ContactBack.DomainModel.UserStatus.Active));

        if (order != null) {
            for (Order o : order) {
                query = query.addOrder(o);
            }
        }
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
        } catch (Exception e) {
            logger.error("getByAgentStatus Error", e);
        }

        if (logger.isInfoEnabled()) {
            logger.info("getByAgentStatus(AgentStatus[], Order...) !");
        }
        return retValue;
    }
    public List<T> getByAgentStatus(int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.AgentStatus... status) {
        return getByAgentStatus(currentPage, pageSize, status, defaultOrder());
    }
    public List<T> getByAgentStatus(int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.AgentStatus[] status, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByAgentStatus(%d, %d, %s, %s)", currentPage, pageSize, status.toString(), order.toString()));
        }

        Criteria query = getCriteria()
                .add( AgentStatus.in(status) )
                .add(UserStatus.eq(DeveloperCity.ContactBack.DomainModel.UserStatus.Active))
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
            logger.error("getByAgentStatus Error", e);
        }

        if (logger.isInfoEnabled()) {
            logger.info("getByAgentStatus(AgentStatus[], Order...) !");
        }
        return retValue;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Change">
    public Agent setAgent(Agent item) throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("set(%s)", item));
        }
        try {
            if (item.getUserID() > 0) {
                Agent o = getByID(item.getUserID());
                o.setAgentStatus(item.getAgentStatus());
                o.setBirthday(item.getBirthday());
                o.setCallManagerCallIDs(item.getCallManagerCallIDs());
                o.setDirectoryNumber(item.getDirectoryNumber());
                o.setEmail(item.getEmail());
                o.setLastCallTime(item.getLastCallTime());
                o.setName(item.getName());
                o.setPassword(item.getPassword());
                o.setRequestingBreak(item.getRequestingBreak());
                o.setRequestingLogoff(item.getRequestingLogoff());
                o.setTerminal(item.getTerminal());
                o.setUserStatus(item.getUserStatus());
                o.setUsername(item.getUsername());
                item = o;
            }
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
