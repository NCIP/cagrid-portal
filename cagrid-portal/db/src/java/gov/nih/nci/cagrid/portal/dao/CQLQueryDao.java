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

import gov.nih.nci.cagrid.portal.domain.dataservice.CQLQuery;

import java.util.List;

import javax.persistence.NonUniqueResultException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class CQLQueryDao extends AbstractDao<CQLQuery> {

	/**
	 * 
	 */
	public CQLQueryDao() {
	
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return CQLQuery.class;
	}
	
	public CQLQuery getByHash(String hash) {
		CQLQuery query = null;

		List queries = getHibernateTemplate().find(
				"from CQLQuery where hash = '" + hash + "'");

// does not matter. If the hash is the same, its the same query       
//		if (queries.size() > 1) {
//			throw new NonUniqueResultException("Found " + queries.size()
//					+ " CQLQuery objects for hash '" + hash + "'");
//		}
		if (queries.size() > 0) {
			query = (CQLQuery) queries.get(0);
		}
		return query;
	}



}
