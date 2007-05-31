/**
 * 
 */
package gov.nih.nci.cagrid.portal2.dao;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.portal2.domain.GridService;
import gov.nih.nci.cagrid.portal2.domain.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal2.util.ServiceMetadataBuilder;

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

		HibernateTemplate templ = (HibernateTemplate)getApplicationContext().getBean("hibernateTemplate");
		ServiceMetadataBuilder builder = new ServiceMetadataBuilder();
		builder.setHibernateTemplate(templ);
		builder.setPersist(true);
		File dir = new File("common/test/resources/serviceMetadata");
		File[] files = dir.listFiles();
		for (File file : files) {
			gov.nih.nci.cagrid.metadata.ServiceMetadata svcMetaIn = null;
			try {
				svcMetaIn = (gov.nih.nci.cagrid.metadata.ServiceMetadata) Utils
						.deserializeObject(
								new FileReader(file),
								gov.nih.nci.cagrid.metadata.ServiceMetadata.class);

			} catch (Exception ex) {
				fail("Error deserializing " + file.getAbsolutePath());
			}
			
			ServiceMetadata svcMetaOut = builder.build(svcMetaIn);
			assertNotNull("No ID for service metadata for " + file.getAbsolutePath(), svcMetaOut.getId());
		}
	}
	
	public void testQueryServiceMetadata(){
		ServiceMetadataDao dao = (ServiceMetadataDao)getApplicationContext().getBean("serviceMetadataDao");
		List gridServices = dao.getHibernateTemplate().find("from GridService");
		for(Iterator i = gridServices.iterator(); i.hasNext();){
			GridService svc = (GridService)i.next();
			ServiceMetadata svcMeta = svc.getServiceMetadata();
			assertNotNull("ServiceMetadata is null for " + svc.getUrl());
		}
	}

}
