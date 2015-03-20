/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DomainModel.IVR;

import java.io.Serializable;
import java.net.InetAddress;

/**
 *
 * @author lbarbosa
 */
public class AskPhoneNumberCall extends CallBase<AskPhoneNumberEventHandler> implements Serializable {

    private static final long serialVersionUID = 1L;
    private Status currentStatus = Status.Empty;
    private String phoneNumber = "";

    public enum Status {

        Empty,
        DDDInProgress,
        NumberEmpty,
        NumberInProgress,
        Validating,
        Confirmation,
        Complete
    }

    public AskPhoneNumberCall(int callID, String originalPhoneNumber) {
        super(callID);
        super.originalPhoneNumber = originalPhoneNumber;
    }

    public AskPhoneNumberCall(int callID, InetAddress remoteIP, int remotePort, String originalPhoneNumber) {
        super(callID, remoteIP, remotePort, originalPhoneNumber);
    }

    @Override
    protected void onInit() {
        askDDD(true);
    }

    @Override
    protected void onKeyPadPressed(char digit) {
    }

    @Override
    protected void onEndCall() {
    }

    @Override
    protected void onEndedCall() {
    }

    void reviewStatus() {
        int size = phoneNumber.length();
        if (size == 0) {
            resetPhoneNumber();
        }
        else if (size >= 11) {
            PhoneNumber p = PhoneNumber.fromBina(phoneNumber);
            if (p.isValid()) {
                currentStatus = Status.Confirmation;
                for(AskPhoneNumberEventHandler observer : observerList) {
                    observer.askingConfirmation(this, phoneNumber, true);
                }
            } else {
                invalidFullNumber();
            }

        }
        else if (size == 10 && currentStatus == Status.Validating) {
            PhoneNumber p = PhoneNumber.fromBina(phoneNumber);
            if (p.isValid()) {
                currentStatus = Status.Confirmation;
                for(AskPhoneNumberEventHandler observer : observerList) {
                    observer.askingConfirmation(this, phoneNumber, true);
                }
            } else {
                invalidFullNumber();
            }
        }
        else if (size == 10 && currentStatus == Status.NumberInProgress && !phoneNumber.startsWith("119")) {
            PhoneNumber p = PhoneNumber.fromBina(phoneNumber);
            if (p.isValid()) {
                currentStatus = Status.Confirmation;
                for(AskPhoneNumberEventHandler observer : observerList) {
                    observer.askingConfirmation(this, phoneNumber, true);
                }
            } else {
                invalidFullNumber();
            }            
        }
        else if (size == 1) {
            currentStatus = Status.DDDInProgress;
            for(AskPhoneNumberEventHandler observer : observerList) {
                observer.sentenceChanged(this, phoneNumber, true);
            }
        }
        else if (size == 2) {
            currentStatus = Status.NumberEmpty;
            for(AskPhoneNumberEventHandler observer : observerList) {
                observer.dddComplete(this, phoneNumber);
                observer.askingNumber(this, true);
            }
        }
        else {
            currentStatus = Status.NumberInProgress;
            for(AskPhoneNumberEventHandler observer : observerList) {
                observer.sentenceChanged(this, phoneNumber, false);
            }
        }
    }

    @Override
    protected void onValidDigit(char digit) {
        if (digit == '*') {
            resetPhoneNumber();
        }
        else if (digit == '#' && currentStatus == Status.NumberInProgress) {
            currentStatus = Status.Validating;
            reviewStatus();
        }
        else if (digit == '#' && currentStatus == Status.Confirmation) {
            complete();            
        }
        else {
            phoneNumber += digit;
            reviewStatus();
        }
    }

    private void complete() {
        currentStatus = Status.Complete;
        for(AskPhoneNumberEventHandler observer : observerList) {
            observer.numberComplete(this, phoneNumber);
        }
    }
    private void resetPhoneNumber() {
        phoneNumber = "";
        for(AskPhoneNumberEventHandler observer : observerList) {
            observer.resetPhoneNumber(this);
        }
        askDDD(false);
    }
    private void askDDD(boolean firstTime) {
        currentStatus = Status.Empty;
        for(AskPhoneNumberEventHandler observer : observerList) {
            observer.askingDDD(this, firstTime);
        }
    }
    
    private void invalidFullNumber() {
        resetPhoneNumber();
    }
    
    @Override
    protected void onInvalidDigit(char digit) {
        if (currentStatus == Status.Complete) {
            // Complete, nothing is allowed
        }
        else if (currentStatus == Status.Empty && digit == '0') {
            // 1st DDD digit cannot be 0
            for(AskPhoneNumberEventHandler observer : observerList) {
                observer.invalidDDDStart(this, digit);
            }
        }
        else if (currentStatus == Status.NumberEmpty && (digit == '1' || digit == '0')) {
            // 1st number digit cannot be either 1 or 0
            for(AskPhoneNumberEventHandler observer : observerList) {
                observer.invalidNumberStart(this, digit);
            }
        }
        else if (currentStatus == Status.Confirmation) {
            // Trying to exceed digits
            for(AskPhoneNumberEventHandler observer : observerList) {
                observer.invalidConfirmationChar(this, digit);
                observer.askingConfirmation(this, phoneNumber, false);
            }
        }
        else if (digit == '#') {
            // It's not complete, validation digit is not allowed
        }
    }

    @Override
    protected void onAddedObserver(AskPhoneNumberEventHandler observer) {
    }

    @Override
    protected void onRemovedObserver(AskPhoneNumberEventHandler observer) {
    }

    @Override
    protected boolean digitValidation(char digit) {
        if (currentStatus == Status.Complete) {
            // Complete, nothing is allowed
            return false;
        }
        else if (currentStatus == Status.Empty && digit == '0') {
            // 1st DDD digit cannot be 0
            return false;
        }
        else if (digit == '*') {
            // Cleanup
            return true;
        }
        else if (currentStatus == Status.NumberEmpty && (digit == '1' || digit == '0')) {
            // 1st number digit cannot be either 1 or 0
            return false;
        }
        else if (currentStatus == Status.Confirmation && digit == '#') {
            // Confirmation digit
            return true;
        }
        else if (currentStatus == Status.Confirmation) {
            // Trying to exceed digits
            return false;
        }
        else if (phoneNumber.length() < 10 && digit == '#') {
            // It's not complete, validation digit is not allowed
            return false;
        }
        else if (digit == '#') {
            return true;
        }
        return true;
    }

    public Status getCurrentStatus() {
        return currentStatus;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
