/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DomainModel;

import java.io.Serializable;

/**
 *
 * @author lbarbosa
 */
public enum AgentStatus implements Serializable {

    NotLogged((short)0),
    Available((short)1),
    //InCall((short)2),
    NotReady((short)3),
    Break((short)4);
    private short status;

    private static final long serialVersionUID = 1L;
    
    AgentStatus(short status) {
        this.status = status;
    }

    public short getStatus() {
        return this.status;
    }

    public String getDescription() {
        switch(this) {
            case NotLogged:
                return "Offline";
            case Available:
                return "Online";
            case NotReady:
                return "Ramal bloqueado";
            case Break:
                return "Pausa";
            default:
                return "Offline";
        }
    }

    public static AgentStatus getFrom(Short statusNumber) {
        if (statusNumber == null) {
            return null;
        }

        for (AgentStatus agentStatus : AgentStatus.values()) {
            if (statusNumber.equals(agentStatus.getStatus())) {
                return agentStatus;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return getDescription();
    }

}
