package gov.nih.nci.cagrid.data.common;

import gov.nih.nci.cadsr.umlproject.domain.SemanticMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLAssociationMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdge;

import java.util.HashMap;
import java.util.HashSet;
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
		clazz.setSemanticMetadataCollection(meta.getSemanticMetadataCollection());
		
		UMLAttributeMetadata[] attribMeta = meta.getUMLAttributeMetadataCollection();
		UMLAttribute[] attribs = new UMLAttribute[attribMeta.length];
		for (int i = 0; i < attribMeta.length; i++) {
			attribs[i] = new UMLAttribute();
			attribs[i].setDescription(attribMeta[i].getDescription());
			attribs[i].setName(attribMeta[i].getName());
			SemanticMetadata[] sem = attribMeta[i].getSemanticMetadataCollection();
			attribs[i].setSemanticMetadataCollection(sem);
		}
		clazz.setUmlAttributeCollection(attribs);
		
		return clazz;
	}
	
	
	public static UMLAssociation createUmlAssociation(UMLAssociationMetadata meta) {
		UMLAssociation assoc = new UMLAssociation();
		assoc.setBidirectional(meta.isIsBidirectional());
		
		// create target association edge
		UMLAssociationEdge targetEdge = new UMLAssociationEdge();
		targetEdge.setMaxCardinality(meta.getTargetHighCardinality().intValue());
		targetEdge.setMinCardinality(meta.getTargetLowCardinality().intValue());
		targetEdge.setRoleName(meta.getTargetRoleName());
		targetEdge.setUmlClass(createUmlClass(meta.getTargetUMLClassMetadata()));
		assoc.setTargetUMLAssociationEdge(targetEdge);
		
		// create source association edge
		UMLAssociationEdge sourceEdge = new UMLAssociationEdge();
		sourceEdge.setMaxCardinality(meta.getSourceHighCardinality().intValue());
		sourceEdge.setMinCardinality(meta.getSourceLowCardinality().intValue());
		sourceEdge.setRoleName(meta.getSourceRoleName());
		sourceEdge.setUmlClass(createUmlClass(meta.getSourceUMLClassMetadata()));
		assoc.setSourceUMLAssociationEdge(sourceEdge);
		
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
			UMLAssociationMetadata[] assocMd = umlClassMd.getUMLAssociationMetadataCollection();
			for (int j = 0; j < assocMd.length; j++) {
				UMLAssociation association = createUmlAssociation(assocMd[j]);
				associationEdges.add(association);
			}
		}
		model.setExposedUMLClassCollection(exposedClasses);
		UMLAssociation[] associations = new UMLAssociation[associationEdges.size()];
		associationEdges.toArray(associations);
		model.setExposedUMLAssociationCollection(associations);
	}
	
	
	private static Map umlClassMetadataByName(UMLPackageMetadata pack) {
		Map classMd = new HashMap();
		UMLClassMetadata[] classMdArray = pack.getUMLClassMetadataCollection();
		for (int i = 0; i < classMdArray.length; i++) {
			classMd.put(classMdArray[i].getName(), classMdArray[i]);
		}
		return classMd;
	}
}
