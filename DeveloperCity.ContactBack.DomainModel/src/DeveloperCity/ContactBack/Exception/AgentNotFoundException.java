/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.Exception;

import DeveloperCity.ContactBack.DomainModel.Agent;

/**
 *
 * @author lbarbosa
 */
public class AgentNotFoundException extends Exception {

    private static final long serialVersionUID = 1L;
    private Agent agent;

    public AgentNotFoundException(Agent agent) {
        this.agent = agent;
    }

    @Override
    public String toString() {
        return String.format("Agent not found.");
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }
}
