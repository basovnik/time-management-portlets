/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : ContextConverter.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.tasks.dao;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import dip.xbasov00.tasks.domain.Context;

/**
 * Class ContextConverter ensures conversion between string and object
 * representation of context entity.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@FacesConverter(value="contextConverter", forClass=dip.xbasov00.tasks.domain.Context.class)
public class ContextConverter implements Converter {

    private ContextDAO contextDao;

    /**
     * ContextConverter constructor.
     * EJB components is not possible inject by annotation (bug)
     * so it is possible to retrieve it manually by it's EJB JNDI resource name.
     * @see <a href="http://docs.oracle.com/javaee/6/tutorial/doc/gipjf.html">Accessing Enterprise Beans</a>
     */
    public ContextConverter() {
        super();
        try {
            InitialContext ic = new InitialContext();
            contextDao = (ContextDAO) ic.lookup("java:module/EJBContextDAO");
           } catch (NamingException e) {
            e.printStackTrace();
       }
    }



    /**
     * Transform string value to context object.
     * @return Context object.
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
        String value) {

        if (value == null || value.isEmpty()) {
            return null;
        }
        return contextDao.getContext(Integer.parseInt(value));
    }



    /**
     * Transform context object to string value.
     * @return String representation (id) of context object.
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {
        if (value == null || (value instanceof String && ((String) value).isEmpty())) {
            return "";
        }
        return String.valueOf(((Context) value).getId());

    }
}
