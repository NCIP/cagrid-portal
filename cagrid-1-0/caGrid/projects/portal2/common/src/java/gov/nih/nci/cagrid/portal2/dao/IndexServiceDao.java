/**
 * 
 */
package gov.nih.nci.cagrid.portal2.dao;

import gov.nih.nci.cagrid.portal2.domain.IndexService;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class IndexServiceDao extends AbstractDao<IndexService> {

	/**
	 * 
	 */
	public IndexServiceDao() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal2.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return IndexService.class;
	}

	public IndexService getIndexServiceByUrl(String indexServiceUrl) {
		IndexService eg = new IndexService();
		eg.setUrl(indexServiceUrl);
		return this.getByExample(eg);
	}

}
