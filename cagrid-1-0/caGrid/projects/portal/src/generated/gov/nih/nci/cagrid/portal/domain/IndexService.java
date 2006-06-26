package gov.nih.nci.cagrid.portal.domain;

import java.util.List;

/**
 * Represents a Index Service Instance
 *
 * @version 1.0
 * @created 19-Jun-2006 4:08:50 PM
 * @hibernate.class table="INDEX_SERVICE"
 */
public class IndexService implements GridService {

    private List registeredServicesCollection;
    private String epr;
    private String description;
    private String name;
    private EPR handle;

    /**
     * Needed for hibernate
     */
    public IndexService() {

    }


    /**
     * @hibernate.collection-many-to-many class RegisteredService
     */
    public List getRegisteredServicesCollection() {
        return registeredServicesCollection;
    }

    public void setRegisteredServicesCollection(List registeredServicesCollection) {
        this.registeredServicesCollection = registeredServicesCollection;
    }

    /**
     * @hibernate.id type string
     * column EPR
     * generator assigned
     */
    public String getEpr() {
        return epr;
    }

    public void setEpr(String epr) {
        this.epr = epr;
        this.handle = new EPR(epr);
    }

    /**
     * @hibernate.property column DESCRIPTION
     * type string
     */
    public String getDescription() {
        return this.description;
    }


    public EPR getHandle() {
        return this.handle;
    }


    /**
     * @hibernate.property column NAME
     * type string
     */
    public String getName() {
        return this.name;

    }

    /**
     * @param desc
     */
    public void setDescription(String desc) {
        this.description = desc;
    }

    /**
     * @param handle
     */
    public void setHandle(EPR handle) {
        this.handle = handle;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param handle
     */
    public void setHandle(String handle) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

}