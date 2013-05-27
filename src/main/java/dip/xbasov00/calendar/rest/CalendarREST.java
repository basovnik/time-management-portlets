/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : CalendarREST.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.calendar.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import dip.xbasov00.calendar.dao.CalendarDAO;
import dip.xbasov00.calendar.domain.Calendar;
import dip.xbasov00.calendar.util.CalendarException;


/**
 * Class CalendarREST represents REST API for getting of iCalendar with specified private_id.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Path("/calendars")
@RequestScoped
public class CalendarREST {

    @Inject
    private CalendarDAO calendarDao;


    /**
     * Returns file with specified iCalendar.
     * If private ID does not exists method returns error html page.
     *
     * @param privateId Private ID of calendar.
     * @return
     * @throws CalendarException
     */
    @GET
    @Path("{privateId}")
    @Produces("text/Calendar")
    public Response getICalendar(@PathParam("privateId") String privateId) throws CalendarException {

        Calendar calendar = calendarDao.getCalendarFromPrivateId(privateId);
        if (calendar == null) {
            return Response.status(Status.NOT_FOUND)
                    .type(MediaType.TEXT_HTML)
                    .entity("<h3>Calendar not found</h3>" +
                            "<p>We are sorry, but calendar with private id '" + privateId + "' was not found!</p>")
                    .build();
        } else {
            net.fortuna.ical4j.model.Calendar vcalendar = calendar.getVCalendar();
            String wrRelCalId = vcalendar
                    .getProperty(net.fortuna.ical4j.extensions.property.WrRelCalId.PROPERTY_NAME)
                    .getValue();
            String outputStr = vcalendar.toString();
            String filename = wrRelCalId + ".ics";
            return Response
                    .ok(outputStr)
                    .header("Content-Disposition",
                            "attachment; filename=" + filename).build();
        }
    }
}
