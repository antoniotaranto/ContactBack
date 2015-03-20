package DeveloperCity.ContactBack.DataAccess;

import DeveloperCity.ContactBack.DomainModel.QueueStatus;
import DeveloperCity.ContactBack.DomainModel.WebQueue;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;

public class WebQueueDAO<T extends WebQueue> extends QueueDAO<T> {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private static Logger logger = Logger.getLogger(WebQueueDAO.class);
    public static final Property Customer = Property.forName("Customer");
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Enum">
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor and Override">
    @Override
    protected Order[] defaultOrder() {
        return new Order[] { ScheduleTime.asc() };
    }

    protected WebQueueDAO(Session session, Class<T> type) {
        super(session, type);
    }

    @SuppressWarnings("unchecked")
    public static WebQueueDAO<? extends WebQueue> CreateInstance(Session session) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("CreateInstance(Session session)"));
        }
        WebQueueDAO<WebQueue> retValue = new WebQueueDAO<WebQueue>(session, WebQueue.class);
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
                .add(QueueID.eq(id));
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
    public T getByCustomer(long userId) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByCustomer(%d)", userId));
        }
        Criteria query = getCriteria()
                .add(Property.forName(Customer.getPropertyName() + "." + UserDAO.UserID.getPropertyName()).eq(userId))
                .add(QueueStatus.eq(DeveloperCity.ContactBack.DomainModel.QueueStatus.InQueue));
        T retValue = null;
        try {
            List<T> list = getCollection(query);
            retValue = (list == null || list.isEmpty()) ? null : list.get(0);
        } catch (Exception e) {
            logger.error("getByCustomer Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByCustomer(long) !");
        }
        return retValue;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Get Collection">
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Change">
    public WebQueue setWebQueue(WebQueue item) throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("set(%s)", item));
        }
        try {
            if (item.getQueueID() > 0) {
                WebQueue o = getByID(item.getQueueID());
                o.setCustomer(item.getCustomer());
                o.setAttendCall(item.getAttendCall());
                o.setAttendCount(item.getAttendCount());
                o.setCallManagerCallID(item.getCallManagerCallID());
                o.setCallTime(item.getCallTime());
                o.setDontCallBefore(item.getDontCallBefore());
                o.setEntryPosition(item.getEntryPosition());
                o.setEstimatedTimeToAttend(item.getEstimatedTimeToAttend());
                o.setFrozen(item.isFrozen());
                o.setBinaNumber ( item.getBinaNumber());
                o.setValidBinaNumber ( item.isValidBinaNumber());
                o.setCallBackNumber ( item.getCallBackNumber());
                o.setCallBackNumberType ( item.getCallBackNumberType());                
                o.setPriorityValue(item.getPriorityValue());
                o.setQueueStatus(item.getQueueStatus());
                o.setScheduleTime(item.getScheduleTime());
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
