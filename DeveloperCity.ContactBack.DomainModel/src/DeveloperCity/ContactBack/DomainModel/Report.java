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
public class Report implements Serializable {
    private static final long serialVersionUID = 1L;
    private long ReportID;
    private String ReportFile;
    private String ReportDescription;
    private java.util.Date CreatedOn = new java.util.Date();
    private java.util.Date ModifiedOn;

    public Report() {
    }

    public Report(long ReportID, String ReportFile, String ReportDescription) {
        this.ReportID = ReportID;
        this.ReportFile = ReportFile;
        this.ReportDescription = ReportDescription;
    }

    public Date getCreatedOn() {
        return CreatedOn;
    }

    public Date getModifiedOn() {
        return ModifiedOn;
    }

    public String getReportDescription() {
        return ReportDescription;
    }

    public void setReportDescription(String ReportDescription) {
        this.ReportDescription = ReportDescription;
    }

    public String getReportFile() {
        return ReportFile;
    }

    public void setReportFile(String ReportFile) {
        this.ReportFile = ReportFile;
    }

    public long getReportID() {
        return ReportID;
    }

    public void setReportID(long ReportID) {
        this.ReportID = ReportID;
    }

    @Override
    public String toString() {
        return this.ReportDescription;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Report other = (Report) obj;
        if (this.ReportID != other.getReportID()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (int) (this.ReportID ^ (this.ReportID >>> 32));
        return hash;
    }
}