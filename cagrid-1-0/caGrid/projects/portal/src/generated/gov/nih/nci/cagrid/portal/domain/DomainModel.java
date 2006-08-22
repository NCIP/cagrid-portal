package gov.nih.nci.cagrid.portal.domain;

import java.util.Collection;


/**
 *
 * @version 1.0
 * @created 19-Jun-2006 4:05:30 PM
 *
 * @hibernate.class table="DOMAIN_MODEL"
 */
public class DomainModel implements DomainObject{

    private Integer pk;
    private java.lang.String longName;
    private java.lang.String projectDescription;
    private java.lang.String projectVersion;
    private java.lang.String projectShortName;
    private java.util.Collection umlClassCollection;
    private RegisteredService registeredService;

    public DomainModel() {
    }

    /**
     * @hibernate.id generator-class="foreign" column="SERVICE_ID_KEY"
     * @hibernate.generator-param name="property" value="registeredService"
     *
     *
     */
    public Integer getPk() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    /**
     * @hibernate.property column="PROJECT_LONG_NAME"
     * @return
     */
    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    /**
     * @hibernate.property column="PROJECT_DESC"
     * @return
     */
    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    /**
     * @hibernate.property  column="PROJECT_VERSION"
     * @return
     */
    public String getProjectVersion() {
        return projectVersion;
    }

    public void setProjectVersion(String projectVersion) {
        this.projectVersion = projectVersion;
    }

    /**
     * @hibernate.property column="PROJECT_SHORT_NAME"
     * @return
     */
    public String getProjectShortName() {
        return projectShortName;
    }

    public void setProjectShortName(String projectShortName) {
        this.projectShortName = projectShortName;
    }

    public Collection getUmlClassCollection() {
        return umlClassCollection;
    }

    public void setUmlClassCollection(Collection umlClassCollection) {
        this.umlClassCollection = umlClassCollection;
    }

    /**
     * @hibernate.one-to-one class="gov.nih.nci.cagrid.portal.domain.RegisteredService"
     * name="registeredService"
     *  contrained="true"
     * @return
     */
    public RegisteredService getRegisteredService() {
        return registeredService;
    }

    public void setRegisteredService(RegisteredService registeredService) {
        this.registeredService = registeredService;
    }
}