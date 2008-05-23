/**
 * 
 */
package gov.nih.nci.cagrid.portal.aggr.regsvc;

import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLAssociationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLClassCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelUmlGeneralizationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationSourceUMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationTargetUMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference;
import gov.nih.nci.cagrid.metadata.dataservice.UMLGeneralization;
import gov.nih.nci.cagrid.portal.domain.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.SourceUMLAssociationEdge;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.TargetUMLAssociationEdge;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLAssociationEdge;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.portal.domain.metadata.dataservice.XMLSchema;
import gov.nih.nci.cagrid.portal.util.PortalUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author <a href="joshua.phillips@semanticbits.com">Joshua Phillips</a>
 * 
 */
public class DomainModelBuilder extends ServiceMetadataBuilder {

	private static final Log logger = LogFactory
			.getLog(DomainModelBuilder.class);

//	private String gmeUrl;
//	private String cadsrUrl;

	public DomainModel build(
			gov.nih.nci.cagrid.metadata.dataservice.DomainModel modelIn)
			throws Exception {
		
		pushPath("domainModel");
		DomainModel modelOut = new DomainModel();

		Map<String, List<UMLAssociationEdge>> assocMap = new HashMap<String, List<UMLAssociationEdge>>();
		Map<String, List<String>> subClassMap = new HashMap<String, List<String>>();
		Map<String, String> superClassMap = new HashMap<String, String>();

		// Persist associations
		DomainModelExposedUMLAssociationCollection assocColl = modelIn
				.getExposedUMLAssociationCollection();
		if (assocColl != null) {
			gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation[] umlAssocIns = assocColl
					.getUMLAssociation();
			if (umlAssocIns != null && umlAssocIns.length > 0) {
				for (gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation umlAssocIn : umlAssocIns) {

					String targetRefId = null;
					String sourceRefId = null;
					SourceUMLAssociationEdge sourceEdgeOut = null;
					TargetUMLAssociationEdge targetEdgeOut = null;

					UMLAssociationSourceUMLAssociationEdge srcEdgeContainer = umlAssocIn
							.getSourceUMLAssociationEdge();
					if (srcEdgeContainer != null) {
						if (srcEdgeContainer.getUMLAssociationEdge() != null) {
							sourceRefId = srcEdgeContainer
									.getUMLAssociationEdge()
									.getUMLClassReference().getRefid();
							sourceEdgeOut = (SourceUMLAssociationEdge) buildUMLAssociationEdge(
									srcEdgeContainer.getUMLAssociationEdge(),
									true);
						}
					}
					UMLAssociationTargetUMLAssociationEdge targetEdgeContainer = umlAssocIn
							.getTargetUMLAssociationEdge();
					if (targetEdgeContainer != null) {
						if (targetEdgeContainer.getUMLAssociationEdge() != null) {
							targetRefId = targetEdgeContainer
									.getUMLAssociationEdge()
									.getUMLClassReference().getRefid();
							targetEdgeOut = (TargetUMLAssociationEdge) buildUMLAssociationEdge(
									targetEdgeContainer.getUMLAssociationEdge(),
									false);
						}
					}

					UMLAssociation umlAssocOut = buildUMLAssociation(umlAssocIn);
					if (sourceEdgeOut != null) {
						sourceEdgeOut.setAssociation(umlAssocOut);
						umlAssocOut.setSource(sourceEdgeOut);
						if (sourceRefId == null) {
							throw new RuntimeException(
									"Source reference ID is null for UMLAssociation in DomainModel "
											+ modelIn.getProjectLongName());
						}
						addAssocMapping(assocMap, sourceRefId, sourceEdgeOut);
					}
					if (targetEdgeOut != null) {
						targetEdgeOut.setAssociation(umlAssocOut);
						umlAssocOut.setTarget(targetEdgeOut);
						if (targetRefId == null) {
							throw new RuntimeException(
									"Target reference ID is null for UMLAssociation in DomainModel "
											+ modelIn.getProjectLongName());
						}
						addAssocMapping(assocMap, targetRefId, targetEdgeOut);
					}
					umlAssocOut = (UMLAssociation) handlePersist(umlAssocOut);
				}
			}
		}

		// Persist generalizations
		DomainModelUmlGeneralizationCollection genColl = modelIn
				.getUmlGeneralizationCollection();
		if (genColl != null) {
			UMLGeneralization[] genIns = genColl.getUMLGeneralization();
			if (genIns != null) {
				for (UMLGeneralization genIn : genIns) {
					UMLClassReference subClassRef = genIn
							.getSubClassReference();
					UMLClassReference superClassRef = genIn
							.getSuperClassReference();
					List<String> subClassRefIds = subClassMap.get(superClassRef
							.getRefid());
					if (subClassRefIds == null) {
						subClassRefIds = new ArrayList<String>();
						subClassMap.put(superClassRef.getRefid(),
								subClassRefIds);
					}
					subClassRefIds.add(subClassRef.getRefid());

					superClassMap.put(subClassRef.getRefid(), superClassRef
							.getRefid());
				}
			}
		}

		// Persist classes
		Map<String, UMLClass> umlClasses = new HashMap<String, UMLClass>();
		DomainModelExposedUMLClassCollection classColl = modelIn
				.getExposedUMLClassCollection();
		if (classColl != null) {
			gov.nih.nci.cagrid.metadata.dataservice.UMLClass[] umlClassIns = classColl
					.getUMLClass();
			if (umlClassIns != null && umlClassIns.length > 0) {
				pushPath("classes");
				for (gov.nih.nci.cagrid.metadata.dataservice.UMLClass umlClassIn : umlClassIns) {
					UMLClass umlClassOut = buildUMLClass(umlClassIn);
					umlClasses.put(umlClassIn.getId(), umlClassOut);
				}
				popPath();
				for (String umlClassId : umlClasses.keySet()) {
					UMLClass umlClassOut = umlClasses.get(umlClassId);
					List<UMLAssociationEdge> assocs = assocMap.get(umlClassId);
					if (assocs != null) {

						umlClassOut.getAssociations().addAll(assocs);
						for (UMLAssociationEdge edge : assocs) {
							edge.setType(umlClassOut);
						}
					}
					List<String> subClassRefIds = subClassMap.get(umlClassId);
					if (subClassRefIds != null) {
						for (String refId : subClassRefIds) {
							UMLClass subClass = umlClasses.get(refId);
							if (subClass != null) {
								umlClassOut.getSubClasses().add(subClass);
							}
						}
					}
					String superClassRefId = superClassMap.get(umlClassId);
					if (superClassRefId != null) {
						UMLClass superClass = umlClasses.get(superClassRefId);
						if (superClass != null) {
							umlClassOut.setSuperClass(superClass);
						}
					}

					umlClassOut = (UMLClass) handlePersist(umlClassOut);

					umlClassOut.setModel(modelOut);
					modelOut.getClasses().add(umlClassOut);
				}

			}
		}

		// Remove duplicate associations
		for (UMLClass umlClass : modelOut.getClasses()) {
			Map<String, SourceUMLAssociationEdge> dedupped = new HashMap<String, SourceUMLAssociationEdge>();
			for (UMLAssociationEdge edge : umlClass.getAssociations()) {
				if (edge instanceof SourceUMLAssociationEdge) {
					SourceUMLAssociationEdge source = (SourceUMLAssociationEdge) edge;
					TargetUMLAssociationEdge target = source.getAssociation()
							.getTarget();
					UMLClass targetType = target.getType();
					String targetTypeClassName = targetType.getPackageName()
							+ "." + targetType.getClassName();
					SourceUMLAssociationEdge aSource = dedupped.get(target
							.getRole());
					if (aSource != null) {
						// Prefer the more general
						TargetUMLAssociationEdge aTarget = aSource
								.getAssociation().getTarget();
						UMLClass aTargetType = aTarget.getType();
						UMLClass aTargetTypeSuper = aTargetType.getSuperClass();
						if (aTargetTypeSuper == null) {
							// Then this is the most general
							// Keep this one
						} else {
							search: while (aTargetTypeSuper != null) {
								String aTargetTypeSuperClassName = aTargetTypeSuper
										.getPackageName()
										+ "." + aTargetTypeSuper.getClassName();
								if (targetTypeClassName
										.equals(aTargetTypeSuperClassName)) {
									dedupped.put(target.getRole(), source);
									break search;
								}
								aTargetTypeSuper = aTargetTypeSuper
										.getSuperClass();
							}

						}
					}
				}
			}
			List<UMLAssociationEdge> deduppedList = new ArrayList<UMLAssociationEdge>();
			deduppedList.addAll(dedupped.values());
			umlClass.setAssociations(deduppedList);
			handlePersist(modelOut);
			handlePersist(umlClass);
		}

		modelOut.setProjectDescription(modelIn.getProjectDescription());
		modelOut.setProjectLongName(modelIn.getProjectLongName());
		modelOut.setProjectShortName(modelIn.getProjectShortName());
		modelOut.setProjectVersion(modelIn.getProjectVersion());


		popPath();
		return (DomainModel) handlePersist(modelOut);
	}


	private void addAssocMapping(
			Map<String, List<UMLAssociationEdge>> assocMap, String refId,
			UMLAssociationEdge edgeOut) {
		List<UMLAssociationEdge> edges = assocMap.get(refId);
		if (edges == null) {
			edges = new ArrayList<UMLAssociationEdge>();
			assocMap.put(refId, edges);
		}

		// Ensure that only the most general type is used as the type
		// of a target
		edges.add(edgeOut);
	}

	private UMLAssociation buildUMLAssociation(
			gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation umlAssocIn) {
		UMLAssociation umlAssocOut = new UMLAssociation();
		umlAssocOut.setBidirectional(umlAssocIn.isBidirectional());
		return (UMLAssociation) handlePersist(umlAssocOut);
	}

	private UMLAssociationEdge buildUMLAssociationEdge(
			gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdge edgeIn,
			boolean isSource) {
		UMLAssociationEdge edgeOut = null;
		if (isSource) {
			edgeOut = new SourceUMLAssociationEdge();
		} else {
			edgeOut = new TargetUMLAssociationEdge();
		}
		edgeOut.setMaxCardinality(edgeIn.getMaxCardinality());
		edgeOut.setMinCardinality(edgeIn.getMinCardinality());
		edgeOut.setRole(edgeIn.getRoleName());
		return (UMLAssociationEdge) handlePersist(edgeOut);
	}

	private UMLClass buildUMLClass(
			gov.nih.nci.cagrid.metadata.dataservice.UMLClass umlClassIn) {
		UMLClass umlClassOut = new UMLClass();
		handlePersist(umlClassOut);
		if (umlClassIn.getSemanticMetadata() != null) {
			String objectIdentifier = getObjectIdentifier(umlClassOut);
			for (gov.nih.nci.cagrid.metadata.common.SemanticMetadata sMetaIn : umlClassIn
					.getSemanticMetadata()) {
				umlClassOut.getSemanticMetadata().add(
						buildSemanticMetadata(objectIdentifier, sMetaIn));
			}
		}
		if (umlClassIn.getUmlAttributeCollection() != null) {
			pushPath("umlAttributeCollection");
			for (gov.nih.nci.cagrid.metadata.common.UMLAttribute umlAttrIn : umlClassIn
					.getUmlAttributeCollection().getUMLAttribute()) {
				UMLAttribute umlAttrOut = buildUMLAttribute(umlAttrIn);
				umlAttrOut.setUmlClass(umlClassOut);
				umlClassOut.getUmlAttributeCollection().add(umlAttrOut);
			}
			popPath();
		}
		umlClassOut.setCadsrId(umlClassIn.getId());
		umlClassOut.setClassName(umlClassIn.getClassName());
		umlClassOut.setDescription(umlClassIn.getDescription());
		umlClassOut.setPackageName(umlClassIn.getPackageName());
		umlClassOut.setProjectName(umlClassIn.getProjectName());
		umlClassOut.setProjectVersion(umlClassIn.getProjectVersion());
		umlClassOut.setAllowableAsTarget(umlClassIn.isAllowableAsTarget());
		return (UMLClass) handlePersist(umlClassOut);
	}

}
