/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package DeveloperCity.ContactBack.Test;

import DeveloperCity.ContactBack.DomainModel.Utils;
import java.util.Calendar;
import java.util.Date;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author lbarbosa
 */
public class WorkTimeTester {
    @Test
    public void IsTodayMethod() {
        Assert.assertTrue(Utils.isToday(new Date()));
        Assert.assertTrue(Utils.isToday(Calendar.getInstance().getTime()));
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Assert.assertFalse(Utils.isToday(cal.getTime()));
        cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        Assert.assertFalse(Utils.isToday(cal.getTime()));
        cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0,0,1);
        Assert.assertTrue(Utils.isToday(cal.getTime()));
        cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE), 0,0,0);
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Assert.assertFalse(Utils.isToday(cal.getTime()));
        cal.add(Calendar.MILLISECOND, -1);
        Assert.assertTrue(Utils.isToday(cal.getTime()));
    }

    @Test
    public void TimeDuration() {
        int days = 3, hours = 11, minutes = 0, seconds = 1;
        long duration = seconds + (minutes * 60) + (hours * 60 * 60) + (days * 60 * 60 * 24);
        String response = Utils.TimeDuration(duration);
    }
}
