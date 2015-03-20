/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.ContactBack.DomainModel.Agent;
import org.apache.log4j.Logger;

/**
 *
 * @author lbarbosa
 */
public class AgentManager implements IAgentCallObserver, IAgentBreakObserver {

    private static Logger logger = Logger.getLogger(BreakService.class);
    private RealTimeData realTimeData;
    private AgentService sAgent;

    public AgentManager(RealTimeData realTimeData, AgentService sAgent) {
        this.realTimeData = realTimeData;
        this.sAgent = sAgent;
    }

    public void AgentStartingNewCall(Agent from, long callManagerCallID) {
        realTimeData.addAgentCall(from, callManagerCallID);
    }

    public void AgentIncomingCallRinging(String from, Agent to, long callManagerCallID) {
        realTimeData.addAgentCall(to, callManagerCallID);
    }

    public void AgentOutgoingCallStarting(Agent from, String to, long callManagerCallID) {
        realTimeData.addAgentCall(from, callManagerCallID);
    }

    public void AgentOutgoingCallRinging(Agent from, String to, long callManagerCallID) {
        realTimeData.addAgentCall(from, callManagerCallID);
    }

    public void AgentOutgoingCallBusy(Agent from, String to, long callManagerCallID) {
        //realTimeData.addAgentCall(from, callManagerCallID);
    }

    public void AgentOutgoingCallAnswered(Agent from, String to, long callManagerCallID) {
        //realTimeData.addAgentCall(from, callManagerCallID);
    }

    public void AgentEndedCall(Agent agent, long callManagerCallID) {
        realTimeData.removeAgentCall(agent, callManagerCallID);
    }
    public boolean startBreak(long agentID, long breakTypeID) {
        return realTimeData.StartAgentBreak(agentID, breakTypeID);
    }
    public boolean endBreak(long agentID, long breakID) {
        return realTimeData.EndAgentBreak(agentID, breakID);
    }
    public boolean startSession(long agentID, CallManagerService sCallManager) {
        return realTimeData.StartAgentSession(agentID, sCallManager);
    }
    public boolean endSession(long agentID) {
        return realTimeData.EndAgentSession(agentID);
    }
    public boolean restartSession(long agentID, long breakTypeID, CallManagerService sCallManager) {
        return realTimeData.RestartAgentSession(agentID, breakTypeID, sCallManager);
    }
    public Agent setAgent(Agent agent) throws Exception {
        return sAgent.setAgent(agent);
    }

    // <editor-fold defaultstate="collapsed" desc="IAgentBreakObserver interface implementation">
    public void OutOfServicePhone(Agent agent) {
        realTimeData.StartAgentSystemBreak(agent.getUserID(), realTimeData.getSetup().getSystemBreakDeviceOff());
    }

    public void InServicePhone(Agent agent) {
        realTimeData.EndAgentSystemBreak(agent.getUserID(), realTimeData.getSetup().getSystemBreakDeviceOff());
    }
    // </editor-fold>
}

