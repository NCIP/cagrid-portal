/**
 * 
 */
package gov.nih.nci.cagrid.portal2.dao;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.portal2.aggr.regsvc.ServiceMetadataBuilder;
import gov.nih.nci.cagrid.portal2.domain.GridService;
import gov.nih.nci.cagrid.portal2.domain.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal2.domain.metadata.service.InputParameter;
import gov.nih.nci.cagrid.portal2.domain.metadata.service.Operation;
import gov.nih.nci.cagrid.portal2.domain.metadata.service.Service;
import gov.nih.nci.cagrid.portal2.domain.metadata.service.ServiceContext;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.List;

import org.dbunit.operation.DatabaseOperation;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ServiceMetadataDaoTest extends AbstractDaoTest {

	protected DatabaseOperation getSetUpOperation() throws Exception {
		return DatabaseOperation.CLEAN_INSERT;
	}

	protected DatabaseOperation getTearDownOperation() throws Exception {
		return DatabaseOperation.DELETE_ALL;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal2.dao.AbstractDaoTest#getDataSetFileName()
	 */
	@Override
	protected String getDataSetFileName() {
		return getDataSetFileName("test/data/ServiceMetadataDaoTest.xml");
	}


	public void testServiceMetadata() {
//
//		HibernateTemplate templ = (HibernateTemplate)getApplicationContext().getBean("hibernateTemplate");
//		ServiceMetadataBuilder builder = new ServiceMetadataBuilder();
//		builder.setHibernateTemplate(templ);
//		builder.setPersist(true);
//		File dir = new File("common/test/resources/serviceMetadata");
//		File[] files = dir.listFiles();
//		for (File file : files) {
//			gov.nih.nci.cagrid.metadata.ServiceMetadata svcMetaIn = null;
//			try {
//				svcMetaIn = (gov.nih.nci.cagrid.metadata.ServiceMetadata) Utils
//						.deserializeObject(
//								new FileReader(file),
//								gov.nih.nci.cagrid.metadata.ServiceMetadata.class);
//
//			} catch (Exception ex) {
//				fail("Error deserializing " + file.getAbsolutePath());
//			}
//			
//			ServiceMetadata svcMetaOut = builder.build(svcMetaIn);
//			assertNotNull("No ID for service metadata for " + file.getAbsolutePath(), svcMetaOut.getId());
//		}
	}
	
	public void testQueryServiceMetadata(){
		GridServiceDao dao = (GridServiceDao)getApplicationContext().getBean("gridServiceDao");
		List<GridService> gridServices = dao.getAll();
		System.out.println("Got " + gridServices.size() + " services.");
		for(GridService svc : gridServices){
			ServiceMetadata svcMeta = svc.getServiceMetadata();
			Service svcDesc = svcMeta.getServiceDescription();
			for(ServiceContext ctx : svcDesc.getServiceContextCollection()){
				for(Operation op : ctx.getOperationCollection()){
					for(InputParameter param : op.getInputParameterCollection()){
						System.out.println("param: " + param.getName());
					}
				}
			}
		}
	}

}
