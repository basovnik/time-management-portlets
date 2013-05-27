/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : ContactsREST.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.contacts.rest;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import dip.xbasov00.calendar.util.CalendarException;
import dip.xbasov00.contacts.dao.ContactDAO;
import dip.xbasov00.contacts.dao.ContactGroupDAO;
import dip.xbasov00.contacts.domain.Contact;
import dip.xbasov00.contacts.domain.ContactGroup;


/**
 * Class CalendarREST represents REST API for getting of iCalendar with specified private_id.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Path("/contacts")
@RequestScoped
public class ContactsREST {


    @Inject
    private ContactDAO contactDao;


    @Inject
    private ContactGroupDAO contactGroupDao;



    /**
     * Returns file with specified vCard.
     * If private ID does not exists method returns error html page.
     *
     * @param privateId Private ID of contact.
     * @return Reponse
     */
    @GET
    @Path("vcard/{privateId}")
    @Produces("text/vcard")
    public Response getVCard(@PathParam("privateId") String privateId) {

        Contact contact = contactDao.getContactFromPrivateId(privateId);

        if (contact == null) {
            return Response.status(Status.NOT_FOUND)
                    .type(MediaType.TEXT_HTML)
                    .entity("<h3>Contact not found</h3>" +
                            "<p>We are sorry, but contact with private id '" + privateId + "' was not found!</p>")
                    .build();
        } else {
            ezvcard.VCard vCard = contact.getVCard();

            String outputStr = vCard.write();
            String filename = privateId + ".vcf";

            return Response
                    .ok(outputStr)
                    .header("Content-Disposition",
                            "attachment; filename=" + filename).build();
        }
    }


    /**
     * Returns file with specified vCard group.
     * If private ID does not exists method returns error html page.
     *
     * @param privateId Private ID of contact group.
     * @return Reponse
     */
    @GET
    @Path("vcard-group/{privateId}")
    @Produces("text/vcard")
    public Response getVCardGroup(@PathParam("privateId") String privateId) {

        ContactGroup contactGroup = contactGroupDao.getContactGroupFromPrivateId(privateId);

        if (contactGroup == null) {
            return Response.status(Status.NOT_FOUND)
                    .type(MediaType.TEXT_HTML)
                    .entity("<h3>Contact group not found</h3>" +
                            "<p>We are sorry, but contact group with private id '" + privateId + "' was not found!</p>")
                    .build();
        } else {

            String outputStr = contactGroup.getVCardsOutput();
            String filename = "group_" + privateId + ".vcf";

            return Response
                    .ok(outputStr)
                    .header("Content-Disposition",
                            "attachment; filename=" + filename).build();
        }
    }
    
    
    
    
    /**
     * Returns file with specified iCalendar.
     * If private ID does not exists method returns error html page.
     *
     * @param privateId Private ID of calendar.
     * @return
     * @throws CalendarException
     */
    @GET
    @Path("ical-group/{privateId}")
    @Produces("text/Calendar")
    public Response getICalendar(@PathParam("privateId") String privateId) throws CalendarException {

        ContactGroup contactGroup = contactGroupDao.getContactGroupFromPrivateId(privateId);
        
        if (contactGroup == null) {
            return Response.status(Status.NOT_FOUND)
                    .type(MediaType.TEXT_HTML)
                    .entity("<h3>Contact group not found</h3>" +
                            "<p>We are sorry, but contact group with private id '" + privateId + "' was not found!</p>")
                    .build();
        } else {
            net.fortuna.ical4j.model.Calendar vcalendar = contactGroup.getVCalendar();
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
