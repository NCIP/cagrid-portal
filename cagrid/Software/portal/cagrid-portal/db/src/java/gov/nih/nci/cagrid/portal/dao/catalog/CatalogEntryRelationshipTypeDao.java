/**
 * 
 */
package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.dao.AbstractDao;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRelationshipType;

import java.util.List;

import javax.persistence.NonUniqueResultException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * 
 */
public class CatalogEntryRelationshipTypeDao extends
		AbstractDao<CatalogEntryRelationshipType> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.dao.AbstractDao#domainClass()
	 */
	@Override
	public Class domainClass() {
		return CatalogEntryRelationshipType.class;
	}

	public CatalogEntryRelationshipType getByName(String name) {
		CatalogEntryRelationshipType relType = null;
		List l = getHibernateTemplate().find(
				"from CatalogEntryRelationshipType where name = ?", name);
		if (l.size() > 1) {
			throw new NonUniqueResultException(
					"More than one CatalogEntryRelationshipType found for name = "
							+ name);
		}
		if (l.size() == 1) {
			relType = (CatalogEntryRelationshipType) l.iterator().next();
		}
		return relType;
	}

}
