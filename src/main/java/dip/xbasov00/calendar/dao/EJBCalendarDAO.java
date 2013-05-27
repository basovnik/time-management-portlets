/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : EJBCalendarDAO.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.dao;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.extensions.CalendarBuilder;
import dip.xbasov00.calendar.domain.CalDAVRemoteCalendar;
import dip.xbasov00.calendar.domain.Calendar;
import dip.xbasov00.calendar.domain.CalendarComponent;
import dip.xbasov00.calendar.domain.Event;
import dip.xbasov00.calendar.domain.ICalRemoteCalendar;
import dip.xbasov00.calendar.domain.RemoteCalendar;
import dip.xbasov00.calendar.domain.ToDo;
import dip.xbasov00.calendar.util.CalDAVConnector;
import dip.xbasov00.calendar.util.CalendarException;
import dip.xbasov00.calendar.util.ICal4JConnector;
import dip.xbasov00.util.Resources;

/**
 * Class EJBCalendarDAO represents class that manipulates with calendar entities.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 */

@Stateless
public class EJBCalendarDAO implements CalendarDAO {

    @Inject
    private EntityManager em;

    @Inject
    private CalendarComponentDAO calCompDao;

    
    @Inject
    private EventDAO eventDao;
    
    @Inject
    private Logger logger;



    /**
     * {@inheritDoc}
     */
    @Override
    public List<Calendar> getAll() {
        TypedQuery<Calendar> query = em.createQuery("SELECT t FROM Calendar t", Calendar.class);
        return query.getResultList();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public List<Calendar> getlAllInPortletInstance(String portletInstanceId) {
        TypedQuery<Calendar> query = em.createQuery("SELECT t FROM Calendar t WHERE t.portletInstanceId = :portletInstanceId", Calendar.class)
                .setParameter("portletInstanceId", portletInstanceId);
        return query.getResultList();
    }



    /**
     * Get all remote calendars
     * @return All remote calendars.
     */
    public List<RemoteCalendar> getAllRemote() {
        TypedQuery<RemoteCalendar> query = em.createQuery("SELECT t FROM RemoteCalendar t", RemoteCalendar.class);
        return query.getResultList();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public Calendar getCalendar(int id) {
        return em.find(Calendar.class, id);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public Calendar getCalendarFromPrivateId(String privateId) {
        TypedQuery<Calendar> query = em.createQuery("SELECT t FROM Calendar t WHERE t.privateId = :privateId", Calendar.class)
                .setParameter("privateId", privateId);
        List<Calendar> calendars =  query.getResultList();
        return calendars.isEmpty() ? null : calendars.get(0);
    }



    /**
     * {@inheritDoc}
     * @throws CalendarException 
     */
    @Override
    public void createCalendar(Calendar calendar) throws CalendarException {
        
        em.persist(calendar);
        
        // Try to synchronize remote calendar.
        if (calendar instanceof RemoteCalendar) { 
            try {
                synchronizeRemoteCalendar((RemoteCalendar) calendar);
            } catch (Exception e) {
                deleteCalendar(calendar);
                e.printStackTrace();
                throw new CalendarException(e.getMessage());
            }
        }
        
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void editCalendar(Calendar calendar) {
        em.merge(calendar);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteCalendar(Calendar calendar) {
        if (!em.contains(calendar)) {
            calendar = em.merge(calendar);
        }

        em.remove(calendar);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void synchronizeRemoteCalendar(RemoteCalendar calendar) throws CalendarException {

        RemoteCalendar remoteCalendar = null;
        if (calendar instanceof ICalRemoteCalendar) {
            remoteCalendar = getICalRemoteCalendar(calendar.getUrl());
        }
        else if (calendar instanceof CalDAVRemoteCalendar) {
            remoteCalendar = getCalDAVRemoteCalendar(calendar.getUrl(),
                ((CalDAVRemoteCalendar) calendar).getUsername(),  // User name
                ((CalDAVRemoteCalendar) calendar).getPassword()   // Password
            );
        }
        else {
            throw new IllegalArgumentException("Calendar object is not instance of any RemoteCalendar type.");
        }

        if (remoteCalendar == null) {
            throw new CalendarException("Getting of remote calendar \"" + calendar.getName() + "\" failed!");
        }
        
        // Synchronize local calendar representation.
        removeExtraCalendarComponents(calendar, remoteCalendar);  // Remove extra calendars
        
        mergeCalendars(calendar, remoteCalendar);   // Merge calendars

        logger.log(Level.INFO, "Synchronization of Calendar '" + calendar.getName() + "' was successful!");

    }



    /**
     * Returns remote calendar from specified URL with iCalendar file.
     * @param calendar ICalRemoteCalendar
     * @return RemoteCalendar
     * @throws CalendarException
     */
    private RemoteCalendar getICalRemoteCalendar(String url) throws CalendarException {
        try {
            // Get input stream.
            URL urlObj = new URL(url);
            InputStream is = urlObj.openStream();

            // Get remote calendar
            CalendarBuilder builder = new CalendarBuilder();
            net.fortuna.ical4j.model.Calendar vcalendar = builder.build(is);
            return new ICalRemoteCalendar(vcalendar);
            
        } catch (IllegalArgumentException e) { 
            throw new CalendarException(Resources.getBundle().getString("BadURL"));
        } catch (MalformedURLException e) {
            throw new CalendarException(Resources.getBundle().getString("BadURL"));
        } catch (IOException e) {
            throw new CalendarException(Resources.getBundle().getString("ReadingURLError"));
        } catch (ParserException e) {
            throw new CalendarException(Resources.getBundle().getString("ICalParseError"));
        }
    }



    /**
     * Returns remote calendar from CalDAV server specified by URL.
     * 
     * @param url URL to remote calendar.
     * @param user User name.
     * @param password User password.
     * @return RemoteCalendar
     * @throws CalendarException
     */
    private RemoteCalendar getCalDAVRemoteCalendar(String url, String user, String password) throws CalendarException {
        CalDAVConnector connector = new ICal4JConnector(url);

        if (connector.connect(user, password) == true) { // Connect
            RemoteCalendar remoteCalendar = connector.getCalendar();
            connector.disconnect(); // Disconnect
            return remoteCalendar;
        }
        return null;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void mergeCalendars(Calendar calendarDest, Calendar calendarSrc) {
        for(CalendarComponent ccSrc : calendarSrc.getCalendarComponents()) {

            // Check if calendars contains an event with the same vevent_uid
            CalendarComponent ccDest = calCompDao.getCalendarComponent(calendarDest, ccSrc.getICalCompUID());
            
            if (ccDest != null) { // Event with the same vevent_uid already exists
                
                if (ccSrc instanceof Event && ccDest instanceof Event) {
                    Event e = (Event) ccDest;
                    eventDao.deleteRecurRule(e);
                    
                    e.updateBy((Event)ccSrc);
                    calCompDao.editCalendarComponent(e);
                }
                else if (ccSrc instanceof ToDo && ccDest instanceof ToDo) {
                    ToDo t = (ToDo) ccDest;
                    t.updateBy((ToDo) ccSrc);
                }
                
                
            }
            else { // Calendar component is not in calendarDest. We have to add it!
                ccSrc.setCalendar(calendarDest);
                calCompDao.createCalendarComponent(ccSrc);
            }
        }
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public int removeExtraCalendarComponents(Calendar calendarDest, Calendar calendarSrc) {
        // Creates new list but with the original references to events!
        // We cannot delete item from list while iterating through it!
        List<CalendarComponent> calendarComponentsTmp = new ArrayList<CalendarComponent>(calendarDest.getCalendarComponents());
        int count = 0;
        for(CalendarComponent cc1 : calendarComponentsTmp) {
            boolean uidExists = false;
            for(CalendarComponent cc2 : calendarSrc.getCalendarComponents()) {
                if (cc1.getICalCompUID().equals(cc2.getICalCompUID())) {
                    uidExists = true;
                    break;
                }
            }
            if (!uidExists) {
                calCompDao.deleteCalendarComponent(cc1);
                count++;
            }
        }
        return count;
    }



    /**
     * Synchronize all remote calendars.
     * This method will be invoked every 5 minutes.
     */
    //@Schedule(second = "*", minute = "*/5", hour = "*")
    public void synchronyzeAllRemoteCalendars() {

        for(RemoteCalendar remoteCalendar : getAllRemote()) {
            try {
                synchronizeRemoteCalendar(remoteCalendar);

            } catch (CalendarException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }

        }
    }





}
