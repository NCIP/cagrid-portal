/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr;

import gov.nih.nci.cagrid.portal.aggr.regsvc.DomainModelBuilder;
import gov.nih.nci.cagrid.portal.aggr.regsvc.ServiceMetadataBuilder;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.portal.util.Metadata;
import gov.nih.nci.cagrid.portal.util.PortalUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class AbstractMetadataListener implements ApplicationListener, ApplicationContextAware {
	
	private static final Log logger = LogFactory.getLog(AbstractMetadataListener.class);

	private long metadataTimeout = 5000;
	
	private ApplicationContext applicationContext;
	

	/**
	 * 
	 */
	public AbstractMetadataListener() {

	}
	
	public void setApplicationContext(ApplicationContext applicationContext){
		this.applicationContext = applicationContext;
	}
	public ApplicationContext getApplicationContext(){
		return applicationContext;
	}
	
	protected void setMetadata(GridService service, Metadata meta) throws Exception{
		ServiceMetadataBuilder sMetaBuilder = (ServiceMetadataBuilder) getApplicationContext().getBean("serviceMetadataBuilderPrototype");
		sMetaBuilder.setPersist(true);
		sMetaBuilder.setGridService(service);
		DomainModelBuilder dModelBuilder = (DomainModelBuilder) getApplicationContext().getBean("domainModelBuilderPrototype");
		dModelBuilder.setPersist(true);
		dModelBuilder.setGridService(service);
		if (service instanceof GridDataService) {
			
			GridDataService dataService = (GridDataService)service;
			DomainModel domainModel = dModelBuilder.build(meta.dmodel);
			dataService.setDomainModel(domainModel);
			domainModel.setService(dataService);

		}
		ServiceMetadata serviceMetadata = sMetaBuilder.build(meta.smeta);
		serviceMetadata.setService(service);
		service.setServiceMetadata(serviceMetadata);
		String hash = PortalUtils.createHashFromMetadata(meta);
		logger.debug("new hash: " + hash);
		service.setMetadataHash(hash);
	}


	public long getMetadataTimeout() {
		return metadataTimeout;
	}

	public void setMetadataTimeout(long metadataTimeout) {
		this.metadataTimeout = metadataTimeout;
	}

}
