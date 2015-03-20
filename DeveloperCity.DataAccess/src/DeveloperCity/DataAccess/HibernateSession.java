package DeveloperCity.DataAccess;

import org.hibernate.SessionFactory;

public class HibernateSession {

    private static final SessionFactory sessionFactory;


    static {
        try {
            sessionFactory =
                    new org.hibernate.cfg.Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
