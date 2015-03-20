/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Workflow.Telephony;

import DeveloperCity.ContactBack.DomainModel.*;
import DeveloperCity.ContactBack.DomainModel.IVR.AskPhoneNumberCall;
import DeveloperCity.ContactBack.DomainModel.IVR.AskPhoneNumberCallCollection;
import DeveloperCity.ContactBack.DomainModel.IVR.AskPhoneNumberEventHandler;
import DeveloperCity.ContactBack.DomainModel.IVR.EventHandler;
import DeveloperCity.ContactBack.DomainModel.IVR.PhoneNumber;
import DeveloperCity.ContactBack.Workflow.CallManagerService;
import DeveloperCity.ContactBack.Workflow.IWaitTimeEstimativeCalculator;
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
public class IVRAskPhoneNumber implements CallControlCallObserver, TerminalObserver, MediaCallObserver, AskPhoneNumberEventHandler {
    private static Logger logger = Logger.getLogger(IVRAskPhoneNumber.class);
    private AskPhoneNumberCallCollection ivrCalls;
    private CallManagerProvider callManagerProvider;
    private Map<Integer, SendAudioThread> IVRThreads;
    private synchronized AskPhoneNumberCallCollection getIVRCalls() { return ivrCalls; }
    private ConfigIVR configIVR;
    private List<IIVRObserver> ivrCompleteCallObserver = new ArrayList<IIVRObserver>();
    private CallManagerService sCallManager;
    private CiscoTerminal myJtapiDevice;
    private String firstAddress = "";
    private IWaitTimeEstimativeCalculator waitTimeEstimativeCalculator;
    private int udpLocalPort;
    private Setup setup;

    public IVRAskPhoneNumber(ConfigIVR configIVR, Setup setup, CallManagerProvider callManagerProvider, CallManagerService sCallManager, int udpLocalPort, IWaitTimeEstimativeCalculator waitTimeEstimativeCalculator) {
        this.udpLocalPort = udpLocalPort;
        this.sCallManager = sCallManager;
        this.configIVR = configIVR;
        this.setup = setup;
        this.callManagerProvider = callManagerProvider;
        this.waitTimeEstimativeCalculator = waitTimeEstimativeCalculator;
        ivrCalls = new AskPhoneNumberCallCollection();
        ivrCalls.addObserver(this);
        IVRThreads = new HashMap<Integer, SendAudioThread>();
    }

    public void init() throws CiscoRegistrationException, ResourceUnavailableException, MethodNotSupportedException, PlatformException, InvalidStateException, InvalidArgumentException {
        myJtapiDevice = (CiscoTerminal) callManagerProvider.getProvider().getTerminal(setup.getIVR_DeviceName());
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
                MediaTermConnDtmfEv mediaEv = (MediaTermConnDtmfEv) ciscoEv;
                char digit = mediaEv.getDtmfDigit();
                getIVRCalls().pressKey(((CiscoCall) mediaEv.getCall()).getCallID().intValue(), digit);
            }
            else if (ciscoEv instanceof ConnFailedEv) {
                logger.warn(String.format("ConnFailedEv %s (call %s)", ciscoEv, ciscoEv.getCall()));
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
//                String formattedCallerID = null;
//                if (from == null || from.trim().length() < 2 || from.contains("Unknown")) {
//                    formattedCallerID = null;
//                } else {
//                    formattedCallerID = PhoneNumber.fromBina(from).getNumberToDial();
//                }
                
                CiscoRTPOutputProperties rtpOutProp = rtpOutStarted.getRTPOutputProperties();
                int callID = rtpOutStarted.getCallID().intValue();
                InetAddress address = rtpOutProp.getRemoteAddress();
                int port = rtpOutProp.getRemotePort();
                getIVRCalls().addCall(new AskPhoneNumberCall(callID, address, port, from));
            }
            else if (ev instanceof CiscoMediaOpenLogicalChannelEv) {
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
                CiscoRTPOutputStoppedEv ciscoEv = (CiscoRTPOutputStoppedEv) ev;
                AskPhoneNumberCall call = getIVRCalls().get(ciscoEv.getCallID().intValue());
                if (call != null) {
                    SendAudioThread sat = IVRThreads.get(call.getCallID());
                    if (sat != null && sat.isPlaying()) {
                        sat.askStop();
                    }
                    getIVRCalls().endCall(call);
                }
            }
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="IVR Events">
    private String fileForDigit(char digit) {
        String numberM = configIVR.getNumberM(digit);
        if (numberM != null && numberM.length() > 0) {
            return numberM;
        }
        return "";
    }
    private String fileForNumber(String number, boolean male) {
        String file = male ? configIVR.getNumberM(number) : configIVR.getNumberF(number);
        if (file != null && file.length() > 0) {
            return file;
        }
        return "";
    }
    private String fileForTelephone(String number) {
        if (number.length() == 10) {
            String retValue = fileForNumber(String.format("%s%s", number.charAt(0), number.charAt(1) ), false);
            for (int i = 2; i < 10; i++) {
                retValue += "," + fileForDigit( number.charAt(i) );
            }
            return retValue;
        }

        String retValue = null;
        for (int i = 0; i < number.length(); i++) {
            if (retValue != null) {
                retValue += ",";
            }
            retValue += fileForDigit(number.charAt(i));
        }
        return retValue;
    }
    public void init(AskPhoneNumberCall call) {
        if (logger.isDebugEnabled()) {
            logger.debug( String.format("init(AskPhoneNumberCall call = %s)", call));
        }
        try {
            IVRThreads.put(call.getCallID(), new SendAudioThread(setup.getVoiceFolder(), call.getCallID(), call.getRemoteIP(), call.getRemotePort()));
            DestinationType dt = getDestinationTypeByCall(call);
            String init = null;
            if  (dt == DestinationType.ForceAnotherPhone) {
                init = configIVR.getInitRequired();
            } else if(dt == DestinationType.OptionalAnotherPhone) {
                init = configIVR.getInitOptional();
            }

            if (init != null && init.length() > 0) {
                IVRThreads.get(call.getCallID()).setFileQueue(init.split(","));
            }
        } catch(Exception e)  { logger.error(e); }
        if (logger.isDebugEnabled()) {
            logger.debug("init(AskPhoneNumberCall call)!");
        }
    }
    public void validDigit(AskPhoneNumberCall call, char digit) {
        String validDigit = configIVR.getValidDigit();
        if (validDigit != null && validDigit.length() > 0) {
            IVRThreads.get(call.getCallID()).setFileQueue(validDigit.replace("%d", fileForDigit(digit) ).split(","));
        }
    }
    public void invalidDigit(AskPhoneNumberCall call, char digit) {
        String invalidDigit = configIVR.getInvalidDigit();
        if (invalidDigit != null && invalidDigit.length() > 0) {
            IVRThreads.get(call.getCallID()).setFileQueue(invalidDigit.replace("%d", fileForDigit(digit) ).split(","));
        }
    }
    public void sentenceChanged(AskPhoneNumberCall call, String tempPhoneNumber, boolean isDDDPart) {
        char digit = tempPhoneNumber.charAt(tempPhoneNumber.length() - 1);

        String sentenceChanged = configIVR.getSentenceChanged();
        if (sentenceChanged != null && sentenceChanged.length() > 0) {
            IVRThreads.get(call.getCallID()).setFileQueue(sentenceChanged.replace("%d", fileForDigit(digit) ).split(","));
        }
    }
    public void resetPhoneNumber(AskPhoneNumberCall call) {
        if (logger.isDebugEnabled()) {
            logger.debug( String.format("resetPhoneNumber(AskPhoneNumberCall call = %s)", call));
        }

        String resetPhoneNumber = configIVR.getResetPhoneNumber();
        if (resetPhoneNumber != null && resetPhoneNumber.length() > 0) {
            IVRThreads.get(call.getCallID()).setFileQueue(resetPhoneNumber.split(","));
        }
        if (logger.isDebugEnabled()) {
            logger.debug("resetPhoneNumber(AskPhoneNumberCall call)!");
        }
    }
    public void invalidDDDStart(AskPhoneNumberCall call, char digit) {
        String invalidDDDStart = configIVR.getInvalidDDDStart();
        if (invalidDDDStart != null && invalidDDDStart.length() > 0) {
            IVRThreads.get(call.getCallID()).setFileQueue(invalidDDDStart.replace("%d", fileForDigit(digit) ).split(","));
        }
    }
    public void invalidNumberStart(AskPhoneNumberCall call, char digit) {
        String invalidNumberStart = configIVR.getInvalidNumberStart();
        if (invalidNumberStart != null && invalidNumberStart.length() > 0) {
            IVRThreads.get(call.getCallID()).setFileQueue(invalidNumberStart.replace("%d", fileForDigit(digit) ).split(","));
        }
    }
    public void invalidConfirmationChar(AskPhoneNumberCall call, char digit) {
        String invalidConfirmationChar = configIVR.getInvalidConfirmationChar();
        if (invalidConfirmationChar != null && invalidConfirmationChar.length() > 0) {
            IVRThreads.get(call.getCallID()).setFileQueue(invalidConfirmationChar.replace("%d", fileForDigit(digit) ).split(","));
        }
    }
    public void dddComplete(AskPhoneNumberCall call, String ddd) {
        String dddComplete = configIVR.getDDDComplete();
        char dddLastDigit = ddd.charAt(1);
        if (dddComplete != null && dddComplete.length() > 0) {
            IVRThreads.get(call.getCallID()).setFileQueue(dddComplete.replace("%n", fileForNumber(ddd, false) ).replace("%d", fileForDigit(dddLastDigit)).split(","));
        }
    }
    public void askingConfirmation(AskPhoneNumberCall call, String number, boolean firstTime) {
        String askingConfirmation = configIVR.getAskingConfirmation();
        if (askingConfirmation != null && askingConfirmation.length() > 0) {
            IVRThreads.get(call.getCallID()).setFileQueue(askingConfirmation.replace("%t", fileForTelephone(number) ).split(","));
        }
    }
    public void addIVRCallCompleteObserver(IIVRObserver observer) {
        this.ivrCompleteCallObserver.add(observer);

    }
    public void removeIVRCallCompleteObserver(IIVRObserver observer) {
        this.ivrCompleteCallObserver.remove(observer);

    }
    public void numberComplete(AskPhoneNumberCall call, String number) {
        if (logger.isDebugEnabled()) {
            logger.debug( String.format("numberComplete(AskPhoneNumberCall call = %s, String number = %s)", call, number));
        }

        boolean success = notifyCompleteCallObservers(call);
        if (success) {
            String wholeText = configIVR.getNumberCompleteSuccess();
            if (wholeText.contains("%h") || wholeText.contains("%p")) {
                Queue queueItem = waitTimeEstimativeCalculator.getQueueObject(call.getPhoneNumber());
                if (queueItem == null) {
                    wholeText = wholeText.replace("%h", "").replace("%p", "");
                } else {
                    wholeText = wholeText.contains("%h") ?
                        (wholeText.replace("%h", configIVR.getFullTimeFiles(waitTimeEstimativeCalculator.calculate(queueItem), false))) :
                        (wholeText);
                    wholeText = wholeText.contains("%H") ?
                        (wholeText.replace("%H", configIVR.getFullTimeFiles(waitTimeEstimativeCalculator.calculate(queueItem), true))) :
                        (wholeText);
                    wholeText = wholeText.contains("%p") ?
                        (wholeText.replace("%p", configIVR.getNumberM(String.valueOf(queueItem.getEntryPosition())))) :
                        (wholeText);
                }
            }
            if (wholeText.contains("%t")) {
                wholeText = wholeText.replace("%t", fileForTelephone(number));
            }

            playAndDrop(call, wholeText);
        } else {
            playAndDrop(call, configIVR.getNumberCompleteFail().replace("%t", fileForTelephone(number)));
        }
        if (logger.isDebugEnabled()) {
            logger.debug("numberComplete(AskPhoneNumberCall call, String number)");
        }
    }
    private void playAndDrop(AskPhoneNumberCall call, String file) {
        if (logger.isDebugEnabled()) {
            logger.debug( String.format("playAndDrop(AskPhoneNumberCall call = %s, String file = %s)", call, file));
        }
        final AskPhoneNumberCall c = call;
        SendAudioThread sat = IVRThreads.get(call.getCallID());
        if (file != null && file.length() > 0) {
            try {
                final String[] fileSplit = file.split(",");
                sat.addSendAudioEventListener(new SendAudioThread.SendAudioEventListener() {
                    public void SendAudioCompleteEvent(SendAudioThread thread, String file) { }
                    public void SendAudioListCompleteEvent(SendAudioThread thread, List<String> files) {
                        if (files.size() == fileSplit.length && files.get(files.size() - 1).equalsIgnoreCase(fileSplit[fileSplit.length - 1]) ) {
                            if (logger.isDebugEnabled()) {
                                logger.debug( String.format("SendAudioListCompleteEvent(SendAudioThread thread = %s, List<String> files = %s)", thread, files));
                            }
                            try {
                                ivrCalls.endCall(c);
                            } catch(Exception e) { logger.error(e); }
                            try {
                                if (myJtapiDevice == null || myJtapiDevice.getTerminalConnections() == null) {
                                    if (logger.isDebugEnabled()) { logger.debug( "SendAudioListCompleteEvent(SendAudioThread thread, List<String> files)!" ); }
                                    return;
                                }
                                for (TerminalConnection tc : myJtapiDevice.getTerminalConnections()) {
                                    if (tc == null) { if (logger.isDebugEnabled()) { logger.debug( "SendAudioListCompleteEvent(SendAudioThread thread, List<String> files)!" ); } return; }
                                    CiscoConnection cc = (CiscoConnection) tc.getConnection();
                                    if (cc == null) { if (logger.isDebugEnabled()) { logger.debug( "SendAudioListCompleteEvent(SendAudioThread thread, List<String> files)!" ); } return; }
                                    CiscoCall c = (CiscoCall) cc.getCall();
                                    if (c != null && c.getCallID() != null && thread != null && (c.getCallID().intValue() == (int) thread.getIdentifier()) ) {
                                        if (logger.isTraceEnabled()) { logger.debug( "Drop call on PBX" ); }
                                        try { c.drop(); } catch(Exception e) { logger.error(e); }
                                        if (logger.isDebugEnabled()) {
                                            logger.debug( "SendAudioListCompleteEvent(SendAudioThread thread, List<String> files)!" );
                                        }
                                        return;
                                    }
                                }
                            } catch (Exception e) {
                                logger.error(e);
                            }
                        }
                    }
                });
                sat.setFileQueue(fileSplit);
            } catch(Exception e) { try { logger.error(e); sat.dispose(); } catch(Exception ex) { logger.error(ex); } }
        }
        if (logger.isDebugEnabled()) {
            logger.debug("playAndDrop(AskPhoneNumberCall call, String file)!");
        }
    }
    public void askingDDD(AskPhoneNumberCall call, boolean firstTime) {
        String askingDDD = configIVR.getAskingDDD();
        if (askingDDD != null && askingDDD.length() > 0) {
            IVRThreads.get(call.getCallID()).setFileQueue(askingDDD.split(","));
        }
    }
    public void askingNumber(AskPhoneNumberCall call, boolean firstTime) {
        String askingNumber = configIVR.getAskingNumber();
        if (askingNumber != null && askingNumber.length() > 0) {
            IVRThreads.get(call.getCallID()).setFileQueue(askingNumber.split(","));
        }
    }
    public void keyPadPressed(AskPhoneNumberCall call, char digit) {
        String keyPadPressed = configIVR.getKeyPadPressed();
        if (keyPadPressed != null && keyPadPressed.length() > 0) {
            IVRThreads.get(call.getCallID()).setFileQueue(keyPadPressed.replace("%d", fileForDigit(digit) ).split(","));
        }
    }
    private DestinationType getDestinationTypeByCall(AskPhoneNumberCall call) {
        String originalNumber = call.getOriginalPhoneNumber();
        PhoneNumber numberAnalyzer = PhoneNumber.fromBina(originalNumber);
        
        if (! numberAnalyzer.isValid()) {
            return setup.getInvalidNumberDestination();
        }

        if (numberAnalyzer.isMobile()) {
            // Decide what to do with mobile phone
            return setup.getMobilePhoneDestination();
        } else {
            // Decide what to do with land line
            return setup.getLandLineDestination();
        }
    }
    public void endedCall(AskPhoneNumberCall call) {
        if (logger.isDebugEnabled()) {
            logger.debug( String.format("endedCall(AskPhoneNumberCall call = %s)", call));
        }

        if (call.getCurrentStatus() != AskPhoneNumberCall.Status.Complete) {
            if (logger.isDebugEnabled()) {
                logger.debug( String.format("Incomplete call = %s)", call));
            }
            // Ended before complete get all digits (dropped call)
            DestinationType dt = getDestinationTypeByCall(call);
            if (dt == DestinationType.OptionalAnotherPhone) {
                notifyCompleteCallObservers(call);
            }
        }
        try {
            SendAudioThread sendAudio = IVRThreads.get(call.getCallID());
            sendAudio.setAlive(false);
            sendAudio.dispose();
            getIVRCalls().removeCall(call.getCallID());
            IVRThreads.remove(call.getCallID());
        } catch(Exception e) {
            logger.error(e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug( "endedCall(AskPhoneNumberCall call) !");
        }
    }
    public boolean notifyCompleteCallObservers (AskPhoneNumberCall call) {
        boolean success = false;
        // Save original number in queue (or queue history in case of insistence)
        for (IIVRObserver observer : ivrCompleteCallObserver) {
            PhoneNumber binaNumber = PhoneNumber.fromBina( call.getOriginalPhoneNumber() );
            PhoneNumber callBackNumber = PhoneNumber.fromBina( call.getCurrentStatus() == AskPhoneNumberCall.Status.Complete ?
                                                               call.getPhoneNumber() :
                                                               call.getOriginalPhoneNumber()
                                         );
            CallBackNumberType callBackNumberType = (call.getCurrentStatus() == AskPhoneNumberCall.Status.Complete) ?
                                                    CallBackNumberType.FromTyping :
                                                    CallBackNumberType.FromBina;
            success =
                observer.IVRCallComplete(
                    call.getCallID(),
                    binaNumber,
                    callBackNumber,
                    callBackNumberType)
                || success;
        }
        return success;
    }
    public void addedObserver(EventHandler<AskPhoneNumberCall> observer) {
    }
    public void removedObserver(EventHandler<AskPhoneNumberCall> observer) {
    }
    // </editor-fold>
}
