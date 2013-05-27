/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : PortletHelper.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.util;

import javax.faces.context.FacesContext;
import javax.inject.Singleton;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletSession;

import org.exoplatform.services.security.ConversationState;
import org.gatein.wci.security.Credentials;

/**
 * Class PortletHelper is used for some often used portlet operations.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Singleton
public class PortletHelper {

    /**
     * Get portlet request.
     *
     * @return portlet request
     */
    public static PortletRequest getRequest() {
        return (PortletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    }

    /**
     * Get portlet response.
     *
     * @return portlet response
     */
    public static PortletResponse getResponse() {
        return (PortletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
    }


    /**
     * Get user credentials to obtain username and password.
     *
     * @return user credentials
     */
    public static Credentials getUserCredentials() {
        return (Credentials) ConversationState.getCurrent().getAttribute(Credentials.CREDENTIALS);
    }
    
    
    
    
    /**
     * Get portlet session attribute.
     * 
     * @param name Name of attribute
     * @return
     */
    public static Object getPortletSessionAttribute(String attrName, Class<?> className) {
        
        Object objSession = FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        if (objSession instanceof PortletSession) 
        {
            PortletSession portalSession = (PortletSession) objSession;
            Object o = portalSession.getAttribute(attrName, PortletSession.PORTLET_SCOPE);
            if (className.isInstance(o)) {
                return className.cast(o);
            }
        }        
        return null;
    }
    
    
    
    
    /**
     * Set portlet session attribute.
     * 
     * @param attrName Attribute name
     * @param object Object name
     */
    public static void setPortletSessionAttribute(String attrName, Object object) {
        
        Object objSession = FacesContext.getCurrentInstance().getExternalContext().getSession(false);

        if (objSession instanceof PortletSession) {
            PortletSession portalSession = (PortletSession) objSession;
            portalSession.setAttribute(attrName, object, PortletSession.PORTLET_SCOPE);
        }
    }
}
