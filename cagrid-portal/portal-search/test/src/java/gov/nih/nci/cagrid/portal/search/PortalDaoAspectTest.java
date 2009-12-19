package gov.nih.nci.cagrid.portal.search;

import gov.nih.nci.cagrid.portal.dao.PersonDao;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.dao.catalog.CatalogEntryDao;
import gov.nih.nci.cagrid.portal.dao.catalog.GridServiceEndPointCatalogEntryDao;
import gov.nih.nci.cagrid.portal.dao.catalog.InstitutionCatalogEntryDao;
import gov.nih.nci.cagrid.portal.dao.catalog.PersonCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.Person;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.GridServiceEndPointCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.InstitutionCatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.PersonCatalogEntry;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PortalDaoAspectTest extends PortalDaoAspectTestBase {

    public void testDelete() {
        PersonCatalogEntry pCE = new PersonCatalogEntry();
        PersonCatalogEntryDao pCEDao = (PersonCatalogEntryDao) getApplicationContext().getBean("personCatalogEntryDao");
        pCEDao.save(pCE);
        MockHttpClient.assertJustRan();

        pCEDao.delete(pCE);
    }

    public void testPersonCEAspect() {
        PersonCatalogEntry pCE = new PersonCatalogEntry();
        PersonCatalogEntryDao pCEDao = (PersonCatalogEntryDao) getApplicationContext().getBean("personCatalogEntryDao");
        pCEDao.save(pCE);

    }

    public void testNonCEDao() throws Exception {
        Person p = new Person();
        PersonDao cEDao = (PersonDao) getApplicationContext().getBean("personDao");
        cEDao.save(p);
        assertFalse("Solr HTTP interface should not be called for non CatalogEntry DAO", MockHttpClient.assertJustRan());
        MockHttpClient httpClient = (MockHttpClient) getApplicationContext().getBean("defaultHttpClient");
        httpClient.executeMethod(null);
    }

    public void testCEAspect() {
        CatalogEntry ce = new CatalogEntry();
        CatalogEntryDao cEDao = (CatalogEntryDao) getApplicationContext().getBean("catalogEntryDao");
        cEDao.save(ce);

    }

    public void testGSCEAspect() {
        GridServiceEndPointCatalogEntry ce = new GridServiceEndPointCatalogEntry();
        GridServiceEndPointCatalogEntryDao cEDao = (GridServiceEndPointCatalogEntryDao) getApplicationContext().getBean("gridServiceEndPointCatalogEntryDao");
        cEDao.save(ce);

    }

    public void testInstCEAspect() {
        InstitutionCatalogEntry ce = new InstitutionCatalogEntry();
        InstitutionCatalogEntryDao cEDao = (InstitutionCatalogEntryDao) getApplicationContext().getBean("institutionCatalogEntryDao");
        cEDao.save(ce);

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


