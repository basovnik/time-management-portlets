/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : EJBProjectDAO.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.tasks.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import dip.xbasov00.tasks.domain.Project;

@Stateless
public class EJBProjectDAO implements ProjectDAO {

    @Inject
    private EntityManager em;


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Project> getAll() {
        TypedQuery<Project> query = em.createQuery("SELECT t FROM Project t", Project.class);
        return query.getResultList();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public List<Project> getPortletProjects(String portletId) {
        TypedQuery<Project> query = em.createQuery("SELECT t FROM Project t WHERE t.portletInstanceId = :portletId", Project.class)
                .setParameter("portletId", portletId);
        return query.getResultList();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public Project getProject(int id) {
        return em.find(Project.class, id);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void createProject(Project project) {
        em.persist(project);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void editProject(Project project) {
        em.merge(project);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteProject(Project project) {
        if (!em.contains(project)) {
            project = em.merge(project);
        }
        em.remove(project);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public List<Project> getUnfinishedProjects() {
        TypedQuery<Project> query = em.createQuery("SELECT p FROM Project p WHERE NOT EXISTS (SELECT t FROM Task t WHERE t.project = p AND t.datetimeEnd IS NULL)", Project.class);
        return query.getResultList();
    }



    @Override
    public void refreshProject(Project project) {
        if (!em.contains(project)) {
            project = em.merge(project);
        }
        em.refresh(project);
        
    }


}
