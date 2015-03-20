package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.ContactBack.Workflow.Telephony.IIVRObserver;
import DeveloperCity.DataAccess.HibernateSession;
import DeveloperCity.ContactBack.DataAccess.CallManagerDAO;
import DeveloperCity.ContactBack.DomainModel.*;
import DeveloperCity.ContactBack.DomainModel.IVR.PhoneNumber;
import DeveloperCity.ContactBack.Exception.CallManagerUndefinedException;
import DeveloperCity.ContactBack.Workflow.Telephony.CallCenterDevices;
import DeveloperCity.ContactBack.Workflow.Telephony.CallControl;
import DeveloperCity.ContactBack.Workflow.Telephony.CallManagerProvider;
import DeveloperCity.ContactBack.Workflow.Telephony.EntryPointCTI;
import DeveloperCity.ContactBack.Workflow.Telephony.IVRAskPhoneNumber;
import DeveloperCity.ContactBack.Workflow.Telephony.IVRPlayAndDrop;
import DeveloperCity.ContactBack.Workflow.Telephony.IVRSayEstimatedTimeToAttend;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.telephony.JtapiPeerUnavailableException;
import javax.telephony.InvalidArgumentException;
import javax.telephony.InvalidStateException;
import javax.telephony.MethodNotSupportedException;
import javax.telephony.PlatformException;
import javax.telephony.ResourceUnavailableException;
import com.cisco.jtapi.JtapiPeerUnavailableExceptionImpl;
import com.cisco.jtapi.extensions.CiscoRegistrationException;
import java.util.Date;
import javax.telephony.InvalidPartyException;
import javax.telephony.PrivilegeViolationException;

public class CallManagerService implements IIVRObserver {

    // <editor-fold defaultstate="collapsed" desc="Fields">
    private static Logger logger = Logger.getLogger(CallManagerService.class);
    private CallManager publisherServer;
    final Object syncCallManager = new Object();
    private final RealTimeData realTimeData;
    private CallManagerProvider callManagerProvider;
    private EntryPointCTI entryPointCTI;
    private CallCenterDevices callCenterDevices;
    private CallControl callControl;
    private IEnqueueAgent enqueueAgent;
    private IVRAskPhoneNumber ivrDevice;
    private IVRSayEstimatedTimeToAttend ivrQueueSuccess;
    private IVRPlayAndDrop ivrQueueAlready;
    private IVRPlayAndDrop ivrQueueInvalidNumber;
    private IVRPlayAndDrop ivrQueueNotInTimeShift;
    // </editor-fold>

    private class WaitTimeCalculator implements IWaitTimeEstimativeCalculator {
        private WaitTimeCalculator() {

        }
        public Queue getQueueObject(String phoneNumber) {
            Queue q = realTimeData.getQueueByPhoneNumber(phoneNumber);
            if (q == null) {
                logger.error("WaitTimeCalculator.calculate(String phoneNumber) => Queue item not found: " + phoneNumber);
                return null;
            }
            return q;
        }
        public long calculate(Queue queueItem) {
            long now = new Date().getTime();
            long longEstimated = queueItem.getEstimatedTimeToAttend().getTime();
            return (long) Math.max(0L, Math.floor( ((double)(longEstimated - now)) / 1000D ));
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Constructor, connect">
    public CallManagerService(RealTimeData realTimeDataObject) throws CallManagerUndefinedException {
        this.realTimeData = realTimeDataObject;
        fillPublisherServer();
        callManagerProvider = new CallManagerProvider();
        entryPointCTI = new EntryPointCTI(realTimeData.getSetup().getCTI_DeviceName(), callManagerProvider);
        callCenterDevices = new CallCenterDevices(callManagerProvider);
        ivrDevice = new IVRAskPhoneNumber(this.realTimeData.getSetup().getConfigIVR(), this.realTimeData.getSetup(), callManagerProvider, this, 48302, new WaitTimeCalculator());
        ivrDevice.addIVRCallCompleteObserver(this);
        callControl = new CallControl(callManagerProvider);
        ivrQueueSuccess = new IVRSayEstimatedTimeToAttend(
                realTimeData.getSetup().getQueueSuccessDevice(),
                realTimeData.getSetup().getVoiceFolder(),
                realTimeData.getSetup().getConfigIVR().getSuccess(),
                callManagerProvider, 48304, this.realTimeData.getSetup().getConfigIVR(),
                new WaitTimeCalculator());
        ivrQueueAlready = new IVRPlayAndDrop(realTimeData.getSetup().getQueueAlreadyDevice(), this.realTimeData.getSetup().getVoiceFolder(), realTimeData.getSetup().getConfigIVR().getCallingAgain(), callManagerProvider, 48306);
        ivrQueueInvalidNumber = new IVRPlayAndDrop(realTimeData.getSetup().getQueueInvalidNumberDevice(), this.realTimeData.getSetup().getVoiceFolder(), realTimeData.getSetup().getConfigIVR().getInvalidNumberStart(), callManagerProvider, 48308);
        ivrQueueNotInTimeShift = new IVRPlayAndDrop(realTimeData.getSetup().getQueueNotInShiftTimeDevice(), this.realTimeData.getSetup().getVoiceFolder(), realTimeData.getSetup().getConfigIVR().getInvalidTime(), callManagerProvider, 48310);
    }
    public void Connect() throws JtapiPeerUnavailableExceptionImpl, ResourceUnavailableException, MethodNotSupportedException, InvalidArgumentException, CiscoRegistrationException, PlatformException, InvalidStateException, JtapiPeerUnavailableException {
        logger.info("Connecting to call manager...");
        callManagerProvider.Connect(publisherServer.getConnectionString());
        logger.info("Connected");

        logger.info("Adding agent observers...");
        callCenterDevices.registerAgentDevices(realTimeData.getAllAgents());
        logger.info("Added");

        logger.info("Registering IVR Play And Drop...");
        ivrQueueSuccess.init();
        ivrQueueAlready.init();
        ivrQueueInvalidNumber.init();
        ivrQueueNotInTimeShift.init();
        logger.info("Registered");

        logger.info("Registering IVR Ask Phone Number...");
        ivrDevice.init();
        logger.info("Registered");

        logger.info("Registering CTI Entry Point...");
        entryPointCTI.init();
        logger.info("Registered");
    }
    public boolean testDeviceAndExtension(String terminal, String extension) {
        return this.callCenterDevices.isTerminalExtensionValid(terminal, extension);
    }
    public boolean registerAgentDevice(Agent a) {
        try {
            this.callCenterDevices.registerAgentDevice(a);
            return true;
        } catch(Exception e) {
            return false;
        }
    }
    public boolean unregisterAgentDevice(long agentID) {
        try { return this.callCenterDevices.unregisterAgentDevice(agentID); }
        catch(Exception e) { return false; }
    }
    private void fillPublisherServer() throws CallManagerUndefinedException {
        synchronized (syncCallManager) {
            List<CallManager> callManagers = getAll();
            if (callManagers == null || callManagers.isEmpty()) {
                throw new CallManagerUndefinedException("No CallManager set on configuration.");
            }
            publisherServer = callManagers.get(0);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Call Manager Call Control">
    public Call Dial(Call c) throws InvalidArgumentException, ResourceUnavailableException, PrivilegeViolationException, InvalidPartyException, InvalidStateException, MethodNotSupportedException, Exception {
        String dialingTo =
                String.format("%s%s", realTimeData.getSetup().getPrefixDial(), c.getQueue().getCallBackNumber());
        long callManagerCallID = callControl.Dial(c.getAgent().getTerminal(), c.getAgent().getDirectoryNumber(), dialingTo);
        c.setCallManagerCallID(callManagerCallID);
        return c;
    }
    public void BlindTransferToQueueSuccess(long callManagerCallID) {
        callControl.BlindTransfer(callManagerCallID, ivrQueueSuccess.getFirstAddress() );
    }
    public void BlindTransferToQueueAlready(long callManagerCallID) {
        callControl.BlindTransfer(callManagerCallID, ivrQueueAlready.getFirstAddress() );
    }
    public void BlindTransferToQueueInvalidNumber(long callManagerCallID) {
        logger.info(String.format("TransferToQueueInvalidNumber(%d)", callManagerCallID));
        callControl.BlindTransfer(callManagerCallID, ivrQueueInvalidNumber.getFirstAddress() );
        logger.info("TransferToQueueInvalidNumber(long) !");
    }
    public void BlindTransferToQueueNotInTimeShift(long callManagerCallID) {
        logger.info(String.format("TransferToQueueNotInTimeShift(%d)", callManagerCallID));
        callControl.BlindTransfer(callManagerCallID, ivrQueueNotInTimeShift.getFirstAddress() );
        logger.info("TransferToQueueNotInTimeShift(long) !");
    }
    public void BlindTransferToQueueAskPhoneNumber(long callManagerCallID) {
        logger.info(String.format("TransferToQueueAskPhoneNumber(%d)", callManagerCallID));
        callControl.BlindTransfer(callManagerCallID, ivrDevice.getFirstAddress());
        logger.info("TransferToQueueAskPhoneNumber(long) !");
    }

    public void TransferToQueueSuccess(long callManagerCallID) {
        callControl.Transfer(callManagerCallID, ivrQueueSuccess.getFirstAddress() );
    }
    public void TransferToQueueAlready(long callManagerCallID) {
        callControl.Transfer(callManagerCallID, ivrQueueAlready.getFirstAddress() );
    }
    public void TransferToQueueInvalidNumber(long callManagerCallID) {
        logger.info(String.format("TransferToQueueInvalidNumber(%d)", callManagerCallID));
        callControl.Transfer(callManagerCallID, ivrQueueInvalidNumber.getFirstAddress() );
        logger.info("TransferToQueueInvalidNumber(long) !");
    }
    public void TransferToQueueNotInTimeShift(long callManagerCallID) {
        logger.info(String.format("TransferToQueueNotInTimeShift(%d)", callManagerCallID));
        callControl.Transfer(callManagerCallID, ivrQueueNotInTimeShift.getFirstAddress() );
        logger.info("TransferToQueueNotInTimeShift(long) !");
    }
    public void TransferToQueueAskPhoneNumber(long callManagerCallID) {
        logger.info(String.format("TransferToQueueAskPhoneNumber(%d)", callManagerCallID));
        callControl.Transfer(callManagerCallID, ivrDevice.getFirstAddress());
        logger.info("TransferToQueueAskPhoneNumber(long) !");
    }

    public boolean IsInService(String terminal) { return callControl.IsInService(terminal); }
    public void EndCall(long callManagerCallID) { callControl.EndCall(callManagerCallID); }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Get">
    public CallManager getByID(long id) {
        CallManager retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = CallManagerDAO.CreateInstance(s).getByID(id);
        tx.commit();
        s.close();

        return retValue;
    }
    public static CallManager getFirst() {
        CallManager retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        List<? extends CallManager> all = CallManagerDAO.CreateInstance(s).getAll();
        if (all != null && all.size() > 0) {
            retValue = all.get(0);
        }
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="List">

    @SuppressWarnings("unchecked")
    public List<CallManager> getAll() {
        List<CallManager> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<CallManager>) CallManagerDAO.CreateInstance(s).getAll();
        tx.commit();
        s.close();

        return retValue;
    }
    @SuppressWarnings("unchecked")
    public List<CallManager> getAll(int currentPage, int pageSize) {
        List<CallManager> retValue = null;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = (List<CallManager>) CallManagerDAO.CreateInstance(s).getAll(currentPage, pageSize);
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Scalar">

    public int countAll() {
        int retValue = 0;

        Session s = HibernateSession.getSessionFactory().openSession();
        Transaction tx = s.beginTransaction();
        retValue = CallManagerDAO.CreateInstance(s).CountAll();
        tx.commit();
        s.close();

        return retValue;
    }
    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc="Save">

    public boolean changeCallManager(CallManager callManager) {
        try {
            setCallManager(callManager);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public CallManager setCallManager(CallManager callManager) throws Exception {
        CallManager retValue = CallManagerService.setCallManagerStatic(callManager);
        fillPublisherServer();
        return retValue;
    }
    @SuppressWarnings("unchecked")
    public static CallManager setCallManagerStatic(CallManager callManager) throws Exception {
        Session s = HibernateSession.getSessionFactory().openSession();
        org.hibernate.Transaction tx = s.beginTransaction();

        try {
            callManager = ((CallManagerDAO<CallManager>) CallManagerDAO.CreateInstance(s)).setCallManager(callManager);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        } finally {
            s.close();
        }
        return callManager;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Listeners (add/remove)">
    public void addEntryPointObserver(ICallReceived observer) {
        this.entryPointCTI.addEntryPointObserver(observer);
    }
    public void removeEntryPointObserver(ICallReceived observer) {
        this.entryPointCTI.removeEntryPointObserver(observer);
    }
    public void addAgentCallObserver(IAgentCallObserver observer) {
        callCenterDevices.addAgentCallObserver(observer);
    }
    public void removeAgentCallObserver(IAgentCallObserver observer) {
        callCenterDevices.removeAgentCallObserver(observer);
    }
    public void addAgentBreakObserver(IAgentBreakObserver observer) {
        callCenterDevices.addAgentBreakListener(observer);
    }
    public void removeAgentBreakObserver(IAgentBreakObserver observer) {
        callCenterDevices.removeAgentBreakListener(observer);
    }
    public boolean IVRCallComplete(long callManagerCallID, PhoneNumber originalNumber, PhoneNumber typedNumber, CallBackNumberType callBackNumberType) {
        if (enqueueAgent == null) {
            return false;
        }
        if (callBackNumberType == CallBackNumberType.FromBina) {
            return enqueueAgent.EnQueue(callManagerCallID, originalNumber);
        } else {
            return enqueueAgent.EnQueue(callManagerCallID, originalNumber, typedNumber);
        }
    }
    public IEnqueueAgent getEnqueueAgent() {
        return enqueueAgent;
    }
    public void setEnqueueAgent(IEnqueueAgent enqueueAgent) {
        this.enqueueAgent = enqueueAgent;
    }
    // </editor-fold>
}
