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

import gov.nih.nci.cagrid.portal.dao.PersonDao;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.dao.catalog.GridServiceEndPointCatalogEntryDao;
import gov.nih.nci.cagrid.portal.dao.catalog.InstitutionCatalogEntryDao;
import gov.nih.nci.cagrid.portal.dao.catalog.PersonCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.Participant;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.catalog.PersonCatalogEntry;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PortalDaoAspectTest extends PortalDaoAspectTestBase {

    public PortalDaoAspectTest() {
        HttpCommandExecutor executor = (HttpCommandExecutor)getApplicationContext().getBean("defaultHttpCommandExecutor");
        PortalDaoAspect aspect = (PortalDaoAspect)getApplicationContext().getBean("deltaImportAspect");
        aspect.setExecutor(executor);

    }

    public void testDelete() {
        PersonCatalogEntry pCE = new PersonCatalogEntry();
        PersonCatalogEntryDao pCEDao = (PersonCatalogEntryDao) getApplicationContext().getBean("personCatalogEntryDao");
        pCEDao.save(pCE);
        MockHttpClient.assertJustRan();

        pCEDao.delete(pCE);
    }

//    public void testPersonCEAspect() {
//        PersonCatalogEntryDao pCEDao = (PersonCatalogEntryDao) getApplicationContext().getBean("personCatalogEntryDao");
//        pCEDao.createCatalogAbout(new PortalUser());
//
//    }

    public void testNonCEDao() throws Exception {
        Person p = new Person();
        PersonDao cEDao = (PersonDao) getApplicationContext().getBean("personDao");
        cEDao.save(p);
        assertFalse("Solr HTTP interface should not be called for non CatalogEntry DAO", MockHttpClient.assertJustRan());
        MockHttpClient httpClient = (MockHttpClient) getApplicationContext().getBean("defaultHttpClient");
        httpClient.executeMethod(null);
    }


    public void testGSCEAspect()throws Exception {
        GridServiceEndPointCatalogEntryDao cEDao = (GridServiceEndPointCatalogEntryDao) getApplicationContext().getBean("gridServiceEndPointCatalogEntryDao");
        cEDao.createCatalogAbout(new GridService());
    }

    public void testInstCEAspect() throws Exception {
        InstitutionCatalogEntryDao cEDao = (InstitutionCatalogEntryDao) getApplicationContext().getBean("institutionCatalogEntryDao");
        cEDao.createCatalogAbout(new Participant());
    }


    @Override
    protected void onTearDown() throws Exception {
        super.onTearDown();    //To change body of overridden methods use File | Settings | File Templates.

        assertTrue("Solr HTTP interface was not called", MockHttpClient.assertJustRan());
    }

    public PersonCatalogEntryDao getPersonCatalogEntryDao() {
        return personCatalogEntryDao;
    }

    public void setPersonCatalogEntryDao(PersonCatalogEntryDao personCatalogEntryDao) {
        this.personCatalogEntryDao = personCatalogEntryDao;
    }

    public PortalUserDao getPortalUserDao() {
        return portalUserDao;
    }

    public void setPortalUserDao(PortalUserDao portalUserDao) {
        this.portalUserDao = portalUserDao;
    }


}


