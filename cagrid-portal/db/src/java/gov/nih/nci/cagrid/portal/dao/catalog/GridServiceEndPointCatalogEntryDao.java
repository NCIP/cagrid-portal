package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.ConceptHierarchyNode;
import gov.nih.nci.cagrid.portal.domain.catalog.GridServiceEndPointCatalogEntry;
import gov.nih.nci.cagrid.portal.util.BeanUtils;
import gov.nih.nci.cagrid.portal.annotation.UpdatesCatalogs;

import javax.persistence.NonUniqueResultException;
import java.util.List;
import java.sql.SQLException;

import org.springframework.orm.hibernate3.HibernateCallback;
import org.hibernate.Session;
import org.hibernate.HibernateException;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;

/**
 * User: kherm
 * 
 * @author kherm manav.kher@semanticbits.com
 */
public class GridServiceEndPointCatalogEntryDao extends
		AboutCatalogEntryDao<GridServiceEndPointCatalogEntry, GridService> {

	public GridServiceEndPointCatalogEntryDao() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return GridServiceEndPointCatalogEntry.class;
	}

	/**
	 * Returns CE's for services matching the URL. For autocompleter
	 * 
	 * @param url
	 * @return
	 */
	public List<GridServiceEndPointCatalogEntry> getByPartialUrl(
			final String url) {
		return (List<GridServiceEndPointCatalogEntry>) getHibernateTemplate()
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {

						return session.createCriteria(
								GridServiceEndPointCatalogEntry.class)
								.createCriteria("about").add(
										Restrictions.like("url", url,
												MatchMode.ANYWHERE))
								.setResultTransformer(
										Criteria.DISTINCT_ROOT_ENTITY).list();
					}
				});

	}

	public List<GridServiceEndPointCatalogEntry> getByUmlClassNameAndPartialUrl(
			final String packageName, final String className, final String url) {

		return (List<GridServiceEndPointCatalogEntry>) getHibernateTemplate()
				.execute(new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {

						return session.createCriteria(
								GridServiceEndPointCatalogEntry.class)
								.setResultTransformer(
										Criteria.DISTINCT_ROOT_ENTITY)
								.createCriteria("about").add(
										Restrictions.like("url", url,
												MatchMode.ANYWHERE)).add(
										Restrictions.eq("class",
												"GridDataService"))
								.createCriteria("domainModel").createCriteria(
										"classes")
								.add(Restrictions.eq("className", className))
								.add(
										Restrictions.eq("packageName",
												packageName))
								.list();
					}
				});
	}

	public GridServiceEndPointCatalogEntry getByUrl(String url) {
		GridServiceEndPointCatalogEntry catalog = null;
		List l = getHibernateTemplate().find(
				"from " + domainClass().getSimpleName()
						+ " where about.url = ?", new Object[] { url });
		if (l.size() > 1) {
			throw new NonUniqueResultException(
					"More than one CatalogEntry found for GridServiceEndPointCatalogEntry Object with URL = "
							+ url);
		}
		if (l.size() == 1) {
			catalog = (GridServiceEndPointCatalogEntry) l.iterator().next();
		}
		return catalog;
	}

    @UpdatesCatalogs
	public GridServiceEndPointCatalogEntry createCatalogAbout(
			GridService service) {
		GridServiceEndPointCatalogEntry entry = isAbout(service);
		if (entry == null) {
			entry = new GridServiceEndPointCatalogEntry();
			entry.setAbout(service);
			service.setCatalog(entry);
		} else
			logger
					.debug("Catalog entry already exists. Will update the existing one");
		if (!entry.isPublished()) {
			logger
					.debug("Catalog entry has not been published. Will sync with domain object");
			entry.setName(BeanUtils.traverse(service,
					"serviceMetadata.serviceDescription.name"));
			entry.setDescription(BeanUtils.traverse(service,
					"serviceMetadata.serviceDescription.description"));
		}
		save(entry);
		return entry;
	}

}
