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
public class PlayAndDropCall extends CallBase<PlayAndDropEventHandler> implements Serializable {

    private static final long serialVersionUID = 1L;
    public PlayAndDropCall(int callID) {
        super(callID);
    }

    public PlayAndDropCall(int callID, InetAddress remoteIP, int remotePort, String originalPhoneNumber) {
        super(callID, remoteIP, remotePort, originalPhoneNumber);
    }

    @Override
    protected void onInit() {
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

    @Override
    protected void onValidDigit(char digit) {
    }

    @Override
    protected void onInvalidDigit(char digit) {
    }

    @Override
    protected boolean digitValidation(char digit) {
        return false;
    }
    @Override
    protected void onAddedObserver(PlayAndDropEventHandler observer) {
    }

    @Override
    protected void onRemovedObserver(PlayAndDropEventHandler observer) {
    }
}
