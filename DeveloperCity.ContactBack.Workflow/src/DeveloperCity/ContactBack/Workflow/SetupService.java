package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.DataAccess.HibernateSession;
import DeveloperCity.ContactBack.DataAccess.SetupDAO;
import DeveloperCity.ContactBack.DomainModel.ConfigIVR;
import DeveloperCity.ContactBack.DomainModel.Setup;
import DeveloperCity.ContactBack.Exception.SetupUndefinedException;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class SetupService {

    private static Logger logger = Logger.getLogger(SetupService.class);
    private Setup setup;
    final Object syncSetup = new Object();

    public SetupService() throws SetupUndefinedException {
        reloadSetup();
    }
    public SetupService(ConfigIVR configIVR) throws SetupUndefinedException {
        this();
        setup.setConfigIVR(configIVR);
    }

    // <editor-fold defaultstate="collapsed" desc="Get">
    public Setup getByID(long id) {
        Setup retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = SetupDAO.CreateInstance(s).getByID(id);
        tx.commit();
        s.close();

        return retValue;
    }
    public final void reloadSetup() throws SetupUndefinedException {
        List<Setup> setups = getAll();
        if (setups == null || setups.isEmpty()) {
            throw new SetupUndefinedException("No Setup Row on configuration.");
        }
        setup = setups.get(0);
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="List">
    @SuppressWarnings("unchecked")
    public final List<Setup> getAll() {
        List<Setup> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Setup>) SetupDAO.CreateInstance(s).getAll();
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public final List<Setup> getAll(int currentPage, int pageSize) {
        List<Setup> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Setup>) SetupDAO.CreateInstance(s).getAll(currentPage, pageSize);
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Scalar">
    public int countAll() {
        int retValue = 0;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = SetupDAO.CreateInstance(s).CountAll();
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Save">
    @SuppressWarnings("unchecked")
    public Setup setSetup(Setup setup) throws Exception {
        synchronized(syncSetup) {
            Session s = HibernateSession.getSessionFactory().openSession();
            org.hibernate.Transaction tx = s.beginTransaction();
            try {
                setup = ((SetupDAO<Setup>) SetupDAO.CreateInstance(s)).setSetup(setup);
                tx.commit();
                this.setup = setup;
            } catch (Exception e) {
                tx.rollback();
                throw e;
            } finally {
                s.close();
            }
            return setup;
        }
    }
    // </editor-fold>
    
    public Setup getSetup() {
        return setup;
    }
}
