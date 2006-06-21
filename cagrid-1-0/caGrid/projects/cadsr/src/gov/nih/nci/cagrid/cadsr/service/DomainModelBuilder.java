package gov.nih.nci.cagrid.cadsr.service;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.SemanticMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLAssociationMetadata;
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
		cachePackages(proj);
		Map umlPackages = (Map) projectUmlPackages.get(proj);
		String[] packageNames = new String[umlPackages.keySet().size()];
		umlPackages.keySet().toArray(packageNames);
		return getDomainModel(proj, packageNames);
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
		// grab the classes out of the packages
		List classes = new ArrayList();
		for (int i = 0; i < packageNames.length; i++) {
			UMLPackageMetadata pack = getPackageMetadata(proj, packageNames[i]);
			UMLClassMetadata[] mdArray = getClasses(proj, pack);
			Collections.addAll(classes, mdArray);
		}
		UMLClassMetadata[] classArray = new UMLClassMetadata[classes.size()];
		classes.toArray(classArray);

		// grab associations for each class
		List associationMetadataList = new ArrayList();
		for (int i = 0; i < classArray.length; i++) {
			UMLClassMetadata clazz = classArray[i];
			UMLAssociationMetadata[] mdArray = getAssociations(proj, clazz);
			Collections.addAll(associationMetadataList, mdArray);
		}
		UMLAssociation[] associationArray = new UMLAssociation[associationMetadataList.size()];
		for (int i = 0; i < associationMetadataList.size(); i++) {
			UMLAssociationMetadata assocMd = (UMLAssociationMetadata) associationMetadataList.get(i);
			associationArray[i] = CaDSRUtils.convertAssociation(assocMd);
		}

		// hand off
		return getDomainModel(proj, classArray, associationArray);
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
		DomainModel model = new DomainModel();
		// project
		model.setProjectDescription(proj.getDescription());
		model.setProjectLongName(proj.getLongName());
		model.setProjectShortName(proj.getShortName());
		model.setProjectVersion(proj.getVersion());

		// classes
		DomainModelExposedUMLClassCollection exposedClasses = new DomainModelExposedUMLClassCollection();
		UMLClass[] umlClasses = new UMLClass[classes.length];
		for (int i = 0; i < classes.length; i++) {
			umlClasses[i] = CaDSRUtils.convertClass(classes[i]);
		}
		exposedClasses.setUMLClass(umlClasses);
		model.setExposedUMLClassCollection(exposedClasses);

		// associations
		DomainModelExposedUMLAssociationCollection exposedAssociations = new DomainModelExposedUMLAssociationCollection();
		gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation[] umlAssociations = new gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation[associations.length];
		for (int i = 0; i < associations.length; i++) {
			umlAssociations[i] = convertAssociation(associations[i]);
		}
		exposedAssociations.setUMLAssociation(umlAssociations);
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
		sourceEdge.setUMLClassReference(new UMLClassReference(cadsrAssociation.getSourceUMLClassMetadata()
			.getUMLClassMetadata().getId()));
		convertedSourceEdge.setUMLAssociationEdge(sourceEdge);
		converted.setSourceUMLAssociationEdge(convertedSourceEdge);
		UMLAssociationTargetUMLAssociationEdge convertedTargetEdge = new UMLAssociationTargetUMLAssociationEdge();
		UMLAssociationEdge targetEdge = new UMLAssociationEdge();
		targetEdge.setMaxCardinality(cadsrAssociation.getTargetMaxCardinality());
		targetEdge.setMinCardinality(cadsrAssociation.getTargetMinCardinality());
		targetEdge.setRoleName(cadsrAssociation.getTargetRoleName());
		targetEdge.setUMLClassReference(new UMLClassReference(cadsrAssociation.getTargetUMLClassMetadata()
			.getUMLClassMetadata().getId()));
		convertedTargetEdge.setUMLAssociationEdge(targetEdge);
		converted.setTargetUMLAssociationEdge(convertedTargetEdge);
		return converted;
	}


	private UMLPackageMetadata getPackageMetadata(Project proj, String packageName) throws RemoteException {
		cachePackages(proj);
		Map umlPackages = (Map) projectUmlPackages.get(proj);
		UMLPackageMetadata pack = (UMLPackageMetadata) umlPackages.get(packageName);
		return pack;
	}


	private void cachePackages(Project proj) throws RemoteException {
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
					// ensure the attributes and semantic metadata are brought
					// back
					Iterator attribIter = metadata.getUMLAttributeMetadataCollection().iterator();
					while (attribIter.hasNext()) {
						attribIter.next();
					}
					Iterator smIter = metadata.getSemanticMetadataCollection().iterator();
					while (smIter.hasNext()) {
						SemanticMetadata sm = (SemanticMetadata) smIter.next();
						LOG.debug(sm.getConceptCode());
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
				Iterator associationListIter = cadsr.search(UMLAssociationMetadata.class, associationPrototype)
					.iterator();
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
}
