package gov.nih.nci.cagrid.data.codegen;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.SemanticMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLAssociationMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.cadsr.client.CaDSRServiceClient;
import gov.nih.nci.cagrid.data.common.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.metadata.common.UMLAttributeSemanticMetadataCollection;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.common.UMLClassSemanticMetadataCollection;
import gov.nih.nci.cagrid.metadata.common.UMLClassUmlAttributeCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLAssociationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLClassCollection;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdgeUmlClass;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationSourceUMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationTargetUMLAssociationEdge;

import java.io.FileWriter;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.globus.wsrf.encoding.ObjectSerializer;

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
	
	/**
	 * Initializes the metadata builder
	 * 
	 * @param cadsrUrl
	 * 		The URL at which the caDSR grid service can be found
	 * @param project
	 * 		The name of the project to create the domain model from
	 * @param pack
	 * 		The name of the package within the project to pull classes from
	 * @param targetClasses
	 * 		The class names available as query targets
	 */
	public MetadataBuilder(String cadsrUrl, String project, String pack, String[] targetClasses) {
		this.cadsrUrl = cadsrUrl;
		this.projectName = project;
		this.packageName = pack;
		this.classNames = targetClasses;
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
	
	
	public DomainModel getBigDomainModel() throws RemoteException, CodegenExtensionException {
		DomainModel model = new DomainModel();
		model.setProjectDescription(getProject().getDescription());
		model.setProjectLongName(getProject().getLongName());
		model.setProjectShortName(getProject().getShortName());
		model.setProjectVersion(getProject().getVersion());
		
		model.setExposedUMLClassCollection(getAllClassesCollection());
		
		model.setExposedUMLAssociationCollection(getExposedAssociationCollection());
		return model;		
	}
	
	
	/**
	 * Generates the exposed UML associations between classes.
	 * For now, this will just return ALL associations in the package
	 * @return
	 * @throws RemoteException
	 * @throws CodegenExtensionException
	 */
	private DomainModelExposedUMLAssociationCollection getExposedAssociationCollection() throws RemoteException, CodegenExtensionException {
		DomainModelExposedUMLAssociationCollection exposed = new DomainModelExposedUMLAssociationCollection();
		Set associations = new HashSet();
		/*
		for (int i = 0; i < classNames.length; i++) {
			Set classAssociations = getUmlAssociations(classNames[i]);
			associations.addAll(classAssociations);
		}
		*/
		UMLClassMetadata[] classes = getClassMetadata();
		for (int i =  0; i < classes.length; i++) {
			Set classAssociations = getUmlAssociations(classes[i]);
			associations.addAll(classAssociations);
		}
		UMLAssociation[] associationArray = new UMLAssociation[associations.size()];
		associations.toArray(associationArray);
		exposed.setUMLAssociation(associationArray);
		return exposed;
	}
	
	
	private Set getUmlAssociations(UMLClassMetadata classMd) throws RemoteException, CodegenExtensionException {
		Set associations = new HashSet();
		// association metadata
		Iterator assocMetadataIter = classMd.getUMLAssociationMetadataCollection().iterator();
		while (assocMetadataIter.hasNext()) {
			UMLAssociationMetadata assocMd = (UMLAssociationMetadata) assocMetadataIter.next();
			UMLAssociation assoc = new UMLAssociation();
			assoc.setBidirectional(assocMd.getIsBidirectional().booleanValue());
			assoc.setTargetUMLAssociationEdge(createTargetAssociationEdge(assocMd));
			assoc.setSourceUMLAssociationEdge(createSourceAssociationEdge(assocMd));
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
	
	
	private DomainModelExposedUMLClassCollection getAllClassesCollection() throws RemoteException, CodegenExtensionException {
		DomainModelExposedUMLClassCollection exposed = new DomainModelExposedUMLClassCollection();
		exposed.setUMLClass(getAllUmlClasses());
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
		umlClass.setPackageName(getPackageMetadata().getName());
		umlClass.setClassName(metadata.getFullyQualifiedName());
		umlClass.setDescription(metadata.getDescription());
		umlClass.setProjectName(getProject().getLongName());
		umlClass.setProjectVersion(getProject().getVersion());
		
		// semantic metadata
		SemanticMetadata[] sem = getSemanticMetadataArray(metadata.getSemanticMetadataCollection());
		umlClass.setSemanticMetadataCollection(new UMLClassSemanticMetadataCollection(sem));
		
		// UMLAttributes
		UMLAttribute[] attributes = getUmlAttributes(metadata);
		umlClass.setUmlAttributeCollection(new UMLClassUmlAttributeCollection(attributes));
		return umlClass;
	}
	
	
	private UMLClass[] getAllUmlClasses() throws RemoteException, CodegenExtensionException {
		UMLClassMetadata[] classMds = getClassMetadata();
		UMLClass[] classes = new UMLClass[classMds.length];
		for (int i = 0; i < classMds.length; i++) {
			UMLClassMetadata metadata = classMds[i];
			UMLClass umlClass = new UMLClass();
			umlClass.setPackageName(getPackageMetadata().getName());
			umlClass.setClassName(metadata.getFullyQualifiedName());
			umlClass.setDescription(metadata.getDescription());
			umlClass.setProjectName(getProject().getLongName());
			umlClass.setProjectVersion(getProject().getVersion());
			
			// semantic metadata
			SemanticMetadata[] sem = getSemanticMetadataArray(metadata.getSemanticMetadataCollection());
			umlClass.setSemanticMetadataCollection(new UMLClassSemanticMetadataCollection(sem));
			
			// UMLAttributes
			UMLAttribute[] attributes = getUmlAttributes(metadata);
			umlClass.setUmlAttributeCollection(new UMLClassUmlAttributeCollection(attributes));
			classes[i] = umlClass;
		}
		return classes;
	}
	
	
	private UMLAttribute[] getUmlAttributes(UMLClassMetadata metadata) throws RemoteException, CodegenExtensionException {
		Iterator attribMetadata = metadata.getUMLAttributeMetadataCollection().iterator();
		UMLAttribute[] attributes = new UMLAttribute[metadata.getUMLAttributeMetadataCollection().size()];
		int i = 0;
		while (attribMetadata.hasNext()) {
			UMLAttributeMetadata md = (UMLAttributeMetadata) attribMetadata.next();
			UMLAttribute attrib = new UMLAttribute();
			attrib.setDescription(md.getDescription());
			attrib.setName(md.getFullyQualifiedName());
			SemanticMetadata[] attrSemantic = getSemanticMetadataArray(md.getSemanticMetadataCollection());
			attrib.setSemanticMetadataCollection(new UMLAttributeSemanticMetadataCollection(attrSemantic));
			attributes[i] = attrib;
			i++;
		}
		return attributes;
	}
	
	
	private SemanticMetadata[] getSemanticMetadataArray(Collection semanticMetadataCollection) {
		SemanticMetadata[] array = new SemanticMetadata[semanticMetadataCollection.size()];
		Iterator semIter = semanticMetadataCollection.iterator();
		int i = 0;
		while (semIter.hasNext()) {
			array[i] = (SemanticMetadata) semIter.next();
			i++;
		}
		return array;
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
			// semantic metadata on each class
			for (int i = 0; i < classMetadata.length; i++) {
				SemanticMetadata[] classSemMd = getCadsrClient().findSemanticMetadataForClass(getProject(), classMetadata[i]);
				List semanticList = toArray(classSemMd);
				classMetadata[i].setSemanticMetadataCollection(semanticList);
			}
			// attributes
			for (int i = 0; i < classMetadata.length; i++) {
				UMLAttributeMetadata[] metadataArray = getCadsrClient().findAttributesInClass(getProject(), classMetadata[i]);
				List attribList = toArray(metadataArray);
				classMetadata[i].setUMLAttributeMetadataCollection(attribList);
			}
			// TODO: associations
		}
		return classMetadata;
	}
	
	
	private List toArray(Object[] objs) {
		List l = new ArrayList(objs.length);
		for (int i = 0; i < objs.length; i++) {
			l.add(objs[i]);
		}
		return l;
	}
	
	
	public static void main(String[] args) {
		String url = args[0];
		String proj = args[1];
		String pack = args[2];
		String[] targets = {"this will be ignored anyway"};
		
		MetadataBuilder builder = new MetadataBuilder(url, proj, pack, targets);
		try {
			DomainModel model = builder.getBigDomainModel();
			FileWriter objectWriter = new FileWriter("domainModel.xml");
			ObjectSerializer.serialize(objectWriter, model, DataServiceConstants.DOMAIN_MODEL_QNAME);
			objectWriter.flush();
			objectWriter.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
