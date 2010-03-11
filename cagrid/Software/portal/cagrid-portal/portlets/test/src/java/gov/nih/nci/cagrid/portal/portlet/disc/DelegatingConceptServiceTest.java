package gov.nih.nci.cagrid.portal.portlet.disc;

import gov.nih.nci.cagrid.portal.portlet.CaGridPortletApplicationException;
import gov.nih.nci.cagrid.portal.portlet.discovery.evs.ConceptService;
import gov.nih.nci.cagrid.portal.portlet.discovery.evs.DelegatingConceptService;
import gov.nih.nci.cagrid.portal.portlet.discovery.evs.EVSConceptDTO;
import gov.nih.nci.cagrid.portal.portlet.discovery.evs.PortalConceptService;
import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class DelegatingConceptServiceTest extends TestCase {

    public void testService() {
        DelegatingConceptService delegatingService = new DelegatingConceptService();

        EVSConceptDTO dto1 = new EVSConceptDTO();
        dto1.setName("name1");
        dto1.setCode("code1");
        EVSConceptDTO dto2 = new EVSConceptDTO();
        dto2.setName("name2");
        dto2.setCode("code2");
        final HashSet<EVSConceptDTO> _returnSet = new HashSet<EVSConceptDTO>();
        _returnSet.add(dto1);
        _returnSet.add(dto2);


        ConceptService service = new PortalConceptService() {
            @Override
            public Set<EVSConceptDTO> getConceptsForKeyword(String keyword) throws CaGridPortletApplicationException {
                return _returnSet;
            }
        };

        delegatingService.getConceptServices().add(service);

        assertEquals(_returnSet.size(),
                delegatingService.getConceptsForKeyword("keyword").size());

        delegatingService.getConceptServices().add(service);
        assertEquals(_returnSet.size(),
                delegatingService.getConceptsForKeyword("keyword").size());

        EVSConceptDTO dto3 = new EVSConceptDTO();
        dto3.setName("name3");
        dto3.setCode("code3");
        _returnSet.add(dto3);

        assertEquals("Duplicate concepts found", 3, _returnSet.size());


    }
}
