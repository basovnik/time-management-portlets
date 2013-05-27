/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : EventController.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.bean;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.inject.Inject;
import javax.inject.Named;

import lombok.Getter;
import dip.xbasov00.calendar.dao.CalendarComponentDAO;
import dip.xbasov00.calendar.dao.EventDAO;
import dip.xbasov00.calendar.domain.Event;
import dip.xbasov00.calendar.domain.RecurRule;
import dip.xbasov00.util.CalendarHelper;
import dip.xbasov00.util.PortletHelper;
import dip.xbasov00.util.Resources;

/**
 * Class EventController is used to manipulated with entities of type Event.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Named
@SessionScoped
public class EventController implements Serializable {


    private static final long serialVersionUID = 777366673660543348L;

    // Frequency constants for recurrence
    private static final String NEVER = "NEVER";
    private static final String DAILY = "DAILY";
    private static final String WEEKLY = "WEEKLY";
    private static final String MONTHLY = "MONTHLY";
    private static final String YEARLY = "YEARLY";

    // Types of recurrence end
    private static final String END_BY_DATE = "END_BY_DATE";
    private static final String END_BY_NUMBER = "END_BY_NUMBER";

    // Types of interval
    private static final String INTERVAL_1ST = "INTERVAL_1ST";
    private static final String INTERVAL_2ND = "INTERVAL_2ND";
    private static final String INTERVAL_3RD = "INTERVAL_3RD";
    private static final String INTERVAL_4TH = "INTERVAL_4TH";
    private static final String CUSTOM = "CUSTOM";

    // Types of month recurrence
    private static final String DAY_OF_MONTH = "DAY_OF_MONTH";
    private static final String DAY_OF_WEEK = "DAY_OF_WEEK";

    
    /**
     * DateTime format used in event Entities.
     */
    private static final String datetimeFormat = "dd/MM/yyyy HH:mm";


    /**
     * Date format used in event Entities.
     */
    private static final String dateFormat = "dd/MM/yyyy";
    
    
    /**
     * Attribute key in portlet specific session representing selected event.
     *
     * Selected event.
     *
     * Event is used for most of operations in this bean.
     * It is used for creating and editing of event,
     * detail view etc.
     */
    private static final String ATTR_EVENT = "EventController:event";
    
    
    
    /**
     * Attribute key in portlet specific session representing selected recurRule.
     *
     * Selected recurRule.
     *
     * This property represents actual managed recur rule of selected event.
     * @see http://www.kanzaki.com/docs/ical/rrule.html
     */
    private static final String ATTR_RECUR_RULE = "EventController:recurRule";

     
    
    @Inject
    private Logger logger;


    @Inject
    private EventDAO eventDao;
    
    
    @Inject
    private CalendarComponentDAO calCompDao;



    /**
     * Frequency of recurrence.
     * Possible values: "NEVER", "DAILY", "WEEKLY", "MONTHLY", "YEARLY"
     * 
     * Note: This variable is available only from popup panel, we do not have to save it to portlet session.
     */
    @Getter
    private String frequency;


    /**
     * Type of recurrence end.
     * Possible values: "END_BY_DATE", "END_BY_NUMBER"
     * 
     * Note: This variable is available only from popup panel, we do not have to save it to portlet session.
     */
    @Getter
    private String endOfRecur;


    /**
     * Interval type of recurrence
     * Possible values: "INTERVAL_1ST", "INTERVAL_2ND", "INTERVAL_3RD", "INTERVAL_4TH", "CUSTOM"
     * 
     * Note: This variable is available only from popup panel, we do not have to save it to portlet session.
     */
    @Getter
    private String recurInterval;


    /**
     * Type of month recurrence.
     * Possible values: "DAY_OF_MONTH", "DAY_OF_MONTH"
     * 
     * Note: This variable is available only from popup panel, we do not have to save it to portlet session.
     */
    @Getter
    private String monthlyRepeatType;
    
    
    /**
     * Set event.
     * 
     * @param event
     */
    public void setEvent(Event event) {
        PortletHelper.setPortletSessionAttribute(ATTR_EVENT, event);
        setRecurRule(event.getRecurRule());
    }
    
    
    
    
    /**
     * Get event.
     * 
     * @return event
     */
    public Event getEvent() {       
        Object obj = PortletHelper.getPortletSessionAttribute(ATTR_EVENT, Event.class);
        if (obj instanceof Event) {
            return (Event) obj;
        }
        else {
            init();
            return getEvent();
        }
    }
    
    
    /**
     * Set recur rule.
     * 
     * @param recur rule
     */
    public void setRecurRule(RecurRule rr) {
        PortletHelper.setPortletSessionAttribute(ATTR_RECUR_RULE, rr);
        setupRecurrenceSettings(getRecurRule());
    }
    
    
    
    /**
     * Get recur rule.
     * 
     * @return recur rule
     */
    public RecurRule getRecurRule() {       
        Object obj = PortletHelper.getPortletSessionAttribute(ATTR_RECUR_RULE, RecurRule.class);
        if (obj instanceof RecurRule) {
            return (RecurRule) obj;
        }
        else return null;
    }
    
    
    

    /**
     * Bean initialization.
     */
    @PostConstruct
    public void init() {
        initEvent(new Date());
    }



    /**
     * Inits event's period and some recurrence parameters.
     *
     * @param datetimeFrom
     */
    public void initEvent(Date datetimeFrom) {
        setEvent(new Event());
        getEvent().setDatetimeFrom(datetimeFrom);
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(datetimeFrom);
        cal.add(java.util.Calendar.HOUR_OF_DAY, 1);
        getEvent().setDatetimeTo(cal.getTime());

        setRecurRule(null);
        this.frequency = NEVER;
        this.endOfRecur = NEVER;
        this.recurInterval = INTERVAL_1ST;
        this.monthlyRepeatType = DAY_OF_MONTH;
    }



    /**
     * Returns map of possible localized frequency types of recurrence.
     *
     * @return Map of frequency types for recurrence.
     */
    public Map<String, String> getRecurenceFrequencies() {
        Map<String, String> items = new LinkedHashMap<String, String>();
        items.put(Resources.getBundle().getString(NEVER), NEVER);
        items.put(Resources.getBundle().getString(DAILY), DAILY);
        items.put(Resources.getBundle().getString(WEEKLY), WEEKLY);
        items.put(Resources.getBundle().getString(MONTHLY), MONTHLY);
        items.put(Resources.getBundle().getString(YEARLY), YEARLY);
        return items;
    }



    /**
     * Setup properties used in recurrence form according to recurRule.
     *
     * @param recurRule
     */
    private void setupRecurrenceSettings(RecurRule recurRule) {
        
        if (recurRule != null) {
            // Setting of the frequency
            this.frequency = recurRule.getFrequency();
            if (this.frequency.equals(MONTHLY)) {
                if (!recurRule.getMonthDayList().isEmpty()) {
                    monthlyRepeatType = DAY_OF_MONTH;
                }
                else {
                    monthlyRepeatType = DAY_OF_WEEK;
                }
            }

            // Setting of the end of recurrence
            if (recurRule.getUntil() == null && recurRule.getCount() == -1) {
                this.endOfRecur = NEVER;
            }
            else if (recurRule.getCount() != -1) {
                this.endOfRecur = END_BY_NUMBER;
            }
            else {
                this.endOfRecur = END_BY_DATE;
            }

            // Setting of the interval
            if (recurRule.getInterval() != -1) {
                if (recurRule.getInterval() == 1) {
                    this.recurInterval = INTERVAL_1ST;
                }
                else if (recurRule.getInterval() == 2) {
                    this.recurInterval = INTERVAL_2ND;
                }
                else if (recurRule.getInterval() == 3) {
                    this.recurInterval = INTERVAL_3RD;
                }
                else if (recurRule.getInterval() == 4) {
                    this.recurInterval = INTERVAL_4TH;
                }
                else {
                    this.recurInterval = CUSTOM;
                }
            }
        }
    }



    /**
     * Sets frequency of selected recur rule.
     * If frequency is NEVER it sets recur rule to null. Otherwise
     * method initialize interval to 1.
     *
     * @param frequency
     */
    public void setFrequency(String frequency) {
        this.frequency = frequency;        

        if (frequency.equals(NEVER)) {
            setRecurRule(null);
        }
        else {
            RecurRule r = new RecurRule();
            r.setFrequency(frequency);
            r.setInterval(1);
            setRecurRule(r);
        }
    }



    /**
     * Sets type of end of recurrence.
     * According to type it inialice either number of recurrences or date of end.
     *
     * @param endOfRecur
     */
    public void setEndOfRecur(String endOfRecur) {
        this.endOfRecur = endOfRecur;

        // Default 10 recurrences
        if(endOfRecur.equals(END_BY_NUMBER)) {
            getRecurRule().setCount(10);
        }
        // Default 3 months of recurrences
        else if(endOfRecur.equals(END_BY_DATE)) {
            Date d = (getEvent().getDatetimeFrom() != null) ? getEvent().getDatetimeFrom() : new Date();
            Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(d);
            cal.add(java.util.Calendar.MONTH, 3);
            getRecurRule().setUntil(cal.getTime());
        }
        else {
            // This automatically clears 'count' and 'until' attribute
            // @see ical4j Recur class
            getRecurRule().setCount(-1); // equivalent to this.recurRule.setUntil(null);
        }
    }



    /**
     * Sets recur interval
     * e.g. Interval 2 with DAILY frequency means event will repeat every second day.
     *
     * @param recurInterval
     */
    public void setRecurInterval(String recurInterval) {
        this.recurInterval = recurInterval;

        if (recurInterval.equals(INTERVAL_1ST)) {
            getRecurRule().setInterval(1);
        }
        else if (recurInterval.equals(INTERVAL_2ND)) {
            getRecurRule().setInterval(2);
        }
        else if (recurInterval.equals(INTERVAL_3RD)) {
            getRecurRule().setInterval(3);
        }
        else if (recurInterval.equals(INTERVAL_4TH)) {
            getRecurRule().setInterval(4);
        }
        else {
            getRecurRule().setInterval(5); // Default value for CUSTOM
        }
    }



    /**
     * Sets monthly repeat type.
     * If type is DAY_OF_WEEK event will repeat the same weekday
     * the same nth week following months. If type is DAY_OF_MONTH
     * event will repeat the sate date every month.
     *
     * @param monthlyRepeatType
     */
    public void setMonthlyRepeatType(String monthlyRepeatType) {
        this.monthlyRepeatType = monthlyRepeatType;


        if (getEvent().getDatetimeFrom() != null) {
            if (monthlyRepeatType.endsWith(DAY_OF_WEEK)) {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(getEvent().getDatetimeFrom());

                String day = "";
                switch(cal.get(java.util.Calendar.DAY_OF_WEEK)) {
                    case 1  : day="SU"; break;
                    case 2  : day="MO"; break;
                    case 3  : day="TU"; break;
                    case 4  : day="WE"; break;
                    case 5  : day="TH"; break;
                    case 6  : day="FR"; break;
                    case 7  : day="SA"; break;
                }
                int offset = CalendarHelper.getNthWeekdayOccurence(getEvent().getDatetimeFrom());
                getRecurRule().setDay(day, offset);
            }
            else {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                cal.setTime(getEvent().getDatetimeFrom());
                //TODO length of month 28..31 ? What about February if offset is 30?
                Set<Integer> set = new HashSet<Integer>();
                set.add(cal.get(java.util.Calendar.DATE));
                getRecurRule().setMonthDayList(set);
            }
        }
        else {
            logger.log(Level.SEVERE, "MONTHLY recurrence cannot be set because datetimeFrom is not set!");
        }
    }



    /**
     * Get DateTime format
     *
     * @return DateTime format.
     */
    public String getDatetimeFormat() {
        return getEvent().isAllDayEvent() ? EventController.dateFormat : EventController.datetimeFormat;
    }



    /**
     * Deletes event.
     *
     * @param event Event to delete.
     * @return Status "success" if action was successful.
     */
    public String deleteEvent(Event event) {
        eventDao.deleteRecurRule(event);
        calCompDao.deleteCalendarComponent(event);
        return "success";
    }



    /**
     * Creates new event
     * Append recur rule to selected event before creation.
     *
     * @return Status "success" if action was successful.
     */
    public String createEvent() {
           
        if (getRecurRule() != null) {
            getRecurRule().setEvent(getEvent());
            getEvent().setRecurRule(getRecurRule());
        }
        calCompDao.createCalendarComponent(getEvent());        
        
        return "success";
    }



    /**
     * Edit selected event.
     * Append recur rule to selected event before action.
     *
     * @return Status "success" if action was successful.
     */
    public String editEvent() {
        if (getEvent().getRecurRule() != null) {
            eventDao.deleteRecurRule(getEvent()); //delete old recurRule
        }

        if (getRecurRule() != null) {
            getRecurRule().setEvent(getEvent());
            getEvent().setRecurRule(getRecurRule());
        }
        calCompDao.editCalendarComponent(getEvent());
        return "success";
    }
    
    
    /**
     * Validate if datetimeFrom is before datetimeTo
     * 
     * @param e event
     */
    public void validateDatetimeTo(ComponentSystemEvent e) {

        FacesContext fc = FacesContext.getCurrentInstance();
        UIComponent components = e.getComponent();
        
        // get datetimeFrom
        UIInput uiDatetimeFrom = (UIInput) components.findComponent("datetimeFrom");
        Date datetimeFrom = (Date) uiDatetimeFrom.getLocalValue();

        // get datetimeTo
        UIInput uiDatetimeTo = (UIInput) components.findComponent("datetimeTo");
        Date datetimeTo = (Date) uiDatetimeTo.getLocalValue();
        String datetimeToId = uiDatetimeTo.getClientId();

        if (uiDatetimeFrom == null || uiDatetimeTo == null) {
            return;
        }

        if (datetimeFrom.after(datetimeTo)) {

          FacesMessage msg = new FacesMessage(Resources.getBundle().getString("StartDateAfterEndDate"));
          msg.setSeverity(FacesMessage.SEVERITY_ERROR);
          fc.addMessage(datetimeToId, msg);
          fc.renderResponse();

        }
    }


}
