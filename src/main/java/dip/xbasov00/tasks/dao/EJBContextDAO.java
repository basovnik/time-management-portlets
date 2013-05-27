/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : EJBContextDAO.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.tasks.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import dip.xbasov00.tasks.domain.Context;
import dip.xbasov00.tasks.domain.Task;

@Stateless
public class EJBContextDAO implements ContextDAO {

    @Inject
    private EntityManager em;


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Context> getAll() {
        TypedQuery<Context> query = em.createQuery("SELECT t FROM Context t", Context.class);
        return query.getResultList();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public List<Context> getPortletContexts(String portletId) {
        TypedQuery<Context> query = em.createQuery("SELECT t FROM Context t WHERE t.portletInstanceId = :portletId", Context.class)
                .setParameter("portletId", portletId);
        return query.getResultList();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public Context getContext(int id) {
        return em.find(Context.class, id);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void createContext(Context context) {
        em.persist(context);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void editContext(Context context) {
        em.merge(context);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteContext(Context context) {
        for(Task t : context.getTasks()) {
            t.getContexts().remove(context);
            em.merge(t);
        }
        
        if (!em.contains(context)) {
            context = em.merge(context);
        }
        em.remove(context);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public List<Context> getContexts(Task task) {
        TypedQuery<Context> query = em.createQuery("SELECT c FROM Context c JOIN c.tasks t WHERE t.id = :id", Context.class)
                .setParameter("id", task.getId());
        return query.getResultList();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public Context getByName(String name, String portletId) {
        TypedQuery<Context> query = em.createQuery("SELECT t FROM Context t WHERE t.name = :name AND t.portletInstanceId = :portletId", Context.class)
                .setParameter("name", name)
                .setParameter("portletId", portletId);
        List<Context> contexts =  query.getResultList();
        return contexts.isEmpty() ? null : contexts.get(0);
    }



    @Override
    public void refreshContext(Context context) {
        em.refresh(context);
        
    }


}
