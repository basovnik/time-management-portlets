/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : ContactsException.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.contacts.util;

import dip.xbasov00.contacts.domain.Contact;


/**
 * Class ContactsException represents own exception.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
public class ContactsException extends Exception {

    private static final long serialVersionUID = 5954527037720687290L;

    private Contact contact;

    public ContactsException(String message) {
        super(message);
    }

    public ContactsException(Contact contact, String message) {
        super(message);
        this.contact = contact;
    }

    public Contact getContact() {
        return contact;
    }

    @Override
    public String getMessage() {
        if (contact != null) {
            return "ContactException [contact-id= '" + contact.getId() + "']: " + super.getMessage();
        }
        else return super.getMessage();
    }


}
