/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : TelephoneNumber.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.contacts.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;

import lombok.Getter;
import lombok.Setter;
import ezvcard.parameters.TelephoneTypeParameter;
import ezvcard.types.TelephoneType;

/**
 * Entity TelephoneNumber represents telephone number entity.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Entity
@Table(name="telephone_number")
public class TelephoneNumber implements Serializable{

    private static final long serialVersionUID = -6370073204533885007L;

    private static final String NO_TITLE_PREFIX = "tel";

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Getter @Setter
    private int id;


    @Column(length=20, nullable=false)
    @Getter @Setter
    private String name;


    @Column(name="telephone_number", length=20, nullable=false)
    @Getter @Setter
    private String telephoneNumber;


    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="contact_id")
    @Getter @Setter
    private Contact contact;



    public TelephoneNumber() { }



    public TelephoneNumber(TelephoneType telType) {
        if (telType.getTypes().isEmpty()) {
            this.setName(NO_TITLE_PREFIX);
        }
        else {
            List<String> names = new ArrayList<String>();
            for (TelephoneTypeParameter ttp : telType.getTypes()) {
                names.add(ttp.getValue());
            }
            this.setName(StringUtils.join(names, ", "));
        }
        this.setTelephoneNumber(telType.getValue());
    }

}
