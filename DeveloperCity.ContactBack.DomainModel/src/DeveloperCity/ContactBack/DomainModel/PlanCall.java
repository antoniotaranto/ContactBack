/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.DomainModel;

import java.io.Serializable;

public class PlanCall implements Serializable {
    private static final long serialVersionUID = 1L;
    public PlanCall() { }
    public PlanCall(String AgentTerminal, String AgentExtension, String CallerID) {
        this.AgentTerminal = AgentTerminal;
        this.AgentExtension = AgentExtension;
        this.CallerID = CallerID;
    }

    private String AgentTerminal;
    public String getAgentTerminal() { return AgentTerminal; }
    public void setAgentTerminal(String value) { AgentTerminal = value; }

    private String AgentExtension;
    public String getAgentExtension() { return AgentExtension; }
    public void setAgentExtension(String value) { AgentExtension = value; }

    private String CallerID;
    public String getCallerID() { return CallerID; }
    public void setCallerID(String value) { CallerID = value; }
}
