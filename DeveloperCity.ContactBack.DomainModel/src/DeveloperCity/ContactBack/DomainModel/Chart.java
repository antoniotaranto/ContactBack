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
public class Chart implements Serializable {
    private static final long serialVersionUID = 1L;
    private long ChartID;
    private String ChartFile;
    private String ChartDescription;
    private java.util.Date CreatedOn = new java.util.Date();
    private java.util.Date ModifiedOn;

    public Chart() {
    }

    public Chart(long ChartID, String ChartFile, String ChartDescription) {
        this.ChartID = ChartID;
        this.ChartFile = ChartFile;
        this.ChartDescription = ChartDescription;
    }

    public Date getCreatedOn() {
        return CreatedOn;
    }

    public Date getModifiedOn() {
        return ModifiedOn;
    }

    public String getChartDescription() {
        return ChartDescription;
    }

    public void setChartDescription(String ChartDescription) {
        this.ChartDescription = ChartDescription;
    }

    public String getChartFile() {
        return ChartFile;
    }

    public void setChartFile(String ChartFile) {
        this.ChartFile = ChartFile;
    }

    public long getChartID() {
        return ChartID;
    }

    public void setChartID(long ChartID) {
        this.ChartID = ChartID;
    }

    @Override
    public String toString() {
        return this.ChartDescription;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Chart other = (Chart) obj;
        if (this.ChartID != other.getChartID()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 43 * hash + (int) (this.ChartID ^ (this.ChartID >>> 32));
        return hash;
    }
}