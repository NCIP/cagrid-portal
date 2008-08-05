/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.metachange;

import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.portal.TestDB;
import gov.nih.nci.cagrid.portal.aggr.AbstractMetadataListener;
import gov.nih.nci.cagrid.portal.aggr.regsvc.DomainModelBuilder;
import gov.nih.nci.cagrid.portal.aggr.regsvc.ServiceMetadataBuilder;
import gov.nih.nci.cagrid.portal.dao.CQLQueryDao;
import gov.nih.nci.cagrid.portal.dao.CQLQueryInstanceDao;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.SharedCQLQueryDao;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQuery;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.SharedCQLQuery;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.util.Metadata;
import gov.nih.nci.cagrid.portal.util.PortalUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import junit.framework.TestCase;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class MetadataChangeTest extends TestCase {

	/**
	 * 
	 */
	public MetadataChangeTest() {

	}

	/**
	 * @param name
	 */
	public MetadataChangeTest(String name) {
		super(name);

	}

	public void testMetadataChange() {
		TestDB.create();

		HibernateTemplate templ = (HibernateTemplate) TestDB
				.getApplicationContext().getBean("hibernateTemplate");

		DomainModelBuilder domainModelBuilder = new DomainModelBuilder();
		domainModelBuilder.setHibernateTemplate(templ);
		domainModelBuilder.setPersist(true);

		ServiceMetadataBuilder serviceMetadataBuilder = new ServiceMetadataBuilder();
		serviceMetadataBuilder.setHibernateTemplate(templ);
		serviceMetadataBuilder.setPersist(true);

		TestMetadataListener l = new TestMetadataListener();
		
//		l.setDomainModelBuilder(domainModelBuilder);
//		l.setServiceMetadataBuilder(serviceMetadataBuilder);

		GridServiceDao gridServiceDao = (GridServiceDao) TestDB
				.getApplicationContext().getBean("gridServiceDao");
		CQLQueryDao cqlQueryDao = (CQLQueryDao) TestDB.getApplicationContext()
				.getBean("cqlQueryDao");
		CQLQueryInstanceDao cqlQueryInstanceDao = (CQLQueryInstanceDao) TestDB
				.getApplicationContext().getBean("cqlQueryInstanceDao");
		SharedCQLQueryDao sharedCqlQueryDao = (SharedCQLQueryDao) TestDB
				.getApplicationContext().getBean("sharedCqlQueryDao");

		MetadataChangeListener changeListener = new TestMetadataListener();
		changeListener.setCqlQueryInstanceDao(cqlQueryInstanceDao);
//		changeListener.setDomainModelBuilder(domainModelBuilder);
		changeListener.setGridServiceDao(gridServiceDao);
//		changeListener.setServiceMetadataBuilder(serviceMetadataBuilder);
		changeListener.setSharedCqlQueryDao(sharedCqlQueryDao);

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

		TestDB.drop();
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
                new String[]{"applicationContext-aggr.xml","applicationContext-db.xml"});
//            return TestDB.getApplicationContext();
        }
    }

}
