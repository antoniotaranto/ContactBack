package DeveloperCity.ContactBack.DomainModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author lbarbosa
 */
public class HolidayList extends ArrayList<Holiday> implements Serializable {
    private static final long serialVersionUID = 1L;
    public HolidayList() { }
    public HolidayList(Collection<Holiday> collection) { super(collection); }
    public HolidayList(int initialCapacity) { super(initialCapacity); }

    public boolean isHoliday(Date date) {
        if (date == null) { return false; }
        Calendar day = Calendar.getInstance();
        day.setTime(date);
        day.set(Calendar.MILLISECOND, 0);
        day.set(Calendar.SECOND, 0);
        day.set(Calendar.MINUTE, 0);
        day.set(Calendar.HOUR_OF_DAY, 0);
        for(Holiday h : this) {
            if (h.getDay().getTime() == day.getTime().getTime()) {
                return true;
            }
        }
        return false;
    }
}
