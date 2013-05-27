/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : EJBEventDAO.java
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

import dip.xbasov00.calendar.domain.Calendar;
import dip.xbasov00.calendar.domain.CalendarComponent;
import dip.xbasov00.calendar.domain.Event;
import dip.xbasov00.calendar.domain.RecurRule;
import dip.xbasov00.calendar.util.CalendarException;
import dip.xbasov00.util.CalendarHelper;

/**
 *
 * Class EJBEventDAO represents class that manipulates with event entities.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */

@Stateless
public class EJBEventDAO implements EventDAO {

    
    @Inject
    private EntityManager em;


    @Inject
    private Logger logger;
    
    
    @Inject
    private CalendarComponentDAO calCompDao;

    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Event> getEvents(Calendar calendar, Date date) {

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

        Set<Event> events = new HashSet<Event>(query.getResultList());

        // Add recurring events
        List<Event> recurEvents = new ArrayList<Event>();
        for (Event event : getEvents(calendar)) {
            try {
                recurEvents.addAll(event.getRecurEvents(calStart.getTime()));
            } catch (CalendarException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
        events.addAll(recurEvents);

        return new ArrayList<Event>(events);
    }




    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteRecurRule(Event event) {
        RecurRule rule = event.getRecurRule();
        if (rule != null) {
            if (!em.contains(rule)) {
                rule = em.merge(rule);
            }
            em.remove(rule);
        }
        event.setRecurRule(null);
        em.merge(event);

    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Event> getEvents(Calendar calendar) {
        TypedQuery<Event> query = em.createQuery("SELECT t FROM Event t WHERE " +
                "t.calendar = :calendar", Event.class)
                .setParameter("calendar", calendar);

        return query.getResultList();
    }




    /**
     * {@inheritDoc}
     */
    @Override
    public Event getEvent(int id) {
        return em.find(Event.class, id);
    }




    /**
     * {@inheritDoc}
     */
    @Override
    public Event getEvent(Calendar calendar, String calCompUid) {
        CalendarComponent cc = calCompDao.getCalendarComponent(calendar, calCompUid);
        return (cc instanceof Event) ? (Event) cc : null;
    }




}
