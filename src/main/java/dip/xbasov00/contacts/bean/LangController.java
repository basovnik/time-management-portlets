/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : LangController.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.contacts.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import dip.xbasov00.contacts.dao.LanguageDAO;
import dip.xbasov00.contacts.domain.Lang;
import dip.xbasov00.util.PortletHelper;

@Named
@SessionScoped
public class LangController implements Serializable {

    private static final long serialVersionUID = 170049743802542296L;
    
    /**
     * Attribute key in portlet specific session representing selected context.
     */
    private static final String ATTR_LANG = "LangController:lang";

    @Inject
    private LanguageDAO languageDao;
    

    
    /**
     * Set lang.
     * 
     * @param lang
     */
    public void setLang(Lang lang) {
        PortletHelper.setPortletSessionAttribute(ATTR_LANG, lang);
    }
    
    
    
    /**
     * Get lang.
     * 
     * @return
     */
    public Lang getLang() {       
        Object obj = PortletHelper.getPortletSessionAttribute(ATTR_LANG, Lang.class);
        if (obj instanceof Lang) {
            return (Lang) obj;
        }
        else {
            initLang();
            return getLang();
        }
    }
    
    

    @PostConstruct
    public void init() {
        initLang();
    }

    public void initLang() {
        setLang(new Lang());
    }


    public List<Lang> getAllLanguages() {
        return languageDao.getAll();
    }

}
