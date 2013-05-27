/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : EJBContactDAO.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.contacts.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import dip.xbasov00.contacts.domain.Contact;
import dip.xbasov00.contacts.domain.ContactGroup;

/**
 * Class EJBContactDAO represents class that manipulates with contact entities.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Stateless
public class EJBContactDAO implements ContactDAO {

    @Inject
    private EntityManager em;


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Contact> getAll() {
        TypedQuery<Contact> query = em.createQuery("SELECT t FROM Contact t", Contact.class);
        return query.getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Contact getContact(int id) {
        return em.find(Contact.class, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void createContact(Contact contact) {
        em.persist(contact);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void editContact(Contact contact) {
        em.merge(contact);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteContact(Contact contact) {
        if (!em.contains(contact)) {
            contact = em.merge(contact);
        }
        em.remove(contact);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Contact> searchPortletContacts(String searchStr, String portletId) {

        TypedQuery<Contact> query = em.createQuery("SELECT t FROM Contact t WHERE" +
                " (" +
                    "UPPER(t.name) LIKE :name " +
                    "OR UPPER(t.middleName) LIKE :middleName " +
                    "OR UPPER(t.surname) LIKE :surname " +
                    "OR UPPER(t.nickName) LIKE :nickName" +
                ")" +
                " AND t.portletInstanceId = :portletId", Contact.class)
                .setParameter("name", "%" + searchStr.toUpperCase() + "%")
                .setParameter("middleName", "%" + searchStr.toUpperCase() + "%")
                .setParameter("surname", "%" + searchStr.toUpperCase() + "%")
                .setParameter("nickName", "%" + searchStr.toUpperCase() + "%")
                .setParameter("portletId", portletId);

        return query.getResultList();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Contact> getContacts(ContactGroup groupFilter) {
        if (groupFilter == null) {
            return getAll();
        }
        TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c JOIN c.contactGroups g WHERE g.id = :id", Contact.class)
                .setParameter("id", groupFilter.getId());
        return query.getResultList();

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Contact> searchPortletContacts(ContactGroup groupFilter, String keyWord, String portletId) {
        if (groupFilter == null) {
            return searchPortletContacts(keyWord, portletId);
        }
        if (keyWord == null || keyWord.trim().isEmpty()) {
            return getContacts(groupFilter);
        }

        TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c JOIN c.contactGroups g WHERE g.id = :id" +
                " AND (" +
                    "UPPER(c.name) LIKE :name " +
                    "OR UPPER(c.middleName) LIKE :middleName " +
                    "OR UPPER(c.surname) LIKE :surname " +
                    "OR UPPER(c.nickName) LIKE :nickName" +
                ")" +
                " AND c.portletInstanceId = :portletId", Contact.class)
                .setParameter("id", groupFilter.getId())
                .setParameter("name", "%" + keyWord.toUpperCase() + "%")
                .setParameter("middleName", "%" + keyWord.toUpperCase() + "%")
                .setParameter("surname", "%" + keyWord.toUpperCase() + "%")
                .setParameter("nickName", "%" + keyWord.toUpperCase() + "%")
                .setParameter("portletId", portletId);

        return query.getResultList();

    }



    /**
     * {@inheritDoc}
     */
    @Override
    public Contact getContactFromPrivateId(String privateId) {
        TypedQuery<Contact> query = em.createQuery("SELECT c FROM Contact c WHERE c.privateId = :privateId", Contact.class)
                .setParameter("privateId", privateId);
        List<Contact> contacts =  query.getResultList();
        return contacts.isEmpty() ? null : contacts.get(0);
    }


}
