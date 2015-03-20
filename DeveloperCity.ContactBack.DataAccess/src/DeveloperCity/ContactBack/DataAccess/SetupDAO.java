/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DataAccess;

import DeveloperCity.DataAccess.DAOGeneric;
import DeveloperCity.ContactBack.DomainModel.Setup;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;

public class SetupDAO<T extends Setup> extends DAOGeneric<T> {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private static Logger logger = Logger.getLogger(SetupDAO.class);
    public static final Property SetupID = Property.forName("SetupID");
    public static final Property TimeBetweenCallBacks = Property.forName("TimeBetweenCallBacks");
    public static final Property LateCallBackAfter = Property.forName("LateCallBackAfter");
    public static final Property LateCallBackTime = Property.forName("LateCallBackTime");
    public static final Property EndOfQueue = Property.forName("EndOfQueue");
    public static final Property MaxCallBacks = Property.forName("MaxCallBacks");
    public static final Property CTI_DeviceName = Property.forName("CTI_DeviceName");
    public static final Property IVR_DeviceName = Property.forName("IVR_DeviceName");
    public static final Property VoiceFolder = Property.forName("VoiceFolder");
    public static final Property PrefixDial = Property.forName("PrefixDial");
    public static final Property InternalExtensionDestination = Property.forName("InternalExtensionDestination");
    public static final Property MobilePhoneDestination = Property.forName("MobilePhoneDestination");
    public static final Property LandLineDestination = Property.forName("LandLineDestination");
    public static final Property InvalidNumberDestination = Property.forName("InvalidNumberDestination");
    public static final Property ShiftWeekdayStartHour = Property.forName("ShiftWeekdayStartHour");
    public static final Property ShiftWeekdayEndHour = Property.forName("ShiftWeekdayEndHour");
    public static final Property ShiftWeekdayStartMinute = Property.forName("ShiftWeekdayStartMinute");
    public static final Property ShiftWeekdayEndMinute = Property.forName("ShiftWeekdayEndMinute");
    public static final Property ShiftSaturdayStartHour = Property.forName("ShiftSaturdayStartHour");
    public static final Property ShiftSaturdayEndHour = Property.forName("ShiftSaturdayEndHour");
    public static final Property ShiftSaturdayStartMinute = Property.forName("ShiftSaturdayStartMinute");
    public static final Property ShiftSaturdayEndMinute = Property.forName("ShiftSaturdayEndMinute");
    public static final Property QueueSuccessDevice = Property.forName("QueueSuccessDevice");
    public static final Property QueueAlreadyDevice = Property.forName("QueueAlreadyDevice");
    public static final Property QueueInvalidNumberDevice = Property.forName("QueueInvalidNumberDevice");
    public static final Property QueueNotInShiftTimeDevice = Property.forName("QueueNotInShiftTimeDevice");
    public static final Property SMSUrl = Property.forName("SMSUrl");
    public static final Property SMSMessage = Property.forName("SMSMessage");
    public static final Property SMSAccount = Property.forName("SMSAccount");
    public static final Property SMSCode = Property.forName("SMSCode");
    public static final Property SMSFrom = Property.forName("SMSFrom");
    public static final Property DefaultPassword = Property.forName("DefaultPassword");
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Enum">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructor and Override">
    @Override
    protected Order[] defaultOrder() {
        return new Order[]{SetupID.asc()};
    }

    protected SetupDAO(Session session, Class<T> type) {
        super(session, type);
    }

    public static SetupDAO<? extends Setup> CreateInstance(Session session) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("CreateInstance(Session session)"));
        }
        SetupDAO<Setup> retValue = new SetupDAO<Setup>(session, Setup.class);
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
        Criteria query = getCriteria().add(SetupID.eq(id));
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
    public T setSetup(T item) throws Exception {
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
