/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.tree;

import gov.nih.nci.cagrid.portal2.domain.DomainObject;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public interface TreeNodeDomainObjectRetriever {
	DomainObject retrieve(TreeNode node);
}
