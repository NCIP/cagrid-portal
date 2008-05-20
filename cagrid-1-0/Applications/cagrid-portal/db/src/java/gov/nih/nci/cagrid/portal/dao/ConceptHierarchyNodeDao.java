/**
 * 
 */
package gov.nih.nci.cagrid.portal.dao;

import gov.nih.nci.cagrid.portal.domain.ConceptHierarchy;
import gov.nih.nci.cagrid.portal.domain.ConceptHierarchyNode;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.NonUniqueResultException;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class ConceptHierarchyNodeDao extends AbstractDao<ConceptHierarchyNode> {

	/**
	 * 
	 */
	public ConceptHierarchyNodeDao() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return ConceptHierarchyNode.class;
	}

	public ConceptHierarchyNode getByConceptInHierarchy(
			final ConceptHierarchy hierarchy,
			final List<ConceptHierarchyNode> pathFromRoot,
			final String conceptCode) {

		ConceptHierarchyNode node = null;

		final Set<Integer> ancestorIds = new HashSet<Integer>();
		for (ConceptHierarchyNode n : pathFromRoot) {
			ancestorIds.add(n.getId());
		}
		List<ConceptHierarchyNode> nodes = (List<ConceptHierarchyNode>) getHibernateTemplate()
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {

						Criteria crit = session.createCriteria(
								ConceptHierarchyNode.class).createAlias(
								"hierarchy", "h").add(
								Restrictions.eq("h.id", hierarchy.getId()))
								.add(Restrictions.eq("code", conceptCode))
								.setResultTransformer(
										Criteria.DISTINCT_ROOT_ENTITY);
						if (pathFromRoot != null && pathFromRoot.size() > 0) {
							crit.createCriteria("ancestors").add(
									Restrictions.in("id", ancestorIds));
						}
						return crit.list();
					}
				});
		if (nodes.size() > 1) {
			throw new NonUniqueResultException("Found " + nodes.size()
					+ " concept hierarchy nodes.");
		} else if (nodes.size() == 1) {
			node = nodes.get(0);
		}
		return node;
	}

	public ConceptHierarchy getHierarchyByUri(String uri) {
		ConceptHierarchy h = null;
		List l = getHibernateTemplate().find(
				"from ConceptHierarchy where uri = ?", new Object[] { uri });
		if (l.size() > 1) {
			throw new NonUniqueResultException(
					"More than one hierarchy found for '" + uri + "'");
		} else if (l.size() == 1) {
			h = (ConceptHierarchy) l.get(0);
		}
		return h;
	}

	public List<ConceptHierarchyNode> getByConceptCode(String conceptCode) {
		return (List<ConceptHierarchyNode>) getHibernateTemplate().find(
				"from ConceptHierarchyNode where code = ?",
				new Object[] { conceptCode });
	}

}
