/**
 * 
 */
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.portal.domain.DomainObject;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeNode;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeNodeDomainObjectRetriever;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 *
 */
public class CriteriaBeanTreeNodeDomainObjectRetriever implements
		TreeNodeDomainObjectRetriever {

	/**
	 * 
	 */
	public CriteriaBeanTreeNodeDomainObjectRetriever() {

	}

	/* (non-Javadoc)
	 * @see gov.nih.nci.cagrid.portal.portlet.tree.TreeNodeDomainObjectRetriever#retrieve(gov.nih.nci.cagrid.portal.portlet.tree.TreeNode)
	 */
	public DomainObject retrieve(TreeNode node) {
		DomainObject domainObject = null;
		if(node.getContent() != null && node.getContent() instanceof AssociationBean){
			domainObject = ((AssociationBean)node.getContent()).getCriteriaBean().getUmlClass();
		}
		return domainObject;
	}

}
