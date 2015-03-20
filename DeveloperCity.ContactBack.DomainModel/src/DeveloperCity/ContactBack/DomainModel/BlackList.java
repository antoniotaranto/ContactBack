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
public class BlackList implements Serializable {

    private static final long serialVersionUID = 1L;
    private long BlackListID;
    private Date StartTime;
    private Date EndTime;
    private String Number;
    private MatchMode MatchMode;
    private List<Weekdays> Weekdays;
    private DestinationType BlackListDestination;
    private java.util.Date CreatedOn = new java.util.Date();
    private java.util.Date ModifiedOn;

    public BlackList() {
    }

    public BlackList(long BlackListID, Date StartTime, Date EndTime, String Number, MatchMode MatchMode, List<Weekdays> Weekdays) {
        this.BlackListID = BlackListID;
        this.StartTime = StartTime;
        this.EndTime = EndTime;
        this.Number = Number;
        this.MatchMode = MatchMode;
        this.Weekdays = Weekdays;
    }

    public long getBlackListID() {
        return BlackListID;
    }

    public void setBlackListID(long BlackListID) {
        this.BlackListID = BlackListID;
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

    public DestinationType getBlackListDestination() {
        return BlackListDestination;
    }

    public void setBlackListDestination(DestinationType BlackListDestination) {
        this.BlackListDestination = BlackListDestination;
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
        return String.format("%s %s from %s to %s (%s) - ID: %d", this.MatchMode, this.Number, this.StartTime, this.EndTime, this.Weekdays, this.BlackListID);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BlackList other = (BlackList) obj;
        if (this.BlackListID != other.getBlackListID()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (int) (this.BlackListID ^ (this.BlackListID >>> 32));
        return hash;
    }
}
