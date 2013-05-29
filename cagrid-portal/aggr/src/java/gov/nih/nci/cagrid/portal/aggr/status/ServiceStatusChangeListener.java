/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-core/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.status;

import java.util.Date;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.StatusChange;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Transactional
public class ServiceStatusChangeListener implements ApplicationListener {

	private static final Log logger = LogFactory.getLog(ServiceStatusChangeListener.class);
	
	private GridServiceDao gridServiceDao;
	
	/**
	 * 
	 */
	public ServiceStatusChangeListener() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.context.ApplicationListener#onApplicationEvent(org.springframework.context.ApplicationEvent)
	 */
	public void onApplicationEvent(ApplicationEvent e) {
		if (e instanceof ServiceStatusChangeEvent) {
			try {
				updateServiceStatus((ServiceStatusChangeEvent) e);
			} catch (Exception ex) {
				logger.error("Error updating service status: "
						+ ex.getMessage(), ex);
			}
		}
	}

	private void updateServiceStatus(ServiceStatusChangeEvent event) {

		logger.debug("Updating status for " + event.getServiceUrl());
		
		GridService gridService = getGridServiceDao().getByUrl(event.getServiceUrl());
		StatusChange change = new StatusChange();
		change.setService(gridService);
		change.setTime(new Date());
		change.setStatus(event.getNewStatus());
		gridService.getStatusHistory().add(change);
		getGridServiceDao().save(gridService);
		
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

}
