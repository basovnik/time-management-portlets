/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : ICal4JConnector.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import net.fortuna.ical4j.connector.FailedOperationException;
import net.fortuna.ical4j.connector.ObjectNotFoundException;
import net.fortuna.ical4j.connector.ObjectStoreException;
import net.fortuna.ical4j.connector.dav.CalDavCalendarCollection;
import net.fortuna.ical4j.connector.dav.CalDavCalendarStore;
import net.fortuna.ical4j.connector.dav.PathResolver;
import net.fortuna.ical4j.model.ConstraintViolationException;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.util.CompatibilityHints;

import org.apache.jackrabbit.webdav.client.methods.DeleteMethod;

import dip.xbasov00.calendar.domain.CalDAVRemoteCalendar;
import dip.xbasov00.calendar.domain.Calendar;
import dip.xbasov00.calendar.domain.CalendarComponent;
import dip.xbasov00.calendar.domain.Event;
import dip.xbasov00.calendar.domain.RemoteCalendar;
import dip.xbasov00.util.Resources;



/**
 * Class ICal4JConnector is adapter for different libraries to work with.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */

public class ICal4JConnector implements CalDAVConnector{


    private URL url;

    /**
     * Store which can manipulate with all calendars on remote CalDAV server.
     */
    private CalDavCalendarStore store;


    /**
     * Path to "selected" calendar on remote CalDAV server.
     */
    private String calendarId;



    /**
     * Constructor.
     * @param url URL to remote calendar which we want to manipulate with.
     */
    public ICal4JConnector(String url) {
        try {
            this.url =  new URL(url);
            this.calendarId = this.url.getPath();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
   
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean connect(String user, String password) throws CalendarException {
           
        for(PathResolver pr : getPossiblePathResolvers()) {
            store = new CalDavCalendarStore(CalendarSettingsSingleton.prodId, url, pr);
            try {
                store.connect(user, password.toCharArray());
                return  store.isConnected();
            } catch (ObjectStoreException e) {
                // Try next path resolver...
            }
        }
        throw new CalendarException(Resources.getBundle().getString("CalDAVConnectError"));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() {
         store.disconnect();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isConnected() {
        return (store != null &&  store.isConnected());
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public RemoteCalendar getCalendar() throws CalendarException{

        // Check if connection is created.
        if (isConnected()) {
            try {
                CalDavCalendarCollection calCollection =  store.getCollection(calendarId);

                // Method getEvents returns each event of the calendar wrapped in Calendar.
                // It is important to join events to one calendar.
                CalDAVRemoteCalendar calendarTmp = null;
                
                // Sets compatibility mode for unsupported iCalendar features
                CompatibilityHints.setHintEnabled(CompatibilityHints.KEY_RELAXED_UNFOLDING, true);
                
                calendarTmp = new CalDAVRemoteCalendar(); // There can be no event in calendar...

                boolean isFirst = true;              
                
                for(net.fortuna.ical4j.model.Calendar cal : calCollection.getEvents()) {
                    if (isFirst) {
                        // Trick for obtaining all calendars properties!
                        calendarTmp = new CalDAVRemoteCalendar(cal);
                        calendarTmp.getCalendarComponents().clear();
                        isFirst = false;
                    }
                    if (cal.getComponents("VEVENT").size() > 0) {
                        VEvent vevent = (VEvent) (cal.getComponents("VEVENT").get(0));
                        Event event = new Event(vevent);
                        calendarTmp.addCalendarComponent(event);
                    }
                }
                
                

                return calendarTmp;

            } catch(ObjectNotFoundException e) {
                throw new CalendarException(e.getMessage());
            }
            catch(ObjectStoreException e) {
                throw new CalendarException(e.getMessage());
            }
        }
        return null;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public CalendarComponent getCalendarComponent(String uid) throws CalendarException {

        // Check if connection is created.
        if (isConnected()) {
            try {
                CalDavCalendarCollection calCollection =  store.getCollection(calendarId);

                // Method getCalendar returns calendar with one component specified by uid
                // or null if event does not exist.
                net.fortuna.ical4j.model.Calendar vcalendar = calCollection.getCalendar(uid);
                if (vcalendar == null) { //
                    return null;
                }

                RemoteCalendar calendar = new CalDAVRemoteCalendar(calCollection.getCalendar(uid));
                return calendar.getCalendarComponents().get(0);

            } catch(ObjectNotFoundException e) {
                throw new CalendarException(e.getMessage());
            } catch(ObjectStoreException e) {
                throw new CalendarException(e.getMessage());
            }
        }
        return null;
    }



    /**
     * {@inheritDoc}
     * TODO: Conflict 409, see: http://tools.ietf.org/html/rfc4791#section-5.3.1.1
     */
    @Override
    public boolean addCalendarComponent(CalendarComponent calendarComponent) throws CalendarException {

        // Check if connection is created.
        if (isConnected()) {
            try {
                CalDavCalendarCollection calCollection =  store.getCollection(calendarId);

                // Copy of event's calendar
                Calendar calendarTmp = new CalDAVRemoteCalendar((CalDAVRemoteCalendar) calendarComponent.getCalendar());
                calendarTmp.getCalendarComponents().clear();
                calendarTmp.addCalendarComponent(calendarComponent);
                calCollection.addCalendar(calendarTmp.getVCalendar());
                return true;

            } catch(ObjectNotFoundException e) {
                throw new CalendarException(e.getMessage());
            } catch(ObjectStoreException e) {
                throw new CalendarException(e.getMessage());
            } catch (ConstraintViolationException e) {
                throw new CalendarException(e.getMessage());
            }
        }
        return false;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public boolean deleteCalendarComponent(String uid) throws CalendarException {
        // Check if connection is created.
        if (isConnected()) {
            try {
                CalDavCalendarCollection calCollection =  store.getCollection(calendarId);

                // HACK: start ------------
                // There is a bug in method CalDavCalendarCollection.removeCalendar(String uid)
                // Rewritten method "removeCalendar" with corrected variable "path".

                String path = calCollection.getPath();
                if (!path.endsWith("/")) {
                    path = path.concat("/");
                }

                String str = path + uid + ".ics";
                DeleteMethod deleteMethod = new DeleteMethod(str);

                calCollection.getStore().getClient().execute(deleteMethod);

                if (!deleteMethod.succeeded()) {
                    throw new FailedOperationException(deleteMethod.getStatusLine().toString());
                }
                return true;

                // HACK: end -------------

            } catch(ObjectNotFoundException e) {
                throw new CalendarException(e.getMessage());
            } catch(ObjectStoreException e) {
                throw new CalendarException(e.getMessage());
            } catch (FailedOperationException e) {
                throw new CalendarException(e.getMessage());
            } catch (IOException e) {
                throw new CalendarException(e.getMessage());
            }
        }
        return false;
    }
    
    
    
    /**
     * Get possible path resolvers
     * 
     * @return List of path resolvers.
     */
    private List<PathResolver> getPossiblePathResolvers() {       
        List<PathResolver> prs = new ArrayList<PathResolver>();   
        
        prs.add(PathResolver.GCAL);
        prs.add(PathResolver.CALENDAR_SERVER);
        prs.add(PathResolver.BEDEWORK);           
        prs.add(PathResolver.CGP);
        prs.add(PathResolver.CHANDLER);
        prs.add(PathResolver.DAVICAL);
        prs.add(PathResolver.ICAL_SERVER);
        prs.add(PathResolver.KMS);
        prs.add(PathResolver.ORACLE_CS);
        prs.add(PathResolver.SOGO);
        prs.add(PathResolver.ZIMBRA);
        
        // GENERIC     
        return prs;
    }


}
