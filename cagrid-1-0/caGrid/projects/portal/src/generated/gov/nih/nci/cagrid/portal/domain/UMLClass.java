package gov.nih.nci.cagrid.portal.domain;

import java.util.Collection;


/**
 * @version 1.0
 * @created 19-Jun-2006 4:08:51 PM
 * @hibernate.class table="UML_CLASS"
 */
public class UMLClass implements DomainObject {
    private java.lang.Integer pk;
    private java.lang.String className;
    private java.lang.String packageName;
    private java.lang.String description;
    private java.lang.String projectName;
    private java.lang.String projectVersion;
    private java.util.Collection umlAttributeCollection;
    private java.util.Collection semanticMetadataCollection;

    public UMLClass() {
    }

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
     * @hibernate.property column="CLASS_NAME"
     */
    public java.lang.String getClassName() {
        return className;
    }

    /**
     * @return
     * @hibernate.property column="DESCRIPTION"
     */
    public java.lang.String getDescription() {
        return description;
    }

    /**
     * @return
     * @hibernate.property column="PACKAGE_NAME"
     */
    public java.lang.String getPackageName() {
        return packageName;
    }

    /**
     * @return
     * @hibernate.property column="PROJECT_NAME"
     */
    public java.lang.String getProjectName() {
        return projectName;
    }

    /**
     * @return
     * @hibernate.property column="PROJECT_VERSION"
     */
    public java.lang.String getProjectVersion() {
        return projectVersion;
    }


    public void setClassName(java.lang.String newVal) {
        className = newVal;
    }

    /**
     * @param newVal
     */
    public void setDescription(java.lang.String newVal) {
        description = newVal;
    }

    /**
     * @param newVal
     */
    public void setPackageName(java.lang.String newVal) {
        packageName = newVal;
    }

    /**
     * @param newVal
     */
    public void setProjectName(java.lang.String newVal) {
        projectName = newVal;
    }

    /**
     * @param newVal
     */
    public void setProjectVersion(java.lang.String newVal) {
        projectVersion = newVal;
    }

    public Collection getUmlAttributeCollection() {
        return umlAttributeCollection;
    }

    public void setUmlAttributeCollection(Collection umlAttributeCollection) {
        this.umlAttributeCollection = umlAttributeCollection;
    }

    public Collection getSemanticMetadataCollection() {
        return semanticMetadataCollection;
    }

    public void setSemanticMetadataCollection(
            Collection semanticMetadataCollection) {
        this.semanticMetadataCollection = semanticMetadataCollection;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final UMLClass umlClass = (UMLClass) o;

        if (className != null ? !className.equals(umlClass.className) : umlClass.className != null) return false;
        if (packageName != null ? !packageName.equals(umlClass.packageName) : umlClass.packageName != null)
            return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = (className != null ? className.hashCode() : 0);
        result = 29 * result + (packageName != null ? packageName.hashCode() : 0);
        return result;
    }
}
