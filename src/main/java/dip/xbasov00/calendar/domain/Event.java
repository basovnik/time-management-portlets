/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : Event.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.domain;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import net.fortuna.ical4j.model.DateList;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.Recur;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.parameter.Value;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.Location;
import net.fortuna.ical4j.model.property.RRule;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.util.SimpleHostInfo;
import net.fortuna.ical4j.util.UidGenerator;
import dip.xbasov00.calendar.util.CalendarException;
import dip.xbasov00.util.CalendarHelper;
import dip.xbasov00.util.DateWithDefaultTimezone;
import dip.xbasov00.util.Resources;
import dip.xbasov00.util.StringHelper;

/**
 * Class Event represents event entity.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Entity
@Table(name = "Event")
public class Event extends CalendarComponent implements Serializable{

    private static final long serialVersionUID = 8358898467573180299L;

    @Getter @Setter
    private String name;

    @Column(length=1000)
    @Getter
    private String description;

    @Getter
    private String location;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datetime_from")
    @Getter @Setter
    private Date datetimeFrom;


    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datetime_to")
    @Getter @Setter
    private Date datetimeTo;


    @Column(name = "all_day_event")
    @Getter
    private boolean allDayEvent;


    /**
     * Recurrence rule according to RFC 5545
     * @see http://tools.ietf.org/html/rfc5545#section-3.3.10
     */
    @OneToOne(mappedBy="event", cascade=CascadeType.ALL, orphanRemoval=true, fetch = FetchType.EAGER)
    @Getter @Setter
    private RecurRule recurRule;

 
    public Event() { }



    public Event(int id, String name, String description, String location, Date datetimeFrom, Date datetimeTo, boolean allDayEvent, RecurRule recurRule, Calendar calendar, String veventUid) {
        super(id, veventUid, calendar);
        this.name = name;
        this.description = description;
        this.location = location;
        this.datetimeFrom = datetimeFrom;
        this.datetimeTo = datetimeTo;
        this.allDayEvent = allDayEvent;
        this.recurRule = recurRule;
    }




    public Event(Event event) {
        // Warning: event.calendar is original reference!
        // TODO recurrences - clone
        this(event.id, event.name, event.description, event.location, event.datetimeFrom, event.datetimeTo, event.allDayEvent, event.recurRule, event.calendar, event.iCalCompUID);
    }



    /**
     * Creates Event from VEvent.
     * 
     * Note:
     * All-one-day-event is represented as:
     * DTSTART;VALUE=DATE:20130527 
     * DTEND;VALUE=DATE:20130528 
     * but this application will use time period:
     * 27.5.2013 00:00:00:000 - 27.5.2013 23:59:59:999
     * Following constructor have to handle this problem...
     * 
     * @see <a href="http://build.mnode.org/projects/ical4j/apidocs/index.html">iCal4j API</a>
     * @param vevent
     * @param calendar
     */
    public Event(VEvent vevent) {
        this.name = vevent.getSummary().getValue();

        Property tmpProp = vevent.getDescription();
        this.description = (tmpProp == null || tmpProp.getValue() == null) ? null : tmpProp.getValue();

        tmpProp = vevent.getLocation();
        this.location = (tmpProp == null || tmpProp.getValue() == null) ? null : tmpProp.getValue();

        
        this.allDayEvent = !(vevent.getStartDate().getDate() instanceof net.fortuna.ical4j.model.DateTime);
        
        this.datetimeFrom = vevent.getStartDate().getDate();
        this.datetimeTo = vevent.getEndDate().getDate();
        
        if (allDayEvent) {
            this.datetimeFrom = CalendarHelper.trimDaySeconds(datetimeFrom);
            this.datetimeTo = new Date(CalendarHelper.trimDaySeconds(datetimeTo).getTime() - 1);           
        }
        
        Property rRuleProp = vevent.getProperty(net.fortuna.ical4j.model.property.RRule.RRULE);
        if (rRuleProp != null) {
            this.recurRule = new RecurRule(rRuleProp.getValue(), this);
        }
        super.iCalCompUID = vevent.getUid().getValue();
    }
    
    
    public String getVeventUid() {
        return super.iCalCompUID;
    }
    
    
    public void setVeventUid(String vevent_uid) {
        super.iCalCompUID = vevent_uid;
    }


    /**
     * Checks if event contains recurrent events.
     *
     * @return
     */
    public boolean hasRecurrences() {
        return (recurRule != null);
    }



    public void setDescription(String description) {
        this.description = StringHelper.trimOrNull(description);
    }


    public void setLocation(String location) {
        this.location = StringHelper.trimOrNull(location);
    }


    /**
     * Set all-day event.
     * If all-day event is set. The datetimeFrom and datetimeTo are adjusted.
     *
     * @param allDayEvent
     */
    public void setAllDayEvent(boolean allDayEvent) {

        // Set duration 00:00:00 - 23:59:59:999
        if(allDayEvent == true) {
            java.util.Calendar calendar = java.util.Calendar.getInstance();
            calendar.setTime(datetimeFrom);
            CalendarHelper.trimDaySeconds(calendar);
            setDatetimeFrom(calendar.getTime());

            calendar.setTime(datetimeTo);
            CalendarHelper.trimDaySeconds(calendar);
            calendar.add(java.util.Calendar.DATE, 1);
            calendar.add(java.util.Calendar.MILLISECOND, -1);
            setDatetimeTo(calendar.getTime());
        }
        // If event was all-day-event. We do not want to rewrite time.
        // Set duration now - (now + 1 hour)
        else if (this.allDayEvent == true){
            setDatetimeFrom(new Date());
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(datetimeFrom);
            cal.add(java.util.Calendar.HOUR_OF_DAY, 1);
            setDatetimeTo(cal.getTime());
        }

        this.allDayEvent = allDayEvent;
    }

    
    
    @Override
    public net.fortuna.ical4j.model.component.CalendarComponent getVCalendarComponent(String hostName) throws CalendarException {
        return getVEvent(hostName);
    }
    
    
    
    /**
     * Creates VEvent component for VCalendar
     * @see <a href="http://build.mnode.org/projects/ical4j/apidocs/index.html">iCal4j API</a>
     * @param hostName Host name (e.g. "host.com")
     * @return VEvent component for VCalendar
     * @throws CalendarException
     */
    public VEvent getVEvent(String hostName) throws CalendarException {

        // Create VEvent
        // Add DTSTART, SUMMARY
        VEvent vevent = null;
        if (isAllDayEvent()) {
            vevent = new VEvent(new DateWithDefaultTimezone(datetimeFrom.getTime()), new DateWithDefaultTimezone(datetimeTo.getTime() +1), name);           
        }
        else {
            vevent = new VEvent(new net.fortuna.ical4j.model.DateTime(datetimeFrom), new net.fortuna.ical4j.model.DateTime(datetimeTo), name);
        }

        // Add UID
        if (iCalCompUID == null) { // Event of local calendar - own uid
            UidGenerator ug = new UidGenerator(new SimpleHostInfo(hostName), Integer.toString(id));
            vevent.getProperties().add(ug.generateUid());
        }
        else { // Event of remote calendar - original uid
            vevent.getProperties().add(new Uid(iCalCompUID));
        }

        // Add DESCRIPTION
        if (description != null) {
            vevent.getProperties().add(new Description(description));
        }

        // Add LOCATION
        if (location != null) {
            vevent.getProperties().add(new Location(location));
        }

        // Add RRULE
        if (recurRule != null) {
            try {
                vevent.getProperties().add(new RRule(recurRule.getRrule()));
            } catch (ParseException e) {
                throw new CalendarException(e.getMessage());
            }
        }
        return vevent;
    }



    /**
     * This method updates event's properties by it's newer representation.
     * Events must have the same vevent uid! Calendar property is not modified!
     * 
     * @param eventSrc Newer version of object.
     */
    public void updateBy(Event eventSrc) {

        if (eventSrc.iCalCompUID != null && iCalCompUID.equals(eventSrc.iCalCompUID)) {
            this.name = eventSrc.name;
            this.location = eventSrc.location;
            this.datetimeFrom = eventSrc.datetimeFrom;
            this.datetimeTo = eventSrc.datetimeTo;
            this.allDayEvent = eventSrc.allDayEvent;
            this.recurRule = eventSrc.recurRule;
            if (this.recurRule != null) {
                this.recurRule.setEvent(this);
            }
            this.description = eventSrc.description;
            // this.calendar = eventSrc.calendar;
        }
    }


    /**
     * Returns list of recurring events calculated from RRULE in specified period.
     * @param fromDate Start of period.
     * @param toDate End of period.
     * @return List of start datetimes in specified period.
     * @throws CalendarException
     */
    @SuppressWarnings("unchecked")
    public List<Date> getRecurDates(Date fromDate, Date toDate) throws CalendarException {
        List<Date> recs = new ArrayList<Date>();
        if (hasRecurrences()) {
            try {
                Recur recurs = new Recur(recurRule.getRrule());
                DateList dl = recurs.getDates(
                    new net.fortuna.ical4j.model.DateTime(this.getDatetimeFrom()), // seed
                    new net.fortuna.ical4j.model.DateTime(fromDate), // datetimeFrom
                    new net.fortuna.ical4j.model.DateTime(toDate), // datetimeTo
                    Value.DATE_TIME);
                recs.addAll(dl);
            } catch (ParseException e) {
                throw new CalendarException(e.getMessage());
            }
        }
        return recs;
    }



    /**
     * Returns list of events in specified date calculated from RRULE.
     * @param date Date (time = 00:00:00)
     * @return List of events or null. One event can recur more times in one day.
     * @throws CalendarException Method
     */
    public List<Event> getRecurEvents(Date date) throws CalendarException {
        List<Event> events = new ArrayList<Event>();
        if (hasRecurrences()) {
            // Calculate next day.
            Date nextDay = new Date(date.getTime() + TimeUnit.DAYS.toMillis(1) - 1);

            List<Date> dates = getRecurDates(date, nextDay);

            for(Date d : dates) {
                Date startDate = CalendarHelper.setEqualDayTime(this.datetimeFrom, d);
                Event eventCopy = new Event(this);
                // eventCopy.rRule = null;
                eventCopy.setDatetimeFrom(startDate);
                eventCopy.setDatetimeTo(new Date(startDate.getTime() + (datetimeTo.getTime() - datetimeFrom.getTime())));
                events.add(eventCopy);
            }
        }
        return events;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (allDayEvent ? 1231 : 1237);
        result = prime * result
                + ((datetimeFrom == null) ? 0 : datetimeFrom.hashCode());
        result = prime * result
                + ((datetimeTo == null) ? 0 : datetimeTo.hashCode());
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result
                + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result
                + ((recurRule == null) ? 0 : recurRule.hashCode());
        return result;
    }



    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Event other = (Event) obj;
        if (allDayEvent != other.allDayEvent)
            return false;
        if (datetimeFrom == null) {
            if (other.datetimeFrom != null)
                return false;
        } else if (!datetimeFrom.equals(other.datetimeFrom))
            return false;
        if (datetimeTo == null) {
            if (other.datetimeTo != null)
                return false;
        } else if (!datetimeTo.equals(other.datetimeTo))
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (recurRule == null) {
            if (other.recurRule != null)
                return false;
        } else if (!recurRule.equals(other.recurRule))
            return false;
        return true;
    }



    @Override
    public Date getDateToCompare() {
        return this.datetimeFrom;
    }



    @Override
    public String getFormattedDate(Date selectedDate) {
        
        if (isAllDayEvent()) {
            return Resources.getBundle().getString("AllDayEvent");
        }
        java.util.Calendar from = java.util.Calendar.getInstance();
        from.setTime(datetimeFrom);
        CalendarHelper.trimDaySeconds(from);

        java.util.Calendar to = java.util.Calendar.getInstance();
        to.setTime(datetimeTo);
        CalendarHelper.trimDaySeconds(to);

        java.util.Calendar selected = java.util.Calendar.getInstance();
        selected.setTime(selectedDate);
        CalendarHelper.trimDaySeconds(selected);

        if (from.compareTo(selected) < 0 && to.compareTo(selected) > 0) {
            return Resources.getBundle().getString("AllDayEvent");
        }
        SimpleDateFormat hourFormatter = new SimpleDateFormat("HH:mm");

        if (from.compareTo(selected) == 0 && to.compareTo(selected) > 0) {
            return Resources.getBundle().getString("fromDatetime") + " " + hourFormatter.format(datetimeFrom);
        }

        if (to.compareTo(selected) == 0 && from.compareTo(selected) < 0) {
            return Resources.getBundle().getString("toDatetime") + " " + hourFormatter.format(datetimeTo);
        }

        return Resources.getBundle().getString("fromDatetime") + " " + hourFormatter.format(datetimeFrom)
                + " " + Resources.getBundle().getString("toDatetime") + " " + hourFormatter.format(datetimeTo);
    }
    
}

