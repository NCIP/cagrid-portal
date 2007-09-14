/**
 * 
 */
package gov.nih.nci.cagrid.portal2.aggr;

import gov.nih.nci.cagrid.portal2.aggr.regsvc.DomainModelBuilder;
import gov.nih.nci.cagrid.portal2.aggr.regsvc.ServiceMetadataBuilder;
import gov.nih.nci.cagrid.portal2.domain.GridDataService;
import gov.nih.nci.cagrid.portal2.domain.GridService;
import gov.nih.nci.cagrid.portal2.domain.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal2.domain.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.portal2.util.Metadata;
import gov.nih.nci.cagrid.portal2.util.PortalUtils;

import org.springframework.context.ApplicationListener;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public abstract class AbstractMetadataListener implements ApplicationListener {

	private ServiceMetadataBuilder serviceMetadataBuilder;

	private DomainModelBuilder domainModelBuilder;

	private long metadataTimeout = 5000;
	

	/**
	 * 
	 */
	public AbstractMetadataListener() {

	}
	
	protected void setMetadata(GridService service, Metadata meta) throws Exception{
		ServiceMetadataBuilder sMetaBuilder = getServiceMetadataBuilder();
		sMetaBuilder.setPersist(true);
		DomainModelBuilder dModelBuilder = getDomainModelBuilder();
		dModelBuilder.setPersist(true);
		if (service instanceof GridDataService) {
			
			GridDataService dataService = (GridDataService)service;
			DomainModel domainModel = dModelBuilder.build(meta.dmodel);
			dataService.setDomainModel(domainModel);
			domainModel.setService(dataService);
		}
		ServiceMetadata serviceMetadata = sMetaBuilder.build(meta.smeta);
		serviceMetadata.setService(service);
		service.setServiceMetadata(serviceMetadata);
		service.setMetadataHash(PortalUtils.createHashFromMetadata(meta));
	}
	
	

	public DomainModelBuilder getDomainModelBuilder() {
		return domainModelBuilder;
	}

	public void setDomainModelBuilder(DomainModelBuilder domainModelBuilder) {
		this.domainModelBuilder = domainModelBuilder;
	}

	public long getMetadataTimeout() {
		return metadataTimeout;
	}

	public void setMetadataTimeout(long metadataTimeout) {
		this.metadataTimeout = metadataTimeout;
	}

	public ServiceMetadataBuilder getServiceMetadataBuilder() {
		return serviceMetadataBuilder;
	}

	public void setServiceMetadataBuilder(
			ServiceMetadataBuilder serviceMetadataBuilder) {
		this.serviceMetadataBuilder = serviceMetadataBuilder;
	}


}
