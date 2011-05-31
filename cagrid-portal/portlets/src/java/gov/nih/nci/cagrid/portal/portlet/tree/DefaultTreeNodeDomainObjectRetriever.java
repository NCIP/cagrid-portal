/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.tree;

import gov.nih.nci.cagrid.portal.domain.DomainObject;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class DefaultTreeNodeDomainObjectRetriever implements
		TreeNodeDomainObjectRetriever {

	/**
	 * 
	 */
	public DefaultTreeNodeDomainObjectRetriever() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.tree.TreeNodeDomainObjectRetriever#retrieve(gov.nih.nci.cagrid.portal.portlet.tree.TreeNode)
	 */
	public DomainObject retrieve(TreeNode node) {
		DomainObject domainObject = null;
		if(node.getContent() != null && node.getContent() instanceof DomainObject){
			domainObject = (DomainObject)node.getContent();
		}
		return domainObject;
	}

}
