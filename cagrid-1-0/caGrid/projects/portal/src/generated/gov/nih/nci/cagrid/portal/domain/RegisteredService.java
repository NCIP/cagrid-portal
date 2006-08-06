package gov.nih.nci.cagrid.portal.domain;

import gov.nih.nci.cagrid.portal.exception.MetadataRetreivalException;
import gov.nih.nci.cagrid.portal.utils.GridUtils;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;

import java.util.Collection;
import java.util.Set;


/**
 * @version 1.0
 * @created 19-Jun-2006 4:08:50 PM
 * @hibernate.class table="REGISTERED_SERVICES"
 */
public class RegisteredService implements GridService {
    // Hibernate identifier
    private Integer pk;
    private ResearchCenter researchCenter;
    private IndexService indexService;
    public java.util.Set statisticsCollection;
    public Operation operations;
    private DomainModel domainModel;
    private String epr;
    private EndpointReferenceType handle;
    private String version;
    private String name;
    private String description;


    public RegisteredService() {
    }

    public RegisteredService(EndpointReferenceType handle) {
        this.setHandle(handle);
    }

    public RegisteredService(EndpointReferenceType handle,boolean loadMetadata)

    {
        this(handle);
        if(loadMetadata){

            try {
                setName(GridUtils.getServiceName(handle));
                setDescription(GridUtils.getServiceDescription(handle));
                setVersion(GridUtils.getServiceVersion(handle));
            } catch (MetadataRetreivalException e) {
                //do nothing just log it
            }

        }
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
     * @hibernate.property column="SERVICE_EPR"
     * type="string"
     */
    public String getEpr() {
        return epr;
    }

    private void setEpr(String epr) throws URI.MalformedURIException {
        this.epr = epr;

        // Once epr is set also set the handle property
        setHandle(GridUtils.getEPR(epr));
    }

    public EndpointReferenceType getHandle() {
        return this.handle;
    }

    /**
     * @hibernate.property column="NAME"
     * type="string"
     * @return
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @hibernate.property column="DESCRIPTION"
     * type="string"
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHandle(EndpointReferenceType handle) {
        this.handle = handle;
        this.epr = handle.getAddress().toString();
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

    public void setStatisticsCollection(Set statisticsCollection) {
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


    public boolean isActive() {
        return false; //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * @return
     * @hibernate.property column="VERSION"
     * type="string"
     */
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    /**
     * @hibernate.many-to-one name="indexService"
     *                        column="INDEX_ID_KEY"
     *                         class="gov.nih.nci.cagrid.portal.domain.IndexService"
                                not-null="true"
     *
     *
     */
    public IndexService getIndex() {
        return indexService;
    }

    public void setIndex(IndexService indexService) {
        this.indexService = indexService;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        final RegisteredService that = (RegisteredService) o;

        if (!epr.equals(that.epr)) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        return epr.hashCode();
    }
}
