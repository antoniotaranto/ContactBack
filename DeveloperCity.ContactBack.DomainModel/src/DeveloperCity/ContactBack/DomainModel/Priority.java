/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DomainModel;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author lbarbosa
 */
public class Priority implements Serializable {

    private static final long serialVersionUID = 1L;
    private long PriorityID;
    private Date StartTime;
    private Date EndTime;
    private String Number;
    private MatchMode MatchMode;
    private short PriorityValue;
    private List<Weekdays> Weekdays;
    private java.util.Date CreatedOn = new java.util.Date();
    private java.util.Date ModifiedOn;

    public Priority() {
    }

    public Priority(long PriorityID, Date StartTime, Date EndTime, String Number, MatchMode MatchMode, short PriorityValue, List<Weekdays> Weekdays) {
        this.PriorityID = PriorityID;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
        this.Number = Number;
        this.MatchMode = MatchMode;
        this.PriorityValue = PriorityValue;
        this.Weekdays = Weekdays;
    }

    public Date getEndTime() {
        return EndTime;
    }

    public void setEndTime(Date EndTime) {
        this.EndTime = EndTime;
    }

    public MatchMode getMatchMode() {
        return MatchMode;
    }

    public void setMatchMode(MatchMode MatchMode) {
        this.MatchMode = MatchMode;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String Number) {
        this.Number = Number;
    }

    public long getPriorityID() {
        return PriorityID;
    }

    public void setPriorityID(long PriorityID) {
        this.PriorityID = PriorityID;
    }

    public short getPriorityValue() {
        return PriorityValue;
    }

    public void setPriorityValue(short PriorityValue) {
        this.PriorityValue = PriorityValue;
    }

    public Date getStartTime() {
        return StartTime;
    }

    public void setStartTime(Date StartTime) {
        this.StartTime = StartTime;
    }

    public List<Weekdays> getWeekdays() {
        return Weekdays;
    }

    public void setWeekdays(List<Weekdays> Weekdays) {
        this.Weekdays = Weekdays;
    }

    private boolean timePolicy() {
        Calendar now = Calendar.getInstance();
        int weekDay = now.get(Calendar.DAY_OF_WEEK);
        if (
                (weekDay == Calendar.MONDAY && getWeekdays().contains( DeveloperCity.ContactBack.DomainModel.Weekdays.Monday ))
                ||
                (weekDay == Calendar.TUESDAY && getWeekdays().contains( DeveloperCity.ContactBack.DomainModel.Weekdays.Tuesday ))
                ||
                (weekDay == Calendar.WEDNESDAY && getWeekdays().contains( DeveloperCity.ContactBack.DomainModel.Weekdays.Wednesday ))
                ||
                (weekDay == Calendar.THURSDAY && getWeekdays().contains( DeveloperCity.ContactBack.DomainModel.Weekdays.Thurday ))
                ||
                (weekDay == Calendar.FRIDAY && getWeekdays().contains( DeveloperCity.ContactBack.DomainModel.Weekdays.Friday ))
                ||
                (weekDay == Calendar.SATURDAY && getWeekdays().contains( DeveloperCity.ContactBack.DomainModel.Weekdays.Saturday ))
                ||
                (weekDay == Calendar.SUNDAY && getWeekdays().contains( DeveloperCity.ContactBack.DomainModel.Weekdays.Sunday ))
            ) {
            Calendar startTime = Calendar.getInstance();
            Calendar endTime = Calendar.getInstance();
            Calendar currentTime = Calendar.getInstance();

            startTime  .setTime(StartTime);
            endTime    .setTime(EndTime);
            currentTime.setTime(new Date());

            startTime  .set(2000, 0, 1);
            endTime    .set(2000, 0, 1);
            currentTime.set(2000, 0, 1);

            return (currentTime.after(startTime)) && (currentTime.before(endTime));
        }
        return false;
    }
    
    public boolean Match(String number) {
        if (number == null) {
            return false;
        }
        if (!timePolicy()) {
            return false;
        }
        if (this.MatchMode == DeveloperCity.ContactBack.DomainModel.MatchMode.Exact) {
            return number.equals(this.Number);
        } else if (this.MatchMode == DeveloperCity.ContactBack.DomainModel.MatchMode.StartsWith) {
            return number.startsWith(this.Number);
        } else if (this.MatchMode == DeveloperCity.ContactBack.DomainModel.MatchMode.EndsWith) {
            return number.endsWith(this.Number);
        } else if (this.MatchMode == DeveloperCity.ContactBack.DomainModel.MatchMode.Contains) {
            return number.contains(this.Number);
        }
        return false;
    }

    public java.util.Date getCreatedOn() {
        return CreatedOn;
    }

    public java.util.Date getModifiedOn() {
        return ModifiedOn;
    }

    @Override
    public String toString() {
        return String.format("%s %s value %d from %s to %s (%s) - ID: %d", this.MatchMode, this.Number, this.PriorityValue, this.StartTime, this.EndTime, this.Weekdays, this.PriorityID);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Priority other = (Priority) obj;
        if (this.PriorityID != other.getPriorityID()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + (int) (this.PriorityID ^ (this.PriorityID >>> 32));
        return hash;
    }
}
