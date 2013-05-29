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
