/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DataAccess;

import DeveloperCity.DataAccess.DAOGeneric;
import DeveloperCity.ContactBack.DomainModel.Priority;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;

public class PriorityDAO<T extends Priority> extends DAOGeneric<T> {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private static Logger logger = Logger.getLogger(PriorityDAO.class);
    public static final Property PriorityID = Property.forName("PriorityID");
    public static final Property StartTime = Property.forName("StartTime");
    public static final Property EndTime = Property.forName("EndTime");
    public static final Property Number = Property.forName("Number");
    public static final Property MatchMode = Property.forName("MatchMode");
    public static final Property PriorityValue = Property.forName("PriorityValue");
    public static final Property Weekdays = Property.forName("Weekdays");
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Enum">
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor and Override">
    @Override
    protected Order[] defaultOrder() {
        return new Order[] { StartTime.asc(), EndTime.asc() };
    }

    protected PriorityDAO(Session session, Class<T> type) {
        super(session, type);
    }

    public static PriorityDAO<? extends Priority> CreateInstance(Session session) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("CreateInstance(Session session)"));
        }
        PriorityDAO<Priority> retValue = new PriorityDAO<Priority>(session, Priority.class);
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
        Criteria query = getCriteria().add(PriorityID.eq(id));
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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Change">
    public T setPriority(T item) throws Exception {
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
    public void deletePriority(T item) throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("delete(%s)", item));
        }
        try {
            super.session.delete(item);
        } catch (Exception e) {
            logger.error("delete Error", e);
            throw e;
        }
        if (logger.isInfoEnabled()) {
            logger.info("delete(T) !");
        }
    }
    // </editor-fold>
}