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
