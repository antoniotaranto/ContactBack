/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.DomainModel;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Properties;
import org.apache.log4j.Logger;

/**
 *
 * @author lbarbosa
 */
public class ConfigIVR implements Serializable {
    private static Logger logger = Logger.getLogger(ConfigIVR.class);
    private static final long serialVersionUID = 1L;
    private String success;
    private String callingAgain;
    private String invalidNumber;
    private String invalidTime;
    private String initRequired;
    private String initOptional;
    private String validDigit;
    private String invalidDigit;
    private String sentenceChanged;
    private String resetPhoneNumber;
    private String invalidDDDStart;
    private String invalidNumberStart;
    private String invalidConfirmationChar;
    private String dddComplete;
    private String askingConfirmation;
    private String numberCompleteSuccess;
    private String numberCompleteFail;
    private String askingDDD;
    private String askingNumber;
    private String keyPadPressed;
    private String digitPound;
    private String digitStar;
    private String hour;
    private String hours;
    private String minute;
    private String minutes;
    private String second;
    private String seconds;
    private String and;
    private String estimatedTimeIs;
    private String number100andM;
    private String number200andM;
    private String number100andF;
    private String number200andF;


    private HashMap<Integer, String> numbersM = new HashMap<Integer, String>();
    private HashMap<Integer, String> numbersF = new HashMap<Integer, String>();

    public ConfigIVR() {
    }

    public String getFullTimeFiles(long totalSeconds, boolean saySeconds) {
        logger.info(String.format("getFullTimeFiles(long totalSeconds = %d, boolean saySeconds = %s)", totalSeconds, saySeconds));
        long[] timespan = Utils.TimeSpan(totalSeconds);
        long vdays = timespan[0];
        long vhours = timespan[1];
        long vtotalHours = vhours + (24*vdays);
        long vminutes = timespan[2];
        long vseconds = timespan[3];
        if (!saySeconds) {
            if (vseconds < 30) {
                vseconds = 0;
            } else {
                vseconds = 0;
                vminutes++;
                if (vminutes >= 60) {
                    vtotalHours++;
                    vminutes -= 60;
                }
            }
        }
        boolean sayHours = false;
        boolean sayMinutes = true;
        if (vseconds == 0) {
            saySeconds = false;
        }
        if (vminutes == 0 && vseconds == 0) {
            sayMinutes = false;
        }
        if (vtotalHours > 0) {
            sayHours = true;
        }

        StringBuilder sb = new StringBuilder();
        if (sayHours) {
            sb.append(getNumberF(String.valueOf(vtotalHours)));
            sb.append(",");
            sb.append((vtotalHours != 1 ? hours : hour));
            if (sayMinutes || saySeconds) {
                sb.append(",");
            }
            if (sayMinutes != saySeconds) {
                sb.append(and);
                sb.append(",");
            }
        }
        if (sayMinutes) {
            sb.append(getNumberM(String.valueOf(vminutes)));
            sb.append(",");
            sb.append((vminutes != 1 ? minutes : minute));
            if (saySeconds) {
                sb.append(",");
                sb.append(and);
                sb.append(",");
            }
        }
        if (saySeconds || (!sayMinutes && !sayHours)) {
            sb.append(getNumberM(String.valueOf(vseconds)));
            sb.append(",");
            sb.append((vseconds != 1 ? seconds : second));
        }

        logger.info(sb.toString());
        logger.info("getFullTimeFiles(long totalSeconds, boolean saySeconds) !");
        return sb.toString();
    }

    public String getNumberM(char digit) {
        if (digit == '*') {
            return (digitStar == null || digitStar.length() == 0) ? null : digitStar;
        } else if (digit == '#') {
            return (digitPound == null || digitPound.length() == 0) ? null : digitPound;
        }
        return getNumberM(String.valueOf(digit));
    }
    public String getNumberF(char digit) {
        if (digit == '*') {
            return (digitStar == null || digitStar.length() == 0) ? null : digitStar;
        } else if (digit == '#') {
            return (digitPound == null || digitPound.length() == 0) ? null : digitPound;
        }
        return getNumberF(String.valueOf(digit));
    }
    public String getNumberM(String number) {
        Integer n = Integer.parseInt(number);
        if (n > 299) {
            n = 299;
        }

        if (n > 100 && n < 200) {
            return this.number100andM + "," + getNumberM( String.valueOf(n - 100) );
        } else if (n > 200 && n < 300) {
            return this.number200andM + "," + getNumberM( String.valueOf(n - 200) );
        }

        String val = numbersM.get(n);

        if (val != null && val.length() > 0) {
            return val;
        } else {
            return String.valueOf(n) + ".wav";
        }
    }
    public String getNumberF(String number) {
        Integer n = Integer.parseInt(number);
        if (n > 299) {
            n = 299;
        }

        if (n > 100 && n < 200) {
            return this.number100andF + "," + getNumberF( String.valueOf(n - 100) );
        } else if (n > 200 && n < 300) {
            return this.number200andF + "," + getNumberF( String.valueOf(n - 200) );
        }

        String val = numbersF.get(n);
        if (val != null && val.length() > 0) {
            return val;
        } else {
            return getNumberM(number);
        }
    }

    public ConfigIVR(Properties p) {
        this.success = p.getProperty("success");
        this.callingAgain = p.getProperty("callingAgain");
        this.invalidNumber = p.getProperty("invalidNumber");
        this.invalidTime = p.getProperty("invalidTime");
        this.initRequired = p.getProperty("initRequired");
        this.initOptional = p.getProperty("initOptional");
        this.validDigit = p.getProperty("validDigit");
        this.invalidDigit = p.getProperty("invalidDigit");
        this.sentenceChanged = p.getProperty("sentenceChanged");
        this.resetPhoneNumber = p.getProperty("resetPhoneNumber");
        this.invalidDDDStart = p.getProperty("invalidDDDStart");
        this.invalidNumberStart = p.getProperty("invalidNumberStart");
        this.invalidConfirmationChar = p.getProperty("invalidConfirmationChar");
        this.dddComplete = p.getProperty("dddComplete");
        this.askingConfirmation = p.getProperty("askingConfirmation");
        this.numberCompleteSuccess = p.getProperty("numberCompleteSuccess");
        this.numberCompleteFail = p.getProperty("numberCompleteFail");
        this.askingDDD = p.getProperty("askingDDD");
        this.askingNumber = p.getProperty("askingNumber");
        this.keyPadPressed = p.getProperty("keyPadPressed");
        this.digitPound = p.getProperty("digitPound");
        this.digitStar = p.getProperty("digitStar");
        this.hour = p.getProperty("hour");
        this.hours = p.getProperty("hours");
        this.minute = p.getProperty("minute");
        this.minutes = p.getProperty("minutes");
        this.second = p.getProperty("second");
        this.seconds = p.getProperty("seconds");
        this.and = p.getProperty("and");
        this.number100andM = p.getProperty("Number_100and_M");
        this.number200andM = p.getProperty("Number_200and_M");
        this.number100andF = p.getProperty("Number_100and_F");
        this.number200andF = p.getProperty("Number_200and_F");

        this.estimatedTimeIs = p.getProperty("estimatedTimeIs");
        for (int i = 0; i < 299; i++) {
            numbersM.put(i, p.getProperty( "Number_" + String.valueOf(i) + "_M" ) );
            numbersF.put(i, p.getProperty( "Number_" + String.valueOf(i) + "_F" ) );
        }
    }

    public ConfigIVR(String success, String callingAgain, String invalidNumber, String invalidTime, String initRequired, String initOptional, String validDigit, String invalidDigit, String sentenceChanged, String resetPhoneNumber, String invalidDDDStart, String invalidNumberStart, String invalidConfirmationChar, String dddComplete, String askingConfirmation, String numberCompleteSuccess, String numberCompleteFail, String askingDDD, String askingNumber, String keyPadPressed, String digitPound, String digitStar, String hour, String hours, String minute, String minutes, String second, String seconds, String and, String estimatedTimeIs, String number100andM, String number200andM, String number100andF, String number200andF) {
        this.success = success;
        this.callingAgain = callingAgain;
        this.invalidNumber = invalidNumber;
        this.invalidTime = invalidTime;
        this.initRequired = initRequired;
        this.initOptional = initOptional;
        this.validDigit = validDigit;
        this.invalidDigit = invalidDigit;
        this.sentenceChanged = sentenceChanged;
        this.resetPhoneNumber = resetPhoneNumber;
        this.invalidDDDStart = invalidDDDStart;
        this.invalidNumberStart = invalidNumberStart;
        this.invalidConfirmationChar = invalidConfirmationChar;
        this.dddComplete = dddComplete;
        this.askingConfirmation = askingConfirmation;
        this.numberCompleteSuccess = numberCompleteSuccess;
        this.numberCompleteFail = numberCompleteFail;
        this.askingDDD = askingDDD;
        this.askingNumber = askingNumber;
        this.keyPadPressed = keyPadPressed;
        this.digitPound = digitPound;
        this.digitStar = digitStar;
        this.hour = hour;
        this.hours = hours;
        this.minute = minute;
        this.minutes = minutes;
        this.second = second;
        this.seconds = seconds;
        this.and = and;
        this.estimatedTimeIs = estimatedTimeIs;
        this.number100andM = number100andM;
        this.number200andM = number200andM;
        this.number100andF = number100andF;
        this.number200andF = number200andF;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getCallingAgain() {
        return callingAgain;
    }

    public void setCallingAgain(String callingAgain) {
        this.callingAgain = callingAgain;
    }

    public String getInvalidNumber() {
        return invalidNumber;
    }

    public void setInvalidNumber(String invalidNumber) {
        this.invalidNumber = invalidNumber;
    }

    public String getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(String invalidTime) {
        this.invalidTime = invalidTime;
    }

    public String getAskingConfirmation() {
        return askingConfirmation;
    }

    public void setAskingConfirmation(String askingConfirmation) {
        this.askingConfirmation = askingConfirmation;
    }

    public String getAskingDDD() {
        return askingDDD;
    }

    public void setAskingDDD(String askingDDD) {
        this.askingDDD = askingDDD;
    }

    public String getAskingNumber() {
        return askingNumber;
    }

    public void setAskingNumber(String askingNumber) {
        this.askingNumber = askingNumber;
    }

    public String getDDDComplete() {
        return dddComplete;
    }

    public void setDDDComplete(String dddComplete) {
        this.dddComplete = dddComplete;
    }

    public String getInitRequired() {
        return initRequired;
    }

    public void setInitRequired(String initRequired) {
        this.initRequired = initRequired;
    }

    public String getInitOptional() {
        return initOptional;
    }

    public void setInitOptional(String initOptional) {
        this.initRequired = initOptional;
    }

    public String getInvalidConfirmationChar() {
        return invalidConfirmationChar;
    }

    public void setInvalidConfirmationChar(String invalidConfirmationChar) {
        this.invalidConfirmationChar = invalidConfirmationChar;
    }

    public String getInvalidDDDStart() {
        return invalidDDDStart;
    }

    public void setInvalidDDDStart(String invalidDDDStart) {
        this.invalidDDDStart = invalidDDDStart;
    }

    public String getInvalidDigit() {
        return invalidDigit;
    }

    public void setInvalidDigit(String invalidDigit) {
        this.invalidDigit = invalidDigit;
    }

    public String getInvalidNumberStart() {
        return invalidNumberStart;
    }

    public void setInvalidNumberStart(String invalidNumberStart) {
        this.invalidNumberStart = invalidNumberStart;
    }

    public String getKeyPadPressed() {
        return keyPadPressed;
    }

    public void setKeyPadPressed(String keyPadPressed) {
        this.keyPadPressed = keyPadPressed;
    }

    public String getNumberCompleteFail() {
        return numberCompleteFail;
    }

    public void setNumberCompleteFail(String numberCompleteFail) {
        this.numberCompleteFail = numberCompleteFail;
    }

    public String getNumberCompleteSuccess() {
        return numberCompleteSuccess;
    }

    public void setNumberCompleteSuccess(String numberCompleteSuccess) {
        this.numberCompleteSuccess = numberCompleteSuccess;
    }

    public String getResetPhoneNumber() {
        return resetPhoneNumber;
    }

    public void setResetPhoneNumber(String resetPhoneNumber) {
        this.resetPhoneNumber = resetPhoneNumber;
    }

    public String getSentenceChanged() {
        return sentenceChanged;
    }

    public void setSentenceChanged(String sentenceChanged) {
        this.sentenceChanged = sentenceChanged;
    }

    public String getValidDigit() {
        return validDigit;
    }

    public void setValidDigit(String validDigit) {
        this.validDigit = validDigit;
    }

    public String getDigitPound() {
        return digitPound;
    }

    public void setDigitPound(String digitPound) {
        this.digitPound = digitPound;
    }

    public String getDigitStar() {
        return digitStar;
    }

    public void setDigitStar(String digitStar) {
        this.digitStar = digitStar;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }

    public String getAnd() {
        return and;
    }

    public void setAnd(String and) {
        this.and = and;
    }

    public String getNumber100andM() {
        return number100andM;
    }

    public void setNumber100andM(String number100andM) {
        this.number100andM = number100andM;
    }

    public String getNumber200andM() {
        return number200andM;
    }

    public void setNumber200andM(String number200andM) {
        this.number200andM = number200andM;
    }

    public String getNumber100andF() {
        return number100andF;
    }

    public void setNumber100andF(String number100andF) {
        this.number100andF = number100andF;
    }

    public String getNumber200andF() {
        return number200andF;
    }

    public void setNumber200andF(String number200andF) {
        this.number200andF = number200andF;
    }

    public String getEstimatedTimeIs() {
        return estimatedTimeIs;
    }

    public void setEstimatedTimeIs(String estimatedTimeIs) {
        this.estimatedTimeIs = estimatedTimeIs;
    }
}