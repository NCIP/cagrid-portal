package gov.nih.nci.cagrid.portal.domain;

import java.util.Collection;

/**
 * @version 1.0
 * @created 19-Jun-2006 4:05:30 PM
 */
public class DomainModel {

    private java.lang.String longName;
    private java.lang.String projectDescription;
    private java.lang.String projectVersion;
    private java.lang.String serviceEPR;
    private java.lang.String shortName;
    private RegisteredService registeredService;
    private java.util.Collection umlClassCollection;

    public DomainModel() {

    }

    public void finalize() throws Throwable {

    }

    public java.lang.String getLongName() {
        return longName;
    }

    public java.lang.String getProjectDescription() {
        return projectDescription;
    }

    public java.lang.String getProjectVersion() {
        return projectVersion;
    }

    public java.lang.String getServiceEPR() {
        return serviceEPR;
    }

    public java.lang.String getShortName() {
        return shortName;
    }

    /**
     * @param newVal
     */
    public void setLongName(java.lang.String newVal) {
        longName = newVal;
    }

    /**
     * @param newVal
     */
    public void setProjectDescription(java.lang.String newVal) {
        projectDescription = newVal;
    }

    /**
     * @param newVal
     */
    public void setProjectVersion(java.lang.String newVal) {
        projectVersion = newVal;
    }

    /**
     * @param newVal
     */
    public void setServiceEPR(java.lang.String newVal) {
        serviceEPR = newVal;
    }

    /**
     * @param newVal
     */
    public void setShortName(java.lang.String newVal) {
        shortName = newVal;
    }

    public RegisteredService getRegisteredService() {
        return registeredService;
    }

    public void setRegisteredService(RegisteredService registeredService) {
        this.registeredService = registeredService;
    }

    public Collection getUmlClassCollection() {
        return umlClassCollection;
    }

    public void setUmlClassCollection(Collection umlClassCollection) {
        this.umlClassCollection = umlClassCollection;
    }

}