package DeveloperCity.DataAccess;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;

public abstract class DAOBase {

    private static Logger logger = Logger.getLogger(DAOBase.class);
    protected Session session;

    public DAOBase(Session session) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("DAOBase(Session session)"));
        }
        this.session = session;
        if (logger.isInfoEnabled()) {
            logger.info(String.format("DAOBase(Session session) !"));
        }
    }

    public int CountAll() {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("CountAll()"));
        }
        Criteria query = getCriteria();
        query.setProjection(Projections.rowCount());
        int retValue = 0;
        try {
            retValue = getScalar(query, Integer.class);
        } catch (Exception e) {
            logger.error(String.format("CountAll Error"), e);
        }
        if (logger.isInfoEnabled()) {
            logger.info(String.format("CountAll() !"));
        }
        return retValue;
    }

    protected <T> T getScalar(Criteria query, Class<T> type) throws Exception {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getScalar(Criteria query, Class<T> type)"));
        }
        T retValue = null;

        Transaction tx = session.beginTransaction();
        try {
            Object getResult = query.uniqueResult();
            retValue = (T) getResult;
        } catch (Exception ex) {
            tx.rollback();
            retValue = null;
            if (logger.isInfoEnabled()) {
                logger.info(String.format("getScalar(Criteria query, Class<T> type) !"));
            }
            throw ex;
        }
        if (logger.isInfoEnabled()) {
            logger.info(String.format("getScalar(Criteria query, Class<T> type) !"));
        }
        return retValue;
    }

    protected abstract Criteria getCriteria();
}