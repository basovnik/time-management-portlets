/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : StringHelper.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.util;

import javax.inject.Singleton;

@Singleton
public class StringHelper {

    public static String trimOrNull(String str) {
        return (str == null || str.trim().isEmpty()) ? null : str.trim();
    }
}
