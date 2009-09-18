package gov.nih.nci.cagrid.portal.catalog;

import gov.nih.nci.cagrid.portal.TestDB;
import gov.nih.nci.cagrid.portal.aggr.regsvc.RegisteredServiceEvent;
import gov.nih.nci.cagrid.portal.aggr.regsvc.RegisteredServiceListener;
import gov.nih.nci.cagrid.portal.dao.catalog.GridServiceEndPointCatalogEntryDao;
import gov.nih.nci.cagrid.portal.util.DefaultCatalogEntryRelationshipTypesFactory;
import gov.nih.nci.cagrid.portal.util.Metadata;
import gov.nih.nci.cagrid.portal.util.MetadataUtils;
import gov.nih.nci.cagrid.portal.util.PortalAggrIntegrationTestBase;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.io.FileReader;

/**
 * User: kherm
 * 
 * @author kherm manav.kher@semanticbits.com
 */
public class ServiceMetadataCatalogEntryBuilderAspectTest extends
		PortalAggrIntegrationTestBase {
	GridServiceEndPointCatalogEntryDao gridServiceEndPointCatalogEntryDao;
	RegisteredServiceListener registeredServiceListener;

	// need to load data so initializing beans can do their job
	public ServiceMetadataCatalogEntryBuilderAspectTest() throws Exception {
		super();

	}

	public void testAspect() throws Exception {

		TestDB.loadData(getDataSet());
		DefaultCatalogEntryRelationshipTypesFactory b = (DefaultCatalogEntryRelationshipTypesFactory) TestDB
				.getApplicationContext().getBean(
						"defaultCatalogEntryRelationshipTypesFactory");
		b.init();

		int initCount = gridServiceEndPointCatalogEntryDao.getAll().size();

		MetadataUtils mockUtil = mock(MetadataUtils.class);
		Metadata meta = new Metadata();
		meta.smeta = (gov.nih.nci.cagrid.metadata.MetadataUtils
				.deserializeServiceMetadata(new FileReader(
						"test/data/cabioServiceMetadata.xml")));
		meta.dmodel = (gov.nih.nci.cagrid.metadata.MetadataUtils
				.deserializeDomainModel(new FileReader(
						"test/data/cabioModelSnippet.xml")));

		doReturn(meta).when(mockUtil).getMetadata(anyString(), anyLong());
		registeredServiceListener.setMetadataUtils(mockUtil);

		RegisteredServiceEvent evt = mock(RegisteredServiceEvent.class);
		doReturn("http://").when(evt).getServiceUrl();
		doReturn("http://").when(evt).getIndexServiceUrl();

		registeredServiceListener.persistService(evt);

		// make sure catalog item is created for service
		assertEquals(gridServiceEndPointCatalogEntryDao.getAll().size()
				- initCount, 1);

	}

	@Override
	protected String[] getConfigLocations() {
		return new String[] { "classpath*:applicationContext-aggr-catalog.xml",
				"applicationContext-aggr-regsvc-beans.xml",
				"applicationContext-db-relationships.xml",
				"applicationContext-db-aspects.xml" };
	}

	protected String getDataSet() throws Exception {
		return "test/data/GridServiceEndPointCatalog.xml";
	}

	public RegisteredServiceListener getRegisteredServiceListener() {
		return registeredServiceListener;
	}

	public void setRegisteredServiceListener(
			RegisteredServiceListener registeredServiceListener) {
		this.registeredServiceListener = registeredServiceListener;
	}

	public GridServiceEndPointCatalogEntryDao getGridServiceEndPointCatalogEntryDao() {
		return gridServiceEndPointCatalogEntryDao;
	}

	public void setGridServiceEndPointCatalogEntryDao(
			GridServiceEndPointCatalogEntryDao gridServiceEndPointCatalogEntryDao) {
		this.gridServiceEndPointCatalogEntryDao = gridServiceEndPointCatalogEntryDao;
	}

}
