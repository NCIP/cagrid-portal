package gov.nih.nci.cagrid.portal.domain;

import java.util.HashSet;
import java.util.Set;


/**
 * @version 1.0
 * @created 19-Jun-2006 4:05:30 PM
 * @hibernate.class table="DOMAIN_MODEL"
 */
public class DomainModel implements DomainObject {

    private Integer pk;
    private java.lang.String longName;
    private java.lang.String projectDescription;
    private java.lang.String projectVersion;
    private java.lang.String projectShortName;
    private java.util.Set umlClassCollection = new HashSet();
    private RegisteredService registeredService;

    public DomainModel() {
    }

    /**
     * @hibernate.id generator-class="foreign" column="DOMAIN_MODEL_ID_KEY"
     * @hibernate.generator-param name="property" value="registeredService"
     */
    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    /**
     * @return
     * @hibernate.property column="PROJECT_LONG_NAME"
     */
    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    /**
     * @return
     * @hibernate.property column="PROJECT_DESC"
     */
    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    /**
     * @return
     * @hibernate.property column="PROJECT_VERSION"
     */
    public String getProjectVersion() {
        return projectVersion;
    }

    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }

    /**
     * @return
     * @hibernate.property column="PROJECT_SHORT_NAME"
     */
    public String getProjectShortName() {
        return projectShortName;
    }

    public void setProjectShortName(String projectShortName) {
        this.projectShortName = projectShortName;
    }

    /**
     * @hibernate.set name="umlClassCollection"
     * cascade="all-delete-orphan"
     * @hibernate.collection-key column="DOMAIN_MODEL_ID_KEY"
     * @hibernate.collection-one-to-many class="gov.nih.nci.cagrid.portal.domain.UMLClass"
     */
    public Set getUmlClassCollection() {
        return umlClassCollection;
    }

    /**
     * @hibernate.one-to-one name="registeredService"
     * class="gov.nih.nci.cagrid.portal.domain.RegisteredService"
     * constrained="true"
     */
    public RegisteredService getRegisteredService() {
        return registeredService;
    }

    public void setRegisteredService(RegisteredService registeredService) {
        this.registeredService = registeredService;
    }

    public void setUmlClassCollection(Set umlClassCollection) {
        this.umlClassCollection = umlClassCollection;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final DomainModel that = (DomainModel) o;

        if (longName != null ? !longName.equals(that.longName) : that.longName != null) return false;
        if (projectDescription != null ? !projectDescription.equals(that.projectDescription) : that.projectDescription != null)
            return false;
        if (projectShortName != null ? !projectShortName.equals(that.projectShortName) : that.projectShortName != null)
            return false;
        if (projectVersion != null ? !projectVersion.equals(that.projectVersion) : that.projectVersion != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (longName != null ? longName.hashCode() : 0);
        result = 29 * result + (projectDescription != null ? projectDescription.hashCode() : 0);
        result = 29 * result + (projectVersion != null ? projectVersion.hashCode() : 0);
        result = 29 * result + (projectShortName != null ? projectShortName.hashCode() : 0);
        return result;
    }
}