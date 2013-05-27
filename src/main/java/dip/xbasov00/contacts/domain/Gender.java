/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : Gender.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.contacts.domain;

import dip.xbasov00.util.Resources;

/**
 * Enum Gender.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
public enum Gender {
    MALE,
    FEMALE;

    public String toString() {
        return Resources.getBundle().getString(super.toString());
    }
}
