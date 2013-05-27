/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : GTDGroupType.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.tasks.domain;

import dip.xbasov00.util.Resources;

/**
 * Enum represents possible types of task according to GTD time management.
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
public enum GTDGroupType {
    INBOX,
    SCHEDULED,
    NEXT,
    SOMEDAY,
    WAITING_FOR;

    public String toString() {
        return Resources.getBundle().getString(super.toString());
    }
}
