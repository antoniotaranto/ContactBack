/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.Exception;

import DeveloperCity.ContactBack.DomainModel.Queue;

/**
 *
 * @author lbarbosa
 */
public class NumberAlreadyInQueueException extends Exception {
    private static final long serialVersionUID = 1L;

    private Queue queue;

    public NumberAlreadyInQueueException(Queue item) {
        this.queue = item;
    }

    @Override
    public String toString() {
        return String.format("Number '%s' is already in queue.", queue.getCallBackNumber());
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }
}
