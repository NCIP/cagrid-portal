package gov.nih.nci.cagrid.data.common;

import gov.nih.nci.cadsr.umlproject.domain.SemanticMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLAssociationMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLAssociationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLClassCollection;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdgeUmlClass;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationSourceUMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationTargetUMLAssociationEdge;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/** 
 *  MetadataUtilities
 *  Utilities for metadata
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 8, 2006 
 * @version $Id$ 
 */
public class MetadataUtilities {

	public static UMLClass createUmlClass(UMLClassMetadata meta) {
		UMLClass clazz = new UMLClass();
		clazz.set_package(meta.getUMLPackageMetadata().getName());
		clazz.setClassname(meta.getName());
		clazz.setDescription(meta.getDescription());
		clazz.setProjectName(meta.getProject().getLongName());
		clazz.setProjectVersion(meta.getProject().getVersion());
		SemanticMetadata[] semanticMetadata = new SemanticMetadata[meta.getSemanticMetadataCollection().size()];
		meta.getSemanticMetadataCollection().toArray(semanticMetadata);
		clazz.setSemanticMetadataCollection(semanticMetadata);
		
		Iterator attribMetaIter = meta.getUMLAttributeMetadataCollection().iterator();
		UMLAttribute[] attribs = new UMLAttribute[meta.getUMLAttributeMetadataCollection().size()];
		int i = 0;
		while (attribMetaIter.hasNext()) {
			UMLAttributeMetadata attribMeta = (UMLAttributeMetadata) attribMetaIter.next();
			attribs[i] = new UMLAttribute();
			attribs[i].setDescription(attribMeta.getDescription());
			attribs[i].setName(attribMeta.getName());
			SemanticMetadata[] sem = new SemanticMetadata[attribMeta.getSemanticMetadataCollection().size()];
			attribMeta.getSemanticMetadataCollection().toArray(sem);
			attribs[i].setSemanticMetadataCollection(sem);
		}
		clazz.setUmlAttributeCollection(attribs);
		
		return clazz;
	}
	
	
	public static UMLAssociation createUmlAssociation(UMLAssociationMetadata meta) {
		UMLAssociation assoc = new UMLAssociation();
		assoc.setBidirectional(meta.getIsBidirectional().booleanValue());
		
		// create target association edge
		UMLAssociationEdge targetEdge = new UMLAssociationEdge();
		targetEdge.setMaxCardinality(meta.getTargetHighCardinality().intValue());
		targetEdge.setMinCardinality(meta.getTargetLowCardinality().intValue());
		targetEdge.setRoleName(meta.getTargetRoleName());
		targetEdge.setUmlClass(new UMLAssociationEdgeUmlClass(
			createUmlClass(meta.getTargetUMLClassMetadata())));
		assoc.setTargetUMLAssociationEdge(new UMLAssociationTargetUMLAssociationEdge(targetEdge));
		
		// create source association edge
		UMLAssociationEdge sourceEdge = new UMLAssociationEdge();
		sourceEdge.setMaxCardinality(meta.getSourceHighCardinality().intValue());
		sourceEdge.setMinCardinality(meta.getSourceLowCardinality().intValue());
		sourceEdge.setRoleName(meta.getSourceRoleName());
		sourceEdge.setUmlClass(new UMLAssociationEdgeUmlClass(
			createUmlClass(meta.getSourceUMLClassMetadata())));
		assoc.setSourceUMLAssociationEdge(new UMLAssociationSourceUMLAssociationEdge(sourceEdge));
		
		return assoc;
	}
	
	
	public static DomainModel createDomainModel(UMLPackageMetadata pkg) {
		DomainModel model = new DomainModel();
		model.setProjectDescription(pkg.getProject().getDescription());
		model.setProjectLongName(pkg.getProject().getLongName());
		model.setProjectShortName(pkg.getProject().getShortName());
		model.setProjectVersion(pkg.getProject().getVersion());
		return model;
	}
	
	
	public static void setExposedClasses(DomainModel model, UMLPackageMetadata pack, String[] classNames) {
		Map umlClasses = umlClassMetadataByName(pack);
		UMLClass[] exposedClasses = new UMLClass[classNames.length];
		Set associationEdges = new HashSet();
		for (int i = 0; i < classNames.length; i++) {
			UMLClassMetadata umlClassMd = (UMLClassMetadata) umlClasses.get(classNames[i]);
			UMLClass umlClass = createUmlClass(umlClassMd);
			exposedClasses[i] = umlClass;
			Iterator assocMdIter = umlClassMd.getUMLAssociationMetadataCollection().iterator();
			while (assocMdIter.hasNext()) {
				UMLAssociationMetadata assocMd = (UMLAssociationMetadata) assocMdIter.next();
				UMLAssociation association = createUmlAssociation(assocMd);
				associationEdges.add(association);
			}
		}
		model.setExposedUMLClassCollection(
			new DomainModelExposedUMLClassCollection(exposedClasses));
		UMLAssociation[] associations = new UMLAssociation[associationEdges.size()];
		associationEdges.toArray(associations);
		model.setExposedUMLAssociationCollection(
			new DomainModelExposedUMLAssociationCollection(associations));
	}
	
	
	private static Map umlClassMetadataByName(UMLPackageMetadata pack) {
		Map classMd = new HashMap();
		Iterator classMdIter = pack.getUMLClassMetadataCollection().iterator();
		while (classMdIter.hasNext()) {
			UMLClassMetadata md = (UMLClassMetadata) classMdIter.next();
			classMd.put(md.getName(), md);
		}
		return classMd;
	}
}
