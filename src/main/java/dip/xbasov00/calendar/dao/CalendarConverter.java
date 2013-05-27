/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : CalendarConverter.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.dao;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import dip.xbasov00.calendar.domain.Calendar;

/**
 * Class CalendarConverter ensures conversion between string and object
 * representation of calendar entity.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@FacesConverter(value="calendarConverter", forClass=dip.xbasov00.calendar.domain.Calendar.class)
public class CalendarConverter implements Converter {

    private CalendarDAO calendarDao;

    /**
     * CalendarConverter constructor.
     * EJB components is not possible inject by annotation (bug)
     * so it is possible to retrieve it manually by it's EJB JNDI resource name.
     * @see <a href="http://docs.oracle.com/javaee/6/tutorial/doc/gipjf.html">Accessing Enterprise Beans</a>
     */
    public CalendarConverter() {
        super();
        try {
            InitialContext ic = new InitialContext();
            calendarDao = (CalendarDAO) ic.lookup("java:module/EJBCalendarDAO");
           } catch (NamingException e) {
            e.printStackTrace();
       }
    }



    /**
     * Transform string value to calendar object.
     * @return Calendar object.
     */
    @Override
    public Object getAsObject(FacesContext context, UIComponent component,
        String value) {

        Calendar c = calendarDao.getCalendar(Integer.parseInt(value));
        return c;
    }



    /**
     * Transform calendar object to string value.
     * @return String representation (id) of calendar object.
     */
    @Override
    public String getAsString(FacesContext context, UIComponent component,
            Object value) {

        return String.valueOf(((Calendar) value).getId());

    }
}
