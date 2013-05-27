/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : ToDo.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.VToDo;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Locality;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.util.SimpleHostInfo;
import net.fortuna.ical4j.util.UidGenerator;
import dip.xbasov00.calendar.util.CalendarException;
import dip.xbasov00.util.Resources;

@Entity
@Table(name = "ToDo")
public class ToDo extends CalendarComponent implements Serializable{


    private static final long serialVersionUID = 2672604417141223808L;
    
 
    @Column(length=255, nullable=false)
    @Getter
    private String name;


    @Column(nullable=true, length=1000)
    @Getter
    private String description;
    
    
    @Column(nullable=true,length=255)
    @Getter
    private String location;
    
    
    /**
     * Scheduled datetime
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datetime_scheduled")
    @Getter @Setter
    private Date datetimeScheduled;
    
    
    @Column(name = "all_day_todo")
    @Getter
    private boolean allDayToDo;
    
    
    public ToDo() { }
    
    
    
    public ToDo(int id, String name, String description, String location, Date datetimeScheduled, boolean allDayToDo, Calendar calendar, String vCalendarCompUID) {
        super(id, vCalendarCompUID, calendar);
        this.name = name;
        this.description = description;
        this.location = location;
        this.datetimeScheduled = datetimeScheduled;
        this.allDayToDo = allDayToDo;
    }
    
    
    
    public ToDo(ToDo toDo) {
        this(toDo.id, toDo.name, toDo.description, toDo.location, toDo.datetimeScheduled, toDo.allDayToDo, toDo.calendar, toDo.iCalCompUID);
    }
       
    
    
    public ToDo(VToDo vtodo) {

        this.name = vtodo.getSummary().getValue();
        
        Property tmpProp = vtodo.getDescription();
        this.description = (tmpProp == null) ? null : tmpProp.getValue();

        tmpProp = vtodo.getLocation();
        this.location = (tmpProp == null) ? null : tmpProp.getValue();
        
        
        if (vtodo.getStartDate() != null) {
            this.datetimeScheduled = vtodo.getStartDate().getDate();
            this.allDayToDo = !(vtodo.getStartDate().getDate() instanceof net.fortuna.ical4j.model.DateTime);
        }        
    
        this.iCalCompUID = vtodo.getUid().getValue();
        
    }


    @Override
    public net.fortuna.ical4j.model.component.CalendarComponent getVCalendarComponent(
            String hostName) throws CalendarException {
        
        // Create VTodo
        VToDo vtodo = new VToDo(new PropertyList());


        // Add DTSTART
        if (this.datetimeScheduled != null) {
            vtodo.getProperties().add(new DtStart(new net.fortuna.ical4j.model.DateTime(this.datetimeScheduled)));
        }


        // Add SUMMARY
        vtodo.getProperties().add(new Summary(this.name));

        // Add UID
        if (iCalCompUID == null) {
            UidGenerator ug = new UidGenerator(new SimpleHostInfo(hostName), Integer.toString(id));
            vtodo.getProperties().add(ug.generateUid());
        }
        else { // Todo of remote calendar - original uid
            vtodo.getProperties().add(new Uid(iCalCompUID));
        }

        // Add DESCRIPTION
        if (description != null) {
            vtodo.getProperties().add(new Description(description));
        }
        
        // Add LOCALITY
        if (location != null) {
            vtodo.getProperties().add(new Locality(location));
        }

        return vtodo;
    }



    public void updateBy(ToDo todo) {
        if (todo.iCalCompUID != null && iCalCompUID.equals(todo.iCalCompUID)) {
            this.name = todo.name;
            this.location = todo.location;
            this.datetimeScheduled = todo.datetimeScheduled;
            this.allDayToDo = todo.allDayToDo;
            this.description = todo.description;
        }
        
    }



    @Override
    public Date getDateToCompare() {
        return this.datetimeScheduled;
    }



    @Override
    public String getFormattedDate(Date selectedDate) {
        if (isAllDayToDo()) {
            return Resources.getBundle().getString("AllDayToDo");
        }

        SimpleDateFormat hourFormatter = new SimpleDateFormat("HH:mm");
        return hourFormatter.format(datetimeScheduled);       
    }
}
