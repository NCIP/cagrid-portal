/**
 * 
 */
package gov.nih.nci.cagrid.portal2.discovery;

import gov.nih.nci.cagrid.discovery.client.DiscoveryClient;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.portal2.dao.ServiceMetadataDao;
import gov.nih.nci.cagrid.portal2.domain.GridDataService;
import gov.nih.nci.cagrid.portal2.domain.GridService;
import gov.nih.nci.cagrid.portal2.domain.ServiceStatusType;
import gov.nih.nci.cagrid.portal2.domain.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.portal2.util.ServiceMetadataBuilder;

import java.util.Date;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ServiceMetadataAggregator {

	private static final Log logger = LogFactory
			.getLog(ServiceMetadataAggregator.class);

	private boolean metadataCompliance;

	private String indexServiceUrl;

	private ServiceMetadataDao serviceMetadataDao;
	private ServiceMetadataBuilder serviceMetadataBuilder;

	public String getIndexServiceUrl() {
		return indexServiceUrl;
	}

	public void setIndexServiceUrl(String indexServiceUrl) {
		this.indexServiceUrl = indexServiceUrl;
	}

	public boolean isMetadataCompliance() {
		return metadataCompliance;
	}

	public void setMetadataCompliance(boolean metadataCompliance) {
		this.metadataCompliance = metadataCompliance;
	}

	public ServiceMetadataDao getServiceMetadataDao() {
		return serviceMetadataDao;
	}

	public void setServiceMetadataDao(ServiceMetadataDao serviceMetadataDao) {
		this.serviceMetadataDao = serviceMetadataDao;
	}

	public void execute() throws AggregationException {
		try {
			DiscoveryClient client = new DiscoveryClient(getIndexServiceUrl());
			EndpointReferenceType[] eprs = client
					.getAllServices(isMetadataCompliance());
			for (EndpointReferenceType epr : eprs) {
				String url = epr.getAddress().toString();
				try {
					boolean serviceAdded = addService(epr);
					if (serviceAdded) {
						logger.debug("Added new service: " + url);
					}
				} catch (Exception ex) {
					logger.error("Error aggregating " + url + ": "
							+ ex.getMessage(), ex);
				}
			}
		} catch (Exception ex) {
			throw new AggregationException(
					"Error aggregating service metadata: " + ex.getMessage(),
					ex);
		}
	}

	public boolean addService(EndpointReferenceType epr) throws Exception {
		boolean serviceAdded = false;

		ServiceMetadataDao dao = getServiceMetadataDao();
		String url = epr.getAddress().toString();
		if (dao.getGridServiceByUrl(url) != null) {
			logger.debug("Service " + url + " exits. Ignoring");
		} else {
			logger.debug("Persisting " + url);
			serviceAdded = true;

			// Get the metadata
			gov.nih.nci.cagrid.metadata.ServiceMetadata sMeta = MetadataUtils
					.getServiceMetadata(epr);
			if (sMeta == null) {
				throw new RuntimeException("Didn't retrieve metadata for "
						+ url);
			}

			// Get DomainModel, if it exists
			gov.nih.nci.cagrid.metadata.dataservice.DomainModel dMeta = null;
			try {
				dMeta = MetadataUtils.getDomainModel(epr);
			} catch (Exception ex) {
				logger.info("No DomainModel found for " + url + ": "
						+ ex.getMessage());
			}

			GridService service = null;
			if (dMeta != null) {

//				DomainModel domainModel = constructDomainModel(dMeta);
//				dao.saveDomainModel(domainModel);
//				GridDataService dataService = new GridDataService();
//				dataService.setDataModelId(domainModel.getHjid());
//				service = dataService;
				service = new GridDataService();
			} else {
				service = new GridService();
			}

			ServiceMetadata serviceMetadata = constructServiceMetadata(sMeta);
			serviceMetadata.setService(service);
//			dao.save(serviceMetadata);
			service.setServiceMetadata(serviceMetadata);
			service.setUrl(url);
			service.setFirstSeen(new Date());
			service.setLastSeen(new Date());
			service.setStatus(ServiceStatusType.UP);
			
			logger.debug("Saving grid service");
			dao.saveGridService(service);

		}

		return serviceAdded;
	}

	private ServiceMetadata constructServiceMetadata(
			gov.nih.nci.cagrid.metadata.ServiceMetadata sMeta) throws Exception {
		return getServiceMetadataBuilder().build(sMeta);
	}

//	private DomainModel constructDomainModel(
//			gov.nih.nci.cagrid.metadata.dataservice.DomainModel dMeta)
//			throws Exception {
//		StringWriter w = new StringWriter();
//		MetadataUtils.serializeDomainModel(dMeta, w);
//		JAXBContext context = JAXBContext.newInstance(DomainModel.class
//				.getPackage().getName());
//		Unmarshaller unmarshaller = context.createUnmarshaller();
//		String xml = w.getBuffer().toString();
//		writeToFile("dataModel-" + System.currentTimeMillis() + ".xml", xml);
//		logger.debug("Data Model: \n\n" + xml);
//		return (DomainModel) unmarshaller.unmarshal(new ByteArrayInputStream(
//				xml.getBytes()));
//	}
//	private void writeToFile(String fileName, String xml) throws Exception {
//		FileWriter w = new FileWriter(fileName);
//		w.write(xml);
//		w.flush();
//		w.close();
//	}

	public ServiceMetadataBuilder getServiceMetadataBuilder() {
		return serviceMetadataBuilder;
	}

	public void setServiceMetadataBuilder(
			ServiceMetadataBuilder serviceMetadataBuilder) {
		this.serviceMetadataBuilder = serviceMetadataBuilder;
	}
}
