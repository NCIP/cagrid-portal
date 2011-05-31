package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.DomainModel;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public interface CaDSRClient {
    String getContext(DomainModel domainModel) throws Exception;
}
