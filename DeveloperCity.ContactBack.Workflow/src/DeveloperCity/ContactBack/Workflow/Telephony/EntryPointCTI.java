/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.Workflow.Telephony;

import DeveloperCity.ContactBack.Workflow.ICallReceived;
import com.cisco.jtapi.extensions.CiscoCall;
import com.cisco.jtapi.extensions.CiscoCallEv;
import com.cisco.jtapi.extensions.CiscoRegistrationException;
import com.cisco.jtapi.extensions.CiscoRouteTerminal;
import java.util.ArrayList;
import java.util.List;
import javax.telephony.Address;
import javax.telephony.InvalidArgumentException;
import javax.telephony.InvalidStateException;
import javax.telephony.MethodNotSupportedException;
import javax.telephony.PlatformException;
import javax.telephony.ResourceUnavailableException;
import javax.telephony.callcontrol.CallControlCallObserver;
import javax.telephony.events.TermConnRingingEv;
import org.apache.log4j.Logger;

/**
 *
 * @author lbarbosa
 */
public class EntryPointCTI implements CallControlCallObserver {

    private static Logger logger = Logger.getLogger(EntryPointCTI.class);
    private String terminalName;
    private CallManagerProvider callManagerProvider;
    private List<ICallReceived> entryPointObservers = new ArrayList<ICallReceived>();

    public EntryPointCTI(String terminalName, CallManagerProvider callManagerProvider) {
        this.callManagerProvider = callManagerProvider;
        this.terminalName = terminalName;
    }

    public void init() throws InvalidArgumentException, CiscoRegistrationException, ResourceUnavailableException, MethodNotSupportedException, PlatformException, InvalidStateException {
        logger.info("init()");
        Object o = callManagerProvider.getProvider().getTerminal(terminalName);
        CiscoRouteTerminal termRoutePoint = (CiscoRouteTerminal) o;

        com.cisco.jtapi.extensions.CiscoMediaCapability[] cmCap = new com.cisco.jtapi.extensions.CiscoMediaCapability[1];
        //cmCap[0] = com.cisco.jtapi.extensions.CiscoMediaCapability.G711_64K_30_MILLISECONDS;
        cmCap[0] = new com.cisco.jtapi.extensions.CiscoMediaCapability(com.cisco.jtapi.extensions.CiscoRTPPayload.G711ULAW64K, 160); // G711 64K 20ms
        //cmCap[2] = new com.cisco.jtapi.extensions.CiscoMediaCapability(com.cisco.jtapi.extensions.CiscoRTPPayload.G711ULAW64K, 320); // G711 64K 40ms
        //cmCap[3] = new com.cisco.jtapi.extensions.CiscoMediaCapability(com.cisco.jtapi.extensions.CiscoRTPPayload.G711ULAW64K, 480); // G711 64K 60ms
        termRoutePoint.register(cmCap, CiscoRouteTerminal.DYNAMIC_MEDIA_REGISTRATION);

        termRoutePoint.addCallObserver(this);

        Address[] addrs = termRoutePoint.getAddresses();
        if (addrs.length != 1) {
            throw new CiscoRegistrationException("The CTI entry point should have exactly ONE address");
        }

        Address a = addrs[0];

        com.cisco.jtapi.extensions.CiscoAddress ciscoAddr = (com.cisco.jtapi.extensions.CiscoAddress) a;
        logger.info(ciscoAddr);

        for (int i = 0; i < 10; i++) {
            if (ciscoAddr.getState() != ciscoAddr.OUT_OF_SERVICE) {
                break;
            }

            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
        ciscoAddr.setAutoAcceptStatus(com.cisco.jtapi.extensions.CiscoAddress.AUTOACCEPT_ON, termRoutePoint);
        logger.info("init() !");
    }

    public void callChangedEvent(javax.telephony.events.CallEv[] events) {
        // Received Call On Entry Point
        if (entryPointObservers == null || entryPointObservers.isEmpty()) {
            return;
        }

        for (javax.telephony.events.CallEv ev : events) {
            if (ev == null) {
                continue;
            }
            if (!(ev instanceof CiscoCallEv)) {
                continue;
            }
            CiscoCallEv ciscoEv = (CiscoCallEv) ev;
            if (ciscoEv.getCall() == null) {
                continue;
            }

            if (ciscoEv.getID() == TermConnRingingEv.ID && ciscoEv.getMetaCode() == 128) {
                CiscoCall call = ((CiscoCall) ciscoEv.getCall());
                String from = call.getCallingAddress().getName();
                long callID = call.getCallID().intValue();
                for (ICallReceived observer : entryPointObservers) {
                    observer.IncomingCall(from, callID);
                }
            }
        }
    }
    public void addEntryPointObserver(ICallReceived observer) {
        entryPointObservers.add(observer);
    }
    public void removeEntryPointObserver(ICallReceived observer) {
        entryPointObservers.remove(observer);
    }
}
