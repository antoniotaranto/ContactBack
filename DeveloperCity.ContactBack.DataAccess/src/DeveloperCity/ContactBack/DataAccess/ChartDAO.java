/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.DataAccess;
import DeveloperCity.DataAccess.DAOGeneric;
import DeveloperCity.ContactBack.DomainModel.Chart;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Property;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Order;

/**
 *
 * @author lbarbosa
 */
public class ChartDAO<T extends Chart> extends DAOGeneric<T> {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private static Logger logger = Logger.getLogger(BreakTypeDAO.class);
    public static final Property ChartID = Property.forName("ChartID");
    public static final Property ChartFile = Property.forName("ChartFile");
    public static final Property ChartDescription = Property.forName("ChartDescription");
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Enum">
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Constructor and Override">
    @Override
    protected Order[] defaultOrder() {
        return new Order[] { ChartDescription.asc() };
    }

    protected ChartDAO(Session session, Class<T> type) {
        super(session, type);
    }

    public static ChartDAO<? extends Chart> CreateInstance(Session session) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("CreateInstance(Session session)"));
        }
        ChartDAO<Chart> retValue = new ChartDAO<Chart>(session, Chart.class);
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
        Criteria query = getCriteria().add(ChartID.eq(id));
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
    public T setChart(T item) throws Exception {
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