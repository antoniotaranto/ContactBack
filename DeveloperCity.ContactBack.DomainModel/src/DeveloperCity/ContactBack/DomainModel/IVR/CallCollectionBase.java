/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DomainModel.IVR;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author lbarbosa
 */
@SuppressWarnings("rawtypes")
public abstract class CallCollectionBase<T extends CallBase, Y extends EventHandler<T>> extends HashMap<Integer, T> implements EventHandler<T>, Serializable {

    private static final long serialVersionUID = 1L;
    protected Set<Y> observerList;

    protected abstract void onAddedObserver(Y observer);
    protected abstract void onRemovedObserver(Y observer);

    public CallCollectionBase() {
        observerList = new HashSet<Y>();
    }

    public void addObserver(Y observer) {
        if (observerList.contains(observer)) {
            return;
        }
        observerList.add(observer);
        for (Y o : observerList) {
            o.addedObserver(observer);
        }
        onAddedObserver(observer);
    }
    public void removeObserver(Y observer) {
        if (!observerList.contains(observer)) {
            return;
        }
        observerList.remove(observer);
        for (Y o : observerList) {
            o.removedObserver(observer);
        }
        onRemovedObserver(observer);
    }

    public T get(int callID) {
        return super.get(callID);
    }

    @SuppressWarnings("unchecked")
    public void addCall(T call) {
        if (super.containsKey(call.getCallID())) {
            return;
        }

        call.addObserver(this);
        put(call.getCallID(), call);
        call.init();
    }

    @SuppressWarnings("unchecked")
    public void removeCall(int callID) {
        if (!super.containsKey(callID)) {
            return;
        }

        T call = get(callID);
        remove(callID);
        call.removeObserver(this);
    }

    public void pressKey(int callID, char digit) {
        T call = get(callID);
        if (call != null) {
            call.pressKey(digit);
        }
    }

    protected abstract void onCallInit(T call);
    protected abstract void onKeyPadPressed(T call, char digit);
    protected abstract void onEndCall(T call);
    protected abstract void onEndedCall(T call);
    protected abstract void onValidDigit(T call, char digit);
    protected abstract void onInvalidDigit(T call, char digit);

    public void init(T call) {
        for (Y observer : observerList) {
            observer.init(call);
        }
        onCallInit(call);
    }
    public void keyPadPressed(T call, char digit) {
        for (Y observer : observerList) {
            observer.keyPadPressed(call, digit);
        }
        onKeyPadPressed(call, digit);
    }
    public void endCall(T call) {
        call.endCall();
        onEndCall(call);
    }
    public void endedCall(T call) {
        for (Y observer : observerList) {
            observer.endedCall(call);
        }
        onEndedCall(call);
    }
    public void validDigit(T call, char digit) {
        for (Y observer : observerList) {
            observer.validDigit(call, digit);
        }
        onValidDigit(call, digit);
    }
    public void invalidDigit(T call, char digit) {
        for (Y observer : observerList) {
            observer.invalidDigit(call, digit);
        }
        onInvalidDigit(call, digit);
    }
}
