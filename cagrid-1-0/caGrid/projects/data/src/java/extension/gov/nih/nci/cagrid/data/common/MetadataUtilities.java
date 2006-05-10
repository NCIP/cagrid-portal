package gov.nih.nci.cagrid.data.common;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.SemanticMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLAssociationMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
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

import java.rmi.RemoteException;
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
	
	private static CaDSRServiceClient cadsrClient = null;
	
	public static CaDSRServiceClient getCadsrClient() {
		return cadsrClient;
	}
	
	
	public static void setCadsrClient(CaDSRServiceClient client) {
		cadsrClient = client;
	}
	

	public static UMLClass createUmlClass(UMLClassMetadata classMd, UMLPackageMetadata pack) {
		UMLClass clazz = new UMLClass();
		clazz.set_package(pack.getName());
		clazz.setClassname(classMd.getName());
		clazz.setDescription(classMd.getDescription());
		clazz.setProjectName(classMd.getProject().getLongName());
		clazz.setProjectVersion(classMd.getProject().getVersion());
		SemanticMetadata[] semanticMetadata = new SemanticMetadata[classMd.getSemanticMetadataCollection().size()];
		classMd.getSemanticMetadataCollection().toArray(semanticMetadata);
		clazz.setSemanticMetadataCollection(semanticMetadata);
		
		Iterator attribMetaIter = classMd.getUMLAttributeMetadataCollection().iterator();
		UMLAttribute[] attribs = new UMLAttribute[classMd.getUMLAttributeMetadataCollection().size()];
		int i = 0;
		while (attribMetaIter.hasNext()) {
			UMLAttributeMetadata attribMeta = (UMLAttributeMetadata) attribMetaIter.next();
			attribs[i] = new UMLAttribute();
			attribs[i].setDescription(attribMeta.getDescription());
			attribs[i].setName(attribMeta.getName());
			SemanticMetadata[] sem = new SemanticMetadata[attribMeta.getSemanticMetadataCollection().size()];
			attribMeta.getSemanticMetadataCollection().toArray(sem);
			attribs[i].setSemanticMetadataCollection(sem);
			i++;
		}
		clazz.setUmlAttributeCollection(attribs);
		
		return clazz;
	}
	
	
	public static UMLAssociation createUmlAssociation(UMLAssociationMetadata meta, UMLPackageMetadata pack) {
		UMLAssociation assoc = new UMLAssociation();
		assoc.setBidirectional(meta.getIsBidirectional().booleanValue());
		
		// create target association edge
		UMLAssociationEdge targetEdge = new UMLAssociationEdge();
		targetEdge.setMaxCardinality(meta.getTargetHighCardinality().intValue());
		targetEdge.setMinCardinality(meta.getTargetLowCardinality().intValue());
		targetEdge.setRoleName(meta.getTargetRoleName());
		targetEdge.setUmlClass(new UMLAssociationEdgeUmlClass(
			createUmlClass(meta.getTargetUMLClassMetadata(), pack)));
		assoc.setTargetUMLAssociationEdge(new UMLAssociationTargetUMLAssociationEdge(targetEdge));
		
		// create source association edge
		UMLAssociationEdge sourceEdge = new UMLAssociationEdge();
		sourceEdge.setMaxCardinality(meta.getSourceHighCardinality().intValue());
		sourceEdge.setMinCardinality(meta.getSourceLowCardinality().intValue());
		sourceEdge.setRoleName(meta.getSourceRoleName());
		sourceEdge.setUmlClass(new UMLAssociationEdgeUmlClass(
			createUmlClass(meta.getSourceUMLClassMetadata(), pack)));
		assoc.setSourceUMLAssociationEdge(new UMLAssociationSourceUMLAssociationEdge(sourceEdge));
		
		return assoc;
	}
	
	
	public static DomainModel createDomainModel(Project proj) {
		DomainModel model = new DomainModel();
		model.setProjectDescription(proj.getDescription());
		model.setProjectLongName(proj.getLongName());
		model.setProjectShortName(proj.getShortName());
		model.setProjectVersion(proj.getVersion());
		return model;
	}
	
	
	public static void setExposedClasses(DomainModel model, Project proj, UMLPackageMetadata pack, String[] classNames) throws RemoteException {
		Map umlClasses = umlClassMetadataByName(proj, pack);
		UMLClass[] exposedClasses = new UMLClass[classNames.length];
		Set associationEdges = new HashSet();
		for (int i = 0; i < classNames.length; i++) {
			UMLClassMetadata umlClassMd = (UMLClassMetadata) umlClasses.get(classNames[i]);
			UMLClass umlClass = createUmlClass(umlClassMd, pack);
			exposedClasses[i] = umlClass;
			Iterator assocMdIter = umlClassMd.getUMLAssociationMetadataCollection().iterator();
			while (assocMdIter.hasNext()) {
				UMLAssociationMetadata assocMd = (UMLAssociationMetadata) assocMdIter.next();
				UMLAssociation association = createUmlAssociation(assocMd, pack);
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
	
	
	private static Map umlClassMetadataByName(Project proj, UMLPackageMetadata pack) throws RemoteException {
		Map classMd = new HashMap();
		UMLClassMetadata[] mdArray = cadsrClient.findClassesInPackage(proj, pack.getName());
		for (int i = 0; i < mdArray.length; i++) {
			classMd.put(mdArray[i].getName(), mdArray[i]);
		}
		return classMd;
	}
}
