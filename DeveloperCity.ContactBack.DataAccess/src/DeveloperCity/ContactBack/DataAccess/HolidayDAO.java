package DeveloperCity.ContactBack.DataAccess;

import DeveloperCity.DataAccess.DAOGeneric;
import DeveloperCity.ContactBack.DomainModel.Holiday;
import java.util.Calendar;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;

public class HolidayDAO<T extends Holiday> extends DAOGeneric<T> {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private static Logger logger = Logger.getLogger(HolidayDAO.class);
    public static final Property HolidayID = Property.forName("HolidayID");
    public static final Property HolidayName = Property.forName("HolidayName");
    public static final Property Day = Property.forName("Day");
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Enum">
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor and Override">
    @Override
    protected Order[] defaultOrder() {
        return new Order[] { Day.asc() };
    }

    protected HolidayDAO(Session session, Class<T> type) {
        super(session, type);
    }

    public static HolidayDAO<? extends Holiday> CreateInstance(Session session) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("CreateInstance(Session session)"));
        }
        HolidayDAO<Holiday> retValue = new HolidayDAO<Holiday>(session, Holiday.class);
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
        Criteria query = getCriteria().add(HolidayID.eq(id));
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
    public List<T> getFutureHolidays() {
        if (logger.isInfoEnabled()) {
            logger.info("getFutureHolidays()");
        }
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);
        Criteria query = getCriteria().add(Day.ge( today.getTime() ));
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
        } catch (Exception e) {
            logger.error("getFutureHolidays Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getFutureHolidays() !");
        }
        return retValue;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Change">
    public T setHoliday(T item) throws Exception {
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
    public void deleteHoliday(T item) throws Exception {
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