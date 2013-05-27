/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : ContextController.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.tasks.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dip.xbasov00.tasks.dao.ContextDAO;
import dip.xbasov00.tasks.domain.Context;
import dip.xbasov00.util.PortletHelper;


/**
 * Class ContextController is used to manipulated with entity Context.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Named
@SessionScoped
public class ContextController implements Serializable{

    private static final long serialVersionUID = -7860172555761469548L;

    /**
     * Attribute key in portlet specific session representing selected context.
     */
    private static final String ATTR_CONTEXT = "ContextController:context";

    
    @Inject
    private ContextDAO contextDao;   
   
    

    /**
     * Set context.
     * 
     * @param context
     */
    public void setContext(Context context) {
        PortletHelper.setPortletSessionAttribute(ATTR_CONTEXT, context);
    }
    
    
    
    /**
     * Get context.
     * 
     * @return
     */
    public Context getContext() {       
        Object obj = PortletHelper.getPortletSessionAttribute(ATTR_CONTEXT, Context.class);
        if (obj instanceof Context) {
            return (Context) obj;
        }
        else {
            initContext();
            return getContext();
        }
    }
    
    
    
    /**
     * Refreshing of selected context.
     */
    public void refreshContext() {
        if (getContext() != null) {
            setContext(contextDao.getContext(getContext().getId()));
        }
    }



    /**
     * Bean initialization.
     */
    @PostConstruct
    public void init() {
        initContext();
    }

    
    
    /**
     * Initialize context.
     */
    public void initContext() {
        setContext(new Context());     
    }




    /**
     * Get all contexts from current portlet.
     *
     * @return List of contexts.
     */
    public List<Context> getAllContexts() {
        return contextDao.getPortletContexts(PortletHelper.getRequest().getWindowID());
    }





    /**
     * Creates new context.
     *
     * @return Status "success" if action was successful.
     */
    public String createContext() {
        getContext().setPortletInstanceId(PortletHelper.getRequest().getWindowID());
        contextDao.createContext(getContext());
        return "success";
    }



    /**
     * Edits context.
     *
     * @return Status "success" if action was successful.
     */
    public String editContext() {
        contextDao.editContext(getContext());
        return "success";
    }



    /**
     * Deletes context
     *
     * @return Status "success" if action was successful.
     */
    public String deleteContext() {
        return deleteContext(getContext());
    }



    /**
     * Deletes context
     *
     * @param context
     * @return Status "success" if action was successful.
     */
    public String deleteContext(Context context) {
        contextDao.deleteContext(context);
        return "success";
    }



}
