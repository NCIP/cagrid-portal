package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class CatalogEntryDao extends AbstractCatalogEntryDao<CatalogEntry> {
	public CatalogEntryDao() {
	}/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
	 */

	public List<CatalogEntry> getLatestContent(int limit) {
		List<CatalogEntry> latest = new ArrayList<CatalogEntry>();

		List<Integer> ids = getHibernateTemplate().find(
				"select entry.id from CatalogEntry entry "
						+ "order by entry.updatedAt desc");
		List<CatalogEntry> l = new ArrayList<CatalogEntry>();
		for (Integer id : ids) {
			l.add((CatalogEntry) getHibernateTemplate().get(CatalogEntry.class,
					id));
		}
		for (int i = 0; i < limit && i < l.size(); i++) {
			latest.add(l.get(i));
		}
		return latest;
	}

	public List<CatalogEntry> getByPartialName(final String name) {
		return (List<CatalogEntry>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {

						return session.createCriteria(CatalogEntry.class).add(
								Restrictions.like("name", name,
										MatchMode.ANYWHERE))
								.setResultTransformer(
										Criteria.DISTINCT_ROOT_ENTITY).list();
					}
				});

	}

	public List<CatalogEntry> getByPartialNameAndType(final String name,
			final String className) {
		try {
			final Class klass = Class.forName(className);
			
			return (List<CatalogEntry>) getHibernateTemplate().execute(
					new HibernateCallback() {
						public Object doInHibernate(Session session)
								throws HibernateException, SQLException {

							return session.createCriteria(klass)
									.add(
											Restrictions.like("name", name,
													MatchMode.ANYWHERE))
									.setResultTransformer(
											Criteria.DISTINCT_ROOT_ENTITY)
									.list();
						}
					});
		} catch (Exception ex) {
			throw new RuntimeException("Error searching by name and type: "
					+ ex.getMessage(), ex);
		}

	}

	@Override
	public void delete(final CatalogEntry ce) {

		HibernateTemplate templ = getHibernateTemplate();
		templ.execute(new HibernateCallback() {

			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {

				Query query = session
						.createQuery(
								"select id from CatalogEntryRelationshipInstance "
										+ "where roleA.catalogEntry.id = ? or roleB.catalogEntry.id = ?")
						.setInteger(0, ce.getId()).setInteger(1, ce.getId());
				List<Integer> relIds = query.list();

				if (relIds.size() > 0) {
					query = session
							.createQuery(
									"update CatalogEntryRelationshipInstance set roleA = null where id in (:ids)")
							.setParameterList("ids", relIds);
					query.executeUpdate();
					session.flush();
					query = session
							.createQuery(
									"update CatalogEntryRelationshipInstance set roleB = null where id in (:ids)")
							.setParameterList("ids", relIds);
					query.executeUpdate();
					session.flush();

					query = session
							.createQuery(
									"delete from CatalogEntryRoleInstance c where c.relationship.id in (:ids)")
							.setParameterList("ids", relIds);
					query.executeUpdate();
					session.flush();

					query = session
							.createQuery(
									"delete from CatalogEntryRelationshipInstance c where c.id in (:ids)")
							.setParameterList("ids", relIds);

					query.executeUpdate();
					session.flush();
				}

				session.delete(ce);
				session.flush();

				return null;
			}
		});

	}

	@Override
	public Class domainClass() {
		return CatalogEntry.class;
	}
}