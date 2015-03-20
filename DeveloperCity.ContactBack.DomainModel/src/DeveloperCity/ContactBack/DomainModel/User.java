/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DomainModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author lbarbosa
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    protected long UserID;
    private String Username;
    private String Name;
    private String Email;
    private Date Birthday;
    private String Password;
    private UserStatus UserStatus;
    private List<UserPermission> Permissions;
    private List<UserReport> Reports;
    private List<UserChart> Charts;
    private java.util.Date CreatedOn = new java.util.Date();
    private java.util.Date ModifiedOn;
    private List<Portal> PortalAccess;

    public User() {
    }

    public User(long UserID, String Username, String Name, String Email, Date Birthday, String Password, UserStatus UserStatus, List<UserPermission> Permissions, List<UserReport> Reports, List<UserChart> Charts) {
        this.UserID = UserID;
        this.Username = Username;
        this.Name = Name;
        this.Email = Email;
        this.Birthday = Birthday;
        this.Password = Password;
        this.UserStatus = UserStatus;
        this.Permissions = Permissions;
        this.Reports = Reports;
        this.Charts = Charts;
    }

    public Date getBirthday() {
        return Birthday;
    }

    public void setBirthday(Date Birthday) {
        this.Birthday = Birthday;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String Password) {
        this.Password = Password;
    }

    public List<UserPermission> getPermissions() {
        return Permissions;
    }

    public void setPermissions(List<UserPermission> Permissions) {
        this.Permissions = Permissions;
    }

    public List<UserReport> getReports() {
        return Reports;
    }

    public void setReports(List<UserReport> Reports) {
        this.Reports = Reports;
    }

    public List<UserChart> getCharts() {
        return Charts;
    }

    public void setCharts(List<UserChart> Charts) {
        this.Charts = Charts;
    }

    public long getUserID() {
        return UserID;
    }

    public void setUserID(long UserID) {
        this.UserID = UserID;
    }

    public UserStatus getUserStatus() {
        return UserStatus;
    }

    public void setUserStatus(UserStatus UserStatus) {
        this.UserStatus = UserStatus;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public java.util.Date getCreatedOn() {
        return CreatedOn;
    }

    public java.util.Date getModifiedOn() {
        return ModifiedOn;
    }

    @Override
    public String toString() {
        return this.Name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (this.UserID != other.UserID) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + (int) (this.UserID ^ (this.UserID >>> 32));
        return hash;
    }

    private List<Portal> getPortalAccess() {
        if (PortalAccess == null) {
            PortalAccess = new ArrayList<Portal>();
            for(UserPermission userPermission : getPermissions()) {
                if (userPermission.getModule().getPortal() == Portal.BackOffice && (!PortalAccess.contains(Portal.BackOffice))) {
                    PortalAccess.add(Portal.BackOffice);
                }
                else if (userPermission.getModule().getPortal() == Portal.CockPit && (!PortalAccess.contains(Portal.CockPit))) {
                    PortalAccess.add(Portal.CockPit);
                }
                else if (userPermission.getModule().getPortal() == Portal.Agent && (!PortalAccess.contains(Portal.Agent))) {
                    PortalAccess.add(Portal.Agent);
                }
            }
        }
        return PortalAccess;
    }

    public boolean hasBackOfficeAccess() {
        return getPortalAccess().contains(Portal.BackOffice);
    }
    public boolean hasCockPitAccess() {
        return getPortalAccess().contains(Portal.CockPit);
    }
    public boolean hasAgentCockPitAccess() {
        return getPortalAccess().contains(Portal.Agent);
    }
}
