package gov.nih.nci.cagrid.cadsr.common;

import gov.nih.nci.cadsr.umlproject.domain.SemanticMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLAssociationMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cagrid.cadsr.domain.UMLAssociation;
import gov.nih.nci.cagrid.cadsr.domain.UMLAssociationSourceUMLClassMetadata;
import gov.nih.nci.cagrid.cadsr.domain.UMLAssociationTargetUMLClassMetadata;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.metadata.common.UMLAttributeSemanticMetadataCollection;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.common.UMLClassSemanticMetadataCollection;
import gov.nih.nci.cagrid.metadata.common.UMLClassUmlAttributeCollection;

import java.util.Collection;
import java.util.Iterator;


/**
 * @author oster
 * 
 */
public class CaDSRUtils {

	public static UMLAssociation convertAssociation(UMLAssociationMetadata association) {
		UMLAssociation converted = null;
		if (association != null) {
			converted = new UMLAssociation();
			converted.setId(association.getId());
			converted.setIsBidirectional(association.getIsBidirectional().booleanValue());
			converted.setSourceMaxCardinality(association.getSourceHighCardinality().intValue());
			converted.setSourceMinCardinality(association.getSourceLowCardinality().intValue());
			converted.setSourceRoleName(association.getSourceRoleName());
			converted.setTargetMaxCardinality(association.getTargetHighCardinality().intValue());
			converted.setTargetMinCardinality(association.getTargetLowCardinality().intValue());
			converted.setTargetRoleName(association.getTargetRoleName());

			UMLClassMetadata source = association.getSourceUMLClassMetadata();
			if (source != null) {
				UMLAssociationSourceUMLClassMetadata src = new UMLAssociationSourceUMLClassMetadata(source);
				converted.setSourceUMLClassMetadata(src);
			}

			UMLClassMetadata target = association.getTargetUMLClassMetadata();
			if (target != null) {
				UMLAssociationTargetUMLClassMetadata targ = new UMLAssociationTargetUMLClassMetadata(target);
				converted.setTargetUMLClassMetadata(targ);
			}
		}

		return converted;
	}
	
	
	public static UMLClass convertClass(UMLClassMetadata classMetadata) {
		UMLClass converted = null;
		if (classMetadata != null) {
			converted = new UMLClass();
			converted.setClassName(classMetadata.getFullyQualifiedName());
			converted.setDescription(classMetadata.getDescription());
			converted.setPackageName(classMetadata.getUMLPackageMetadata().getName());
			converted.setProjectName(classMetadata.getProject().getLongName());
			converted.setUmlAttributeCollection(convertAttributeCollection(classMetadata));
			SemanticMetadata[] smArray = convertSemanticMetadataCollection(
				classMetadata.getSemanticMetadataCollection()); 
			converted.setSemanticMetadataCollection(new UMLClassSemanticMetadataCollection(smArray));
		}
		return converted;
	}
	
	
	public static UMLClassUmlAttributeCollection convertAttributeCollection(UMLClassMetadata classMetadata) {
		UMLAttribute[] attribArray = new UMLAttribute[classMetadata.getUMLAssociationMetadataCollection().size()];
		int i = 0;
		Iterator attribIter = classMetadata.getUMLAttributeMetadataCollection().iterator();
		while (attribIter.hasNext()) {
			UMLAttributeMetadata attribMetadata = (UMLAttributeMetadata) attribIter.next();
			attribArray[i] = convertAttribute(attribMetadata);
			i++;
		}
		return new UMLClassUmlAttributeCollection(attribArray);
	}
	
	
	public static UMLAttribute convertAttribute(UMLAttributeMetadata attribMetadata) {
		UMLAttribute converted = null;
		if (attribMetadata != null) {
			converted = new UMLAttribute();
			converted.setDescription(attribMetadata.getDescription());
			converted.setName(attribMetadata.getName());
			SemanticMetadata[] smArray = convertSemanticMetadataCollection(attribMetadata.getSemanticMetadataCollection());
			converted.setSemanticMetadataCollection(new UMLAttributeSemanticMetadataCollection(smArray));
		}
		return converted;
	}
	
	
	public static SemanticMetadata[] convertSemanticMetadataCollection(Collection semanticMetadata) {
		SemanticMetadata[] smArray = new SemanticMetadata[semanticMetadata.size()];
		int i = 0;
		Iterator semanticIter = semanticMetadata.iterator();
		while (semanticIter.hasNext()) {
			smArray[i] = (SemanticMetadata) semanticIter.next();
			i++;
		}
		return smArray;
	}
}
