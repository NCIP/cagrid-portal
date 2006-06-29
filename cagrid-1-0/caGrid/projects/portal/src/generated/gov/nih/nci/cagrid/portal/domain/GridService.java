package gov.nih.nci.cagrid.portal.domain;

import org.apache.axis.message.addressing.EndpointReferenceType;

/**
 * @version 1.0
 * @created 19-Jun-2006 4:08:50 PM
 */
public interface GridService {

    public java.lang.String getDescription();

    public EndpointReferenceType getHandle();

    public java.lang.String getName();

    /**
     *
     * @param desc
     */
    public void setDescription(java.lang.String desc);

    /**
     *
     * @param handle
     */
    public void setHandle(EndpointReferenceType handle);

    /**
     *
     * @param name
     */
    public void setName(java.lang.String name);

}