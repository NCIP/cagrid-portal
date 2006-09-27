package gov.nih.nci.cagrid.portal.domain;

import gov.nih.nci.cagrid.portal.exception.MetadataRetreivalException;
import gov.nih.nci.cagrid.portal.utils.GridUtils;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;

import java.util.HashSet;
import java.util.Set;


/**
 * @version 1.0
 * @created 19-Jun-2006 4:08:50 PM
 * @hibernate.class table="REGISTERED_SERVICES"
 */
public class RegisteredService implements GridService, Comparable {

    public static final String DATA_SERVICE = "Data Service";
    public static final String ANALYTICAL_SERVICE = "Analyitcal Service";

    // Properties
    private Integer pk;
    private String EPR;
    private String version;
    private String name;
    private String description;

    //Associations
    private ResearchCenter rc;
    private Set operationCollection = new HashSet();
    private DomainModel domainModel;
    private EndpointReferenceType handle;


    public RegisteredService() {
    }

    public RegisteredService(String epr) throws URI.MalformedURIException {
        this(GridUtils.getEPR(epr));
    }

    public RegisteredService(EndpointReferenceType handle) {
        this.setHandle(handle);
    }

    public RegisteredService(EndpointReferenceType handle, boolean loadMetadata)
            throws MetadataRetreivalException {
        this(handle);

        if (loadMetadata) {
            setName(GridUtils.getServiceName(handle));
            setDescription(GridUtils.getServiceDescription(handle));
            setVersion(GridUtils.getServiceVersion(handle));
        }
    }

    /**
     * @hibernate.id generator-class="increment"
     * default-value="-1"
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
     * unique="true"
     * not-null="true"
     */
    public String getEPR() {
        return EPR;
    }

    private void setEPR(String EPR) throws URI.MalformedURIException {
        this.EPR = EPR;

        // Once epr is set also set the handle property
        setHandle(GridUtils.getEPR(EPR));
    }

    public EndpointReferenceType getHandle() {
        return this.handle;
    }

    /**
     * @return
     * @hibernate.property column="NAME"
     * type="string"
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return
     * @hibernate.property column="DESCRIPTION"
     * type="string"
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setHandle(EndpointReferenceType handle) {
        this.handle = handle;
        this.EPR = handle.getAddress().toString();
    }

    /**
     * @hibernate.one-to-one class="gov.nih.nci.cagrid.portal.domain.ResearchCenter"
     * cascade="all"
     */
    private ResearchCenter getRc() {
        return rc;
    }

    private void setRc(ResearchCenter rc) {
        this.rc = rc;
    }


    public ResearchCenter getResearchCenter() {
        return rc;
    }

    public void setResearchCenter(ResearchCenter researchCenter) {
        this.rc = researchCenter;
        researchCenter.setRegisteredService(this);
    }

    /**
     * @hibernate.set name="operationsCollection"
     * cascade="all"
     * update="false"
     * inverse="true"
     * @hibernate.collection-key column="SERVICE_ID_KEY"
     * @hibernate.collection-one-to-many class="gov.nih.nci.cagrid.portal.domain.Operation"
     */
    private Set getOperationCollection() {
        return operationCollection;
    }

    private void setOperationCollection(Set operationCollection) {
        this.operationCollection = operationCollection;
    }

    /**
     * Public method to add operations
     *
     * @param oper
     */
    public void addOperation(Operation oper) {
        oper.setRegisteredService(this);
        this.operationCollection.add(oper);
    }

    /**
     * @hibernate.one-to-one class="gov.nih.nci.cagrid.portal.domain.DomainModel"
     * cascade="all"
     */
    public DomainModel getDomainModel() {
        return domainModel;
    }

    private void setDomainModel(DomainModel domainModel) {
        this.domainModel = domainModel;
    }

    //public method to set domain model
    public void setObjectModel(DomainModel domainModel) {
        this.domainModel = domainModel;
        domainModel.setRegisteredService(this);

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


    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final RegisteredService that = (RegisteredService) o;

        if (EPR != null ? !EPR.equals(that.EPR) : that.EPR != null) return false;

        return true;
    }

    public int hashCode() {
        return (EPR != null ? EPR.hashCode() : 0);
    }

    public int compareTo(Object o) {
        if ((o == null)) {
            throw new NullPointerException("Object being compared to null");
        }

        final RegisteredService that = (RegisteredService) o;
        //if any name attribute is null
        //send it to the end of the list
        if (this.getName() == null) {
            return 1;
        } else if (that.getName() == null) {
            return -1;
        } else {
            return that.getName().compareToIgnoreCase(this.getEPR());
        }
    }
}
