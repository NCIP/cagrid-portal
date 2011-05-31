package gov.nih.nci.cagrid.portal.util;

import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.DomainModel;

/**
 * User: kherm
 * Dummy implementation. Returns pre-configured context
 * for all Domain Modelss
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class StaticCaDSRClient implements CaDSRClient {

    private String context;

    public String getContext(DomainModel domainModel) throws Exception {
        return context;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
