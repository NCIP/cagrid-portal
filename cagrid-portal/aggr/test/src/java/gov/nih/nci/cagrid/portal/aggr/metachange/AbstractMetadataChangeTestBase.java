package gov.nih.nci.cagrid.portal.aggr.metachange;

import gov.nih.nci.cagrid.portal.TestDB;
import gov.nih.nci.cagrid.portal.aggr.regsvc.DomainModelBuilder;
import gov.nih.nci.cagrid.portal.aggr.regsvc.ServiceMetadataBuilder;
import gov.nih.nci.cagrid.portal.dao.CQLQueryDao;
import gov.nih.nci.cagrid.portal.dao.CQLQueryInstanceDao;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.SharedCQLQueryDao;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.SharedCQLQuery;
import gov.nih.nci.cagrid.portal.util.Metadata;
import static junit.framework.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.orm.hibernate3.HibernateTemplate;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

public abstract class AbstractMetadataChangeTestBase {
	private GridServiceDao gridServiceDao;
	private TestMetadataListener metadataListener;
	private TestMetadataListener changeListener;
	private CQLQueryDao cqlQueryDao;
	private CQLQueryInstanceDao cqlQueryInstanceDao;
	private SharedCQLQueryDao sharedCqlQueryDao;

	public AbstractMetadataChangeTestBase() {
	}

	protected String[] getConfigLocations() {
		return new String[] { "applicationContext-db.xml",
				"applicationContext-aggr.xml" };
	}


	@Before
	public void setUp() throws Exception {
		TestDB.create();
		HibernateTemplate templ = (HibernateTemplate) TestDB
				.getApplicationContext().getBean("hibernateTemplate");

		DomainModelBuilder domainModelBuilder = new DomainModelBuilder();
		domainModelBuilder.setHibernateTemplate(templ);
		domainModelBuilder.setPersist(true);

		ServiceMetadataBuilder serviceMetadataBuilder = new ServiceMetadataBuilder();
		serviceMetadataBuilder.setHibernateTemplate(templ);
		serviceMetadataBuilder.setPersist(true);

		metadataListener = new TestMetadataListener(getConfigLocations());

		gridServiceDao = (GridServiceDao) TestDB.getApplicationContext()
				.getBean("gridServiceDao");
		cqlQueryDao = (CQLQueryDao) TestDB.getApplicationContext().getBean(
				"cqlQueryDao");
		cqlQueryInstanceDao = (CQLQueryInstanceDao) TestDB
				.getApplicationContext().getBean("cqlQueryInstanceDao");
		sharedCqlQueryDao = (SharedCQLQueryDao) TestDB.getApplicationContext()
				.getBean("sharedCqlQueryDao");

		changeListener = new TestMetadataListener(getConfigLocations());
		changeListener.setCqlQueryInstanceDao(cqlQueryInstanceDao);
		changeListener.setGridServiceDao(gridServiceDao);
		changeListener.setSharedCqlQueryDao(sharedCqlQueryDao);

	}

	@After
	public void tearDown() throws Exception {
		TestDB.drop();
	}

	protected void verify(GridDataService dataService,
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

	String loadCQL(String fileName) throws Exception {
		StringBuilder sb = new StringBuilder();
		BufferedReader r = new BufferedReader(new FileReader(fileName));
		String line = null;
		while ((line = r.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

	protected static class TestMetadataListener extends MetadataChangeListener {
		
		public String[] configLocations;
		
		TestMetadataListener(String[] configLocations){
			this.configLocations = configLocations;
		}

		public void onApplicationEvent(ApplicationEvent event) {

		}

		public void loadMetadata(GridDataService service, Metadata meta)
				throws Exception {
			setMetadata(service, meta);
		}

		@Override
		public ApplicationContext getApplicationContext() {
			return new ClassPathXmlApplicationContext(this.configLocations);
		}
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public CQLQueryDao getCqlQueryDao() {
		return cqlQueryDao;
	}

	public CQLQueryInstanceDao getCqlQueryInstanceDao() {
		return cqlQueryInstanceDao;
	}

	public SharedCQLQueryDao getSharedCqlQueryDao() {
		return sharedCqlQueryDao;
	}

	public TestMetadataListener getChangeListener() {
		return changeListener;
	}

	public TestMetadataListener getMetadataListener() {
		return metadataListener;
	}
}