/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : ContactGroupDAO.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.contacts.dao;

import java.util.List;

import dip.xbasov00.contacts.domain.Contact;
import dip.xbasov00.contacts.domain.ContactGroup;

/**
 * Interface ContactGroupDAO
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
public interface ContactGroupDAO {


    /**
     * Get all contact groups
     * @return All contact groups.
     */
    List<ContactGroup> getAll();



    /**
     * Get contact group with specified id.
     * @param id Id of contact group.
     * @return Contact group with specified ID.
     */
    ContactGroup getContactGroup(int id);



    /**
     * Get Contact groups of specified contact
     * @param c Contact
     * @return List of contact groups.
     */
    List<ContactGroup> getContactGroups(Contact c);



    /**
     * Creates new contact group.
     *
     * @param contactGroup
     */
    void createContactGroup(ContactGroup contactGroup);



    /**
     * Edits specified contact group.
     *
     * @param contactGroup
     */
    void editContactGroup(ContactGroup contactGroup);



    /**
     * Deletes specified contact group.
     *
     * @param contactGroup
     */
    void deleteContactGroup(ContactGroup contactGroup);



    /**
     * Get all contact groups from specified portlet.
     *
     * @param portletId
     * @return List of contact groups.
     */
    List<ContactGroup> getPortletContactGroups(String portletId);



    /**
     * Returns contact group from private id.
     *
     * @param privateId Private id of contact group.
     * @return Contact group or null.
     */
    ContactGroup getContactGroupFromPrivateId(String privateId);
}
