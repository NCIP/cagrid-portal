package gov.nih.nci.cagrid.portal.domain;

import org.apache.axis.message.addressing.EndpointReferenceType;


/**
 * Interface to be implemented
 * by any GridService type
 * class
 *
 * @version 1.0
 * @created 19-Jun-2006 4:08:50 PM
 */
public interface GridService {
    public String getDescription();

    public EndpointReferenceType getHandle();

    public String getName();

    public boolean isActive();
}
