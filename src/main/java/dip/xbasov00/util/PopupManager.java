/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : PopupManager.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.util;


/**
 * Class PopupManager ensures dynamic changing of content of main popup panel.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */

public class PopupManager {

    
    /**
     * Name of view
     */
    protected String viewKeyName;
    
    
    /**
     * Name of title
     */
    protected String titleKeyName;
    
    
    /**
     * Set view.
     * 
     * @param view
     */
    public void setView(String view) {
        PortletHelper.setPortletSessionAttribute(viewKeyName, view);
    }
    
    
    
    /**
     * Get view.
     * 
     * @return view
     */
    public String getView() {       
        Object obj = PortletHelper.getPortletSessionAttribute(viewKeyName, String.class);
        return (obj instanceof String) ? (String) obj : null;

    }
    
    
    /**
     * Set title.
     * 
     * @param title
     */
    public void setTitle(String title) {
        PortletHelper.setPortletSessionAttribute(titleKeyName, title);
    }
    
    
    
    /**
     * Get title.
     * 
     * @return title
     */
    public String getTitle() {       
        Object obj = PortletHelper.getPortletSessionAttribute(titleKeyName, String.class);
        return (obj instanceof String) ? (String) obj : null;
    }
    
    
    /**
     * Set view and title
     * 
     * @param view
     * @param title
     */
    public void setPopup(String view, String title) {
        setView(view);
        setTitle(title);
    }
    
}
