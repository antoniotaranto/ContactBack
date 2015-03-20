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
public enum DestinationType implements Serializable {

    DropCall((short) 0),
    Enqueue((short) 1),
    OptionalAnotherPhone((short) 2),
    ForceAnotherPhone((short) 3);
    private short destinationType;

    private static final long serialVersionUID = 1L;
    
    DestinationType(short destinationType) {
        this.destinationType = destinationType;
    }

    public short getDestinationType() {
        return this.destinationType;
    }

    public static DestinationType getFrom(Short destinationTypeNumber) {
        if (destinationTypeNumber == null) {
            return null;
        }

        for (DestinationType dt : DestinationType.values()) {
            if (destinationTypeNumber.equals(dt.getDestinationType())) {
                return dt;
            }
        }
        return null;
    }
}
