/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : EventDAO.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.dao;

import java.util.Date;
import java.util.List;

import dip.xbasov00.calendar.domain.Calendar;
import dip.xbasov00.calendar.domain.Event;

/**
 * Interface EventDAO
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
public interface EventDAO {


    /**
     * Get events from specified calendar which has intersection with specified date.
     * 
     * @param calendar Calendar
     * @param date Date
     * @return List of events.
     */
    List<Event> getEvents(Calendar calendar, Date date);


    /**
     * Get events from specified calendar.
     * 
     * @param calendar Calendar
     * @return List of events.
     */
    List<Event> getEvents(Calendar calendar);


    /**
     * Get event with specified id.
     * 
     * @param id ID of event.
     * @return Event with specified ID.
     */
    Event getEvent(int id);


    /**
     * Get event from specified calendar with specified vevent UID.
     * 
     * @param calendar Calendar
     * @param veventUid Vevent UID.
     * @return Event.
     */
    Event getEvent(Calendar calendar, String veventUid);



    /**
     * Delete recur rule of this event
     * 
     * @param event
     */
    void deleteRecurRule(Event event);



}
