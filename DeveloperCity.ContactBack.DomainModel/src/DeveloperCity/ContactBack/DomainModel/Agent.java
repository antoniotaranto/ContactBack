/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DomainModel;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author lbarbosa
 */
public class Agent extends User implements Serializable, Comparable<Agent> {

    private static final long serialVersionUID = 1L;
    private String DirectoryNumber;
    private String Terminal;
    private AgentStatus AgentStatus;
    //Call CurrentCall;
    private Date LastCallTime;
    private List<WorkTime> WorkTimes;
    private Set<Long> callManagerCallIDs = new HashSet<Long>();
    private long requestingBreak = 0;
    private boolean requestingLogoff = false;

    public Agent() {
    }

    public Agent(String DirectoryNumber, String Terminal, AgentStatus AgentStatus, /*Call CurrentCall,*/ Date LastCallTime, List<WorkTime> WorkTimes) {
        this.DirectoryNumber = DirectoryNumber;
        this.Terminal = Terminal;
        this.AgentStatus = AgentStatus;
        //this.CurrentCall = CurrentCall;
        this.LastCallTime = LastCallTime;
        this.WorkTimes = WorkTimes;
    }

    public AgentStatus getAgentStatus() {
        return AgentStatus;
    }

    public void setAgentStatus(AgentStatus AgentStatus) {
        this.AgentStatus = AgentStatus;
    }

    public String getAgentStatusDescription() {
        return AgentStatus.getDescription();
    }

    /*
    public Call getCurrentCall() {
        return CurrentCall;
    }

    public void setCurrentCall(Call CurrentCall) {
        this.CurrentCall = CurrentCall;
    }
    */

    public String getDirectoryNumber() {
        return DirectoryNumber;
    }

    public void setDirectoryNumber(String DirectoryNumber) {
        this.DirectoryNumber = DirectoryNumber;
    }

    public Date getLastCallTime() {
        return LastCallTime;
    }

    public void setLastCallTime(Date LastCallTime) {
        this.LastCallTime = LastCallTime;
    }

    public String getTerminal() {
        return Terminal;
    }

    public void setTerminal(String Terminal) {
        this.Terminal = Terminal;
    }

    public List<WorkTime> getWorkTimes() {
        return WorkTimes;
    }

    public void setWorkTimes(List<WorkTime> WorkTimes) {
        this.WorkTimes = WorkTimes;
    }

    public Set<Long> getCallManagerCallIDs() {
        return callManagerCallIDs;
    }

    public void setCallManagerCallIDs(Set<Long> callManagerCallIDs) {
        this.callManagerCallIDs = callManagerCallIDs;
    }

    public long getRequestingBreak() {
        return requestingBreak;
    }

    public void setRequestingBreak(long requestingBreak) {
        this.requestingBreak = requestingBreak;
    }

    public boolean getRequestingLogoff() {
        return requestingLogoff;
    }

    public void setRequestingLogoff(boolean requestingLogoff) {
        this.requestingLogoff = requestingLogoff;
    }
    public int compareTo(Agent o) {
        if (this.getLastCallTime() == null) {
            return -1;
        }
        else if (o == null || o.getLastCallTime() == null) {
            return 1;
        }
        else {
            return this.getLastCallTime().compareTo(o.getLastCallTime());
        }
    }
}
