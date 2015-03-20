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
public enum MatchMode implements Serializable {
    Exact((short) 0),
    StartsWith((short) 1),
    EndsWith((short) 2),
    Contains((short) 3);
    private short mode;

    private static final long serialVersionUID = 1L;
    
    MatchMode(short mode) {
        this.mode = mode;
    }

    public short getStatus() {
        return this.mode;
    }

    public static MatchMode getFrom(Short statusNumber) {
        if (statusNumber == null) {
            return null;
        }

        for (MatchMode matchMode : MatchMode.values()) {
            if (statusNumber.equals(matchMode.getStatus())) {
                return matchMode;
            }
        }

        return null;
    }
}
