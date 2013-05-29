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
package gov.nih.nci.cagrid.portal.aggr.regsvc;

import gov.nih.nci.cagrid.portal.aggr.ServiceUrlProvider;
import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DBCachedServiceUrlProvider implements ServiceUrlProvider {

	private GridServiceDao gridServiceDao;
	
	/**
	 * 
	 */
	public DBCachedServiceUrlProvider() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.aggr.regsvc.ServiceUrlProvider#getUrls(java.lang.String)
	 */
	public Set<String> getUrls(String indexServiceUrl) {

		Set<String> urls = new HashSet<String>();
		
		List<GridService> services = getGridServiceDao().getByIndexServiceUrl(indexServiceUrl);
		for(GridService service : services){
			urls.add(service.getUrl());
		}
		
		return urls;
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

}
