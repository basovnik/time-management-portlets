/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : CalDAVRemoteCalendar.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.domain;


import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;


/**
 * Class CalDAVRemoteCalendar represents remote calendar on specific URL. This calendar
 * is running on CalDAV server and it is possible as reading as modifying of this calendar.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */

@Entity
public class CalDAVRemoteCalendar extends RemoteCalendar {

    private static final long serialVersionUID = 830379453819228744L;


    /**
     * Username for remote account.
     */
    @Getter @Setter
    private String username;

    /**
     * Password for remote account.
     */
    private String password;


    /**
     * Get password
     * @return password
     */
    public String getPassword() {

        /*
            Credentials c = (Credentials) ConversationState.getCurrent().getAttribute(Credentials.CREDENTIALS);
            if (c != null && c.getPassword() != null) {
                try {
                    return LocalEncryptor.decrypt(key, c.getPassword());
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            else return password;
        */

        return password;
    }


    /**
     * Set password
     * @param password
     */
    public void setPassword(String password) {

        /*
            // ADD ENCRYPTED FLAG !!! Not logged users cannot read calendars with encrypted passwords!!!!
            // And what about users in the same group? How to decrypt password?

            Credentials c = (Credentials) ConversationState.getCurrent().getAttribute(Credentials.CREDENTIALS);
            if (c != null && c.getPassword() != null) {
                try {
                    this.password = LocalEncryptor.encrypt(key, c.getPassword());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                this.password = password;
            }
        */
        this.password = password;
    }



    /**
     * Object creator.
     */
    public CalDAVRemoteCalendar() {
        super();
    }



    /**
     * Object creator.
     *
     * @param calendar
     * @param url
     */
    public CalDAVRemoteCalendar(Calendar calendar, String url) {
        super(calendar, url);
    }


    /**
     * Creation of "clone" object.
     *
     * @param calendar
     */
    public CalDAVRemoteCalendar(CalDAVRemoteCalendar calendar) {
        super(calendar);
    }


    /**
     * Construct object from object representing iCalendar.
     *
     * @param vcalendar
     */
    public CalDAVRemoteCalendar(net.fortuna.ical4j.model.Calendar vcalendar) {
        super(vcalendar);
    }
    
    
    /**
     * Update calendar properties
     * @param eventSrc Newer version of object.
     */
    public void updateBy(CalDAVRemoteCalendar calendar) {
        super.updateBy(calendar);
        this.username = calendar.username;
        this.password = calendar.password;
    }

}
