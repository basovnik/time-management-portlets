/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : TaskDAO.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */
package dip.xbasov00.tasks.dao;

import java.util.Date;
import java.util.List;

import dip.xbasov00.tasks.domain.GTDGroupType;
import dip.xbasov00.tasks.domain.Task;


/**
 * Interface TaskDAO
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
public interface TaskDAO {

    /**
     * Get all tasks.
     *
     * @return List of tasks.
     */
    List<Task> getAll();



    /**
     * Get all tasks from specified portlet instance.
     *
     * @param portletID
     * @return List of tasks.
     */
    List<Task> getPortletTasks(String portletID);



    /**
     * Get all tasks from specified GTD group type and portlet instance.
     *
     * @param inbox
     * @param portletID
     * @return List of tasks.
     */
    List<Task> getPortletTasks(GTDGroupType inbox, String portletID);



    /**
     * Get all completed tasks.
     *
     * @param portletID
     * @return List of tasks.
     */
    List<Task> getPortletCompletedTasks(String portletID);



    /**
     * Get all scheduled tasks from specified date and portlet instance.
     *
     * @param date
     * @param portletID
     * @return List of tasks.
     */
    List<Task> getPortletScheduledTasks(Date date, String portletID);



    /**
     * Get all scheduled tasks greater or equal then specified date with specified portlet instance.
     *
     * @param date
     * @param portletID
     * @return List of tasks.
     */
    List<Task> getPortletScheduledTasksFrom(Date time, String portletID);



    /**
     * Get all scheduled tasks less or equal then specified date with specified portlet instance.
     *
     * @param date
     * @param portletID
     * @return List of tasks.
     */
    List<Task> getPortletScheduledTasksTo(Date time, String portletID);



    /**
     * Get task group with specified id.
     *
     * @param id Task id.
     * @return Task with specified id.
     */
    Task getTask(int id);



    /**
     * Create task
     * 
     * @param task
     */
    void createTask(Task task);



    /**
     * Edit task
     * 
     * @param task
     */
    void editTask(Task task);



    /**
     * Delete task
     * 
     * @param task
     */
    void deleteTask(Task task);


    
    /**
     * Get task by its vtodoUid
     * 
     * @param vtodoUid
     * @param portletId
     * @return Task
     */
    Task getByVToDoUid(String vtodoUid, String portletId);


}
