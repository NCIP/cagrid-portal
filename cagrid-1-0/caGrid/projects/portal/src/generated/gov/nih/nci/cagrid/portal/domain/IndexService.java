package gov.nih.nci.cagrid.portal.domain;

import java.util.Collection;

/**
 * Represents a Index Service Instance
 * @version 1.0
 * @created 19-Jun-2006 3:47:13 PM
 */
public class IndexService implements GridService {

    private boolean active;
    private java.util.Collection registeredServicesCollection;

    public IndexService(){

    }

    public void finalize() throws Throwable {

    }

    public boolean isActive(){
        return active;
    }

    /**
     *
     * @param newVal
     */
    public void setActive(boolean newVal){
        active = newVal;
    }

    public EPR getHandle(){
        return null;
    }

    public java.lang.String getName(){
        return null;
    }

    public java.lang.String getDescription(){
        return null;
    }

    /**
     *
     * @param desc
     */
    public void setDescription(java.lang.String desc){

    }

    /**
     *
     * @param name
     */
    public void setName(java.lang.String name){

    }

    /**
     *
     * @param handle
     */
    public void setHandle(java.lang.String handle){

    }

    public Collection getRegisteredServicesCollection() {
        return registeredServicesCollection;
    }

    public void setRegisteredServicesCollection(Collection registeredServicesCollection) {
        this.registeredServicesCollection = registeredServicesCollection;
    }

}