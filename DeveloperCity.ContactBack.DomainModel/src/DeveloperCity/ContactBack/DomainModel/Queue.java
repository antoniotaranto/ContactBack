/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DomainModel;

import DeveloperCity.ContactBack.DomainModel.IVR.PhoneNumber;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author lbarbosa
 */
public class Queue implements Serializable, Comparable<Queue> {

    private static final long serialVersionUID = 2L;
    private static Logger logger = Logger.getLogger(Queue.class);
    private long QueueID;
    //private String CallerNumber;
    //private String NumberToCallBack;
    private String  BinaNumber;
    private boolean ValidBinaNumber;
    private String  CallBackNumber;
    private CallBackNumberType CallBackNumberType;
    private Date CallTime;
    private Date DontCallBefore;
    private Date ScheduleTime;
    private Date EstimatedTimeToAttend;
    private int EntryPosition;
    private Call AttendCall;
    private QueueStatus QueueStatus;
    private long CallManagerCallID;
    private int AttendCount;
    private short PriorityValue;
    private List<Call> Calls;
    private List<QueueHistory> History;
    private java.util.Date CreatedOn = new java.util.Date();
    private java.util.Date ModifiedOn;

    public Queue() {
    }

    public Queue(long QueueID, String BinaNumber, boolean ValidBinaNumber, String CallBackNumber, DeveloperCity.ContactBack.DomainModel.CallBackNumberType CallBackNumberType, Date CallTime, Date DontCallBefore, Date ScheduleTime, Date EstimatedTimeToAttend, int EntryPosition, Call AttendCall, DeveloperCity.ContactBack.DomainModel.QueueStatus QueueStatus, long CallManagerCallID, int AttendCount, short PriorityValue, List<Call> Calls, List<QueueHistory> History, Date ModifiedOn) {
        this.QueueID = QueueID;
        this.BinaNumber = BinaNumber;
        this.ValidBinaNumber = ValidBinaNumber;
        this.CallBackNumber = CallBackNumber;
        this.CallBackNumberType = CallBackNumberType;
        this.CallTime = CallTime;
        this.DontCallBefore = DontCallBefore;
        this.ScheduleTime = ScheduleTime;
        this.EstimatedTimeToAttend = EstimatedTimeToAttend;
        this.EntryPosition = EntryPosition;
        this.AttendCall = AttendCall;
        this.QueueStatus = QueueStatus;
        this.CallManagerCallID = CallManagerCallID;
        this.AttendCount = AttendCount;
        this.PriorityValue = PriorityValue;
        this.Calls = Calls;
        this.History = History;
        this.ModifiedOn = ModifiedOn;
    }
    
    public Call getAttendCall() {
        return AttendCall;
    }

    public void setAttendCall(Call AttendCall) {
        this.AttendCall = AttendCall;
    }

    public int getAttendCount() {
        return AttendCount;
    }

    public void setAttendCount(int AttendCount) {
        this.AttendCount = AttendCount;
    }

    public long getCallManagerCallID() {
        return CallManagerCallID;
    }

    public void setCallManagerCallID(long CallManagerCallID) {
        this.CallManagerCallID = CallManagerCallID;
    }

    public Date getCallTime() {
        return CallTime;
    }

    public void setCallTime(Date CallTime) {
        this.CallTime = CallTime;
    }

    public List<Call> getCalls() {
        return Calls;
    }

    public void setCalls(List<Call> Calls) {
        this.Calls = Calls;
    }

    public Date getDontCallBefore() {
        return DontCallBefore;
    }

    public void setDontCallBefore(Date DontCallBefore) {
        this.DontCallBefore = DontCallBefore;
    }

    public String getBinaNumber() {
        return BinaNumber;
    }

    public void setBinaNumber(String BinaNumber) {
        this.BinaNumber = BinaNumber;
    }

    public String getCallBackNumber() {
        return CallBackNumber;
    }

    public void setCallBackNumber(String CallBackNumber) {
        this.CallBackNumber = CallBackNumber;
    }

    public DeveloperCity.ContactBack.DomainModel.CallBackNumberType getCallBackNumberType() {
        return CallBackNumberType;
    }

    public void setCallBackNumberType(DeveloperCity.ContactBack.DomainModel.CallBackNumberType CallBackNumberType) {
        this.CallBackNumberType = CallBackNumberType;
    }

    public boolean isValidBinaNumber() {
        return ValidBinaNumber;
    }

    public void setValidBinaNumber(boolean ValidBinaNumber) {
        this.ValidBinaNumber = ValidBinaNumber;
    }

    public long getQueueID() {
        return QueueID;
    }

    public void setQueueID(long QueueID) {
        this.QueueID = QueueID;
    }

    public QueueStatus getQueueStatus() {
        return QueueStatus;
    }

    public void setQueueStatus(QueueStatus QueueStatus) {
        this.QueueStatus = QueueStatus;
    }

    public Date getScheduleTime() {
        return ScheduleTime;
    }

    public void setScheduleTime(Date ScheduleTime) {
        this.ScheduleTime = ScheduleTime;
    }

    public Date getScheduleTimeWithPriority() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(ScheduleTime);
        cal.add(Calendar.MINUTE, (-5) * PriorityValue);
        return cal.getTime();
    }

    public Date getEstimatedTimeToAttend() {
        return EstimatedTimeToAttend;
    }

    public void setEstimatedTimeToAttend(Date EstimatedTimeToAttend) {
        this.EstimatedTimeToAttend = EstimatedTimeToAttend;
    }

    public int getEntryPosition() {
        return EntryPosition;
    }

    public void setEntryPosition(int EntryPosition) {
        this.EntryPosition = EntryPosition;
    }

    public short getPriorityValue() {
        return PriorityValue;
    }

    public void setPriorityValue(short PriorityValue) throws Exception {
        if (PriorityValue > 12 || PriorityValue < -12) {
            throw new Exception("The value for priority is out of bounds. Value must be between -12 and 12.");
        }
        this.PriorityValue = PriorityValue;
    }

    public boolean isFrozen() {
        if (this.DontCallBefore == null) {
            return false;
        }
        return this.DontCallBefore.after(new Date());
    }

    public void setFrozen(boolean frozen) {
    }

    public List<QueueHistory> getHistory() {
        return History;
    }

    public void setHistory(List<QueueHistory> History) {
        this.History = History;
    }

    public boolean isFrom(String callBackNumber) {
        return PhoneNumber.sameNumber(callBackNumber, this.CallBackNumber);
    }

    public java.util.Date getCreatedOn() {
        return CreatedOn;
    }

    public java.util.Date getModifiedOn() {
        return ModifiedOn;
    }

    @Override
    public String toString() {
        return String.format("%s (return to %s), since %s, AppID: %d, CCMID: %d", BinaNumber != null ? BinaNumber : "?", CallBackNumber, CallTime, QueueID, CallManagerCallID);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Queue other = (Queue) obj;
        if (this.QueueID != other.getQueueID()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + (int) (this.QueueID ^ (this.QueueID >>> 32));
        return hash;
    }

    public int compareTo(Queue o) {
        if (this.getScheduleTimeWithPriority() == null) {
            return -1;
        }
        else if (o == null || o.getScheduleTimeWithPriority() == null) {
            return 1;
        }
        else {
            return this.getScheduleTimeWithPriority().compareTo(o.getScheduleTimeWithPriority());
        }
    }

    public static Date estimateTimeToAttend(List<Queue> queue, List<Agent> agents, List<Call> calls) {
        Calendar now = Calendar.getInstance();
        Calendar retValue = Calendar.getInstance();

        // first approach
        // take the oldest item in queue and see how much time he's waiting for attend
        Date oldestItem = null;
        int queueCount = 0;
        for (Queue q : queue) {
            if (q.getQueueStatus() != q.getQueueStatus().InQueue) {
                continue;
            }
            queueCount++;
            if (oldestItem == null || q.getDontCallBefore().before(oldestItem)) {
                oldestItem = q.getDontCallBefore();
            }
        }
        long oldestWait = oldestItem != null ? (long) Math.floor(((double)(now.getTime().getTime() - oldestItem.getTime())) / 1000D) : 0;
        if (oldestWait < 0) { oldestWait = 0; }
        // oldestWait represents, in seconds, how much time is the biggest wait.

        // second approach
        // take all current calls and see the average time, then multiply by the number of items in queue
        long waitTimeTotal = 0;
        int validCalls = 0;
        for (Call c : calls) {
            if (c.getAnswerTime() != null && c.getEndTime() == null) {
                logger.info(c.getStartTime().getTime());
                logger.info(c.getQueue().getDontCallBefore().getTime());
                waitTimeTotal += (long) Math.floor(((double)( c.getStartTime().getTime() - c.getQueue().getDontCallBefore().getTime() )) / 1000D);
                validCalls++;
            }
        }
        double waitTimeAverage = validCalls > 0 ? ((double)waitTimeTotal) / ((double)validCalls) : 0;
        if (waitTimeAverage < 0) { waitTimeAverage = 0; }
        double queueTotalWaitExpected = queueCount * waitTimeAverage;

        // Old method - Average between two approaches
//        double estimatedWaitTime = (queueTotalWaitExpected + ((double) oldestWait)) / 2;
        // New method, just first approach
        double estimatedWaitTime = oldestWait;

        // Plus 20 seconds (audio expected length)
        int averageEstimated = ((int)Math.floor(estimatedWaitTime)) + 20;

        retValue.add(Calendar.SECOND, averageEstimated);
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("First approach (wait: %d, date: %s). Second approach (total: %d, average: %.3f, queue count: %d, expected: %.3f). Response (wait: %.3f, time: %s)",
                    oldestWait,
                    oldestItem,
                    waitTimeTotal,
                    waitTimeAverage,
                    queueCount,
                    queueTotalWaitExpected,
                    estimatedWaitTime,
                    retValue.getTime()));
        }
        return retValue.getTime();
    }
}