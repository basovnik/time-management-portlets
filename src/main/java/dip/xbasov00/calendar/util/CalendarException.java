/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : CalendarException.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.util;

import dip.xbasov00.calendar.domain.Calendar;


/**
 * Class CalendarException represents own exception.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
public class CalendarException extends Exception {

    private static final long serialVersionUID = 5954527037720687290L;

    private Calendar calendar;

    public CalendarException(String message) {
        super(message);
    }

    public CalendarException(Calendar calendar, String message) {
        super(message);
    }

    public Calendar getCalendar() {
        return calendar;
    }

    @Override
    public String getMessage() {
        if (calendar != null) {
            return "CalendarException [calendar-id= '" + calendar.getId() + "']: " + super.getMessage();
        }
        else return super.getMessage();
    }


}
