/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : RemoteCalendar.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.domain;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import lombok.Getter;
import lombok.Setter;

import org.hibernate.validator.constraints.URL;


/**
 * Class RemoteCalendar represents remote calendar entity.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
abstract public class RemoteCalendar extends Calendar {

    private static final long serialVersionUID = 1058117410260588030L;

    /**
     * URL of remote iCalendar file.
     */
    @URL
    @Getter @Setter
    private String url;

    public RemoteCalendar() {
        super();
        this.url="http://";
    }

    public RemoteCalendar(Calendar calendar, String url) {
        super(calendar.getId(), calendar.getName(), calendar.getDescription(), calendar.getCalendarComponents());
        this.url = url;
    }

    public RemoteCalendar(RemoteCalendar calendar) {
        super(calendar);
        this.url = calendar.url;
    }


    public RemoteCalendar(net.fortuna.ical4j.model.Calendar vcalendar) {
        super(vcalendar);
    }


    public boolean sourceEquals(Calendar cal) {
        if (this == cal)
            return true;
        if (cal == null)
            return false;
        if (getClass() != cal.getClass())
            return false;
        if (!(cal instanceof RemoteCalendar))
            return false;
        RemoteCalendar remoteCal = (RemoteCalendar) cal;
        if (url == null) {
            if (remoteCal.url != null)
                return false;
        } else if (!url.equals(remoteCal.url))
            return false;
        return true;

    }
    
    /**
     * Update calendar properties
     * @param eventSrc Newer version of object.
     */
    public void updateBy(RemoteCalendar calendar) {
        super.updateBy(calendar);
        this.url = calendar.url;
    }

}
