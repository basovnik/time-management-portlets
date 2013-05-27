/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : Resources.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.util;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Class Resources represents some injectable resources.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@ApplicationScoped
public class Resources {

    @SuppressWarnings("unused")
    @PersistenceContext(unitName = "primary")
    @Produces
    private EntityManager em;

    @Produces
    Logger getLogger(InjectionPoint ip) {
        String category = ip.getMember().getDeclaringClass().getName();
        return Logger.getLogger(category);
    }


    static public ResourceBundle getBundle() {
        String basename = "dip/xbasov00/text";
        Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
        return ResourceBundle.getBundle(basename, locale);
    }

}
