/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : GTDGroupController.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.tasks.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import dip.xbasov00.util.PortletHelper;

@Named
@SessionScoped
public class GTDGroupController implements Serializable {

    private static final long serialVersionUID = -491863308254739629L;

    private static final String GTD_SECTION_INBOX = "inbox";
    
    
    /**
     * Attribute key in portlet specific session representing GTD group.
     */
    private static final String ATTR_GTD_SECTION = "GTDGroupController:gtdSection";

    
    /**
     * Set GTD section.
     * 
     * @param gtdSection
     */
    public void setGtdSection(String gtdSection) {
        PortletHelper.setPortletSessionAttribute(ATTR_GTD_SECTION, gtdSection);
    }
    
    
    
    /**
     * Get GTD section.
     * 
     * @return
     */
    public String getGtdSection() {       
        Object obj = PortletHelper.getPortletSessionAttribute(ATTR_GTD_SECTION, String.class);
        if (obj instanceof String) {
            return (String) obj;
        }
        else {
            init();
            return getGtdSection();
        }

    }

    
    @PostConstruct
    public void init() {
        setGtdSection(GTD_SECTION_INBOX);
    }


}
