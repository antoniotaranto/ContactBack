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
public enum QueueStatus implements Serializable {
    None((short) 0),
    URA((short)1),
    QueueProcessing((short) 2),
    InQueue((short) 3),
    Planning((short) 4),
    CallStatus((short) 5),
    ManuallyRemoved((short) 6),
    AutoRemoved((short) 7),
    Complete((short) 8);
    private short status;

    private static final long serialVersionUID = 1L;
    
    QueueStatus(short status) {
        this.status = status;
    }

    public short getStatus() {
        return this.status;
    }

    public static QueueStatus getFrom(Short statusNumber) {
        if (statusNumber == null) {
            return null;
        }

        for (QueueStatus queueStatus : QueueStatus.values()) {
            if (statusNumber.equals(queueStatus.getStatus())) {
                return queueStatus;
            }
        }

        return null;
    }
}
