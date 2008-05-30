/**
 *
 */
package gov.nih.nci.cagrid.portal.portlet.query.cql;

import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.*;
import gov.nih.nci.cagrid.portal.portlet.query.QueryConstants;
import gov.nih.nci.cagrid.portal.portlet.query.builder.ForeignTargetsProvider;
import gov.nih.nci.cagrid.portal.portlet.query.dcql.ForeignUMLClassBean;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeNode;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeNodeListener;
import gov.nih.nci.cagrid.portal.util.PortalUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:joshua.phillips@semanticbits.com">Joshua Phillips</a>
 */
@Transactional
public class UMLClassTreeNodeListener implements TreeNodeListener {

	private static final Log logger = LogFactory
			.getLog(UMLClassTreeNodeListener.class);
	private HibernateTemplate hibernateTemplate;

	private ForeignTargetsProvider targetsProvider;

	/**
	 * 
	 */
	public UMLClassTreeNodeListener() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.portlet.tree.TreeNodeListener#onClose(gov.nih.nci.cagrid.portal.portlet.tree.TreeNode,
	 *      java.util.Map)
	 */
	public void onClose(TreeNode node, Map params) {
		// Nothing

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal.portlet.tree.TreeNodeListener#onOpen(gov.nih.nci.cagrid.portal.portlet.tree.TreeNode,
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
				umlClass = (UMLClass) getHibernateTemplate().get(
						umlClass.getClass(), umlClass.getId());
				if (content instanceof ForeignUMLClassBean) {
					UMLClassBean parentBean = (UMLClassBean) node.getParent()
							.getContent();
					umlClassBean = new ForeignUMLClassBean(umlClass, parentBean
							.getAttributes().get(0));
				} else {
					umlClassBean = new UMLClassBean(umlClass);
				}
				node.setContent(umlClassBean);

				for (UMLAssociationEdge edge : PortalUtils.getOtherEdges(
						umlClass.getClassName(), umlClass.getAssociations())) {
					UMLClass targetType = edge.getType();
					TreeNode targetNode = new TreeNode(node, edge.getRole());
					targetNode.setLabel(edge.getRole());
					node.getChildren().add(targetNode);
					targetNode.setContent(new UMLClassBean(targetType));
				}
				// Add foreign nodes
				List<UMLClass> classes = targetsProvider
						.getSemanticallyEquivalentClasses(umlClassBean
								.getUmlClass());
				int count = 0;
				for (UMLClass target : classes) {
					TreeNode fnode = new TreeNode(node,
							QueryConstants.FOREIGN_UML_CLASS_PREFIX
									+ target.getId() + ":" + count++);
					fnode.setLabel(target.getClassName());
					// add only if foreign join can be established
					try {
						fnode.setContent(new ForeignUMLClassBean(target,
								umlClassBean.getAttributes().get(0)));
					} catch (IllegalArgumentException e) {
						logger
								.warn("Class does not have valid Join attributes."
										+ "Not adding Foreign class with ID "
										+ target.getId());
					}
					node.getChildren().add(fnode);
				}
			}
		}
	}

	public ForeignTargetsProvider getTargetsProvider() {
		return targetsProvider;
	}

	public void setTargetsProvider(ForeignTargetsProvider targetsProvider) {
		this.targetsProvider = targetsProvider;
	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}
