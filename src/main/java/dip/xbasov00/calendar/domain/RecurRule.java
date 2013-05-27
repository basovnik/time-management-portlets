/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : RecurRule.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import dip.xbasov00.util.Resources;

import lombok.Getter;
import lombok.Setter;
import net.fortuna.ical4j.model.Recur;
import net.fortuna.ical4j.model.WeekDay;

/**
 * Class RecurRule creates adapter for class
 * net.fortuna.ical4j.model.Recur for use of this application.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Entity
@Table(name="recur_rule")
public class RecurRule implements Serializable {

    private static final long serialVersionUID = -819136964265066692L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Getter @Setter
    private int id;

    /**
     * Rrule represents string value RRULE from iCalendar format.
     */
    @Getter @Setter
    private String rrule;


    @OneToOne
    @JoinColumn(name="EVENT_ID")
    @Getter @Setter
    private Event event;



    public RecurRule() {
        rrule = "";
    }



    public RecurRule(String rrule, Event event) {
        this.rrule = rrule;
        this.event = event;
    }



    /**
     * Method returns object of type Recur generated from string value RRULE.
     *
     * @return Object representing recurrence.
     */
    private Recur getRecur() {
        Recur recur = new Recur();
        try {
            recur = new Recur(rrule);
        } catch (Exception e) {
        }

        return recur;
    }



    /**
     * Checks if some recurrences exists.
     *
     * @return
     */
    public boolean hasRecurrence() {
        return !rrule.isEmpty();
    }



    /**
     * Returns frequency of recurrence.
     *
     * @see http://tools.ietf.org/html/rfc5545
     * @return Frequency
     */
    public String getFrequency() {
        return getRecur().getFrequency();
    }



    /**
     * Sets frequency of current recurrence.
     * If frequency equals "NEVER", recurrence will be canceled.
     *
     * @see http://tools.ietf.org/html/rfc5545
     * @param frequency Frequency of recurrence (DAILY, WEEKLY, ..)
     */
    public void setFrequency(String frequency) {
        Recur recur = getRecur();
        recur.setFrequency(frequency);
        this.rrule = recur.toString();
    }



    /**
     * Returns interval of current recurrence.
     * e.g. Value 2 means event will occur every second possible appearance.
     *
     * @see http://tools.ietf.org/html/rfc5545
     * @return Interval of recurrence.
     */
    public int getInterval() {
        return getRecur().getInterval();
    }



    /**
     * Sets interval.
     *
     * @see http://tools.ietf.org/html/rfc5545
     * @param Interval of recurrence.
     */
    public void setInterval(int interval) {
        Recur recur = getRecur();
        recur.setInterval(interval);
        this.rrule = recur.toString();
    }



    /**
     * Get count.
     * e.g. Value 2 means then event will occur 2 times.
     *
     * @see http://tools.ietf.org/html/rfc5545
     * @return Count of recurrences.
     */
    public int getCount() {
        return getRecur().getCount();
    }



    /**
     * Set count.
     *
     * @see http://tools.ietf.org/html/rfc5545
     * @param count
     */
    public void setCount(int count) {
        Recur recur = getRecur();
        recur.setCount(count);
        this.rrule = recur.toString();
    }



    /**
     * Returns date to which recurrence is set.
     *
     * @see http://tools.ietf.org/html/rfc5545
     * @return Until value of recurrence.
     */
    public Date getUntil() {
        return (Date) getRecur().getUntil();
    }



    /**
     * Set until.
     *
     * @see http://tools.ietf.org/html/rfc5545
     * @param until
     */
    public void setUntil(Date until) {
        Recur recur = getRecur();
        recur.setUntil(new net.fortuna.ical4j.model.Date(until));
        this.rrule = recur.toString();
    }



    /**
     * Returns set of days of week in which event will occure.
     *
     * @see http://tools.ietf.org/html/rfc5545
     * @return Set of Days (e.g. {"SU", "MO"})
     */
    public Set<String> getDayList() {
        Set<String> set = new HashSet<String>();
        for(Object o : getRecur().getDayList()) {
            WeekDay wd = (WeekDay) o;
            set.add(wd.getDay());
        }
        return set;
    }



    /**
     * Set day list.
     *
     * @see http://tools.ietf.org/html/rfc5545
     * @param set e.g. {"TU", "SA"}
     */
    public void setDayList(Set<String> set) {
        Recur recur =  getRecur();
        recur.getDayList().clear();
        for(String day : set) {
            recur.getDayList().add(new WeekDay(day));
        }
        this.rrule = recur.toString();
    }


    /**
     * Sets just one n-th weekday with offset to weekday list.
     *
     * @see http://tools.ietf.org/html/rfc5545
     * @param day Day of week {"SU", "MO", .., "FR", "SA"}
     * @param offset Indicates the nth occurrence of the specific day within the month.
     */
    public void setDay(String day, int offset) {
        Recur recur =  getRecur();
        recur.getDayList().clear();
        recur.getDayList().add(new WeekDay(new WeekDay(day), offset));
        this.rrule = recur.toString();
    }



    /**
     * Returns days of month.
     *
     * @see http://tools.ietf.org/html/rfc5545
     * @return set of month days.
     */
    public Set<Integer> getMonthDayList() {
        Set<Integer> set = new HashSet<Integer>();
        for(Object o : getRecur().getMonthDayList()) {
            Integer md = (Integer) o;
            set.add(md);
        }
        return set;
    }



    /**
     * Set month day set.
     *
     * @see http://tools.ietf.org/html/rfc5545
     * @param set
     */
    public void setMonthDayList(Set<Integer> set) {
        Recur recur =  getRecur();
        recur.getMonthDayList().clear();
        for(Integer day : set) {
            recur.getMonthDayList().add(day);
        }
        this.rrule = recur.toString();
    }



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((rrule == null) ? 0 : rrule.hashCode());
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
        RecurRule other = (RecurRule) obj;
        if (id != other.id)
            return false;
        if (rrule == null) {
            if (other.rrule != null)
                return false;
        } else if (!rrule.equals(other.rrule))
            return false;
        return true;
    }


    /**
     * Return string representation of rrule.
     * @see http://tools.ietf.org/html/rfc5545
     */
    @Override
    public String toString() {
        return rrule;
    }
    
    
    /**
     * Readable representation of occurrence.
     * @return
     */
    public String getLocalizedDesc() {
        String str = new String();
        
        if (hasRecurrence()) {
            str += Resources.getBundle().getString("Repeat");
            str += " " + Resources.getBundle().getString(getFrequency());           
            
            if (getInterval() != -1) {
                str += ", " + Resources.getBundle().getString("every");
                str += " " + getInterval() + ". " + Resources.getBundle().getString("occurrence");
            }
            
            str += ".";
            
            if (getCount() != -1) {
                str += " " + Resources.getBundle().getString("OccurrencesCountIs");
                str += " " + getCount();
                str += ".";
            }
            
            if (getUntil() != null) {
                SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
                str += " " + Resources.getBundle().getString("FinishOccurrencesAfterDate");
                str += " " + dateFormatter.format(getUntil());
                str += ".";
            }
        }
        return str;
    }

}
