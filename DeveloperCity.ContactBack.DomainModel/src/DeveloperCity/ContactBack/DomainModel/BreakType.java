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
public class BreakType implements Serializable {

    private static final long serialVersionUID = 1L;
    private long BreakTypeID;
    private String Description;
    private java.util.Date CreatedOn = new java.util.Date();
    private java.util.Date ModifiedOn;
    private boolean SystemBreak;

    public BreakType() {
    }

    public BreakType(long BreakTypeID, String Description) {
        this.BreakTypeID = BreakTypeID;
        this.Description = Description;
    }

    public long getBreakTypeID() {
        return BreakTypeID;
    }

    public void setBreakTypeID(long BreakTypeID) {
        this.BreakTypeID = BreakTypeID;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
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

    @Override
    public String toString() {
        return Description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final BreakType other = (BreakType) obj;
        if (this.BreakTypeID != other.getBreakTypeID()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + (int) (this.BreakTypeID ^ (this.BreakTypeID >>> 32));
        return hash;
    }
}
