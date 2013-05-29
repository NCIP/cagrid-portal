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
package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.IndexService;

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
	 * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
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
