package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.DataAccess.HibernateSession;
import DeveloperCity.ContactBack.DataAccess.ModuleDAO;
import DeveloperCity.ContactBack.DataAccess.UserDAO;
import DeveloperCity.ContactBack.DataAccess.UserPermissionDAO;
import DeveloperCity.ContactBack.DomainModel.Module;
import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.DomainModel.UserPermission;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserPermissionService {

    private static Logger logger = Logger.getLogger(UserPermissionService.class);

    public UserPermissionService() {
    }

    // <editor-fold defaultstate="collapsed" desc="Get">
    public UserPermission getByID(long id) {
        UserPermission retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = UserPermissionDAO.CreateInstance(s).getByID(id);
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="List">
    @SuppressWarnings("unchecked")
    public List<UserPermission> getAll() {
        List<UserPermission> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<UserPermission>) UserPermissionDAO.CreateInstance(s).getAll();
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<UserPermission> getAll(int currentPage, int pageSize) {
        List<UserPermission> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<UserPermission>) UserPermissionDAO.CreateInstance(s).getAll(currentPage, pageSize);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<UserPermission> getByUserID(long userID) {
        List<UserPermission> retValue = new ArrayList<UserPermission>();

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        
        User u = null;
        if (userID > 0) {
            u = UserDAO.CreateInstance(s).getByID(userID);
            for (UserPermission up : u.getPermissions()) {
                String a = up.getModule().getPortal().toString();
                retValue.add(up);
            }
        } else { u = new User(); }

        List<Module> modules = (List<Module>) ModuleDAO.CreateInstance(s).getAll();
        for (Module m : modules) {
            boolean hasAccess = false;
            for (UserPermission up : retValue) {
                if (up.getModule().getModuleID() == m.getModuleID()) {
                    hasAccess = true;
                    break;
                }
            }
            if (!hasAccess) {
                retValue.add(new UserPermission(0, u, m, ""));
            }
        }

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
        retValue = UserPermissionDAO.CreateInstance(s).CountAll();
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Save">
    @SuppressWarnings("unchecked")
    public UserPermission setUserPermission(UserPermission userPermission) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            userPermission = ((UserPermissionDAO<UserPermission>) UserPermissionDAO.CreateInstance(s)).setUserPermission(userPermission);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return userPermission;
    }
    @SuppressWarnings("unchecked")
    public List<UserPermission> setUserPermission(Long userID, List<Long> modules) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        List<UserPermission> retValue = null;
        try {
            List<UserPermission> removed = new ArrayList<UserPermission>();
            User u = UserDAO.CreateInstance(s).getByID(userID);
            ModuleDAO<Module> mDao = (ModuleDAO<Module>) ModuleDAO.CreateInstance(s);
            List<Long> notChangedIds = new ArrayList<Long>();
            UserPermissionDAO<UserPermission> daoUP = (UserPermissionDAO<UserPermission>) UserPermissionDAO.CreateInstance(s);
            retValue = daoUP.getByUserID(userID.longValue());
            for(UserPermission p : retValue) {
                if (!modules.contains(p.getModule().getModuleID())) {
                    removed.add(p);
                    s.delete(p);
                } else {
                    notChangedIds.add( modules.get(modules.indexOf(p.getModule().getModuleID())) );
                }
            }
            retValue.removeAll(removed);
            if (modules.size() > 0) {
                for(long m : modules) {
                    if (m > 0 && !notChangedIds.contains(m)) {
                        Module newMod = mDao.getByID(m);
                        UserPermission up = new UserPermission();
                        up.setModule(newMod);
                        up.setUser(u);
                        up.setReadWritePermission("rw");
                        up = daoUP.setUserPermission(up);
                        retValue.add(up);
                    }
                }
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return retValue;
    }

    // </editor-fold>
}