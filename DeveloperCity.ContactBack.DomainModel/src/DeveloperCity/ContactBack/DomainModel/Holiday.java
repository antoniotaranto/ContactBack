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
public class Holiday implements Serializable {

    private static final long serialVersionUID = 1L;
    private long HolidayID;
    private String HolidayName;
    private Date Day;
    private Date CreatedOn = new java.util.Date();
    private Date ModifiedOn;

    public Holiday() {
    }

    public Holiday(long HolidayID, String HolidayName, Date Day) {
        this.HolidayID = HolidayID;
        this.HolidayName = HolidayName;
        this.Day = Day;
    }

    public long getHolidayID() {
        return HolidayID;
    }

    public void setHolidayID(long HolidayID) {
        this.HolidayID = HolidayID;
    }

    public String getHolidayName() {
        return HolidayName;
    }

    public void setHolidayName(String HolidayName) {
        this.HolidayName = HolidayName;
    }

    public Date getDay() {
        return Day;
    }

    public void setDay(Date Day) {
        this.Day = Day;
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
        final Holiday other = (Holiday) obj;
        if (this.HolidayID != other.getHolidayID()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (this.HolidayID ^ (this.HolidayID >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", Day, HolidayName);
    }
}
