package gov.nih.nci.cagrid.portal.domain;

/**
 * @version 1.0
 * @created 19-Jun-2006 4:08:50 PM
 */
public interface GridService {

    public abstract java.lang.String getDescription();

    public abstract EPR getHandle();

    public abstract java.lang.String getName();

    /**
     *
     * @param desc
     */
    public abstract void setDescription(java.lang.String desc);

    /**
     *
     * @param handle
     */
    public void setHandle(java.lang.String handle);

    /**
     *
     * @param name
     */
    public abstract void setName(java.lang.String name);

}