package DeveloperCity.Telephony.CallManagerWatcher;

import javax.telephony.*;
import javax.telephony.events.*;

public interface ICallEventResponder {

    boolean getIgnoreProviderEvents();

    void ReceivedCallEvent(CallEv[] events, Address address);

    void OnStart();

    void OnStop();

    void OnStateChanged();
}
