/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Workflow.Telephony;

import DeveloperCity.ContactBack.DomainModel.CallBackNumberType;
import DeveloperCity.ContactBack.DomainModel.IVR.PhoneNumber;

/**
 *
 * @author lbarbosa
 */
public interface IIVRObserver {
    boolean IVRCallComplete(long callManagerCallID, PhoneNumber originalNumber, PhoneNumber typedNumber, CallBackNumberType callBackNumberType);
}
