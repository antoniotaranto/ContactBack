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
public class CallManager implements Serializable {

    private static final long serialVersionUID = 1L;
    private long CallManagerID;
    private String IP;
    private String JtapiUsername;
    private String JtapiPassword;
    private java.util.Date CreatedOn = new java.util.Date();
    private java.util.Date ModifiedOn;

    public CallManager() {
    }

    public CallManager(long CallManagerID, String IP, String JtapiUsername, String JtapiPassword) {
        this.CallManagerID = CallManagerID;
        this.IP = IP;
        this.JtapiUsername = JtapiUsername;
        this.JtapiPassword = JtapiPassword;
    }

    public long getCallManagerID() {
        return CallManagerID;
    }

    public void setCallManagerID(long CallManagerID) {
        this.CallManagerID = CallManagerID;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getJtapiPassword() {
        return JtapiPassword;
    }

    public void setJtapiPassword(String JtapiPassword) {
        this.JtapiPassword = JtapiPassword;
    }

    public String getJtapiUsername() {
        return JtapiUsername;
    }

    public void setJtapiUsername(String JtapiUsername) {
        this.JtapiUsername = JtapiUsername;
    }

    public String getConnectionString() {
        return String.format("%s;login=%s;passwd=%s", getIP(), getJtapiUsername(), getJtapiPassword());
    }

    public java.util.Date getCreatedOn() {
        return CreatedOn;
    }

    public java.util.Date getModifiedOn() {
        return ModifiedOn;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CallManager other = (CallManager) obj;
        if (this.CallManagerID != other.getCallManagerID()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + (int) (this.CallManagerID ^ (this.CallManagerID >>> 32));
        return hash;
    }
}
