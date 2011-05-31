/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.browse;

import gov.nih.nci.cagrid.portal.domain.catalog.CatalogEntryRoleInstance;
import gov.nih.nci.cagrid.portal.domain.catalog.Comment;
import gov.nih.nci.cagrid.portal.domain.catalog.Temporal;
import gov.nih.nci.cagrid.portal.portlet.util.TemporalComparator;

import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 * 
 */
public class CatalogEntryRoleInstanceViewBean implements Temporal {

	private CatalogEntryRoleInstance roleInstance;
	private SortedSet<Comment> orderedComments;

	@SuppressWarnings("unchecked")
	public CatalogEntryRoleInstanceViewBean(CatalogEntryRoleInstance roleInstance) {
		this.roleInstance = roleInstance;
		orderedComments = new TreeSet(new TemporalComparator());
		orderedComments.addAll(roleInstance.getComments());
	}
	
	public SortedSet<Comment> getOrderedComments(){
		return orderedComments;
	}
	
	public CatalogEntryRoleInstance getRoleInstance(){
		return roleInstance;
	}

	public Date getCreatedAt() {
		return getRoleInstance().getCreatedAt();
	}

}
