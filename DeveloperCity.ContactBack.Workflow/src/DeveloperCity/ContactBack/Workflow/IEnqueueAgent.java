/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.ContactBack.DomainModel.IVR.PhoneNumber;

/**
 *
 * @author lbarbosa
 */
public interface IEnqueueAgent {
    boolean EnQueue(long callManagerCallID, PhoneNumber validBina);
    boolean EnQueue(long callManagerCallID, PhoneNumber invalidBina, PhoneNumber typedNumber);
}
