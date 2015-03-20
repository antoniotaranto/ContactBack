package DeveloperCity.ContactBack;

import DeveloperCity.ContactBack.DomainModel.ConfigIVR;
import DeveloperCity.ContactBack.Workflow.AdministrativeService;
import DeveloperCity.ContactBack.Workflow.AgentManager;
import DeveloperCity.ContactBack.Workflow.AgentService;
import DeveloperCity.ContactBack.Workflow.CallManagerService;
import DeveloperCity.ContactBack.Workflow.CallService;
import DeveloperCity.ContactBack.Workflow.DialerManager;
import DeveloperCity.ContactBack.Exception.CallManagerUndefinedException;
import DeveloperCity.ContactBack.Exception.SetupUndefinedException;
import DeveloperCity.ContactBack.Workflow.QueueManager;
import DeveloperCity.ContactBack.Workflow.QueueService;
import DeveloperCity.ContactBack.Workflow.RealTimeData;
import DeveloperCity.ContactBack.Workflow.SetupService;
import DeveloperCity.ContactBack.Workflow.Telephony.SMSSender;
import com.cisco.jtapi.JtapiPeerUnavailableExceptionImpl;
import com.cisco.jtapi.extensions.CiscoRegistrationException;
import java.io.IOException;
import java.util.Properties;
import javax.telephony.InvalidArgumentException;
import javax.telephony.InvalidStateException;
import javax.telephony.JtapiPeerUnavailableException;
import javax.telephony.MethodNotSupportedException;
import javax.telephony.PlatformException;
import javax.telephony.ResourceUnavailableException;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.PropertyConfigurator;
/**
 *
 * @author lbarbosa
 */
public class Program {
    private static Logger logger = Logger.getLogger(Program.class);
    private static PackageVersion applicationVersion;

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) throws SetupUndefinedException, CallManagerUndefinedException, IOException, JtapiPeerUnavailableExceptionImpl, ResourceUnavailableException, MethodNotSupportedException, InvalidArgumentException, CiscoRegistrationException, PlatformException, InvalidStateException, JtapiPeerUnavailableException {
        // <editor-fold defaultstate="collapsed" desc="Log all information">
        PropertyConfigurator.configure("log4j.properties");
        applicationVersion = new PackageVersion();
        applicationVersion.getApplicationDiagnostics().logAll(getPriorityLevelToDiagnosticsLog());
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Common services">
        Properties p = new Properties();
        p.load(Program.class.getClassLoader().getResourceAsStream("DeveloperCity/ContactBack/audiofiles.properties"));
        ConfigIVR configIVR = new ConfigIVR(p);
        SetupService sSetup = new SetupService(configIVR); // Setup and preferences database service and RAM object manager
        QueueService sQueue = new QueueService();          // Current queue database service
        CallService sCall = new CallService();             // In talk calls database service
        AgentService sAgent = new AgentService();          // Agents database service
        // RAM object manager for Queue, Calls and Agents, to avoid 2 threads accessing the same volatile data
        RealTimeData realTimeData = new RealTimeData(sQueue.getInQueue(), sAgent.getUserActive(), sCall.getTalking(), sSetup);
        realTimeData.setApplicationDiagnostics(applicationVersion.getApplicationDiagnostics());
        // CallManager database service, RAM object manager and JTAPI protocol
        CallManagerService sCallManager = new CallManagerService(realTimeData);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Enqueue workflow manager">
        QueueManager queueManager = new QueueManager(realTimeData, sQueue, sCallManager);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Agent workflow manager">
        AgentManager agentManager = new AgentManager(realTimeData, sAgent);
        sCallManager.addAgentCallObserver(agentManager);
        sCallManager.addAgentBreakObserver(agentManager);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="CallBack (dialer) workflow manager">
        DialerManager dialerManager = new DialerManager(realTimeData, sQueue, sCall, agentManager, sCallManager);
        // </editor-fold>

        // <editor-fold defaultstate="collapsed" desc="Administrative protocol service">
        AdministrativeService sAdministrative = new AdministrativeService(realTimeData, sCallManager, queueManager, dialerManager, agentManager);
        sAdministrative.Start();
        // </editor-fold>
    }

    @SuppressWarnings("deprecation")
    private static Priority getPriorityLevelToDiagnosticsLog() {
        return Priority.DEBUG;
    }
}
