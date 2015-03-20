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
public class WorkTime implements Serializable {

    private static final long serialVersionUID = 1L;
    private long WorkTimeID;
    private Date LoginTime;
    private Date LogoutTime;
    private Agent Agent;
    private List<Break> Breaks;
    private java.util.Date CreatedOn = new java.util.Date();
    private java.util.Date ModifiedOn;

    public WorkTime() {
    }

    public WorkTime(long WorkTimeID, Date LoginTime, Date LogoutTime, Agent Agent, List<Break> Breaks) {
        this.WorkTimeID = WorkTimeID;
        this.LoginTime = LoginTime;
        this.LogoutTime = LogoutTime;
        this.Agent = Agent;
        this.Breaks = Breaks;
    }

    public Agent getAgent() {
        return Agent;
    }

    public void setAgent(Agent Agent) {
        this.Agent = Agent;
    }

    public List<Break> getBreaks() {
        return Breaks;
    }

    public void setBreaks(List<Break> Breaks) {
        this.Breaks = Breaks;
    }

    public Date getLoginTime() {
        return LoginTime;
    }

    public void setLoginTime(Date LoginTime) {
        this.LoginTime = LoginTime;
    }

    public Date getLogoutTime() {
        return LogoutTime;
    }

    public void setLogoutTime(Date LogoutTime) {
        this.LogoutTime = LogoutTime;
    }

    public long getWorkTimeID() {
        return WorkTimeID;
    }

    public void setWorkTimeID(long WorkTimeID) {
        this.WorkTimeID = WorkTimeID;
    }

    public java.util.Date getCreatedOn() {
        return CreatedOn;
    }

    public java.util.Date getModifiedOn() {
        return ModifiedOn;
    }

    public double getSessionDuration() {
        Date end = getLogoutTime() == null ? new Date() : getLogoutTime();
        double duration = (end.getTime() - getLoginTime().getTime()) / 1000;
        return duration;
    }

    public double getBreakDuration() {
        double duration = 0;
        List<Break> breaks = null;
        try {
            breaks = getBreaks();
            for (Break b : breaks) {
                duration += b.getBreakDuration();
            }
        }
        catch(Exception e) {
            return 0;
        }
        return duration;
    }

    public boolean isLoginToday() {
        return Utils.isToday(LoginTime);
    }

    public boolean isLogoutToday() {
        if (LogoutTime == null) {
            return true;
        }
        return Utils.isToday(LogoutTime);
    }

    public String getSessionDurationString() {
        return Utils.TimeDuration(getSessionDuration());
    }

    public String getBreakDurationString() {
        return Utils.TimeDuration(getBreakDuration());
    }

    @Override
    public String toString() {
        return String.format("%s to %s - ID: %d", LoginTime, LogoutTime, WorkTimeID);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final WorkTime other = (WorkTime) obj;
        if (this.WorkTimeID != other.getWorkTimeID()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + (int) (this.WorkTimeID ^ (this.WorkTimeID >>> 32));
        return hash;
    }
}
