/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DomainModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author lbarbosa
 */
public enum Weekdays implements Serializable {

    Sunday((short) 0x01),
    Monday((short) 0x02),
    Tuesday((short) 0x04),
    Wednesday((short) 0x08),
    Thurday((short) 0x10),
    Friday((short) 0x20),
    Saturday((short) 0x40);
    private short status;

    private static final long serialVersionUID = 1L;
    
    Weekdays(short status) {
        this.status = status;
    }

    public short getStatus() {
        return this.status;
    }

    public static List<Weekdays> getFrom(Short statusNumber) {
        if (statusNumber == null) {
            return null;
        }

        List<Weekdays> retValue = new ArrayList<Weekdays>();
        for (Weekdays weekdays : Weekdays.values()) {
            if ((statusNumber & weekdays.getStatus()) == weekdays.getStatus()) {
                retValue.add(weekdays);
            }
        }

        return retValue;
    }
    public static Short getFrom(List<Weekdays> days) {
        if (days == null || days.isEmpty()) {
            return 0;
        }

        short retValue = 0;
        for (Weekdays day : days) {
            retValue |= day.getStatus();
        }
        return retValue;
    }
}
