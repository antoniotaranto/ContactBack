/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DomainModel.IVR;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author lbarbosa
 */
@SuppressWarnings("rawtypes")
public abstract class CallBase<T extends EventHandler> implements Serializable {

    private static final long serialVersionUID = 1L;
    private int callID;
    private InetAddress remoteIP;
    private int remotePort;
    protected Set<T> observerList;
    private boolean started = false;
    protected String originalPhoneNumber;

    public CallBase(int callID) {
        observerList = new HashSet<T>();
        this.callID = callID;
    }

    public CallBase(int callID, InetAddress remoteIP, int remotePort, String originalPhoneNumber) {
        observerList = new HashSet<T>();
        this.callID = callID;
        this.remoteIP = remoteIP;
        this.remotePort = remotePort;
        this.originalPhoneNumber = originalPhoneNumber;
    }

    protected abstract void onInit();
    protected abstract void onKeyPadPressed(char digit);
    protected abstract void onEndCall();
    protected abstract void onEndedCall();
    protected abstract void onValidDigit(char digit);
    protected abstract void onInvalidDigit(char digit);
    protected abstract void onAddedObserver(T observer);
    protected abstract void onRemovedObserver(T observer);
    protected abstract boolean digitValidation(char digit);

    @SuppressWarnings("unchecked")
    public void init() {
        if (started) {
            return;
        }
        started = true;
        for (T observer : observerList) {
            observer.init(this);
        }
        onInit();
    }

    @SuppressWarnings("unchecked")
    public void pressKey(char digit) {
        for (T observer : observerList) {
            observer.keyPadPressed(this, digit);
        }
        onKeyPadPressed(digit);
        boolean valid = digitValidation(digit);
        if (valid) {
            validDigit(digit);
        }
        else {
            invalidDigit(digit);
        }
    }

    protected void endCall() {
        onEndCall();
        endedCall();
    }

    @SuppressWarnings("unchecked")
    public void endedCall() {
        for (T observer : observerList) {
            observer.endedCall(this);
        }
        onEndedCall();
    }

    @SuppressWarnings("unchecked")
    private void validDigit(char digit) {
        for (T observer : observerList) {
            observer.validDigit(this, digit);
        }
        onValidDigit(digit);
    }

    @SuppressWarnings("unchecked")
    private void invalidDigit(char digit) {
        for (T observer : observerList) {
            observer.invalidDigit(this, digit);
        }
        onInvalidDigit(digit);
    }

    @SuppressWarnings("unchecked")
    protected void addObserver(T observer) {
        if (observerList.contains(observer)) {
            return;
        }
        observerList.add(observer);
        for (T o : observerList) {
            o.addedObserver(observer);
        }
        onAddedObserver(observer);
    }

    @SuppressWarnings("unchecked")
    protected void removeObserver(T observer) {
        if (!observerList.contains(observer)) {
            return;
        }
        observerList.remove(observer);
        for (T o : observerList) {
            o.removedObserver(observer);
        }
        onRemovedObserver(observer);
    }

    public int getCallID() {
        return callID;
    }

    public InetAddress getRemoteIP() {
        return remoteIP;
    }

    public void setRemoteIP(InetAddress remoteIP) {
        this.remoteIP = remoteIP;
    }

    public int getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(int remotePort) {
        this.remotePort = remotePort;
    }
    public String getOriginalPhoneNumber() {
        return originalPhoneNumber;
    }
    @Override
    public String toString() {
        return String.format("CallBase<T> CallID=%d, remoteIP=%s, remotePort=%d, originalPhoneNumber=%s", callID, remoteIP, remotePort, originalPhoneNumber);
    }
}
