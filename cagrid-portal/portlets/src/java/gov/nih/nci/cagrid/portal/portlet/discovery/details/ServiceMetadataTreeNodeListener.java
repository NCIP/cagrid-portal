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
package gov.nih.nci.cagrid.portal.portlet.discovery.details;

import gov.nih.nci.cagrid.portal.domain.DomainObject;
import gov.nih.nci.cagrid.portal.domain.GridDataService;
import gov.nih.nci.cagrid.portal.domain.GridService;
import gov.nih.nci.cagrid.portal.domain.metadata.common.Enumeration;
import gov.nih.nci.cagrid.portal.domain.metadata.common.PointOfContact;
import gov.nih.nci.cagrid.portal.domain.metadata.common.ResearchCenter;
import gov.nih.nci.cagrid.portal.domain.metadata.common.ResearchCenterPointOfContact;
import gov.nih.nci.cagrid.portal.domain.metadata.common.SemanticMetadata;
import gov.nih.nci.cagrid.portal.domain.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.portal.domain.metadata.common.ValueDomain;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.SourceUMLAssociationEdge;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.TargetUMLAssociationEdge;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLAssociationEdge;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.XMLSchema;
import gov.nih.nci.cagrid.portal.domain.metadata.service.ContextProperty;
import gov.nih.nci.cagrid.portal.domain.metadata.service.Fault;
import gov.nih.nci.cagrid.portal.domain.metadata.service.InputParameter;
import gov.nih.nci.cagrid.portal.domain.metadata.service.Operation;
import gov.nih.nci.cagrid.portal.domain.metadata.service.Output;
import gov.nih.nci.cagrid.portal.domain.metadata.service.Service;
import gov.nih.nci.cagrid.portal.domain.metadata.service.ServiceContext;
import gov.nih.nci.cagrid.portal.domain.metadata.service.ServicePointOfContact;
import gov.nih.nci.cagrid.portal.portlet.tree.NodeState;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeNode;
import gov.nih.nci.cagrid.portal.portlet.tree.TreeNodeListener;
import gov.nih.nci.cagrid.portal.portlet.util.PortletUtils;
import gov.nih.nci.cagrid.portal.util.PortalUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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
	 * @see gov.nih.nci.cagrid.portal.portlet.tree.TreeNodeListener#onClose(gov.nih.nci.cagrid.portal.portlet.tree.TreeNode,
	 *      java.util.Map)
	 */
	public void onClose(TreeNode node, Map params) {
		// Nothing to do here
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
		} else if (object instanceof DomainModel) {
			handleDomainModelNode(node, params, (DomainModel) object);
		} else if (object instanceof UMLClass) {
			handleUMLClassNode(node, params, (UMLClass) object);
		} else if (object instanceof UMLAttribute) {
			handleUMLAttributeNode(node, params, (UMLAttribute) object);
		} else if (object instanceof ValueDomain) {
			handleValueDomainNode(node, params, (ValueDomain) object);
		} else if (object instanceof Enumeration) {
			handleEnumerationNode(node, params, (Enumeration) object);
		} else if (object instanceof Operation) {
			handleOperationNode(node, params, (Operation) object);
		} else {
			logger.warn("Unhandled type: " + object);
		}
	}

	public void handleOperationNode(TreeNode node, Map params,
			Operation operation) {
		List<InputParameter> inputs = operation.getInputParameterCollection();
		if (inputs.size() > 0) {
			TreeNode inputsNode = new TreeNode(node, "inputParameterCollection");
			inputsNode.setLabel("Input Parameters");
			node.getChildren().add(inputsNode);
			int i = 0;
			for (InputParameter param : inputs) {

				TreeNode paramNode = new TreeNode(inputsNode, "param_" + i++);
				paramNode.setLabel(param.getName());
				inputsNode.getChildren().add(paramNode);
				paramNode.setContent(param);
			}
		}

		Output output = operation.getOutput();
		if (output != null) {
			TreeNode outputNode = new TreeNode(node, "output");
			outputNode.setLabel("Output");
			node.getChildren().add(outputNode);
			outputNode.setContent(output);
		}

		List<Fault> faults = operation.getFaultCollection();
		if (faults.size() > 0) {
			TreeNode faultsNode = new TreeNode(node, "faultCollection");
			faultsNode.setLabel("Faults");
			node.getChildren().add(faultsNode);
			int i = 0;
			for (Fault fault : faults) {

				TreeNode faultNode = new TreeNode(faultsNode, "fault_" + i++);
				faultNode.setLabel(fault.getName());
				faultsNode.getChildren().add(faultNode);
				faultNode.setContent(fault);
			}
		}

		addSemanticMetadata(node, operation.getSemanticMetadata());
	}

	public void handleEnumerationNode(TreeNode node, Map params,
			Enumeration enumeration) {

		addSemanticMetadata(node, enumeration.getSemanticMetadata());

	}

	public void handleValueDomainNode(TreeNode node, Map params,
			ValueDomain valueDomain) {

		List<Enumeration> enums = valueDomain.getEnumerationCollection();
		if (enums.size() > 0) {

			TreeNode enumsNode = new TreeNode(node, "enumerationCollection");
			enumsNode.setLabel("Enumeration");
			node.getChildren().add(enumsNode);
			int i = 0;
			for (Enumeration e : enums) {

				TreeNode enumNode = new TreeNode(enumsNode, "enum_" + i++);
				enumNode.setLabel(e.getPermissibleValue());
				enumsNode.getChildren().add(enumNode);
				enumNode.setContent(e);

			}
			sortChildren(enumsNode);
		}

		addSemanticMetadata(node, valueDomain.getSemanticMetadata());
	}

	public void handleUMLAttributeNode(TreeNode node, Map params,
			UMLAttribute attribute) {

		ValueDomain valueDomain = attribute.getValueDomain();
		if (valueDomain != null) {
			TreeNode vdNode = new TreeNode(node, "valueDomain");
			vdNode.setLabel("Value Domain");
			node.getChildren().add(vdNode);
			vdNode.setContent(valueDomain);
		}

		addSemanticMetadata(node, attribute.getSemanticMetadata());
	}

	public void addUMLClass(TreeNode node, Map params, UMLClass umlClass,
			String packageName) {
		int dotIdx = packageName.indexOf(".");
		if (dotIdx != -1) {
			String localName = packageName.substring(0, dotIdx);

			TreeNode pkgNode = null;
			for (Iterator i = node.getChildren().iterator(); i.hasNext();) {
				TreeNode child = (TreeNode) i.next();
				if (child.getName().equals(localName)) {
					pkgNode = child;
					break;
				}
			}
			if (pkgNode == null) {
				pkgNode = new TreeNode(node, localName);
				pkgNode.setLabel(localName);
				node.getChildren().add(pkgNode);
			}
			sortChildren(node);

			addUMLClass(pkgNode, params, umlClass, packageName
					.substring(dotIdx + 1));

		} else {

			TreeNode umlClassNode = new TreeNode(node, "umlClass_"
					+ umlClass.getId());
			umlClassNode.setLabel(umlClass.getClassName());
			node.getChildren().add(umlClassNode);
			umlClassNode.setContent(umlClass);
			sortChildren(node);
		}
	}

	private void sortChildren(TreeNode node) {
		SortedMap<String, TreeNode> sorted = new TreeMap<String, TreeNode>();
		for (TreeNode c : node.getChildren()) {
			sorted.put(c.getLabel(), c);
		}
		node.getChildren().clear();
		node.getChildren().addAll(sorted.values());
	}

	public void handleUMLClassNode(TreeNode node, Map params, UMLClass umlClass) {

		List<TreeNode> attNodes = new ArrayList<TreeNode>();
		List<TreeNode> assocNodes = new ArrayList<TreeNode>();

		int attIdx = 0;
		int assocIdx = 0;

		TreeNode attsNode = new TreeNode(node, "umlAttributeCollection");
		TreeNode assocsNode = new TreeNode(node, "associations");

		UMLClass superClass = umlClass;
		while (superClass != null) {

			// Add attributes
			List<UMLAttribute> atts = superClass.getUmlAttributeCollection();
			for (UMLAttribute att : atts) {

				TreeNode attNode = new TreeNode(attsNode, "att_" + attIdx++);
				attNode.setLabel(att.getName());
				attNodes.add(attNode);
				attNode.setContent(att);
			}

			// Add associations
			List<UMLAssociationEdge> edgesToAdd = PortalUtils.getOtherEdges(
					superClass.getClassName(), superClass.getAssociations());

			for (UMLAssociationEdge edge : edgesToAdd) {
				TreeNode assocNode = new TreeNode(assocsNode, "assoc_"
						+ assocIdx++);
				assocNode.setLabel(edge.getRole());
				assocNodes.add(assocNode);
				assocNode.setContent(edge);
			}

			superClass = superClass.getSuperClass();
		}
		if (attNodes.size() > 0) {
			attsNode.setLabel("Attributes");
			node.getChildren().add(attsNode);
			attsNode.getChildren().addAll(attNodes);
			sortChildren(attsNode);
		}
		if (assocNodes.size() > 0) {
			assocsNode.setLabel("Associations");
			node.getChildren().add(assocsNode);
			assocsNode.getChildren().addAll(assocNodes);
			sortChildren(assocsNode);
		}

		// Add semantic metadata
		addSemanticMetadata(node, umlClass.getSemanticMetadata());

	}

	public void addSemanticMetadata(TreeNode node, List<SemanticMetadata> sms) {
		if (sms.size() > 0) {
			TreeNode smsNode = new TreeNode(node, "semanticMetadata");
			smsNode.setLabel("Semantic Metadata");
			node.getChildren().add(smsNode);
			smsNode.setContent(sms);
		}
	}

	public void handleDomainModelNode(TreeNode node, Map params,
			DomainModel domainModel) {

		List<UMLClass> classes = domainModel.getClasses();
		TreeNode classesNode = new TreeNode(node, "classes");
		classesNode.setLabel("UML Classes");
		node.getChildren().add(classesNode);
		for (UMLClass umlClass : classes) {
			addUMLClass(classesNode, params, umlClass, umlClass
					.getPackageName()+".");
		}

		List<XMLSchema> xmlSchemas = domainModel.getXmlSchemas();
		if (xmlSchemas.size() > 0) {
			TreeNode xmlSchemasNode = new TreeNode(node, "xmlSchemas");
			xmlSchemasNode.setLabel("XML Schemas");
			node.getChildren().add(xmlSchemasNode);
			xmlSchemasNode.setContent(xmlSchemas);
		}
	}

	public void handleServiceContextNode(TreeNode node, Map params,
			ServiceContext serviceContext) {
		logger.debug("on update for ServiceContext");

		List<ContextProperty> cps = serviceContext
				.getContextPropertyCollection();
		if (cps.size() > 0) {
			TreeNode cpsNode = new TreeNode(node, "contextPropertyCollection");
			cpsNode.setLabel("Context Properties");
			node.getChildren().add(cpsNode);
			cpsNode.setContent(cps);
		}

		List<Operation> ops = serviceContext.getOperationCollection();
		if (ops.size() > 0) {
			TreeNode opsNode = new TreeNode(node, "operationCollection");
			opsNode.setLabel("Operations");
			node.getChildren().add(opsNode);
			int i = 0;
			for (Operation op : ops) {
				TreeNode opNode = new TreeNode(opsNode, "op_" + i++);
				opNode.setLabel(op.getName());
				opsNode.getChildren().add(opNode);
				opNode.setContent(op);
			}
			sortChildren(opsNode);
		}
	}

	public void handleGridServiceNode(TreeNode node, Map params,
			GridService gridService) {

		if (gridService instanceof GridDataService) {

			GridDataService dataService = (GridDataService) gridService;
			DomainModel domainModel = dataService.getDomainModel();
			TreeNode dmNode = new TreeNode(node, "domainModel");
			dmNode.setLabel("Domain Model");
			node.getChildren().add(dmNode);
			dmNode.setContent(domainModel);
		}

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
		addSemanticMetadata(node, sms);

		// Add points of contact
		List<ServicePointOfContact> pocs = svcDesc
				.getPointOfContactCollection();
		if (pocs.size() > 0) {
			TreeNode pocsNode = new TreeNode(node, "pointOfContactCollection");
			pocsNode.setLabel("Points of Contact");
			node.getChildren().add(pocsNode);
			i = 0;
			for (PointOfContact poc : pocs) {
				TreeNode pocNode = new TreeNode(pocsNode, "poc_" + i++);
				pocNode.setLabel(poc.getPerson().getFirstName() + " "
						+ poc.getPerson().getLastName());
				pocsNode.getChildren().add(pocNode);
				pocNode.setContent(poc);
				pocNode.setState(NodeState.OPEN);
			}
			sortChildren(pocsNode);
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

		List<ResearchCenterPointOfContact> pocs = researchCenter
				.getPointOfContactCollection();
		if (pocs.size() > 0) {
			TreeNode pocsNode = new TreeNode(node, "pointOfContactCollection");
			pocsNode.setLabel("Points of Contact");
			node.getChildren().add(pocsNode);
			int i = 0;
			for (PointOfContact poc : pocs) {
				TreeNode pocNode = new TreeNode(pocsNode, "poc_" + i++);
				pocNode.setLabel(poc.getPerson().getFirstName() + " "
						+ poc.getPerson().getLastName());
				pocsNode.getChildren().add(pocNode);
				pocNode.setContent(poc);
				pocNode.setState(NodeState.OPEN);
			}
			sortChildren(pocsNode);
		}

	}

	public HibernateTemplate getHibernateTemplate() {
		return hibernateTemplate;
	}

	public void setHibernateTemplate(HibernateTemplate hibernateTemplate) {
		this.hibernateTemplate = hibernateTemplate;
	}

}
