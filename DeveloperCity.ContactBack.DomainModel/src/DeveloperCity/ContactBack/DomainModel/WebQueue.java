package DeveloperCity.ContactBack.DomainModel;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author lbarbosa
 */
public class WebQueue extends Queue implements Serializable {

    private static final long serialVersionUID = 1L;
    private Customer Customer;

    public WebQueue() {
    }

    public WebQueue(DeveloperCity.ContactBack.DomainModel.Customer Customer, long QueueID, String BinaNumber, boolean ValidBinaNumber, String CallBackNumber, DeveloperCity.ContactBack.DomainModel.CallBackNumberType CallBackNumberType, Date CallTime, Date DontCallBefore, Date ScheduleTime, Date EstimatedTimeToAttend, int EntryPosition, Call AttendCall, DeveloperCity.ContactBack.DomainModel.QueueStatus QueueStatus, long CallManagerCallID, int AttendCount, short PriorityValue, List<Call> Calls, List<QueueHistory> History, Date ModifiedOn) {
        super(QueueID, BinaNumber, ValidBinaNumber, CallBackNumber, CallBackNumberType, CallTime, DontCallBefore, ScheduleTime, EstimatedTimeToAttend, EntryPosition, AttendCall, QueueStatus, CallManagerCallID, AttendCount, PriorityValue, Calls, History, ModifiedOn);
        this.Customer = Customer;
    }

    public Customer getCustomer() {
        return Customer;
    }

    public void setCustomer(Customer Customer) {
        this.Customer = Customer;
    }
}
