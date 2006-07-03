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

    // Primary key
    private int key;

    public IndexService(String epr) throws URI.MalformedURIException {
        //Use setter to throw appropriate exception
        this.setEpr(epr);

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
     * @hibernate.property column="SERVICE_EPR"
     * not-null="true"
     */
    public String getEpr() {
        return epr;
    }


    /**
     * @throws URI.MalformedURIException Will throw an exception if epr string is not a valid URI
     */
    public void setEpr(String epr) throws URI.MalformedURIException {
        this.epr = epr;

        // Once epr is set also set the handle property
        setHandle(GridUtils.getEPR(epr));
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

    public EndpointReferenceType getHandle() {
        return handle;
    }

    public void setHandle(EndpointReferenceType handle) {
        this.handle = handle;
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

    /**
     * @return Integer primary key
     *         ToDo Should be Implemented by concrete classes
     */
    public Integer getPK() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

}