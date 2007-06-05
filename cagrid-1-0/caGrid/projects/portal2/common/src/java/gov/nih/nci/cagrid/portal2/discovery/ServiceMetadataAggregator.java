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
import gov.nih.nci.cagrid.portal2.domain.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.portal2.util.DomainModelBuilder;
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

	private DomainModelBuilder domainModelBuilder;

	private long metadataTimeout = 5000;

	public DomainModelBuilder getDomainModelBuilder() {
		return domainModelBuilder;
	}

	public void setDomainModelBuilder(DomainModelBuilder domainModelBuilder) {
		this.domainModelBuilder = domainModelBuilder;
	}

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

			MetadataThread t = new MetadataThread(epr);
			t.start();
			try {
				t.join(getMetadataTimeout());
			} catch (InterruptedException ex) {
				logger.error("metadata thread interrupted", ex);
			}
			if (t.getEx() != null) {
				logger.error(t.getEx());
			}else if(!t.isFinished()){
				logger.error("Metadata query to " + url + " timed out.");
			}else{
				GridService service = null;
				if (t.getDomainModel() != null) {
					GridDataService dataService = new GridDataService();
					DomainModel domainModel = constructDomainModel(t
							.getDomainModel());
					dataService.setDomainModel(domainModel);
					domainModel.setService(dataService);
					service = dataService;

				} else {
					service = new GridService();
				}

				ServiceMetadata serviceMetadata = constructServiceMetadata(t
						.getServiceMetadata());
				serviceMetadata.setService(service);
				service.setServiceMetadata(serviceMetadata);
				service.setUrl(url);
				service.setFirstSeen(new Date());
				service.setLastSeen(new Date());
				service.setStatus(ServiceStatusType.UP);

				logger.debug("Saving grid service");
				dao.saveGridService(service);
			}

		}

		return serviceAdded;
	}

	private DomainModel constructDomainModel(
			gov.nih.nci.cagrid.metadata.dataservice.DomainModel dMeta)
			throws Exception {
		return getDomainModelBuilder().build(dMeta);
	}

	private ServiceMetadata constructServiceMetadata(
			gov.nih.nci.cagrid.metadata.ServiceMetadata sMeta) throws Exception {
		return getServiceMetadataBuilder().build(sMeta);
	}

	public ServiceMetadataBuilder getServiceMetadataBuilder() {
		return serviceMetadataBuilder;
	}

	public void setServiceMetadataBuilder(
			ServiceMetadataBuilder serviceMetadataBuilder) {
		this.serviceMetadataBuilder = serviceMetadataBuilder;
	}

	private static class MetadataThread extends Thread {
		private EndpointReferenceType epr;

		private gov.nih.nci.cagrid.metadata.dataservice.DomainModel domainModel;

		private gov.nih.nci.cagrid.metadata.ServiceMetadata serviceMetadata;

		private Exception ex;

		private boolean finished;

		public gov.nih.nci.cagrid.metadata.dataservice.DomainModel getDomainModel() {
			return domainModel;
		}

		public void setDomainModel(
				gov.nih.nci.cagrid.metadata.dataservice.DomainModel domainModel) {
			this.domainModel = domainModel;
		}

		public EndpointReferenceType getEpr() {
			return epr;
		}

		public void setEpr(EndpointReferenceType epr) {
			this.epr = epr;
		}

		public Exception getEx() {
			return ex;
		}

		public void setEx(Exception ex) {
			this.ex = ex;
		}

		public boolean isFinished() {
			return finished;
		}

		public void setFinished(boolean finished) {
			this.finished = finished;
		}

		public gov.nih.nci.cagrid.metadata.ServiceMetadata getServiceMetadata() {
			return serviceMetadata;
		}

		public void setServiceMetadata(
				gov.nih.nci.cagrid.metadata.ServiceMetadata serviceMetadata) {
			this.serviceMetadata = serviceMetadata;
		}

		MetadataThread(EndpointReferenceType epr) {
			this.epr = epr;
		}

		public void run() {
			try {
				// Get the metadata
				this.serviceMetadata = MetadataUtils
						.getServiceMetadata(this.epr);
				if (this.serviceMetadata == null) {
					throw new RuntimeException("Didn't retrieve metadata for "
							+ epr);
				}

				// Get DomainModel, if it exists
				try {
					this.domainModel = MetadataUtils.getDomainModel(this.epr);
				} catch (Exception ex) {
					logger.info("No DomainModel found for " + epr + ": "
							+ ex.getMessage());
				}
				this.finished = true;
			} catch (Exception ex) {
				this.ex = ex;
			}
		}
	}

	public long getMetadataTimeout() {
		return metadataTimeout;
	}

	public void setMetadataTimeout(long metadataTimeout) {
		this.metadataTimeout = metadataTimeout;
	}
}
