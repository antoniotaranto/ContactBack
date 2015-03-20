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
public class Break implements Serializable {

    private static final long serialVersionUID = 1L;
    private long BreakID;
    private WorkTime WorkTime;
    private BreakType BreakType;
    private Date BreakStart;
    private Date BreakEnd;
    private java.util.Date CreatedOn = new java.util.Date();
    private java.util.Date ModifiedOn;
    private boolean SystemBreak;

    public Break() {
    }

    public Break(long BreakID, WorkTime WorkTime, BreakType BreakType, Date BreakStart, Date BreakEnd) {
        this.BreakID = BreakID;
        this.WorkTime = WorkTime;
        this.BreakType = BreakType;
        this.BreakStart = BreakStart;
        this.BreakEnd = BreakEnd;
    }

    public Date getBreakEnd() {
        return BreakEnd;
    }

    public void setBreakEnd(Date BreakEnd) {
        this.BreakEnd = BreakEnd;
    }

    public long getBreakID() {
        return BreakID;
    }

    public void setBreakID(long BreakID) {
        this.BreakID = BreakID;
    }

    public Date getBreakStart() {
        return BreakStart;
    }

    public void setBreakStart(Date BreakStart) {
        this.BreakStart = BreakStart;
    }

    public BreakType getBreakType() {
        return BreakType;
    }

    public void setBreakType(BreakType BreakType) {
        this.BreakType = BreakType;
    }

    public WorkTime getWorkTime() {
        return WorkTime;
    }

    public void setWorkTime(WorkTime WorkTime) {
        this.WorkTime = WorkTime;
    }

    public java.util.Date getCreatedOn() {
        return CreatedOn;
    }

    public java.util.Date getModifiedOn() {
        return ModifiedOn;
    }

    public boolean isSystemBreak() {
        return SystemBreak;
    }

    public void setSystemBreak(boolean SystemBreak) {
        this.SystemBreak = SystemBreak;
    }

    public double getBreakDuration() {
        Date end = getBreakEnd() == null ? new Date() : getBreakEnd();
        double duration = (end.getTime() - getBreakStart().getTime()) / 1000;
        return duration;
    }
    public String getBreakDurationString() {
        return Utils.TimeDuration(getBreakDuration());
    }

    public boolean isBreakStartToday() {
        return Utils.isToday(BreakStart);
    }

    public boolean isBreakEndToday() {
        if (BreakEnd == null) {
            return true;
        }
        return Utils.isToday(BreakEnd);
    }

    @Override
    public String toString() {
        return String.format("%s from %s to %s - ID: %d", this.BreakType, this.BreakStart, this.BreakEnd, this.BreakID);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Break other = (Break) obj;
        if (this.BreakID != other.getBreakID()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 71 * hash + (int) (this.BreakID ^ (this.BreakID >>> 32));
        return hash;
    }
}
