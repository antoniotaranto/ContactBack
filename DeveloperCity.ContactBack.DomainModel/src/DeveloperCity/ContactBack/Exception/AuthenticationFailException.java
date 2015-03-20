/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Exception;

/**
 *
 * @author lbarbosa
 */
public class AuthenticationFailException extends Exception {
    private static final long serialVersionUID = 1L;
    public AuthenticationFailException(String message) {
        super(message);
    }
}
