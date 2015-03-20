/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DomainModel;

import java.io.Serializable;

/**
 *
 * @author lbarbosa
 */
public class UserPermission implements Serializable {

    private static final long serialVersionUID = 1L;
    private long UserPermissionID;
    private User User;
    private Module Module;
    private String ReadWritePermission;
    private java.util.Date CreatedOn = new java.util.Date();
    private java.util.Date ModifiedOn;

    public UserPermission() {
    }

    public UserPermission(long UserPermissionID, User User, Module Module, String ReadWritePermission) {
        this.UserPermissionID = UserPermissionID;
        this.User = User;
        this.Module = Module;
        this.ReadWritePermission = ReadWritePermission;
    }

    public Module getModule() {
        return Module;
    }

    public void setModule(Module Module) {
        this.Module = Module;
    }

    public String getReadWritePermission() {
        return ReadWritePermission;
    }

    public void setReadWritePermission(String ReadWritePermission) {
        this.ReadWritePermission = ReadWritePermission;
    }

    public User getUser() {
        return User;
    }

    public void setUser(User User) {
        this.User = User;
    }

    public long getUserPermissionID() {
        return UserPermissionID;
    }

    public void setUserPermissionID(long UserPermissionID) {
        this.UserPermissionID = UserPermissionID;
    }

    public java.util.Date getCreatedOn() {
        return CreatedOn;
    }

    public java.util.Date getModifiedOn() {
        return ModifiedOn;
    }

    @Override
    public String toString() {
        return String.format("%s: %s - ID: %d", Module, ReadWritePermission, UserPermissionID);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserPermission other = (UserPermission) obj;
        if (this.UserPermissionID != other.getUserPermissionID()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (int) (this.UserPermissionID ^ (this.UserPermissionID >>> 32));
        return hash;
    }
}
