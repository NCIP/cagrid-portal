/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.metachange;

import gov.nih.nci.cagrid.portal.aggr.AbstractMetadataListener;
import gov.nih.nci.cagrid.portal.dao.CQLQueryInstanceDao;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.SharedCQLQueryDao;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQueryInstance;
import gov.nih.nci.cagrid.portal.domain.dataservice.SharedCQLQuery;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.util.Metadata;
import gov.nih.nci.cagrid.portal.util.PortalUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Transactional
public class MetadataChangeListener extends AbstractMetadataListener {

	private static final Log logger = LogFactory
			.getLog(MetadataChangeListener.class);

	private GridServiceDao gridServiceDao;
	private SharedCQLQueryDao sharedCqlQueryDao;
	private CQLQueryInstanceDao cqlQueryInstanceDao;

	/**
	 * 
	 */
	public MetadataChangeListener() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	public void onApplicationEvent(ApplicationEvent e) {
		try {
			if (e instanceof MetadataChangeEvent) {
				updateServiceMetadata((MetadataChangeEvent) e);
			}
		} catch (Exception ex) {
			logger.error("Error updated metadata: " + ex.getMessage(), ex);
		}
	}

	protected void updateServiceMetadata(MetadataChangeEvent event)
			throws Exception {
		logger.debug("Updating service metadata for " + event.getServiceUrl());
		
		Metadata meta = PortalUtils.getMetadata(event.getServiceUrl(),
				getMetadataTimeout());
		updateServiceMetadata(event.getServiceUrl(), meta);
	}

	public void updateServiceMetadata(String serviceUrl, Metadata meta)
			throws Exception {
		
		GridService service = getGridServiceDao().getByUrl(serviceUrl);
		if (service == null) {
			throw new RuntimeException("Coudln't find service for "	+ serviceUrl);
		}

		Set<CQLQueryInstance> cqlQueryInstances = new HashSet<CQLQueryInstance>();
		Map<String, SharedCQLQuery> sharedCqlQueries = new HashMap<String, SharedCQLQuery>();
		if (meta.dmodel != null) {

			// Need to preserve associations with SharedQuery objects
			// and CQLQueryInstance objects.

			GridDataService dataService = (GridDataService) service;
			for (SharedCQLQuery query : getSharedCqlQueryDao()
					.getByDataService(dataService)) {
				UMLClass umlClass = query.getTargetClass();
				sharedCqlQueries.put(umlClass.getPackageName() + "."
						+ umlClass.getClassName(), query);
			}
			cqlQueryInstances.addAll(getCqlQueryInstanceDao().getByDataService(
					dataService));
		}

		// NOTE: Associations with XML schemas in GME is handled
		// by the XMLSchemaMonitor. So, we don't need to address it here.

		getGridServiceDao().deleteMetadata(service);
		setMetadata(service, meta);
		getGridServiceDao().save(service);

		if (sharedCqlQueries.size() > 0) {
			GridDataService dataService = (GridDataService) service;
			for (UMLClass umlClass : dataService.getDomainModel().getClasses()) {
				SharedCQLQuery query = sharedCqlQueries.get(umlClass
						.getPackageName()
						+ "." + umlClass.getClassName());
				if (query != null) {
					query.setTargetClass(umlClass);
					query.setTargetService(dataService);
					getSharedCqlQueryDao().save(query);
				}
			}
		}

		if (cqlQueryInstances.size() > 0) {
			GridDataService dataService = (GridDataService) service;
			for (CQLQueryInstance instance : cqlQueryInstances) {
				instance.setDataService(dataService);
				getCqlQueryInstanceDao().save(instance);
			}
		}

	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

	public SharedCQLQueryDao getSharedCqlQueryDao() {
		return sharedCqlQueryDao;
	}

	public void setSharedCqlQueryDao(SharedCQLQueryDao sharedCqlQueryDao) {
		this.sharedCqlQueryDao = sharedCqlQueryDao;
	}

	public CQLQueryInstanceDao getCqlQueryInstanceDao() {
		return cqlQueryInstanceDao;
	}

	public void setCqlQueryInstanceDao(CQLQueryInstanceDao cqlQueryInstanceDao) {
		this.cqlQueryInstanceDao = cqlQueryInstanceDao;
	}

}
