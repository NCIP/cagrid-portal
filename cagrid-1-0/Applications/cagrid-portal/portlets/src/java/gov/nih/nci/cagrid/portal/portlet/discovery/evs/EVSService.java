package gov.nih.nci.cagrid.portal.portlet.discovery.evs;

import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;

import java.util.Set;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public interface EVSService {

    Set<EVSConceptDTO> getConceptsForKeyword(String keyword) throws CaGridPortletApplicationException;
}
