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
public enum UserStatus implements Serializable {

    Deleted((short) 0),
    Active((short) 1),
    Blocked((short) 2);
    private short status;

    private static final long serialVersionUID = 1L;
    
    UserStatus(short status) {
        this.status = status;
    }

    public short getStatus() {
        return this.status;
    }

    public static UserStatus getFrom(Short statusNumber) {
        if (statusNumber == null) {
            return null;
        }

        for (UserStatus userStatus : UserStatus.values()) {
            if (statusNumber.equals(userStatus.getStatus())) {
                return userStatus;
            }
        }

        return null;
    }
}
