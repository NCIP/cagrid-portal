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
package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.annotation.UpdatesCatalogs;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.catalog.GridServiceEndPointCatalogEntry;
import gov.nih.nci.cagrid.portal.util.BeanUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.orm.hibernate3.HibernateCallback;

import javax.persistence.NonUniqueResultException;
import java.sql.SQLException;
import java.util.List;

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
     * Will not return hidden catalogs
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
                                .add(Restrictions.eq("hidden",false))
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
                                .add(Restrictions.eq("hidden",false))
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
