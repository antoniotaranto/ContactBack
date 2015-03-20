/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.ContactBack.DomainModel.Agent;

/**
 *
 * @author lbarbosa
 */
public interface IAgentCallObserver {
    void AgentIncomingCallRinging(String from, Agent to, long callManagerCallID);
    void AgentStartingNewCall(Agent from, long callManagerCallID);
    void AgentOutgoingCallStarting(Agent from, String to, long callManagerCallID);
    void AgentOutgoingCallBusy(Agent from, String to, long callManagerCallID);
    void AgentOutgoingCallAnswered(Agent from, String to, long callManagerCallID);
    void AgentOutgoingCallRinging(Agent from, String to, long callManagerCallID);
    void AgentEndedCall(Agent agent, long callManagerCallID);
}
