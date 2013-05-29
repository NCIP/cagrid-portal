/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.search;

import gov.nih.nci.cagrid.portal.dao.catalog.InstitutionCatalogEntryDao;
import gov.nih.nci.cagrid.portal.dao.catalog.GridServiceEndPointCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.Participant;
import gov.nih.nci.cagrid.portal.domain.GridService;

/**
 * Tests use the caching executor
 * 
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PortalDaoAspectCachingExecutorTest extends PortalDaoAspectTest {

    public PortalDaoAspectCachingExecutorTest() {
        HttpCommandExecutor executor = (HttpCommandExecutor)getApplicationContext().getBean("cachingHttpCommandExecutor");
        PortalDaoAspect aspect = (PortalDaoAspect)getApplicationContext().getBean("deltaImportAspect");
        aspect.setExecutor(executor);
    }

    public void testMultipleCalls() {
        InstitutionCatalogEntryDao cEDao = (InstitutionCatalogEntryDao) getApplicationContext().getBean("institutionCatalogEntryDao");
        cEDao.createCatalogAbout(new Participant());

        GridServiceEndPointCatalogEntryDao gsDao = (GridServiceEndPointCatalogEntryDao) getApplicationContext().getBean("gridServiceEndPointCatalogEntryDao");
        gsDao.createCatalogAbout(new GridService());

// solr update shld only be called once       
    }

    @Override
    protected void onTearDown() throws Exception {
        Thread.currentThread().sleep(10000);
        super.onTearDown();
    }
}
