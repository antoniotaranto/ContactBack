package DeveloperCity.ContactBack.DomainModel;

import java.io.Serializable;

/**
 *
 * @author lbarbosa
 */
public enum CallBackNumberType implements Serializable {

    FromBina((short) 0),
    FromTyping((short) 1);
    private short callBackNumberType;

    private static final long serialVersionUID = 1L;
    
    CallBackNumberType(short callBackNumberType) {
        this.callBackNumberType = callBackNumberType;
    }

    public short getCallBackNumberType() {
        return this.callBackNumberType;
    }

    public static CallBackNumberType getFrom(Short callBackNumberTypeNumber) {
        if (callBackNumberTypeNumber == null) {
            return null;
        }

        for (CallBackNumberType types : CallBackNumberType.values()) {
            if (callBackNumberTypeNumber.equals(types.getCallBackNumberType())) {
                return types;
            }
        }

        return null;
    }
}
