/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : Email.java
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

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.StringUtils;

import ezvcard.parameters.EmailTypeParameter;
import ezvcard.types.EmailType;

/**
 * Entity Email represents email entity.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Entity
public class Email implements Serializable{

    private static final long serialVersionUID = 7416054465523280294L;

    private static final String NO_TITLE_PREFIX = "email";


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Getter @Setter
    private int id;


    @Column(length=20)
    @Getter @Setter
    private String name;


    @org.hibernate.validator.constraints.Email
    @Getter @Setter
    private String email;


    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="contact_id")
    @Getter @Setter
    private Contact contact;


    public Email() { 
        this.email = "@";
    }


    public Email(EmailType emailType) {
        if (emailType.getTypes().isEmpty()) {
            this.setName(NO_TITLE_PREFIX);
        }
        else {
            List<String> names = new ArrayList<String>();
            for (EmailTypeParameter etp : emailType.getTypes()) {
                names.add(etp.getValue());
            }
            this.setName(StringUtils.join(names, ", "));
        }
        this.setEmail(emailType.getValue());
    }
}
