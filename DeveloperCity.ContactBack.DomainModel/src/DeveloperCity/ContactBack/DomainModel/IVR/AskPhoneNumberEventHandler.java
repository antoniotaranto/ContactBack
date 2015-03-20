/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.DomainModel.IVR;

/**
 *
 * @author lbarbosa
 */
public interface AskPhoneNumberEventHandler extends EventHandler<AskPhoneNumberCall> {
    void resetPhoneNumber(AskPhoneNumberCall call);
    void invalidDDDStart(AskPhoneNumberCall call, char digit);
    void invalidNumberStart(AskPhoneNumberCall call, char digit);
    void invalidConfirmationChar(AskPhoneNumberCall call, char digit);
    void askingDDD(AskPhoneNumberCall call, boolean firstTime);
    void dddComplete(AskPhoneNumberCall call, String ddd);
    void askingNumber(AskPhoneNumberCall call, boolean firstTime);
    void numberComplete(AskPhoneNumberCall call, String number);
    void sentenceChanged(AskPhoneNumberCall call, String tempPhoneNumber, boolean isDDDPart);
    void askingConfirmation(AskPhoneNumberCall call, String number, boolean firstTime);
}