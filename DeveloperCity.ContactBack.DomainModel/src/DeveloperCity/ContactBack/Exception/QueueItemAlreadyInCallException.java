/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.Exception;

import DeveloperCity.ContactBack.DomainModel.Call;

/**
 *
 * @author lbarbosa
 */
public class QueueItemAlreadyInCallException extends Exception {

    private static final long serialVersionUID = 1L;
    private Call call;

    public QueueItemAlreadyInCallException(Call call) {
        this.call = call;
    }

    @Override
    public String toString() {
        return String.format("Queue item is already in a call.");
    }

    public Call getCall() {
        return call;
    }

    public void setCall(Call call) {
        this.call = call;
    }
}
