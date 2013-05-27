/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : EJBContactGroupDAO.java
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
 * Class EJBContactGroupDAO represents class that manipulates with contact group entities.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Stateless
public class EJBContactGroupDAO implements ContactGroupDAO {

    @Inject
    private EntityManager em;


    /**
     * {@inheritDoc}
     */
    @Override
    public List<ContactGroup> getAll() {
        TypedQuery<ContactGroup> query = em.createQuery("SELECT t FROM ContactGroup t", ContactGroup.class);
        return query.getResultList();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public List<ContactGroup> getPortletContactGroups(String portletId) {
        TypedQuery<ContactGroup> query = em.createQuery("SELECT t FROM ContactGroup t WHERE" +
                " t.portletInstanceId = :portletId", ContactGroup.class)
                .setParameter("portletId", portletId);
        return query.getResultList();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public ContactGroup getContactGroup(int id) {
        return em.find(ContactGroup.class, id);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public List<ContactGroup> getContactGroups(Contact c) {
        TypedQuery<ContactGroup> query = em.createQuery("SELECT t FROM ContactGroup t JOIN t.contacts c WHERE c.id = :id", ContactGroup.class)
                .setParameter("id", c.getId());
        return query.getResultList();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void createContactGroup(ContactGroup contactGroup) {
        em.persist(contactGroup);

    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void editContactGroup(ContactGroup contactGroup) {
        em.merge(contactGroup);

    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteContactGroup(ContactGroup contactGroup) {
        
        contactGroup = getContactGroup(contactGroup.getId());

        for(Contact c :contactGroup.getContacts()) {
            c.getContactGroups().remove(contactGroup);
            em.merge(c);
        }
        
        
        if (!em.contains(contactGroup)) {
            contactGroup = em.merge(contactGroup);
        }

        em.remove(contactGroup);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public ContactGroup getContactGroupFromPrivateId(String privateId) {
        TypedQuery<ContactGroup> query = em.createQuery("SELECT cg FROM ContactGroup cg WHERE cg.privateId = :privateId", ContactGroup.class)
                .setParameter("privateId", privateId);
        List<ContactGroup> contactGroups =  query.getResultList();
        return contactGroups.isEmpty() ? null : contactGroups.get(0);
    }


}
