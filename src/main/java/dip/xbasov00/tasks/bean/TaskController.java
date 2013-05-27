/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : TaskController.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.tasks.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.portlet.PortletRequest;

import net.fortuna.ical4j.data.ParserException;
import net.fortuna.ical4j.extensions.CalendarBuilder;
import net.fortuna.ical4j.model.component.VToDo;
import net.fortuna.ical4j.model.property.Categories;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import dip.xbasov00.tasks.dao.ContextDAO;
import dip.xbasov00.tasks.dao.TaskDAO;
import dip.xbasov00.tasks.domain.Context;
import dip.xbasov00.tasks.domain.GTDGroupType;
import dip.xbasov00.tasks.domain.Task;
import dip.xbasov00.util.PortletHelper;

/**
 * Class TaskController is used to manipulated with entity Task.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Named
@SessionScoped
public class TaskController implements Serializable {


    private static final long serialVersionUID = 7783213762621244401L;

    
    /**
     * Attribute key in portlet specific session representing selected task.
     */
    private static final String ATTR_TASK = "TaskController:task";
    
    
    @Inject
    private TaskDAO taskDao;

    @Inject
    private ContextDAO contextDao;
          
    
    @Inject
    private Logger logger;
    

    /**
     * Set task.
     * 
     * @param task
     */
    public void setTask(Task task) {
        PortletHelper.setPortletSessionAttribute(ATTR_TASK, task);
    }
    
    
    
    /**
     * Get task.
     * 
     * @return
     */
    public Task getTask() {       
        Object obj = PortletHelper.getPortletSessionAttribute(ATTR_TASK, Task.class);
        if (obj instanceof Task) {
            return (Task) obj;
        }
        else {
            initTask();
            return getTask();
        }
    }



    /**
     * Bean initialization.
     */
    @PostConstruct
    public void init() {
        initTask();
    }



    /**
     * Initialize task.
     */
    public void initTask() {
        setTask(new Task());
    }



    /**
     * Get all tasks from GTD group INBOX from current portlet.
     *
     * @return List of tasks.
     */
    public List<Task> getInboxTasks() {
        return taskDao.getPortletTasks(GTDGroupType.INBOX, PortletHelper.getRequest().getWindowID());
    }



    /**
     * Get all tasks from GTD group NEXT from current portlet.
     *
     * @return List of tasks.
     */
    public List<Task> getNextTasks() {
        return taskDao.getPortletTasks(GTDGroupType.NEXT, PortletHelper.getRequest().getWindowID());
    }



    /**
     * Get all tasks from GTD group WAITING_FOR from current portlet.
     *
     * @return List of tasks.
     */
    public List<Task> getWaitingForTasks() {
        return taskDao.getPortletTasks(GTDGroupType.WAITING_FOR, PortletHelper.getRequest().getWindowID());
    }



    /**
     * Get all tasks from GTD group SOMEDAY from current portlet.
     *
     * @return List of tasks.
     */
    public List<Task> getSomedayTasks() {
        return taskDao.getPortletTasks(GTDGroupType.SOMEDAY, PortletHelper.getRequest().getWindowID());
    }



    /**
     * Get all tasks from GTD group TODAY from current portlet.
     * Returns all task from today and older.
     *
     * @return List of tasks.
     */
    public List<Task> getTodayTasks() {
        return taskDao.getPortletScheduledTasks(new Date(), PortletHelper.getRequest().getWindowID());
    }



    /**
     * Get all completed tasks.
     *
     * @return List of tasks.
     */
    public List<Task> getCompletedTasks() {
        return taskDao.getPortletCompletedTasks(PortletHelper.getRequest().getWindowID());
    }



    /**
     * Get all tasks from GTD group TODAY from current portlet.
     * Returns all task from today and older.
     *
     * @return List of tasks.
     */
    public List<Task> getUncompletedPastTasks() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(java.util.Calendar.DATE, -1);
        return taskDao.getPortletScheduledTasksTo(cal.getTime(), PortletHelper.getRequest().getWindowID());
    }



    /**
     * Get all tasks from GTD group TODAY from current portlet.
     *
     * @param isAllDay If value is true it returns only all-day tasks. Otherwise only timed tasks.
     * @return List of tasks.
     */
    public List<Task> getTodayAllDayTasks(boolean isAllDay) {
        List<Task> tasks = new ArrayList<Task>();
        for(Task t : getTodayTasks()) {
            if (isAllDay && t.isAllDayTask()) {
                tasks.add(t);
            }
            else if (!isAllDay && !t.isAllDayTask()) {
                tasks.add(t);
            }
        }
        return tasks;
    }



    /**
     * Returns tasks which will happen after tomorrow.
     *
     * @return List of tasks.
     */
    public List<Task> getScheduledTasks() {
        // Add 2 days
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(java.util.Calendar.DATE, 2);

        return taskDao.getPortletScheduledTasksFrom(cal.getTime(), PortletHelper.getRequest().getWindowID());
    }



    /**
     * Get all tasks from GTD group TOMORROW from current portlet.
     *
     * @return List of tasks.
     */
    public List<Task> getTomorrowTasks() {
        // Add 1 day
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(java.util.Calendar.DATE, 1);

        return taskDao.getPortletScheduledTasks(cal.getTime(), PortletHelper.getRequest().getWindowID());
    }


    
    /**
     * Workaround for: LazyInitializationException: could not initialize proxy - no Session
     *
     * @return List of task's contexts
     */
    public List<Context> getTaskContexts() {
        return contextDao.getContexts(getTask());
    }



    /**
     * Workaround for: LazyInitializationException: could not initialize proxy - no Session
     *
     * @param List of contexts for current task.
     */
    public void setTaskContexts(List<Context> contexts) {
        getTask().setContexts(new HashSet<Context>(contexts));
    }
    
    
    /**
     * Returns array of task types
     *
     * @return
     */
    public GTDGroupType[] getGTDGroupTypes() {
        return GTDGroupType.values();
    }


    /**
     * Creates new task.
     *
     * @return Status "success" if action was successful.
     */
    public String createTask() {
        getTask().setPortletInstanceId(PortletHelper.getRequest().getWindowID());
        for(Context c : getTask().getContexts()) {
            c.getTasks().add(getTask());
        }
        taskDao.createTask(getTask());
        return "success";
    }



    /**
     * Edits task.
     *
     * @return Status "success" if action was successful.
     */
    public String editTask() {
        taskDao.editTask(getTask());
        return "success";
    }



    /**
     * Deletes task
     *
     * @return Status "success" if action was successful.
     */
    public String deleteTask() {

        for (Context c : getTask().getContexts()) {
            c.getTasks().remove(getTask());
        }
        taskDao.deleteTask(getTask());
        return "success";
    }



    /**
     * Sets task completed.

     */
    public void setCompletedTask(boolean isCompleted) {
        getTask().setCompleted(isCompleted);
        taskDao.editTask(getTask());
    }
    
    
    
    /**
     * Listener for uploading of file containing iCalendar.
     * Method creates new tasks.
     *
     * @param event
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


            for(Object obj : vcalendar.getComponents("VTODO"))
            {
                if (obj instanceof VToDo) {
                    VToDo vtodo = (VToDo) obj;
                    
                    // Check if current portlet contains task with the same global vtodoUid. If so, continue...
                    if (taskDao.getByVToDoUid(vtodo.getUid().getValue(), PortletHelper.getRequest().getWindowID()) != null) {
                        continue;
                    }
                    
                    Task task = new Task(vtodo);
                    task.setPortletInstanceId(PortletHelper.getRequest().getWindowID());
                    
                    if (vtodo.getProperties().getProperty(net.fortuna.ical4j.model.property.Categories.CATEGORIES) != null) {
                        Categories catgs = (Categories) vtodo.getProperties().getProperty(net.fortuna.ical4j.model.property.Categories.CATEGORIES);
                        
                        @SuppressWarnings("unchecked")
                        Iterator<String> it = catgs.getCategories().iterator();
                        while (it.hasNext()) {
                            Context c = null;
                            String name = it.next();
                            if ((c = contextDao.getByName(name, PortletHelper.getRequest().getWindowID())) != null) {
                            } else {
                                c = new Context();
                                c.setName(name);
                                c.setPortletInstanceId(PortletHelper.getRequest().getWindowID());
                                contextDao.createContext(c);
                            }                            
                            c.getTasks().add(task);
                            task.getContexts().add(c);
                        }
                    }
                    taskDao.createTask(task);
                }
            }
            
            // Log imported VCalendar
            logger.log(Level.INFO, "VCalendar "
                    + " from file " + file.getName()
                    + " was successfully imported!");

        }catch(ParserException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }
        catch(IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }


    }
    
    
    
    /**
     * This method returns full URL to iCalendar file.
     *
     * @param contact group
     * @return URL to vCard file
     */
    public String getTasksShareURL() {
        PortletRequest portletRequest = PortletHelper.getRequest();
        return "http://" + portletRequest.getServerName() + ":" +
            + portletRequest.getServerPort()
            + portletRequest.getContextPath()
            + "/rest/tasks/ical/" + portletRequest.getWindowID();
    }


}
