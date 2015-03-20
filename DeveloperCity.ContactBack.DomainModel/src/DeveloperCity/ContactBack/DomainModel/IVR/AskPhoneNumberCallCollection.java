/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.DomainModel.IVR;

import java.io.Serializable;

/**
 *
 * @author lbarbosa
 */
public class AskPhoneNumberCallCollection extends CallCollectionBase<AskPhoneNumberCall, AskPhoneNumberEventHandler> implements AskPhoneNumberEventHandler, Serializable {

    private static final long serialVersionUID = 1L;
    public AskPhoneNumberCallCollection() {
    }

    @Override
    protected void onAddedObserver(AskPhoneNumberEventHandler observer) {
    }

    @Override
    protected void onRemovedObserver(AskPhoneNumberEventHandler observer) {
    }

    @Override
    protected void onCallInit(AskPhoneNumberCall call) {
    }

    @Override
    protected void onKeyPadPressed(AskPhoneNumberCall call, char digit) {
    }

    @Override
    protected void onEndCall(AskPhoneNumberCall call) {
    }

    @Override
    protected void onEndedCall(AskPhoneNumberCall call) {
    }

    @Override
    protected void onValidDigit(AskPhoneNumberCall call, char digit) {
    }

    @Override
    protected void onInvalidDigit(AskPhoneNumberCall call, char digit) {
    }

    public void addedObserver(EventHandler<AskPhoneNumberCall> observer) {
    }

    public void removedObserver(EventHandler<AskPhoneNumberCall> observer) {
    }

    public void resetPhoneNumber(AskPhoneNumberCall call) {
        for (AskPhoneNumberEventHandler observer : observerList) {
            observer.resetPhoneNumber(call);
        }
    }
    public void invalidDDDStart(AskPhoneNumberCall call, char digit) {
        for (AskPhoneNumberEventHandler observer : observerList) {
            observer.invalidDDDStart(call, digit);
        }
    }
    public void invalidNumberStart(AskPhoneNumberCall call, char digit) {
        for (AskPhoneNumberEventHandler observer : observerList) {
            observer.invalidNumberStart(call, digit);
        }
    }
    public void invalidConfirmationChar(AskPhoneNumberCall call, char digit) {
        for (AskPhoneNumberEventHandler observer : observerList) {
            observer.invalidConfirmationChar(call, digit);
        }
    }
    public void askingDDD(AskPhoneNumberCall call, boolean firstTime) {
        for (AskPhoneNumberEventHandler observer : observerList) {
            observer.askingDDD(call, firstTime);
        }
    }
    public void dddComplete(AskPhoneNumberCall call, String ddd) {
        for (AskPhoneNumberEventHandler observer : observerList) {
            observer.dddComplete(call, ddd);
        }
    }
    public void askingNumber(AskPhoneNumberCall call, boolean firstTime) {
        for (AskPhoneNumberEventHandler observer : observerList) {
            observer.askingNumber(call, firstTime);
        }
    }
    public void numberComplete(AskPhoneNumberCall call, String number) {
        for (AskPhoneNumberEventHandler observer : observerList) {
            observer.numberComplete(call, number);
        }
    }
    public void sentenceChanged(AskPhoneNumberCall call, String tempPhoneNumber, boolean isDDDPart) {
        for (AskPhoneNumberEventHandler observer : observerList) {
            observer.sentenceChanged(call, tempPhoneNumber, isDDDPart);
        }
    }
    public void askingConfirmation(AskPhoneNumberCall call, String number, boolean firstTime) {
        for (AskPhoneNumberEventHandler observer : observerList) {
            observer.askingConfirmation(call, number, firstTime);
        }
    }
}
