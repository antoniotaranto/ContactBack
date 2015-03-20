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
public class Module implements Serializable {

    private static final long serialVersionUID = 1L;
    private long ModuleID;
    private String Description;
    private String Servlet;
    private java.util.Date CreatedOn = new java.util.Date();
    private java.util.Date ModifiedOn;
    private Portal Portal;

    public Module() {
    }

    public Module(long ModuleID, String Description, String Servlet, Portal Portal) {
        this.ModuleID = ModuleID;
        this.Description = Description;
        this.Servlet = Servlet;
        this.Portal = Portal;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String Description) {
        this.Description = Description;
    }

    public long getModuleID() {
        return ModuleID;
    }

    public void setModuleID(long ModuleID) {
        this.ModuleID = ModuleID;
    }

    public String getServlet() {
        return Servlet;
    }

    public void setServlet(String Servlet) {
        this.Servlet = Servlet;
    }

    public java.util.Date getCreatedOn() {
        return CreatedOn;
    }

    public java.util.Date getModifiedOn() {
        return ModifiedOn;
    }

    public Portal getPortal() {
        return Portal;
    }

    public void setPortal(Portal Portal) {
        this.Portal = Portal;
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
        final Module other = (Module) obj;
        if (this.ModuleID != other.getModuleID()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + (int) (this.ModuleID ^ (this.ModuleID >>> 32));
        return hash;
    }
}
