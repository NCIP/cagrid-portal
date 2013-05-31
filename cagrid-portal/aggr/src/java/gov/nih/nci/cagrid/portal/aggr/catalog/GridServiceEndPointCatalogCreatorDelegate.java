/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
package gov.nih.nci.cagrid.portal.aggr.catalog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.dao.catalog.GridServiceEndPointCatalogEntryDao;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.util.filter.ServiceFilter;

/**
 * User: muralic
 *
 * @author muralic murali.chadaram@semanticbits.com
 */
@Transactional
public class GridServiceEndPointCatalogCreatorDelegate {
	
	private GridServiceDao gridServiceDao;
    private GridServiceEndPointCatalogEntryDao gridServiceEndPointCatalogEntryDao;
    private ServiceMetadataCatalogEntryBuilder serviceMetadataCatalogEntryBuilder;
    private ServiceFilter baseServiceFilter;
    Log logger = LogFactory.getLog(getClass());
    
    /**
     * Will make sure catalog items exist for all services Run on container
     * startup
     *
     * @throws Exception
     */
    public void loadCatalogItems() throws Exception{
    	for (GridService g : gridServiceDao.getAll()) {
            if (gridServiceEndPointCatalogEntryDao.isAbout(g) == null) {
                if (!baseServiceFilter.willBeFiltered(g)) { 
                	 logger
                     .debug("GridService Endpoint catalog not found. Will create for id "
                             + g.getId());
                    serviceMetadataCatalogEntryBuilder.build(g);
                }
            }
        }
    }

    // spring getters and setters
	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

	public GridServiceEndPointCatalogEntryDao getGridServiceEndPointCatalogEntryDao() {
		return gridServiceEndPointCatalogEntryDao;
	}

	public void setGridServiceEndPointCatalogEntryDao(
			GridServiceEndPointCatalogEntryDao gridServiceEndPointCatalogEntryDao) {
		this.gridServiceEndPointCatalogEntryDao = gridServiceEndPointCatalogEntryDao;
	}

	public ServiceMetadataCatalogEntryBuilder getServiceMetadataCatalogEntryBuilder() {
		return serviceMetadataCatalogEntryBuilder;
	}

	public void setServiceMetadataCatalogEntryBuilder(
			ServiceMetadataCatalogEntryBuilder serviceMetadataCatalogEntryBuilder) {
		this.serviceMetadataCatalogEntryBuilder = serviceMetadataCatalogEntryBuilder;
	}

	public ServiceFilter getBaseServiceFilter() {
		return baseServiceFilter;
	}

	public void setBaseServiceFilter(ServiceFilter baseServiceFilter) {
		this.baseServiceFilter = baseServiceFilter;
	}

}
