/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : ContextDAO.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.tasks.dao;

import java.util.List;

import dip.xbasov00.tasks.domain.Context;
import dip.xbasov00.tasks.domain.Task;


/**
 * Interface ProjectDAO
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
public interface ContextDAO {

    /**
     * get all contexts
     * @return
     */
    List<Context> getAll();


    /**
     * Get context with specified id.
     *
     * @param id Context id.
     * @return Context with specified id.
     */
    Context getContext(int id);


    /**
     * Create context.
     *
     * @param context
     */
    void createContext(Context project);


    /**
     * Edit context.
     *
     * @param context
     */
    void editContext(Context project);


    /**
     * Delete context.
     *
     * @param context
     */
    void deleteContext(Context project);
    
    
    /**
     * Get all contexts from current portlet.
     *
     * @param portlet window id
     * @return List of contexts.
     */
    List<Context> getPortletContexts(String portletID);


    
    /**
     * Get contexts of task.
     * @param task
     * @return List of contexts.
     */
    List<Context> getContexts(Task task);


    
    /**
     * Get context by name
     * @param name
     * @param portlet window id
     * @return
     */
    Context getByName(String name, String portletId);


    void refreshContext(Context context);


}
