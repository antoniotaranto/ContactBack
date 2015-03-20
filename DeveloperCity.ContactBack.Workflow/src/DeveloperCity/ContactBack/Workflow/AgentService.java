package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.DataAccess.HibernateSession;
import DeveloperCity.ContactBack.DataAccess.AgentDAO;
import DeveloperCity.ContactBack.DataAccess.BreakDAO;
import DeveloperCity.ContactBack.DataAccess.BreakTypeDAO;
import DeveloperCity.ContactBack.DataAccess.WorkTimeDAO;
import DeveloperCity.ContactBack.DomainModel.Agent;
import DeveloperCity.ContactBack.DomainModel.AgentStatus;
import DeveloperCity.ContactBack.DomainModel.Break;
import DeveloperCity.ContactBack.DomainModel.BreakType;
import DeveloperCity.ContactBack.DomainModel.UserStatus;
import DeveloperCity.ContactBack.DomainModel.Utils;
import DeveloperCity.ContactBack.DomainModel.WorkTime;
import DeveloperCity.ContactBack.Exception.AlreadyInBreakException;
import DeveloperCity.ContactBack.Exception.NotInBreakException;
import DeveloperCity.ContactBack.Exception.StartSessionException;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class AgentService {

    private static Logger logger = Logger.getLogger(AgentService.class);

    public AgentService() {
    }

    // <editor-fold defaultstate="collapsed" desc="Get">
    public Agent getByID(long id) {
        Agent retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = AgentDAO.CreateInstance(s).getByID(id);
        tx.commit();
        s.close();

        return retValue;
    }
    public Agent getByIDIncludeLastWorkTime(long id) {
        Agent retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = AgentDAO.CreateInstance(s).getByIDIncludeLastWorkTime(id);
        if (retValue.getWorkTimes().size() > 0) {
            for (DeveloperCity.ContactBack.DomainModel.Break b : retValue.getWorkTimes().get(0).getBreaks()) {
                String foo = b.getBreakType().getDescription();
            }
        }
        tx.commit();
        s.close();

        return retValue;
    }
    public Agent getByAuthentication(String username, String password) {
        Agent retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (Agent) AgentDAO.CreateInstance(s).getByAuthentication(username, password);
        tx.commit();
        s.close();

        return retValue;
    }
    public Agent getByUsername(String username) {
        Agent retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (Agent) AgentDAO.CreateInstance(s).getByUsername(username);
        tx.commit();
        s.close();

        return retValue;
    }
    public Agent getByDirectoryNumber(String directoryNumber) {
        Agent retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = AgentDAO.CreateInstance(s).getByDirectoryNumber(directoryNumber);
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="List">
    @SuppressWarnings("unchecked")
    public List<Agent> getAgentLogged() {
        List<Agent> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Agent>) AgentDAO.CreateInstance(s).getByNotAgentStatus(AgentStatus.NotLogged);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Agent> getAgentLogged(int currentPage, int pageSize) {
        List<Agent> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Agent>) AgentDAO.CreateInstance(s).getByNotAgentStatus(currentPage, pageSize, AgentStatus.NotLogged);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Agent> getAgentNotLogged() {
        List<Agent> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Agent>) AgentDAO.CreateInstance(s).getByAgentStatus(AgentStatus.NotLogged);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Agent> getAgentNotLogged(int currentPage, int pageSize) {
        List<Agent> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Agent>) AgentDAO.CreateInstance(s).getByAgentStatus(currentPage, pageSize, AgentStatus.NotLogged);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Agent> getAgentWorking() {
        List<Agent> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Agent>) AgentDAO.CreateInstance(s).getByNotAgentStatus(AgentStatus.NotLogged, AgentStatus.Break);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Agent> getAgentWorking(int currentPage, int pageSize) {
        List<Agent> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Agent>) AgentDAO.CreateInstance(s).getByNotAgentStatus(currentPage, pageSize, AgentStatus.NotLogged, AgentStatus.Break);
        tx.commit();
        s.close();

        return retValue;
    }
//    public List<Agent> getAgentInCall() {
//        List<Agent> retValue = null;
//
//        Session s = HibernateSession.getSessionFactory().openSession();
//        Transaction tx = s.beginTransaction();
//        retValue = AgentDAO.CreateInstance(s).getByAgentStatus(AgentStatus.InCall);
//        tx.commit();
//        s.close();
//
//        return retValue;
//    }
//    public List<Agent> getAgentInCall(int currentPage, int pageSize) {
//        List<Agent> retValue = null;
//
//        Session s = HibernateSession.getSessionFactory().openSession();
//        Transaction tx = s.beginTransaction();
//        retValue = AgentDAO.CreateInstance(s).getByAgentStatus(currentPage, pageSize, AgentStatus.InCall);
//        tx.commit();
//        s.close();
//
//        return retValue;
//    }
    @SuppressWarnings("unchecked")
    public List<Agent> getAgentInBreak() {
        List<Agent> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Agent>) AgentDAO.CreateInstance(s).getByAgentStatus(AgentStatus.Break);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Agent> getAgentInBreak(int currentPage, int pageSize) {
        List<Agent> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Agent>) AgentDAO.CreateInstance(s).getByAgentStatus(currentPage, pageSize, AgentStatus.Break);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Agent> getAgentFree() {
        List<Agent> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Agent>) AgentDAO.CreateInstance(s).getByAgentStatus(AgentStatus.Available);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Agent> getAgentFree(int currentPage, int pageSize) {
        List<Agent> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Agent>) AgentDAO.CreateInstance(s).getByAgentStatus(currentPage, pageSize, AgentStatus.Available);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Agent> getUserActive() {
        List<Agent> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Agent>) AgentDAO.CreateInstance(s).getByUserStatus(UserStatus.Active);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Agent> getUserActive(int currentPage, int pageSize) {
        List<Agent> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Agent>) AgentDAO.CreateInstance(s).getByUserStatus(currentPage, pageSize, UserStatus.Active);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Agent> getUserInactive() {
        List<Agent> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Agent>) AgentDAO.CreateInstance(s).getByNotUserStatus(UserStatus.Active);
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<Agent> getUserInactive(int currentPage, int pageSize) {
        List<Agent> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<Agent>) AgentDAO.CreateInstance(s).getByNotUserStatus(currentPage, pageSize, UserStatus.Active);
        tx.commit();
        s.close();

        return retValue;
    }
    // <editor-fold defaultstate="collapsed" desc="Scalar">
    public int countAll() {
        int retValue = 0;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = AgentDAO.CreateInstance(s).CountAll();
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Save">
    public Agent setAgent(Agent agent) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            agent = AgentDAO.CreateInstance(s).setAgent(agent);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return agent;
    }
    @SuppressWarnings("unchecked")
    public Agent setAgentReopenSession(Agent agent, long breakTypeID) throws StartSessionException {
        logger.info("setAgentReopenSession");
        Date now = new Date();
        Session s = HibernateSession.getSessionFactory().openSession();
        AgentDAO<Agent> agentDAO = (AgentDAO<Agent>) AgentDAO.CreateInstance(s);
        WorkTimeDAO<WorkTime> workTimeDAO = (WorkTimeDAO<WorkTime>) WorkTimeDAO.CreateInstance(s);
        BreakDAO<Break> breakDAO = (BreakDAO<Break>) BreakDAO.CreateInstance(s);
        BreakTypeDAO<BreakType> breakTypeDAO = (BreakTypeDAO<BreakType>) BreakTypeDAO.CreateInstance(s);

        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            agent = agentDAO.getByID(agent.getUserID());
            WorkTime currentSession = workTimeDAO.getByAgentUnfinished(agent);
            if (currentSession != null) {
                if (agent.getAgentStatus() == AgentStatus.NotLogged) {
                    logger.info("Incorrect Agent Status (setting to Available)");
                    agent.setAgentStatus(AgentStatus.Available);
                    agent = agentDAO.setAgent(agent);
                    tx.commit();
                }
                else {
                    tx.rollback();
                }
                s.close();
                return agent;
            }

            currentSession = workTimeDAO.getByAgentLastOne(agent);
            if (currentSession == null || (!Utils.isToday(currentSession.getLoginTime())) ) {
                throw new StartSessionException();
            }
            
            Date logoff = currentSession.getLogoutTime();

            Break newBreak = new Break();
            newBreak.setWorkTime(currentSession);
            newBreak.setBreakType(breakTypeDAO.getByID(breakTypeID));
            newBreak.setBreakStart(logoff);
            newBreak.setBreakEnd(now);
            newBreak = breakDAO.setBreak(newBreak);

            agent.setAgentStatus(AgentStatus.Available);
            agent = agentDAO.setAgent(agent);
            agent.setRequestingBreak(0);
            agent.setRequestingLogoff(false);

            currentSession.setLogoutTime(null);

            currentSession = workTimeDAO.setWorkTime(currentSession);
            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            logger.error(e);
            throw new StartSessionException();
        } finally {
            s.close();
        }
        return agent;
    }
    @SuppressWarnings("unchecked")
    public Agent setAgentLogin(Agent agent) throws StartSessionException {
        logger.info("setAgentLogin");
        Date now = new Date();
        Session s = HibernateSession.getSessionFactory().openSession();
        AgentDAO<Agent> agentDAO = (AgentDAO<Agent>) AgentDAO.CreateInstance(s);
        WorkTimeDAO<WorkTime> workTimeDAO = (WorkTimeDAO<WorkTime>) WorkTimeDAO.CreateInstance(s);

        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            agent = agentDAO.getByID(agent.getUserID());
            WorkTime currentSession = workTimeDAO.getByAgentUnfinished(agent);
            if (currentSession != null) {
                if (agent.getAgentStatus() == AgentStatus.NotLogged) {
                    logger.info("Incorrect Agent Status (setting to Available)");
                    agent.setAgentStatus(AgentStatus.Available);
                    agent = agentDAO.setAgent(agent);
                    tx.commit();
                }
                else {
                    tx.rollback();
                }
                s.close();
                return agent;
            }

            currentSession = workTimeDAO.getByAgentLastOne(agent);
            if (currentSession != null && Utils.isToday(currentSession.getLoginTime()) ) {
                throw new StartSessionException();
            }

            agent.setAgentStatus(AgentStatus.Available);
            agent = agentDAO.setAgent(agent);
            agent.setRequestingBreak(0);
            agent.setRequestingLogoff(false);

            WorkTime newSession = new WorkTime();
            newSession.setAgent(agent);
            newSession.setLoginTime(now);

            newSession = workTimeDAO.setWorkTime(newSession);
            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            logger.error(e);
            throw new StartSessionException();
        } finally {
            s.close();
        }
        return agent;
    }
    @SuppressWarnings("unchecked")
    public Agent setAgentLogoff(Agent agent) throws Exception {
        logger.info("setAgentLogoff");
        Date now = new Date();
        Session s = HibernateSession.getSessionFactory().openSession();
        AgentDAO<Agent> agentDAO = (AgentDAO<Agent>) AgentDAO.CreateInstance(s);
        BreakDAO<Break> breakDAO = (BreakDAO<Break>) BreakDAO.CreateInstance(s);
        WorkTimeDAO<WorkTime> workTimeDAO = (WorkTimeDAO<WorkTime>) WorkTimeDAO.CreateInstance(s);

        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            agent = agentDAO.getByID(agent.getUserID());
            WorkTime currentSession = workTimeDAO.getByAgentUnfinished(agent);
            if (currentSession == null) {
                if (agent.getAgentStatus() != AgentStatus.NotLogged) {
                    logger.info("Incorrect Agent Status (setting to NotLogged)");
                    agent.setAgentStatus(AgentStatus.NotLogged);
                    agent = agentDAO.setAgent(agent);
                    tx.commit();
                } else {
                    tx.rollback();
                }
                s.close();
                return agent;
            }
            List<Break> openBreaks = breakDAO.getByWorkTimeUnfinished(currentSession);
            for (int i = 0; i < openBreaks.size(); i++) {
                Break b = openBreaks.get(i);
                b.setBreakEnd(now);
                b = breakDAO.setBreak(b);
                openBreaks.set(i, b);
            }
            agent.setAgentStatus(AgentStatus.NotLogged);
            agent = agentDAO.setAgent(agent);
            agent.setRequestingBreak(0);
            agent.setRequestingLogoff(false);
            
            currentSession.setLogoutTime(now);
            currentSession = workTimeDAO.setWorkTime(currentSession);
            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            logger.error(e);
            throw e;
        } finally {
            s.close();
        }
        return agent;
    }
    @SuppressWarnings("unchecked")
    public Agent setStartBreak(Agent agent, long breakTypeID) throws AlreadyInBreakException, Exception {
        logger.info("setStartBreak");
        Session s = HibernateSession.getSessionFactory().openSession();
        AgentDAO<Agent> agentDAO = (AgentDAO<Agent>) AgentDAO.CreateInstance(s);
        BreakDAO<Break> breakDAO = (BreakDAO<Break>) BreakDAO.CreateInstance(s);
        WorkTimeDAO<WorkTime> workTimeDAO = (WorkTimeDAO<WorkTime>) WorkTimeDAO.CreateInstance(s);
        BreakTypeDAO<BreakType> breakTypeDAO = (BreakTypeDAO<BreakType>) BreakTypeDAO.CreateInstance(s);

        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            agent = agentDAO.getByID(agent.getUserID());
            WorkTime currentSession = workTimeDAO.getByAgentUnfinished(agent);
            if (currentSession == null) {
                agent.setRequestingBreak(0);
                agent.setRequestingLogoff(false);
                if (agent.getAgentStatus() != AgentStatus.NotLogged) {
                    logger.info("Incorrect Agent Status (setting to NotLogged)");
                    agent.setAgentStatus(AgentStatus.NotLogged);
                    agent = agentDAO.setAgent(agent);
                    tx.commit();
                }
                else {
                    tx.rollback();
                }
                s.close();
                return agent;
            }
            List<Break> openBreaks = breakDAO.getByWorkTimeUnfinished(currentSession);
            for (Break b : openBreaks) {
                if (!b.getBreakType().isSystemBreak()) {
                    throw new AlreadyInBreakException(agent, b.getBreakType());
                }
            }

            agent.setAgentStatus(AgentStatus.Break);
            agent = agentDAO.setAgent(agent);
            agent.setRequestingBreak(0);
            agent.setRequestingLogoff(false);

            Break newBreak = new Break();
            newBreak.setWorkTime(currentSession);
            newBreak.setBreakType(breakTypeDAO.getByID(breakTypeID));
            newBreak.setBreakStart(new Date());
            newBreak = breakDAO.setBreak(newBreak);
            tx.commit();
        } catch(AlreadyInBreakException e) {
            tx.rollback();
            logger.error(e);
            throw e;
        } catch(Exception e) {
            tx.rollback();
            logger.error(e);
            throw e;
        } finally {
            s.close();
        }
        return agent;
    }
    @SuppressWarnings("unchecked")
    public Agent setEndBreak(Agent agent, long breakID) throws NotInBreakException, Exception {
        logger.info("setEndBreak");
        Session s = HibernateSession.getSessionFactory().openSession();
        AgentDAO<Agent> agentDAO = (AgentDAO<Agent>) AgentDAO.CreateInstance(s);
        BreakDAO<Break> breakDAO = (BreakDAO<Break>) BreakDAO.CreateInstance(s);
        WorkTimeDAO<WorkTime> workTimeDAO = (WorkTimeDAO<WorkTime>) WorkTimeDAO.CreateInstance(s);

        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            agent = agentDAO.getByID(agent.getUserID());
            WorkTime currentSession = workTimeDAO.getByAgentUnfinished(agent);
            if (currentSession == null) {
                agent.setRequestingBreak(0);
                agent.setRequestingLogoff(false);
                if (agent.getAgentStatus() != AgentStatus.NotLogged) {
                    logger.info("Incorrect Agent Status (setting to NotLogged)");
                    agent.setAgentStatus(AgentStatus.NotLogged);
                    agent = agentDAO.setAgent(agent);
                    tx.commit();
                } else {
                    tx.rollback();
                }
                s.close();
                return agent;
            }
            Break breakToClose = null;
            boolean systemBreak = false;
            List<Break> openBreaks = breakDAO.getByWorkTimeUnfinished(currentSession);
            for (int i = 0; i < openBreaks.size(); i++) {
                Break b = openBreaks.get(i);
                if (breakID == b.getBreakID() && (!b.isSystemBreak())) {
                    breakToClose = b;
                }
                else if (b.isSystemBreak()) {
                    systemBreak = true;
                }
            }

            if (breakToClose == null) {
                throw new NotInBreakException(agent);
            }

            agent.setAgentStatus(systemBreak ? AgentStatus.NotReady : AgentStatus.Available);
            agent = agentDAO.setAgent(agent);
            agent.setRequestingBreak(0);
            agent.setRequestingLogoff(false);

            breakToClose.setBreakEnd(new Date());
            breakToClose = breakDAO.setBreak(breakToClose);
            tx.commit();
        } catch(NotInBreakException e) {
            tx.rollback();
            logger.error(e);
            throw e;
        } catch(Exception e) {
            tx.rollback();
            logger.error(e);
            throw e;
        } finally {
            s.close();
        }
        return agent;
    }
    @SuppressWarnings("unchecked")
    public Agent setAgentPhoneInService(Agent agent, long deviceOffBreak) throws Exception {
        logger.info("setAgentPhoneInService");
        Date now = new Date();
        Session s = HibernateSession.getSessionFactory().openSession();
        AgentDAO<Agent> agentDAO = (AgentDAO<Agent>) AgentDAO.CreateInstance(s);
        BreakDAO<Break> breakDAO = (BreakDAO<Break>) BreakDAO.CreateInstance(s);
        WorkTimeDAO<WorkTime> workTimeDAO = (WorkTimeDAO<WorkTime>) WorkTimeDAO.CreateInstance(s);

        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            agent = agentDAO.getByID(agent.getUserID());
            WorkTime currentSession = workTimeDAO.getByAgentUnfinished(agent);
            if (currentSession == null) {
                if (agent.getAgentStatus() != AgentStatus.NotLogged) {
                    logger.info("Incorrect Agent Status (setting to NotLogged)");
                    agent.setAgentStatus(AgentStatus.NotLogged);
                    agent = agentDAO.setAgent(agent);
                    tx.commit();
                } else {
                    tx.rollback();
                }
                s.close();
                return agent;
            }
            List<Break> openBreaks = breakDAO.getByWorkTimeUnfinished(currentSession);
            for (int i = 0; i < openBreaks.size(); i++) {
                Break b = openBreaks.get(i);
                if (b.getBreakType().getBreakTypeID() == deviceOffBreak) {
                    b.setBreakEnd(now);
                    b = breakDAO.setBreak(b);
                    openBreaks.set(i, b);
                }
            }

            if (agent.getAgentStatus() == AgentStatus.NotReady) {
                agent.setAgentStatus(AgentStatus.Available);
                agent = agentDAO.setAgent(agent);
            }
            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            logger.error(e);
            throw e;
        } finally {
            s.close();
        }
        return agent;
    }
    @SuppressWarnings("unchecked")
    public Agent setAgentPhoneOutOfService(Agent agent, long deviceOffBreak) throws Exception {
        logger.info("setAgentPhoneOutOfService");
        Session s = HibernateSession.getSessionFactory().openSession();
        AgentDAO<Agent> agentDAO = (AgentDAO<Agent>) AgentDAO.CreateInstance(s);
        BreakDAO<Break> breakDAO = (BreakDAO<Break>) BreakDAO.CreateInstance(s);
        WorkTimeDAO<WorkTime> workTimeDAO = (WorkTimeDAO<WorkTime>) WorkTimeDAO.CreateInstance(s);
        BreakTypeDAO<BreakType> breakTypeDAO = (BreakTypeDAO<BreakType>) BreakTypeDAO.CreateInstance(s);

        org.hibernate.Transaction tx = s.beginTransaction();
        try {
            agent = agentDAO.getByID(agent.getUserID());
            WorkTime currentSession = workTimeDAO.getByAgentUnfinished(agent);
            if (currentSession == null) {
                if (agent.getAgentStatus() != AgentStatus.NotLogged) {
                    logger.info("Incorrect Agent Status (setting to NotLogged)");
                    agent.setAgentStatus(AgentStatus.NotLogged);
                    agent = agentDAO.setAgent(agent);
                    tx.commit();
                } else {
                    tx.rollback();
                }
                s.close();
                return agent;
            }
            List<Break> openBreaks = breakDAO.getByWorkTimeUnfinished(currentSession);
            for (Break b : openBreaks) {
                if (b.getBreakType().getBreakTypeID() == deviceOffBreak) {
                    throw new AlreadyInBreakException(agent, b.getBreakType());
                }
            }

            if (agent.getAgentStatus() == AgentStatus.Available || agent.getAgentStatus() == AgentStatus.NotLogged) {
                agent.setAgentStatus(AgentStatus.NotReady);
                agent = agentDAO.setAgent(agent);
            }
            Break newBreak = new Break();
            newBreak.setWorkTime(currentSession);
            newBreak.setBreakType(breakTypeDAO.getByID(deviceOffBreak));
            newBreak.setBreakStart(new Date());
            newBreak.setSystemBreak(true);
            newBreak = breakDAO.setBreak(newBreak);
            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            logger.error(e);
            throw e;
        } finally {
            s.close();
        }
        return agent;
    }
    @SuppressWarnings("unchecked")
    public Agent setAgentPassword(long id, String plainCurrentPassword, String plainPassword, String confirmPassword) throws Exception {
        if (plainPassword == null || plainPassword.trim().length() < 5) {
            throw new Exception ("Password must have at least 5 characteres.");
        }

        if (! plainPassword.equals(confirmPassword)) {
            throw new Exception ("Password and confirmation are different.");
        }

        Session s = HibernateSession.getSessionFactory().openSession();
        AgentDAO<Agent> agentDAO = (AgentDAO<Agent>) AgentDAO.CreateInstance(s);
        org.hibernate.Transaction tx = s.beginTransaction();
        Agent agent = null;
        try {
            agent = agentDAO.getByID(id);

            if (agent == null) {
                throw new Exception("Agent not found.");
            }

            if (!(DeveloperCity.Security.Crypto.EncryptForever(plainCurrentPassword).equals(agent.getPassword()))) {
                throw new Exception("Current password is not valid.");
            }

            agent.setPassword(DeveloperCity.Security.Crypto.EncryptForever(plainPassword));
            agent = agentDAO.setAgent(agent);
            tx.commit();
        } catch(Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return agent;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="FetchChild">
    public void FetchPermissions(Agent agent) {
        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        s.lock(agent, LockMode.NONE);
        boolean i = agent.getPermissions().isEmpty();
        tx.commit();
        s.close();
    }
    public void FetchPermissions(List<Agent> agents) {
        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        boolean i;
        for (Agent agent : agents) {
            s.lock(agent, LockMode.NONE);
            i = agent.getPermissions().isEmpty();
        }
        tx.commit();
        s.close();
    }
    public void FetchWorkTimes(Agent agent) {
        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        s.lock(agent, LockMode.NONE);
        boolean i = agent.getWorkTimes().isEmpty();
        tx.commit();
        s.close();
    }
    public void FetchWorkTimes(List<Agent> agents) {
        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        boolean i;
        for (Agent agent : agents) {
            s.lock(agent, LockMode.NONE);
            i = agent.getWorkTimes().isEmpty();
        }
        tx.commit();
        s.close();
    }
    // </editor-fold>
}