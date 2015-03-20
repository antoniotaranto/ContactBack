package DeveloperCity.Telephony.CallManagerWatcher;

import java.util.*;
import javax.telephony.*;
import javax.telephony.events.*;
import javax.telephony.media.MediaTerminalConnection;

import com.cisco.cti.util.Condition;
import com.cisco.jtapi.extensions.*;
import org.apache.log4j.Logger;

public class CCM implements ProviderObserver, TerminalObserver {

    private static Logger logger = Logger.getLogger(CCM.class);
    Vector<Watcher> actors = new Vector<Watcher>();
    Condition conditionInService = new Condition();
    Provider provider;
    String connString;
    ICallEventResponder callResponder;

    public CCM(String connectionString) {
        this(connectionString, null);
    }

    public CCM(String connectionString, ICallEventResponder callResponder) {
        this.connString = connectionString;
        this.callResponder = callResponder;
    }

    public Provider getProvider() {
        return provider;
    }

    public void Connect() throws
            com.cisco.jtapi.JtapiPeerUnavailableExceptionImpl,
            ResourceUnavailableException,
            MethodNotSupportedException {
        JtapiPeer peer = null;
        try {
            if (logger.isInfoEnabled()) {
                logger.info("Initializing Jtapi");
            }
            peer = JtapiPeerFactory.getJtapiPeer(null);
        } catch (Exception ex) {
            String erro = "Cannot bind CallManager Server. Check the connection string and try again.\r\n" + ex.getMessage();
            logger.error(erro, ex);
            throw new com.cisco.jtapi.JtapiPeerUnavailableExceptionImpl(erro);
        }

        if (logger.isInfoEnabled()) {
            logger.info("Opening provider");
        }
        provider = peer.getProvider(connString);
        provider.addObserver(this);

        conditionInService.waitTrue();
    }

    public void AddAddressWatcher(Address a) throws ResourceUnavailableException, MethodNotSupportedException {
        if (logger.isInfoEnabled()) {
            logger.info("Starting address observer to " + a.getName());
        }
        Watcher watcher = new Watcher(a, callResponder);
        watcher.setCallResponder(callResponder);
        a.addCallObserver(watcher);
        watcher.Initialize();
        actors.add(watcher);
        watcher.Start();
    }

    public void AddAddressWatcher(Address a, ICallEventResponder callEventResponder) throws ResourceUnavailableException, MethodNotSupportedException {
        if (logger.isInfoEnabled()) {
            logger.info("Starting address observer to " + a.getName());
        }
        Watcher watcher = new Watcher(a, callEventResponder);
        watcher.setCallResponder(callEventResponder);
        a.addCallObserver(watcher);
        watcher.Initialize();
        actors.add(watcher);
        watcher.Start();
    }

    public void terminalChangedEvent(TermEv[] blas) {
    }

    public void Disconnect() {
    }

    public synchronized void providerChangedEvent(ProvEv[] eventList) {
        if (eventList != null) {
            for (int i = 0; i < eventList.length; i++) {
                if (eventList[i] instanceof ProvInServiceEv) {
                    conditionInService.set();
                }
            }
        }
    }

    public void Call(String terminalSource, String addressSource, String destinationNumber) {
        Terminal ciscoDevice = null;
        Address ciscoAddress = null;

        try {
            ciscoDevice = provider.getTerminal(terminalSource);
        } catch (Exception ex) {
            ciscoDevice = null;
        }

        if (ciscoDevice == null) {
            return;
        }
        try {
            ciscoAddress = provider.getAddress(addressSource);
        } catch (Exception ex) {
            ciscoAddress = null;
        }

        if (ciscoAddress == null) {
            return;
        }
        Call call = null;
        try {
            call = provider.createCall();
        } catch (Exception ex) {
            call = null;
        }

        if (call == null) {
            return;
        }
        try {
            call.connect(ciscoDevice, ciscoAddress, destinationNumber);
        } catch (Exception ex) {
            return;
        }
    }

    public void RecordCall(TerminalConnection deviceConn) {
        //com.cisco.jtapi.extensions.CiscoMediaTerminal cmt = (com.cisco.jtapi.extensions.CiscoMediaTerminal)device; 
        MediaTerminalConnection mtc = (MediaTerminalConnection) deviceConn;

        java.io.File f = new java.io.File("recTest.au");
        try {
            System.out.println("" + f.toURI().toURL());

            mtc.usePlayURL(f.toURI().toURL());
            mtc.startPlaying();

            //mtc.useRecordURL( f.toURI().toURL() );
            //mtc.startRecording();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        //javax.telephony.media.MediaCallObserver
    }

    public CiscoCall getCall(String callID) {
        try {
            Call[] allCalls = provider.getCalls();
            for (int i = 0; i < allCalls.length; i++) {
                if (new Integer(((CiscoCall) allCalls[i]).getCallID().intValue()).toString().equals(callID)) {
                    return (CiscoCall) allCalls[i];
                }
            }
        } catch (Exception ex) {
        }
        return null;
    }

    public Vector<CiscoCall> getCallList(String extension) {
        try {
            Address address = provider.getAddress(extension);
            Connection[] conns = address.getConnections();
            if (conns.length == 0) {
                return null;
            }

            Vector<CiscoCall> retValue = new Vector<CiscoCall>();
            for (int i = 0; i < conns.length; i++) {
                retValue.add((CiscoCall) conns[i].getCall());
            }
            return retValue;
        } catch (Exception ex) {
        }
        return null;
    }

    public void AnswerCall(String terminal, String numberPhone1, String numberPhone2, String callID) {
        com.cisco.jtapi.extensions.CiscoCall myCall = getCall(callID);

        if (myCall == null) {
            return;
        }
        com.cisco.jtapi.extensions.CiscoAddress calledAddress = (com.cisco.jtapi.extensions.CiscoAddress) myCall.getCalledAddress();
        Terminal[] devs = calledAddress.getInServiceAddrTerminals();
        for (int i = 0; i < devs.length; i++) {
            // All devices in service ringing
            if (devs[i].getName().equals(terminal)) {
                // Found device which must answer
                CiscoTerminal device = (CiscoTerminal) devs[i];
                TerminalConnection[] activeConns = device.getTerminalConnections();
                for (int j = 0; j < activeConns.length; j++) {
                    // All connections of this device
                    if (activeConns[j].getConnection().getCall() == myCall) {
                        // Connection we want to answer
                        try {
                            activeConns[j].answer();
                        } catch (Exception ex) {
                        }
                        return;
                    }
                }
            }
        }
    }

    public void EndCall(String callID) {
        com.cisco.jtapi.extensions.CiscoCall myCall = getCall(callID);
        if (myCall != null) {
            try {
                myCall.drop();
            } catch (Exception ex) {
            }
        }
    }

    public void HoldCall(String holdNumber, String callID) {
        CiscoCall myCall = getCall(callID);
        if (myCall == null) {
            return;
        }

        CiscoAddress holdAddress = null;
        try {
            holdAddress = (CiscoAddress) provider.getAddress(holdNumber);
        } catch (Exception ex) {
            return;
        }

        CiscoTerminalConnection[] holdDevices = getTerminalConnectionsFromCall(holdAddress, myCall);
        for (int i = 0; i < holdDevices.length; i++) {
            HoldCall(holdDevices[i]);
        }
    }

    public void HoldCall(CiscoTerminalConnection holdTerminal) {
        if (holdTerminal.getCallControlState() == javax.telephony.callcontrol.CallControlTerminalConnection.HELD) {
            try {
                holdTerminal.unhold();
            } catch (Exception ex) {
            }
            return;
        }
        else {
            try {
                holdTerminal.hold();
            } catch (Exception ex) {
            }
            return;
        }
    }

    public void TransferCall(String abandonPhoneNumber, String numberPhone2, String callID) {
        CiscoCall myCall = getCall(callID);
        if (myCall == null) {
            return;
        }

        CiscoAddress abandonAddress = null;
        try {
            abandonAddress = (CiscoAddress) provider.getAddress(abandonPhoneNumber);
        } catch (Exception ex) {
            return;
        }

        CiscoTerminalConnection[] abandonDevices = getTerminalConnectionsFromCall(abandonAddress, myCall);
        TransferCall(abandonDevices[0], numberPhone2);
//        for (int i = 0; i < abandonDevices.length; i++)
//        {
//            TransferCall(abandonDevices[i], numberPhone2);
//        }
    }

    public void BlindTransferCall(String CallID, String numberToTransferTo) {
        BlindTransferCall(getCall(CallID), numberToTransferTo);
    }

    public void BlindTransferCall(CiscoCall myCall, String numberToTransferTo) {
        com.cisco.jtapi.extensions.CiscoAddress calledAddress = (com.cisco.jtapi.extensions.CiscoAddress) myCall.getCalledAddress();
        Terminal[] devs = calledAddress.getInServiceAddrTerminals();
        TerminalConnection[] activeConns = devs[0].getTerminalConnections();

        for (int j = 0; j < activeConns.length; j++) {
            // All connections of this device
            if (activeConns[j].getConnection().getCall() == myCall) {
                try {
                    if (activeConns[j].getState() == TerminalConnection.RINGING) {
                        while (activeConns[j].getState() == TerminalConnection.RINGING) {
                            try {
                                activeConns[j].answer();
                            } catch (Exception ex) {
                            }
                        }
                    }
                    if (activeConns[j].getState() != TerminalConnection.ACTIVE) {
                        throw new Exception("Connection not active");
                    }
                    javax.telephony.callcontrol.CallControlCall ccc = (javax.telephony.callcontrol.CallControlCall) myCall;
                    ccc.setTransferController(activeConns[j]);
                    ccc.transfer(numberToTransferTo);
                    return;
                } catch (Exception ex) {
                    logger.error("Error in blind transfer call", ex);
                }
            }
        }
    }

    public void TransferCall(CiscoTerminalConnection connectionMaster, String numberToTransferTo) {
        CiscoCall myCall = (CiscoCall) connectionMaster.getConnection().getCall();
        CiscoCall callCreateNew = null;
        try {
            callCreateNew = (CiscoCall) provider.createCall();
            connectionMaster.hold();
            ((CiscoConnection) connectionMaster.getConnection()).redirect(
                    numberToTransferTo,
                    CiscoConnection.REDIRECT_NORMAL,
                    CiscoConnection.ADDRESS_SEARCH_SPACE);
        } catch (Exception ex) {
            try {
                TerminalConnection terminalNewCreated = connectionMaster;
                if (myCall.getConferenceController() == null) {
                    myCall.setConferenceController(terminalNewCreated);
                    callCreateNew.setTransferEnable(true);
                }
                callCreateNew.consult(terminalNewCreated, numberToTransferTo);
                myCall.transfer(callCreateNew);
            } catch (Exception ex1) {
            }
        }
    }

    private CiscoTerminalConnection[] getTerminalConnectionsFromCall(CiscoAddress address, CiscoCall call) {
        Vector<CiscoTerminalConnection> retValue = new Vector<CiscoTerminalConnection>();

        Terminal[] devs = address.getInServiceAddrTerminals();
        for (int i = 0; i < devs.length; i++) {
            for (int j = 0; j < devs[i].getTerminalConnections().length; j++) {
                if (devs[i].getTerminalConnections()[j].getConnection().getCall() == call) {
                    retValue.add((CiscoTerminalConnection) devs[i].getTerminalConnections()[j]);
                }
            }
        }
        CiscoTerminalConnection[] retArray = new CiscoTerminalConnection[retValue.size()];
        retValue.toArray(retArray);
        return retArray;
    }

    public void SendPush(com.cisco.jtapi.extensions.CiscoTerminal device, String URI1, String URI2, String Priority) {
        try {
            device.addObserver(this);
            String pushxml = "<CiscoIPPhoneExecute><ExecuteItem Priority=\"" + Priority + "\" URL=\"" + URI1 + "\"/><ExecuteItem Priority=\"0\" URL=\"" + URI2 + "\"/></CiscoIPPhoneExecute>";
            device.sendData(pushxml);
        } catch (Exception ex) {
            System.out.println(ex);
        } finally {
            try {
                device.removeObserver(this);
            } catch (Exception ex) {
            }
        }
    }

    public String getConnString() {
        return connString;
    }

    public void setConnString(String connString) {
        this.connString = connString;
    }
}
