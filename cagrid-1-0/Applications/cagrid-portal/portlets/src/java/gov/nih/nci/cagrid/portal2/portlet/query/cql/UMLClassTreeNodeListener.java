/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.query.cql;

import gov.nih.nci.cagrid.portal2.domain.DomainObject;
import gov.nih.nci.cagrid.portal2.domain.metadata.dataservice.SourceUMLAssociationEdge;
import gov.nih.nci.cagrid.portal2.domain.metadata.dataservice.TargetUMLAssociationEdge;
import gov.nih.nci.cagrid.portal2.domain.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.portal2.domain.metadata.dataservice.UMLAssociationEdge;
import gov.nih.nci.cagrid.portal2.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal2.portlet.tree.TreeNode;
import gov.nih.nci.cagrid.portal2.portlet.tree.TreeNodeListener;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
@Transactional
public class UMLClassTreeNodeListener implements TreeNodeListener {

	private static final Log logger = LogFactory
			.getLog(UMLClassTreeNodeListener.class);
	private HibernateTemplate hibernateTemplate;

	/**
	 * 
	 */
	public UMLClassTreeNodeListener() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal2.portlet.tree.TreeNodeListener#onClose(gov.nih.nci.cagrid.portal2.portlet.tree.TreeNode,
	 *      java.util.Map)
	 */
	public void onClose(TreeNode node, Map params) {
		// Nothing

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal2.portlet.tree.TreeNodeListener#onOpen(gov.nih.nci.cagrid.portal2.portlet.tree.TreeNode,
	 *      java.util.Map)
	 */
	public void onOpen(TreeNode node, Map params) {
		if (node.getChildren().size() == 0) {
			Object content = node.getContent();
			if (!(content instanceof UMLClassBean)) {
				logger.warn(node.getPath()
						+ " content is not instanceof UMLClassBean");
			} else {
				UMLClassBean umlClassBean = (UMLClassBean) content;
				UMLClass umlClass = umlClassBean.getUmlClass();
				umlClass = (UMLClass) getHibernateTemplate().get(umlClass.getClass(),
						umlClass.getId());
				umlClassBean = new UMLClassBean(umlClass);
				node.setContent(umlClassBean);
				for (UMLAssociationEdge edge : umlClass.getAssociations()) {
					if (edge instanceof SourceUMLAssociationEdge) {
						UMLAssociation assoc = ((SourceUMLAssociationEdge)edge).getAssociation();
						TargetUMLAssociationEdge target = assoc.getTarget();
						UMLClass targetType = target.getType();
						TreeNode targetNode = new TreeNode(node, target.getRole());
						targetNode.setLabel(target.getRole());
						node.getChildren().add(targetNode);
						targetNode.setContent(new UMLClassBean(targetType));
					}
				}
			}
		}
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}
