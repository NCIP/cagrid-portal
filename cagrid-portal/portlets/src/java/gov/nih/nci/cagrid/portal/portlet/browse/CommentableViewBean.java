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

import gov.nih.nci.cagrid.portal.domain.catalog.Comment;
import gov.nih.nci.cagrid.portal.domain.catalog.Commentable;
import gov.nih.nci.cagrid.portal.portlet.util.TemporalComparator;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 *
 */
public class CommentableViewBean<T extends Commentable> {

	private T commentable;
	private SortedSet<Comment> orderedComments;
	
	public CommentableViewBean(T commentable){
		this.commentable = commentable;
		orderedComments = new TreeSet<Comment>(new TemporalComparator());
		orderedComments.addAll(commentable.getComments());
	}
	
	public T getCommentable(){
		return commentable;
	}
	
	public SortedSet<Comment> getOrderedComments(){
		return orderedComments;
	}
	
}
