/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : MonthCalendarBean.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.portlet.PortletPreferences;

import org.apache.commons.lang.time.DateUtils;

import dip.xbasov00.calendar.dao.CalendarComponentDAO;
import dip.xbasov00.calendar.dao.CalendarDAO;
import dip.xbasov00.calendar.domain.Calendar;
import dip.xbasov00.calendar.domain.CalendarComponent;
import dip.xbasov00.util.PortletHelper;


/**
 * Class MonthCalendarBean manipulates with calendar component in month view.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Named
@SessionScoped
public class MonthCalendarBean implements Serializable {

    private static final long serialVersionUID = 8712614200764974043L;

    
    /**
     * Attribute key in portlet specific session representing selected date.
     * 
     * Selected date in month-view calendar component.
     */
    private static final String ATTR_SELECTED_DATE = "MonthCalendarBean:selectedDate";
    
    
    
    /**
     * Attribute key in portlet specific session representing selected calendars.
     * 
     * Selected calendars for month-view calendar component.
     */
    private static final String ATTR_SELECTED_CALENDARS = "MonthCalendarBean:selectedCalendars";
        
    
    @Inject
    private CalendarComponentDAO calCompDao;


    @Inject
    private CalendarDAO calendarDao;


    /**
     * Number of days in week.
     */
    private static final int DAYS_IN_WEEK = 7;


    /**
     * Number of displayed weeks in month.
     */
    private static final int DISPLAYED_WEEKS = 6;



    /**
     * Select date.
     * 
     * @param date
     */
    public void setSelectedDate(Date date) {
        PortletHelper.setPortletSessionAttribute(ATTR_SELECTED_DATE, date);
    }
    
    
    
    /**
     * Get selected date.
     * 
     * @return date
     */
    public Date getSelectedDate() {       
        Object obj = PortletHelper.getPortletSessionAttribute(ATTR_SELECTED_DATE, Date.class);
        if (obj instanceof Date) {
            return (Date) obj;
        }
        else {
            init();
            return getSelectedDate();
        }
    }


    /**
     * Select calendars.
     * 
     * @param calendars
     */
    public void setSelectedCalendars(List<Calendar> selectedCalendars) {
        PortletHelper.setPortletSessionAttribute(ATTR_SELECTED_CALENDARS, selectedCalendars);
    }
    
    
    
    /**
     * Get selected calendars.
     * 
     * @return calendars
     */
    @SuppressWarnings("unchecked")
    public List<Calendar> getSelectedCalendars() {       
        Object obj = PortletHelper.getPortletSessionAttribute(ATTR_SELECTED_CALENDARS, List.class);
        if (obj instanceof List) {
            return (List<Calendar>) obj;
        }
        else {
            init();
            return getSelectedCalendars();
        }
    }



    /**
     * Bean initialization.
     */
    @PostConstruct
    public void init() {
        setSelectedDate(new Date());
        setSelectedCalendars(new ArrayList<Calendar>());
        getSelectedCalendars().addAll(calendarDao.getlAllInPortletInstance(PortletHelper.getRequest().getWindowID()));
    }



    /**
     * Returns number of days in week.
     *
     * @return Number of days in week.
     */
    public int getDaysInWeek() {
        return DAYS_IN_WEEK;
    }



    /**
     * Returns number of displayed weeks.
     *
     * @return Number of displayed weeks.
     */
    public int getDisplayedWeeks() {
        return DISPLAYED_WEEKS;
    }


    /**
     * Returns first day in week from user preferences.
     *
     * @return 1=Sunday, 2=Monday
     */
    public int getFirstDayInWeek() {
        PortletPreferences portletPreferences = PortletHelper.getRequest().getPreferences();
        return Integer.parseInt(portletPreferences.getValue("firstDayInWeek", String.valueOf(java.util.Calendar.MONDAY)));
    }



    /**
     * Tests if week starts on Sunday.
     *
     * @return Boolean value.
     */
    public boolean weekStartsOnSunday() {
        return (getFirstDayInWeek() == java.util.Calendar.SUNDAY);
    }



    /**
     * Returns dates to display in actual month-view calendar component.
     * @return Dates to display.
     */
    public List<Date> getDaysFromMonthView() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(getSelectedDate());
        cal.set(java.util.Calendar.DATE, 1);

        // Sunday = 1, Monday = 2, ..., Saturday = 6
        int firstWeekDayInMonth = cal.get(java.util.Calendar.DAY_OF_WEEK);

        // Setup first (Sunday|Monday) in week where was the 1st day of month.
        int dayStep = -firstWeekDayInMonth + getFirstDayInWeek();
        if (dayStep > 0) { // 1st day in month is Sunday && 1st weekday is Monday
            dayStep -= DAYS_IN_WEEK;
        }
        cal.add(java.util.Calendar.DAY_OF_MONTH, dayStep);

        List<Date> calendars = new ArrayList<Date>();
        for(int i=0; i < DAYS_IN_WEEK * DISPLAYED_WEEKS; i++) { // 5 weeks
            calendars.add(cal.getTime());
            cal.add(java.util.Calendar.DAY_OF_MONTH, 1);
        }
        return calendars;

    }



    /**
     * Sets the 1st day in next month.
     */
    public void nextMonth() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(getSelectedDate());
        cal.add(java.util.Calendar.MONTH, 1);
        cal.set(java.util.Calendar.DATE, 1);
        setSelectedDate(cal.getTime());
    }



    /**
     * Sets the 1st day in previous month.
     */
    public void prevMonth() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(getSelectedDate());
        cal.add(java.util.Calendar.MONTH, -1);
        cal.set(java.util.Calendar.DATE, 1);
        setSelectedDate(cal.getTime());
    }



    /**
     * Sets the first day in the same month next year.
     */
    public void nextYear() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(getSelectedDate());
        cal.add(java.util.Calendar.YEAR, 1);
        cal.set(java.util.Calendar.DATE, 1);
        setSelectedDate(cal.getTime());
    }



    /**
     * Sets the first day in the same month previous year.
     */
    public void prevYear() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(getSelectedDate());
        cal.add(java.util.Calendar.YEAR, -1);
        cal.set(java.util.Calendar.DATE, 1);
        setSelectedDate(cal.getTime());
    }



    /**
     * Sets the CSS style class for specified date in actual view.
     *
     * @param date Date in actual view.
     * @return String that contains whitespace separated style classes.
     */
    public String getDayCellClass(Date date) {

        java.util.Calendar actCal = java.util.Calendar.getInstance();
        actCal.setTime(date);
        java.util.Calendar todayCal = java.util.Calendar.getInstance();
        todayCal.setTime(new Date());
        java.util.Calendar selectedCal = java.util.Calendar.getInstance();
        selectedCal.setTime(getSelectedDate());

        String elClasses = "weekday";

        if (DateUtils.isSameDay(date, todayCal.getTime())) {
            elClasses += " today";
        }

        if (DateUtils.isSameDay(date, getSelectedDate())) {
            elClasses += " selected";
        }

        if (actCal.get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.SUNDAY) {
            elClasses += " sunday";
        }

        if (actCal.get(java.util.Calendar.MONTH) != selectedCal.get(java.util.Calendar.MONTH)) {
            elClasses += " anotherMonthDay";
        }

        return elClasses.trim();

    }



    /**
     * Get CalendarComponents from selected calendars sorted by datetimeFrom.
     *
     * @return CalendarComponents from selected calendars.
     */
    public List<CalendarComponent> getEventsFromSelectedCalendars(Date date) {
        List<CalendarComponent> calComps = new ArrayList<CalendarComponent>();

        for(Calendar c : getSelectedCalendars()) {
            calComps.addAll(calCompDao.getCalendarComponents(c, date));
        }

        Collections.sort(calComps);
        return calComps;
    }



    /**
     * Calendar (un)setter to selected calendars.
     *
     * @param calendar Calendar to switch.
     * @return Status "success" if action was successful.
     */
    public String switchSelectedCalendar(Calendar calendar) {
        if (getSelectedCalendars().contains(calendar)) {
            getSelectedCalendars().remove(calendar);
        }
        else {
            getSelectedCalendars().add(calendar);
        }
        return "success";
    }

    
    
    /**
     * Adjust calendar component's duration according to selected date.
     *
     * @param event Selected calendar component
     * @return String that represents time duration of selected calendar component in selected date.
     */
    public String timeAtSelectedDate(CalendarComponent calendarComp) {
        return calendarComp.getFormattedDate(getSelectedDate());
    }



}
