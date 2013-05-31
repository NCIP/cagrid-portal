/**
*============================================================================
*  The Ohio State University Research Foundation, The University of Chicago -
*  Argonne National Laboratory, Emory University, SemanticBits LLC, 
*  and Ekagra Software Technologies Ltd.
*
*  Distributed under the OSI-approved BSD 3-Clause License.
*  See http://ncip.github.com/cagrid-portal/LICENSE.txt for details.
*============================================================================
**/
/**
 * 
 */
package gov.nih.nci.cagrid.portal.dao.catalog;

import gov.nih.nci.cagrid.portal.dao.AbstractDao;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRelationshipInstance;

import java.util.List;

import javax.persistence.NonUniqueResultException;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class CatalogEntryRelationshipInstanceDao extends
		AbstractDao<CatalogEntryRelationshipInstance> {

	@Override
	public Class domainClass() {
		return CatalogEntryRelationshipInstance.class;
	}

	public CatalogEntryRelationshipInstance getDynamicRelationship(
			String relTypeName, CatalogEntry ce1, CatalogEntry ce2) {
		CatalogEntryRelationshipInstance relInst = null;
		List l = getHibernateTemplate().find(
				"from CatalogEntryRelationshipInstance ri where "
						+ "ri.author is null and " + " ri.type.name = ? and "
						+ "ri.roleA.catalogEntry.id = ? and ri.roleB.catalogEntry.id = ?",
				new Object[] { relTypeName, ce1.getId(), ce2.getId() });
		if (l.size() > 1) {
			throw new NonUniqueResultException(
					"More than one dynamic CatalogEntryRelationshipInstance found for ['"
							+ relTypeName + "', " + ce1.getClass().getName()
							+ ":" + ce1.getId() + ", "
							+ ce1.getClass().getName() + ":" + ce1.getId()
							+ "]");

		}
		if (l.size() == 1) {
			relInst = (CatalogEntryRelationshipInstance) l.iterator().next();
		}
		return relInst;
	}

	public List<CatalogEntryRelationshipInstance> getRelationshipsForCatalogEntry(
			Integer entryId) {
		List<CatalogEntryRelationshipInstance> l = getHibernateTemplate()
				.find(
						"from CatalogEntryRelationshipInstance ri where " +
						"ri.roleA.catalogEntry.id = ? or ri.roleB.catalogEntry.id = ? ",
						new Object[] { entryId, entryId });

		return l;
	}
}
