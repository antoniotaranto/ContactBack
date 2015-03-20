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
public enum CallStatus implements Serializable {
    None((short) 0),
    Planning((short)1),
    Dialing((short) 2),
    Busy((short) 3),
    Ringing((short) 4),
    NoAnswer((short) 5),
    Talking((short) 6),
    Complete((short) 7),
    Error((short) 8),
    VoiceMachine((short) 9);
    private short status;

    private static final long serialVersionUID = 1L;
    
    CallStatus(short status) {
        this.status = status;
    }

    public short getStatus() {
        return this.status;
    }

    public static CallStatus getFrom(Short statusNumber) {
        if (statusNumber == null) {
            return null;
        }

        for (CallStatus callStatus : CallStatus.values()) {
            if (statusNumber.equals(callStatus.getStatus())) {
                return callStatus;
            }
        }

        return null;
    }
}
