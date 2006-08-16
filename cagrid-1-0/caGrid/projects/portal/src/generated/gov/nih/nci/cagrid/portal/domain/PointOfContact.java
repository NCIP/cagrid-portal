package gov.nih.nci.cagrid.portal.domain;

/**
 * @hibernate.class table="POINT_OF_CONTACT"
 *
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 9, 2006
 * Time: 11:14:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class PointOfContact implements DomainObject{

    private Integer pk;
    private String affiliation;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String role;


    /**
     * @hibernate.id generator-class="increment"
     * column="ID_KEY"
     */
    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    /**
     * @hibernate.property column="AFFILIATION"
     * @return
     */
    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    /**
     * @hibernate.property column="EMAIL"
     * @return
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @hibernate.property column="FIRST_NAME"
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @hibernate.property column="LAST_NAME"
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @hibernate.property column="PHONE_NUMBER"
     * @return
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    /**
     * @hibernate.property column="ROLE"
     * @return
     */
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
