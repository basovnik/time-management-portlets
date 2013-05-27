/* 
 * Project       : Master Thesis - Portlets based time management tools
 * Document      : Contact.java
 * Author        : Bc. Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 * Organization: : FIT VUT <http://www.fit.vutbr.cz>
 */

package dip.xbasov00.contacts.domain;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Past;

import lombok.Getter;
import lombok.Setter;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.RRule;
import net.fortuna.ical4j.model.property.Uid;

import org.apache.commons.lang.StringUtils;

import dip.xbasov00.util.CalendarHelper;
import dip.xbasov00.util.DateWithDefaultTimezone;
import dip.xbasov00.util.StringHelper;
import ezvcard.VCard;
import ezvcard.types.AddressType;
import ezvcard.types.AnniversaryType;
import ezvcard.types.BirthdayType;
import ezvcard.types.EmailType;
import ezvcard.types.GenderType;
import ezvcard.types.LanguageType;
import ezvcard.types.StructuredNameType;
import ezvcard.types.TelephoneType;
import ezvcard.types.UrlType;


/**
 * Entity Contact represents contact entity.
 *
 * @author Martin Basovnik <xbasov00@stud.fit.vutbr.cz>
 *
 */
@Entity
public class Contact implements Serializable {

    private static final long serialVersionUID = 6701557920555789158L;

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Getter @Setter
    private int id;


    @Column(unique=true, nullable=false, name="private_id")
    @Getter @Setter
    private String privateId;


    @Column(name="portlet_instance_id")
    @Getter @Setter
    private String portletInstanceId;

    @Column(length=20)
    @Getter
    private String name;

    @Column(length=20, nullable=true)
    @Getter
    private String middleName;

    @Column(length=20, nullable=true)
    @Getter
    private String surname;


    @Column(name = "degree_before", length=10, nullable=true)
    @Getter
    private String degreeBefore;


    @Column(name = "degree_after", length=10, nullable=true)
    @Getter
    private String degreeAfter;


    @Column(name = "nick_name", length=20, nullable=true)
    @Getter
    private String nickName;


    @Column(name = "displayed_name", length=50, nullable=true)
    @Getter
    private String displayedName;


    @Temporal(TemporalType.DATE)
    @Column(name = "birthday", nullable=true)
    @Past
    @Getter @Setter
    private Date birthday;


    @Temporal(TemporalType.DATE)
    @Column(name = "anniversary", nullable=true)
    @Getter @Setter
    private Date anniversary;


    @Enumerated(EnumType.ORDINAL)
    @Getter @Setter
    private Gender gender;

    @Column(nullable=true, length=1000)
    @Getter
    private String note;


    @OneToMany(mappedBy="contact", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
    @Getter @Setter
    private Set<Email> emails;


    @OneToMany(mappedBy="contact", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
    @Getter @Setter
    private Set<URL> urls;


    @OneToMany(mappedBy="contact", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
    @Getter @Setter
    private Set<TelephoneNumber> telephoneNumbers;


    @OneToMany(mappedBy="contact", cascade=CascadeType.ALL, orphanRemoval=true, fetch=FetchType.EAGER)
    @Getter @Setter
    private Set<Address> addresses;


    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="contact_lang",
        joinColumns=
            @JoinColumn(name="contact_id", referencedColumnName="id"),
        inverseJoinColumns=
            @JoinColumn(name="lang_id", referencedColumnName="id")
        )
    @Getter @Setter
    private Set<Lang> langs;


    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name="contact_contactgroup",
        joinColumns=
            @JoinColumn(name="contact_id", referencedColumnName="id"),
        inverseJoinColumns=
            @JoinColumn(name="contactgroup_id", referencedColumnName="id")
        )
    @Getter @Setter
    private Set<ContactGroup> contactGroups;



    public Contact() {
        generatePrivateId();
        emails = new HashSet <Email>();
        urls = new HashSet <URL>();
        telephoneNumbers = new HashSet <TelephoneNumber>();
        addresses = new HashSet <Address>();
        gender = Gender.MALE;
        langs = new HashSet<Lang>();
        contactGroups = new HashSet<ContactGroup>();
    }



    /**
     * Construct object from VCard object.
     *
     * @param vc VCard object
     */
    public Contact(VCard vc) {
        this();

        // N
        if (vc.getStructuredName() != null) {

            if (vc.getStructuredName().getPrefixes() != null) {
                this.degreeBefore = StringUtils.join(vc.getStructuredName().getPrefixes(), " ");
            }

            if (vc.getStructuredName().getGiven() != null) {
                this.name = vc.getStructuredName().getGiven();
            }
            else { // Name cannot be null
                this.name = "<Name>";
            }

            if (vc.getStructuredName().getAdditional() != null) {
                this.middleName = StringUtils.join(vc.getStructuredName().getAdditional(), " ");
            }

            this.surname = vc.getStructuredName().getFamily();

            if (vc.getStructuredName().getSuffixes() != null) {
                this.degreeAfter = StringUtils.join(vc.getStructuredName().getSuffixes(), ", ");
            }
        }

        // NOTE
        if (vc.getNotes() != null && vc.getNotes().size() > 0) {
            this.note = vc.getNotes().get(0).getValue();
        }

        // NICKNAME
        if (vc.getNickname() != null && vc.getNickname().getValues().size() > 0) {
            this.nickName = vc.getNickname().getValues().get(0);
        }

        // FN
        if (vc.getFormattedName() != null) {
            this.displayedName = vc.getFormattedName().getValue();
        }

        // GENDER
        if (vc.getGender() != null) {
            if (vc.getGender().isMale()) {
                this.gender = Gender.MALE;
            }
            else if (vc.getGender().isFemale()) {
                this.gender = Gender.FEMALE;
            }
        }

        // BDAY
        if (vc.getBirthday() != null) {
            this.birthday = vc.getBirthday().getDate();
        }

        // ANNIVERSARY
        if (vc.getAnniversary() != null) {
            this.anniversary = vc.getAnniversary().getDate();
        }

        // EMAIL
        for (EmailType et : vc.getEmails()) {
            Email email = new Email(et);
            email.setContact(this);
            this.emails.add(email);
        }

        // TEL
        for (TelephoneType tt : vc.getTelephoneNumbers()) {
            TelephoneNumber tn = new TelephoneNumber(tt);
            tn.setContact(this);
            this.telephoneNumbers.add(tn);
        }

        // URL
        for (UrlType ut : vc.getUrls()) {
            URL url = new URL(ut);
            url.setContact(this);
            this.urls.add(url);
        }

        // ADR
        for (AddressType at : vc.getAddresses()) {
            Address a = new Address(at);
            a.setContact(this);
            this.addresses.add(a);
        }

        // TODO languages
    }


    /**
     * Generates private id to new random one.
     */
    public void generatePrivateId() {
        this.privateId = UUID.randomUUID().toString();
    }


    public void setName(String name) {
        this.name = StringHelper.trimOrNull(name);
    }


    public void setMiddleName(String middleName) {
        this.middleName = StringHelper.trimOrNull(middleName);
    }


    public void setSurname(String surname) {
        this.surname = StringHelper.trimOrNull(surname);
    }


    public void setDegreeBefore(String degreeBefore) {
        this.degreeBefore = StringHelper.trimOrNull(degreeBefore);
    }


    public void setDegreeAfter(String degreeAfter) {
        this.degreeAfter = StringHelper.trimOrNull(degreeAfter);
    }


    public void setNickName(String nickName) {
        this.nickName = StringHelper.trimOrNull(nickName);
    }


    public void setDisplayedName(String displayedName) {
        this.displayedName = StringHelper.trimOrNull(displayedName);
    }


    /**
     * @return Returns displayedName or (degreeBefore + name + middleName + surname + degreeAfter)
     */
    public String getUsedName() {
        String str = "";
        if (displayedName != null) {
            str += displayedName;
        }
        else {
            if (degreeBefore != null) {
                str += degreeBefore + " ";
            }
            if (name != null) {
                str += name;
            }
            if (middleName != null) {
                str += " " + middleName;
            }
            if (surname != null) {
                str += " " + surname;
            }
            if (degreeAfter != null) {
                str += ", " + degreeAfter;
            }
        }
        return str;
    }


    /**
     * Returns true if gender is MALE or it is not set.
     * @return
     */
    public boolean isMale() {
        return gender == null || gender.equals(Gender.MALE);
    }


    public void setNote(String note) {
        this.note = StringHelper.trimOrNull(note);
    }


    public List<Email> getEmailsAsList() {
        return new ArrayList<Email>(emails);
    }



    public List<URL> getUrlsAsList() {
        return new ArrayList<URL>(urls);
    }


    public List<TelephoneNumber> getTelephoneNumbersAsList() {
        return new ArrayList<TelephoneNumber>(telephoneNumbers);
    }



    public List<Address> getAddressesAsList() {
        return new ArrayList<Address>(addresses);
    }



    public List<Lang> getLangsAsList() {
        return new ArrayList<Lang>(langs);
    }



    public List<ContactGroup> getContactGroupsAsList() {
        return new ArrayList<ContactGroup>(contactGroups);
    }



    /**
     * Returns VCard object generated from current object.
     *
     * @see http://code.google.com/p/ez-vcard/
     * @return VCard representation
     */
    public VCard getVCard() {
        VCard vcard = new VCard();

        // GENDER
        // @see http://tools.ietf.org/html/rfc6350#section-6.2.7
        if (gender.equals(Gender.MALE)) {
            vcard.setGender(GenderType.male());
        }
        else if (gender.equals(Gender.FEMALE)) {
            vcard.setGender(GenderType.female());
        }

        // N
        // @see http://tools.ietf.org/html/rfc6350#section-6.2.2
        StructuredNameType n = new StructuredNameType();
        n.setFamily(surname);
        n.setGiven(name);
        if (middleName != null) {
            n.addAdditional(middleName);
        }
        if (degreeBefore != null) {
            n.addPrefix(degreeBefore);
        }
        if (degreeAfter != null) {
            n.addSuffix(degreeAfter);
        }
        vcard.setStructuredName(n);

        // NICKNAME
        // @see http://tools.ietf.org/html/rfc6350#section-6.2.3
        if (nickName != null) {
            vcard.setNickname(nickName);
        }


        // FN
        // @see http://tools.ietf.org/html/rfc6350#section-6.2.1
        vcard.setFormattedName(getUsedName());

        // LANG
        // @see http://tools.ietf.org/html/rfc6350#section-6.4.4
        // Works only for version vCard 4.0+
        for(Lang l : langs) {
            System.out.println(l.getCode());
            vcard.addLanguage(new LanguageType(l.getCode()));
        }

        // ADR
        // @see http://tools.ietf.org/html/rfc6350#section-6.3.1
        for(Address a : addresses) {
            AddressType adr = new AddressType();
            adr.setStreetAddress(a.getStreet());
            adr.setPoBox(a.getPoBox());
            adr.setLocality(a.getCity());
            // adr.setRegion("NY");
            adr.setPostalCode(a.getZipCode());
            // adr.setCountry("USA");
            adr.setLabel(a.getNiceAddress());
            // adr.addType(AddressTypeParameter.HOME); // TODO @see vcard.getExtendedType("X-ABLABEL")...
            vcard.addAddress(adr);
        }

        // TEL
        // @see http://tools.ietf.org/html/rfc6350#section-6.4.1
        for(TelephoneNumber tn : telephoneNumbers) {
            vcard.addTelephoneNumber(tn.getTelephoneNumber());
            // TODO @see vcard.getExtendedType("X-ABLABEL")...
        }

        // URL
        // @see http://tools.ietf.org/html/rfc6350#section-6.7.8
        for(URL url : urls) {
            vcard.addUrl(url.getUrl());
            // @see vcard.getExtendedType("X-ABLABEL")...
        }


        // EMAIL
        // @see http://tools.ietf.org/html/rfc6350#section-6.7.8
        for(Email email : emails) {
            vcard.addEmail(new EmailType(email.getEmail()));
            // @see vcard.getExtendedType("X-ABLABEL")...
        }

        // NOTE
        // @see http://tools.ietf.org/html/rfc6350#section-6.7.2
        if (note != null) {
            vcard.addNote(note);
        }

        // BDAY
        // @see http://tools.ietf.org/html/rfc6350#section-6.2.5
        if (birthday != null) {
            vcard.setBirthday(new BirthdayType(birthday));
        }

        // ANNIVERSARY
        // @see http://tools.ietf.org/html/rfc6350#section-6.2.6
        if (anniversary != null) {
            vcard.setAnniversary(new AnniversaryType(anniversary));
        }

        return vcard;
    }
    
    
    
    /**
     * Creates events from birthday and anniversary if exist.
     * 
     * @param hostName
     * @return List of events
     */
    public List<VEvent> getVEvents(String hostName){
        List<VEvent> vevents = new ArrayList<VEvent>();
        
        final String recurRuleStr = "FREQ=YEARLY;INTERVAL=1";
        
        if (this.birthday != null) {
            Date from = CalendarHelper.trimDaySeconds(birthday);
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(from);
            cal.add(java.util.Calendar.DATE, 1);
            Date to = cal.getTime();
            VEvent bday = new VEvent(new DateWithDefaultTimezone(from.getTime()), new DateWithDefaultTimezone(to.getTime()), getUsedName() + " - birthday");           
            
            bday.getProperties().add(new Uid("b"+getContactUid(hostName)));
            
            try {
                bday.getProperties().add(new RRule(recurRuleStr));
                vevents.add(bday);
            } catch (ParseException e) { }
            
        }
        
        if (this.anniversary != null) {
            Date from = CalendarHelper.trimDaySeconds(anniversary);
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(from);
            cal.add(java.util.Calendar.DATE, 1);
            Date to = cal.getTime();
            VEvent annday = new VEvent(new DateWithDefaultTimezone(from.getTime()), new DateWithDefaultTimezone(to.getTime()), getUsedName() + " - anniversary");           
            
            annday.getProperties().add(new Uid("a"+getContactUid(hostName)));
            
            try {
                annday.getProperties().add(new RRule(recurRuleStr));
                vevents.add(annday);
            } catch (ParseException e) { }         
        }
        
        return vevents;
    }
    
    
    
    /**
     * Generates global identifier
     * 
     * @param host
     * @return global identifier
     */
    private String getContactUid(String host) {
        return this.privateId+"-"+id+"@"+host;
    }

}
