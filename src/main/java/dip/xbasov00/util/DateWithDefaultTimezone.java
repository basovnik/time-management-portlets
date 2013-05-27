/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : DateWithDefaultTimezone.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.util;

import net.fortuna.ical4j.util.Dates;


/**
 * Class net.fortuna.ical4j.model.Date automatically sets TimeZone to UTC_TIMEZONE.
 * Interesting is that for net.fortuna.ical4j.model.DateTime it sets timezone to defaultTimezone.
 * Too overcome this problem following class was implemented!
 * 
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 */
public class DateWithDefaultTimezone extends net.fortuna.ical4j.model.Date {
    private static final long serialVersionUID = -4528252136869678828L;

    public DateWithDefaultTimezone(final long time) {
        super(time, Dates.PRECISION_DAY, java.util.TimeZone.getDefault());
    }
}
