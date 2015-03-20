/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.ContactBack.DomainModel.Agent;
import DeveloperCity.ContactBack.DomainModel.CallManager;
import DeveloperCity.ContactBack.DomainModel.Setup;
import com.cisco.jtapi.JtapiPeerUnavailableExceptionImpl;
import com.cisco.jtapi.extensions.CiscoRegistrationException;
import java.io.IOException;
import javax.telephony.InvalidArgumentException;
import javax.telephony.InvalidStateException;
import javax.telephony.JtapiPeerUnavailableException;
import javax.telephony.MethodNotSupportedException;
import javax.telephony.PlatformException;
import javax.telephony.ResourceUnavailableException;
import org.apache.log4j.Logger;

/**
 *
 * @author lbarbosa
 */
public class AdministrativeService {
    private static Logger logger = Logger.getLogger(AdministrativeService.class);
    private RealTimeData realTimeData;
    private CallManagerService sCallManager;
    private QueueManager queueManager;
    private DialerManager dialerManager;
    private AdministrativeProtocolManager protocol;
    private Thread protocolThread;
    private AgentManager agentManager;
    private PriorityService priorityService;
    private BlackListService blackListService;
    private HolidayService holidayService;
    private volatile boolean executing = false;
    private final int port = 1910;

    public AdministrativeService(RealTimeData realTimeData, CallManagerService callManagerService, QueueManager queueManager, DialerManager dialerManager, AgentManager agentManager) throws IOException {
        this.realTimeData = realTimeData;
        this.sCallManager = callManagerService;
        this.queueManager = queueManager;
        this.dialerManager = dialerManager;
        this.priorityService = new PriorityService();
        this.blackListService = new BlackListService();
        this.holidayService = new HolidayService();
        this.protocol = new AdministrativeProtocolManager(this, port);
        realTimeData.loadSMSSender();
        this.agentManager = agentManager;
        protocolThread = new Thread(protocol);
        protocolThread.setName(String.format("AdminListener on %d", port));
    }

    public boolean applySestupChanges(Setup newSetup) {
        try {
            return this.realTimeData.changeSetup(newSetup);
        } catch(Exception e) {
            return false;
        }
    }

    public boolean modifyCallManager(CallManager newCCM) {
        try {
            return sCallManager.changeCallManager(newCCM);
        } catch(Exception e) {
            return false;
        }
    }

    public boolean refreshRealTimeData() {
        return queueManager.RefreshFromDatabase();
    }
    public boolean refreshQueueData() {
        return queueManager.RefreshFromDatabase();
    }
    public boolean isQueueManagerRunning() {
        return this.queueManager.isExecuting();
    }
    public boolean isDialerManagerRunning() {
        return this.dialerManager.isExecuting();
    }
    public void stopQueueManager() {
        this.queueManager.Stop();
    }
    public void startQueueManager() {
        this.queueManager.Start();
    }
    public void stopDialerManager() {
        this.dialerManager.Stop();
    }
    public void startDialerManager() {
        this.dialerManager.Start();
    }
    private void startProtocolListener() {
        if (!protocolThread.isAlive()) {
            protocolThread.start();
        }
        executing = true;
    }
    private void stopProtocolListener() {
        executing = false;
    }
    public boolean startAgentBreak(long agentID, long breakTypeID) {
        return agentManager.startBreak(agentID, breakTypeID);
    }
    public boolean endAgentBreak(long agentID, long breakID) {
        return agentManager.endBreak(agentID, breakID);
    }
    public boolean startAgentSession(long agentID) {
        return agentManager.startSession(agentID, sCallManager);
    }
    public boolean endAgentSession(long agentID) {
        return agentManager.endSession(agentID);
    }
    public boolean restartAgentSession(long agentID, long breakTypeID) {
        return agentManager.restartSession(agentID, breakTypeID, sCallManager);
    }
    public void Start() throws JtapiPeerUnavailableExceptionImpl, ResourceUnavailableException, MethodNotSupportedException, InvalidArgumentException, CiscoRegistrationException, PlatformException, InvalidStateException, JtapiPeerUnavailableException {
        logger.info("Start()");

        logger.info("Connecting to call manager...");
        sCallManager.Connect();
        logger.info("Connected");

        logger.info("Starting queue manager...");
        reloadPriorityPolicy();
        reloadBlackList();
        reloadHolidays();
        startQueueManager();
        logger.info("Queue manager done!");

        logger.info("Starting dialer manager...");
        startDialerManager();
        logger.info("Dialer manager done!");

        logger.info("Starting protocol listener...");
        startProtocolListener();
        logger.info("Protocol listener done!");

        logger.info("Start() !");
    }
    public boolean isAdministrativeServiceRunning() { return executing; }

    public String getQueueJson() throws IOException {
        return realTimeData.getReadOnlyQueue();
    }
    public String getQueueCountJson() throws IOException {
        return realTimeData.getReadOnlyQueueCount();
    }
    public String getAgentsJson() throws IOException {
        return realTimeData.getReadOnlyAgents();
    }
    public String getAgentJson(long agentID) throws IOException {
        return realTimeData.getReadOnlyAgent(agentID);
    }
    public String changeAgent(Agent a) {
        return realTimeData.changeAgent(a, this.sCallManager, this.agentManager);
    }
    public String addWebQueue(long customerID, String number) throws IOException, Exception {
        return realTimeData.addWebQueue(customerID, number, this.queueManager);
    }
    public String setMailBox(long callID) throws IOException, Exception {
        return realTimeData.setMailBox(callID);
    }
    public String getCurrentCallsJson() throws IOException {
        return realTimeData.getReadOnlyCurrentCalls();
    }
    public String getPriorityPolicyJson() throws IOException {
        return realTimeData.getReadOnlyPriorityPolicy();
    }
    public String getApplicationDiagnosticsJson() throws IOException {
        return realTimeData.getApplicationDiagnostics().toJson();
    }
    public boolean reloadPriorityPolicy() {
        try {
            realTimeData.setPriorityPolicy(priorityService.getAll());
        } catch (Exception e) { logger.error(e); return false; }
        return true;
    }
    public boolean reloadBlackList() {
        try {
            realTimeData.setBlackList(blackListService.getAll());
        } catch (Exception e) { logger.error(e); return false; }
        return true;
    }
    public boolean reloadHolidays() {
        try {
            realTimeData.setHolidays(holidayService.getFutureHolidays());
        } catch (Exception e) { logger.error(e); return false; }
        return true;
    }
    public boolean reloadSetup() {
        try {
            realTimeData.reloadSetup();
        } catch (Exception e) { logger.error(e); return false; }
        return true;
    }
}
