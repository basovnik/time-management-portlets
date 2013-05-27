/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : CalendarController.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.portlet.PortletRequest;

import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.extensions.CalendarBuilder;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import dip.xbasov00.calendar.dao.CalendarDAO;
import dip.xbasov00.calendar.domain.CalDAVRemoteCalendar;
import dip.xbasov00.calendar.domain.Calendar;
import dip.xbasov00.calendar.domain.ICalRemoteCalendar;
import dip.xbasov00.calendar.domain.RemoteCalendar;
import dip.xbasov00.calendar.util.CalendarException;
import dip.xbasov00.util.PortletHelper;
import dip.xbasov00.util.Resources;

/**
 * Class CalendarController is used to manipulated with entities of type Calendar.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Named
@SessionScoped
public class CalendarController implements Serializable {

    
    private static final long serialVersionUID = 341437966085806118L;

    
    /**
     * Attribute key in portlet specific session representing selected calendar.
     *
     * Calendar is used for most of operations in this bean.
     * It is used for creating and editing of calendar,
     * detail view etc.
     */
    private static final String ATTR_CALENDAR = "CalendarController:calendar";
    
    
    
    /**
     * Type of calendar to create.
     *
     * This property is used to dynamic creating of different
     * types of calendar.
     */
    private static final String ATTR_CREATION_TYPE = "CalendarController:creationType";
    
    

    // Types of calendar creation.
    private static final String LOCAL_CAL = "localCal";
    private static final String UPLOAD_CAL = "uploadCal";
    private static final String REMOTE_CAL = "remoteCal";
    private static final String CALDAV_CAL = "calDavCal";


    @Inject
    private Logger logger;


    @Inject
    private CalendarDAO calendarDao;
    
    
    @Inject
    private MonthCalendarBean monthCalendarBean;

    
    @Inject
    private PopupManagerCalendarBean popupBean;
    
    
    
    /**
     * Set calendar.
     * 
     * @param calendar
     */
    public void setCalendar(Calendar calendar) {
        PortletHelper.setPortletSessionAttribute(ATTR_CALENDAR, calendar);
    }
    
    
    
    /**
     * Get calendar.
     * 
     * @return calendar
     */
    public Calendar getCalendar() {       
        Object obj = PortletHelper.getPortletSessionAttribute(ATTR_CALENDAR, Calendar.class);
        if (obj instanceof Calendar) {
            return (Calendar) obj;
        }
        else {
            init();
            return getCalendar();
        }
    }
    
    
    
    /**
     * Get creation type.
     * 
     * @return string
     */
    public String getCreationType() {       
        Object obj = PortletHelper.getPortletSessionAttribute(ATTR_CREATION_TYPE, String.class);
        if (obj instanceof String) {
            return (String) obj;
        }
        else {
            init();
            return getCreationType();
        }
    }
    
    
    
    
    /**
     * This method sets calendar creation type.
     * According to this value, new working calendar is instantiated.
     *
     * @param creationType  Type of new Calendar.
     *                      Possible values from set {LOCAL_CAL, UPLOAD_CAL, REMOTE_CAL, CALDAV_CAL}.
     */
    public void setCreationType(String creationType) {

        if (creationType.equals(LOCAL_CAL)) {
            setCalendar(new Calendar());
        }
        else if (creationType.equals(UPLOAD_CAL)) {
            // init value for OneFromManyList element.
            List<Calendar> calendars = calendarDao.getlAllInPortletInstance(PortletHelper.getRequest().getWindowID());
            setCalendar(calendars.isEmpty() ? null : calendars.get(0));
        }
        else if (creationType.equals(REMOTE_CAL)) {
            setCalendar(new ICalRemoteCalendar());
        }
        else if (creationType.equals(CALDAV_CAL)) {
            setCalendar(new CalDAVRemoteCalendar());
        }

        PortletHelper.setPortletSessionAttribute(ATTR_CREATION_TYPE, creationType);
    }



    /**
     * Bean initialization.
     */
    @PostConstruct
    public void init() {
        setCreationType(LOCAL_CAL);
    }



    /**
     * This method returns full URL to iCalendar file.
     *
     * @param calendar
     * @return URL to iCalendar file
     */
    public String getCalendarShareURL(Calendar calendar) {
        PortletRequest portletRequest = PortletHelper.getRequest();
        return "http://" + portletRequest.getServerName() + ":" +
            + portletRequest.getServerPort()
            + portletRequest.getContextPath()
            + "/rest/calendars/" + calendar.getPrivateId();
    }



    /**
     * Returns map of possible localized creation types.
     *
     * @return Map of creation types.
     */
    public Map<String, String> getCreationTypes() {
        Map<String, String> items = new LinkedHashMap<String, String>();
        items.put(Resources.getBundle().getString("LocalCalendar"), LOCAL_CAL);
        items.put(Resources.getBundle().getString("FileUpload"), UPLOAD_CAL);
        items.put(Resources.getBundle().getString("RemoteICalendar"), REMOTE_CAL);
        items.put(Resources.getBundle().getString("RemoteCalDAV"), CALDAV_CAL);
        return items;
    }



    /**
     * Returns remote calendar if selected.
     * @return Remote calendar or null.
     */
    public RemoteCalendar getRemoteCalendar() {
        return (getCalendar() instanceof RemoteCalendar) ? (RemoteCalendar) getCalendar() : null;
    }



    /**
     * Returns remote CalDAV calendar if selected.
     * @return Remote CalDAV calendar or null.
     */
    public CalDAVRemoteCalendar getCalDavRemoteCalendar() {
        return (getCalendar() instanceof CalDAVRemoteCalendar) ? (CalDAVRemoteCalendar) getCalendar() : null;
    }



    /**
     * Checks if selected calendar is remote
     *
     * @return Boolean value
     */
    public boolean isCurrentCalendarRemote() {
        return isCalendarRemote(getCalendar());
    }



    /**
     * Checks if selected calendar is remote.
     *
     * @param cal Calendar to check.
     * @return Boolean value.
     */
    public boolean isCalendarRemote(Calendar cal) {
        return (cal instanceof RemoteCalendar);
    }



    /**
     * This method returns all calendars of actual requested portlet instance in ViewScope.
     * Note: Use this method for further selects in bean.
     *
     * @return All calendars from actual requested portlet instance in ViewScope.
     */
    public List<Calendar> getAllCalendars() {
        return calendarDao.getlAllInPortletInstance(PortletHelper.getRequest().getWindowID());
    }



    /**
     * This method returns editable calendars. All types instead of ICalRemoteCalendar.
     *
     * @return All editable calendars.
     */
    public List<Calendar> getEditableCalendars() {
        List<Calendar> calendars = new ArrayList<Calendar>();
        for(Calendar c : getAllCalendars()) {
            if (!(c instanceof ICalRemoteCalendar)) {
                calendars.add(c);
            }
        }
        return calendars;
    }



    /**
     * Creates new calendar from selected one in actual portlet context.
     * Sets the calendar as selected and prepares settings for popup-panel.
     * 
     * If calendar is remote it will try to synchronize calendar. If it fails
     * calendar will be deleted.
     *
     * @return Status "success" if action was successful.
     */
    public String createCalendar() {
        getCalendar().setPortletInstanceId(PortletHelper.getRequest().getWindowID());

        try {
            calendarDao.createCalendar(getCalendar());
            
            monthCalendarBean.switchSelectedCalendar(getCalendar());
            popupBean.setPopup("calendarDetail", Resources.getBundle().getString("CalendarDetail"));
            
            return "success";
        } catch (Exception e) {
            
            Calendar cal = getCalendar();
            setCreationType(getCreationType());
            
            if (cal instanceof CalDAVRemoteCalendar && getCalendar() instanceof CalDAVRemoteCalendar) {
                ((CalDAVRemoteCalendar) getCalendar()).updateBy((CalDAVRemoteCalendar) cal);
            }
            else if (cal instanceof RemoteCalendar && getCalendar() instanceof RemoteCalendar) {
                ((RemoteCalendar) getCalendar()).updateBy((RemoteCalendar) cal);
            }
            else {
                getCalendar().updateBy(cal);
            }
            
            
            FacesContext fc = FacesContext.getCurrentInstance();
            FacesMessage msg = new FacesMessage(e.getMessage());
            msg.setSeverity(FacesMessage.SEVERITY_ERROR);
            fc.addMessage(null, msg);
            fc.renderResponse();
        }
        return "error";
    }


    
    /**
     * Edits selected calendar.
     *
     * @return Status "success" if action was successful.
     */
    public String editCalendar() {
        calendarDao.editCalendar(getCalendar());
        monthCalendarBean.switchSelectedCalendar(getCalendar());
        return "success";
    }



    /**
     * Deletes selected calendar.
     *
     * @param calendar Calendar to delete.
     * @return Status "success" if action was successful.
     */
    public String deleteCalendar(Calendar calendar) {
        calendarDao.deleteCalendar(calendar);
        return "success";
    }



    /**
     * Synchronize selected calendar if calendar is remote.
     *
     * @return Status "success" if action was successful.
     */
    public String synchronyzeCalendar() {
        if (isCurrentCalendarRemote()) {
            try {
                calendarDao.synchronizeRemoteCalendar((RemoteCalendar) getCalendar());
                
                FacesContext fc = FacesContext.getCurrentInstance();
                FacesMessage msg = new FacesMessage(Resources.getBundle().getString("SynchroOK"));
                msg.setSeverity(FacesMessage.SEVERITY_INFO);
                fc.addMessage(null, msg);
                
            } catch (CalendarException e) {
                
                FacesContext fc = FacesContext.getCurrentInstance();
                FacesMessage msg = new FacesMessage(e.getMessage());
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                fc.addMessage(null, msg);
                fc.renderResponse();
                               
                logger.log(Level.SEVERE, e.getMessage());
            }
        }
        
        return "success";
    }



    /**
     * Renews private id of selected calendar.
     * @return
     */
    public String renewCalendarPrivateId() {
        getCalendar().generatePrivateId();
        calendarDao.editCalendar(getCalendar());
        return "success";
    }



    /**
     * Listener ensures upload of one iCalendar file.
     * Listener is used in rich:fileUpload element.
     *
     * @param event FileUploadEvent
     * @throws Exception
     */
    public void filesUploadlistener(FileUploadEvent event) throws Exception {

        UploadedFile file = event.getUploadedFile();
        logger.log(Level.INFO, "Filename: " + file.getName());

        try {
            // Create new VCalendar
            InputStream is = file.getInputStream();
            CalendarBuilder builder = new CalendarBuilder();
            net.fortuna.ical4j.model.Calendar vcalendar = builder.build(is);

            Calendar cal = new Calendar(vcalendar);
            calendarDao.mergeCalendars(getCalendar(), cal);

            // Log imported VCalendar
            logger.log(Level.INFO, "VCalendar "
                    + " from file " + file.getName()
                    + " to calendar '" + getCalendar().getName() + "' was successfully appended!");

        }catch(ParserException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        catch(IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
    }

}
