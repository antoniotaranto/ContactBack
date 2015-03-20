package DeveloperCity.Telephony.CallManagerWatcher;

import javax.telephony.events.CallEv;

public interface ICallManagerWatcher {

    public void ReceivedEvent(CallEv[] events);
}


