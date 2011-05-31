/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.browse;

import java.util.Date;

import gov.nih.nci.cagrid.portal.domain.catalog.Commentable;
import gov.nih.nci.cagrid.portal.domain.catalog.Temporal;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com>Joshua Phillips</a>
 *
 */
public class TemporalCommentableViewBean<T extends Commentable> extends CommentableViewBean<T> implements
		Temporal {

	public TemporalCommentableViewBean(T commentable) {
		super(commentable);
		if(!(commentable instanceof Temporal)){
			throw new IllegalArgumentException("argument must be of type Temporal");
		}
	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.domain.catalog.Temporal#getCreatedAt()
	 */
	public Date getCreatedAt() {
		return ((Temporal)getCommentable()).getCreatedAt();
	}

}
