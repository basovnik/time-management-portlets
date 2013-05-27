/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : LanguageDAO.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.contacts.dao;

import java.util.List;

import dip.xbasov00.contacts.domain.Contact;
import dip.xbasov00.contacts.domain.Lang;

/**
 * Interface LangDAO
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
public interface LanguageDAO {

    /**
     * Get all languages
     * @return All languages.
     */
    List<Lang> getAll();

    /**
     * Get language with specified id.
     * @param id Id of language.
     * @return Language with specified ID.
     */
    Lang getLanguage(int id);

    /**
     * Get all languages from selected contact.
     * @param c
     * @return
     */
    List<Lang> getLanguages(Contact c);
}
