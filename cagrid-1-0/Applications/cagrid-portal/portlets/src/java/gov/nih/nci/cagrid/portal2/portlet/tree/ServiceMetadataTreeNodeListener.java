/**
 * 
 */
package gov.nih.nci.cagrid.portal2.portlet.tree;

import gov.nih.nci.cagrid.portal2.domain.DomainObject;
import gov.nih.nci.cagrid.portal2.domain.GridService;
import gov.nih.nci.cagrid.portal2.domain.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.portal2.domain.metadata.common.ResearchCenter;
import gov.nih.nci.cagrid.portal2.domain.metadata.common.SemanticMetadata;
import gov.nih.nci.cagrid.portal2.domain.metadata.service.ContextProperty;
import gov.nih.nci.cagrid.portal2.domain.metadata.service.Operation;
import gov.nih.nci.cagrid.portal2.domain.metadata.service.Service;
import gov.nih.nci.cagrid.portal2.domain.metadata.service.ServiceContext;

import java.util.List;
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
public class ServiceMetadataTreeNodeListener implements TreeNodeListener {

	private static final Log logger = LogFactory
			.getLog(ServiceMetadataTreeNodeListener.class);
	private HibernateTemplate hibernateTemplate;

	/**
	 * 
	 */
	public ServiceMetadataTreeNodeListener() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see gov.nih.nci.cagrid.portal2.portlet.tree.TreeNodeListener#onClose(gov.nih.nci.cagrid.portal2.portlet.tree.TreeNode,
	 *      java.util.Map)
	 */
	public void onClose(TreeNode node, Map params) {
		// Nothing to do here
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
			if (!(content instanceof DomainObject)) {
				logger.warn(node.getPath()
						+ " content is not instanceof DomainObject");
			} else {
				DomainObject domainObject = (DomainObject) content;
				node.setContent(getHibernateTemplate().get(
						domainObject.getClass(), domainObject.getId()));
				doOnOpen(node, params);
			}
		}
	}

	public void doOnOpen(TreeNode node, Map params) {

		Object object = node.getContent();
		if (object instanceof GridService) {
			handleGridServiceNode(node, params, (GridService) object);
		} else if (object instanceof ResearchCenter) {
			handleResearchCenterNode(node, params, (ResearchCenter) object);
		} else if (object instanceof ServiceContext) {
			handleServiceContextNode(node, params, (ServiceContext) object);
		} else {
			logger.warn("Unhandled type: " + object);
		}
	}

	public void handleServiceContextNode(TreeNode node, Map params,
			ServiceContext serviceContext) {
		logger.debug("on update for ServiceContext");
		
		List<ContextProperty> cps = serviceContext.getContextPropertyCollection();
		if(cps.size() > 0){
			TreeNode cpsNode = new TreeNode(node, "contextPropertyCollection");
			cpsNode.setLabel("Context Properties");
			node.getChildren().add(cpsNode);
			cpsNode.setContent(cps);
		}
		
		List<Operation> ops = serviceContext.getOperationCollection();
		if(ops.size() > 0){
			TreeNode opsNode = new TreeNode(node, "operationCollection");
			opsNode.setLabel("Operations");
			node.getChildren().add(opsNode);
			int i = 0;
			for(Operation op : ops){
				TreeNode opNode = new TreeNode(opsNode, "op_" + i++);
				opNode.setLabel(op.getName());
				opsNode.getChildren().add(opNode);
				opNode.setContent(op);
			}
		}
	}

	public void handleGridServiceNode(TreeNode node, Map params,
			GridService gridService) {
		logger.debug("on update for GridService");

		Service svcDesc = gridService.getServiceMetadata()
				.getServiceDescription();

		// Add service contexts
		List<ServiceContext> scs = svcDesc.getServiceContextCollection();
		TreeNode sccNode = new TreeNode(node, "serviceContextCollection");
		sccNode.setLabel("Service Contexts");
		node.getChildren().add(sccNode);
		int i = 0;
		for (ServiceContext sc : scs) {
			TreeNode scNode = new TreeNode(sccNode, "sc_" + i++);
			scNode.setLabel(sc.getName());
			sccNode.getChildren().add(scNode);
			scNode.setContent(sc);
		}

		// Add semantic metadata
		List<SemanticMetadata> sms = svcDesc.getSemanticMetadata();
		if (sms.size() > 0) {
			TreeNode smsNode = new TreeNode(node, "semanticMetadata");
			smsNode.setLabel("Semantic Metadata");
			node.getChildren().add(smsNode);
			smsNode.setContent(sms);
		}

		// Add points of contact
		List<PointOfContact> pocs = svcDesc.getPointOfContactCollection();
		if (pocs.size() > 0) {
			TreeNode pocsNode = new TreeNode(node, "pointOfContactCollection");
			pocsNode.setLabel("Points of Contact");
			node.getChildren().add(pocsNode);
			i = 0;
			for (PointOfContact poc : pocs) {
				TreeNode pocNode = new TreeNode(pocsNode, "poc_" + i++);
				pocNode.setLabel(poc.getPerson().getFirstName() + " " + poc.getPerson().getLastName());
				pocsNode.getChildren().add(pocNode);
				pocNode.setContent(poc);
				pocNode.setState(NodeState.OPEN);
			}
		}

		// Add research center
		ResearchCenter center = gridService.getServiceMetadata()
				.getHostingResearchCenter();
		if (center != null) {
			TreeNode rcNode = new TreeNode(node, "hostingResearchCenter");
			rcNode.setLabel("Hosting Research Center");
			rcNode.setContent(center);
			node.getChildren().add(rcNode);
		}

	}

	public void handleResearchCenterNode(TreeNode node, Map params,
			ResearchCenter researchCenter) {
		logger.debug("on update for ResearchService");

		List<PointOfContact> pocs = researchCenter
				.getPointOfContactCollection();
		if (pocs.size() > 0) {
			TreeNode pocsNode = new TreeNode(node, "pointOfContactCollection");
			pocsNode.setLabel("Points of Contact");
			node.getChildren().add(pocsNode);
			int i = 0;
			for (PointOfContact poc : pocs) {
				TreeNode pocNode = new TreeNode(pocsNode, "poc_" + i++);
				pocNode.setLabel(poc.getPerson().getFirstName() + " " + poc.getPerson().getLastName());
				pocsNode.getChildren().add(pocNode);
				pocNode.setContent(poc);
				pocNode.setState(NodeState.OPEN);
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
