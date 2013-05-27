/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : CalendarHelper.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.util;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Singleton;

import org.apache.commons.lang.time.DateUtils;

/**
 * Class CalendarHelper is used for some often used calendar operations.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Singleton
public class CalendarHelper {


    /**
     * Trim all seconds from last time "00:00:00:000".
     *
     * @param cal
     * @return Calendar
     */
    public static java.util.Calendar trimDaySeconds(java.util.Calendar cal) {
        cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
        cal.set(java.util.Calendar.MINUTE, 0);
        cal.set(java.util.Calendar.SECOND, 0);
        cal.set(java.util.Calendar.MILLISECOND, 0);
        return cal;
    }



    /**
     * Trim all seconds from last time "00:00:00:000".
     *
     * @param date
     * @return Calendar
     */
    public static Date trimDaySeconds(Date date) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        CalendarHelper.trimDaySeconds(cal);
        return cal.getTime();
    }



    /**
     * Sets the same date-time at "dest" date from "src" date.
     *
     * @param src Source date.
     * @param dest Destination date.
     * @return
     */
    public static Date setEqualDayTime(Date src, Date dest) {
        Date srcMidnight = CalendarHelper.trimDaySeconds(src);
        long diff = src.getTime() - srcMidnight.getTime();
        Date dstMidnight = CalendarHelper.trimDaySeconds(dest);
        return new Date(dstMidnight.getTime() + diff);
    }



    /**
     * Returns the number representing how many the same week-days were in month from specified date.
     *
     * @param date
     * @return
     */
    public static int getNthWeekdayOccurence(Date date) {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(date);
        int dateNum = cal.get(java.util.Calendar.DATE);
        int nthOccurence = 0;
        while(dateNum > 0) {
            nthOccurence++;
            dateNum -= 7;
        }
        return nthOccurence;
    }



    /**
     * Checks if the date is nth date from today.
     *
     * @param date Date to check.
     * @param n As positive as negative value.
     * @return
     */
    public static boolean isNthDayFromToday(Date date, int n) {
        if (date == null) {
            return false;
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(new Date());
        cal2.add(java.util.Calendar.DATE, n);

        return DateUtils.isSameDay(cal1.getTime(), cal2.getTime());
    }
}
