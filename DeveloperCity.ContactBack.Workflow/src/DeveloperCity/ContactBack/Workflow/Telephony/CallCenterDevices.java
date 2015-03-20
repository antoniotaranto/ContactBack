/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Workflow.Telephony;

import DeveloperCity.ContactBack.DomainModel.Agent;
import DeveloperCity.ContactBack.Workflow.IAgentBreakObserver;
import DeveloperCity.ContactBack.Workflow.IAgentCallObserver;
import com.cisco.jtapi.extensions.CiscoTerminal;
import java.util.ArrayList;
import java.util.List;
import javax.telephony.InvalidArgumentException;
import javax.telephony.MethodNotSupportedException;
import javax.telephony.ResourceUnavailableException;
import org.apache.log4j.Logger;

/**
 *
 * @author lbarbosa
 */
public class CallCenterDevices extends ArrayList<AgentDevice> implements IAgentCallObserver, IAgentBreakObserver {
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(CallCenterDevices.class);
    private List<IAgentCallObserver> agentCallObservers = new ArrayList<IAgentCallObserver>();
    private List<IAgentBreakObserver> agentBreakObservers = new ArrayList<IAgentBreakObserver>();
    private CallManagerProvider callManagerProvider;

    public CallCenterDevices(CallManagerProvider callManagerProvider) {
        this.callManagerProvider = callManagerProvider;
    }

    public void registerAgentDevices(List<Agent> agents) throws InvalidArgumentException, ResourceUnavailableException, MethodNotSupportedException {
        for (Agent agent : agents) {
            registerAgentDevice(agent);
        }
    }
    public void registerAgentDevice(Agent agent) throws InvalidArgumentException, ResourceUnavailableException, MethodNotSupportedException {
        String terminal = agent.getTerminal();
        logger.info(String.format("Agent %s, terminal %s", agent.getUsername(), terminal ));
        CiscoTerminal t = getTerminal(terminal);
        AgentDevice newDevice = new AgentDevice(t, agent);
        newDevice.addAgentBreakListener(this);
        newDevice.addAgentCallObserver(this);
        t.addCallObserver(newDevice);
        t.addObserver(newDevice);
        this.add(newDevice);
    }
    public CiscoTerminal getTerminal(String terminal) throws InvalidArgumentException {
        return (CiscoTerminal)callManagerProvider.getProvider().getTerminal(terminal);
    }
    public boolean isTerminalExtensionValid(String terminal, String extension) {
        CiscoTerminal t = null;
        try {
            t = getTerminal(terminal);
            if (t == null || t.getAddresses() == null || t.getAddresses().length == 0) { return false; }
            return t.getAddresses()[0].getName().equalsIgnoreCase(extension);
        }
        catch(Exception e) { return false; }
    }
    public boolean unregisterAgentDevice(long agentID) {
        for (AgentDevice ad : this) {
            if (ad.getAgent().getUserID() == agentID) {
                ad.removeAgentBreakListener(this);
                ad.removeAgentCallObserver(this);
                this.remove(ad);
                return true;
            }
        }
        return false;
    }
    public void addAgentCallObserver(IAgentCallObserver observer) {
        agentCallObservers.add(observer);
    }
    public void removeAgentCallObserver(IAgentCallObserver observer) {
        agentCallObservers.remove(observer);
    }
    public void addAgentBreakListener(IAgentBreakObserver observer) {
        agentBreakObservers.add(observer);
    }
    public void removeAgentBreakListener(IAgentBreakObserver observer) {
        agentBreakObservers.remove(observer);
    }

    public void AgentIncomingCallRinging(String from, Agent to, long callManagerCallID) {
        for (IAgentCallObserver observer : agentCallObservers) {
            observer.AgentIncomingCallRinging(from, to, callManagerCallID);
        }
    }

    public void AgentStartingNewCall(Agent from, long callManagerCallID) {
        for (IAgentCallObserver observer : agentCallObservers) {
            observer.AgentStartingNewCall(from, callManagerCallID);
        }
    }

    public void AgentOutgoingCallStarting(Agent from, String to, long callManagerCallID) {
        for (IAgentCallObserver observer : agentCallObservers) {
            observer.AgentOutgoingCallStarting(from, to, callManagerCallID);
        }
    }

    public void AgentOutgoingCallBusy(Agent from, String to, long callManagerCallID) {
        for (IAgentCallObserver observer : agentCallObservers) {
            observer.AgentOutgoingCallBusy(from, to, callManagerCallID);
        }
    }

    public void AgentOutgoingCallAnswered(Agent from, String to, long callManagerCallID) {
        for (IAgentCallObserver observer : agentCallObservers) {
            observer.AgentOutgoingCallAnswered(from, to, callManagerCallID);
        }
    }

    public void AgentOutgoingCallRinging(Agent from, String to, long callManagerCallID) {
        for (IAgentCallObserver observer : agentCallObservers) {
            observer.AgentOutgoingCallRinging(from, to, callManagerCallID);
        }
    }

    public void AgentEndedCall(Agent agent, long callManagerCallID) {
        for (IAgentCallObserver observer : agentCallObservers) {
            observer.AgentEndedCall(agent, callManagerCallID);
        }
    }

    public void OutOfServicePhone(Agent agent) {
        for (IAgentBreakObserver observer : agentBreakObservers) {
            observer.OutOfServicePhone(agent);
        }
    }

    public void InServicePhone(Agent agent) {
        for (IAgentBreakObserver observer : agentBreakObservers) {
            observer.InServicePhone(agent);
        }
    }
}
