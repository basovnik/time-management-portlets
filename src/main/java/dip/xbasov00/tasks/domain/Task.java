/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : Task.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.tasks.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;
import net.fortuna.ical4j.model.Property;
import net.fortuna.ical4j.model.PropertyList;
import net.fortuna.ical4j.model.component.VToDo;
import net.fortuna.ical4j.model.property.Categories;
import net.fortuna.ical4j.model.property.Completed;
import net.fortuna.ical4j.model.property.Description;
import net.fortuna.ical4j.model.property.DtStamp;
import net.fortuna.ical4j.model.property.DtStart;
import net.fortuna.ical4j.model.property.Due;
import net.fortuna.ical4j.model.property.Locality;
import net.fortuna.ical4j.model.property.Priority;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.property.Uid;
import net.fortuna.ical4j.util.SimpleHostInfo;
import net.fortuna.ical4j.util.UidGenerator;
import dip.xbasov00.tasks.util.TasksException;
import dip.xbasov00.util.CalendarHelper;
import dip.xbasov00.util.StringHelper;


/**
 * 
 * Class Task represents entity Task.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Entity
public class Task implements Serializable{

    private static final long serialVersionUID = -3622585966492456680L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Getter @Setter
    private int id;


    @Column(unique=true, nullable=false, name="private_id")
    @Getter @Setter
    private String privateId;


    @Column(name="portlet_instance_id")
    @Getter @Setter
    private String portletInstanceId;


    @Column(length=255, nullable=false)
    @Getter
    private String name;


    @Column(nullable=true, length=1000)
    @Getter
    private String description;
    
    
    @Column(length=255)
    @Getter
    private String locality;


    /**
     * Created datetime
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datetime_created", nullable=false)
    @Getter @Setter
    private Date datetimeCreated;


    /**
     * Scheduled datetime
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datetime_scheduled")
    @Getter @Setter
    private Date datetimeScheduled;


    @Column(name = "all_day_task")
    @Getter @Setter
    private boolean allDayTask;



    /**
     * Completion of task
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datetime_completed")
    @Getter @Setter
    private Date datetimeCompleted;


    /**
     * Deadline of task
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "deadline")
    @Getter @Setter
    private Date deadline;


    /**
     * Priority of task.
     * 0 = undefined
     * 1 = top priority
     * 9 = the lowest priority
     */
    @Max(9) @Min(0)
    @Getter @Setter
    private int priority;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id")
    @Getter @Setter
    private Project project;
    
    
    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="task_context",
        joinColumns=
            @JoinColumn(name="task_id", referencedColumnName="id"),
        inverseJoinColumns=
            @JoinColumn(name="context_id", referencedColumnName="id")
        )
    @Getter @Setter
    private Set<Context> contexts;
    

    @Enumerated(EnumType.STRING)
    @Column(name = "gtd_group", nullable=false)
    @Getter
    private GTDGroupType gtdGroup;



    /**
     * VtodoId represents UID of VTODO component in VCALENDAR.
     * This column do not have to be unique because we can share calendars
     * that share just right this event.
     */
    @Column(name = "vtodo_uid")
    @Getter @Setter
    private String vtodoUid;


    public Task() {
        generatePrivateId();
        this.priority = 5;
        this.gtdGroup = GTDGroupType.INBOX;
        this.allDayTask = true;
        this.contexts = new HashSet<Context>();
    }



    /**
     * Constructs task from object of type VToDo (component of iCalendar)
     * 
     * @param vtodo Task
     */
    public Task(VToDo vtodo) {
        this();
        
        this.name = vtodo.getSummary().getValue();
        
        Property tmpProp = vtodo.getDescription();
        this.description = (tmpProp == null) ? null : tmpProp.getValue();

        tmpProp = vtodo.getLocation();
        this.locality = (tmpProp == null) ? null : tmpProp.getValue();
        
        tmpProp = vtodo.getPriority();
        if (tmpProp != null) {
            this.priority = Integer.decode(tmpProp.getValue()).intValue();
        }
        
        if (vtodo.getStartDate() != null) {
            this.datetimeScheduled = vtodo.getStartDate().getDate();
            this.allDayTask = !(vtodo.getStartDate().getDate() instanceof net.fortuna.ical4j.model.DateTime);
            this.gtdGroup = GTDGroupType.SCHEDULED;
        }
        
        if (vtodo.getDateStamp() != null) {
            this.datetimeCreated = vtodo.getDateStamp().getDate();
        }
        
        if (vtodo.getDue() != null) {
            this.deadline = vtodo.getDue().getDate();
        }
        
        if (vtodo.getDateCompleted() != null) {
            this.datetimeCompleted = vtodo.getDateCompleted().getDate();
        }
    
        this.vtodoUid = vtodo.getUid().getValue();
        
    }



    /**
     * Generates private id to new random one.
     */
    public void generatePrivateId() {
        this.privateId = UUID.randomUUID().toString();
    }



    public void setName(String name) {
        this.name = StringHelper.trimOrNull(name);
    }


    public void setDescription(String description) {
        this.description = StringHelper.trimOrNull(description);
    }
    
    
    public void setLocality(String locality) {
        this.locality = StringHelper.trimOrNull(locality);
    }
    


    public void setGtdGroup(GTDGroupType gtdGroup) {
        this.gtdGroup = gtdGroup;
        if (gtdGroup.equals(GTDGroupType.SCHEDULED)) {
            this.datetimeScheduled = new Date();
        }
        else {
            this.datetimeScheduled = null;
        }
    }
    
    
    public List<Context> getContextsAsList() {
        return new ArrayList<Context>(contexts);
    }
    

    public boolean isCompleted() {
        return this.datetimeCompleted != null;
    }



    /**
     * Checks if task shall happen current day.
     * @return
     */
    public boolean isToday() {
        return CalendarHelper.isNthDayFromToday(this.datetimeScheduled, 0);
    }



    /**
     * Checks if task shall happen tomorrow.
     * @return
     */
    public boolean isTomorrow() {
        return CalendarHelper.isNthDayFromToday(this.datetimeScheduled, 1);
    }



    /**
     * Checks if task shall happen after tomorrow.
     * @return
     */
    public boolean isAfterTomorrow() {
        if (this.datetimeScheduled == null) {
            return false;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        CalendarHelper.trimDaySeconds(cal);
        cal.add(java.util.Calendar.DATE, 3);
        cal.add(java.util.Calendar.MILLISECOND, -1);

        return cal.getTime().getTime() < datetimeScheduled.getTime();
    }



    /**
     * Checks if task shall happen tomorrow.
     * @return
     */
    public boolean isDelayed() {
        if (this.datetimeScheduled == null) {
            return false;
        }
        Date date = new Date();
        CalendarHelper.trimDaySeconds(date);
        return this.datetimeScheduled.getTime() < date.getTime();
    }



    /**
     * Set task as completed.
     */
    public void setCompleted(boolean isCompleted) {
        if (isCompleted) {
            this.setDatetimeCompleted(new Date());
        }
        else {
            this.setDatetimeCompleted(null);
        }
    }



    /**
     * Creates VEvent component for VCalendar
     * @see <a href="http://build.mnode.org/projects/ical4j/apidocs/index.html">iCal4j API</a>
     * @see <a href="http://tools.ietf.org/html/rfc5545#section-3.6.2">RFC 5545</a>
     * @param hostName Host name (e.g. "host.com")
     * @return VToDo component for VCalendar
     * @throws TasksException
     */
    public VToDo getVTodo(String hostName) throws TasksException {

        // Create VTodo
        VToDo vtodo = new VToDo(new PropertyList());

        // Add DTSTAMP
        vtodo.getProperties().add(new DtStamp(new net.fortuna.ical4j.model.DateTime(this.datetimeCreated)));

        // Add DTSTART
        if (this.datetimeScheduled != null) {
            vtodo.getProperties().add(new DtStart(new net.fortuna.ical4j.model.DateTime(this.datetimeScheduled)));
        }

        // Add COMPLETE
        if (this.datetimeCompleted != null) {
            vtodo.getProperties().add(new Completed(new net.fortuna.ical4j.model.DateTime(this.datetimeCompleted)));
        }

        // Add DEADLINE
        if (this.deadline != null) {
            vtodo.getProperties().add(new Due(new net.fortuna.ical4j.model.DateTime(this.deadline)));
        }

        // Add SUMMARY
        vtodo.getProperties().add(new Summary(this.name));

        // Add UID
        if (vtodoUid == null) { // Todo of local calendar - own uid
            UidGenerator ug = new UidGenerator(new SimpleHostInfo(hostName), Integer.toString(id));
            vtodo.getProperties().add(ug.generateUid());
        }
        else { // Todo of remote calendar - original uid
            vtodo.getProperties().add(new Uid(vtodoUid));
        }

        // Add DESCRIPTION
        if (description != null) {
            vtodo.getProperties().add(new Description(description));
        }
        
        // Add LOCALITY
        if (locality != null) {
            vtodo.getProperties().add(new Locality(locality));
        }

        // Add PRIORITY
        if (this.priority != 0) {
            vtodo.getProperties().add(new Priority(this.priority));
        }
        
        if (!this.getContexts().isEmpty()) {
            String catgs = new String();  
            boolean isFirst = true;
            for (Context c : this.getContexts()) {
                if (!isFirst) {
                    catgs += ",";
                }
                else isFirst = false;
                catgs += c.getName().replace(",", "\\,"); // comma is delimiter in iCalendar
                
            }
            vtodo.getProperties().add(new Categories(catgs));
        }

        return vtodo;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime
                * result
                + ((portletInstanceId == null) ? 0 : portletInstanceId
                        .hashCode());
        result = prime * result
                + ((privateId == null) ? 0 : privateId.hashCode());
        result = prime * result
                + ((vtodoUid == null) ? 0 : vtodoUid.hashCode());
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
        Task other = (Task) obj;
        if (id != other.id)
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
        if (vtodoUid == null) {
            if (other.vtodoUid != null)
                return false;
        } else if (!vtodoUid.equals(other.vtodoUid))
            return false;
        return true;
    }
    
   
    
}
