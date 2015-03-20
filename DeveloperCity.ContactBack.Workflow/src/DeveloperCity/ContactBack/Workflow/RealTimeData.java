/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.ContactBack.DomainModel.*;
import DeveloperCity.ContactBack.DomainModel.IVR.PhoneNumber;
import DeveloperCity.ContactBack.Exception.AlreadyInBreakException;
import DeveloperCity.ContactBack.Exception.NotInBreakException;
import DeveloperCity.ContactBack.Workflow.Telephony.SMSSender;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import net.sf.gilead.core.hibernate.HibernateUtil;
import net.sf.gilead.core.store.NoProxyStore;
import net.sf.gilead.core.PersistentBeanManager;

/**
 *
 * @author lbarbosa
 */
public class RealTimeData {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private static Logger logger = Logger.getLogger(RealTimeData.class);
    private ApplicationDiagnostics applicationDiagnostics;
    private List<Queue> queue;
    private List<Agent> agentCollection;
    private List<Call> currentCalls;
    private List<Priority> priorityPolicy;
    private List<BlackList> blackListPolicy;
    private HolidayList Holidays;
    private SetupService sSetup;
    private SMSSender smsSender;
    private final Object syncQueue = new Object();
    private final Object syncAgent = new Object();
    private final Object syncCall = new Object();
    private final Object syncPriority = new Object();
    private final Object syncBlackList = new Object();
    private final Object syncHolidays = new Object();
    // </editor-fold>

    public RealTimeData(List<Queue> queue, List<Agent> agents, List<Call> currentCalls, SetupService sSetup) {
        synchronized(syncQueue) {
            synchronized(syncAgent) {
                synchronized(syncCall) {
                    this.queue = queue;
                    this.agentCollection = agents;
                    this.currentCalls = currentCalls;
                    this.sSetup = sSetup;
                }
            }
        }
    }

    // <editor-fold defaultstate="collapsed" desc="Get volatile data">
    // <editor-fold defaultstate="collapsed" desc="Agent">
    public Agent getAgentByExtension(String extension) {
        synchronized (syncAgent) {
            return getAgentByExtensionUnsync(extension);
        }
    }
    private Agent getAgentByExtensionUnsync(String extension) {
        Agent retValue = null;
        for (Agent a : agentCollection) {
            if (a.getDirectoryNumber().equals(extension)) {
                retValue = a;
                break;
            }
        }
        return retValue;
    }
    public Agent getAgentByTerminal(String terminal) {
        synchronized (syncAgent) {
            return getAgentByTerminalUnsync(terminal);
        }
    }
    private Agent getAgentByTerminalUnsync(String terminal) {
        Agent retValue = null;
        for (Agent a : agentCollection) {
            if (a.getTerminal().equals(terminal)) {
                retValue = a;
                break;
            }
        }
        return retValue;
    }
    public List<Agent> getAllAgents() {
        return this.agentCollection;
    }
    public Agent getAgentByID(long id) {
        synchronized(syncAgent) {
            return getAgentByIDUnsync(id);
        }
    }
    private Agent getAgentByIDUnsync(long id) {
        for(Agent a : agentCollection) {
            if (a.getUserID() == id) {
                return a;
            }
        }
        return null;
    }
    public List<Agent> getFreeAgents() {
        synchronized (syncAgent) {
            return getFreeAgentsUnsync();
        }
    }
    private List<Agent> getFreeAgentsUnsync() {
        List<Agent> retValue = new ArrayList<Agent>();

        // <editor-fold defaultstate="collapsed" desc="Filter">
        for (Agent a : agentCollection) {
            if (a.getAgentStatus() == AgentStatus.Available && a.getCallManagerCallIDs().isEmpty()) {
                retValue.add(a);
            }
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sort">
        java.util.Collections.sort(retValue);
        // </editor-fold>

        return retValue;
    }
    public String getReadOnlyAgents() throws IOException {
        Agent[] agentCollectionClone = agentCollection.toArray( new Agent[0] );
        java.util.Arrays.sort(agentCollectionClone, new Comparator<Agent>() {
            public int compare(Agent o1, Agent o2) {
                if (o1 == null) {
                    return 1;
                } else if (o2 == null) {
                    return -1;
                } else if (o1.getAgentStatus() == o2.getAgentStatus()) {
                    if (o1.getLastCallTime() == null) {
                        return 1;
                    } else if (o2.getLastCallTime() == null) {
                        return -1;
                    } else {
                        return o1.getLastCallTime().compareTo(o2.getLastCallTime());
                    }
                } else {
                    if (o1.getAgentStatus() == AgentStatus.NotLogged) {
                        return 1;
                    } else if (o2.getAgentStatus() == AgentStatus.NotLogged) {
                        return -1;
                    } else if (o1.getAgentStatus() == AgentStatus.Break) {
                        return 1;
                    } else if (o2.getAgentStatus() == AgentStatus.Break) {
                        return -1;
                    } else if (o1.getAgentStatus() == AgentStatus.NotReady) {
                        return 1;
                    } else if (o2.getAgentStatus() == AgentStatus.NotReady) {
                        return -1;
                    }

                    return o1.getLastCallTime().compareTo(o2.getLastCallTime());
                }
            }
        });
        List<String> includes = new ArrayList<String>();
        includes.add("callManagerCallIDs");
        return hibernateToJson(agentCollectionClone, includes, null);
    }
    public String getReadOnlyAgent(long agentID) throws IOException {
        Agent[] agentCollectionClone = agentCollection.toArray( new Agent[0] );
        Agent chosenOne = null;
        for (Agent a : agentCollectionClone) {
            if (a.getUserID() == agentID) {
                chosenOne = a;
                break;
            }
        }
        if (chosenOne == null) {
            return "";
        }
        List<String> includes = new ArrayList<String>();
        includes.add("callManagerCallIDs");
        return hibernateToJson(chosenOne, includes, null);
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Queue">
    private Queue getQueueByIDUnsync(long id) {
        for(Queue q : queue) {
            if (q.getQueueID() == id) {
                return q;
            }
        }

        return null;
    }
    public List<Queue> getPendentQueue() {
        synchronized (syncQueue) {
            return getPendentQueueUnsync();
        }
    }
    private List<Queue> getPendentQueueUnsync() {
        List<Queue> retValue = new ArrayList<Queue>();

        // <editor-fold defaultstate="collapsed" desc="Filter">
        for (Queue q : queue) {
            if (q.getQueueStatus() != QueueStatus.InQueue) {
                continue;
            }
            if (q.isFrozen()) {
                continue;
            }

            retValue.add(q);
        }
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Sort">
        java.util.Collections.sort(retValue);
        // </editor-fold>

        return retValue;
    }
    public String getReadOnlyQueue() throws IOException {
        Object[] queueClone = queue.toArray();
        return hibernateToJson(queueClone);
    }
    public String getReadOnlyQueueCount() throws IOException {
        Queue[] queueCollectionClone = queue.toArray( new Queue[0] );
        Call[] callCollectionClone = currentCalls.toArray( new Call[0] );
        int total = queueCollectionClone.length;
        int inqueue = 0;
        int others = 0;
        int frozen = 0;
        int incall = 0;
        Date now = new Date();
        for (Queue q : queueCollectionClone) {
            if (q.getQueueStatus() == q.getQueueStatus().InQueue && q.getDontCallBefore().getTime() > now.getTime()) {
                frozen++;
            } else if (q.getQueueStatus() == q.getQueueStatus().InQueue) {
                inqueue++;
            }
        }
        for (Call c : callCollectionClone) {
            if (c.getEndTime() == null && c.getAnswerTime() != null && c.getCallStatus() == CallStatus.Talking) {
                incall++;
            }
        }
        others = total - (frozen + inqueue);
        SimpleStatus simpleStatus = new SimpleStatus(inqueue, frozen, incall, others, total + incall);
        return hibernateToJson(simpleStatus);
    }
    public Queue getQueueByPhoneNumber(String phoneNumber) {
        synchronized (syncQueue) {
            return getQueueByPhoneNumberUnsync(phoneNumber);
        }
    }
    private Queue getQueueByPhoneNumberUnsync(String phoneNumber) {
        for (Queue q : this.queue) {
            if (q.isFrom(phoneNumber) &&
                    q.getQueueStatus() != QueueStatus.Complete &&
                    q.getQueueStatus() != QueueStatus.AutoRemoved &&
                    q.getQueueStatus() != QueueStatus.ManuallyRemoved) {
                return q;
            }
        }
        return null;
    }
    public Queue getQueueByPhoneNumberOrCustomer(long customerID, String phoneNumber) {
        synchronized (syncQueue) {
            return getQueueByPhoneNumberOrCustomerUnsync(customerID, phoneNumber);
        }
    }
    private Queue getQueueByPhoneNumberOrCustomerUnsync(long customerID, String phoneNumber) {
        for (Queue q : this.queue) {
            if (q.isFrom(phoneNumber) &&
                    q.getQueueStatus() != QueueStatus.Complete &&
                    q.getQueueStatus() != QueueStatus.AutoRemoved &&
                    q.getQueueStatus() != QueueStatus.ManuallyRemoved) {
                return q;
            }
            if (q instanceof WebQueue) {
                WebQueue wq = (WebQueue) q;
                if (wq.getCustomer().getUserID() == customerID &&
                        q.getQueueStatus() != QueueStatus.Complete &&
                        q.getQueueStatus() != QueueStatus.AutoRemoved &&
                        q.getQueueStatus() != QueueStatus.ManuallyRemoved) {
                    return wq;
                }
            }
        }
        return null;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Call">
    public Call getCallByCCMID(long ccmID) {
        synchronized(syncCall) {
            return getCallByCCMIDUnsync(ccmID);
        }
    }
    private Call getCallByCCMIDUnsync(long ccmID) {
        for (Call c : currentCalls) {
            if (c.getCallManagerCallID() == ccmID) {
                return c;
            }
        }
        return null;
    }
    public String getReadOnlyCurrentCalls() throws IOException {
        Object[] currentCallsClone = currentCalls.toArray();
        return hibernateToJson(currentCallsClone);
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Setup">
    public Setup getSetup() {
        return sSetup.getSetup();
    }
    public ApplicationDiagnostics getApplicationDiagnostics() {
        return applicationDiagnostics;
    }
    public List<Priority> getPriorityPolicy() {
        synchronized(syncPriority) {
            return getPriorityPolicyUnsync();
        }
    }
    private List<Priority> getPriorityPolicyUnsync() {
        return priorityPolicy;
    }
    public Priority getPriorityPolicyByNumber(String number) {
        synchronized(syncPriority) {
            List<Priority> policy = getPriorityPolicyUnsync();
            Priority retValue = null;
            for (Priority p : policy) {
                if (p.Match(number)) {
                    if (p.getMatchMode() == MatchMode.Exact) {
                        return p;
                    } else if (retValue == null || retValue.getMatchMode() == MatchMode.Contains) {
                        retValue = p;
                    }
                }
            }
            return retValue;
        }
    }
    public String getReadOnlyPriorityPolicy() throws IOException {
        Object[] priorityPolicyClone = priorityPolicy.toArray();
        List<String> includes = new ArrayList<String>();
        includes.add("weekdays");
        return hibernateToJson(priorityPolicyClone, includes, null);
    }
    public List<BlackList> getBlackList() {
        synchronized(syncBlackList) {
            return getBlackListUnsync();
        }
    }
    private List<BlackList> getBlackListUnsync() {
        return blackListPolicy;
    }
    public BlackList getBlackListPolicyByNumber(String number) {
        synchronized(syncBlackList) {
            List<BlackList> policy = getBlackListUnsync();
            BlackList retValue = null;
            for (BlackList p : policy) {
                if (p.Match(number)) {
                    if (p.getMatchMode() == MatchMode.Exact) {
                        return p;
                    } else if (retValue == null || retValue.getMatchMode() == MatchMode.Contains) {
                        retValue = p;
                    }
                }
            }
            return retValue;
        }
    }
    public String getReadOnlyBlackList() throws IOException {
        Object[] blackListPolicyClone = blackListPolicy.toArray();
        List<String> includes = new ArrayList<String>();
        includes.add("weekdays");
        return hibernateToJson(blackListPolicyClone, includes, null);
    }
    public boolean isHolidayToday() {
        boolean retValue = false;
        synchronized(syncHolidays) {
            retValue = Holidays.isHoliday(new Date());
        }
        return retValue;
    }
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Change volatile data">
    // <editor-fold defaultstate="collapsed" desc="Agent">
    public void addAgentCall(Agent agent, long callManagerID) {
        synchronized (syncAgent) {
            addAgentCallUnsync(agent, callManagerID);
        }
    }
    private void addAgentCallUnsync(Agent agent, long callManagerID) {
        if (!agentCollection.contains(agent)) {
            return;
        }
        Agent currentAgent = getAgentByID(agent.getUserID());
        currentAgent.getCallManagerCallIDs().add(callManagerID);
    }
    public void removeAgentCall(Agent agent, long callManagerID) {
        synchronized (syncAgent) {
            removeAgentCallUnsync(agent, callManagerID);
        }
    }
    private void removeAgentCallUnsync(Agent agent, long callManagerID) {
        if (!agentCollection.contains(agent)) {
            return;
        }
        Agent currentAgent = getAgentByID(agent.getUserID());
        currentAgent.getCallManagerCallIDs().remove(callManagerID);
        
        if (currentAgent.getCallManagerCallIDs().isEmpty()) {
            if (currentAgent.getRequestingBreak() != 0) {
                StartRequestedAgentBreak(currentAgent);
            }
            else if (currentAgent.getRequestingLogoff()) {
                EndRequestedAgentSession(currentAgent);
            }
        }
    }
    private void updateAgentCollection(Agent newAgentStatus) {
        logger.info(String.format("updateAgentList(%s)", newAgentStatus));
        logger.debug(String.format("Status: %s", newAgentStatus.getAgentStatus()));
        int index = agentCollection.indexOf(newAgentStatus);
        if (index < 0) {
            if (newAgentStatus.getUserStatus() == UserStatus.Active) {
                agentCollection.add(newAgentStatus);
            }
        } else {
            if (newAgentStatus.getUserStatus() == UserStatus.Active) {
                Agent a = agentCollection.get(index);
                newAgentStatus.setCallManagerCallIDs(a.getCallManagerCallIDs());
                agentCollection.set(index, newAgentStatus);
            } else {
                agentCollection.remove(index);
            }
        }
        logger.info("updateAgentList(Agent) !");
    }
    public String changeAgent(Agent agent, CallManagerService sCcm, AgentManager sAgent) {
        boolean isNew = (agent.getUserID() == 0);
        boolean isBlocked = (agent.getUserStatus() != UserStatus.Active);
        boolean wasCollected = false;
        Agent original = null;
        if (!isNew) {
            // UPDATE
            synchronized(syncAgent) {
                original = getAgentByIDUnsync(agent.getUserID());
                wasCollected = (original != null);

                if (wasCollected && isBlocked) {
                    return updateAgentSituation1(agent, sCcm, sAgent, original);
                } else if(wasCollected && (!isBlocked)) {
                    return updateAgentSituation2(agent, sCcm, sAgent, original);
                } else if (isBlocked) {
                    return updateAgentSituation3(agent, sCcm, sAgent);
                } else {
                    return updateAgentSituation4(agent, sCcm, sAgent);
                }
            }
        } else {
            // INSERT
            if (isBlocked) {
                return insertAgentSituation1(agent, sCcm, sAgent);
            } else {
                return insertAgentSituation2(agent, sCcm, sAgent);
            }
        }
    }
    private String updateAgentSituation1(Agent agent, CallManagerService sCcm, AgentManager sAgent, Agent original) {
        // Was active but is not anymore = Deactivation
        if (original.getAgentStatus() != AgentStatus.NotLogged) { return "AgentInUseException"; }
        agent = persistAgent(sAgent, agent);
        if (agent == null) { return "AgentInUseException"; }
        boolean ccmSuccess = unregisterAgent(agent, sCcm);
        if (!ccmSuccess) { persistAgent(sAgent, original); return "InvalidDirectoryNumberException"; }
        updateAgentCollection(agent);
        return "Success";
    }
    private String updateAgentSituation2(Agent agent, CallManagerService sCcm, AgentManager sAgent, Agent original) {
        // Was active and still is = Change volatile data
        if (original.getAgentStatus() != AgentStatus.NotLogged) { return "AgentInUseException"; }
        boolean inUse = isTerminalOrExtensionReserved(agent.getTerminal(), agent.getDirectoryNumber(), agent.getUserID());
        if (inUse) { return "InvalidTerminalException"; }
        boolean isCorrect = isTerminalAndExtensionCorrect(agent.getTerminal(), agent.getDirectoryNumber(), sCcm);
        if (!isCorrect) { return "InvalidDirectoryNumberException"; }
        agent = persistAgent(sAgent, agent);
        if (agent == null) { return "AgentInUseException"; }
        boolean ccmSuccess = unregisterAgent(agent, sCcm);
        if (!ccmSuccess) { persistAgent(sAgent, original); return "InvalidDirectoryNumberException"; }
        ccmSuccess = registerAgent(agent, sCcm);
        if (!ccmSuccess) { persistAgent(sAgent, original); return "InvalidDirectoryNumberException"; }
        updateAgentCollection(agent);
        return "Success";
    }
    private String updateAgentSituation3(Agent agent, CallManagerService sCcm, AgentManager sAgent) {
        // Was blocked and still is = Update just in database
        agent = persistAgent(sAgent, agent);
        if (agent == null) { return "AgentInUseException"; }
        return "Success";
    }
    private String updateAgentSituation4(Agent agent, CallManagerService sCcm, AgentManager sAgent) {
        // Was blocked but is not anymore = Reativation
        boolean inUse = isTerminalOrExtensionReserved(agent.getTerminal(), agent.getDirectoryNumber(), agent.getUserID());
        if (inUse) { return "InvalidTerminalException"; }
        boolean isCorrect = isTerminalAndExtensionCorrect(agent.getTerminal(), agent.getDirectoryNumber(), sCcm);
        if (!isCorrect) { return "InvalidDirectoryNumberException"; }
        agent = persistAgent(sAgent, agent);
        if (agent == null) { return "AgentInUseException"; }
        boolean ccmSuccess = registerAgent(agent, sCcm);
        if (!ccmSuccess) { agent.setUserStatus(UserStatus.Deleted); persistAgent(sAgent, agent); return "InvalidDirectoryNumberException"; }
        updateAgentCollection(agent);
        return "Success";
    }
    private String insertAgentSituation1(Agent agent, CallManagerService sCcm, AgentManager sAgent) {
        // New blocked agent = Update just in database
        agent = persistAgent(sAgent, agent);
        if (agent == null) { return "AgentInUseException"; }
        return "Success";
    }
    private String insertAgentSituation2(Agent agent, CallManagerService sCcm, AgentManager sAgent) {
        // New active agent
        synchronized(syncAgent) {
            boolean inUse = isTerminalOrExtensionReserved(agent.getTerminal(), agent.getDirectoryNumber(), 0);
            if (inUse) { return "InvalidTerminalException"; }
            boolean isCorrect = isTerminalAndExtensionCorrect(agent.getTerminal(), agent.getDirectoryNumber(), sCcm);
            if (!isCorrect) { return "InvalidDirectoryNumberException"; }
            agent = persistAgent(sAgent, agent);
            if (agent == null) { return "AgentInUseException"; }
            boolean ccmSuccess = registerAgent(agent, sCcm);
            if (!ccmSuccess) { agent.setUserStatus(UserStatus.Deleted); persistAgent(sAgent, agent); return "InvalidDirectoryNumberException"; }
            updateAgentCollection(agent);
        }
        return "Success";
    }
    private boolean isTerminalOrExtensionReserved(String terminal, String extension, long agentAllowed) {
        for (Agent a : agentCollection) {
            if (a.getUserID() == agentAllowed) { continue; }
            if (a.getTerminal().equalsIgnoreCase(terminal)) { return true; }
            if (a.getDirectoryNumber().equalsIgnoreCase(extension)) { return true; }
        }
        return false;
    }
    private boolean isTerminalAndExtensionCorrect(String terminal, String extension, CallManagerService sCcm) {
        return sCcm.testDeviceAndExtension(terminal, extension);
    }
    private Agent persistAgent(AgentManager sAgent, Agent a) {
        try { a = sAgent.setAgent(a); return a; }
        catch(Exception e) { return null; }
    }
    private boolean registerAgent(Agent a, CallManagerService sCcm) {
        return sCcm.registerAgentDevice(a);
    }
    private boolean unregisterAgent(Agent a, CallManagerService sCcm) {
        return sCcm.unregisterAgentDevice(a.getUserID());
    }

    // <editor-fold defaultstate="collapsed" desc="Agent Break">
    public boolean StartAgentBreak(long agentID, long breakTypeID) {
        synchronized (syncAgent) {
            Agent agent = getAgentByIDUnsync(agentID);
            if (agent == null) {
                return false;
            }
            if (agent.getAgentStatus() == AgentStatus.Break ||
                agent.getAgentStatus() == AgentStatus.NotLogged ||
                agent.getRequestingLogoff() ||
                agent.getRequestingBreak() != 0) {
                return false;
            }
            agent.setRequestingBreak(breakTypeID);
            if (agent.getCallManagerCallIDs().size() > 0) {
                // in a call, do it later
                return true;
            }

            StartRequestedAgentBreak(agent);
            return true;
        }
    }
    private void StartRequestedAgentBreak(Agent agent) {
        long breakTypeID = agent.getRequestingBreak();
        AgentService sAgent = new AgentService();
        agent.setRequestingBreak(0);
        agent.setRequestingLogoff(false);
        try {
            updateAgentCollection(sAgent.setStartBreak(agent, breakTypeID));
        } catch (AlreadyInBreakException ex) {
            agent = ex.getAgent();
            agent.setRequestingBreak(0);
            agent.setRequestingLogoff(false);
            updateAgentCollection(agent);
            logger.error(ex);
        } catch (Exception ex) {
            logger.error(ex);
        }
    }
    public boolean EndAgentBreak(long agentID, long breakID) {
        synchronized (syncAgent) {
            Agent agent = getAgentByIDUnsync(agentID);
            if (agent == null) {
                return false;
            }
            if (agent.getAgentStatus() == AgentStatus.NotLogged) {
                return false;
            }

            agent.setRequestingBreak(0);
            agent.setRequestingLogoff(false);
            // Setting correctly on sAgent.setEndBreak
//            agent.setAgentStatus(AgentStatus.Available); // --> Verificar se há system breaks
            AgentService sAgent = new AgentService();
            try {
                updateAgentCollection(sAgent.setEndBreak(agent, breakID));
            } catch(NotInBreakException ex) {
                agent = ex.getAgent();
                agent.setRequestingBreak(0);
                agent.setRequestingLogoff(false);
                updateAgentCollection(agent);
                logger.error(ex);
            } catch(Exception ex) {
                logger.error(ex);
            }
            return true;
        }
    }

    public void StartAgentSystemBreak(long agentID, long breakTypeID) {
        synchronized (syncAgent) {
            Agent agent = getAgentByIDUnsync(agentID);
            if (agent == null) {
                return;
            }
            if (agent.getAgentStatus() == AgentStatus.NotReady || agent.getAgentStatus() == AgentStatus.NotLogged) {
                return;
            }

            AgentService sAgent = new AgentService();
            try {
                updateAgentCollection(sAgent.setAgentPhoneOutOfService(agent, breakTypeID));
            } catch (AlreadyInBreakException ex) {
                updateAgentCollection(ex.getAgent());
                logger.error(ex);
            } catch (Exception ex) {
                logger.error(ex);
            }
        }
    }
    public void EndAgentSystemBreak(long agentID, long breakTypeID) {
        synchronized (syncAgent) {
            Agent agent = getAgentByIDUnsync(agentID);
            if (agent == null) {
                return;
            }
            if (agent.getAgentStatus() == AgentStatus.NotLogged) {
                return;
            }

            AgentService sAgent = new AgentService();
            try {
                updateAgentCollection(sAgent.setAgentPhoneInService(agent, breakTypeID));
            } catch(NotInBreakException ex) {
                updateAgentCollection(ex.getAgent());
                logger.error(ex);
            } catch(Exception ex) {
                logger.error(ex);
            }
        }
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Agent Session">
    public boolean StartAgentSession(long agentID, CallManagerService sCallManager) {
        synchronized (syncAgent) {
            Agent agent = getAgentByIDUnsync(agentID);
            if (agent == null) {
                logger.warn(String.format("Agent %d trying to log in, but it's null", agentID));
                return false;
            }
            if (agent.getAgentStatus() != AgentStatus.NotLogged) {
                logger.warn(String.format("Agent %s trying to log in, but current status is %s", agent.getName(), agent.getAgentStatus()));
                return false;
            }
            if (!sCallManager.IsInService(agent.getTerminal())) {
                logger.warn(String.format("Agent %s trying to log in, but device %s is not in service", agent.getName(), agent.getTerminal()));
                return false;
            }

            AgentService sAgent = new AgentService();
            try{
                updateAgentCollection(sAgent.setAgentLogin(agent));
            } catch(Exception ex) {
                logger.error(ex);
                return false;
            }
            return true;
        }
    }
    public boolean RestartAgentSession(long agentID, long breakTypeID, CallManagerService sCallManager) {
        synchronized (syncAgent) {
            Agent agent = getAgentByIDUnsync(agentID);
            if (agent == null) {
                logger.warn(String.format("Agent %d trying to relog in, but it's null", agentID));
                return false;
            }
            if (agent.getAgentStatus() != AgentStatus.NotLogged) {
                logger.warn(String.format("Agent %s trying to relog in, but current status is %s", agent.getName(), agent.getAgentStatus()));
                return false;
            }
            if (!sCallManager.IsInService(agent.getTerminal())) {
                logger.warn(String.format("Agent %s trying to relog in, but device %s is not in service", agent.getName(), agent.getTerminal()));
                return false;
            }

            agent.setAgentStatus(AgentStatus.Available);
            AgentService sAgent = new AgentService();
            try{
                updateAgentCollection(sAgent.setAgentReopenSession(agent, breakTypeID));
            } catch(Exception ex) {
                logger.error(ex);
                return false;
            }
            return true;
        }
    }
    public boolean EndAgentSession(long agentID) {
        synchronized (syncAgent) {
            Agent agent = getAgentByIDUnsync(agentID);
            if (agent == null) {
                logger.warn(String.format("Agent %d trying to log out, but it's null", agentID));
                return false;
            }
            if (agent.getAgentStatus() == AgentStatus.NotLogged ||
                agent.getRequestingLogoff() ||
                agent.getRequestingBreak() != 0) {
                logger.warn(String.format("Agent %s trying to log out, but current status is %s", agent.getName(), agent.getAgentStatus()));
                return false;
            }

            agent.setRequestingLogoff(true);
            if (agent.getCallManagerCallIDs().size() > 0) {
                // in a call, do it later
                return true;
            }
            EndRequestedAgentSession(agent);
            return true;
        }
    }
    public void EndRequestedAgentSession(Agent agent) {
        AgentService sAgent = new AgentService();
        agent.setRequestingBreak(0);
        agent.setRequestingLogoff(false);
        try {
            updateAgentCollection(sAgent.setAgentLogoff(agent));
        } catch (Exception ex) {
            updateAgentCollection(agent);
            logger.error(ex);
        }
    }
    // </editor-fold>
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Queue">
    private void updateQueueList(Queue queueItem) {
        logger.info(String.format("updateQueueList(%s)", queueItem));
        if (queueItem.getQueueStatus() == QueueStatus.AutoRemoved ||
            queueItem.getQueueStatus() == QueueStatus.ManuallyRemoved ||
            queueItem.getQueueStatus() == QueueStatus.Complete) {
            logger.info("Remove Queue Item");
            queue.remove(queueItem);
        }
        else {
            logger.info("Update Queue Item");
            queue.set(queue.indexOf(queueItem), queueItem);
        }
        logger.info("updateQueueList(Queue) !");
    }
    public boolean EnQueue(long callManagerCallID, PhoneNumber originalNumber, PhoneNumber callBackNumber, CallBackNumberType callBackNumberType, QueueManager queueManager) throws Exception {
        synchronized (syncQueue) {
            if (callBackNumber == null) {
                callBackNumber = originalNumber;
            }
            if (!callBackNumber.isValid()) {
                return false;
            }
            Queue alreadyInQueue = getQueueByPhoneNumberUnsync(callBackNumber.getNumberToDial());

            if (alreadyInQueue != null) {
                alreadyInQueue = queueManager.QueueCallingAgain(alreadyInQueue, callManagerCallID);
                return false;
            }
            else {
                Priority priority = getPriorityPolicyByNumber(callBackNumber.getNumberToDial());
                short PriorityValue = priority == null ? 0 : priority.getPriorityValue();
                Queue newQueueItem = queueManager.QueueFirstTime(callManagerCallID, originalNumber, callBackNumber, callBackNumberType, Queue.estimateTimeToAttend(queue, agentCollection, currentCalls), queue.size(), PriorityValue );
                this.queue.add(newQueueItem);
                return true;
            }
        }
    }
    public boolean Refresh(List<Queue> queue) {
        Queue[] oldQueue = new Queue[0];
        synchronized (syncQueue) {
            oldQueue = this.queue.toArray(oldQueue);
            this.queue = queue;
        }
        return true;
    }
    private void queueItemRemoved(Queue currentQueue) {
        logger.info("queueItemRemoved(Queue currentQueue)");
        final Queue q = currentQueue;

        final PhoneNumber p = PhoneNumber.fromBina(q.getCallBackNumber());
        if (smsSender != null && p.isMobile() ) {
            logger.info("Send SMS valid condition");
            Runnable r = new Runnable() {
                public void run() {
                    try {
                        logger.info("Sending...");
                        smsSender.Send(getSetup().getSMSMessage(), p.getNumberToText());
                        logger.info("Sent");
                    } catch(Exception e) {
                        logger.error(e);
                    }
                }
            };
            new Thread(r, "SMSSender to " + p.getNumberToText() ).start();
        }
        logger.info("queueItemRemoved(Queue currentQueue)");
    }
    public String addWebQueue(long customerID, String numberTyped, QueueManager queueManager) throws Exception {
        synchronized (syncQueue) {
            PhoneNumber number = PhoneNumber.fromBina(numberTyped);
            Queue alreadyInQueue = getQueueByPhoneNumberOrCustomerUnsync(customerID, number.getNumberToDial());

            if (alreadyInQueue != null) {
                alreadyInQueue = queueManager.QueueCallingAgain(alreadyInQueue, 0);
                return "Usuário ou telefone já consta na fila.";
            }
            else {
                Priority priority = getPriorityPolicyByNumber(number.getNumberToDial());
                short PriorityValue = priority == null ? 0 : priority.getPriorityValue();
                WebQueue newQueueItem = queueManager.QueueFirstTime(number, Queue.estimateTimeToAttend(queue, agentCollection, currentCalls), queue.size(), PriorityValue, customerID);
                this.queue.add(newQueueItem);
                return "Success";
            }
        }
    }
    public String setMailBox(long callID) throws Exception {
        synchronized(syncQueue) {
            synchronized(syncCall) {
                CallService callService = new CallService();
                QueueService queueService = new QueueService();
                Call call = callService.getByID(callID);

                if (call.getEndTime() == null) { throw new Exception("Chamada em andamento, aguarde finalização."); }
                if (call.getAnswerTime() == null) { throw new Exception("Chamada não atendida, não é possível definir como caixa postal."); }
                if (call.getCallStatus() != CallStatus.Complete) { throw new Exception("Chamada precisa estar com o status de Sucesso para ser alterada para caixa postal.."); }

                Queue q = call.getQueue();
                q.setAttendCall(null);
                int tries = q.getAttendCount();

                call.setCallStatus(CallStatus.VoiceMachine);

                if (tries >= getSetup().getMaxCallBacks()) {
                    // Quote reached
                    q.setQueueStatus(QueueStatus.AutoRemoved);
                    queue.remove(q);
                    queueItemRemoved(q);
                }
                else if (tries % getSetup().getEndOfQueue() == 0) {
                    // End of queue time
                    q.setQueueStatus(QueueStatus.InQueue);
                    // Back to queue, but on the end
                    java.util.Calendar nowPlus20seconds = java.util.Calendar.getInstance();
                    nowPlus20seconds.add(java.util.Calendar.SECOND, 20);
                    java.util.Calendar nowPlusTimeBetweenCalls = java.util.Calendar.getInstance();
                    nowPlusTimeBetweenCalls.add(java.util.Calendar.SECOND, getSetup().getTimeBetweenCallBacks());
                    q.setDontCallBefore(nowPlus20seconds.getTime().after(nowPlusTimeBetweenCalls.getTime()) ? nowPlus20seconds.getTime() : nowPlusTimeBetweenCalls.getTime());
                    q.setScheduleTime(nowPlus20seconds.getTime());
                    this.queue.add(q);
                } else {
                    // Just another failed try
                    q.setQueueStatus(QueueStatus.InQueue);
                    // Back to queue, but wait some seconds
                    java.util.Calendar nowPlusTimeBetweenCalls = java.util.Calendar.getInstance();
                    nowPlusTimeBetweenCalls.setTime(call.getEndTime());
                    nowPlusTimeBetweenCalls.add(java.util.Calendar.SECOND, getSetup().getTimeBetweenCallBacks());
                    q.setDontCallBefore(nowPlusTimeBetweenCalls.getTime());
                    this.queue.add(q);
                }
                call.setQueue(q);
                callService.setCall(call);
                queueService.setQueue(q);

                return "Success";
            }
        }
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Call">
    private void updateCallList(Call call) {
        logger.info(String.format("updateCallList(%s)", call));
        if (call.getEndTime() != null) {
            logger.info("Remove Call Item");
            currentCalls.remove(call);
        } else {
            logger.info("Update Call Item");
            currentCalls.set(currentCalls.indexOf(call), call);
        }
        logger.info("updateCallList(Call) !");
    }
    public void LockToCallBack(DialerManager dialerManager) {
        synchronized (syncQueue) {
            synchronized (syncAgent) {
                synchronized (syncCall) {
                    List<Agent> freeAgent = getFreeAgentsUnsync();
                    List<Queue> pendentQueue = getPendentQueueUnsync();
                    List<Call> programmingCalls = dialerManager.Pair(freeAgent, pendentQueue);
                    programmingCalls = dialerManager.Dial(programmingCalls);

                    for(int i = 0; i < programmingCalls.size(); i++) {
                        Call dialingCall = programmingCalls.get(i);
                        // Get agent reference
                        Agent currentAgent = getAgentByIDUnsync(dialingCall.getAgent().getUserID());

                        // Update agent properties (and call property to agent)
                        addAgentCallUnsync(currentAgent, dialingCall.getCallManagerCallID());
                        //currentAgent.setCurrentCall(dialingCall);
                        dialingCall.setAgent(currentAgent);

                        // Get queue reference
                        Queue currentQueue = getQueueByIDUnsync(dialingCall.getQueue().getQueueID());

                        // Update queue properties (and call property to queue)
                        currentQueue.setQueueStatus(QueueStatus.CallStatus);
                        dialingCall.setQueue(currentQueue);
                        
                        try {
                            // Persist all to database
                            dialingCall.setCallStatus(CallStatus.Dialing);
                            dialingCall = dialerManager.SaveCall(dialingCall, currentAgent, currentQueue, false);
                        } catch (Exception ex) {
                            logger.error(ex);
                        }
                        currentCalls.add(dialingCall);

                        programmingCalls.set(i, dialingCall);
                    }
                }
            }
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Telephony events">
    public Call StartedCall(Agent from, String to, long callManagerCallID, DialerManager dialerManager) {
        logger.info("StartedCall");
        Call call = null;
        synchronized (syncQueue) {
            synchronized (syncAgent) {
                synchronized (syncCall) {
                    call = getCallByCCMIDUnsync(callManagerCallID);
                    if (call == null) {
                        Agent currentAgent = getAgentByIDUnsync(from.getUserID());
                        logger.info ("Private call: " + currentAgent.getName() + " to " + to);
                        addAgentCallUnsync(currentAgent, callManagerCallID);
                        return null;
                    }

                    try {
                        // Get agent reference
                        Agent currentAgent = getAgentByIDUnsync(from.getUserID());

                        // Update agent properties (and call property to agent)
                        addAgentCallUnsync(currentAgent, call.getCallManagerCallID());
                        //currentAgent.setCurrentCall(call);
                        currentAgent.setLastCallTime(new java.util.Date());
                        call.setAgent(currentAgent);

                        // Get queue reference
                        Queue currentQueue = getQueueByIDUnsync(call.getQueue().getQueueID());

                        // Update queue properties (and call property to queue)
                        currentQueue.setQueueStatus(QueueStatus.CallStatus);
                        currentQueue.setAttendCount(currentQueue.getAttendCount() + 1);
                        call.setQueue(currentQueue);

                        try {
                            // Persist all to database
                            call = dialerManager.SaveCall(call, currentAgent, currentQueue, true);
                            updateAgentCollection(call.getAgent());
                        } catch (Exception ex) {
                            logger.error(ex);
                        }
                        updateCallList(call);
                    } catch (Exception ex) {
                        logger.error(ex);
                    }
                }
            }
        }
        return call;
    }

    public Call RingingCall(Agent from, String to, long callManagerCallID, DialerManager dialerManager) {
        logger.info("RingingCall");
        Call call = null;
        synchronized (syncQueue) {
            synchronized (syncAgent) {
                synchronized (syncCall) {
                    call = getCallByCCMIDUnsync(callManagerCallID);
                    if (call == null) {
                        return null;
                    }
                    call.setCallStatus(CallStatus.Ringing);
                }
            }
        }
        return call;
    }

    public Call AnsweredCall(Agent from, String to, long callManagerCallID, DialerManager dialerManager) {
        logger.info("AnsweredCall");
        Call call = null;
        synchronized (syncQueue) {
            synchronized (syncAgent) {
                synchronized (syncCall) {
                    call = getCallByCCMIDUnsync(callManagerCallID);
                    if (call == null) {
                        return null;
                    }

                    try {
//                        // Get agent reference
//                        Agent currentAgent = getAgentByIDUnsync(from.getUserID());
//
//                        // Update agent properties (and call property to agent)
//                        currentAgent.getCallManagerCallIDs().add(call.getCallManagerCallID());
//                        currentAgent.setCurrentCall(call);
//                        currentAgent.setLastCallTime(new java.util.Date());
//                        call.setAgent(currentAgent);

                        // Get queue reference
                        Queue currentQueue = getQueueByIDUnsync(call.getQueue().getQueueID());

                        // Update queue properties (and call property to queue)
                        currentQueue.setQueueStatus(QueueStatus.Complete);
                        currentQueue.setAttendCall(call);
                        call.setQueue(currentQueue);

                        try {
                            // Persist all to database
                            call.setAnswerTime(new java.util.Date());
                            call.setCallStatus(CallStatus.Talking);
                            call = dialerManager.SaveCall(call, call.getAgent(), currentQueue, false);
                        } catch (Exception ex) {
                            logger.error(ex);
                        }
                        updateCallList(call);
                    } catch (Exception ex) {
                        logger.error(ex);
                    }
                }
            }
        }
        return call;
    }

    public Call FailedCall(Agent from, String to, long callManagerCallID, DialerManager dialerManager) {
        logger.info("FailedCall");
        Call call = null;
        synchronized (syncQueue) {
            synchronized (syncAgent) {
                synchronized (syncCall) {
                    call = getCallByCCMIDUnsync(callManagerCallID);
                    if (call == null) {
                        Agent currentAgent = getAgentByIDUnsync(from.getUserID());
                        currentAgent.getCallManagerCallIDs().remove(callManagerCallID);
                        return null;
                    }

                    call = NotAnswered(from, call, dialerManager, true);
                }
            }
        }
        return call;
    }

    public Call EndedCall(Agent agent, long callManagerCallID, DialerManager dialerManager) {
        logger.info("EndedCall");
        Call call = null;
        synchronized (syncQueue) {
            synchronized (syncAgent) {
                synchronized (syncCall) {
                    call = getCallByCCMIDUnsync(callManagerCallID);
                    if (call == null) {
                        Agent currentAgent = getAgentByIDUnsync(agent.getUserID());
                        currentAgent.getCallManagerCallIDs().remove(callManagerCallID);
                        return null;
                    }

                    if (call.getAnswerTime() == null) {
                        if (call.getCallStatus() == CallStatus.Error ||
                            call.getCallStatus() == CallStatus.Busy) {
                            //call.getCallStatus() == CallStatus.NoAnswer) {
                            return call;
                        }
                        return NotAnswered(agent, call, dialerManager, false);
                    }

                    try {
                        // Get agent reference
                        Agent currentAgent = getAgentByIDUnsync(agent.getUserID());
                        //currentAgent.setCurrentCall(null);

                        // Get queue reference
                        Queue currentQueue = getQueueByIDUnsync(call.getQueue().getQueueID());
                        call.setQueue(currentQueue);

                        try {
                            // Persist all to database
                            call.setEndTime(new java.util.Date());
                            call.setCallStatus(CallStatus.Complete);
                            call = dialerManager.SaveCall(call, currentAgent, currentQueue, false);
                        } catch (Exception ex) {
                            logger.error(ex);
                        }
                        // Remove from volatile controllers (call from agent, and call from currentCalls list)
                        currentAgent.getCallManagerCallIDs().remove(callManagerCallID);
                        updateCallList(call);
                        updateQueueList(currentQueue);
                        //updateAgentList(currentAgent);
                    } catch (Exception ex) {
                        logger.error(ex);
                    }
                }
            }
        }
        return call;
    }

    private Call NotAnswered(Agent agent, Call call, DialerManager dialerManager, boolean busy) {
        logger.info("NotAnswered");
        try {
            // Get agent reference
            Agent currentAgent = getAgentByIDUnsync(agent.getUserID());
            if (currentAgent == null) {
                logger.debug(String.format("Null agent item: %d", agent.getUserID()));
            }
            //currentAgent.setCurrentCall(null);

            // Get queue reference
            Queue currentQueue = getQueueByIDUnsync(call.getQueue().getQueueID()); // queue.set(queue.indexOf(call.getQueue()), call.getQueue());

            if (currentQueue == null) {
                logger.debug(String.format("Null queue item: %d", call.getQueue().getQueueID()));
            }

            // Update queue properties (and call property to queue)
            currentQueue.setAttendCall(null); // Always set to null, indicating fail on call back
            if (currentQueue.getAttendCount() >= getSetup().getMaxCallBacks()) {
                // Quote reached
                currentQueue.setQueueStatus(QueueStatus.AutoRemoved);
                queue.remove(currentQueue);
                queueItemRemoved(currentQueue);
            }
            else if (currentQueue.getAttendCount() % getSetup().getEndOfQueue() == 0) {
                // End of queue time
                currentQueue.setQueueStatus(QueueStatus.InQueue);
                // Back to queue, but on the end
                java.util.Calendar nowPlus20seconds = java.util.Calendar.getInstance();
                nowPlus20seconds.add(java.util.Calendar.SECOND, 20);
                java.util.Calendar nowPlusTimeBetweenCalls = java.util.Calendar.getInstance();
                nowPlusTimeBetweenCalls.add(java.util.Calendar.SECOND, getSetup().getTimeBetweenCallBacks());
                currentQueue.setDontCallBefore(nowPlus20seconds.getTime().after(nowPlusTimeBetweenCalls.getTime()) ? nowPlus20seconds.getTime() : nowPlusTimeBetweenCalls.getTime());
                currentQueue.setScheduleTime(nowPlus20seconds.getTime());
            }
            else {
                // Just another failed try
                currentQueue.setQueueStatus(QueueStatus.InQueue);
                // Back to queue, but wait some seconds
                java.util.Calendar nowPlusTimeBetweenCalls = java.util.Calendar.getInstance();
                nowPlusTimeBetweenCalls.add(java.util.Calendar.SECOND, getSetup().getTimeBetweenCallBacks());
                currentQueue.setDontCallBefore(nowPlusTimeBetweenCalls.getTime());
            }
            call.setQueue(currentQueue);

            try {
                // Persist all to database
                if (call.getCallStatus() == CallStatus.Talking) {
                    call.setCallStatus(CallStatus.Error); // Error after talking (technical issues)
                }
                else if (busy) {
                    call.setCallStatus(CallStatus.Busy); // Busy call
                }
                else {
                    call.setCallStatus(CallStatus.NoAnswer); // No answer
                }
                if (call.getEndTime() == null) { call.setEndTime(new Date()); }
                call = dialerManager.SaveCall(call, currentAgent, currentQueue, false);
            } catch (Exception ex) {
                logger.error(ex);
            }
            updateCallList(call);
            updateQueueList(currentQueue);
        } catch (Exception ex) {
            logger.error(ex);
        }
        return call;
    }
    // </editor-fold>
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Setup">
    public boolean changeSetup(Setup setup) {
        try { this.sSetup.setSetup(setup); return true; }
        catch(Exception e) { return false; }
    }
    public void reloadSetup() throws Exception {
        synchronized(syncQueue) {
            synchronized(syncAgent) {
                synchronized(syncCall) {
                    this.sSetup.reloadSetup();
                    loadSMSSender();
                }
            }
        }
    }
    public void loadSMSSender() {
        if (getSetup().getSMSUrl() != null && (!getSetup().getSMSUrl().trim().isEmpty()) ) {
            java.net.Proxy p = java.net.Proxy.NO_PROXY;
            if (getSetup().getProxyIP() != null && getSetup().getProxyPort() != null) {
                p = new java.net.Proxy(Type.HTTP, new InetSocketAddress(
                        getSetup().getProxyIP(),
                        getSetup().getProxyPort().intValue()));
            }
            SMSSender s = new SMSSender(getSetup().getSMSUrl(), getSetup().getSMSAccount(), getSetup().getSMSCode(), getSetup().getSMSFrom(), p);
            setSmsSender(s);
        } else { setSmsSender(null); }
    }
    public void setApplicationDiagnostics(ApplicationDiagnostics applicationDiagnostics) {
        this.applicationDiagnostics = applicationDiagnostics;
    }
    public void setPriorityPolicy(List<Priority> priorityPolicy) {
        synchronized(syncPriority) {
            this.priorityPolicy = priorityPolicy;
        }
    }
    public void setBlackList(List<BlackList> blackListPolicy) {
        synchronized(syncBlackList) {
            this.blackListPolicy = blackListPolicy;
        }
    }
    public void setHolidays(List<Holiday> holidays) {
        synchronized(syncHolidays) {
            Holidays = new HolidayList(holidays);
        }
    }
    // </editor-fold>
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="SMS">
    public SMSSender getSmsSender() {
        return smsSender;
    }
    public void setSmsSender(SMSSender smsSender) {
        this.smsSender = smsSender;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Serialization helper">
    public static String hibernateToJson(Object domainObject, List<String> includes, List<String> excludes) throws IOException {
        if (excludes == null) {
            excludes = new ArrayList<String>();
        }
        excludes.add("*.password");
        return DeveloperCity.Serialization.JSON.Serialize(cleanHibernateObject(domainObject), includes, excludes);
    }
    public static String hibernateToJson(Object domainObject) throws IOException {
        List<String> excludes = new ArrayList<String>();
        excludes.add("*.password");
        return DeveloperCity.Serialization.JSON.Serialize(cleanHibernateObject(domainObject), null, excludes);
    }
    private static Object cleanHibernateObject(Object domainObject) {
        HibernateUtil persistenceUtil = new HibernateUtil(DeveloperCity.DataAccess.HibernateSession.getSessionFactory());
        PersistentBeanManager persistentBeanManager = new PersistentBeanManager();
        persistentBeanManager.setProxyStore(new NoProxyStore());
        persistentBeanManager.setPersistenceUtil(persistenceUtil);
        Object o = persistentBeanManager.clone(domainObject);
        return o;
    }
    // </editor-fold>
}
