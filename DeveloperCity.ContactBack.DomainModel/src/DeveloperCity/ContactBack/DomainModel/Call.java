/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DomainModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author lbarbosa
 */
public class Call implements Serializable {

    private static final long serialVersionUID = 1L;
    private long CallID;
    private Queue Queue;
    private Date StartTime;
    private Date AnswerTime;
    private Date EndTime;
    private CallStatus CallStatus;
    private long CallManagerCallID;
    private Agent Agent;
    private List<Record> Records;
    private java.util.Date CreatedOn = new java.util.Date();
    private java.util.Date ModifiedOn;

    public Call() {
    }

    public Call(long CallID, Queue Queue, Date StartTime, Date AnswerTime, Date EndTime, CallStatus CallStatus, long CallManagerCallID, Agent Agent, List<Record> Records) {
        this.CallID = CallID;
        this.Queue = Queue;
        this.StartTime = StartTime;
        this.AnswerTime = AnswerTime;
        this.EndTime = EndTime;
        this.CallStatus = CallStatus;
        this.CallManagerCallID = CallManagerCallID;
        this.Agent = Agent;
        this.Records = Records;
    }

    public Agent getAgent() {
        return Agent;
    }

    public void setAgent(Agent Agent) {
        this.Agent = Agent;
    }

    public Date getAnswerTime() {
        return AnswerTime;
    }

    public void setAnswerTime(Date AnswerTime) {
        this.AnswerTime = AnswerTime;
    }

    public long getCallID() {
        return CallID;
    }

    public void setCallID(long CallID) {
        this.CallID = CallID;
    }

    public long getCallManagerCallID() {
        return CallManagerCallID;
    }

    public void setCallManagerCallID(long CallManagerCallID) {
        this.CallManagerCallID = CallManagerCallID;
    }

    public CallStatus getCallStatus() {
        return CallStatus;
    }

    public double getDuration() {
        if (getAnswerTime() == null) {
            return 0;
        }
        Date end = getEndTime() == null ? new Date() : getEndTime();
        double duration = (end.getTime() - getAnswerTime().getTime()) / 1000;
        return duration;
    }

    public String getDurationString() {
        return Utils.TimeDuration(getDuration());
    }

    public double getWaitTime() {
        Date callTime = getQueue().getCallTime();
        Date callbackTime = getStartTime();
        double duration = (callbackTime.getTime() - callTime.getTime()) / 1000;
        return duration;
    }

    public String getWaitTimeString() {
        return Utils.TimeDuration(getWaitTime());
    }

    public void setCallStatus(CallStatus CallStatus) {
        this.CallStatus = CallStatus;
    }

    public Date getEndTime() {
        return EndTime;
    }

    public void setEndTime(Date EndTime) {
        this.EndTime = EndTime;
    }

    public Queue getQueue() {
        return Queue;
    }

    public void setQueue(Queue Queue) {
        this.Queue = Queue;
    }

    public List<Record> getRecords() {
        return Records;
    }

    public void setRecords(List<Record> Records) {
        this.Records = Records;
    }

    public Date getStartTime() {
        return StartTime;
    }

    public void setStartTime(Date StartTime) {
        this.StartTime = StartTime;
    }

    public java.util.Date getCreatedOn() {
        return CreatedOn;
    }

    private void setCreatedOn(java.util.Date CreatedOn) {
        this.CreatedOn = CreatedOn;
    }

    public java.util.Date getModifiedOn() {
        return ModifiedOn;
    }

    private void setModifiedOn(java.util.Date ModifiedOn) {
        this.ModifiedOn = ModifiedOn;
    }

    @Override
    public String toString() {
        return String.format("Status: %s, Start: %s, Answer: %s, End: %s - ID: %d", this.CallStatus, this.StartTime, this.AnswerTime, this.EndTime, this.CallID);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Call other = (Call) obj;
        if (this.CallID != other.getCallID()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (int) (this.CallID ^ (this.CallID >>> 32));
        return hash;
    }
}
