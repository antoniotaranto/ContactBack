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
public class SimpleStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    private int inQueue;
    private int frozen;
    private int inCall;
    private int others;
    private int total;

    public SimpleStatus() {
    }
    public SimpleStatus(int inQueue, int frozen, int inCall, int others, int total) {
        this.inQueue = inQueue;
        this.frozen = frozen;
        this.inCall = inCall;
        this.others = others;
        this.total = total;
    }

    public int getFrozen() {
        return frozen;
    }

    public void setFrozen(int frozen) {
        this.frozen = frozen;
    }

    public int getInCall() {
        return inCall;
    }

    public void setInCall(int inCall) {
        this.inCall = inCall;
    }

    public int getInQueue() {
        return inQueue;
    }

    public void setInQueue(int inQueue) {
        this.inQueue = inQueue;
    }

    public int getOthers() {
        return others;
    }

    public void setOthers(int others) {
        this.others = others;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
