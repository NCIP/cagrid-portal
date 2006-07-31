package gov.nih.nci.cagrid.cadsr.service;

import gov.nih.nci.cadsr.domain.DataElement;
import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.SemanticMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLAssociationMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.cadsr.common.CaDSRUtils;
import gov.nih.nci.cagrid.cadsr.domain.UMLAssociation;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLAssociationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLClassCollection;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationSourceUMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationTargetUMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * DomainModelBuilder Builds a DomainModel
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Jun 1, 2006
 * @version $Id$
 */
public class DomainModelBuilder {
	protected static Log LOG = LogFactory.getLog(DomainModelBuilder.class.getName());

	// Project -> (packageName, UMLPackageMetadata)
	private static Map projectUmlPackages = null;
	// UMLPackageMetadata -> Set(UMLClassMetadata)
	private static Map packageClasses = null;
	// UMLClassMetadata -> Set(UMLAssociationMetadata)
	private static Map classAssociations = null;

	private ApplicationService cadsr = null;
	private Project mostRecentCompleteProject = null;

	public DomainModelBuilder(ApplicationService cadsr) {
		this.cadsr = cadsr;
	}


	/**
	 * Gets a DomainModel that represents the entire project
	 * 
	 * @param proj
	 * @return
	 */
	public DomainModel getDomainModel(Project proj) throws RemoteException {
		// find all packages in the project and hand off to
		// getDomainModel(Package, String[]);
		Project completeProject = findCompleteProject(proj);
		Map umlPackages = getPackagesFromProject(completeProject);
		String[] packageNames = new String[umlPackages.keySet().size()];
		umlPackages.keySet().toArray(packageNames);
		return getDomainModel(completeProject, packageNames);
	}


	/**
	 * Gets a DomainModel that represents the project and packages
	 * 
	 * @param proj
	 *            The project to build a domain model for
	 * @param packageNames
	 *            The names of packages to include in the domain model
	 * @return
	 * @throws RemoteException
	 */
	public DomainModel getDomainModel(Project proj, String[] packageNames) throws RemoteException {
		// find the complete project
		Project completeProject = findCompleteProject(proj);
		List classes = new ArrayList();
		List associations = new ArrayList();
		// grab the classes out of the packages
		if (packageNames != null) {
			for (int i = 0; i < packageNames.length; i++) {
				UMLPackageMetadata pack = getPackageMetadata(completeProject, packageNames[i]);
				UMLClassMetadata[] classMdArray = getClasses(completeProject, pack);
				Collections.addAll(classes, classMdArray);
				// grab associations for each class		
				for (int j = 0; j < classMdArray.length; j++) {
					UMLClassMetadata clazz = classMdArray[j];
					UMLAssociationMetadata[] assocMdArray = getAssociations(completeProject, clazz);
					for (int k = 0; k < assocMdArray.length; k++) {
						UMLAssociationMetadata assocMd = assocMdArray[k];
						// HACK: caDSR isn't returning the correct source class (target is OK).
						// HACK: Undo this 'fix' when it does return the correct thing.
						assocMd.setSourceUMLClassMetadata(clazz);
						UMLAssociation association = CaDSRUtils.convertAssociation(assocMd);
						associations.add(association);
					}					
				}
			}
		}
		// convert lists to arrays
		UMLClassMetadata[] classArray = new UMLClassMetadata[classes.size()];
		classes.toArray(classArray);
		
		UMLAssociation[] associationArray = new UMLAssociation[associations.size()];
		associations.toArray(associationArray);

		// hand off
		return getDomainModel(completeProject, classArray, associationArray);
	}


	/**
	 * Generates a DomainModel that represents the project and the given subset
	 * of classes and associations
	 * 
	 * @param proj
	 *            The project to build a domain model for
	 * @param classes
	 *            The classes to include in the domain model
	 * @param associations
	 *            The asociations to include in the domain model
	 * @return
	 * @throws RemoteException
	 */
	public DomainModel getDomainModel(Project proj, UMLClassMetadata[] classes, UMLAssociation[] associations)
		throws RemoteException {
		// find the proper project from the caDSR first
		Project completeProject = findCompleteProject(proj);
		
		DomainModel model = new DomainModel();
		// project
		model.setProjectDescription(completeProject.getDescription());
		model.setProjectLongName(completeProject.getLongName());
		model.setProjectShortName(completeProject.getShortName());
		model.setProjectVersion(completeProject.getVersion());

		// classes
		DomainModelExposedUMLClassCollection exposedClasses = new DomainModelExposedUMLClassCollection();
		if (classes != null) {
			UMLClass[] umlClasses = new UMLClass[classes.length];
			for (int i = 0; i < classes.length; i++) {
				umlClasses[i] = CaDSRUtils.convertClass(classes[i]);
			}
			exposedClasses.setUMLClass(umlClasses);
		}
		model.setExposedUMLClassCollection(exposedClasses);

		// associations
		DomainModelExposedUMLAssociationCollection exposedAssociations = 
			new DomainModelExposedUMLAssociationCollection();
		if (associations != null) {
			gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation[] umlAssociations = 
				new gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation[associations.length];
			for (int i = 0; i < associations.length; i++) {
				umlAssociations[i] = convertAssociation(associations[i]);
			}
			exposedAssociations.setUMLAssociation(umlAssociations);
		}
		model.setExposedUMLAssociationCollection(exposedAssociations);
		return model;
	}


	private gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation convertAssociation(UMLAssociation cadsrAssociation) {
		gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation converted = new gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation();
		converted.setBidirectional(cadsrAssociation.isIsBidirectional());
		UMLAssociationSourceUMLAssociationEdge convertedSourceEdge = new UMLAssociationSourceUMLAssociationEdge();
		UMLAssociationEdge sourceEdge = new UMLAssociationEdge();
		sourceEdge.setMaxCardinality(cadsrAssociation.getSourceMaxCardinality());
		sourceEdge.setMinCardinality(cadsrAssociation.getSourceMinCardinality());
		sourceEdge.setRoleName(cadsrAssociation.getSourceRoleName());
		if (sourceEdge.getRoleName() == null) {
			sourceEdge.setRoleName("");
		}
		sourceEdge.setUMLClassReference(new UMLClassReference(cadsrAssociation.getSourceUMLClassMetadata()
			.getUMLClassMetadata().getId()));
		convertedSourceEdge.setUMLAssociationEdge(sourceEdge);
		converted.setSourceUMLAssociationEdge(convertedSourceEdge);
		UMLAssociationTargetUMLAssociationEdge convertedTargetEdge = new UMLAssociationTargetUMLAssociationEdge();
		UMLAssociationEdge targetEdge = new UMLAssociationEdge();
		targetEdge.setMaxCardinality(cadsrAssociation.getTargetMaxCardinality());
		targetEdge.setMinCardinality(cadsrAssociation.getTargetMinCardinality());
		targetEdge.setRoleName(cadsrAssociation.getTargetRoleName());
		if (targetEdge.getRoleName() == null) {
			targetEdge.setRoleName("");
		}
		targetEdge.setUMLClassReference(new UMLClassReference(cadsrAssociation.getTargetUMLClassMetadata()
			.getUMLClassMetadata().getId()));
		convertedTargetEdge.setUMLAssociationEdge(targetEdge);
		converted.setTargetUMLAssociationEdge(convertedTargetEdge);
		return converted;
	}


	private UMLPackageMetadata getPackageMetadata(Project proj, String packageName) throws RemoteException {
		Map umlPackages = getPackagesFromProject(proj);
		UMLPackageMetadata pack = (UMLPackageMetadata) umlPackages.get(packageName);
		return pack;
	}


	private Map getPackagesFromProject(Project proj) throws RemoteException {
		if (projectUmlPackages == null) {
			projectUmlPackages = new HashMap();
		}
		Map umlPackages = (Map) projectUmlPackages.get(proj);
		if (umlPackages == null) {
			LOG.debug("Project " + proj.getLongName() + " not yet cached");
			// project not yet in the cache
			umlPackages = new HashMap();
			// get packages for project from cadsr application service
			UMLPackageMetadata packagePrototype = new UMLPackageMetadata();
			packagePrototype.setProject(proj);
			try {
				List packs = cadsr.search(UMLPackageMetadata.class, packagePrototype);
				Iterator packIter = packs.iterator();
				while (packIter.hasNext()) {
					UMLPackageMetadata pack = (UMLPackageMetadata) packIter.next();
					umlPackages.put(pack.getName(), pack);
				}
				// put the packages in the project -> package mapping cache
				projectUmlPackages.put(proj, umlPackages);
				LOG.debug("Added " + umlPackages.size() + " packages to cache for project " + proj.getLongName());
			} catch (ApplicationException ex) {
				LOG.error("Error searching for packages: " + ex.getMessage());
				throw new RemoteException("Error searching for packages: " + ex.getMessage(), ex);
			}
		}
		return umlPackages;
	}


	private UMLClassMetadata[] getClasses(Project proj, UMLPackageMetadata pack) throws RemoteException {
		if (packageClasses == null) {
			packageClasses = new HashMap();
		}
		UMLClassMetadata[] classes = (UMLClassMetadata[]) packageClasses.get(pack);
		if (classes == null) {
			LOG.debug("Classes for package " + pack.getName() + " not yet cached");
			UMLClassMetadata classPrototype = new UMLClassMetadata();
			classPrototype.setUMLPackageMetadata(pack);
			classPrototype.setProject(proj);
			try {
				Iterator classListIter = cadsr.search(UMLClassMetadata.class, classPrototype).iterator();
				List classList = new ArrayList();
				while (classListIter.hasNext()) {
					UMLClassMetadata metadata = (UMLClassMetadata) classListIter.next();
					// ensure the attributes and semantic metadata are brought back
					Iterator attribIter = metadata.getUMLAttributeMetadataCollection().iterator();
					while (attribIter.hasNext()) {
						UMLAttributeMetadata att = (UMLAttributeMetadata) attribIter.next();
						DataElement de = att.getDataElement();
						de.getContext();
						de.getDataElementConcept().getObjectClass();
						String typeName = de.getValueDomain().getDatatypeName();
						LOG.debug(typeName);
						if (att.getDescription() == null) {
							att.setDescription("");
						}
					}
					Iterator smIter = metadata.getSemanticMetadataCollection().iterator();
					while (smIter.hasNext()) {
						SemanticMetadata sm = (SemanticMetadata) smIter.next();
						LOG.debug(sm.getConceptCode());
						LOG.debug(sm.getConcept().getLongName());
					}
					classList.add(metadata);
				}
				classes = new UMLClassMetadata[classList.size()];
				classList.toArray(classes);
				// cache the classes array
				packageClasses.put(pack, classes);
				LOG.debug("Added " + classes.length + " classes to cache for package " + pack.getName());
			} catch (ApplicationException ex) {
				LOG.error("Error searching for classes in package: " + pack.getName() + ": " + ex.getMessage());
				throw new RemoteException("Error searching for classes in package: " + pack.getName() + ": "
					+ ex.getMessage(), ex);
			}
		}
		return classes;
	}


	private UMLAssociationMetadata[] getAssociations(Project proj, UMLClassMetadata clazz) throws RemoteException {
		if (classAssociations == null) {
			classAssociations = new HashMap();
		}
		UMLAssociationMetadata[] associations = (UMLAssociationMetadata[]) classAssociations.get(clazz);
		if (associations == null) {
			LOG.debug("Associations for class " + clazz.getFullyQualifiedName() + " not yet cached");
			UMLAssociationMetadata associationPrototype = new UMLAssociationMetadata();
			associationPrototype.setSourceUMLClassMetadata(clazz);
			associationPrototype.setProject(proj);
			try {
				Iterator associationListIter = cadsr.search(
					UMLAssociationMetadata.class, associationPrototype).iterator();
				List associationList = new ArrayList();
				while (associationListIter.hasNext()) {
					UMLAssociationMetadata metadata = (UMLAssociationMetadata) associationListIter.next();
					// force population of these fields
					metadata.getSourceUMLClassMetadata();
					metadata.getTargetUMLClassMetadata();
					associationList.add(metadata);
				}
				associations = new UMLAssociationMetadata[associationList.size()];
				associationList.toArray(associations);
				// cache the associations
				classAssociations.put(clazz, associations);
				LOG.debug("Added " + associations.length + " associations to cache for class "
					+ clazz.getFullyQualifiedName());
			} catch (ApplicationException ex) {
				LOG.error("Error searching for associations on class " + clazz.getFullyQualifiedName() + ": "
					+ ex.getMessage());
				throw new RemoteException("Error searching for associations on class " + clazz.getFullyQualifiedName()
					+ ": " + ex.getMessage(), ex);
			}
		}
		return associations;
	}
	
	
	private Project findCompleteProject(Project prototype) throws RemoteException {
		if (prototype != mostRecentCompleteProject) {
			List completeProjects = new ArrayList();
			Iterator projectIter = null;
			try {
				projectIter = cadsr.search(Project.class, prototype).iterator();
			} catch (ApplicationException ex) {
				throw new RemoteException("Error retrieving complete project: " + ex.getMessage(), ex);
			}
			// should be ONLY ONE project from the caDSR
			while (projectIter.hasNext()) {
				completeProjects.add(projectIter.next());
			}
			if (completeProjects.size() == 1) {
				mostRecentCompleteProject = (Project) completeProjects.get(0);
			} else if (completeProjects.size() == 0) {
				throw new RemoteException("No project found in caDSR");
			} else {
				throw new RemoteException("More than one project (" + completeProjects.size() 
					+ ") found.  Prototype project is ambiguous");
			}
		}
		return mostRecentCompleteProject;
	}
}
