package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.DataAccess.HibernateSession;
import DeveloperCity.ContactBack.DataAccess.CustomerDAO;
import DeveloperCity.ContactBack.DomainModel.Customer;
import DeveloperCity.ContactBack.DomainModel.UserStatus;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class CustomerService {

    private static Logger logger = Logger.getLogger(CustomerService.class);

    public CustomerService() {
    }

    // <editor-fold defaultstate="collapsed" desc="Get">
    public Customer getByID(long id) {
        Customer retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = CustomerDAO.CreateInstance(s).getByID(id);
        tx.commit();
        s.close();

        return retValue;
    }
    public Customer getByAuthentication(String username, String password) {
        Customer retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (Customer) CustomerDAO.CreateInstance(s).getByAuthentication(username, password);
        tx.commit();
        s.close();

        return retValue;
    }
    public Customer getByUsername(String username) {
        Customer retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (Customer) CustomerDAO.CreateInstance(s).getByUsername(username);
        tx.commit();
        s.close();

        return retValue;
    }
    public Customer getByUsername(String username, boolean includeDeleted) {
        Customer retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        UserStatus[] status = includeDeleted ? (new UserStatus[] { UserStatus.Active, UserStatus.Blocked, UserStatus.Deleted }) : (new UserStatus[] { UserStatus.Active });
        retValue = (Customer) CustomerDAO.CreateInstance(s).getByUsername(username, status);
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="List">
    @SuppressWarnings("unchecked")
    public List<Customer> getUserActive() {
        List<Customer> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Customer>) CustomerDAO.CreateInstance(s).getByUserStatus(UserStatus.Active);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Customer> getUserActive(int currentPage, int pageSize) {
        List<Customer> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Customer>) CustomerDAO.CreateInstance(s).getByUserStatus(currentPage, pageSize, UserStatus.Active);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Customer> getUserInactive() {
        List<Customer> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Customer>) CustomerDAO.CreateInstance(s).getByNotUserStatus(UserStatus.Active);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Customer> getUserInactive(int currentPage, int pageSize) {
        List<Customer> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Customer>) CustomerDAO.CreateInstance(s).getByNotUserStatus(currentPage, pageSize, UserStatus.Active);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Customer> getAll() {
        List<Customer> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Customer>) CustomerDAO.CreateInstance(s).getAll();
        tx.commit();
        s.close();

        return retValue;
    }
    // <editor-fold defaultstate="collapsed" desc="Scalar">
    public int countAll() {
        int retValue = 0;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = CustomerDAO.CreateInstance(s).CountAll();
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Save">
    public Customer setCustomer(Customer customer) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            customer = CustomerDAO.CreateInstance(s).setCustomer(customer);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return customer;
    }
    @SuppressWarnings("unchecked")
    public Customer setCustomerPassword(long id, String plainCurrentPassword, String plainPassword, String confirmPassword) throws Exception {
        if (plainPassword == null || plainPassword.trim().length() < 5) {
            throw new Exception ("Password must have at least 5 characteres.");
        }

        if (! plainPassword.equals(confirmPassword)) {
            throw new Exception ("Password and confirmation are different.");
        }

        Session s = HibernateSession.getSessionFactory().openSession();
        CustomerDAO<Customer> customerDAO = (CustomerDAO<Customer>) CustomerDAO.CreateInstance(s);
        org.hibernate.Transaction tx = s.beginTransaction();
        Customer customer = null;
        try {
            customer = customerDAO.getByID(id);

            if (customer == null) {
                throw new Exception("Customer not found.");
            }

            if (!(DeveloperCity.Security.Crypto.EncryptForever(plainCurrentPassword).equals(customer.getPassword()))) {
                throw new Exception("Current password is not valid.");
            }

            customer.setPassword(DeveloperCity.Security.Crypto.EncryptForever(plainPassword));
            customer = customerDAO.setCustomer(customer);
            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return customer;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="FetchChild">
    public void FetchPermissions(Customer customer) {
        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        s.lock(customer, LockMode.NONE);
        boolean i = customer.getPermissions().isEmpty();
        tx.commit();
        s.close();
    }
    public void FetchPermissions(List<Customer> customers) {
        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        boolean i;
        for (Customer customer : customers) {
            s.lock(customer, LockMode.NONE);
            i = customer.getPermissions().isEmpty();
        }
        tx.commit();
        s.close();
    }
    // </editor-fold>
}