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
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;
    private long RecordID;
    private Call Call;
    private String Filename;
    private java.util.Date CreatedOn = new java.util.Date();
    private java.util.Date ModifiedOn;

    public Record() {
    }

    public Record(long RecordID, Call Call, String Filename) {
        this.RecordID = RecordID;
        this.Call = Call;
        this.Filename = Filename;
    }

    public Call getCall() {
        return Call;
    }

    public void setCall(Call Call) {
        this.Call = Call;
    }

    public String getFilename() {
        return Filename;
    }

    public void setFilename(String Filename) {
        this.Filename = Filename;
    }

    public long getRecordID() {
        return RecordID;
    }

    public void setRecordID(long RecordID) {
        this.RecordID = RecordID;
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
        final Record other = (Record) obj;
        if (this.RecordID != other.getRecordID()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (this.RecordID ^ (this.RecordID >>> 32));
        return hash;
    }
}
