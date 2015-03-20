/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Exception;

import DeveloperCity.ContactBack.DomainModel.Agent;
import java.util.Date;

/**
 *
 * @author lbarbosa
 */
public class NotInBreakException extends Exception {
    private static final long serialVersionUID = 1L;
    private Agent agent;
    private Date date;
    public NotInBreakException(Agent agent) {
        this.agent = agent;
        date = new Date();
    }

    public Agent getAgent() {
        return agent;
    }

    public Date getDate() {
        return date;
    }
    

    @Override
    public String toString() {
        return String.format("Agent %s is not in this break at %s", agent.getUsername(), date);
    }
}
