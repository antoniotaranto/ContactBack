package DeveloperCity.DataAccess;

import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;

public abstract class DAOGeneric<T extends Object> extends DAOBase {

    private static Logger logger = Logger.getLogger(DAOBase.class);
    Class instanceType;

    protected abstract Order[] defaultOrder();

    public DAOGeneric(Session session, Class<T> type) {
        super(session);
        if (logger.isInfoEnabled()) {
            logger.info(String.format("DAOGeneric(Session session, Class<T> type)"));
        }
        this.instanceType = type;
        if (logger.isInfoEnabled()) {
            logger.info(String.format("DAOGeneric(Session session, Class<T> type) !"));
        }
    }

    public List<T> getAll(Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getAll(Order order)"));
        }
        Criteria query = getCriteria();
        for (Order o : order) {
            query = query.addOrder(o);
        }
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
        } catch (Exception e) {
            logger.error(String.format("getAll Error"), e);
        }
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getAll(Order order) !"));
        }
        return retValue;
    }

    public List<T> getAll() {
        return getAll(defaultOrder());
    }

    public List<T> getAll(int currentPage, int pageSize) {
        return getAll(currentPage, pageSize, defaultOrder());
    }

    public List<T> getAll(int currentPage, int pageSize, Order... order) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getAll(int currentPage, int pageSize, Order order)", currentPage, pageSize));
        }
        Criteria query = getCriteria().setFetchSize(pageSize).setMaxResults(pageSize).setFirstResult(currentPage * pageSize);
        for (Order o : order) {
            query = query.addOrder(o);
        }
        List<T> retValue = null;
        try {
            retValue = getCollection(query);
        } catch (Exception e) {
            if (logger.isInfoEnabled()) {
                logger.info(String.format("getAll Error"), e);
            }
        }
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getAll(int currentPage, int pageSize, Order order) !"));
        }
        return retValue;
    }

    protected List<T> getCollection(Criteria query) throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getCollection(Criteria query)"));
        }
        List<T> retValue = null;
        Transaction tx = session.beginTransaction();
        try {
            retValue = query.list();
        } catch (Exception ex) {
            tx.rollback();
            if (logger.isInfoEnabled()) {
                logger.info(String.format("getCollection(Criteria query) !"));
            }
            throw ex;
        }
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getCollection(Criteria query) !"));
        }
        return retValue;
    }

    protected T getIndividual(Criteria query) throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getIndividual(Criteria query)"));
        }
        T retValue = null;
        Transaction tx = session.beginTransaction();
        try {
            retValue = (T) query.uniqueResult();
        } catch (Exception ex) {
            tx.rollback();
            if (logger.isInfoEnabled()) {
                logger.info(String.format("getIndividual(Criteria query) !"));
            }
            throw ex;
        }
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getIndividual(Criteria query) !"));
        }
        return retValue;
    }

    @Override
    protected Criteria getCriteria() {
        Criteria query = session.createCriteria(instanceType);
        return query;
    }
}
