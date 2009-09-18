/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.browse;

import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntry;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRoleInstance;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * 
 */
public class RelatedCatalogEntryViewBean {

	private CatalogEntry catalogEntry;
	private SortedSet<CatalogEntryRoleTypeViewBean> orderedRoleTypes = new TreeSet<CatalogEntryRoleTypeViewBean>();

	public RelatedCatalogEntryViewBean(CatalogEntry catalogEntry) {
		this.catalogEntry = catalogEntry;
	}

	public CatalogEntry getCatalogEntry() {
		return catalogEntry;
	}

	public void setCatalogEntry(CatalogEntry catalogEntry) {
		this.catalogEntry = catalogEntry;
	}

	public SortedSet<CatalogEntryRoleTypeViewBean> getOrderedRoleTypes() {
		return orderedRoleTypes;
	}

	public void setOrderedRoleTypes(
			SortedSet<CatalogEntryRoleTypeViewBean> orderedRoleTypes) {
		this.orderedRoleTypes = orderedRoleTypes;
	}

	public void addRoleInstance(CatalogEntryRoleInstance targetRole) {
		CatalogEntryRoleTypeViewBean roleTypeVB = null;
		for (CatalogEntryRoleTypeViewBean aRoleTypeVB : getOrderedRoleTypes()) {
			if (aRoleTypeVB.getCatalogEntryRoleType().getName().equals(
					targetRole.getType().getName())) {
				roleTypeVB = aRoleTypeVB;
				break;
			}
		}
		if (roleTypeVB == null) {
			roleTypeVB = new CatalogEntryRoleTypeViewBean(targetRole.getType());
			getOrderedRoleTypes().add(roleTypeVB);
		}
		roleTypeVB.addRoleInstance(targetRole);
	}

}
