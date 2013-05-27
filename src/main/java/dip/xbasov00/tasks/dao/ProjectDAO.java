/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : ProjectDAO.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.tasks.dao;

import java.util.List;

import dip.xbasov00.tasks.domain.Project;


/**
 * Interface ProjectDAO
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
public interface ProjectDAO {

    /**
     * get all projects
     * @return
     */
    List<Project> getAll();


    /**
     * Get project group with specified id.
     *
     * @param id Project id.
     * @return Project with specified id.
     */
    Project getProject(int id);


    /**
     * Create project.
     *
     * @param project
     */
    void createProject(Project project);


    /**
     * Edit Project.
     *
     * @param project
     */
    void editProject(Project project);


    /**
     * Delete project.
     *
     * @param project
     */
    void deleteProject(Project project);



    /**
     * Get unfinished projects.
     *
     * @return List of portlets.
     */
    List<Project> getUnfinishedProjects();


    /**
     * Get all projects from current portlet.
     *
     * @param portlet window id
     * @return List of portlets.
     */
    List<Project> getPortletProjects(String portletID);


    void refreshProject(Project project);
}
