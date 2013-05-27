/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : ContactGroup.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.contacts.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.XProperty;
import dip.xbasov00.calendar.util.CalendarException;
import dip.xbasov00.calendar.util.CalendarSettingsSingleton;

/**
 * Entity ContactGroup represents contact group entity.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Entity
@Table(name="contact_group")
public class ContactGroup implements Serializable{

    private static final long serialVersionUID = -5079412879298191072L;

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


    @Column(length=20)
    @Getter @Setter
    private String name;


    @ManyToMany(mappedBy="contactGroups", fetch=FetchType.EAGER)
    @Getter @Setter
    private Set<Contact> contacts;



    public ContactGroup() {
        generatePrivateId();
        this.contacts = new HashSet<Contact>();
    }



    public ContactGroup(String name) {
        this();
        this.name = name;
    }



    /**
     * Generates private id to new random one.
     */
    public void generatePrivateId() {
        this.privateId = UUID.randomUUID().toString();
    }



    /**
     * Generated contactGroup name
     */
    public void generateName() {
        this.name = "group_" + (new Date()).getTime();
    }




    /**
     * Returns list of contacts in VCard text format.
     *
     * @return VCard output.
     */
    public String getVCardsOutput() {
        String outputStr = "";

        for(Contact c : contacts) {
            outputStr += c.getVCard().write();
        }
        return outputStr;
    }
    
    
    
    
    /**
     * Method returns vcalendar representation of this dates of this contact group.

     * @return vcalendar representation of dates of this contact group.
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


        // Add X-WR-RELCALID (universally unique identifier e.g. 3E26604A-50F4-4449-8B3E-E4F4932D05B5)
        calendar.getProperties().add(new XProperty(
                net.fortuna.ical4j.extensions.property.WrRelCalId.PROPERTY_NAME,
                (id + "@" + CalendarSettingsSingleton.hostName))
        );


        // Add Calendar Components
        for(Contact c : contacts) {
            for (VEvent vevent : c.getVEvents(CalendarSettingsSingleton.hostName)) {
                calendar.getComponents().add(vevent);
            }
        }

        return calendar;
    }




    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        ContactGroup other = (ContactGroup) obj;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }





}
