/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : Context.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.tasks.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.Setter;

/**
 * Class Context represents possible context of entity Task.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Entity
public class Context implements Serializable{

    private static final long serialVersionUID = -730469970035211904L;


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


    @ManyToMany(mappedBy="contexts", fetch=FetchType.EAGER)
    @Getter @Setter
    private List<Task> tasks;


    public Context() {
        this.tasks = new ArrayList<Task>();
        generatePrivateId();
    }   


    /**
     * Generates private id to new random one.
     */
    public void generatePrivateId() {
        this.privateId = UUID.randomUUID().toString();
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        Context other = (Context) obj;
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
