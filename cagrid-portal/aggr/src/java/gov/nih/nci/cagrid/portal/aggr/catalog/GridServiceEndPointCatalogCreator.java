package gov.nih.nci.cagrid.portal.aggr.catalog;

import org.springframework.transaction.annotation.Transactional;

/**
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
@Transactional
public class GridServiceEndPointCatalogCreator extends AbstractCatalogCreator {

    private GridServiceEndPointCatalogCreatorDelegate gridServiceEndPointCatalogCreatorDelegate;

    /**
     * Will make sure catalog items exist for all services Run on container
     * startup
     *
     * @throws Exception
     */
    public void afterPropertiesSet() throws Exception {
    	//call delegate to load catalog items
    	gridServiceEndPointCatalogCreatorDelegate.loadCatalogItems();    	
    }

    // spring getters and setters
   	public GridServiceEndPointCatalogCreatorDelegate getGridServiceEndPointCatalogCreatorDelegate() {
		return gridServiceEndPointCatalogCreatorDelegate;
	}

	public void setGridServiceEndPointCatalogCreatorDelegate(
			GridServiceEndPointCatalogCreatorDelegate gridServiceEndPointCatalogCreatorDelegate) {
		this.gridServiceEndPointCatalogCreatorDelegate = gridServiceEndPointCatalogCreatorDelegate;
	}
}
