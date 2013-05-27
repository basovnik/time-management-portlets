/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : Address.java
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

import dip.xbasov00.util.StringHelper;
import ezvcard.parameters.AddressTypeParameter;
import ezvcard.types.AddressType;

/**
 * Entity Address represents adress entity.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Entity
public class Address implements Serializable{

    private static final long serialVersionUID = -8498192961069030681L;

    private static final String NO_TITLE_PREFIX = "adr";


    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Getter @Setter
    private int id;


    @Column(length=20, nullable=false)
    @Getter @Setter
    private String title;


    @Column(length=30, nullable=true)
    @Getter
    private String street;


    @Column(name="po_box", length=10, nullable=true)
    @Getter
    private String poBox;


    @Column(length=30, nullable=false)
    @Getter
    private String city;


    @Column(name="zip_code", length=10, nullable=true)
    @Getter
    private String zipCode;


    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="contact_id")
    @Getter @Setter
    private Contact contact;


    public Address() { }



    public Address(AddressType addressType) {

        this.setStreet(addressType.getStreetAddress());
        this.setPoBox(addressType.getPoBox());
        this.setCity(addressType.getLocality());
        this.setZipCode(addressType.getPostalCode());

        if (addressType.getTypes().isEmpty()) {
            this.setTitle(NO_TITLE_PREFIX);
        }
        else {
            List<String> names = new ArrayList<String>();
            for (AddressTypeParameter atp : addressType.getTypes()) {
                names.add(atp.getValue());
            }
            this.setTitle(StringUtils.join(names, ", "));
        }
    }


    public void setStreet(String street) {
        this.street = StringHelper.trimOrNull(street);
    }



    public void setPoBox(String poBox) {
        this.poBox = StringHelper.trimOrNull(poBox);
    }



    public void setCity(String city) {
        this.city = StringHelper.trimOrNull(city);
    }



    public void setZipCode(String zipCode) {
        this.zipCode = StringHelper.trimOrNull(zipCode);
    }



    public String getNiceAddress() {
        String addr = "";
        if (street != null) {
            addr += street;
            if (poBox != null) {
                addr += " " + poBox;
            }
            addr += ", ";
        }
        if (city != null) {
            addr += city;
            if (zipCode != null) {
                addr += " " + zipCode;
            }
        }
        return addr;
    }

}
