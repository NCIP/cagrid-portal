package gov.nih.nci.cagrid.portal.portlet.disc;

import gov.nih.nci.cagrid.portal.portlet.PortletIntegrationTestBase;
import gov.nih.nci.cagrid.portal.portlet.discovery.evs.EVSConceptDTO;
import gov.nih.nci.cagrid.portal.portlet.discovery.evs.EVSService;
import gov.nih.nci.cagrid.portal.portlet.discovery.evs.LexbigEVSService;
import org.apache.log4j.Logger;

import java.util.Set;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class EVSServiceTest extends PortletIntegrationTestBase {

    protected EVSService caCoreEVSService;
    protected LexbigEVSService lexbigEVSService;

    private Logger log = Logger.getLogger(EVSServiceTest.class);

    public EVSServiceTest() {
        setAutowireMode(AUTOWIRE_BY_NAME);
    }

    /**
     * Will use the default evs service
     */
    public void testcaCoreEVSKeywordSearch(){

        Long startTime = EVSServiceTest.getTimestamp();
        Set<EVSConceptDTO> resultSet = caCoreEVSService.getConceptsForKeyword("Taxon");
        log.debug("Search for Taxon");

        log.debug("Resultset size is " + resultSet.size());
        assertNotNull("Resultset returned is null",resultSet);
        assertTrue("Resultset size is 0", resultSet.size()>0);

        for(EVSConceptDTO result:resultSet){
            log.debug("Concept name: " + result.getName() + " , Concept Code:" + result.getCode() );
            assertTrue("EVS concept does not have complete information",result.getCode()!=null);
            assertTrue("EVS concept does not have complete information",result.getName()!=null);
        }

        Long endTime = EVSServiceTest.getTimestamp();
        log.debug("Approx Total time taken using caCORE API: " + (endTime-startTime) + " milliseconds");

    }

    public void testcaCoreEVSWildcardSearch1(){

        Long startTime = EVSServiceTest.getTimestamp();
        Set<EVSConceptDTO> resultSet = caCoreEVSService.getConceptsForKeyword("Tax*");
        log.debug("Search for Tax*");

        log.debug("Resultset size is " + resultSet.size());
        assertNotNull("Resultset returned is null",resultSet);
        assertTrue("Resultset size is 0", resultSet.size()>0);

        for(EVSConceptDTO result:resultSet){
            log.debug("Concept name: " + result.getName() + " , Concept Code:" + result.getCode() );

            assertTrue("EVS concept does not have complete information",result.getCode()!=null);
            assertTrue("EVS concept does not have complete information",result.getName()!=null);
        }
        Long endTime = EVSServiceTest.getTimestamp();
        log.debug("Approx Total time taken using caCORE API: " + (endTime-startTime) + " milliseconds");
    }

    public void testcaCoreEVSWildcardSearch2(){
        Long startTime = EVSServiceTest.getTimestamp();

        Set<EVSConceptDTO> resultSet = caCoreEVSService.getConceptsForKeyword("Ta*");
        log.debug("Search for Ta*");

        log.debug("Resultset size is " + resultSet.size());
        assertNotNull("Resultset returned is null",resultSet);
        assertTrue("Resultset size is 0", resultSet.size()>0);

        for(EVSConceptDTO result:resultSet){
            log.debug("Concept name: " + result.getName() + " , Concept Code:" + result.getCode() );

            assertTrue("EVS concept does not have complete information",result.getCode()!=null);
            assertTrue("EVS concept does not have complete information",result.getName()!=null);
        }

        Long endTime = EVSServiceTest.getTimestamp();
        log.debug("Approx Total time taken using caCORE API: " + (endTime-startTime) + " milliseconds");


    }

    public void testcaCoreWildcardSearch3(){
        Long startTime = EVSServiceTest.getTimestamp();

        Set<EVSConceptDTO> resultSet = caCoreEVSService.getConceptsForKeyword("T*");
        log.debug("Search for T*");

        log.debug("Resultset size is " + resultSet.size());
        assertNotNull("Resultset returned is null",resultSet);
        assertTrue("Resultset size is 0", resultSet.size()>0);

        for(EVSConceptDTO result:resultSet){
            log.debug("Concept name: " + result.getName() + " , Concept Code:" + result.getCode() );

            assertTrue("EVS concept does not have complete information",result.getCode()!=null);
            assertTrue("EVS concept does not have complete information",result.getName()!=null);
        }

        Long endTime = EVSServiceTest.getTimestamp();
        log.debug("Approx Total time taken using caCORE API: " + (endTime-startTime) + " milliseconds");
    }

    public void testLexBIGKeywordWithLexbig(){
        Long startTime = EVSServiceTest.getTimestamp();
        Set<EVSConceptDTO> resultSet = lexbigEVSService.getConceptsForKeyword("Name");
        log.debug("Search for Taxon");

        log.debug("Resultset size is " + resultSet.size());
        assertNotNull("Resultset returned is null",resultSet);
        assertTrue("Resultset size is 0", resultSet.size()>0);

        for(EVSConceptDTO result:resultSet){
            log.debug("Concept name: " + result.getName() + " , Concept Code:" + result.getCode() );
            assertTrue("EVS concept does not have complete information",result.getCode()!=null);
            assertTrue("EVS concept does not have complete information",result.getName()!=null);
        }

        Long endTime = EVSServiceTest.getTimestamp();
        log.debug("Approx Total time taken using caCORE API: " + (endTime-startTime) + " milliseconds");


    }



    private static Long getTimestamp(){
        return System.currentTimeMillis();
    }

    public void setCaCoreEVSService(EVSService caCoreEVSService) {
        this.caCoreEVSService = caCoreEVSService;
    }

    public void setLexbigEVSService(LexbigEVSService lexbigEVSService) {
        this.lexbigEVSService = lexbigEVSService;
    }
}
