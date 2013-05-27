/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : CalendarSettingsSingleton.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.util;

import javax.inject.Singleton;

import net.fortuna.ical4j.model.property.CalScale;
import net.fortuna.ical4j.model.property.Version;


/**
 * Singleton represents iCalendar static settings.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Singleton
public class CalendarSettingsSingleton {

    public static String hostName = "dip.xbasov00.cz";

    public static String prodId = "-//xbasov00//calendar//CS";

    public static Version version = Version.VERSION_2_0;

    public static CalScale calScale = CalScale.GREGORIAN;

}
