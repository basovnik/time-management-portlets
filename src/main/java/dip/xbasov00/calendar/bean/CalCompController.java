/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : CalCompController.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.bean;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import dip.xbasov00.calendar.domain.CalendarComponent;
import dip.xbasov00.calendar.domain.Event;
import dip.xbasov00.calendar.domain.ToDo;

/**
 * Class EventController is used to manipulated with entities of type CalendarComponent.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Named
@SessionScoped
public class CalCompController implements Serializable {

    
    private static final long serialVersionUID = -9105044481396748211L;

    
    /**
     * Checks if specified component is of type Event
     * 
     * @param calendar component
     * @return boolean value
     */
    public boolean isEvent(CalendarComponent cp) {
        return cp instanceof Event;
    }

    
    
    /**
     * Chcecks if specified component is of type ToDo
     * 
     * @param calendar component
     * @return boolean value
     */
    public boolean isToDo(CalendarComponent cp) {
        return cp instanceof ToDo;
    }
    
    
    
    /**
     * Returns object of type Event if parameter is of type Event.
     * 
     * @param cp
     * @return Returns event or null.
     */
    public Event getEvent(CalendarComponent cp) {
        return isEvent(cp) ? (Event) cp : null;
    }

    
    
    /**
     * Returns object of type ToDo if parameter is of type ToDo.
     * 
     * @param cp
     * @return Returns todo or null.
     */
    public ToDo getToDo(CalendarComponent cp) {
        return isToDo(cp) ? (ToDo) cp : null;
    }
    
}
