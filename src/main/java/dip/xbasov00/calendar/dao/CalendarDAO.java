/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : CalendarDAO.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.dao;

import java.util.List;

import dip.xbasov00.calendar.domain.Calendar;
import dip.xbasov00.calendar.domain.RemoteCalendar;
import dip.xbasov00.calendar.util.CalendarException;

/**
 * Interface CalendarDAO declares prototypes of methods to manipulate with calendar objects.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
public interface CalendarDAO {

    /**
     * Get all calendars
     * @return All calendars.
     */
    List<Calendar> getAll();



    /**
     * Get all calendars in specified portlet instance.
     * @param portletInstanceId
     * @return All calendars in specified portlet instance.
     */
    List<Calendar> getlAllInPortletInstance(String portletInstanceId);


    /**
     * Get calendar with specified id.
     * @param id Id of calendar.
     * @return Calendar with specified ID.
     */
    Calendar getCalendar(int id);


    /**
     * Creates persistent calendar. If calendar is of type RemoteCalendar method
     * will to try synchronize calendar. When it fails calendar is deleted.
     * 
     * @param calendar Calendar to persist.
     * @throws CalendarException 
     */
    void createCalendar(Calendar calendar) throws CalendarException;


    /**
     * Edits persistent calendar.
     * 
     * @param calendar Changed calendar to persist.
     */
    void editCalendar(Calendar calendar);


    /**
     * Deletes persistent calendar.
     * 
     * @param calendar Calendar to delete.
     */
    void deleteCalendar(Calendar calendar);


    /**
     * Synchronize remote calendar.
     * 
     * @param calendar
     * @throws CalendarException
     */
    void synchronizeRemoteCalendar(RemoteCalendar calendar) throws CalendarException;



    /**
     * Appends calendarComponents of calendarSrc to calendar calendarDest.
     * 
     * @param calendar Calendar to which new calendarComponents will be joined.
     * @param Calendar to join.
     */
    void mergeCalendars(Calendar calendarDest, Calendar calendarSrc);



    /**
     * Method deletes calendarComponents from calendarDest which are not
     * contained in calendarSrc. Method finds out existence
     * by ical_comp_uid which is global identifier.
     * 
     * @param calendarDest Calendar in which we will delete extra calendarComponents.
     * @param calendarSrc Source calendar.
     * @return Number of deleted calendarComponents.
     */
    public int removeExtraCalendarComponents(Calendar calendarDest, Calendar calendarSrc);



    /**
     * Get calendar from private id or null if not exist.
     * 
     * @param privateId Private id of calendar.
     * @return Calendar
     */
    Calendar getCalendarFromPrivateId(String privateId);

}
