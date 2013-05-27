/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : TasksREST.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.tasks.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import net.fortuna.ical4j.model.property.ProdId;
import net.fortuna.ical4j.model.property.XProperty;
import dip.xbasov00.calendar.util.CalendarSettingsSingleton;
import dip.xbasov00.tasks.dao.TaskDAO;
import dip.xbasov00.tasks.domain.Task;
import dip.xbasov00.tasks.util.TasksException;


/**
 * Class CalendarREST represents REST API for getting of iCalendar with specified private_id.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Path("/tasks")
@RequestScoped
public class TasksREST {


    @Inject
    private TaskDAO taskDao;


    /**
     * Returns file with specified vCard.
     * If private ID does not exists method returns error html page.
     *
     * @param privateId Private ID of contact.
     * @return Reponse
     * @throws TasksException
     */
    @GET
    @Path("ical/{portletId}")
    @Produces("text/Calendar")
    public Response getCalendar(@PathParam("portletId") String portletId) throws TasksException {

        List<Task> tasks = taskDao.getPortletTasks(portletId);


        if (tasks == null || tasks.isEmpty()) {
            return Response.status(Status.NOT_FOUND)
                    .type(MediaType.TEXT_HTML)
                    .entity("<h3>Tasks not found</h3>" +
                            "<p>We are sorry, but not tasks in portlet with id '" + portletId + "' were found!</p>")
                    .build();
        }
        else {

            // Create VCalendar
            net.fortuna.ical4j.model.Calendar vcalendar = new net.fortuna.ical4j.model.Calendar();

            // Add PRODID
            vcalendar.getProperties().add(new ProdId(CalendarSettingsSingleton.prodId));

            // Add VERSION
            vcalendar.getProperties().add(CalendarSettingsSingleton.version);

            // Add CALSCALE (e.g. GREGORIAN)
            vcalendar.getProperties().add(CalendarSettingsSingleton.calScale);

            // EXTRA ATTRIBUTES:

            // Add X-WR-CALNAME (calendar name)
            vcalendar.getProperties().add(new XProperty(
                    net.fortuna.ical4j.extensions.property.WrCalName.PROPERTY_NAME,
                    "GTD-tasks-"+portletId)
            );

            // Add X-WR-RELCALID (universally unique identifier e.g. 3E26604A-50F4-4449-8B3E-E4F4932D05B5)
            vcalendar.getProperties().add(new XProperty(
                    net.fortuna.ical4j.extensions.property.WrRelCalId.PROPERTY_NAME,
                    (portletId + "@" + CalendarSettingsSingleton.hostName))
            );

            // Add VEvents
            for(Task t : tasks) {
                vcalendar.getComponents().add(t.getVTodo(CalendarSettingsSingleton.hostName));
            }


            String outputStr = vcalendar.toString();
            String filename = portletId + ".ics";
            return Response
                    .ok(outputStr)
                    .header("Content-Disposition",
                            "attachment; filename=" + filename).build();

        }
    }

}
