/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : PopupManagerTasksBean.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.tasks.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import dip.xbasov00.util.PopupManager;



/**
 * Class PopupManagerBean ensures dynamic changing of content of main popup panel.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Named
@SessionScoped
public class PopupManagerTasksBean extends PopupManager implements Serializable {

    private static final long serialVersionUID = 6372921109397975775L;
    
    
    /**
     * Attribute key in portlet specific session representing view.
     */
    private static final String ATTR_VIEW = "PopupManagerTasksBean:view";
    
    
    /**
     * Attribute key in portlet specific session representing title.
     */
    private static final String ATTR_TITLE = "PopupManagerTasksBean:title";

    
    @PostConstruct
    public void init() {
        super.titleKeyName = ATTR_TITLE;
        super.viewKeyName = ATTR_VIEW;
    }
      
}
