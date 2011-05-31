package gov.nih.nci.cagrid.portal.portlet.disc;

import gov.nih.nci.cagrid.portal.portlet.discovery.evs.ConceptService;
import gov.nih.nci.cagrid.portal.portlet.discovery.evs.EVSConceptDTO;
import gov.nih.nci.cagrid.portal.portlet.discovery.evs.LexbigConceptService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import java.util.Set;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class AbstractEVSServiceSystemTest extends AbstractDependencyInjectionSpringContextTests {

    @Override
    protected String[] getConfigLocations() {
        return new String[]{
                "classpath*:applicationContext-portlets-test.xml",
        };
    }

    protected ConceptService caCoreConceptService;
    protected LexbigConceptService lexbigEVSService;

    private Log log = LogFactory.getLog(AbstractEVSServiceSystemTest.class);

    public AbstractEVSServiceSystemTest() {
        setAutowireMode(AUTOWIRE_BY_NAME);
    }

    /**
     * Will use the default evs service
     */
    public void testcaCoreEVSKeywordSearch() {

        Long startTime = AbstractEVSServiceSystemTest.getTimestamp();

        String keyword = "Taxon";
        Set<EVSConceptDTO> resultSet = caCoreConceptService.getConceptsForKeyword(keyword);
        log.debug("Search for" + keyword);

        log.debug("Resultset size is " + resultSet.size());
        assertNotNull("Resultset returned is null", resultSet);
        assertTrue("Resultset size is 0", resultSet.size() > 0);

        for (EVSConceptDTO result : resultSet) {
            log.debug("Concept name: " + result.getName() + " , Concept Code:" + result.getCode());
            assertTrue("EVS concept does not have complete information", result.getCode() != null);
            assertTrue("EVS concept does not have complete information", result.getName() != null);
        }

        Long endTime = AbstractEVSServiceSystemTest.getTimestamp();
        log.debug("Approx Total time taken using caCORE API: " + (endTime - startTime) + " milliseconds");

    }

    private void assertNotNull(String s, Set<EVSConceptDTO> resultSet) {
        //To change body of created methods use File | Settings | File Templates.
    }

    public void testCaseEVSSearch() {
        caseSearch(caCoreConceptService);
        caseSearch(lexbigEVSService);
    }

    private void caseSearch(ConceptService service) {
        Long startTime = AbstractEVSServiceSystemTest.getTimestamp();

        String lcKeyword = "taxon";
        String uckeyword = "TAXON";

        Set<EVSConceptDTO> lcResultSet = service.getConceptsForKeyword(lcKeyword);

        log.debug("Search for" + lcKeyword);

        log.debug("Lowercase Resultset size is " + lcResultSet.size());
        assertNotNull("Lowercase Resultset returned is null", lcResultSet);
        assertTrue("Lowercase Resultset size is 0", lcResultSet.size() > 0);

        for (EVSConceptDTO result : lcResultSet) {
            log.debug("Concept name: " + result.getName() + " , Concept Code:" + result.getCode());
            assertTrue("EVS concept does not have complete information", result.getCode() != null);
            assertTrue("EVS concept does not have complete information", result.getName() != null);
        }

        Long endTime = AbstractEVSServiceSystemTest.getTimestamp();
        log.debug("Approx Total time taken using caCORE API: " + (endTime - startTime) + " milliseconds");

        startTime = AbstractEVSServiceSystemTest.getTimestamp();
        Set<EVSConceptDTO> ucResultSet = service.getConceptsForKeyword(uckeyword);
        log.debug("Search for" + lcKeyword);

        log.debug("Uppercase Resultset size is " + ucResultSet.size());
        assertNotNull("Uppercase Resultset returned is null", ucResultSet);
        assertTrue("Uppercase Resultset size is 0", ucResultSet.size() > 0);

        for (EVSConceptDTO result : ucResultSet) {
            log.debug("Concept name: " + result.getName() + " , Concept Code:" + result.getCode());
            assertTrue("EVS concept does not have complete information", result.getCode() != null);
            assertTrue("EVS concept does not have complete information", result.getName() != null);
            boolean wasFound = false;
            for (EVSConceptDTO lcResult : lcResultSet) {
                if (lcResult.getCode().equals(result.getCode())) {
                    wasFound = true;
                    break;
                }
            }
            assertTrue("Concept found in upper case search not found in lower case search",
                    wasFound);
        }

        endTime = AbstractEVSServiceSystemTest.getTimestamp();
        log.debug("Approx Total time taken using caCORE API: " + (endTime - startTime) + " milliseconds");

        assertEquals("Case matters in search", lcResultSet.size(), ucResultSet.size());

    }

    public void testcaCoreEVSWildcardSearch1() {

        Long startTime = AbstractEVSServiceSystemTest.getTimestamp();
        Set<EVSConceptDTO> resultSet = caCoreConceptService.getConceptsForKeyword("Tax*");
        log.debug("Search for Tax*");

        log.debug("Resultset size is " + resultSet.size());
        assertNotNull("Resultset returned is null", resultSet);
        assertTrue("Resultset size is 0", resultSet.size() > 0);

        for (EVSConceptDTO result : resultSet) {
            log.debug("Concept name: " + result.getName() + " , Concept Code:" + result.getCode());

            assertTrue("EVS concept does not have complete information", result.getCode() != null);
            assertTrue("EVS concept does not have complete information", result.getName() != null);
        }
        Long endTime = AbstractEVSServiceSystemTest.getTimestamp();
        log.debug("Approx Total time taken using caCORE API: " + (endTime - startTime) + " milliseconds");
    }

    public void testcaCoreEVSWildcardSearch2() {
        Long startTime = AbstractEVSServiceSystemTest.getTimestamp();

        Set<EVSConceptDTO> resultSet = caCoreConceptService.getConceptsForKeyword("Ta*");
        log.debug("Search for Ta*");

        log.debug("Resultset size is " + resultSet.size());
        assertNotNull("Resultset returned is null", resultSet);
        assertTrue("Resultset size is 0", resultSet.size() > 0);

        for (EVSConceptDTO result : resultSet) {
            log.debug("Concept name: " + result.getName() + " , Concept Code:" + result.getCode());

            assertTrue("EVS concept does not have complete information", result.getCode() != null);
            assertTrue("EVS concept does not have complete information", result.getName() != null);
        }

        Long endTime = AbstractEVSServiceSystemTest.getTimestamp();
        log.debug("Approx Total time taken using caCORE API: " + (endTime - startTime) + " milliseconds");


    }

    public void testcaCoreWildcardSearch3() {
        Long startTime = AbstractEVSServiceSystemTest.getTimestamp();

        Set<EVSConceptDTO> resultSet = caCoreConceptService.getConceptsForKeyword("T*");
        log.debug("Search for T*");

        log.debug("Resultset size is " + resultSet.size());
        assertNotNull("Resultset returned is null", resultSet);
        assertTrue("Resultset size is 0", resultSet.size() > 0);

        for (EVSConceptDTO result : resultSet) {
            log.debug("Concept name: " + result.getName() + " , Concept Code:" + result.getCode());

            assertTrue("EVS concept does not have complete information", result.getCode() != null);
            assertTrue("EVS concept does not have complete information", result.getName() != null);
        }

        Long endTime = AbstractEVSServiceSystemTest.getTimestamp();
        log.debug("Approx Total time taken using caCORE API: " + (endTime - startTime) + " milliseconds");
    }

    public void testLexBIGKeywordWithLexbig() {
        Long startTime = AbstractEVSServiceSystemTest.getTimestamp();
        Set<EVSConceptDTO> resultSet = lexbigEVSService.getConceptsForKeyword("Name");
        log.debug("Search for Taxon");

        log.debug("Resultset size is " + resultSet.size());
        assertNotNull("Resultset returned is null", resultSet);
        assertTrue("Resultset size is 0", resultSet.size() > 0);

        for (EVSConceptDTO result : resultSet) {
            log.debug("Concept name: " + result.getName() + " , Concept Code:" + result.getCode());
            assertTrue("EVS concept does not have complete information", result.getCode() != null);
            assertTrue("EVS concept does not have complete information", result.getName() != null);
        }

        Long endTime = AbstractEVSServiceSystemTest.getTimestamp();
        log.debug("Approx Total time taken using caCORE API: " + (endTime - startTime) + " milliseconds");


    }


    private static Long getTimestamp() {
        return System.currentTimeMillis();
    }

    public ConceptService getCaCoreConceptService() {
        return caCoreConceptService;
    }

    public void setCaCoreConceptService(ConceptService caCoreConceptService) {
        this.caCoreConceptService = caCoreConceptService;
    }

    public LexbigConceptService getLexbigEVSService() {
        return lexbigEVSService;
    }

    public void setLexbigEVSService(LexbigConceptService lexbigEVSService) {
        this.lexbigEVSService = lexbigEVSService;
    }
}
