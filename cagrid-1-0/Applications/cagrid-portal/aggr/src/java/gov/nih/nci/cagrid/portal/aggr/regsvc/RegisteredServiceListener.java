/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.regsvc;

import gov.nih.nci.cagrid.portal.aggr.AbstractMetadataListener;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.IndexServiceDao;
import gov.nih.nci.cagrid.portal.dao.ServiceMetadataDao;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.IndexService;
import gov.nih.nci.cagrid.portal.domain.ServiceStatus;
import gov.nih.nci.cagrid.portal.domain.StatusChange;
import gov.nih.nci.cagrid.portal.util.Metadata;
import gov.nih.nci.cagrid.portal.util.PortalUtils;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Transactional
public class RegisteredServiceListener extends AbstractMetadataListener {

	private static final Log logger = LogFactory
			.getLog(RegisteredServiceListener.class);

	private IndexServiceDao indexServiceDao;

	private GridServiceDao gridServiceDao;

	/**
	 * 
	 */
	public RegisteredServiceListener() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	public void onApplicationEvent(ApplicationEvent e) {

//		logger.debug("Received event " + e.getClass().getName());
		if (e instanceof RegisteredServiceEvent) {
			try {
				persistService((RegisteredServiceEvent) e);
			} catch (Exception ex) {
				logger.warn("Error persisting service: " + ex.getMessage());
			}
		}

	}

	public void persistService(RegisteredServiceEvent event) throws Exception {

		logger.debug("RegisteredServiceEvent:  '" + event.getServiceUrl()
				+ "' found at '" + event.getIndexServiceUrl() + "'");

		IndexService idxSvc = getIndexServiceDao().getIndexServiceByUrl(
				event.getIndexServiceUrl());
		if (idxSvc == null) {
			idxSvc = new IndexService();
			idxSvc.setUrl(event.getIndexServiceUrl());
			getIndexServiceDao().save(idxSvc);
			idxSvc = getIndexServiceDao().getById(idxSvc.getId());
		}

		GridService gSvc = getGridServiceDao().getByUrl(event.getServiceUrl());
		if (gSvc != null) {
			logger.error("GridService '" + event.getServiceUrl() + "' already exists!");
			throw new GridServiceExistsException(event.getServiceUrl());
		}

		Metadata meta = PortalUtils.getMetadata(event.getServiceUrl(),
				getMetadataTimeout());
		GridService service = null;
		if (meta.dmodel != null) {
			service = new GridDataService();
		} else {
			service = new GridService();
		}
		setMetadata(service, meta);

		service.getIndexServices().add(idxSvc);
		service.setUrl(event.getServiceUrl());
		
		StatusChange status = new StatusChange();
		status.setService(service);
		status.setStatus(ServiceStatus.UNKNOWN);
		status.setTime(new Date());
		service.getStatusHistory().add(status);
		
		idxSvc.getServices().add(service);

		getGridServiceDao().save(service);

	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

	public IndexServiceDao getIndexServiceDao() {
		return indexServiceDao;
	}

	public void setIndexServiceDao(IndexServiceDao indexServiceDao) {
		this.indexServiceDao = indexServiceDao;
	}

}