/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : ProjectConverter.java
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

import dip.xbasov00.tasks.domain.Project;

/**
 * Class ProjectConverter ensures conversion between string and object
 * representation of project entity.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@FacesConverter(value="projectConverter", forClass=dip.xbasov00.tasks.domain.Project.class)
public class ProjectConverter implements Converter {

    private ProjectDAO projectDao;

    /**
     * ProjectConverter constructor.
     * EJB components is not possible inject by annotation (bug)
     * so it is possible to retrieve it manually by it's EJB JNDI resource name.
     * @see <a href="http://docs.oracle.com/javaee/6/tutorial/doc/gipjf.html">Accessing Enterprise Beans</a>
     */
    public ProjectConverter() {
        super();
        try {
            InitialContext ic = new InitialContext();
            projectDao = (ProjectDAO) ic.lookup("java:module/EJBProjectDAO");
           } catch (NamingException e) {
            e.printStackTrace();
       }
    }



    /**
     * Transform string value to calendar object.
     * @return Project object.
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
        String value) {

        if (value == null || value.isEmpty()) {
            return null;
        }
        return projectDao.getProject(Integer.parseInt(value));
    }



    /**
     * Transform project object to string value.
     * @return String representation (id) of project object.
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {
        if (value == null || (value instanceof String && ((String) value).isEmpty())) {
            return "";
        }
        return String.valueOf(((Project) value).getId());

    }
}
