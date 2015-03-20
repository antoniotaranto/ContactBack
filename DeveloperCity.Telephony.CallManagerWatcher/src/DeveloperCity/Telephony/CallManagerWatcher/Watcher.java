package DeveloperCity.Telephony.CallManagerWatcher;

import javax.telephony.*;
import javax.telephony.events.*;
import javax.telephony.callcontrol.*;
import org.apache.log4j.Logger;

public class Watcher implements CallControlCallObserver {

    private static Logger logger = Logger.getLogger(CCM.class);

    public enum WatcherState {

        OutOfService,
        InService
    }
    private Address observedAddress;
//	private Terminal observedTerminal;
//	private boolean addressInService;
//	private boolean terminalInService;
    protected WatcherState state = WatcherState.OutOfService;
    private ICallEventResponder callResponder;

    public Watcher(Address observed) {
        this(observed, null);
    }

    public Watcher(Address observed, ICallEventResponder callResponder) {
        this.observedAddress = observed;
//		this.observedTerminal = observed.getTerminals ()[0];
        this.callResponder = callResponder;
    }

    public void Initialize() throws ResourceUnavailableException, MethodNotSupportedException {
        if (observedAddress != null) {
            if (logger.isInfoEnabled()) {
                logger.info("Adding Call observer to address " + observedAddress.getName());
            }
            observedAddress.addCallObserver(this);

            //Now add observer on Address and Terminal
			/*
            eventLog (
            "Adding Adddress Observer to address "
            + observedAddress.getName ()
            );
             */

            //observedAddress.addObserver ( this );

            /*
            eventLog (
            "Adding Terminal Observer to Terminal"
            + observedTerminal.getName ()
            );
             */

            //observedTerminal.addObserver ( this );
        }
    }

    public final void Start() {
        OnStart();
    }

    public final void dispose() {
        try {
            OnStop();
            if (observedAddress != null) {

                /*
                eventLog (
                "Removing observer from Address "
                + observedAddress.getName ()
                );
                //observedAddress.removeObserver ( this );
                 */
                if (logger.isInfoEnabled()) {
                    logger.info("Removing call observer from Address " + observedAddress.getName());
                }
                observedAddress.removeCallObserver(this);

            }
            /*
            if ( observedTerminal != null ){
            eventLog (
            "Removing observer from terminal "
            + observedTerminal.getName ()
            );
            //observedTerminal.removeObserver ( this );
            }
             */
        } catch (Exception e) {
            logger.error("Caught exception", e);
        }
    }

    public final void Stop() {
        OnStop();
    }

    public synchronized final void callChangedEvent(CallEv[] events) {
        metaEvent(events);
    }

    /*
    public void addressChangedEvent ( AddrEv [] events ) {
    //metaEvent ( (CallEv[]) events );

    for ( int i=0; i<events.length; i++ ) {
    Address address = events[i].getAddress ();
    eventLog("Evento Ator (" + address.getName() + ": " + events[i].toString());
    switch ( events[i].getID () ) {
    case CiscoAddrInServiceEv.ID:
    eventLog ("Received " + events[i] + "for "+ address.getName ());
    addressInService = true;
    if ( terminalInService ) {
    if ( state != WatcherState.InService ) {
    state = WatcherState.InService ;
    OnStateChanged ();
    }
    }
    break;
    case CiscoAddrOutOfServiceEv.ID:
    eventLog ( "Received " + events[i] + "for "+ address.getName ());
    addressInService = false;
    if ( state != WatcherState.OutOfService ) {
    state = WatcherState.OutOfService; // you only want to notify when you had notified earlier that you are IN_SERVICE
    OnStateChanged ();
    }
    break;
    }
    }
    }
     */
    /*
    public  void terminalChangedEvent ( TermEv [] events ) {
    //metaEvent ( (CallEv[]) events );

    for ( int i=0; i<events.length; i++ ) {
    Terminal terminal = events[i].getTerminal ();
    switch ( events[i].getID () ) {
    case CiscoTermInServiceEv.ID:
    eventLog ("Received " + events[i] + "for " + terminal.getName ());
    terminalInService = true;
    if ( addressInService ) {
    if ( state != WatcherState.InService ) {
    state = WatcherState.InService;
    OnStateChanged ();
    }
    }
    break;
    case CiscoTermOutOfServiceEv.ID:
    eventLog ( "Received " + events[i] + "for " + terminal.getName () );
    terminalInService = false;
    if ( state != WatcherState.OutOfService ) { // you only want to notify when you had notified earlier that you are IN_SERVICE
    state = WatcherState.OutOfService;
    OnStateChanged ();
    }
    break;
    }
    }
    }
     */
    protected void metaEvent(CallEv[] events) {
        if (callResponder != null) {
            callResponder.ReceivedCallEvent(events, observedAddress);
        }
    }

    protected void OnStart() {
        if (callResponder != null) {
            callResponder.OnStart();
        }
    }

    protected void OnStop() {
        if (callResponder != null) {
            callResponder.OnStop();
        }
    }

    protected void OnStateChanged() {
        if (callResponder != null) {
            callResponder.OnStateChanged();
        }
    }

    public ICallEventResponder getCallResponder() {
        return callResponder;
    }

    public void setCallResponder(ICallEventResponder callResponder) {
        this.callResponder = callResponder;
    }
}
