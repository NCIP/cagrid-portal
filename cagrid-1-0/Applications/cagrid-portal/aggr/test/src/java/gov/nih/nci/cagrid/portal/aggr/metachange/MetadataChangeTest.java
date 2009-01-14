/**
 *
 */
package gov.nih.nci.cagrid.portal.aggr.metachange;

import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.portal.TestDB;
import gov.nih.nci.cagrid.portal.aggr.regsvc.DomainModelBuilder;
import gov.nih.nci.cagrid.portal.aggr.regsvc.ServiceMetadataBuilder;
import gov.nih.nci.cagrid.portal.dao.*;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQuery;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.SharedCQLQuery;
import gov.nih.nci.cagrid.portal.domain.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.util.Metadata;
import gov.nih.nci.cagrid.portal.util.PortalUtils;
import junit.framework.TestCase;
import static org.mockito.Mockito.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;


/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * @author <a href="mailto:manav.kher@semanticbits.com">Manav Kher</a>
*/

public class MetadataChangeTest extends TestCase {

    private GridServiceDao gridServiceDao;
    private TestMetadataListener l;
    private MetadataChangeListener changeListener;
    private CQLQueryDao cqlQueryDao;
    private CQLQueryInstanceDao cqlQueryInstanceDao;
    private SharedCQLQueryDao sharedCqlQueryDao;


    @Override
    protected void setUp() throws Exception {
        TestDB.create();
        HibernateTemplate templ = (HibernateTemplate) TestDB
                .getApplicationContext().getBean("hibernateTemplate");

        DomainModelBuilder domainModelBuilder = new DomainModelBuilder();
        domainModelBuilder.setHibernateTemplate(templ);
        domainModelBuilder.setPersist(true);

        ServiceMetadataBuilder serviceMetadataBuilder = new ServiceMetadataBuilder();
        serviceMetadataBuilder.setHibernateTemplate(templ);
        serviceMetadataBuilder.setPersist(true);

        l = new TestMetadataListener();

        gridServiceDao = (GridServiceDao) TestDB
                .getApplicationContext().getBean("gridServiceDao");
        cqlQueryDao = (CQLQueryDao) TestDB.getApplicationContext()
                .getBean("cqlQueryDao");
        cqlQueryInstanceDao = (CQLQueryInstanceDao) TestDB
                .getApplicationContext().getBean("cqlQueryInstanceDao");
        sharedCqlQueryDao = (SharedCQLQueryDao) TestDB
                .getApplicationContext().getBean("sharedCqlQueryDao");

        changeListener = new TestMetadataListener();
        changeListener.setCqlQueryInstanceDao(cqlQueryInstanceDao);
        changeListener.setGridServiceDao(gridServiceDao);
        changeListener.setSharedCqlQueryDao(sharedCqlQueryDao);

    }

    @Override
    protected void tearDown() throws Exception {
        TestDB.drop();
    }

    public void testTransaction() throws Exception{

        String serviceUrl = "http://service.url";
        GridDataService dataService = new GridDataService();
        dataService
                .setUrl(serviceUrl);
        gridServiceDao.save(dataService);
        Metadata meta = new Metadata();

        try {
            meta.dmodel = MetadataUtils.deserializeDomainModel(new FileReader(
                    "aggr/test/data/cabioModelSnippet.xml"));
            meta.smeta = MetadataUtils
                    .deserializeServiceMetadata(new FileReader(
                            "aggr/test/data/cabioServiceMetadata.xml"));
        } catch (Exception ex) {
            fail("Error deserializing test data: " + ex.getMessage());
            ex.printStackTrace();
        }
        try {
            l.loadMetadata(dataService, meta);
        } catch (Exception ex) {
            fail("Error loading metadata: " + ex.getMessage());
            ex.printStackTrace();
        }
        GridServiceDao testDao = new TestGridServiceDao();

          testDao.setHibernateTemplate(gridServiceDao.getHibernateTemplate());
          testDao.setSessionFactory(gridServiceDao.getHibernateTemplate().getSessionFactory());
        GridServiceDao _mockDao = mock(GridServiceDao.class);

        try {
            doThrow(new RuntimeException()).when(_mockDao).save(new GridService());
            doReturn(gridServiceDao.getByUrl(serviceUrl)).when(_mockDao).getByUrl(anyString());

            changeListener.setGridServiceDao(testDao);
            changeListener.updateServiceMetadata(serviceUrl, meta);
            fail("Transaction should have failed");
        }catch (RuntimeException e){
            ServiceMetadataDao  serviceMetadataDao = (ServiceMetadataDao) TestDB
                    .getApplicationContext().getBean("serviceMetadataDao");
            ServiceMetadata loadedMetadata = serviceMetadataDao.getById(1);
            assertNull("Service metadata was not deleted",loadedMetadata.getService());

            try {
                //reattach and save
                loadedMetadata.setService(dataService);
                serviceMetadataDao.save(loadedMetadata);

                MetadataChangeListener springProvidedListener = (MetadataChangeListener)l.getApplicationContext().getBean("metadataChangeListener");

                springProvidedListener.setGridServiceDao(testDao);

                springProvidedListener.updateServiceMetadata(serviceUrl, meta);
                fail("Transaction should have been interrupted");
            } catch (RuntimeException e1) {
                assertNotSame("NPE is unexpected",e1.getClass(),NullPointerException.class);
                assertNotNull("Metadata was not persisted",serviceMetadataDao.getById(1));
                assertNotNull("Service metadata was deleted. Transaction was not applied" + e1.getMessage(),serviceMetadataDao.getById(1).getService());
            }

        } catch (Exception ex) {
            fail("Error updating metadata: " + ex.getMessage());
            ex.printStackTrace();
        }

    }



    public void testMetadataChange() {

        GridDataService dataService = new GridDataService();
        dataService
                .setUrl("http://cabio-gridservice.nci.nih.gov:80/wsrf-cabio/services/cagrid/CaBIOSvc");
        gridServiceDao.save(dataService);
        Metadata meta = new Metadata();
        try {
            meta.dmodel = MetadataUtils.deserializeDomainModel(new FileReader(
                    "aggr/test/data/cabioDomainModel.xml"));
            meta.smeta = MetadataUtils
                    .deserializeServiceMetadata(new FileReader(
                            "aggr/test/data/cabioServiceMetadata.xml"));
        } catch (Exception ex) {
            fail("Error deserializing test data: " + ex.getMessage());
            ex.printStackTrace();
        }
        try {
            l.loadMetadata(dataService, meta);
        } catch (Exception ex) {
            fail("Error loading metadata: " + ex.getMessage());
            ex.printStackTrace();
        }

        // Now associate some CQLQueryInstance and SharedCQLQuery objects
        String cql = null;
        try {
            cql = loadCQL("aggr/test/data/cabioMouseQuery.xml");
        } catch (Exception ex) {
            fail("Error loading test query: " + ex.getMessage());
            ex.printStackTrace();
        }

        CQLQuery query = new CQLQuery();
        query.setXml(cql);
        query.setHash(PortalUtils.createHash(cql));
        cqlQueryDao.save(query);
        CQLQueryInstance instance = new CQLQueryInstance();
        instance.setQuery(query);
        instance.setDataService(dataService);
        cqlQueryInstanceDao.save(instance);

        String targetClassName = "gov.nih.nci.cabio.domain.Gene";
        UMLClass targetClass = null;
        for (UMLClass klass : dataService.getDomainModel().getClasses()) {
            String className = klass.getPackageName() + "."
                    + klass.getClassName();
            if (className.equals(targetClassName)) {
                targetClass = klass;
                break;
            }
        }
        SharedCQLQuery sharedCqlQuery = new SharedCQLQuery();
        sharedCqlQuery.setTargetClass(targetClass);
        sharedCqlQuery.setTargetService(dataService);
        sharedCqlQueryDao.save(sharedCqlQuery);

        verify(dataService, sharedCqlQueryDao, cqlQueryInstanceDao,
                targetClassName);

        try {
            changeListener.updateServiceMetadata(dataService.getUrl(), meta);
        } catch (Exception ex) {
            fail("Error updating metadata: " + ex.getMessage());
            ex.printStackTrace();
        }

        verify(dataService, sharedCqlQueryDao, cqlQueryInstanceDao,
                targetClassName);

    }

    private void verify(GridDataService dataService,
                        SharedCQLQueryDao sharedCqlQueryDao,
                        CQLQueryInstanceDao cqlQueryInstanceDao, String targetClassName) {
        List<SharedCQLQuery> sharedCqlQueries = sharedCqlQueryDao
                .getByDataService(dataService);
        assertTrue("expected to find one shared query for this service, found "
                + sharedCqlQueries.size(), sharedCqlQueries.size() == 1);
        SharedCQLQuery sharedCqlQuery2 = sharedCqlQueries.get(0);
        assertNotNull("shared query has no target class", sharedCqlQuery2
                .getTargetClass());
        assertNotNull("shared query has no target service", sharedCqlQuery2
                .getTargetService());
        assertEquals("exected to find gene as target class", targetClassName,
                sharedCqlQuery2.getTargetClass().getPackageName() + "."
                        + sharedCqlQuery2.getTargetClass().getClassName());

        List<CQLQueryInstance> cqlQueryInstances = cqlQueryInstanceDao
                .getByDataService(dataService);
        assertTrue("expected to find one cql query instance", cqlQueryInstances
                .size() == 1);
    }

    private String loadCQL(String fileName) throws Exception {
        StringBuilder sb = new StringBuilder();
        BufferedReader r = new BufferedReader(new FileReader(fileName));
        String line = null;
        while ((line = r.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    private static class TestMetadataListener extends MetadataChangeListener {

        public void onApplicationEvent(ApplicationEvent event) {

        }

        public void loadMetadata(GridDataService service, Metadata meta)
                throws Exception {
            setMetadata(service, meta);
        }

        @Override
        public ApplicationContext getApplicationContext() {
            return new ClassPathXmlApplicationContext(
                    new String[]{"applicationContext-aggr.xml","applicationContext-db.xml",
                    });
//            return TestDB.getApplicationContext();
        }
    }

    public class TestGridServiceDao extends GridServiceDao{
        @Override
        public void save(GridService domainObject) {
            throw new RuntimeException();
        }
    }

}
