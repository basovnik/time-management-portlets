/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : ContactGroupConverter.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.contacts.dao;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import dip.xbasov00.contacts.domain.ContactGroup;

/**
 * Class ContactGroupConverter ensures conversion between string and object
 * representation of contact group entity.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@FacesConverter(value="contactGroupConverter", forClass=dip.xbasov00.contacts.domain.ContactGroup.class)
public class ContactGroupConverter implements Converter {

    private ContactGroupDAO contactGroupDao;

    /**
     * ContactGroupConverter constructor.
     * EJB components is not possible inject by annotation (bug)
     * so it is possible to retrieve it manually by it's EJB JNDI resource name.
     * @see <a href="http://docs.oracle.com/javaee/6/tutorial/doc/gipjf.html">Accessing Enterprise Beans</a>
     */
    public ContactGroupConverter() {
        super();
        try {
            InitialContext ic = new InitialContext();
            contactGroupDao = (ContactGroupDAO) ic.lookup("java:module/EJBContactGroupDAO");
           } catch (NamingException e) {
            e.printStackTrace();
       }
    }



    /**
     * Transform string value to contact group object.
     * @return Lang object.
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
        String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        ContactGroup cg = contactGroupDao.getContactGroup(Integer.parseInt(value));
        return cg;
    }



    /**
     * Transform contact group object to string value.
     * @return String representation (id) of contact group object.
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        if (value == null || (value instanceof String && ((String) value).isEmpty())) {
            return "";
        }
        return String.valueOf(((ContactGroup) value).getId());

    }
}
