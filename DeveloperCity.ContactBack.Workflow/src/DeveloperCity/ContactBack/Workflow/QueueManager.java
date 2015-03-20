/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.Workflow;

import DeveloperCity.ContactBack.DomainModel.*;
import DeveloperCity.ContactBack.DomainModel.IVR.PhoneNumber;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.Logger;

/**
 *
 * @author lbarbosa
 */
public class QueueManager implements ICallReceived, IEnqueueAgent {

    private static Logger logger = Logger.getLogger(QueueManager.class);
    private RealTimeData realTimeData;
    private QueueService sQueue;
    private CallManagerService sCallManager;
    private boolean executing = false;

    public QueueManager(RealTimeData realTimeData, QueueService sQueue, CallManagerService sCallManager) {
        this.realTimeData = realTimeData;
        this.sQueue = sQueue;
        this.sCallManager = sCallManager;
    }

    void sendToDropCall(long callManagerCallID) {
        sCallManager.BlindTransferToQueueInvalidNumber(callManagerCallID);        
    }
    void sendToAskNumber(long callManagerCallID) {
        sCallManager.BlindTransferToQueueAskPhoneNumber(callManagerCallID);
    }
    void sendToQueueValidBina(long callManagerCallID, PhoneNumber validBina) {
        try {
            boolean success = EnQueue(callManagerCallID, validBina);
            if (success) {
                sCallManager.BlindTransferToQueueSuccess(callManagerCallID);
            }
            else {
                sCallManager.BlindTransferToQueueAlready(callManagerCallID);
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
    }
    void sendToQueueInvalidBina(long callManagerCallID, PhoneNumber validBina, PhoneNumber typedNumber) {
        try {
            boolean success = EnQueue(callManagerCallID, validBina, typedNumber);
            if (success) {
                sCallManager.BlindTransferToQueueSuccess(callManagerCallID);
            }
            else {
                sCallManager.BlindTransferToQueueAlready(callManagerCallID);
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
    }

    public boolean EnQueue(long callManagerCallID, PhoneNumber validBina) {
        try { boolean success = realTimeData.EnQueue(callManagerCallID, validBina, validBina, CallBackNumberType.FromBina, this); return success; }
        catch(Exception e) { return false; }
    }
    public boolean EnQueue(long callManagerCallID, PhoneNumber invalidBina, PhoneNumber typedNumber) {
        if (!typedNumber.isValid()) {
            logger.error(String.format("QueueManager.EnQueue => InvalidTypedNumber for CallManagerCallID: %d | Original Bina: %s | Typed Number : %s", callManagerCallID, invalidBina.getBina(), typedNumber.getBina() ));
            return false;
        }
        try { boolean success = realTimeData.EnQueue(callManagerCallID, invalidBina, typedNumber, CallBackNumberType.FromTyping, this); return success; }
        catch(Exception e) { return false; }
    }

    public void IncomingCall(String from, long callManagerCallID) {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int weekDay = cal.get(Calendar.DAY_OF_WEEK);
        if (realTimeData.isHolidayToday()) {
            if (logger.isInfoEnabled()) { logger.info("Holiday call from " + from); }
            sCallManager.BlindTransferToQueueNotInTimeShift(callManagerCallID);
            return;
        }
        switch (weekDay) {
            case Calendar.SUNDAY:
//                if (logger.isInfoEnabled()) { logger.info("Sunday call from " + from); }
//                    sCallManager.BlindTransferToQueueNotInTimeShift(callManagerCallID);
//                return;
            case Calendar.SATURDAY:
                if (( hour < realTimeData.getSetup().getShiftSaturdayStartHour() ) ||
                    ( hour == realTimeData.getSetup().getShiftSaturdayStartHour() && minute < realTimeData.getSetup().getShiftSaturdayStartMinute() ) ||
                    ( hour > realTimeData.getSetup().getShiftSaturdayEndHour() ) ||
                    ( hour == realTimeData.getSetup().getShiftSaturdayEndHour() && minute > realTimeData.getSetup().getShiftSaturdayEndMinute() )) {
                    sCallManager.BlindTransferToQueueNotInTimeShift(callManagerCallID);
                    return;
                }
                break;
            default:
                if (( hour < realTimeData.getSetup().getShiftWeekdayStartHour() ) ||
                    ( hour == realTimeData.getSetup().getShiftWeekdayStartHour() && minute < realTimeData.getSetup().getShiftWeekdayStartMinute() ) ||
                    ( hour > realTimeData.getSetup().getShiftWeekdayEndHour() ) ||
                    ( hour == realTimeData.getSetup().getShiftWeekdayEndHour() && minute > realTimeData.getSetup().getShiftWeekdayEndMinute() )) {
                    sCallManager.BlindTransferToQueueNotInTimeShift(callManagerCallID);
                    return;
                }
                break;
        }
        if (logger.isInfoEnabled()) {
            logger.info(String.format("Valid time incoming call: %s", from));
        }
        PhoneNumber callerIDAnalyzer = PhoneNumber.fromBina(from);
        if (!executing) {
            // Decide what to do with invalid number
            logger.fatal( String.format( "Call from %s but Queue Manager is not Executing.", callerIDAnalyzer.getBina()) );
            sendToDropCall(callManagerCallID);
            return;
        }
        if (! callerIDAnalyzer.isValid()) {
            if (logger.isInfoEnabled()) { logger.info(String.format("Formatted invalid call: '%s'", callerIDAnalyzer.getBina() )); }
            // Decide what to do with invalid number
            sendToAskNumber(callManagerCallID);
            return;
        }
        DeveloperCity.ContactBack.DomainModel.BlackList blackList = realTimeData.getBlackListPolicyByNumber( callerIDAnalyzer.getNumberToDial() );
        if (blackList != null) {
            if (logger.isInfoEnabled()) { logger.info( String.format("BlackListed: %s", callerIDAnalyzer.getNumberToDial() )); }
            if (blackList.getBlackListDestination() == DestinationType.DropCall) sendToDropCall(callManagerCallID);
            else if (blackList.getBlackListDestination() == DestinationType.Enqueue) sendToQueueValidBina(callManagerCallID, callerIDAnalyzer);
            else sendToAskNumber(callManagerCallID);
            return;
        }

        if (! callerIDAnalyzer.isMobile()) {
            if (logger.isInfoEnabled()) { logger.info(String.format("Land phone line valid call: %s", callerIDAnalyzer.getNumberToDial())); }
            // Decide what to do with land line
            if (realTimeData.getSetup().getLandLineDestination() == DestinationType.DropCall) sendToDropCall(callManagerCallID);
            else if (realTimeData.getSetup().getLandLineDestination() == DestinationType.Enqueue) sendToQueueValidBina(callManagerCallID, callerIDAnalyzer);
            else sendToAskNumber(callManagerCallID);
            return;
        }
        if ( callerIDAnalyzer.isMobile() ) {
            if (logger.isInfoEnabled()) { logger.info("Mobile phone valid call"); }
            // Decide what to do with mobile phone
            if (realTimeData.getSetup().getMobilePhoneDestination() == DestinationType.DropCall) sendToDropCall(callManagerCallID);
            else if (realTimeData.getSetup().getMobilePhoneDestination() == DestinationType.Enqueue) sendToQueueValidBina(callManagerCallID, callerIDAnalyzer);
            else sendToAskNumber(callManagerCallID);
            return;
        }
    }

    public boolean RefreshFromDatabase() {
        return this.realTimeData.Refresh(sQueue.getInQueue());
    }

    public Queue QueueCallingAgain(Queue oldQueueItem, long callManagerCallID) throws Exception {
        return sQueue.setQueueCallingAgain(oldQueueItem, callManagerCallID);
    }

    public Queue QueueFirstTime(long callManagerCallID, PhoneNumber binaNumber, PhoneNumber callBackNumber, CallBackNumberType callBackNumberType, Date estimatedTimeToAttend, int entryPosition, short PriorityValue) throws Exception {
        return sQueue.setQueueFirstTime(callManagerCallID, binaNumber.getBina(), binaNumber.isValid(), callBackNumber.getNumberToDial(), callBackNumberType, estimatedTimeToAttend, entryPosition, PriorityValue, null);
    }
    public WebQueue QueueFirstTime(PhoneNumber callBackNumber, Date estimatedTimeToAttend, int entryPosition, short PriorityValue, long customerID) throws Exception {
        return (WebQueue) sQueue.setQueueFirstTime(0, "?", false, callBackNumber.getNumberToDial(), CallBackNumberType.FromTyping, estimatedTimeToAttend, entryPosition, PriorityValue, null);
    }

    public void Start() {
        this.executing = true;
        this.sCallManager.addEntryPointObserver(this);
        this.sCallManager.setEnqueueAgent(this);
    }

    public void Stop() {
        this.executing = false;
        this.sCallManager.removeEntryPointObserver(this);
        this.sCallManager.setEnqueueAgent(null);
    }

    public boolean isExecuting() {
        return executing;
    }
}
