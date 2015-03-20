/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.DataAccess;
import DeveloperCity.DataAccess.DAOGeneric;
import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.DomainModel.UserChart;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;

/**
 *
 * @author lbarbosa
 */
public class UserChartDAO<T extends UserChart> extends DAOGeneric<T> {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private static Logger logger = Logger.getLogger(UserPermissionDAO.class);
    public static final Property UserChartID = Property.forName("UserChartID");
    public static final Property User = Property.forName("User");
    public static final Property Chart = Property.forName("Chart");
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Enum">
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor and Override">
    @Override
    protected Order[] defaultOrder() {
        return new Order[] { UserChartID.asc() };
    }

    protected UserChartDAO(Session session, Class<T> type) {
        super(session, type);
    }

    public static UserChartDAO<? extends UserChart> CreateInstance(Session session) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("CreateInstance(Session session)"));
        }
        UserChartDAO<UserChart> retValue = new UserChartDAO<UserChart>(session, UserChart.class);
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
        Criteria query = getCriteria().add(UserChartID.eq(id));
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
    public List<T> getByUser(User user) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByUser(%s)", user));
        }
        Criteria query = getCriteria().add(User.eq(user)).createAlias("User", "u");
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
        } catch (Exception e) {
            logger.error("getByUser Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByUser(User) !");
        }
        return retValue;
    }
    public List<T> getByUserID(long userID) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByUserID(%d)", userID));
        }
        Criteria query = getCriteria().add(Property.forName("User.UserID").eq(userID));
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
        } catch (Exception e) {
            logger.error("getByUserID Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByUserID(long) !");
        }
        return retValue;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Change">
    public T setUserChart(T item) throws Exception {
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