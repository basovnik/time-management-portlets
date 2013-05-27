/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : Project.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.tasks.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Getter;
import lombok.Setter;
import dip.xbasov00.util.StringHelper;

/**
 * Class Project represents entity which can group tasks.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Entity
public class Project implements Serializable{

    private static final long serialVersionUID = -6174819767937087871L;

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


    @Column(nullable=false)
    @Getter @Setter
    private String name;

    @Column(nullable=true, length=1000)
    @Getter
    private String description;


    /**
     * Creation of project
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datetime_start")
    @Getter @Setter
    private Date datetimeStart;



    @OneToMany(mappedBy = "project", fetch = FetchType.EAGER, orphanRemoval=true)
    @Getter @Setter
    private Set<Task> tasks;


    public Project() {      
        generatePrivateId();
        this.tasks = new HashSet<Task>();
    }


    public List<Task> getTasksAsList() {
        return new ArrayList<Task>(tasks);
    }
    
    

    /**
     * Generates private id to new random one.
     */
    public void generatePrivateId() {
        this.privateId = UUID.randomUUID().toString();
    }



    public void addTask(Task task) {
        if (!this.tasks.contains(task)) {
            this.tasks.add(task);
        }
        task.setProject(this);
    }


    public boolean isCompleted() {
        boolean isFinished = true;
        for(Task t : tasks) {
            if (!t.isCompleted()) {
                isFinished = false;
                break;
            }
        }
        return isFinished;
    }



    public void setDescription(String description) {
        this.description = StringHelper.trimOrNull(description);
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((datetimeStart == null) ? 0 : datetimeStart.hashCode());
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
        Project other = (Project) obj;
        if (datetimeStart == null) {
            if (other.datetimeStart != null)
                return false;
        } else if (!datetimeStart.equals(other.datetimeStart))
            return false;
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



    

}
