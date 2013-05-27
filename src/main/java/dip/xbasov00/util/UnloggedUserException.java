/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : UnloggedUserException.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.util;

import javax.portlet.PortletException;


/**
 * Exception represents state when user is not logged to portal.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
public class UnloggedUserException extends PortletException {

    private static final long serialVersionUID = 5134219689512943322L;

}
