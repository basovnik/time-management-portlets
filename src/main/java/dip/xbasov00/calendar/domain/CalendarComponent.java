/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : CalendarComponent.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import dip.xbasov00.calendar.util.CalendarException;

@Entity
@Inheritance(strategy=InheritanceType.TABLE_PER_CLASS)
abstract public class CalendarComponent implements Serializable, Comparable<CalendarComponent> {
    
    private static final long serialVersionUID = 2563133733344566413L;
    
    
    @Id
    @GeneratedValue(strategy=GenerationType.TABLE)
    @Getter @Setter
    protected int id;
    
    
    
    @Column(name = "ical_comp_uid")
    @Getter @Setter
    protected String iCalCompUID;
    
    
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "calendar_id")
    @Getter @Setter
    protected Calendar calendar;
    
    
    
    public CalendarComponent() { }
    
    
    
    public CalendarComponent(int id, String iCalCompUID, Calendar calendar) {
        this.id = id;
        this.iCalCompUID = iCalCompUID;
        this.calendar = calendar;
    }
    
    
    
    public CalendarComponent copy() {
        if (this instanceof Event) {
            return new Event((Event) this);
        }
        else if (this instanceof ToDo) {
            return new ToDo((ToDo) this);
        }
        else {
            return null;
        }
    }
    
    
    /**
     * Returns calendar component
     * @param hostName
     * @return Returns calendar component
     * @throws CalendarException
     */
    abstract public net.fortuna.ical4j.model.component.CalendarComponent getVCalendarComponent(String hostName) throws CalendarException;

    
    
    /**
     * Returns date to compare two calendar components.
     * @return
     */
    abstract public Date getDateToCompare();
    
    
    
    /**
     * Returns formatted date
     * @param selectedDate
     * @return
     */
    abstract public String getFormattedDate(Date selectedDate);

    
    
    /**
     * Checks if calendar component is editable.
     * Calendar component is editable if it is local or if exists interface to modify it.
     * Calendars of type ICalRemoteCalendar do not have this interface.
     *
     * @return Is calendar component editable?
     */
    public boolean isEditable() {
        return (!(calendar instanceof ICalRemoteCalendar));
    }
    
    
    
    /**
     * Compares CalendarComponents according to return value of method getDateToCompare.
     * @param Event to compare.
     */
    @Override
    public int compareTo(CalendarComponent calendarComponent) {
        return (int) (this.getDateToCompare().getTime() - calendarComponent.getDateToCompare().getTime());
    }
    
    
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((iCalCompUID == null) ? 0 : iCalCompUID.hashCode());
        result = prime * result + id;
        return result;
    }

    
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CalendarComponent other = (CalendarComponent) obj;
        if (iCalCompUID == null) {
            if (other.iCalCompUID != null)
                return false;
        } else if (!iCalCompUID.equals(other.iCalCompUID))
            return false;
        if (id != other.id)
            return false;
        return true;
    }
    
}
