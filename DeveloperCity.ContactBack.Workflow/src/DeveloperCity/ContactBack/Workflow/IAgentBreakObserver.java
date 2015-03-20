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
public interface IAgentBreakObserver {
    void OutOfServicePhone(Agent agent);
    void InServicePhone(Agent agent);
}
