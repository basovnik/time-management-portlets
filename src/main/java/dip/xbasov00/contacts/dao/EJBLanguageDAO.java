/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : EJBLanguageDAO.java
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
import dip.xbasov00.contacts.domain.Lang;

/**
 * Class EJBLanguageDAO represents class that manipulates with language entities.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Stateless
public class EJBLanguageDAO implements LanguageDAO {

    @Inject
    private EntityManager em;


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Lang> getAll() {
        TypedQuery<Lang> query = em.createQuery("SELECT t FROM Lang t", Lang.class);
        return query.getResultList();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public Lang getLanguage(int id) {
        return em.find(Lang.class, id);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public List<Lang> getLanguages(Contact c) {
        TypedQuery<Lang> query = em.createQuery("SELECT l FROM Lang l JOIN l.contacts c WHERE c.id = :id", Lang.class)
                .setParameter("id", c.getId());
        return query.getResultList();
    }

}
