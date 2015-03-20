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
public class UserChart  implements Serializable {
    private static final long serialVersionUID = 1L;
    private long UserChartID;
    private User User;
    private Chart Chart;
    private boolean GrantAccess = true;
    private java.util.Date CreatedOn = new java.util.Date();
    private java.util.Date ModifiedOn;

    public UserChart() {
    }

    public UserChart(long UserChartID, User User, Chart Chart) {
        this.UserChartID = UserChartID;
        this.User = User;
        this.Chart = Chart;
    }

    public UserChart(long UserChartID, User User, Chart Chart, boolean GrantAccess) {
        this.UserChartID = UserChartID;
        this.User = User;
        this.Chart = Chart;
        this.GrantAccess = GrantAccess;
    }

    public Chart getChart() {
        return Chart;
    }

    public void setChart(Chart Chart) {
        this.Chart = Chart;
    }

    public User getUser() {
        return User;
    }

    public void setUser(User User) {
        this.User = User;
    }

    public long getUserChartID() {
        return UserChartID;
    }

    public void setUserChartID(long UserChartID) {
        this.UserChartID = UserChartID;
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
        return String.format("%s: %s - ID: %d", Chart, User, UserChartID);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserChart other = (UserChart) obj;
        if (this.UserChartID != other.getUserChartID()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + (int) (this.UserChartID ^ (this.UserChartID >>> 32));
        return hash;
    }
}
