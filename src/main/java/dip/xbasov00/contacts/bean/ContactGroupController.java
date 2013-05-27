/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : ContactGroupController.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.contacts.bean;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.portlet.PortletRequest;
import javax.portlet.ResourceResponse;

import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;

import dip.xbasov00.calendar.util.CalendarException;
import dip.xbasov00.contacts.dao.ContactDAO;
import dip.xbasov00.contacts.dao.ContactGroupDAO;
import dip.xbasov00.contacts.domain.Contact;
import dip.xbasov00.contacts.domain.ContactGroup;
import dip.xbasov00.util.PortletHelper;
import ezvcard.Ezvcard;
import ezvcard.VCard;


/**
 * Class ContactGroupController is used to manipulated with entity Contact group.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Named
@SessionScoped
public class ContactGroupController implements Serializable {

    private static final long serialVersionUID = -5556444806392572321L;

    
    /**
     * Attribute key in portlet specific session representing selected context.
     */
    private static final String ATTR_CONTACT_GROUP = "ContactGroupController:contactGroup";
    
    
    @Inject
    private ContactGroupDAO contactGroupDao;


    @Inject
    private ContactDAO contactDao;


    @Inject
    private Logger logger;



    /**
     * Set contactGroup.
     * 
     * @param contactGroup
     */
    public void setContactGroup(ContactGroup contactGroup) {
        PortletHelper.setPortletSessionAttribute(ATTR_CONTACT_GROUP, contactGroup);
    }
    
    
    
    /**
     * Get contactGroup.
     * 
     * @return
     */
    public ContactGroup getContactGroup() {       
        Object obj = PortletHelper.getPortletSessionAttribute(ATTR_CONTACT_GROUP, ContactGroup.class);
        if (obj instanceof ContactGroup) {
            return (ContactGroup) obj;
        }
        else {
            initContactGroup();
            return getContactGroup();
        }
    }



    /**
     * Bean initialization.
     */
    @PostConstruct
    public void init() {
        initContactGroup();
    }



    /**
     * Init new contact group.
     */
    public void initContactGroup() {
        setContactGroup(new ContactGroup());
    }



    /**
     * Get all contact groups from specified portlet.
     *
     * @return List of contacts.
     */
    public List<ContactGroup> getAllContactGroups() {
        return contactGroupDao.getPortletContactGroups(PortletHelper.getRequest().getWindowID());
    }



    /**
     * Creates new contact group.
     *
     * @return Status "success" if action was successful.
     */
    public String createContactGroup() {
        getContactGroup().setPortletInstanceId(PortletHelper.getRequest().getWindowID());

        contactGroupDao.createContactGroup(getContactGroup());
        return "success";

    }



    /**
     * Saves changes to selected contact group.
     *
     * @return Status "success" if action was successful.
     */
    public String editContactGroup() {
        contactGroupDao.editContactGroup(getContactGroup());
        return "success";
    }

    
    /**
     * Deletes contact group.
     *
     * @param contact Contact group to delete.
     * @return Status "success" if action was successful.
     */
    public String deleteContactGroup() {
        return deleteContactGroup(getContactGroup());
    }
    

    /**
     * Deletes contact group.
     *
     * @param contact Contact group to delete.
     * @return Status "success" if action was successful.
     */
    public String deleteContactGroup(ContactGroup contactGroup) {
        contactGroupDao.deleteContactGroup(contactGroup);
        return "success";
    }



    /**
     * Method exportCalendar returns object calendar as file in format iCalendar.
     * This method is invoked asynchronously through ResourceRequest of portlet API.
     *
     * @param os Output stream
     * @param obj Object of type dip.xbasov00.contacts.domain.Contact
     * @throws CalendarException
     */
    public void exportVCardGroup(OutputStream os, Object obj)  {

        try {

            if (obj instanceof ContactGroup)
            {
                ContactGroup cg = (ContactGroup) obj;

                String outputStr = "";

                for(Contact c : cg.getContacts()) {
                    outputStr += c.getVCard().write();
                }

                //System.out.println(outputStr); // TODO delete

                String filename =  cg.getName() + ".vcf";

                FacesContext fc = FacesContext.getCurrentInstance();
                Object response = fc.getExternalContext().getResponse();

                if (response instanceof ResourceResponse)
                {
                    ResourceResponse rresp = (ResourceResponse) response;

                    rresp.reset();
                    rresp.setContentType("text/vcard");
                    rresp.setProperty("Content-disposition", "attachment; filename=\"" + filename + "\"");

                    try {
                        os.write(outputStr.getBytes(Charset.forName("UTF-8")));
                        os.flush();
                    } finally {
                        os.close();
                    }
                }

                // Signal the JavaServer Faces implementation that the HTTP response for this request has already been generated
                // (such as an HTTP redirect), and that the request processing lifecycle should be terminated
                // as soon as the current phase is completed.
                fc.responseComplete();

                logger.log(Level.INFO, "Export of contact with ID " + cg.getId());

            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

    }



    /**
     * Renews private id of selected calendar.
     * @return
     */
    public String renewContactGroupPrivateId() {
        getContactGroup().generatePrivateId();
        contactGroupDao.editContactGroup(getContactGroup());
        return "success";
    }



    /**
     * This method returns full URL to vCard file with several vCards.
     *
     * @param contact group
     * @return URL to vCard file
     */
    public String getContactGroupShareVCardURL(ContactGroup contactGroup) {
        PortletRequest portletRequest = PortletHelper.getRequest();
        return "http://" + portletRequest.getServerName() + ":" +
            + portletRequest.getServerPort()
            + portletRequest.getContextPath()
            + "/rest/contacts/vcard-group/" + contactGroup.getPrivateId();
    }
    
    
    
    /**
     * This method returns full URL to iCalendar file with several events
     * representing contact's anniversaries and birthdays.
     *
     * @param contact group
     * @return URL to iCalendar file
     */
    public String getContactGroupShareICalURL(ContactGroup contactGroup) {
        PortletRequest portletRequest = PortletHelper.getRequest();
        return "http://" + portletRequest.getServerName() + ":" +
            + portletRequest.getServerPort()
            + portletRequest.getContextPath()
            + "/rest/contacts/ical-group/" + contactGroup.getPrivateId();
    }



    /**
     * Listener for uploading of file containing vcards.
     * Method creates new group with generated name and all vcards from
     * uploaded file append to this group.
     *
     * @param event
     * @throws Exception
     */
    public void filesUploadlistener(FileUploadEvent event) throws Exception {

        UploadedFile file = event.getUploadedFile();

        logger.log(Level.INFO, "Filename: " + file.getName());

        InputStream is = file.getInputStream();
        Reader reader = new InputStreamReader(is);
        List<VCard> vcards = Ezvcard.parse(reader).all();

        if (vcards.size() > 0) {
            // Create contact group
            ContactGroup cg = new ContactGroup();
            cg.generateName();
            cg.setPortletInstanceId(PortletHelper.getRequest().getWindowID());
            contactGroupDao.createContactGroup(cg);

            for (VCard vc : vcards) {
                // Create contact
                Contact c = new Contact(vc);
                c.setPortletInstanceId(PortletHelper.getRequest().getWindowID());
                c.getContactGroups().add(cg);
                contactDao.createContact(c);
            }
        }


    }


}
