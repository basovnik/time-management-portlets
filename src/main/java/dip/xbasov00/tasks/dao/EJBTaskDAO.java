/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : EJBTaskDAO.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.tasks.dao;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import net.fortuna.ical4j.util.SimpleHostInfo;
import net.fortuna.ical4j.util.UidGenerator;
import dip.xbasov00.calendar.util.CalendarSettingsSingleton;
import dip.xbasov00.tasks.domain.GTDGroupType;
import dip.xbasov00.tasks.domain.Task;
import dip.xbasov00.util.CalendarHelper;

@Stateless
public class EJBTaskDAO implements TaskDAO {

    @Inject
    private EntityManager em;


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getAll() {
        TypedQuery<Task> query = em.createQuery("SELECT t FROM Task t", Task.class);
        return query.getResultList();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getPortletCompletedTasks(String portletID) {
        TypedQuery<Task> query = em.createQuery("SELECT t FROM Task t WHERE" +
                " t.portletInstanceId = :portletInstanceId" +
                " AND t.datetimeCompleted IS NOT NULL" +
                " ORDER BY t.datetimeCompleted DESC", Task.class)
                .setParameter("portletInstanceId", portletID);
        return query.getResultList();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getPortletTasks(String portletID) {
        TypedQuery<Task> query = em.createQuery("SELECT t FROM Task t WHERE" +
                " t.portletInstanceId = :portletInstanceId", Task.class)
                .setParameter("portletInstanceId", portletID);
        return query.getResultList();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getPortletTasks(GTDGroupType type, String portletID) {
        TypedQuery<Task> query = em.createQuery("SELECT t FROM Task t WHERE" +
                " t.gtdGroup = :gtdGroup" +
                " AND t.portletInstanceId = :portletInstanceId" +
                " AND t.datetimeCompleted IS NULL" +
                " ORDER BY t.datetimeCreated DESC", Task.class)
                .setParameter("gtdGroup", type)
                .setParameter("portletInstanceId", portletID);

        return query.getResultList();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getPortletScheduledTasks(Date date, String portletID) {
        java.util.Calendar calStart = java.util.Calendar.getInstance();
        calStart.setTime(date);
        CalendarHelper.trimDaySeconds(calStart);

        java.util.Calendar calEnd = java.util.Calendar.getInstance();
        calEnd.setTime(calStart.getTime());
        calEnd.add(java.util.Calendar.DAY_OF_MONTH, 1);
        calEnd.add(java.util.Calendar.MILLISECOND, -1);


        TypedQuery<Task> query = em.createQuery("SELECT t FROM Task t WHERE" +
                " (t.datetimeScheduled >= :dateStart AND t.datetimeScheduled <= :dateEnd)" +
                " AND t.portletInstanceId = :portletInstanceId" +
                " AND t.datetimeCompleted IS NULL" +
                " ORDER BY t.allDayTask DESC, t.datetimeScheduled ASC", Task.class)
                .setParameter("dateStart", calStart.getTime())
                .setParameter("dateEnd", calEnd.getTime())
                .setParameter("portletInstanceId", portletID);

        return query.getResultList();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getPortletScheduledTasksFrom(Date date, String portletID) {
        java.util.Calendar calStart = java.util.Calendar.getInstance();
        calStart.setTime(date);
        CalendarHelper.trimDaySeconds(calStart);

        TypedQuery<Task> query = em.createQuery("SELECT t FROM Task t WHERE" +
                " t.datetimeScheduled >= :dateStart " +
                " AND t.portletInstanceId = :portletInstanceId" +
                " AND t.datetimeCompleted IS NULL" +
                " ORDER BY t.datetimeScheduled ASC", Task.class)
                .setParameter("dateStart", calStart.getTime())
                .setParameter("portletInstanceId", portletID);

        return query.getResultList();
    }



    @Override
    public List<Task> getPortletScheduledTasksTo(Date date, String portletID) {
        java.util.Calendar calStart = java.util.Calendar.getInstance();
        calStart.setTime(date);
        CalendarHelper.trimDaySeconds(calStart);
        calStart.add(java.util.Calendar.DATE, 1);
        calStart.add(java.util.Calendar.MILLISECOND, -1);

        TypedQuery<Task> query = em.createQuery("SELECT t FROM Task t WHERE" +
                " t.datetimeScheduled <= :dateStart " +
                " AND t.portletInstanceId = :portletInstanceId" +
                " AND t.datetimeCompleted IS NULL" +
                " ORDER BY t.datetimeScheduled ASC", Task.class)
                .setParameter("dateStart", calStart.getTime())
                .setParameter("portletInstanceId", portletID);

        return query.getResultList();
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public Task getTask(int id) {
        return em.find(Task.class, id);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void createTask(Task task) {
        task.setDatetimeCreated(new Date());
        em.persist(task);
        
        // Generate iCalendar vevent uid if it is not set.
        if (task.getVtodoUid() == null) {
            UidGenerator ug = new UidGenerator(new SimpleHostInfo(CalendarSettingsSingleton.hostName), Integer.toString(task.getId()));
            task.setVtodoUid(ug.generateUid().getValue());
            em.merge(task);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void editTask(Task task) {
        if (task.getDatetimeCreated() == null) {
            task.setDatetimeCreated(new Date());
        }
        em.merge(task);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteTask(Task task) {
        if (!em.contains(task)) {
            task = em.merge(task);
        }
        em.remove(task);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public Task getByVToDoUid(String vtodoUid, String portletId) {
        TypedQuery<Task> query = em.createQuery("SELECT t FROM Task t WHERE t.vtodoUid = :vtodoUid AND t.portletInstanceId = :portletId", Task.class)
                .setParameter("vtodoUid", vtodoUid)
                .setParameter("portletId", portletId);
        List<Task> tasks =  query.getResultList();
        return tasks.isEmpty() ? null : tasks.get(0);
    }


}
