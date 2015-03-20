/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.DomainModel.IVR;

/**
 *
 * @author lbarbosa
 */
@SuppressWarnings("rawtypes")
public abstract interface EventHandler<T extends CallBase> {
    void keyPadPressed(T call, char digit);
    void endedCall(T call);
    void validDigit(T call, char digit);
    void invalidDigit(T call, char digit);
    void addedObserver(EventHandler<T> observer);
    void removedObserver(EventHandler<T> observer);
    void init(T call);
}
