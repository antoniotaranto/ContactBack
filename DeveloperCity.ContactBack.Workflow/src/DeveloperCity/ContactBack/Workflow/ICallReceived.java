/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Workflow;

/**
 *
 * @author lbarbosa
 */
public interface ICallReceived {
    void IncomingCall(String from, long callManagerCallID);
}
