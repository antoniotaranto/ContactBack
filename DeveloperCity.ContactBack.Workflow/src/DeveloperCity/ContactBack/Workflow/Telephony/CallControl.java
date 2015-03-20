/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Workflow.Telephony;

import com.cisco.jtapi.extensions.CiscoCall;
import com.cisco.jtapi.extensions.CiscoConnection;
import com.cisco.jtapi.extensions.CiscoTerminal;
import javax.telephony.Address;
import javax.telephony.Connection;
import javax.telephony.InvalidArgumentException;
import javax.telephony.InvalidPartyException;
import javax.telephony.InvalidStateException;
import javax.telephony.MethodNotSupportedException;
import javax.telephony.PrivilegeViolationException;
import javax.telephony.ResourceUnavailableException;
import javax.telephony.callcontrol.CallControlConnection;
import org.apache.log4j.Logger;

/**
 *
 * @author lbarbosa
 */
public class CallControl {
    private static Logger logger = Logger.getLogger(CallControl.class);
    private CallManagerProvider callManagerProvider;
    public CallControl(CallManagerProvider callManagerProvider) {
        this.callManagerProvider = callManagerProvider;
    }
    // <editor-fold defaultstate="collapsed" desc="Do call actions">
    public long Dial(String terminalName, String callingNumber, String calledName) throws InvalidArgumentException, ResourceUnavailableException, InvalidStateException, PrivilegeViolationException, MethodNotSupportedException, InvalidPartyException {
        CiscoTerminal terminal = (CiscoTerminal) callManagerProvider.getProvider().getTerminal(terminalName);
        Address dialingAddress = null;
        for(Address address : terminal.getAddresses()) {
            if (address.getName().equals(callingNumber)) {
                dialingAddress = address;
                break;
            }
        }
        if (dialingAddress == null) {
            throw new InvalidArgumentException(String.format("Address %s doesn't match to terminal %s", callingNumber, terminalName));
        }

        CiscoCall dialing = (CiscoCall)callManagerProvider.getProvider().createCall();
        int ccmCallID = dialing.getCallID().intValue();

        javax.telephony.Connection[] conns = null;
        logger.info(String.format("Connect call to terminal %s (dn: %s) and dial to %s", terminal, dialingAddress, calledName));
        dialing.connect(terminal, dialingAddress, calledName);
        logger.info("Done");

        return ccmCallID;
    }
    public void EndCall(long callManagerCallID) {
        javax.telephony.Call[] calls = null;
        try { calls = callManagerProvider.getProvider().getCalls(); }
        catch(Exception e) { logger.error(e); return; }

        if (calls == null) {
            return;
        }

        for(javax.telephony.Call call : calls) {
            try {
                CiscoCall ciscoCall = (CiscoCall)call;
                if(ciscoCall.getCallID().intValue() == callManagerCallID) {
                    try { ciscoCall.drop(); }
                    catch(Exception dropException) { logger.error(dropException); }
                }
            }
            catch(Exception e) {
                logger.error(e);
            }
        }
    }
    public void Transfer(long callManagerCallID, String toExtension) {
        if (logger.isTraceEnabled()) {
            logger.trace(String.format("Transfer(long callManagerCallID = %d, String toExtension = %s)", callManagerCallID, toExtension));
        }
        CiscoCall call = this.getCall(String.valueOf(callManagerCallID));
        CiscoConnection calledConnection = null;
        for(Connection c : call.getCurrentCalledAddress().getConnections()) {
            if (c.getCall() == call) {
                calledConnection = (CiscoConnection) c;
            }
        }
        try {
            call.setTransferController(calledConnection.getTerminalConnections()[0]);
        } catch(Exception e) { logger.error(e); }

        int tries = 0;
        while (tries < 20) {
            tries++;
            try {
                call.transfer(toExtension);
                if (logger.isTraceEnabled()) {
                    logger.trace("Transfer(long, String)!");
                }
                return;
            } catch (Exception ex) {
                logger.error(ex);
            }
            try {
                Thread.sleep(100 * tries);
            } catch(Exception e) {}
        }
        if (logger.isTraceEnabled()) {
            logger.trace("Transfer(long, String)!");
        }
    }
    public void BlindTransfer(long callManagerCallID, String toExtension) {
        CiscoCall call = this.getCall(String.valueOf(callManagerCallID));
        CiscoConnection calledConnection = null;
        for(Connection c : call.getCurrentCalledAddress().getConnections()) {
            if (c.getCall() == call) {
                calledConnection = (CiscoConnection) c;
            }
        }

        while (calledConnection.getCallControlState() != CallControlConnection.ALERTING) {
            try { Thread.sleep(100); }
            catch(Exception e) { }
        }

        int tries = 0;
        while (tries < 20) {
            tries++;
            try {
                calledConnection.redirect(toExtension);
                break;
            } catch (Exception ex) {
                logger.error(ex);
            }
            try {
                Thread.sleep(100 * tries);
            } catch(Exception e) {}
        }
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Ask informations">
    public CiscoCall getCall(String callID) {
        try {
            javax.telephony.Call[] allCalls = callManagerProvider.getProvider().getCalls();
            for (int i = 0; i < allCalls.length; i++) {
                if (new Integer(((CiscoCall) allCalls[i]).getCallID().intValue()).toString().equals(callID)) {
                    return (CiscoCall) allCalls[i];
                }
            }
        } catch (Exception ex) {
        }
        return null;
    }
    public boolean IsInCall(String terminal, CiscoCall call) {
        CiscoTerminal ciscoTerminal = null;
        try {
            ciscoTerminal = (CiscoTerminal) callManagerProvider.getProvider().getTerminal(terminal);
        } catch (InvalidArgumentException ex) {
            logger.error(ex);
        }
        javax.telephony.TerminalConnection[] connections = ciscoTerminal.getTerminalConnections();
        if (connections == null || connections.length == 0) {
            call = null;
            return false;
        }
        else {
            call = (CiscoCall)connections[0].getConnection().getCall();
            return true;
        }
    }
    public boolean IsInService(String terminal) {
        CiscoTerminal ciscoTerminal = null;
        try {
            ciscoTerminal = (CiscoTerminal) callManagerProvider.getProvider().getTerminal(terminal);
        } catch (InvalidArgumentException ex) {
            logger.error(ex);
        }
        return ciscoTerminal.getRegistrationState() == ciscoTerminal.IN_SERVICE;
    }
    // </editor-fold>
}
