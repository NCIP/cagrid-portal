package gov.nih.nci.cagrid.portal.domain;

import java.util.Collection;

/**
 * @version 1.0
 * @created 19-Jun-2006 4:08:50 PM
 * @hibernate.class table="REGISTERED_SERVICES"
 */
public class RegisteredService implements GridService {

    private java.lang.String alias;
    private java.util.Collection indexServiceCollection;
    private ResearchCenter researchCenter;
    public java.util.Collection statisticsCollection;
    public Operation operations;
    private DomainModel domainModel;
    private String epr;
    private EPR handle;

    public RegisteredService() {

    }

    /**
     * @hibernate.id column EPR
     * type string
     */
    public String getEpr() {
        return epr;
    }

    public void setEpr(String epr) {
        this.epr = epr;
        this.handle = new EPR(epr);
    }


    /**
     * @hibernate.property column ALIAS
     */
    public java.lang.String getAlias() {
        return alias;
    }

    /**
     *
     * @param newVal
     */
    public void setAlias(java.lang.String newVal) {
        alias = newVal;
    }

    public EPR getHandle() {
        return this.handle;
    }

    /**
     * @hibernate.property column NAME
     * type string
     */
    public java.lang.String getName() {
        return null;
    }

    /**
     * @hibernate.property column DESCRIPTION
     * type string
     */
    public java.lang.String getDescription() {
        return null;
    }

    /**
     *
     * @param desc
     */
    public void setDescription(java.lang.String desc) {

    }

    /**
     *
     * @param name
     */
    public void setName(java.lang.String name) {

    }

    /**
     *
     * @param handle
     */
    public void setHandle(java.lang.String handle) {

    }

    /**
     * @hibernate.collection-many-to-many class Index
     */
    public Collection getIndexServiceCollection() {
        return indexServiceCollection;
    }

    public void setIndexServiceCollection(Collection indexServiceCollection) {
        this.indexServiceCollection = indexServiceCollection;
    }

    public ResearchCenter getResearchCenter() {
        return researchCenter;
    }

    public void setResearchCenter(ResearchCenter researchCenter) {
        this.researchCenter = researchCenter;
    }

    public Collection getStatisticsCollection() {
        return statisticsCollection;
    }

    public void setStatisticsCollection(Collection statisticsCollection) {
        this.statisticsCollection = statisticsCollection;
    }

    public Operation getOperations() {
        return operations;
    }

    public void setOperations(Operation operations) {
        this.operations = operations;
    }

    public DomainModel getDomainModel() {
        return domainModel;
    }

    public void setDomainModel(DomainModel domainModel) {
        this.domainModel = domainModel;
    }
}