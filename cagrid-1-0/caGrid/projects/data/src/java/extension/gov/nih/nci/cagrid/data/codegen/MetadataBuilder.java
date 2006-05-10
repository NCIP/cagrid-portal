package gov.nih.nci.cagrid.data.codegen;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.SemanticMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLAssociationMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** 
 *  MetadataBuilder
 *  Creates DomainModel metadata for a data service.  This requires access to the
 *  caDSR service.
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created May 10, 2006 
 * @version $Id$ 
 */
public class MetadataBuilder {

	// naming variables
	private String cadsrUrl;
	private String projectName;
	private String packageName;
	private String[] classNames;
	
	private CaDSRServiceClient cadsrClient = null;
	private Project project = null;
	private UMLPackageMetadata packageMetadata = null;
	private UMLClassMetadata[] classMetadata = null;
	
	public MetadataBuilder(String cadsrUrl, String project, String pack, String[] classes) {
		this.cadsrUrl = cadsrUrl;
		this.packageName = project;
		this.packageName = pack;
		this.classNames = classes;
	}
	
	
	public DomainModel getDomainModel() throws RemoteException, CodegenExtensionException {
		DomainModel model = new DomainModel();
		model.setProjectDescription(getProject().getDescription());
		model.setProjectLongName(getProject().getLongName());
		model.setProjectShortName(getProject().getShortName());
		model.setProjectVersion(getProject().getVersion());
		
		model.setExposedUMLClassCollection(getExposedClassCollection());
		
		model.setExposedUMLAssociationCollection(getExposedAssociationCollection());
		return model;
	}
	
	
	private DomainModelExposedUMLAssociationCollection getExposedAssociationCollection() throws RemoteException, CodegenExtensionException {
		DomainModelExposedUMLAssociationCollection exposed = new DomainModelExposedUMLAssociationCollection();
		Set associations = new HashSet();
		for (int i = 0; i < classNames.length; i++) {
			Set classAssociations = getUmlAssociations(classNames[i]);
			associations.addAll(classAssociations);
		}
		UMLAssociation[] associationArray = new UMLAssociation[associations.size()];
		associations.toArray(associationArray);
		exposed.setUMLAssociation(associationArray);
		return exposed;
	}
	
	
	private Set getUmlAssociations(String className) throws RemoteException, CodegenExtensionException {
		Set associations = new HashSet();
		// find the UMLClassMetadata needed
		UMLClassMetadata metadata = null;
		for (int i = 0; i < getClassMetadata().length; i++) {
			if (getClassMetadata()[i].getFullyQualifiedName().equals(className) || getClassMetadata()[i].getName().equals(className)) {
				metadata = getClassMetadata()[i];
				break;
			}
		}
		if (metadata == null) {
			throw new CodegenExtensionException("No class metadata found for association class " + className);
		}
		
		// association metadata
		UMLAssociationMetadata[] assocMetadata = new UMLAssociationMetadata[metadata.getUMLAssociationMetadataCollection().size()];
		metadata.getUMLAssociationMetadataCollection().toArray(assocMetadata);
		for (int i = 0; i < assocMetadata.length; i++) {
			UMLAssociation assoc = new UMLAssociation();
			assoc.setBidirectional(assocMetadata[i].getIsBidirectional().booleanValue());
			assoc.setTargetUMLAssociationEdge(createTargetAssociationEdge(assocMetadata[i]));
			assoc.setSourceUMLAssociationEdge(createSourceAssociationEdge(assocMetadata[i]));
			associations.add(assoc);
		}		
		return associations;
	}
	
	
	private UMLAssociationTargetUMLAssociationEdge createTargetAssociationEdge(UMLAssociationMetadata meta) throws RemoteException, CodegenExtensionException {
		UMLAssociationEdge edge = new UMLAssociationEdge();
		edge.setMaxCardinality(meta.getTargetHighCardinality().intValue());
		edge.setMinCardinality(meta.getTargetLowCardinality().intValue());
		edge.setRoleName(meta.getTargetRoleName());
		UMLClass targetClass = getUmlClass(meta.getTargetUMLClassMetadata().getFullyQualifiedName());
		if (targetClass == null) {
			targetClass = getUmlClass(meta.getTargetUMLClassMetadata().getName());
		}
		edge.setUmlClass(new UMLAssociationEdgeUmlClass(targetClass));
		return new UMLAssociationTargetUMLAssociationEdge(edge);
	}
	
	
	private UMLAssociationSourceUMLAssociationEdge createSourceAssociationEdge(UMLAssociationMetadata meta) throws RemoteException, CodegenExtensionException {
		UMLAssociationEdge edge = new UMLAssociationEdge();
		edge.setMaxCardinality(meta.getSourceHighCardinality().intValue());
		edge.setMaxCardinality(meta.getSourceLowCardinality().intValue());
		edge.setRoleName(meta.getSourceRoleName());
		UMLClass sourceClass = getUmlClass(meta.getSourceUMLClassMetadata().getFullyQualifiedName());
		if (sourceClass == null) {
			sourceClass = getUmlClass(meta.getSourceUMLClassMetadata().getName());
		}
		edge.setUmlClass(new UMLAssociationEdgeUmlClass(sourceClass));
		return new UMLAssociationSourceUMLAssociationEdge(edge);
	}
	
	
	private DomainModelExposedUMLClassCollection getExposedClassCollection() throws RemoteException, CodegenExtensionException {
		DomainModelExposedUMLClassCollection exposed = new DomainModelExposedUMLClassCollection();
		UMLClass[] classes = new UMLClass[classNames.length];
		for (int i = 0; i < classNames.length; i++) {
			UMLClass umlClass = getUmlClass(classNames[i]);
			classes[i] = umlClass;
		}
		exposed.setUMLClass(classes);
		return exposed;
	}
	
	
	private UMLClass getUmlClass(String className) throws RemoteException, CodegenExtensionException {
		UMLClassMetadata[] metadataArray = getClassMetadata();
		UMLClassMetadata metadata = null;
		for (int i = 0; i < metadataArray.length; i++) {
			if (metadataArray[i].getFullyQualifiedName().equals(className) || metadataArray[i].getName().equals(className)) {
				metadata = metadataArray[i];
				break;
			}
		}
		if (metadata == null) {
			throw new CodegenExtensionException("No class metadata found for class name " + className);
		}
		UMLClass umlClass = new UMLClass();
		umlClass.set_package(getPackageMetadata().getName());
		umlClass.setClassname(metadata.getFullyQualifiedName());
		umlClass.setDescription(metadata.getDescription());
		umlClass.setProjectName(getProject().getLongName());
		umlClass.setProjectVersion(getProject().getVersion());
		
		// semantic metadata
		SemanticMetadata[] sem = new SemanticMetadata[metadata.getSemanticMetadataCollection().size()];
		metadata.getSemanticMetadataCollection().toArray(sem);
		umlClass.setSemanticMetadataCollection(sem);
		
		// UMLAttributes
		UMLAttribute[] attributes = getUmlAttributes(metadata);
		umlClass.setUmlAttributeCollection(attributes);
		return umlClass;
	}
	
	
	private UMLAttribute[] getUmlAttributes(UMLClassMetadata metadata) {
		UMLAttribute[] attributes = new UMLAttribute[metadata.getUMLAttributeMetadataCollection().size()];
		Iterator attribMetaIter = metadata.getUMLAttributeMetadataCollection().iterator();
		int attrIndex = 0;
		while (attribMetaIter.hasNext()) {
			UMLAttributeMetadata attribMetadata = (UMLAttributeMetadata) attribMetaIter.next();
			UMLAttribute attrib = new UMLAttribute();
			attrib.setDescription(attribMetadata.getDescription());
			attrib.setName(attribMetadata.getFullyQualifiedName());
			
			// attribute semantic metadata
			SemanticMetadata[] attrSemantic = new SemanticMetadata[attribMetadata.getSemanticMetadataCollection().size()];
			attribMetadata.getSemanticMetadataCollection().toArray(attrSemantic);
			attrib.setSemanticMetadataCollection(attrSemantic);
			attributes[attrIndex] = attrib;
			attrIndex++;
		}
		return attributes;
	}
	
	
	private CaDSRServiceClient getCadsrClient() {
		if (cadsrClient == null) {
			cadsrClient = new CaDSRServiceClient(cadsrUrl);
		}
		return cadsrClient;
	}
	
	
	private Project getProject() throws RemoteException, CodegenExtensionException {
		if (project == null) {
			Project[] allProjects = getCadsrClient().findAllProjects();
			for (int i = 0; i < allProjects.length; i++) {
				if (allProjects[i].getLongName().equals(projectName)) {
					project = allProjects[i];
					return project;
				}
			}
			// at this point, all projects have been looked at, and none match the required name
			throw new CodegenExtensionException("No project " + projectName + " was found in the caDSR " + cadsrUrl);
		}
		return project;
	}
	
	
	private UMLPackageMetadata getPackageMetadata() throws RemoteException, CodegenExtensionException {
		if (packageMetadata == null) {
			UMLPackageMetadata projectPackages[] = getCadsrClient().findPackagesInProject(getProject());
			for (int i = 0; i < projectPackages.length; i++) {
				if (projectPackages[i].getName().equals(packageName)) {
					packageMetadata = projectPackages[i];
					return packageMetadata;
				}
			}
			// at this point, all packages have been checked, and none match the required name
			throw new CodegenExtensionException("No package " + packageName + " could be found in the project " + projectName + " on caDSR " + cadsrUrl);
		}
		return packageMetadata;
	}
	
	
	private UMLClassMetadata[] getClassMetadata() throws RemoteException, CodegenExtensionException {
		if (classMetadata == null) {
			classMetadata = getCadsrClient().findClassesInPackage(getProject(), getPackageMetadata().getName());
		}
		return classMetadata;
	}
}
