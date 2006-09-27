package gov.nih.nci.cagrid.portal.domain;

/**
 * @hibernate.class table="POINT_OF_CONTACT"
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Aug 9, 2006
 * Time: 11:14:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class PointOfContact implements DomainObject {

    private Integer pk;
    private String affiliation;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String role;
    private ResearchCenter researchCenter;


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
     * @return
     * @hibernate.property column="AFFILIATION"
     */
    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    /**
     * @return
     * @hibernate.property column="EMAIL"
     */
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return
     * @hibernate.property column="FIRST_NAME"
     */
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return
     * @hibernate.property column="LAST_NAME"
     */
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return
     * @hibernate.property column="PHONE_NUMBER"
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }


    /**
     * @return
     * @hibernate.property column="ROLE"
     */
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @hibernate.many-to-one column="RC_ID_KEY"
     * class="gov.nih.nci.cagrid.portal.domain.ResearchCenter"
     */
    public ResearchCenter getResearchCenter() {
        return researchCenter;
    }

    public void setResearchCenter(ResearchCenter researchCenter) {
        this.researchCenter = researchCenter;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final PointOfContact that = (PointOfContact) o;

        if (!email.equals(that.email)) return false;
        if (!role.equals(that.role)) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = email.hashCode();
        result = 29 * result + role.hashCode();
        return result;
    }
}
