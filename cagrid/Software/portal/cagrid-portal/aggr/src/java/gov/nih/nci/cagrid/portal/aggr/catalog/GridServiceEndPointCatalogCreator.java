package gov.nih.nci.cagrid.portal.aggr.catalog;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.catalog.GridServiceEndPointCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.GridService;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: kherm
 * 
 * @author kherm manav.kher@semanticbits.com
 */
@Transactional
public class GridServiceEndPointCatalogCreator extends AbstractCatalogCreator {

	GridServiceDao gridServiceDao;
	GridServiceEndPointCatalogEntryDao gridServiceEndPointCatalogEntryDao;

	/**
	 * Will make sure catalog items exist for all services Run on container
	 * startup
	 * 
	 * @throws Exception
	 */
	public void afterPropertiesSet() throws Exception {

//		for (GridService g : gridServiceDao.getAll()) {
//			if (gridServiceEndPointCatalogEntryDao.isAbout(g) == null) {
//				logger
//						.debug("GridService Endpoint catalog not found. Will create for id "
//								+ g.getId());
//				gridServiceEndPointCatalogEntryDao.createCatalogAbout(g);
//			}
//
//		}
	}

	// spring getters and setters
	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	@Required
	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

	public GridServiceEndPointCatalogEntryDao getGridServiceEndPointCatalogEntryDao() {
		return gridServiceEndPointCatalogEntryDao;
	}

	@Required
	public void setGridServiceEndPointCatalogEntryDao(
			GridServiceEndPointCatalogEntryDao gridServiceEndPointCatalogEntryDao) {
		this.gridServiceEndPointCatalogEntryDao = gridServiceEndPointCatalogEntryDao;
	}
}
