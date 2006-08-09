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
public interface GridService extends DomainObject {
    public void setPk(Integer pk);

    public Integer getPk();

    public String getDescription();

    public void setDescription(String description);

    public EndpointReferenceType getHandle();

    public void setHandle(EndpointReferenceType handle);

    public String getEPR();

    public String getName();

    public void setName(String name);

    public boolean isActive();
}
