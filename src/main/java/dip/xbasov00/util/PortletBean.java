/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : PortletBean.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.util;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.portlet.PortletRequest;

/**
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Named
@RequestScoped
public class PortletBean {

    /**
     * Method returns portlet window id.
     * Due to the fact that portlet-bridge EL variables don't have to contain this needed value always
     * we have to ensure portlet window id by this method.
     * @see https://docs.jboss.org/author/display/PBR/Provided+EL+Variables
     * @return Portlet window id.
     */
    public String getPortletWindowId() {
        PortletRequest portletRequest = (PortletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return portletRequest.getWindowID();
    }
}
