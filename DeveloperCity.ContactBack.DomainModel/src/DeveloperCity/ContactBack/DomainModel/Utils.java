/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package DeveloperCity.ContactBack.DomainModel;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;

/**
 *
 * @author lbarbosa
 */
public class Utils {

    @Deprecated
    private static String TelephonyNumberAdjustmentObsolete(String rawNumber) {
        if (rawNumber == null) {
            return null;
        }

        String bina = rawNumber.replaceAll("[^0-9]", "");

        if (bina.length() < 8) {
            return bina;
        }

        if (bina.length() > 10) {
            bina = bina.substring(bina.length() - 10);
        }

        if (bina.startsWith("0")) {
            while (bina.startsWith("0")) {
                bina = bina.substring(1);
            }
        }

        if (bina.length() < 8) {
            return bina;
        }

        if (bina.length() == 8) {
            bina = "11" + bina;
        }

        if (bina.length() == 9) {
            bina = "11" + bina.substring(1);
        }

        return bina;
    }

    public static long[] TimeSpan(double durationTime) {
        double duration = durationTime;
        long days = 0;
        long hours = 0;
        long minutes = 0;
        long seconds = 0;

        if (duration >= 24 * 60 * 60) {
            days = (long) (duration / (24 * 60 * 60));
            duration -= (days * 24 * 60 * 60);
        }
        if (duration >= 60 * 60) {
            hours = (long) (duration / (60 * 60));
            duration -= (hours * 60 * 60);
        }
        if (duration >= 60) {
            minutes = (long) (duration / 60);
            duration -= (minutes * 60);
        }
        seconds = (long)duration;
        long[] retValue = new long[4];
        retValue[0] = days;
        retValue[1] = hours;
        retValue[2] = minutes;
        retValue[3] = seconds;
        return retValue;
    }
    public static String TimeDuration(double durationTime) {
        long[] timespan = TimeSpan(durationTime);
        long days = timespan[0];
        long hours = timespan[1];
        long minutes = timespan[2];
        long seconds = timespan[3];

        NumberFormat formatter = new DecimalFormat("00");

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days);
            sb.append("d ");
        }
        if (days > 0 || hours > 0) {
            sb.append(formatter.format(hours));
            sb.append("h ");
        }
        if (days > 0 || hours > 0 || minutes > 0) {
            sb.append(formatter.format(minutes));
            sb.append("' ");
        }
        sb.append(formatter.format(seconds));
        sb.append("\"");
        return sb.toString();
    }

    public static boolean isToday(java.util.Date fullDate) {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0, 0, 0);
        java.util.Date today = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        java.util.Date tomorrow = cal.getTime();

        return fullDate.after(today) && fullDate.before(tomorrow);
    }
}
