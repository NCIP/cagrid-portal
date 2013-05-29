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
package model1.dao;

import java.sql.SQLException;
import java.util.List;

import model1.domain.Gene;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class GeneDao extends AbstractDao<Gene> {

	/**
	 * 
	 */
	public GeneDao() {

	}

	public List<Gene> getGenesByTerm(final String value) {

		return (List<Gene>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						return session.createCriteria(Gene.class).createAlias(
								"terms", "t").createAlias("t.ancestors", "ta")
								.add(
										Restrictions.disjunction().add(
												Restrictions.eq("ta.value",
														value)).add(
												Restrictions
														.eq("t.value", value)))
								.setResultTransformer(
										Criteria.DISTINCT_ROOT_ENTITY).list();
					}
				});

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model1.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return Gene.class;
	}

}
