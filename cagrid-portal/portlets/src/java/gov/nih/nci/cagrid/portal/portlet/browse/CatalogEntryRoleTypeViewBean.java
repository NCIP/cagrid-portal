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
package gov.nih.nci.cagrid.portal.portlet.browse;

import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRoleInstance;
import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRoleType;
import gov.nih.nci.cagrid.portal.portlet.util.TemporalComparator;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * 
 */
public class CatalogEntryRoleTypeViewBean {

	private SortedSet<CatalogEntryRoleInstanceViewBean> roleInstances;
	private CatalogEntryRoleType catalogEntryRoleType;

	public CatalogEntryRoleTypeViewBean(
			CatalogEntryRoleType catalogEntryRoleType) {

		this.catalogEntryRoleType = catalogEntryRoleType;

		roleInstances = new TreeSet<CatalogEntryRoleInstanceViewBean>(
				new TemporalComparator());
	}

	public CatalogEntryRoleType getCatalogEntryRoleType() {
		return catalogEntryRoleType;
	}

	public SortedSet<CatalogEntryRoleInstanceViewBean> getOrderedRoleInstances() {
		return roleInstances;
	}

	public void addRoleInstance(CatalogEntryRoleInstance roleInstance) {
		//TODO: enable sub types
//		if (!roleInstance.getType().getId().equals(
//				getCatalogEntryRoleType().getId())) {
//			throw new IllegalArgumentException(
//					"This view bean is for roles of type: "
//							+ getCatalogEntryRoleType().getName());
//		}
		roleInstances.add(new CatalogEntryRoleInstanceViewBean(roleInstance));
	}
}
