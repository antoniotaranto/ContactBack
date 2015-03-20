/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.Exception;

import org.apache.log4j.Logger;

/**
 *
 * @author luizguitar
 */
public class InvalidNumberOfDigitsException extends Exception {
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(InvalidNumberOfDigitsException.class);

    public InvalidNumberOfDigitsException(int waitingFor, String receivedNumber) {
        super(String.format("Expected %d digits, but got the number '%s'", waitingFor, receivedNumber ));
        
        logger.fatal( super.getMessage() );
    }
}
