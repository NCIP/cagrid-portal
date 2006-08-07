package gov.nih.nci.cagrid.portal.domain;

import gov.nih.nci.cagrid.portal.exception.MetadataRetreivalException;
import gov.nih.nci.cagrid.portal.utils.GridUtils;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;

import java.util.HashSet;
import java.util.Set;


/**
 * Represents a Index Service Instance
 *
 * @version 1.0
 * @created 19-Jun-2006 4:08:50 PM
 * @hibernate.class table="INDEX_SERVICE"
 */
public class IndexService implements DomainObject, GridService {
    // Primary pk
    private Integer pk;
    private Set registeredServicesCollection = new HashSet();
    private String EPR;
    private String description;
    private String name;
    private EndpointReferenceType handle;
    private boolean active = true;

    public IndexService(String EPR) throws URI.MalformedURIException {
        this(GridUtils.getEPR(EPR));
    }

    /**
     * Self initialize the index Service bean
     * <p/>
     * Should be used carefully only if you know
     * the EPR will resolve to a service and the EPR
     * is accessible
     * <p/>
     * by providing it a valid EPR
     *
     * @param handle

     */
    public IndexService(EndpointReferenceType handle) {
        // Use setters to keep all properties in sync
        this.setHandle(handle);
    }

    /**
     * Constructor that will self load metadata
     * @param handle
     * @param loadMetadata

     */
    public IndexService(EndpointReferenceType handle, boolean loadMetadata)
        throws MetadataRetreivalException {
        this(handle);

        if (loadMetadata) {
            setName(GridUtils.getServiceName(handle));
            setDescription(GridUtils.getServiceDescription(handle));
            setActive(GridUtils.isServiceActive(handle));
        }
    }

    /**
     * Needed for hibernate
     */
    public IndexService() {
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

    public Set getRegisteredServicesCollection() {
        return registeredServicesCollection;
    }

    public void setRegisteredServicesCollection(
        Set registeredServicesCollection) {
        this.registeredServicesCollection = registeredServicesCollection;
    }

    /**
     * set to private because its only intended
     * to be used by hibernate
     *
     * @hibernate.property column="SERVICE_EPR"
     * not-null="true"
     * unique="true"
     * type="string"
     */
    public String getEPR() {
        return EPR.trim();
    }

    /**
     * set to private because its only intended
     * to be used by hibernate
     *
     * @throws URI.MalformedURIException Will throw an exception if epr string is not a valid URI
     */
    private void setEPR(String EPR) throws URI.MalformedURIException {
        this.EPR = EPR;

        // Once epr is set also set the handle(EPR) property
        this.handle = GridUtils.getEPR(EPR);
    }

    /**
     * @hibernate.property column="DESCRIPTION"
     * type="string"
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @hibernate.property column="NAME"
     * type="string"
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHandle(EndpointReferenceType handle) {
        this.handle = handle;

        // Keep it in sync with the epr(String)
        this.EPR = handle.getAddress().toString().trim();
    }

    /**
     * @return boolean
     *         ToDo Should be Implemented by concrerte classes
     */
    public boolean isEqual(DomainObject obj) {
        return false; //To change body of implemented methods use File | Settings | File Templates.
    }

    public EndpointReferenceType getHandle() {
        return handle;
    }

    /**
     * @hibernate.property column="ISACTIVE"
     * type="boolean"
     */
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        final IndexService that = (IndexService) o;

        if (!EPR.equals(that.EPR)) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        return EPR.hashCode();
    }
}
