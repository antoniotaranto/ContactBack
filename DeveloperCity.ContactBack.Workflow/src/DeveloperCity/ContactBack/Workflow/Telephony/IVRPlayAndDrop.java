/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Workflow.Telephony;

import DeveloperCity.ContactBack.DomainModel.IVR.EventHandler;
import DeveloperCity.ContactBack.DomainModel.IVR.PhoneNumber;
import DeveloperCity.ContactBack.DomainModel.IVR.PlayAndDropCall;
import DeveloperCity.ContactBack.DomainModel.IVR.PlayAndDropCallCollection;
import DeveloperCity.ContactBack.DomainModel.IVR.PlayAndDropEventHandler;
import DeveloperCity.ContactBack.DomainModel.Utils;
import com.cisco.jtapi.extensions.CiscoCall;
import com.cisco.jtapi.extensions.CiscoCallEv;
import com.cisco.jtapi.extensions.CiscoConnection;
import com.cisco.jtapi.extensions.CiscoMediaOpenLogicalChannelEv;
import com.cisco.jtapi.extensions.CiscoMediaTerminal;
import com.cisco.jtapi.extensions.CiscoRTPHandle;
import com.cisco.jtapi.extensions.CiscoRTPInputStartedEv;
import com.cisco.jtapi.extensions.CiscoRTPOutputProperties;
import com.cisco.jtapi.extensions.CiscoRTPOutputStartedEv;
import com.cisco.jtapi.extensions.CiscoRTPOutputStoppedEv;
import com.cisco.jtapi.extensions.CiscoRTPParams;
import com.cisco.jtapi.extensions.CiscoRegistrationException;
import com.cisco.jtapi.extensions.CiscoRouteTerminal;
import com.cisco.jtapi.extensions.CiscoTerminal;
import com.cisco.jtapi.extensions.CiscoTerminalConnection;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.telephony.Address;
import javax.telephony.InvalidArgumentException;
import javax.telephony.InvalidStateException;
import javax.telephony.MethodNotSupportedException;
import javax.telephony.PlatformException;
import javax.telephony.ResourceUnavailableException;
import javax.telephony.TerminalConnection;
import javax.telephony.TerminalObserver;
import javax.telephony.callcontrol.CallControlCallObserver;
import javax.telephony.events.ConnFailedEv;
import javax.telephony.events.TermConnRingingEv;
import javax.telephony.media.MediaCallObserver;
import javax.telephony.media.events.MediaTermConnDtmfEv;
import org.apache.log4j.Logger;

/**
 *
 * @author lbarbosa
 */
public class IVRPlayAndDrop implements CallControlCallObserver, TerminalObserver, MediaCallObserver, PlayAndDropEventHandler, SendAudioThread.SendAudioEventListener {
    private static Logger logger = Logger.getLogger(IVRPlayAndDrop.class);
    protected PlayAndDropCallCollection ivrCalls;
    protected CallManagerProvider callManagerProvider;
    protected Map<Integer, SendAudioThread> IVRThreads;
    protected synchronized PlayAndDropCallCollection getIVRCalls() { return ivrCalls; }
    protected String deviceName;
    protected String waveFileName;
    protected String waveFolderName;
    protected List<IIVRObserver> ivrCompleteCallObserver = new ArrayList<IIVRObserver>();
    protected int udpLocalPort;

    private CiscoTerminal myJtapiDevice;
    private String firstAddress = "";

    public IVRPlayAndDrop(String deviceName, String waveFolderName, String waveFileName, CallManagerProvider callManagerProvider, int udpLocalPort) {
        this.udpLocalPort = udpLocalPort;
        this.deviceName = deviceName;
        this.waveFileName = waveFileName;
        this.waveFolderName = waveFolderName;
        this.callManagerProvider = callManagerProvider;
        ivrCalls = new PlayAndDropCallCollection();
        ivrCalls.addObserver(this);
        IVRThreads = new HashMap<Integer, SendAudioThread>();
    }

    public void init() throws CiscoRegistrationException, MethodNotSupportedException, PlatformException, InvalidStateException, InvalidArgumentException, ResourceUnavailableException {
        myJtapiDevice = (CiscoTerminal) callManagerProvider.getProvider().getTerminal(deviceName);
        com.cisco.jtapi.extensions.CiscoMediaCapability[] cmCap = new com.cisco.jtapi.extensions.CiscoMediaCapability[1];
        //cmCap[0] = com.cisco.jtapi.extensions.CiscoMediaCapability.G711_64K_30_MILLISECONDS;
        cmCap[0] = new com.cisco.jtapi.extensions.CiscoMediaCapability(com.cisco.jtapi.extensions.CiscoRTPPayload.G711ULAW64K, 160); // G711 64K 20ms
        if (myJtapiDevice instanceof CiscoRouteTerminal) {
            ((CiscoRouteTerminal)myJtapiDevice).register(cmCap, CiscoRouteTerminal.DYNAMIC_MEDIA_REGISTRATION);
        } else if (myJtapiDevice instanceof CiscoMediaTerminal) {
            ((CiscoMediaTerminal)myJtapiDevice).register(cmCap);
        }

        myJtapiDevice.addObserver(this);
        myJtapiDevice.addCallObserver(this);

        for (Address a : myJtapiDevice.getAddresses()) {
            com.cisco.jtapi.extensions.CiscoAddress ciscoAddr = (com.cisco.jtapi.extensions.CiscoAddress) a;

            for (int i = 0; i < 10; i++) {
                if (ciscoAddr.getState() != ciscoAddr.OUT_OF_SERVICE) {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
            ciscoAddr.setAutoAcceptStatus(com.cisco.jtapi.extensions.CiscoAddress.AUTOACCEPT_ON, myJtapiDevice);
            if (firstAddress.isEmpty()) {
                firstAddress = ciscoAddr.getName();
            }
        }
    }

    public String getFirstAddress() {
        return firstAddress;
    }
    
    protected String[] getFilesToPlay(PlayAndDropCall call) {
        return new String[] { waveFileName };
    }

    // <editor-fold defaultstate="collapsed" desc="JTAPI Events">
    public void callChangedEvent(javax.telephony.events.CallEv[] events) {
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

            if (ciscoEv.getID() == TermConnRingingEv.ID && ciscoEv.getMetaCode() == 128 && ciscoEv.isNewMetaEvent()) {
                TermConnRingingEv ringingEvent = (TermConnRingingEv) ciscoEv;
                CiscoTerminalConnection terminalConnection = (CiscoTerminalConnection) ringingEvent.getTerminalConnection();
                try {
                    terminalConnection.answer();
                } catch (Exception e) {
                    logger.error(e);
                }
            } else if (ciscoEv.getID() == TermConnRingingEv.ID) {
                TermConnRingingEv ringingEvent = (TermConnRingingEv) ciscoEv;
                for (TerminalConnection tc : this.myJtapiDevice.getTerminalConnections() ) {
                    CiscoTerminalConnection ctc = (CiscoTerminalConnection)tc;
                    if (ctc.getConnection().getCall() == ciscoEv.getCall()) {
                        try {
                            ctc.answer();
                            return;
                        } catch (Exception e) {
                            logger.error(e);
                        }
                    }
                }
            } else if (ciscoEv instanceof MediaTermConnDtmfEv) {
//                MediaTermConnDtmfEv mediaEv = (MediaTermConnDtmfEv) ciscoEv;
//                char digit = mediaEv.getDtmfDigit();
//                getIVRCalls().pressKey(((CiscoCall) mediaEv.getCall()).getCallID().intValue(), digit);
            } else if (ciscoEv instanceof ConnFailedEv) {
                logger.error(ciscoEv.toString());
            }
        }
    }

    public void terminalChangedEvent(javax.telephony.events.TermEv[] events) {
        for (javax.telephony.events.TermEv ev : events) {
            if (ev instanceof CiscoRTPInputStartedEv) {
//                CiscoRTPInputStartedEv rtpInStarted = (CiscoRTPInputStartedEv) ev;
//                CiscoRTPInputProperties rtpInProp = rtpInStarted.getRTPInputProperties();
//                String address = rtpInProp.getLocalAddress().getHostAddress();
//                int port = rtpInProp.getLocalPort();
            }
            else if (ev instanceof CiscoRTPOutputStartedEv) {
                if (logger.isInfoEnabled()) {
                    logger.info("IVR Play and Drop: CiscoRTPOutputStartedEv");
                }
                CiscoRTPOutputStartedEv rtpOutStarted = (CiscoRTPOutputStartedEv) ev;
                String from = rtpOutStarted.getCallID().getCall().getCallingAddress().getName();
                String formattedCallerID = null;
                if (from == null || from.trim().length() < 2 || from.contains("Unknown")) {
                    formattedCallerID = null;
                } else {
                    formattedCallerID = PhoneNumber.fromBina(from).getNumberToDial();
                }
                
                CiscoRTPOutputProperties rtpOutProp = rtpOutStarted.getRTPOutputProperties();
                int callID = rtpOutStarted.getCallID().intValue();
                InetAddress address = rtpOutProp.getRemoteAddress();
                int port = rtpOutProp.getRemotePort();
                getIVRCalls().addCall(new PlayAndDropCall(callID, address, port, formattedCallerID));
            }
            else if (ev instanceof CiscoMediaOpenLogicalChannelEv) {
                if (logger.isInfoEnabled()) {
                    logger.info("IVR Play and Drop: CiscoMediaOpenLogicalChannelEv");
                }
                CiscoMediaOpenLogicalChannelEv ciscoEv = (CiscoMediaOpenLogicalChannelEv) ev;
                try {
                    CiscoRTPHandle crh = ciscoEv.getCiscoRTPHandle();
                    CiscoRTPParams crp = new CiscoRTPParams(InetAddress.getLocalHost(), udpLocalPort);
                    if (myJtapiDevice instanceof CiscoRouteTerminal) {
                        ((CiscoRouteTerminal)myJtapiDevice).setRTPParams(crh, crp);
                    } else if (myJtapiDevice instanceof CiscoMediaTerminal) {
                        ((CiscoMediaTerminal)myJtapiDevice).setRTPParams(crh, crp);
                    }
                } catch (Exception e) {
                    logger.error(e);
                }
            }
            else if (ev instanceof CiscoRTPOutputStoppedEv) {
                if (logger.isInfoEnabled()) {
                    logger.info("IVR Play and Drop: CiscoRTPOutputStoppedEv");
                }
                CiscoRTPOutputStoppedEv ciscoEv = (CiscoRTPOutputStoppedEv) ev;
                PlayAndDropCall call = getIVRCalls().get(ciscoEv.getCallID().intValue());
                logger.info(String.format("End call: %d (%s:%d)", call.getCallID(), call.getRemoteIP(), call.getRemotePort()));
                getIVRCalls().endCall(call);
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="IVR Events">
    public void init(PlayAndDropCall call) {
        try {
                if (logger.isInfoEnabled()) {
                    logger.info(String.format("init(PlayAndDropCall call = %s)", call ));
                }
                SendAudioThread sendAudioThread = new SendAudioThread(waveFolderName, call.getCallID(), call.getRemoteIP(), call.getRemotePort());
                sendAudioThread.addSendAudioEventListener(this);
                IVRThreads.put(call.getCallID(), sendAudioThread);
                IVRThreads.get(call.getCallID()).setFileQueue(getFilesToPlay( call ));
        } catch(Exception e)  { logger.error(e); }
        if (logger.isInfoEnabled()) {
            logger.info("init(PlayAndDropCall) !");
        }
    }
    public void validDigit(PlayAndDropCall call, char digit) {
    }
    public void invalidDigit(PlayAndDropCall call, char digit) {
    }
    public void waveEnded(PlayAndDropCall call) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("waveEnded(PlayAndDropCall call = %s)", call));
        }
        boolean success = false;
        if (success) {
            ivrCalls.endCall(call);

            try {
                for (TerminalConnection tc : myJtapiDevice.getTerminalConnections()) {
                    if (tc == null) {
                        if (logger.isInfoEnabled()) {
                            logger.info("waveEnded(PlayAndDropCall call) !");
                        }
                        return;
                    }
                    CiscoConnection cc = (CiscoConnection) tc.getConnection();
                    if (cc == null) {
                        if (logger.isInfoEnabled()) {
                            logger.info("waveEnded(PlayAndDropCall call) !");
                        }
                        return;
                    }
                    CiscoCall c = (CiscoCall) cc.getCall();
                    if (c == null) {
                        if (logger.isInfoEnabled()) {
                            logger.info("waveEnded(PlayAndDropCall call) !");
                        }
                        return;
                    }
                    if (c.getCallID().intValue() == call.getCallID()) {
                        c.drop();
                        if (logger.isInfoEnabled()) {
                            logger.info("Call Drop");
                            logger.info("waveEnded(PlayAndDropCall call) !");
                        }
                        return;
                    }
                }
            } catch (Exception e) {
                logger.error(e);
            }
            if (logger.isInfoEnabled()) {
                logger.info("waveEnded(PlayAndDropCall call) !");
            }
        }
    }
    public void keyPadPressed(PlayAndDropCall call, char digit) {
    }
    public void endedCall(PlayAndDropCall call) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("endedCall(PlayAndDropCall call = %s)", call ));
        }
        SendAudioThread sendAudio = IVRThreads.get(call.getCallID());
        sendAudio.setAlive(false);
        sendAudio.dispose();
        getIVRCalls().removeCall(call.getCallID());
        IVRThreads.remove(call.getCallID());
        if (logger.isInfoEnabled()) {
            logger.info("endedCall(PlayAndDropCall) !");
        }
    }
    public void addedObserver(EventHandler<PlayAndDropCall> observer) {
    }
    public void removedObserver(EventHandler<PlayAndDropCall> observer) {
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Audio Events">
    @Override
    public void SendAudioCompleteEvent(SendAudioThread thread, String file) {
    }
    @Override
    public void SendAudioListCompleteEvent(SendAudioThread thread, List<String> file) {
        if (logger.isInfoEnabled()) {
            logger.info(String.format("SendAudioListCompleteEvent(SendAudioThread thread = %s, List<String> file = %s)", thread, file));
        }
        PlayAndDropCall pdc = this.ivrCalls.get((int)thread.getIdentifier());
        if (pdc != null) {
            try {
                ivrCalls.endCall(pdc);
            } catch(Exception e) { logger.error(e); }
            try {
                if (myJtapiDevice == null || myJtapiDevice.getTerminalConnections() == null) {
                    thread.removeSendAudioEventListener(this);
                    if (logger.isInfoEnabled()) {
                        logger.info("SendAudioListCompleteEvent(SendAudioThread thread, List<String> file) !");
                    }
                    return;
                }
                for (TerminalConnection tc : myJtapiDevice.getTerminalConnections()) {
                    if (tc == null) {
                        thread.removeSendAudioEventListener(this);
                        if (logger.isInfoEnabled()) {
                            logger.info("SendAudioListCompleteEvent(SendAudioThread thread, List<String> file) !");
                        }
                        return;
                    }
                    CiscoConnection cc = (CiscoConnection) tc.getConnection();
                    if (cc == null) {
                        thread.removeSendAudioEventListener(this);
                        if (logger.isInfoEnabled()) {
                            logger.info("SendAudioListCompleteEvent(SendAudioThread thread, List<String> file) !");
                        }
                        return;
                    }
                    CiscoCall c = (CiscoCall) cc.getCall();
                    if (c == null) {
                        thread.removeSendAudioEventListener(this);
                        if (logger.isInfoEnabled()) {
                            logger.info("SendAudioListCompleteEvent(SendAudioThread thread, List<String> file) !");
                        }
                        return;
                    }
                    if (c.getCallID().intValue() == (int)thread.getIdentifier()) {
//                        thread.removeSendAudioEventListener(this);
                        c.drop();
                        if (logger.isInfoEnabled()) {
                            logger.info("Call Drop");
                            logger.info("SendAudioListCompleteEvent(SendAudioThread thread, List<String> file) !");
                        }
                        return;
                    }
                }
            } catch (Exception e) {
                logger.error(e);
            }
//            thread.removeSendAudioEventListener(this);
            if (logger.isInfoEnabled()) {
                logger.info("SendAudioListCompleteEvent(SendAudioThread thread, List<String> file) !");
            }
        }
    }
    // </editor-fold>
}