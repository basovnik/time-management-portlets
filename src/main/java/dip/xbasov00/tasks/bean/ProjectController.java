/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : ProjectController.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.tasks.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dip.xbasov00.tasks.dao.ProjectDAO;
import dip.xbasov00.tasks.domain.Project;
import dip.xbasov00.util.PortletHelper;


/**
 * Class ProjectController is used to manipulated with entity Project.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Named
@SessionScoped
public class ProjectController implements Serializable{

    private static final long serialVersionUID = 2336941709910724013L;
    
    
    /**
     * Attribute key in portlet specific session representing selected project.
     */
    private static final String ATTR_PROJECT = "ProjectController:project";

    
    @Inject
    private ProjectDAO projectDao;

   
    /**
     * Set project.
     * 
     * @param project
     */
    public void setProject(Project project) {
        PortletHelper.setPortletSessionAttribute(ATTR_PROJECT, project);
    }
    
    
    
    /**
     * Get project.
     * 
     * @return
     */
    public Project getProject() {       
        Object obj = PortletHelper.getPortletSessionAttribute(ATTR_PROJECT, Project.class);
        if (obj instanceof Project) {
            return (Project) obj;
        }
        else {
            initProject();
            return getProject();
        }
    }
    
    
    
    /**
     * Refreshing of project
     */
    public void refreshProject() {
        if (getProject() != null) {  
            setProject(projectDao.getProject(getProject().getId()));
        }
    }
    
    
    
    /**
     * Bean initialization.
     */
    @PostConstruct
    public void init() {       
        initProject();
    }
    
    

    /**
     * Initialize project.
     */
    public void initProject() {
        setProject(new Project());    
    }




    /**
     * Get all projects from current portlet.
     *
     * @return List of portlets.
     */
    public List<Project> getAllProjects() {
        return projectDao.getPortletProjects(PortletHelper.getRequest().getWindowID());
    }





    /**
     * Creates new project.
     *
     * @return Status "success" if action was successful.
     */
    public String createProject() {
        getProject().setPortletInstanceId(PortletHelper.getRequest().getWindowID());
        projectDao.createProject(getProject());
        return "success";
    }



    /**
     * Edits project.
     *
     * @return Status "success" if action was successful.
     */
    public String editProject() {
        projectDao.editProject(getProject());
        return "success";
    }



    /**
     * Deletes project
     *
     * @return Status "success" if action was successful.
     */
    public String deleteProject() {
        return deleteProject(getProject());
    }



    /**
     * Deletes project
     *
     * @param project
     * @return Status "success" if action was successful.
     */
    public String deleteProject(Project project) {
        projectDao.deleteProject(project);
        return "success";
    }



}
