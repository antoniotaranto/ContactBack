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
public class MultipleOpenAgentSessionException extends Exception {
    private static final long serialVersionUID = 1L;
    private Agent agent;
    public MultipleOpenAgentSessionException(Agent agent) {
        this.agent = agent;
    }
    @Override
    public String toString(){
        return String.format("Agent %s is in multiple sessions", agent.getUsername());
    }
}
