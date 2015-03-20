/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
public class PlayAndDropCallCollection extends CallCollectionBase<PlayAndDropCall, PlayAndDropEventHandler> implements PlayAndDropEventHandler, Serializable {

    private static final long serialVersionUID = 1L;
    public PlayAndDropCallCollection() {
    }

    @Override
    protected void onAddedObserver(PlayAndDropEventHandler observer) {
    }

    @Override
    protected void onRemovedObserver(PlayAndDropEventHandler observer) {
    }

    @Override
    protected void onCallInit(PlayAndDropCall call) {
    }

    @Override
    protected void onKeyPadPressed(PlayAndDropCall call, char digit) {
    }

    @Override
    protected void onEndCall(PlayAndDropCall call) {
    }

    @Override
    protected void onEndedCall(PlayAndDropCall call) {
    }

    @Override
    protected void onValidDigit(PlayAndDropCall call, char digit) {
    }

    @Override
    protected void onInvalidDigit(PlayAndDropCall call, char digit) {
    }

    public void addedObserver(EventHandler<PlayAndDropCall> observer) {
    }

    public void removedObserver(EventHandler<PlayAndDropCall> observer) {
    }
}
