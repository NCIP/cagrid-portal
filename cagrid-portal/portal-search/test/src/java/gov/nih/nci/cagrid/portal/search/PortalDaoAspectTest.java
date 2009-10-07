package gov.nih.nci.cagrid.portal.search;

import gov.nih.nci.cagrid.portal.DBIntegrationTestBase;
import gov.nih.nci.cagrid.portal.dao.PortalUserDao;
import gov.nih.nci.cagrid.portal.dao.PersonDao;
import gov.nih.nci.cagrid.portal.dao.catalog.*;
import gov.nih.nci.cagrid.portal.domain.catalog.*;
import gov.nih.nci.cagrid.portal.domain.DomainObject;
import gov.nih.nci.cagrid.portal.domain.AbstractDomainObject;
import gov.nih.nci.cagrid.portal.domain.Person;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PortalDaoAspectTest extends DBIntegrationTestBase {


    PersonCatalogEntryDao personCatalogEntryDao;
    PortalUserDao portalUserDao;

    @Override
    protected ConfigurableApplicationContext loadContext(Object o) throws Exception {
        GenericApplicationContext ctx = new GenericApplicationContext();

        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
        xmlReader.loadBeanDefinitions(new ClassPathResource("applicationContext-db.xml"));
        xmlReader.loadBeanDefinitions(new ClassPathResource("applicationContext-portal-search-aspects.xml"));

        ctx.registerBeanDefinition("defaultHttpClient", new RootBeanDefinition(MockHttpClient.class));
        ctx.refresh();


        return ctx;
    }


    public void testDelete(){
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

    public void testNonCEDao() throws Exception{
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


    static class MockHttpClient extends HttpClient {

        private static boolean assertRan = false;

        public MockHttpClient() {
        }


        @Override
        public int executeMethod(HttpMethod httpMethod) throws IOException, HttpException {
            assertRan = true;
            return 0;
        }

        public static boolean assertJustRan() {
            if (assertRan) {
                assertRan = false;
                return !assertRan;
            }
            return assertRan;

        }
    }

}


