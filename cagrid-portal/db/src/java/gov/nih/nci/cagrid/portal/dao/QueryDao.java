/**
 * 
 */
package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.dataservice.Query;

import java.util.List;

import javax.persistence.NonUniqueResultException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class QueryDao extends AbstractDao<Query> {

	@Override
	public Class domainClass() {
		return Query.class;
	}

	public Query getQueryByHash(String hash) {
		Query query = null;

		List queries = getHibernateTemplate().find(
				"from Query where hash = '" + hash + "'");

		if (queries.size() > 1) {
			throw new NonUniqueResultException("Found " + queries.size()
					+ " Query objects for hash '" + hash + "'");
		}
		if (queries.size() > 0) {
			query = (Query) queries.get(0);
		}
		return query;
	}
}
