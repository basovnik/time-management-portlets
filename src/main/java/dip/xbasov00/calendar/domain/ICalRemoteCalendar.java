/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : ICalRemoteCalendar.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.domain;

import javax.persistence.Entity;


/**
 * Class ICalRemoteCalendar represents remote calendar on specific URL. The URL constains
 * file in format "text/calendar". ICalRemoteCalendar is only for reading because this remote
 * file cannot be edited.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */

@Entity
public class ICalRemoteCalendar extends RemoteCalendar {


    private static final long serialVersionUID = 120092580372963070L;

    public ICalRemoteCalendar() {
        super();
    }

    public ICalRemoteCalendar(Calendar calendar, String url) {
        super(calendar, url);
    }

    public ICalRemoteCalendar(ICalRemoteCalendar calendar) {
        super(calendar);
    }

    public ICalRemoteCalendar(net.fortuna.ical4j.model.Calendar vcalendar) {
        super(vcalendar);
    }

}
