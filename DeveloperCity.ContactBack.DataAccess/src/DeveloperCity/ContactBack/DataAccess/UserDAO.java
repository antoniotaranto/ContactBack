/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DataAccess;

import DeveloperCity.DataAccess.DAOGeneric;
import DeveloperCity.ContactBack.DomainModel.Customer;
import DeveloperCity.ContactBack.DomainModel.User;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.apache.log4j.Logger;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;

public class UserDAO<T extends User> extends DAOGeneric<T> {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private static Logger logger = Logger.getLogger(UserDAO.class);
    public static final Property UserID = Property.forName("UserID");
    public static final Property Username = Property.forName("Username");
    public static final Property Name = Property.forName("Name");
    public static final Property Email = Property.forName("Email");
    public static final Property Birthday = Property.forName("Birthday");
    public static final Property Password = Property.forName("Password");
    public static final Property UserStatus = Property.forName("UserStatus");
    public static final Property Permissions = Property.forName("Permissions");
    public static final Property Reports = Property.forName("Reports");
    public static final Property Charts = Property.forName("Charts");
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Enum">
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Constructor and Override">
    @Override
    protected Order[] defaultOrder() {
        return new Order[]{Name.asc()};
    }

    protected UserDAO(Session session, Class<T> type) {
        super(session, type);
    }

    @SuppressWarnings("unchecked")
    public static UserDAO<? extends User> CreateInstance(Session session) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("CreateInstance(Session session)"));
        }
        UserDAO<User> retValue = new UserDAO<User>(session, User.class);
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
        Criteria query = getCriteria().add(UserID.eq(id));
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

    public T getByAuthentication(String username, String password) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByAuthentication(%s, [pwd])", username));
        }
        String newpass = DeveloperCity.Security.Crypto.EncryptForever(password);
        Criteria query =
                getCriteria().add(Username.eq(username)).add(Password.eq(newpass)).add(UserStatus.eq(DeveloperCity.ContactBack.DomainModel.UserStatus.Active));

        T retValue = null;
        try {
            retValue = getIndividual(query);
        } catch (Exception e) {
            logger.error("getByAuthentication Error", e);
        }

        if (logger.isInfoEnabled()) {
            logger.info("getByAuthentication(String, String) !");
        }
        return retValue;
    }

    public T getByUsername(String username) {
        return getByUsername(username, DeveloperCity.ContactBack.DomainModel.UserStatus.Active);
    }
    public T getByUsername(String username, DeveloperCity.ContactBack.DomainModel.UserStatus... status) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByUsername(%s, %s)", username, status.toString()));
        }

        Criteria query = getCriteria().add(Username.eq(username)).add(UserStatus.in(status));

        T retValue = null;
        try {
            retValue = getIndividual(query);
        } catch (Exception e) {
            logger.error("getByUsername Error", e);
        }
        if (logger.isInfoEnabled()) {
            logger.info("getByUsername(String) !");
        }
        return retValue;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Get Collection">
    public List<T> getNotCustomer() {
        if (logger.isInfoEnabled()) {
            logger.info("getNotCustomer()");
        }

        Criteria query = getCriteria();
        query = query.add( UserID.notIn( DetachedCriteria.forClass(Customer.class, "c").setProjection( UserID.as("cID") ) ) );
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
        } catch (Exception e) {
            logger.error("getNotCustomer Error", e);
        }

        if (logger.isInfoEnabled()) {
            logger.info("getNotCustomer() !");
        }
        return retValue;
    }
    public List<T> getByNotUserStatus(DeveloperCity.ContactBack.DomainModel.UserStatus... status) {
        return getByNotUserStatus(status, defaultOrder());
    }
    public List<T> getByNotUserStatus(DeveloperCity.ContactBack.DomainModel.UserStatus[] status, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByNotUserStatus(%s, %s)", status.toString(), order.toString()));
        }

        Criteria query = getCriteria();
        for (DeveloperCity.ContactBack.DomainModel.UserStatus s : status) {
            query = query
                .add(UserStatus.ne(s));
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
            logger.error("getByNotUserStatus Error", e);
        }

        if (logger.isInfoEnabled()) {
            logger.info("getByNotUserStatus(UserStatus[], Order...) !");
        }
        return retValue;
    }
    public List<T> getByNotUserStatus(int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.UserStatus... status) {
        return getByNotUserStatus(currentPage, pageSize, status, defaultOrder());
    }
    public List<T> getByNotUserStatus(int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.UserStatus[] status, Order...order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByNotUserStatus(%d, %d, %s, %s)", currentPage, pageSize, status.toString(), order.toString()));
        }

        Criteria query = getCriteria();
        for (DeveloperCity.ContactBack.DomainModel.UserStatus s : status) {
            query = query
                .add(UserStatus.ne(s));
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
            logger.error("getByNotUserStatus Error", e);
        }

        if (logger.isInfoEnabled()) {
            logger.info("getByNotUserStatus(int, int, UserStatus[], Order...) !");
        }
        return retValue;
    }

    public List<T> getByUserStatus(DeveloperCity.ContactBack.DomainModel.UserStatus... status) {
        return getByUserStatus(status, defaultOrder());
    }
    public List<T> getByUserStatus(DeveloperCity.ContactBack.DomainModel.UserStatus[] status, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByUserStatus(%s, %s)", status.toString(), order.toString()));
        }

        Criteria query = getCriteria().add(UserStatus.in(status));
        if (order != null) {
            for (Order o : order) {
                query = query.addOrder(o);
            }
        }
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
        } catch (Exception e) {
            logger.error("getByUserStatus Error", e);
        }

        if (logger.isInfoEnabled()) {
            logger.info("getByUserStatus(UserStatus[], Order...) !");
        }
        return retValue;
    }
    public List<T> getByUserStatus(int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.UserStatus... status) {
        return getByUserStatus(currentPage, pageSize, status, defaultOrder());
    }
    public List<T> getByUserStatus(int currentPage, int pageSize, DeveloperCity.ContactBack.DomainModel.UserStatus[] status, Order...order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getByUserStatus(%d, %d, %s, %s)", currentPage, pageSize, status.toString(), order.toString()));
        }

        Criteria query = getCriteria()
                .add(UserStatus.in(status))
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
            logger.error("getByUserStatus Error", e);
        }

        if (logger.isInfoEnabled()) {
            logger.info("getByUserStatus(int, int, UserStatus[], Order...) !");
        }
        return retValue;
    }
    //
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Change">
    public T setUser(T item) throws Exception {
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
