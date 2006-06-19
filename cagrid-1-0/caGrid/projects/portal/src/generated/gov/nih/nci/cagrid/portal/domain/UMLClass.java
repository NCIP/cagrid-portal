package gov.nih.nci.cagrid.portal.domain;

import java.util.Collection;

/**
 * @version 1.0
 * @created 19-Jun-2006 4:08:51 PM
 */
public class UMLClass {

    private java.lang.String className;
    private java.lang.String description;
    private int id;
    private java.lang.String packageName;
    private java.lang.String projectName;
    private java.lang.String projectVersion;
    private java.lang.String serviceEPR;
    private DomainModel domainModel;
    private java.util.Collection umlAttributeCollection;
    private java.util.Collection semanticMetadataCollection;

    public UMLClass(){

    }

    public void finalize() throws Throwable {

    }

    public java.lang.String getClassName(){
        return className;
    }

    public java.lang.String getDescription(){
        return description;
    }

    public int getId(){
        return id;
    }

    public java.lang.String getPackageName(){
        return packageName;
    }

    public java.lang.String getProjectName(){
        return projectName;
    }

    public java.lang.String getProjectVersion(){
        return projectVersion;
    }

    public java.lang.String getServiceEPR(){
        return serviceEPR;
    }

    /**
     *
     * @param newVal
     */
    public void setClassName(java.lang.String newVal){
        className = newVal;
    }

    /**
     *
     * @param newVal
     */
    public void setDescription(java.lang.String newVal){
        description = newVal;
    }

    /**
     *
     * @param newVal
     */
    public void setId(int newVal){
        id = newVal;
    }

    /**
     *
     * @param newVal
     */
    public void setPackageName(java.lang.String newVal){
        packageName = newVal;
    }

    /**
     *
     * @param newVal
     */
    public void setProjectName(java.lang.String newVal){
        projectName = newVal;
    }

    /**
     *
     * @param newVal
     */
    public void setProjectVersion(java.lang.String newVal){
        projectVersion = newVal;
    }

    /**
     *
     * @param newVal
     */
    public void setServiceEPR(java.lang.String newVal){
        serviceEPR = newVal;
    }

    public DomainModel getDomainModel() {
        return domainModel;
    }

    public void setDomainModel(DomainModel domainModel) {
        this.domainModel = domainModel;
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

    public void setSemanticMetadataCollection(Collection semanticMetadataCollection) {
        this.semanticMetadataCollection = semanticMetadataCollection;
    }
}