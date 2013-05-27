/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : ContactDAO.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.contacts.dao;

import java.util.List;

import dip.xbasov00.contacts.domain.Contact;
import dip.xbasov00.contacts.domain.ContactGroup;

/**
 * Interface ContactDAO
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
public interface ContactDAO {

    /**
     * Get all contacts
     * @return All contacts.
     */
    List<Contact> getAll();


    /**
     * Get contact with specified id.
     * @param id Id of contact.
     * @return Contact with specified ID.
     */
    Contact getContact(int id);


    /**
     * Creates persistent contact.
     * @param contact Contact to persist.
     */
    void createContact(Contact contact);


    /**
     * Edits persistent contact.
     * @param contact Changed contact to persist.
     */
    void editContact(Contact contact);


    /**
     * Deletes persistent contact.
     * @param contact Contact to delete.
     */
    void deleteContact(Contact contact);


    /**
     * Searches contacts from selected group.
     *
     * @param groupFilter Group
     * @return Contacts from selected group. If group is null returns all contacts.
     */
    List<Contact> getContacts(ContactGroup groupFilter);



    /**
     * Search contacts by string from selected portlet.
     *
     * @param searchStr String to search
     * @param portletID
     * @return List of contacts
     */
    List<Contact> searchPortletContacts(String searchStr, String portletID);


    /**
     * Searches contacts from selected group which contain keyword in its
     * name, middleName, surname or nickName. If group is null it searches in all groups.
     * If keyWord is empty or null it does not filter contacts by keyword.
     * It searches contacts only in selected portlet.
     *
     * @param groupFilter
     * @param keyWord
     * @param portletID
     * @return List of contacts.
     */
    List<Contact> searchPortletContacts(ContactGroup groupFilter,
            String searchStr, String portletID);



    /**
     * Returns contact with specified private id.
     *
     * @param privateId Private id of contact.
     * @return Contact or null.
     */
    Contact getContactFromPrivateId(String privateId);

}
