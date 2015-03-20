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
public class UserReport  implements Serializable {
    private static final long serialVersionUID = 1L;
    private long UserReportID;
    private User User;
    private Report Report;
    private boolean GrantAccess = true;
    private java.util.Date CreatedOn = new java.util.Date();
    private java.util.Date ModifiedOn;

    public UserReport() {
    }

    public UserReport(long UserReportID, User User, Report Report) {
        this.UserReportID = UserReportID;
        this.User = User;
        this.Report = Report;
    }

    public UserReport(long UserReportID, User User, Report Report, boolean GrantAccess) {
        this.UserReportID = UserReportID;
        this.User = User;
        this.Report = Report;
        this.GrantAccess = GrantAccess;
    }

    public Report getReport() {
        return Report;
    }

    public void setReport(Report Report) {
        this.Report = Report;
    }

    public User getUser() {
        return User;
    }

    public void setUser(User User) {
        this.User = User;
    }

    public long getUserReportID() {
        return UserReportID;
    }

    public void setUserReportID(long UserReportID) {
        this.UserReportID = UserReportID;
    }

    public Date getCreatedOn() {
        return CreatedOn;
    }

    public Date getModifiedOn() {
        return ModifiedOn;
    }
    
    public boolean getGrantAccess() {
        return GrantAccess;
    }

    public void setGrantAccess(boolean GrantAccess) {
        this.GrantAccess = GrantAccess;
    }

    @Override
    public String toString() {
        return String.format("%s: %s - ID: %d", Report, User, UserReportID);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserReport other = (UserReport) obj;
        if (this.UserReportID != other.getUserReportID()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.UserReportID ^ (this.UserReportID >>> 32));
        return hash;
    }
}
