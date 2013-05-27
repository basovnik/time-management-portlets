/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : EJBCalendarComponentDAO.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import net.fortuna.ical4j.util.SimpleHostInfo;
import net.fortuna.ical4j.util.UidGenerator;
import dip.xbasov00.calendar.domain.CalDAVRemoteCalendar;
import dip.xbasov00.calendar.domain.Calendar;
import dip.xbasov00.calendar.domain.CalendarComponent;
import dip.xbasov00.calendar.domain.Event;
import dip.xbasov00.calendar.domain.ToDo;
import dip.xbasov00.calendar.util.CalDAVConnector;
import dip.xbasov00.calendar.util.CalendarException;
import dip.xbasov00.calendar.util.CalendarSettingsSingleton;
import dip.xbasov00.calendar.util.ICal4JConnector;
import dip.xbasov00.util.CalendarHelper;

/**
 *
 * Class EJBEventDAO represents class that manipulates with calendar components.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */

@Stateless
public class EJBCalendarComponentDAO implements CalendarComponentDAO {

    @Inject
    protected EntityManager em;


    @Inject
    protected Logger logger;



    /**
     * {@inheritDoc}
     */
    @Override
    public List<CalendarComponent> getCalendarComponents(Calendar calendar, Date date) {

        java.util.Calendar calStart = java.util.Calendar.getInstance();
        calStart.setTime(date);
        CalendarHelper.trimDaySeconds(calStart);

        java.util.Calendar calEnd = java.util.Calendar.getInstance();
        calEnd.setTime(calStart.getTime());
        calEnd.add(java.util.Calendar.DAY_OF_MONTH, 1);
        calEnd.add(java.util.Calendar.MILLISECOND, -1);


        TypedQuery<Event> query = em.createQuery("SELECT t FROM Event t WHERE " +
                "t.calendar = :calendar" +
                " AND (" +
                " (t.datetimeFrom >= :dateStart AND t.datetimeTo <= :dateEnd)" + // Inside the date.
                " OR (t.datetimeFrom < :dateStart AND t.datetimeTo > :dateStart)" + // Pass the start of the date.
                " OR (t.datetimeFrom < :dateEnd AND t.datetimeTo > :dateEnd)" + // Pass the end of the date.
                ")", Event.class)
                .setParameter("calendar", calendar)
                .setParameter("dateStart", calStart.getTime())
                .setParameter("dateEnd", calEnd.getTime());

        Set<CalendarComponent> calComps = new HashSet<CalendarComponent>(query.getResultList());

        // Add recurring events
        List<CalendarComponent> recurEvents = new ArrayList<CalendarComponent>();
        for (CalendarComponent cc : getCalendarComponents(calendar)) {
            if (cc instanceof Event) {
                try {
                    recurEvents.addAll(((Event) cc).getRecurEvents(calStart.getTime()));
                } catch (CalendarException e) {
                    logger.log(Level.SEVERE, e.getMessage());
                }
            }
        }
        calComps.addAll(recurEvents);
        
        
        TypedQuery<ToDo> queryToDo = em.createQuery("SELECT t FROM ToDo t WHERE " +
                "t.calendar = :calendar" +
                " AND (" +
                " (t.datetimeScheduled >= :dateStart AND t.datetimeScheduled <= :dateEnd)" + // Inside the date.
                ")", ToDo.class)
                .setParameter("calendar", calendar)
                .setParameter("dateStart", calStart.getTime())
                .setParameter("dateEnd", calEnd.getTime());
        calComps.addAll(queryToDo.getResultList());

        return new ArrayList<CalendarComponent>(calComps);
    }
    
    
    
    
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<CalendarComponent> getAll() {
        TypedQuery<CalendarComponent> query = em.createQuery("SELECT t FROM CalendarComponent t", CalendarComponent.class);
        return query.getResultList();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public List<CalendarComponent> getCalendarComponents(Calendar calendar) {
        TypedQuery<CalendarComponent> query = em.createQuery("SELECT t FROM CalendarComponent t WHERE " +
                "t.calendar = :calendar", CalendarComponent.class)
                .setParameter("calendar", calendar);

        return query.getResultList();
    }



    


    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarComponent getCalendarComponent(int id) {
        return em.find(CalendarComponent.class, id);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void createCalendarComponent(CalendarComponent calComp) {
        em.persist(calComp);

        // Generate iCalendar ICalCompUID uid if it is not set.
        if (calComp.getICalCompUID() == null) {
            UidGenerator ug = new UidGenerator(new SimpleHostInfo(CalendarSettingsSingleton.hostName), Integer.toString(calComp.getId()));
            calComp.setICalCompUID(ug.generateUid().getValue());
            em.merge(calComp);
        }
        
        // Insert event to remote calendar.
        if (calComp.getCalendar() instanceof CalDAVRemoteCalendar) {
            saveCalCompToCalDAVRemoteCalendar(calComp);
        }
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void editCalendarComponent(CalendarComponent calComp) {
        CalendarComponent oldCalComp = getCalendarComponent(calComp.getId());
        Calendar oldCalendar = oldCalComp.getCalendar();

        // If event before change contained to CalDAVRemoteCalendar.
        if (oldCalendar instanceof CalDAVRemoteCalendar) {
            // If the calendar did not change just update the remote record.
            if (oldCalendar.sourceEquals(calComp.getCalendar())) {
                saveCalCompToCalDAVRemoteCalendar(calComp);
            }
            else { // Event belongs now to different calendar we have to delete original remote event record.
                deleteCalCompFromCalDAVRemoteCalendar(oldCalComp);

                // Event now belongs to !different! CalDAVCalendar.
                if(calComp.getCalendar() instanceof CalDAVRemoteCalendar) { // Insert
                    saveCalCompToCalDAVRemoteCalendar(calComp);
                }
            }
        }
        else {
            // Event now belongs to CalDAVCalendar.
            if(calComp.getCalendar() instanceof CalDAVRemoteCalendar) { // Insert
                saveCalCompToCalDAVRemoteCalendar(calComp);
            }
        }

        // Update local changes.
        em.merge(calComp);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteCalendarComponent(CalendarComponent calendarComponent) {

        if (!em.contains(calendarComponent)) {
            calendarComponent = em.merge(calendarComponent);
        }
        em.remove(calendarComponent);
        
     // If event contained CalDAVRemoteCalendar we should remove remote event record.
        if (calendarComponent.getCalendar() instanceof CalDAVRemoteCalendar) {
            deleteCalCompFromCalDAVRemoteCalendar(calendarComponent);
        }
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarComponent getCalendarComponent(Calendar calendar, String calCompUid) {
        TypedQuery<CalendarComponent> query = em.createQuery("SELECT t FROM CalendarComponent t WHERE " +
                "t.calendar = :calendar" +
                " AND t.iCalCompUID = :iCalCompUID", CalendarComponent.class)
                .setParameter("calendar", calendar)
                .setParameter("iCalCompUID", calCompUid);


        List<CalendarComponent> calendarComponents= query.getResultList();
        if (calendarComponents.isEmpty()) {
            return null;
        }
        else if(calendarComponents.size() > 1) {
            logger.log(Level.WARNING, "There are " + calendarComponents.size() + " calendar components with the same global UID in calendar \"" + calendar.getName() + "\".");
            return calendarComponents.get(0);
        }
        else {
            return calendarComponents.get(0);
        }
    }



    /**
     * Saves (insert/update) event to remote calendar.
     * @param event
     * @return Boolean value
     */
    public boolean saveCalCompToCalDAVRemoteCalendar(CalendarComponent calComp) {
        if (calComp.getCalendar() == null) {
            return false;
        }
        CalDAVRemoteCalendar remoteCalendar = (CalDAVRemoteCalendar) calComp.getCalendar();
        CalDAVConnector connector = new ICal4JConnector(remoteCalendar.getUrl());
        boolean success = false;

        try {
            if (connector.connect(remoteCalendar.getUsername(), remoteCalendar.getPassword()) == true) { // Connect
                success = connector.addCalendarComponent(calComp);
                connector.disconnect(); // Disconnect
            }
        } catch (CalendarException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return success;
    }



    /**
     * Delete calComp from remote calendar.
     * @param calComp
     * @return Boolean value
     */
    public boolean deleteCalCompFromCalDAVRemoteCalendar(CalendarComponent calComp) {
        CalDAVRemoteCalendar remoteCalendar = (CalDAVRemoteCalendar) calComp.getCalendar();
        CalDAVConnector connector = new ICal4JConnector(remoteCalendar.getUrl());
        boolean success = false;

        try {
            if (connector.connect(remoteCalendar.getUsername(), remoteCalendar.getPassword()) == true) { // Connect
                success = connector.deleteCalendarComponent(calComp.getICalCompUID());
                connector.disconnect(); // Disconnect
            }
        } catch (CalendarException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        return success;
    }




}
