/**
 * 
 */
package gov.nih.nci.cagrid.portal2.aggr.metachange;

import gov.nih.nci.cagrid.portal2.aggr.AbstractMetadataListener;
import gov.nih.nci.cagrid.portal2.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal2.domain.GridDataService;
import gov.nih.nci.cagrid.portal2.domain.GridService;
import gov.nih.nci.cagrid.portal2.util.Metadata;
import gov.nih.nci.cagrid.portal2.util.PortalUtils;

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

	private void updateServiceMetadata(MetadataChangeEvent event)
			throws Exception {
		logger.debug("Updating service metadata for " + event.getServiceUrl());
		GridService service = getGridServiceDao().getByUrl(
				event.getServiceUrl());
		if (service == null) {
			throw new RuntimeException("Coudln't find service for "
					+ event.getServiceUrl());
		}
		Metadata meta = PortalUtils.getMetadata(service.getUrl(),
				getMetadataTimeout());
		getGridServiceDao().deleteMetadata(service);
		setMetadata(service, meta);
		getGridServiceDao().save(service);
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

}
