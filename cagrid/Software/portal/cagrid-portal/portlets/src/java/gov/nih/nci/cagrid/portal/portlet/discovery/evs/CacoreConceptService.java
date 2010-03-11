package gov.nih.nci.cagrid.portal.portlet.discovery.evs;

import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;
import gov.nih.nci.evs.domain.DescLogicConcept;
import gov.nih.nci.evs.query.EVSQuery;
import gov.nih.nci.evs.query.EVSQueryImpl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class CacoreConceptService extends BaseConceptService {


    /**
     * Will return concept codes for any given keyword.
     *
     * @param keyword
     * @return
     * @throws CaGridPortletApplicationException
     *
     */
    public Set<EVSConceptDTO> getConceptsForKeyword(String keyword) throws CaGridPortletApplicationException {
        Set<EVSConceptDTO> resultSet = new HashSet<EVSConceptDTO>();

        try {
            List evsResults = new ArrayList();
            EVSQuery evsSearch = new EVSQueryImpl();
            // Perform query: Assume no data is returned
            if (logger.isDebugEnabled())
                logger.debug("Calling evsSearch");

            evsSearch.searchDescLogicConcepts(vocabulary, keyword + "*", searchLimit, 0, "", 3);
            evsResults = appService.evsSearch(evsSearch);

            // Return data

            gov.nih.nci.evs.domain.DescLogicConcept[] concepts = null;

            if (evsResults != null && evsResults.size() > 0) {
                if (logger.isDebugEnabled())
                    logger.debug("Returning Result count: " + evsResults.size());
                concepts = new gov.nih.nci.evs.domain.DescLogicConcept[evsResults.size()];
                System.arraycopy(evsResults.toArray(), 0, concepts, 0, evsResults.size());

                if (concepts != null && concepts.length > 0) {
                    for (DescLogicConcept concept : concepts) {
                        if (concept != null) {
                            if (logger.isDebugEnabled())
                                logger.debug("concept=" + concept.getName());
                            EVSConceptDTO dto = new EVSConceptDTO(concept);
                            resultSet.add(dto);
                        }
                    }
                }
            } else {
                logger.debug("There are no results to return!");
            }
        } catch (Exception e) {
            logger.warn("Error getting evs search results");
            logger.warn(e.getMessage());
            throw new CaGridPortletApplicationException("Could not get evs search results", e);
        }
        return resultSet;
    }


}



