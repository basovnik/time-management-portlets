/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : CalDAVConnector.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.util;

import dip.xbasov00.calendar.domain.CalendarComponent;
import dip.xbasov00.calendar.domain.RemoteCalendar;


/**
 *
 * Class CalDAVConnector is adapter for different libraries to work with.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
public interface CalDAVConnector {

    /**
     * Connects to remote CalDAV server.
     * @param user User name
     * @param password User password
     * @return Boolean value
     * @throws CalendarException
     */
    public boolean connect(String user, String password) throws CalendarException;

    /**
     * Disconnects from remote server.
     */
    public void disconnect();

    /**
     * Checks if the connection was created.
     * @return Boolean value.
     */
    public boolean isConnected();

    /**
     * Returns calendar containing all events.
     * Path to this calendar must be specified in CalDAVConnector constructor.
     * @return RemoteCalendar
     * @throws CalendarException
     */
    public RemoteCalendar getCalendar() throws CalendarException;

    /**
     * Returns CalendarComponent specified by its uid.
     * @param uid CalendarComponent's identifier
     * @return CalendarComponent
     * @throws CalendarException
     */
    public CalendarComponent getCalendarComponent(String uid) throws CalendarException;

    /**
     * Adds new calendarComponent to "selected" calendar on remote CalDAV server.
     * @param event CalendarComponent
     * @return Boolean value
     * @throws CalendarException
     */
    public boolean addCalendarComponent(CalendarComponent calendarComponent) throws CalendarException;

    /**
     * Deletes event from "selected" calendar on remote CalDAV server.
     * @param uid Event's identifier.
     * @return Boolean value
     * @throws CalendarException
     */
    public boolean deleteCalendarComponent(String uid) throws CalendarException;


}
