/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : SettingsBean.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.portlet.ValidatorException;

import dip.xbasov00.calendar.util.CalendarException;
import dip.xbasov00.util.PortletHelper;
import dip.xbasov00.util.Resources;

/**
 * Class SettingsBean manipulates with portlet user settings.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Named
@SessionScoped
public class SettingsBean implements Serializable {

    private static final long serialVersionUID = -6187188615594306142L;

    
    /**
     * First day in week (Sunday or Monday)
     */
    private static final String ATTR_FIRTS_DAY_IN_WEEK = "SettingsBean:firstDayInWeek";

    /**
     * Set First day in week.
     * 
     * @param First day in week
     */
    public void setFirstDayInWeek(Integer firstDayInWeek) {
        PortletHelper.setPortletSessionAttribute(ATTR_FIRTS_DAY_IN_WEEK, firstDayInWeek);
    }
    
    
    
    /**
     * Get First day in week.
     * 
     * @return First day in week
     */
    public Integer getFirstDayInWeek() {       
        Object obj = PortletHelper.getPortletSessionAttribute(ATTR_FIRTS_DAY_IN_WEEK, Integer.class);
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        else {
            init();
            return getFirstDayInWeek();
        }
    }
    

    /**
     * Bean initialization.
     */
    @PostConstruct
    public void init() {
        PortletPreferences portletPreferences = PortletHelper.getRequest().getPreferences();
        setFirstDayInWeek(Integer.decode(portletPreferences.getValue("firstDayInWeek", String.valueOf(java.util.Calendar.MONDAY))));
    }



    /**
     * Returns possible starts of week.
     *
     * @return Map of possible starts of week.
     */
    public Map<String, Integer> getPossibleStartsOfWeek() {

        Map<String, Integer> map = new HashMap<String, Integer>();
        if (Resources.getBundle().containsKey("Sunday")) {
            map.put(Resources.getBundle().getString("Sunday"), java.util.Calendar.SUNDAY);
        }
        else {
            map.put("Sunday", java.util.Calendar.SUNDAY);
        }
        if (Resources.getBundle().containsKey("Monday")) {
            map.put(Resources.getBundle().getString("Monday"), java.util.Calendar.MONDAY);
        }
        else {
            map.put("Monday", java.util.Calendar.MONDAY);
        }

        return map;
    }



    /**
     * Saves user preferences.
     *
     * @throws CalendarException
     */
    public void save() throws CalendarException {
        PortletPreferences portletPreferences = PortletHelper.getRequest().getPreferences();
        try {
            portletPreferences.setValue("firstDayInWeek", String.valueOf(getFirstDayInWeek()));
            portletPreferences.store();
            
            FacesContext fc = FacesContext.getCurrentInstance();
            FacesMessage msg = new FacesMessage(Resources.getBundle().getString("SettingsSaveOK"));
            msg.setSeverity(FacesMessage.SEVERITY_INFO);
            fc.addMessage(null, msg);

        } catch (ReadOnlyException e) {
            throw new CalendarException(e.getMessage());
        } catch (ValidatorException e) {
            throw new CalendarException(e.getMessage());
        } catch (IOException e) {
            throw new CalendarException(e.getMessage());
        }
    }

}
