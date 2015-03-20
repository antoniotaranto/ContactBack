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
public enum Portal implements Serializable {
    None((short) 0),
    BackOffice((short) 1),
    CockPit((short) 2),
    Agent((short) 3);
    private short portal;

    private static final long serialVersionUID = 1L;
    
    Portal(short portal) {
        this.portal = portal;
    }

    public short getPortal() {
        return this.portal;
    }

    public static Portal getFrom(Short portalNumber) {
        if (portalNumber == null) {
            return null;
        }

        for (Portal portalItem : Portal.values()) {
            if (portalNumber.equals(portalItem.getPortal())) {
                return portalItem;
            }
        }

        return null;
    }
}
