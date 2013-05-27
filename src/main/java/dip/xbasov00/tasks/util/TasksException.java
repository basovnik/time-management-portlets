/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : TasksException.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.tasks.util;

import dip.xbasov00.tasks.domain.Task;


/**
 * Class CalendarException represents own exception.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
public class TasksException extends Exception {

    private static final long serialVersionUID = 5954527037720687290L;

    private Task task;

    public TasksException(String message) {
        super(message);
    }

    public TasksException(Task task, String message) {
        super(message);
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    @Override
    public String getMessage() {
        if (task != null) {
            return "TaskException [task-id= '" + task.getId() + "']: " + super.getMessage();
        }
        else return super.getMessage();
    }


}
