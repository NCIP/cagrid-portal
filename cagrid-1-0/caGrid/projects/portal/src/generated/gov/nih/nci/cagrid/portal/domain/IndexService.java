package gov.nih.nci.cagrid.portal.domain;

import gov.nih.nci.cagrid.portal.utils.GridUtils;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;

import java.util.List;

/**
 * Represents a Index Service Instance
 *
 * @version 1.0
 * @created 19-Jun-2006 4:08:50 PM
 * @hibernate.class table="INDEX_SERVICE"
 */
public class IndexService implements DomainObject, GridService {

    private List registeredServicesCollection;
    private String epr;
    private String description;
    private String name;
    private EndpointReferenceType handle;
    private boolean active;

    // Primary key
    private int key;

    /**
     * Self initialize the index Service bean
     * 
     * Should be used carefully only if you know
     * the EPR will resolve to a service and the EPR
     * is accessible
     *
     * by providing it a valid EPR
     * @param handle
     * @throws Exception
     */
    public IndexService(EndpointReferenceType handle) throws Exception{
        // Use setters to keep all properties in sync
        this.setHandle(handle);
        this.setName(GridUtils.getServiceName(handle));
        this.setDescription(GridUtils.getServiceDescription(handle));
        this.setActive(GridUtils.isServiceActive(getHandle()));
    }

    /**
     * Needed for hibernate
     */
    public IndexService() {
    }

    public List getRegisteredServicesCollection() {
        return registeredServicesCollection;
    }

    public void setRegisteredServicesCollection(List registeredServicesCollection) {
        this.registeredServicesCollection = registeredServicesCollection;
    }

    /**
     * set to private because its only intended
     * to be used by hibernate
     *
     * @hibernate.property column="SERVICE_EPR"
     * not-null="true"
     * unique="true"
     *
     */
    private String getEpr() {
        return epr;
    }

    /**
     * set to private because its only intended
     * to be used by hibernate
     *
     * @throws URI.MalformedURIException Will throw an exception if epr string is not a valid URI
     */
    private void setEpr(String epr) throws URI.MalformedURIException {
        this.epr = epr;

        // Once epr is set also set the handle(EPR) property
        this.handle = GridUtils.getEPR(epr);
    }

    /**
     * @hibernate.property column="DESCRIPTION"
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @hibernate.property column="NAME"
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
        this.epr = handle.getAddress().toString();
    }

    /**
     * @hibernate.id generator-class="increment"
     * column="ID_KEY"
     */
    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    /**
     * @return boolean
     *         ToDo Should be Implemented by concrerte classes
     */
    public boolean isEqual(DomainObject obj) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }


    public EndpointReferenceType getHandle() {
        return handle;
    }

    /**
     *
     * @hibernate.property
     * column="ISACTIVE"
     */
    public boolean isActive() {
      return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }


}