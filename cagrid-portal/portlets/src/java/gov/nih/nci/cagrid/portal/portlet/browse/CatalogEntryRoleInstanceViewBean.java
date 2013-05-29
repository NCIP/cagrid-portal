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
