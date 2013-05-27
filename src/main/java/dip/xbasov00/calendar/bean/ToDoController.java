/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : ToDoController.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.bean;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dip.xbasov00.calendar.dao.CalendarComponentDAO;
import dip.xbasov00.calendar.domain.ToDo;
import dip.xbasov00.util.PortletHelper;

/**
 * Class EventController is used to manipulated with entities of type ToDo.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Named
@SessionScoped
public class ToDoController implements Serializable {
    

    private static final long serialVersionUID = -3212344878445216266L;


    /**
     * DateTime format used in todo entities.
     */
    private static final String datetimeFormat = "dd/MM/yyyy HH:mm";


    /**
     * Date format used in todo entities.
     */
    private static final String dateFormat = "dd/MM/yyyy";
    
    
    /**
     * Attribute key in portlet specific session representing selected selected todo.
     *
     * Selected todo.
     *
     * Todo is used for most of operations in this bean.
     * It is used for creating and editing of todo,
     * detail view etc.
     */
    private static final String ATTR_EVENT = "ToDoController:todo";
    
    
    
    @Inject
    private CalendarComponentDAO calCompDao;


    
    /**
     * Set todo.
     * 
     * @param todo
     */
    public void setTodo(ToDo todo) {
        PortletHelper.setPortletSessionAttribute(ATTR_EVENT, todo);
    }
    
    
    
    
    /**
     * Get todo.
     * 
     * @return todo
     */
    public ToDo getTodo() {       
        Object obj = PortletHelper.getPortletSessionAttribute(ATTR_EVENT, ToDo.class);
        if (obj instanceof ToDo) {
            return (ToDo) obj;
        }
        else {
            init();
            return getTodo();
        }
    }
  
    

    /**
     * Bean initialization.
     */
    @PostConstruct
    public void init() {
        initToDo(new Date());
    }



    /**
     * Inits todo's scheduled datetime.
     *
     * @param datetimeFrom
     */
    public void initToDo(Date datetimeFrom) {
        setTodo(new ToDo());
        getTodo().setDatetimeScheduled(datetimeFrom);
    }


    
    /**
     * Get DateTime format
     *
     * @return DateTime format.
     */
    public String getDatetimeFormat() {
        return getTodo().isAllDayToDo() ? ToDoController.dateFormat : ToDoController.datetimeFormat;
    }



    /**
     * Deletes todo.
     *
     * @param todo Todo to delete.
     * @return Status "success" if action was successful.
     */
    public String deleteToDo(ToDo todo) {
        calCompDao.deleteCalendarComponent(todo);
        return "success";
    }


}
