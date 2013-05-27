/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : URL.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.contacts.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import ezvcard.types.UrlType;

/**
 * Entity URL represents url entity.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Entity
public class URL implements Serializable{

    private static final long serialVersionUID = 4554032082311279585L;

    private static final String NO_TITLE_PREFIX = "url";


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Getter @Setter
    private int id;


    @Column(length=20, nullable=false)
    @Getter @Setter
    private String name;


    @org.hibernate.validator.constraints.URL
    @Column(nullable=false)
    @Getter @Setter
    private String url;


    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="contact_id")
    @Getter @Setter
    private Contact contact;



    public URL() { 
        this.url="http://";
    }


    public URL(UrlType urlType) {
        if (urlType.getType() != null) {
            this.setName(urlType.getType());
        }
        else {
            this.setName(NO_TITLE_PREFIX);
        }
        this.setUrl(urlType.getValue());
    }

}
