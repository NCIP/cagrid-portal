package gov.nih.nci.cagrid.portal.portlet.discovery.evs;

import gov.nih.nci.cagrid.portal.domain.DomainObject;

import java.util.List;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public interface ConceptCodeSearchProvider {

    public List<? extends DomainObject> search(String conceptCode);
}
