/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Test;

import DeveloperCity.ContactBack.DomainModel.ConfigIVR;
import DeveloperCity.ContactBack.DomainModel.Utils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author lbarbosa
 */
public class UtilsTester {
    @Test
    public void TelephonyNumberAdjustmentMethod() {
        /*
        Assert.assertEquals(Utils.TelephonyNumberAdjustment("1198761234"), "1198761234");
        Assert.assertEquals(Utils.TelephonyNumberAdjustment("01198761234"), "1198761234");
        Assert.assertEquals(Utils.TelephonyNumberAdjustment("98761234"), "1198761234");
        Assert.assertEquals(Utils.TelephonyNumberAdjustment("198761234"), "1198761234");
        Assert.assertEquals(Utils.TelephonyNumberAdjustment("0000098761234"), "1198761234");
        Assert.assertEquals(Utils.TelephonyNumberAdjustment("00001198761234"), "1198761234");
        Assert.assertEquals(Utils.TelephonyNumberAdjustment("00011198761234"), "1198761234");
        Assert.assertEquals(Utils.TelephonyNumberAdjustment("00000198761234"), "1198761234");
        Assert.assertEquals(Utils.TelephonyNumberAdjustment("11111198761234"), "1198761234");
        Assert.assertEquals(Utils.TelephonyNumberAdjustment("8761234"), "8761234");
        Assert.assertEquals(Utils.TelephonyNumberAdjustment("1234"), "1234");
        Assert.assertEquals(Utils.TelephonyNumberAdjustment("7598761234"), "7598761234");
        Assert.assertEquals(Utils.TelephonyNumberAdjustment("07598761234"), "7598761234");
        Assert.assertEquals(Utils.TelephonyNumberAdjustment("398761234"), "1198761234");
        Assert.assertEquals(Utils.TelephonyNumberAdjustment("70000098761234"), "1198761234");
        Assert.assertEquals(Utils.TelephonyNumberAdjustment("00007598761234"), "7598761234");
        Assert.assertEquals(Utils.TelephonyNumberAdjustment("00017598761234"), "7598761234");
        Assert.assertEquals(Utils.TelephonyNumberAdjustment("700000198761234"), "1198761234");
        Assert.assertEquals(Utils.TelephonyNumberAdjustment("777777598761234"), "7598761234");
        */

    }
    @Test
    public void DateSubtration() throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date sooner = df.parse("2010-11-29 19:46:23");
        Date later = df.parse("2010-11-10 21:00:49");
        
        String result = Utils.TimeDuration( (sooner.getTime() - later.getTime()) / 1000 );
        Assert.assertEquals(result, "18d 22h 45' 34\"");
    }
    @Test
    public void FullTimeString() {
        ConfigIVR configIVR = new ConfigIVR();
        String a = configIVR.getFullTimeFiles(19, false);
        System.out.println(a);
    }
}
