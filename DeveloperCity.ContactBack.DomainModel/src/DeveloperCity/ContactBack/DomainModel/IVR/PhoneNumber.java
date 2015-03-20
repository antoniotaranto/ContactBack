/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DomainModel.IVR;

import DeveloperCity.ContactBack.Exception.InvalidNumberOfDigitsException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author luizguitar
 */
public class PhoneNumber implements Serializable {
    private static final long serialVersionUID = 1L;
    private final static String defaultLocalArea = "11";
    private final static String prefixTextMessage = "55";    
    private final static List<String> nineDigitsMobileAreas = Arrays.asList(new String[] { "11" });
    private final static List<String> nineDigitsExceptions = Arrays.asList(new String[] { "1170", "1177", "1178", "1179" });
    private String bina;
    private String localArea;
    private String phone;
    private PhoneNumberType phoneNumberType = PhoneNumberType.Unknown;
    
    private PhoneNumber() { }

    public String getBina() {
        return bina;
    }

    public boolean isMobile() {
        return phoneNumberType == PhoneNumberType.MobilePhone;
    }
    
    public boolean isValid() {
        return phoneNumberType != PhoneNumberType.Unknown;
    }

    public String getLocalArea() {
        return localArea;
    }

    public String getPhone() {
        return phone;
    }
    public PhoneNumberType getPhoneNumberType() {
        return phoneNumberType;
    }
    
    public static boolean sameNumber(String firstPhone, String otherPhone) {
        if (firstPhone == null || otherPhone == null) return false;
        
        PhoneNumber firstPhoneObj = fromBina(firstPhone);
        if (firstPhoneObj.phoneNumberType == PhoneNumberType.Unknown) return false;
        
        PhoneNumber otherPhoneObj = fromBina(otherPhone);
        if (otherPhoneObj.phoneNumberType == PhoneNumberType.Unknown) return false;
        
        return firstPhoneObj.getNumberToDial().equals( otherPhoneObj.getNumberToDial() );
    }
    
    public static PhoneNumber fromBina(String bina) {
        PhoneNumber _phoneNumber = new PhoneNumber();
        _phoneNumber.bina = bina;
        try {
            _phoneNumber.binaToFields();
        } catch(InvalidNumberOfDigitsException e) {
            _phoneNumber.localArea = null;
            _phoneNumber.phone = null;
            _phoneNumber.phoneNumberType = PhoneNumberType.Unknown;
        }
        return _phoneNumber;
    }
    
    public String getNumberToDial() {
        if (phoneNumberType != PhoneNumberType.Unknown)
            return localArea + phone;
        else
            return null;
    }

    public String getNumberToText() {
        if (phoneNumberType != PhoneNumberType.Unknown)
            return prefixTextMessage + localArea + phone;
        else
            return null;
    }
    
    private void binaToFields() throws InvalidNumberOfDigitsException {
        phoneNumberType = PhoneNumberType.Unknown;
        localArea = null;
        phone = null;
        
        if (bina == null) {
            return;
        }
        
        String justNumbers = bina;
        justNumbers = justNumbers.replaceAll( "[^\\d]", "" ).trim();
        
        binaUnknownLength(justNumbers);
        
        if (shouldInsertNineAtEnd()) {
            this.phone = "9" + this.phone;
        }
    }
    private boolean shouldInsertNineAtEnd() {
        
        return false;
        
        /*
        if (!isValid())
            return false;
        
        if (!isMobile())
            return false;
        
        if (phone.length() != 8)
            return false;
        
        if (nineDigitsMobileAreas == null || nineDigitsMobileAreas.size() == 0)
            return false;
        
        if (!nineDigitsMobileAreas.contains(this.getLocalArea()))
            return false;
        
        if ( nineDigitsExceptions == null || nineDigitsExceptions.size() == 0 )
            return true;
        
        if ( nineDigitsExceptions.contains(  this.getNumberToDial().substring(0, 4) )  )
            return false;
        
        return true;
        */
    }
    private void binaUnknownLength(String justNumbers) throws InvalidNumberOfDigitsException {
        if (justNumbers.length() < 8) {
            return;
        }
        
        if (justNumbers.length() == 8) {
            bina8Digits(justNumbers);
        }
        else if (justNumbers.length() == 9) {
            bina9Digits(justNumbers);
        }
        else if (justNumbers.length() == 10) {
            bina10Digits(justNumbers);
        }
        else if (justNumbers.length() == 11) {
            bina11Digits(justNumbers);
        }
        else if (justNumbers.length() >= 12) {
            bina12PlusDigits(justNumbers);            
        }        
    }
    
    private void bina8Digits(String justNumbers) throws InvalidNumberOfDigitsException {
        if (justNumbers.length() != 8) {
            throw new InvalidNumberOfDigitsException(8, justNumbers);
        }

        int firstDigit = Integer.parseInt( justNumbers.substring(0, 1) );

        if ( firstDigit < 2 ) {
            return;
        }

        if ( firstDigit > 5 ) {
            phoneNumberType = PhoneNumberType.MobilePhone;
        } else {
            phoneNumberType = PhoneNumberType.Landline;
        }

        localArea = defaultLocalArea;
        phone = justNumbers;
    }
    
    private void bina9Digits(String justNumbers) throws InvalidNumberOfDigitsException {
        if (justNumbers.length() != 9) {
            throw new InvalidNumberOfDigitsException(9, justNumbers);
        }
            
        int firstDigit = Integer.parseInt( justNumbers.substring(0, 1) );
        
        if (firstDigit == 0) {
            bina8Digits( justNumbers.substring(1) );
            return;
        }

        if (firstDigit < 9) {
            return;
        }
        
        phone = justNumbers;
        localArea = defaultLocalArea;
        phoneNumberType = PhoneNumberType.MobilePhone;
    }

    private void bina10Digits(String justNumbers) throws InvalidNumberOfDigitsException {
        if (justNumbers.length() != 10) {
            throw new InvalidNumberOfDigitsException(10, justNumbers);
        }
        
        int firstDigit = Integer.parseInt( justNumbers.substring(0, 1) );
        
        if (firstDigit == 0) {
            bina9Digits( justNumbers.substring(1) );
            return;
        }

        String possibleLocalArea = justNumbers.substring(0, 2);
        if ( Double.parseDouble( possibleLocalArea ) % 10d == 0 ) {
            return;
        }
        
        String possibleNumber = justNumbers.substring(2);
        
        bina8Digits(possibleNumber);
        
        if ( isValid() ) {
            localArea = possibleLocalArea;
        }
    }

    private void bina11Digits(String justNumbers) throws InvalidNumberOfDigitsException {
        if (justNumbers.length() != 11) {
            throw new InvalidNumberOfDigitsException(11, justNumbers);
        }
        
        int firstDigit = Integer.parseInt( justNumbers.substring(0, 1) );
        
        if (firstDigit == 0) {
            bina10Digits( justNumbers.substring(1) );
            return;
        }

        String possibleLocalArea = justNumbers.substring(0, 2);
        if ( ! nineDigitsMobileAreas.contains(possibleLocalArea) ) {
            return;
        }
        
        String possibleNumber = justNumbers.substring(2);
        int firstNumberDigit = Integer.parseInt( possibleNumber.substring(0, 1) );
        
        if (firstNumberDigit == 0) {
            return;
        }
        
        bina9Digits(possibleNumber);
        
        if ( isValid() ) {
            localArea = possibleLocalArea;
        }
    
    }
    
    private void bina12PlusDigits(String justNumbers) throws InvalidNumberOfDigitsException {
        if (justNumbers.length() < 12) {
            throw new InvalidNumberOfDigitsException(12, justNumbers);
        }

        int firstDigit = Integer.parseInt( justNumbers.substring(0, 1) );
        
        if (firstDigit == 0) {
            binaUnknownLength(justNumbers.substring(1));
            return;
        }
        
        String takeEleven = justNumbers.substring(justNumbers.length() - 11);
        
        if (takeEleven.startsWith("119")) {
            bina11Digits(takeEleven);
        } else {
            bina10Digits(takeEleven.substring(1));
        }
        
        return;
    }
    
    public enum PhoneNumberType {
        Landline,
        MobilePhone,
        Unknown
    }
}
