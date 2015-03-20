/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DomainModel;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author lbarbosa
 */
public class QueueHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    private long QueueHistoryID;
    private Date CallTime;
    private Queue Queue;
    private java.util.Date CreatedOn = new java.util.Date();
    private java.util.Date ModifiedOn;

    public QueueHistory() {
    }

    public QueueHistory(long QueueHistoryID, Date CallTime, Queue Queue) {
        this.QueueHistoryID = QueueHistoryID;
        this.CallTime = CallTime;
        this.Queue = Queue;
    }

    public Date getCallTime() {
        return CallTime;
    }

    public void setCallTime(Date CallTime) {
        this.CallTime = CallTime;
    }

    public Queue getQueue() {
        return Queue;
    }

    public void setQueue(Queue Queue) {
        this.Queue = Queue;
    }

    public long getQueueHistoryID() {
        return QueueHistoryID;
    }

    public void setQueueHistoryID(long QueueHistoryID) {
        this.QueueHistoryID = QueueHistoryID;
    }

    public java.util.Date getCreatedOn() {
        return CreatedOn;
    }

    public java.util.Date getModifiedOn() {
        return ModifiedOn;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final QueueHistory other = (QueueHistory) obj;
        if (this.QueueHistoryID != other.getQueueHistoryID()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (int) (this.QueueHistoryID ^ (this.QueueHistoryID >>> 32));
        return hash;
    }
}
