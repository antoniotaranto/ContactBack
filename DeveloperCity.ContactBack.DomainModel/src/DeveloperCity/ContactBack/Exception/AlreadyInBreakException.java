/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Exception;

import DeveloperCity.ContactBack.DomainModel.Agent;
import DeveloperCity.ContactBack.DomainModel.BreakType;
import java.util.Date;

/**
 *
 * @author lbarbosa
 */
public class AlreadyInBreakException extends Exception {
    private static final long serialVersionUID = 1L;
    private Agent agent;
    private BreakType breakType;
    private Date date;
    public AlreadyInBreakException(Agent agent, BreakType breakType) {
        this.agent = agent;
        this.breakType = breakType;
        date = new Date();
    }

    public Agent getAgent() {
        return agent;
    }

    public BreakType getBreakType() {
        return breakType;
    }

    public Date getDate() {
        return date;
    }



    @Override
    public String toString() {
        return String.format("Agent %s is already in break of type %s at %s", agent.getUsername(), breakType.getDescription(), date);
    }
}
