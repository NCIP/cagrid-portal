/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.metachange;

import gov.nih.nci.cagrid.portal.dao.GridServiceDao;
import gov.nih.nci.cagrid.portal.domain.GridService;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class DBCachedMetadataHashProvider implements
		MetadataHashProvider {

	private GridServiceDao gridServiceDao;

	/**
	 * 
	 */
	public DBCachedMetadataHashProvider() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.aggr.metachange.ServiceMetadataHashProvider#getHash(java.lang.String)
	 */
	public String getHash(String serviceUrl) {

		GridService service = getGridServiceDao().getByUrl(serviceUrl);
		if (service == null) {
			throw new RuntimeException("Couldn't find service " + serviceUrl);
		}
		return service.getMetadataHash();
	}

	public GridServiceDao getGridServiceDao() {
		return gridServiceDao;
	}

	public void setGridServiceDao(GridServiceDao gridServiceDao) {
		this.gridServiceDao = gridServiceDao;
	}

}
