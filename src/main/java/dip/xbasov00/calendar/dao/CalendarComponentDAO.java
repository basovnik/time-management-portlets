/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : CalendarComponentDAO.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */
package dip.xbasov00.calendar.dao;

import java.util.Date;
import java.util.List;

import dip.xbasov00.calendar.domain.Calendar;
import dip.xbasov00.calendar.domain.CalendarComponent;

/**
 * Interface CalendarComponentDAO declares prototypes of methods to manipulate with calendar components.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
public interface CalendarComponentDAO {


    /**
     * Get all calendarComponents
     * @return All calendarComponents.
     */
    List<CalendarComponent> getAll();



    /**
     * Get calendarComponents from specified calendar.
     * @param calendar Calendar
     * @return List of events.
     */
    List<CalendarComponent> getCalendarComponents(Calendar calendar);


    
    
    /**
     * Get calendarComponents from specified calendar in specified date.
     * @param calendar Calendar
     * @param date date
     * @return List of events.
     */
    List<CalendarComponent> getCalendarComponents(Calendar calendar, Date date);
    
    
    
    
    /**
     * Get calendarComponent with specified id.
     * @param id ID of calendarComponent.
     * @return CalendarComponent with specified ID.
     */
    CalendarComponent getCalendarComponent(int id);


    /**
     * Get event from specified calendar with specified calComp uid.
     * @param calendar Calendar
     * @param calCompUid calComp uid.
     * @return Event.
     */
    CalendarComponent getCalendarComponent(Calendar calendar, String calCompUid);


    /**
     * Create CalendarComponent.
     * @param CalendarComponent CalendarComponent.
     */
    void createCalendarComponent(CalendarComponent event);


    /**
     * Edit CalendarComponent.
     * @param CalendarComponent CalendarComponent.
     */
    void editCalendarComponent(CalendarComponent event);


    /**
     * Delete CalendarComponent.
     * @param CalendarComponent
     */
    void deleteCalendarComponent(CalendarComponent event);



}
