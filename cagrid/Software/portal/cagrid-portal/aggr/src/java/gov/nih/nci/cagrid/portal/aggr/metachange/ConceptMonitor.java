/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.metachange;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Transactional
public class ConceptMonitor {

	private static final Log logger = LogFactory.getLog(ConceptMonitor.class);

	private GridServiceDao gridServiceDao;
	private ServiceConceptIndexer serviceConceptIndexer;

	/**
	 * 
	 */
	public ConceptMonitor() {

	}

	public void processNewConcepts() {
		try {
			Set<String> unresolvable = new HashSet<String>();
			for (GridService gridService : getGridServiceDao()
					.getUnindexedServices()) {
				logger.debug("Processing service: " + gridService.getUrl());
				getServiceConceptIndexer().indexService(gridService.getUrl(), unresolvable);
			}
		} catch (Exception ex) {
			String msg = "Error processing new concepts: " + ex.getMessage();
			logger.error(msg, ex);
			throw new RuntimeException(msg, ex);
		}
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

	public ServiceConceptIndexer getServiceConceptIndexer() {
		return serviceConceptIndexer;
	}

	public void setServiceConceptIndexer(
			ServiceConceptIndexer serviceConceptIndexer) {
		this.serviceConceptIndexer = serviceConceptIndexer;
	}

}
