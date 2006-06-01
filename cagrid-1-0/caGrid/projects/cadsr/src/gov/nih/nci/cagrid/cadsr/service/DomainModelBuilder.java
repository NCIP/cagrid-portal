package gov.nih.nci.cagrid.cadsr.service;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLAssociationMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.cadsr.common.CaDSRUtils;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLAssociationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLClassCollection;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdgeUmlClass;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationSourceUMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationTargetUMLAssociationEdge;
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
 *  DomainModelBuilder
 *  Builds a DomainModel
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
	 * @param proj
	 * @return
	 */
	public DomainModel getDomainModel(Project proj) throws RemoteException {
		// find all packages in the project and hand off to getDomainModel(Package, String[]);
		cachePackages(proj);
		Map umlPackages = (Map) projectUmlPackages.get(proj);
		String[] packageNames = new String[umlPackages.keySet().size()];
		umlPackages.keySet().toArray(packageNames);
		return getDomainModel(proj, packageNames);
	}
	
	
	/**
	 * Gets a DomainModel that represents the project and packages
	 * @param proj
	 * 		The project to build a domain model for
	 * @param packageNames
	 * 		The names of packages to include in the domain model
	 * @return
	 * @throws RemoteException
	 */
	public DomainModel getDomainModel(Project proj, String[] packageNames) throws RemoteException {
		// grab the classes out of the packages
		List classes = new ArrayList();
		for (int i = 0; i < packageNames.length; i++) {
			UMLPackageMetadata pack = getPackageMetadata(proj, packageNames[i]);
			UMLClassMetadata[] mdArray = getClasses(pack);
			Collections.addAll(classes, mdArray);
		}
		UMLClassMetadata[] classArray = new UMLClassMetadata[classes.size()];
		classes.toArray(classArray);
		
		// grab associations for each class
		List associations = new ArrayList();
		for (int i = 0; i < classArray.length; i++) {
			UMLClassMetadata clazz = classArray[i];
			UMLAssociationMetadata[] mdArray = getAssociations(clazz);
			Collections.addAll(associations, mdArray);
		}
		UMLAssociationMetadata[] associationArray = new UMLAssociationMetadata[associations.size()];
		associations.toArray(associationArray);
		
		// hand off
		return getDomainModel(proj, classArray, associationArray);
	}
	
	
	/**
	 * Generates a DomainModel that represents the project and the given subset of 
	 * classes and associations
	 * @param proj
	 * 		The project to build a domain model for
	 * @param classes
	 * 		The classes to include in the domain model
	 * @param associations
	 * 		The asociations to include in the domain model
	 * @return
	 * @throws RemoteException
	 */
	public DomainModel getDomainModel(Project proj, UMLClassMetadata[] classes, UMLAssociationMetadata[] associations) throws RemoteException {
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
		UMLAssociation[] umlAssociations = new UMLAssociation[associations.length];
		for (int i = 0; i < associations.length; i++) {
			gov.nih.nci.cagrid.cadsr.domain.UMLAssociation cadsrAssociation = CaDSRUtils.convertAssociation(associations[i]);
			umlAssociations[i] = convertAssociation(cadsrAssociation);
		}
		exposedAssociations.setUMLAssociation(umlAssociations);
		model.setExposedUMLAssociationCollection(exposedAssociations);
		return model;
	}
	
	
	private UMLAssociation convertAssociation(gov.nih.nci.cagrid.cadsr.domain.UMLAssociation cadsrAssociation) {
		UMLAssociation converted = new UMLAssociation();
		converted.setBidirectional(cadsrAssociation.isIsBidirectional());
		UMLAssociationSourceUMLAssociationEdge convertedSourceEdge = new UMLAssociationSourceUMLAssociationEdge();
		UMLAssociationEdge sourceEdge = new UMLAssociationEdge();
		sourceEdge.setMaxCardinality(cadsrAssociation.getSourceMaxCardinality());
		sourceEdge.setMinCardinality(cadsrAssociation.getSourceMinCardinality());
		sourceEdge.setRoleName(cadsrAssociation.getSourceRoleName());
		sourceEdge.setUmlClass(new UMLAssociationEdgeUmlClass(CaDSRUtils.convertClass(
			cadsrAssociation.getSourceUMLClassMetadata().getUMLClassMetadata())));
		convertedSourceEdge.setUMLAssociationEdge(sourceEdge);
		converted.setSourceUMLAssociationEdge(convertedSourceEdge);
		UMLAssociationTargetUMLAssociationEdge convertedTargetEdge = new UMLAssociationTargetUMLAssociationEdge();
		UMLAssociationEdge targetEdge = new UMLAssociationEdge();
		targetEdge.setMaxCardinality(cadsrAssociation.getTargetMaxCardinality());
		targetEdge.setMinCardinality(cadsrAssociation.getTargetMinCardinality());
		targetEdge.setRoleName(cadsrAssociation.getTargetRoleName());
		targetEdge.setUmlClass(new UMLAssociationEdgeUmlClass(CaDSRUtils.convertClass(
			cadsrAssociation.getTargetUMLClassMetadata().getUMLClassMetadata())));
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
			projectUmlPackages.put(proj, umlPackages);
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
				LOG.debug("Added " + packs.size() + " packages to cache for project " + proj.getLongName());
			} catch (ApplicationException ex) {
				LOG.error("Error searching for packages: " + ex.getMessage());
				throw new RemoteException("Error searching for packages: " + ex.getMessage(), ex);
			}
		}
	}
	
	
	private UMLClassMetadata[] getClasses(UMLPackageMetadata pack) throws RemoteException {
		if (packageClasses == null) {
			packageClasses = new HashMap();
		}
		UMLClassMetadata[] classes = (UMLClassMetadata[]) packageClasses.get(pack);
		if (classes == null) {
			LOG.debug("Classes for package " + pack.getName() + " not yet cached");
			UMLClassMetadata classPrototype = new UMLClassMetadata();
			classPrototype.setUMLPackageMetadata(pack);
			classPrototype.setProject(pack.getProject());
			try {
				List classList = cadsr.search(UMLClassMetadata.class, classPrototype);
				classes = new UMLClassMetadata[classList.size()];
				for (int i = 0; i < classList.size(); i++) {
					classes[i] = (UMLClassMetadata) classList.get(i);
				}
				LOG.debug("Added " + classList.size() + " classes to cache for package " + pack.getName());
			} catch (ApplicationException ex) {
				LOG.error("Error searching for classes in package: " + pack.getName() + ": " + ex.getMessage());
				throw new RemoteException("Error searching for classes in package: " + pack.getName() + ": " + ex.getMessage(), ex);
			}
		}
		return classes;
	}
	
	
	private UMLAssociationMetadata[] getAssociations(UMLClassMetadata clazz) throws RemoteException {
		if (classAssociations == null) {
			classAssociations = new HashMap();
		}
		UMLAssociationMetadata[] associations = (UMLAssociationMetadata[]) classAssociations.get(clazz);
		if (associations == null) {
			LOG.debug("Associations for class " + clazz.getFullyQualifiedName() + " not yet cached");
			UMLAssociationMetadata associationPrototype = new UMLAssociationMetadata();
			associationPrototype.setSourceUMLClassMetadata(clazz);
			try {
				List associationList = cadsr.search(UMLAssociationMetadata.class, associationPrototype);
				associations = new UMLAssociationMetadata[associationList.size()];
				for (int i = 0; i < associationList.size(); i++) {
					associations[i] = (UMLAssociationMetadata) associationList.get(i);
				}
				LOG.debug("Added " + associationList.size() + " associations to cache for class " + clazz.getFullyQualifiedName());
			} catch (ApplicationException ex) {
				LOG.error("Error searching for associations on class " + clazz.getFullyQualifiedName() + ": " + ex.getMessage());
				throw new RemoteException("Error searching for associations on class " + clazz.getFullyQualifiedName() + ": " + ex.getMessage(), ex);
			}
		}
		return associations;
	}
}
