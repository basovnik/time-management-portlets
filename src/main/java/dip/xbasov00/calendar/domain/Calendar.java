/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : Calendar.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.component.VToDo;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.XProperty;
import dip.xbasov00.calendar.util.CalendarException;
import dip.xbasov00.calendar.util.CalendarSettingsSingleton;

/**
 * Class Calendar represents calendar entity.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */

@Entity
@Table(name = "Calendar")
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class Calendar implements Serializable {

    private static final long serialVersionUID = -4835737116973300114L;

    @Id
    @GeneratedValue
    @Column(name = "calendar_id")
    @Getter @Setter
    private int id;

    @Column(unique=true, nullable=false, name="private_id")
    @Getter @Setter
    private String privateId;


    @Column(name="portlet_instance_id")
    @Getter @Setter
    private String portletInstanceId;


    @Getter @Setter
    private String name;

    @Column(length=1000) 
    @Getter @Setter
    private String description;


    @OneToMany(mappedBy = "calendar", fetch = FetchType.EAGER, orphanRemoval=true)
    @Getter
    private List<CalendarComponent> calendarComponents;



    public Calendar() {
        generatePrivateId();
        this.calendarComponents = new ArrayList<CalendarComponent>();
    }


    public Calendar(int id, String name, String description, List<CalendarComponent> calendarComponents) {
        this();
        this.id = id;
        this.name = name;
        this.description = description;
        this.calendarComponents = calendarComponents;
    }


    public Calendar(Calendar calendar) {
        this();
        this.id = calendar.id;
        this.name = calendar.name;
        this.description = calendar.description;
        for(CalendarComponent cc : calendar.getCalendarComponents()) {
            this.addCalendarComponent(cc.copy());
        }

    }

    /**
     * This constructor creates new calendar from calendar in format vcalendar.
     * @param vcalendar
     */
    public Calendar(net.fortuna.ical4j.model.Calendar vcalendar) {
        this();
        Property tmpProp = vcalendar.getProperty(net.fortuna.ical4j.extensions.property.WrCalName.PROPERTY_NAME);
        this.name = (tmpProp != null) ? tmpProp.getValue() : java.util.Calendar.getInstance().getTime() + "_Calendar";

        tmpProp = vcalendar.getProperty(net.fortuna.ical4j.extensions.property.WrCalDesc.PROPERTY_NAME);
        this.description = (tmpProp != null) ? tmpProp.getValue() : null;

        for(Object obj : vcalendar.getComponents())
        {
            // EVENTS
            if (obj instanceof VEvent) {
                VEvent vevent = (VEvent) obj;
                calendarComponents.add(new Event(vevent));
            }
            // TODOS
            // Save only todos with scheduledDate
            else if (obj instanceof VToDo) {
                VToDo vtodo= (VToDo) obj;
                ToDo todo = new ToDo(vtodo);
                if (todo.getDatetimeScheduled() != null) {
                    calendarComponents.add(todo);
                }
            }
        }
    }



    /**
     * Generates private id to new random one.
     */
    public void generatePrivateId() {
        this.privateId = UUID.randomUUID().toString();
    }

    
    /**
     * Sets calendarComponents to this calendar and sets this calendar as their parent.
     * @param calendarComponents
     */
    public void setCalendarComponents(List<CalendarComponent> calendarComponents) {
        for(CalendarComponent cc : calendarComponents) {
            cc.setCalendar(this);
        }
        this.calendarComponents = calendarComponents;
    }


    
    /**
     * Adds calendar component to the calendar and sets this calendar as its parent.
     * @param calendarComponent CalendarComponent
     */
    public void addCalendarComponent(CalendarComponent calendarComponent) {
        calendarComponent.setCalendar(this);
        this.calendarComponents.add(calendarComponent);
    }



    /**
     * Method returns vcalendar representation of this calendar.
     * Supported attributes and components: PRODID, VERSION, CALSCALE, X-WR-CALNAME, X-WR-CALDESC,
     * X-WR-RELCALID, VEVENT, VTODO...
     * @return vcalendar representation of this calendar.
     * @throws CalendarException
     */
    public net.fortuna.ical4j.model.Calendar getVCalendar() throws CalendarException {

        // Create VCalendar
        net.fortuna.ical4j.model.Calendar calendar = new net.fortuna.ical4j.model.Calendar();

        // Add PRODID
        calendar.getProperties().add(new ProdId(CalendarSettingsSingleton.prodId));

        // Add VERSION
        calendar.getProperties().add(CalendarSettingsSingleton.version);

        // Add CALSCALE (e.g. GREGORIAN)
        calendar.getProperties().add(CalendarSettingsSingleton.calScale);

        // EXTRA ATTRIBUTES:

        // Add X-WR-CALNAME (calendar name)
        calendar.getProperties().add(new XProperty(
                net.fortuna.ical4j.extensions.property.WrCalName.PROPERTY_NAME,
                name)
        );

        // Add X-WR-CALDESC (calendar description)
        calendar.getProperties().add(new XProperty(
                net.fortuna.ical4j.extensions.property.WrCalDesc.PROPERTY_NAME,
                description)
        );

        // Add X-WR-RELCALID (universally unique identifier e.g. 3E26604A-50F4-4449-8B3E-E4F4932D05B5)
        calendar.getProperties().add(new XProperty(
                net.fortuna.ical4j.extensions.property.WrRelCalId.PROPERTY_NAME,
                (id + "@" + CalendarSettingsSingleton.hostName))
        );


        // Add Calendar Components
        for(CalendarComponent cc : calendarComponents) {
            calendar.getComponents().add(cc.getVCalendarComponent(CalendarSettingsSingleton.hostName));
        }

        return calendar;
    }





    /**
     * Checks if calendars have the same source
     * @param cal Calendar
     * @return Boolean value.
     */
    public boolean sourceEquals(Calendar cal) {
        if (this == cal)
            return true;
        if (cal == null)
            return false;
        if (getClass() != cal.getClass())
            return false;
        if (id != cal.id)
            return false;
        return true;
    }
    
    
    
    /**
     * Update calendar properties
     * @param calendar Newer version of object.
     */
    public void updateBy(Calendar calendar) {
        this.name = calendar.name;
        this.description = calendar.description;
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((description == null) ? 0 : description.hashCode());
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime
                * result
                + ((portletInstanceId == null) ? 0 : portletInstanceId
                        .hashCode());
        result = prime * result
                + ((privateId == null) ? 0 : privateId.hashCode());
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
        Calendar other = (Calendar) obj;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (portletInstanceId == null) {
            if (other.portletInstanceId != null)
                return false;
        } else if (!portletInstanceId.equals(other.portletInstanceId))
            return false;
        if (privateId == null) {
            if (other.privateId != null)
                return false;
        } else if (!privateId.equals(other.privateId))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return "Calendar [id=" + id + ", privateId=" + privateId
                + ", portletInstanceId=" + portletInstanceId + ", name=" + name
                + ", description=" + description + ", calendarComponents="
                + calendarComponents + "]";
    }

}
