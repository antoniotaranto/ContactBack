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
public class QueueItemNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;
    private Queue queue;

    public QueueItemNotFoundException(Queue queue) {
        this.queue = queue;
    }

    @Override
    public String toString() {
        return String.format("Queue not found.");
    }

    public Queue getQueue() {
        return queue;
    }

    public void setQueue(Queue queue) {
        this.queue = queue;
    }

}
