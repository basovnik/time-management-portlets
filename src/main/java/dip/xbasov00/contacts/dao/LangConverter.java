/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : LangConverter.java
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

import dip.xbasov00.contacts.domain.Lang;

/**
 * Class LangConverter ensures conversion between string and object
 * representation of lang entity.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@FacesConverter(value="langConverter", forClass=dip.xbasov00.contacts.domain.Lang.class)
public class LangConverter implements Converter {

    private LanguageDAO languageDao;

    /**
     * LangConverter constructor.
     * EJB components is not possible inject by annotation (bug)
     * so it is possible to retrieve it manually by it's EJB JNDI resource name.
     * @see <a href="http://docs.oracle.com/javaee/6/tutorial/doc/gipjf.html">Accessing Enterprise Beans</a>
     */
    public LangConverter() {
        super();
        try {
            InitialContext ic = new InitialContext();
            languageDao = (LanguageDAO) ic.lookup("java:module/EJBLanguageDAO");
           } catch (NamingException e) {
            e.printStackTrace();
       }
    }



    /**
     * Transform string value to lang object.
     * @return Lang object.
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
        String value) {

        Lang l = languageDao.getLanguage(Integer.parseInt(value));
        return l;
    }



    /**
     * Transform lang object to string value.
     * @return String representation (id) of lang object.
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        return String.valueOf(((Lang) value).getId());

    }
}
