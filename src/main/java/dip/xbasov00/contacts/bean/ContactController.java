/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : ContactController.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.contacts.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.portlet.PortletRequest;

import dip.xbasov00.contacts.dao.ContactDAO;
import dip.xbasov00.contacts.dao.ContactGroupDAO;
import dip.xbasov00.contacts.dao.LanguageDAO;
import dip.xbasov00.contacts.domain.Address;
import dip.xbasov00.contacts.domain.Contact;
import dip.xbasov00.contacts.domain.ContactGroup;
import dip.xbasov00.contacts.domain.Email;
import dip.xbasov00.contacts.domain.Gender;
import dip.xbasov00.contacts.domain.Lang;
import dip.xbasov00.contacts.domain.TelephoneNumber;
import dip.xbasov00.contacts.domain.URL;
import dip.xbasov00.util.PortletHelper;


/**
 * Class ContactController is used to manipulated with entity Contact.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Named
@SessionScoped
public class ContactController implements Serializable {

    private static final long serialVersionUID = -1238629042747200585L;


    @Inject
    private ContactDAO contactDao;


    @Inject
    private LanguageDAO languageDao;


    @Inject
    private ContactGroupDAO contactGroupDao;


    
    /**
     * Attribute key in portlet specific session representing selected contact.
     */
    private static final String ATTR_CONTACT = "ContactController:contact";
    
    
    /**
     * Attribute key in portlet specific session representing selected groupFilter.
     */
    private static final String ATTR_GROUP_FILTER = "ContactController:groupFilter";
    
    
    /**
     * Attribute key in portlet specific session representing selected email.
     */
    private static final String ATTR_EMAIL = "ContactController:email";
    
    
    /**
     * Attribute key in portlet specific session representing selected telephoneNumber.
     */
    private static final String ATTR_TELEPHONE_NUMBER = "ContactController:telephoneNumber";
    
    
    /**
     * Attribute key in portlet specific session representing selected url.
     */
    private static final String ATTR_URL = "ContactController:url";
    
    
    /**
     * Attribute key in portlet specific session representing selected address.
     */
    private static final String ATTR_ADDRESS = "ContactController:address";
    
    
    /**
     * Attribute key in portlet specific session representing selected searchStr.
     * 
     * String to search contacts. It compares to contacts with "LIKE %str%"
     * and it compares against name, surname, middle name adn nickname.
     */
    private static final String ATTR_SEARCH_STR = "ContactController:searchStr";


    
    /**
     * Set contact.
     * 
     * @param contact
     */
    public void setContact(Contact contact) {
        PortletHelper.setPortletSessionAttribute(ATTR_CONTACT, contact);
    }
    
    
    
    /**
     * Get contact.
     * 
     * @return
     */
    public Contact getContact() {       
        Object obj = PortletHelper.getPortletSessionAttribute(ATTR_CONTACT, Contact.class);
        if (obj instanceof Contact) {
            return (Contact) obj;
        }
        else {
            initContact();
            return getContact();
        }
    }
    
    
    /**
     * Set Contact Group.
     * 
     * @param ContactGroup
     */
    public void setGroupFilter(ContactGroup cg) {
        PortletHelper.setPortletSessionAttribute(ATTR_GROUP_FILTER, cg);
    }
    
    
    
    /**
     * Get contact.
     * 
     * @return
     */
    public ContactGroup getGroupFilter() {       
        Object obj = PortletHelper.getPortletSessionAttribute(ATTR_GROUP_FILTER, ContactGroup.class);
        if (obj instanceof ContactGroup) {
            return (ContactGroup) obj;
        }
        else return null;
    }
    
    
    
    /**
     * Set email.
     * 
     * @param email
     */
    public void setEmail(Email email) {
        PortletHelper.setPortletSessionAttribute(ATTR_EMAIL, email);
    }
    
    
    
    /**
     * Get email.
     * 
     * @return
     */
    public Email getEmail() {       
        Object obj = PortletHelper.getPortletSessionAttribute(ATTR_EMAIL, Email.class);
        if (obj instanceof Email) {
            return (Email) obj;
        }
        else {
            setEmail(new Email());
            return getEmail();
        }
    }
    
    
    
    /**
     * Set telephone number.
     * 
     * @param telephone number
     */
    public void setTelephoneNumber(TelephoneNumber telephoneNumber) {
        PortletHelper.setPortletSessionAttribute(ATTR_TELEPHONE_NUMBER, telephoneNumber);
    }
    
    
    
    /**
     * Get telephone number.
     * 
     * @return telephone number
     */
    public TelephoneNumber getTelephoneNumber() {       
        Object obj = PortletHelper.getPortletSessionAttribute(ATTR_TELEPHONE_NUMBER, TelephoneNumber.class);
        if (obj instanceof TelephoneNumber) {
            return (TelephoneNumber) obj;
        }
        else {
            setTelephoneNumber(new TelephoneNumber());
            return getTelephoneNumber();
        }
    }
    
    
    
    
    /**
     * Set url.
     * 
     * @param url
     */
    public void setUrl(URL url) {
        PortletHelper.setPortletSessionAttribute(ATTR_URL, url);
    }
    
    
    
    /**
     * Get url.
     * 
     * @return url
     */
    public URL getUrl() {       
        Object obj = PortletHelper.getPortletSessionAttribute(ATTR_URL, URL.class);
        if (obj instanceof URL) {
            return (URL) obj;
        }
        else {
            setUrl(new URL());
            return getUrl();
        }
    }
    
    
    /**
     * Set address.
     * 
     * @param address
     */
    public void setAddress(Address address) {
        PortletHelper.setPortletSessionAttribute(ATTR_ADDRESS, address);
    }
    
    
    
    /**
     * Get address.
     * 
     * @return address
     */
    public Address getAddress() {       
        Object obj = PortletHelper.getPortletSessionAttribute(ATTR_ADDRESS, Address.class);
        if (obj instanceof Address) {
            return (Address) obj;
        }
        else {
            setAddress(new Address());
            return getAddress();
        }
    }
    
    
    
    /**
     * Set search str.
     * 
     * @param search str
     */
    public void setSearchStr(String searchStr) {
        PortletHelper.setPortletSessionAttribute(ATTR_SEARCH_STR, searchStr);
    }
    
    
    
    /**
     * Get search str.
     * 
     * @return search str
     */
    public String getSearchStr() {       
        Object obj = PortletHelper.getPortletSessionAttribute(ATTR_SEARCH_STR, String.class);
        if (obj instanceof String) {
            return (String) obj;
        }
        else {
            setSearchStr(new String());
            return getSearchStr();
        }
    }
    
    
    


    /**
     * Bean initialization.
     */
    @PostConstruct
    public void init() {
        initContact();
    }



    /**
     * Initialize current contact.
     */
    public void initContact() {
        setContact(new Contact());
        setEmail(new Email());
        setTelephoneNumber(new TelephoneNumber());
        setUrl(new URL());
        setAddress(new Address());
        setSearchStr(new String());
    }



    /**
     * Workaround for: LazyInitializationException: could not initialize proxy - no Session
     *
     * @return List of contact's langs
     */
    public List<Lang> getContactLangs() {
        return languageDao.getLanguages(getContact());
    }



    /**
     * Workaround for: LazyInitializationException: could not initialize proxy - no Session
     *
     * @param List of langs for current contact.
     */
    public void setContactLangs(List<Lang> langs) {
        getContact().setLangs(new HashSet<Lang>(langs));
    }



    /**
     * Workaround for: LazyInitializationException: could not initialize proxy - no Session
     *
     * @return List of contact groups of current contact.
     */
    public List<ContactGroup> getContactGroups() {
        return contactGroupDao.getContactGroups(getContact());
    }



    /**
     * Workaround for: LazyInitializationException: could not initialize proxy - no Session
     *
     * @param List of contactgroups for current contact.
     */
    public void setContactGroups(List<ContactGroup> contactGroups) {
        getContact().setContactGroups(new HashSet<ContactGroup>(contactGroups));
    }



    /**
     * Returns contacts according to groupFilter, searchText and portlet instance.
     *
     * @return List of contacts.
     */
    public List<Contact> getSelectedContacts() {
        return contactDao.searchPortletContacts(getGroupFilter(), getSearchStr(), PortletHelper.getRequest().getWindowID());
    }



    /**
     * Returns gender values.
     *
     * @return {MALE, FEMALE}
     */
    public Gender[] getGenders() {
        return Gender.values();
    }



    /**
     * Adds temporary email.
     * Need to persist contact!
     */
    public void addEmailTmp() {
        getEmail().setContact(getContact());
        getContact().getEmails().add(getEmail());
        setEmail(new Email());
    }



    /**
     * Deletes email temporarily.
     * Need to persist contact!
     *
     * @param email
     */
    public void deleteEmailTmp(Email email) {
        email.getContact().getEmails().remove(email);
        email.setContact(null);
    }



    /**
     * Adds temporary telephone number.
     * Need to persist contact!
     */
    public void addTelephoneNumberTmp() {
        getTelephoneNumber().setContact(getContact());
        getContact().getTelephoneNumbers().add(getTelephoneNumber());
        setTelephoneNumber(new TelephoneNumber());
    }



    /**
     * Deletes telephone number temporarily.
     * Need to persist contact!
     *
     * @param telephoneNumber
     */
    public void deleteTelephoneNumberTmp(TelephoneNumber telephoneNumber) {
        telephoneNumber.getContact().getTelephoneNumbers().remove(telephoneNumber);
        telephoneNumber.setContact(null);
    }



    /**
     * Adds URL temporarily.
     * Need to persist contact!
     */
    public void addUrlTmp() {
        getUrl().setContact(getContact());
        getContact().getUrls().add(getUrl());
        setUrl(new URL());
    }



    /**
     * Deletes url temporarily.
     * Need to persist contact!
     *
     * @param url
     */
    public void deleteUrlTmp(URL url) {
        url.getContact().getUrls().remove(url);
        url.setContact(null);
    }


    /**
     * Adds address temporarily.
     * Need to persist contact!
     */
    public void addAddressTmp() {
        getAddress().setContact(getContact());
        getContact().getAddresses().add(getAddress());
        setAddress(new Address());
    }



    /**
     * Deletes address temporarily.
     * Need to persist contact!
     *
     * @param address
     */
    public void deleteAddressTmp(Address address) {
        address.getContact().getAddresses().remove(address);
        address.setContact(null);
    }



    /**
     * Edits address temporarily.
     * Need to persist contact!
     *
     * @param address
     */
    public void editAddressTmp(Address address) {
        setAddress(address);
    }



    /**
     * Creates new contact.
     *
     * @return Status "success" if action was successful.
     */
    public String createContact() {
        getContact().setPortletInstanceId(PortletHelper.getRequest().getWindowID());

        contactDao.createContact(getContact());
        return "success";

    }



    /**
     * Saves changes to selected contact.
     *
     * @return Status "success" if action was successful.
     */
    public String editContact() {
        contactDao.editContact(getContact());
        return "success";
    }



    /**
     * Deletes contact.
     *
     * @param contact Contact to delete.
     * @return Status "success" if action was successful.
     */
    public String deleteContact(Contact contact) {
        contactDao.deleteContact(contact);
        return "success";
    }



    /**
     * Renews private id of selected calendar.
     * @return
     */
    public String renewContactPrivateId() {
        getContact().generatePrivateId();
        contactDao.editContact(getContact());
        return "success";
    }



    /**
     * This method returns full URL to vCard file.
     *
     * @param contact
     * @return URL to vCard file
     */
    public String getContactShareURL(Contact contact) {
        PortletRequest portletRequest = PortletHelper.getRequest();
        return "http://" + portletRequest.getServerName() + ":" +
            + portletRequest.getServerPort()
            + portletRequest.getContextPath()
            + "/rest/contacts/vcard/" + contact.getPrivateId();
    }


}
