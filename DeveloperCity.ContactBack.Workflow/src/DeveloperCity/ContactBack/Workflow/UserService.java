package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.ContactBack.Exception.UserNotFoundException;
import DeveloperCity.ContactBack.Exception.AuthenticationFailException;
import DeveloperCity.ContactBack.Exception.PasswordArgumentException;
import DeveloperCity.DataAccess.HibernateSession;
import DeveloperCity.ContactBack.DataAccess.UserDAO;
import DeveloperCity.ContactBack.DomainModel.User;
import DeveloperCity.ContactBack.DomainModel.UserPermission;
import DeveloperCity.ContactBack.DomainModel.UserStatus;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserService {

    private static Logger logger = Logger.getLogger(UserService.class);

    public UserService() {
    }

    // <editor-fold defaultstate="collapsed" desc="Get">
    public User getByID(long id) {
        User retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = UserDAO.CreateInstance(s).getByID(id);
        tx.commit();
        s.close();

        return retValue;
    }
    public User getByAuthentication(String username, String password) {
        return getByAuthentication(username, password, false);
    }
    public User getByAuthentication(String username, String password, boolean fillModules) {
        User retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = UserDAO.CreateInstance(s).getByAuthentication(username, password);
        if (retValue != null && fillModules) {
            for (UserPermission userPermission : retValue.getPermissions()) {
                Object p = userPermission.getModule().getPortal();
            }
        }
        tx.commit();
        s.close();

        return retValue;
    }
    public User getByUsername(String username, boolean includeDeleted) {
        User retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        UserStatus[] status = includeDeleted ? (new UserStatus[] { UserStatus.Active, UserStatus.Blocked, UserStatus.Deleted }) : (new UserStatus[] { UserStatus.Active });
        retValue = UserDAO.CreateInstance(s).getByUsername(username, status);
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="List">
    @SuppressWarnings("unchecked")
    public List<User> getActive() {
        List<User> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<User>) UserDAO.CreateInstance(s).getByUserStatus(UserStatus.Active);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<User> getActive(int currentPage, int pageSize) {
        List<User> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<User>) UserDAO.CreateInstance(s).getByUserStatus(currentPage, pageSize, UserStatus.Active);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<User> getInactive() {
        List<User> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<User>) UserDAO.CreateInstance(s).getByNotUserStatus(UserStatus.Active);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<User> getInactive(int currentPage, int pageSize) {
        List<User> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<User>) UserDAO.CreateInstance(s).getByNotUserStatus(currentPage, pageSize, UserStatus.Active);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<User> getNotCustomer() {
        List<User> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<User>) UserDAO.CreateInstance(s).getNotCustomer();
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<User> getAll() {
        List<User> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<User>) UserDAO.CreateInstance(s).getAll();
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<User> getAll(int currentPage, int pageSize) {
        List<User> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<User>) UserDAO.CreateInstance(s).getAll(currentPage, pageSize);
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
        retValue = UserDAO.CreateInstance(s).CountAll();
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Save">
    @SuppressWarnings("unchecked")
    public User setUser(User user) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            UserDAO<User> userDAO = (UserDAO<User>) UserDAO.CreateInstance(s);
            user = userDAO.setUser(user);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return user;
    }
    @SuppressWarnings("unchecked")
    public User setUserStatus(long id, DeveloperCity.ContactBack.DomainModel.UserStatus newStatus) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        UserDAO<User> userDAO = (UserDAO<User>) UserDAO.CreateInstance(s);
        org.hibernate.Transaction tx = s.beginTransaction();
        User user = null;
        try {
            user = userDAO.getByID(id);
            if (user == null) {
                throw new Exception("User not found");
            }

            user.setUserStatus(newStatus);
            user = userDAO.setUser(user);
            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return user;
    }
    @SuppressWarnings("unchecked")
    public User setUserPassword(long id, String currentPassword, String newPassword, String confirmPassword) throws PasswordArgumentException, UserNotFoundException, AuthenticationFailException, Exception {
        if (currentPassword == null || currentPassword.trim().length() < 5) {
            throw new PasswordArgumentException("Current password must have at least 5 characteres.");
        }
        if (newPassword == null || newPassword.trim().length() < 5) {
            throw new PasswordArgumentException ("New password must have at least 5 characteres.");
        }

        if (! newPassword.equals(confirmPassword)) {
            throw new PasswordArgumentException ("Password and confirmation are different.");
        }

        Session s = HibernateSession.getSessionFactory().openSession();
        UserDAO<User> userDAO = (UserDAO<User>) UserDAO.CreateInstance(s);
        org.hibernate.Transaction tx = s.beginTransaction();
        User user = null;
        try {
            String encodedCurrent = DeveloperCity.Security.Crypto.EncryptForever(currentPassword);
            String encodedNew = DeveloperCity.Security.Crypto.EncryptForever(newPassword);

            user = userDAO.getByID(id);
            if (user == null) {
                throw new UserNotFoundException("User not found");
            }

            if ( !user.getPassword().equals(encodedCurrent) ) {
                throw new AuthenticationFailException("Current password is invalid.");
            }

            user.setPassword(encodedNew);
            user = userDAO.setUser(user);
            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return user;
    }
    @SuppressWarnings("unchecked")
    public User setUserPassword(long id, String newPassword) throws PasswordArgumentException, UserNotFoundException, AuthenticationFailException, Exception {
        if (newPassword == null || newPassword.trim().length() < 5) {
            throw new PasswordArgumentException ("New password must have at least 5 characteres.");
        }

        Session s = HibernateSession.getSessionFactory().openSession();
        UserDAO<User> userDAO = (UserDAO<User>) UserDAO.CreateInstance(s);
        org.hibernate.Transaction tx = s.beginTransaction();
        User user = null;
        try {
            String encodedNew = DeveloperCity.Security.Crypto.EncryptForever(newPassword);

            user = userDAO.getByID(id);
            if (user == null) {
                throw new UserNotFoundException("User not found");
            }

            user.setPassword(encodedNew);
            user = userDAO.setUser(user);
            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return user;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="FetchChild">
    public void FetchPermissions(User user) {
        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        s.lock(user, LockMode.NONE);
        boolean i = user.getPermissions().isEmpty();
        tx.commit();
        s.close();
    }
    public void FetchPermissions(List<User> users) {
        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        boolean i;
        for (User user : users) {
            s.lock(user, LockMode.NONE);
            i = user.getPermissions().isEmpty();
        }
        tx.commit();
        s.close();
    }
    // </editor-fold>
}
