package gov.nih.nci.cagrid.cadsr.common;

import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.SemanticMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLAssociationMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
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
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationSourceUMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationTargetUMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.impl.work.WorkManagerImpl;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import commonj.work.Work;
import commonj.work.WorkManager;


/**
 * DomainModelBuilder Builds a DomainModel
 * 
 * TODO: attempt to handle remote connection refusals by sleeping for a few
 * seconds and retrying?
 * 
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster</A>
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>
 * 
 * @created Jun 1, 2006
 * @version $Id$
 */
public class DomainModelBuilder {
	private static final int DEFAULT_POOL_SIZE = 5;

	protected static Log LOG = LogFactory.getLog(DomainModelBuilder.class.getName());

	private ApplicationService cadsr = null;
	private WorkManager workManager = null;


	public DomainModelBuilder(ApplicationService cadsr) {
		this.cadsr = cadsr;
	}


	/**
	 * Gets a DomainModel that represents the entire project
	 * 
	 * @param proj
	 * @return
	 */
	public DomainModel createDomainModel(Project proj) throws DomainModelGenerationException {
		// get all classes in project, preloading attributes, semantic
		// metadata (current hibernate we are using doesn't process more
		// than 1 preload, but this should work when we move forward)
		DetachedCriteria criteria = DetachedCriteria.forClass(UMLClassMetadata.class).createAlias(
			"UMLAttributeMetadataCollection", "atts").setFetchMode("atts", FetchMode.JOIN).setFetchMode(
			"atts.semanticMetadataCollection", FetchMode.JOIN).setFetchMode("atts.semanticMetadataCollection",
			FetchMode.JOIN).setFetchMode("semanticMetadataCollection", FetchMode.JOIN).setResultTransformer(
			CriteriaSpecification.DISTINCT_ROOT_ENTITY).createCriteria("project").add(
			Restrictions.eq("id", proj.getId()));
		UMLClassMetadata classArr[];
		try {
			classArr = getProjectClasses(proj, criteria);
		} catch (ApplicationException e) {
			throw new DomainModelGenerationException("Problem getting project's classes.", e);
		}

		// get all associations in project, preloading source and target classes
		criteria = DetachedCriteria.forClass(UMLAssociationMetadata.class).setFetchMode("sourceUMLClassMetadata",
			FetchMode.JOIN).setFetchMode("targetUMLClassMetadata", FetchMode.JOIN).setResultTransformer(
			CriteriaSpecification.DISTINCT_ROOT_ENTITY).createCriteria("project").add(
			Restrictions.eq("id", proj.getId()));
		UMLAssociationMetadata[] assocArr;
		try {
			assocArr = getProjectAssociations(proj, criteria);
		} catch (ApplicationException e) {
			throw new DomainModelGenerationException("Problem getting project's associations.", e);
		}

		return createDomainModel(proj, classArr, assocArr);

	}


	/**
	 * @param proj
	 * @param criteria
	 * @return
	 * @throws ApplicationException
	 */
	private UMLAssociationMetadata[] getProjectAssociations(Project proj, DetachedCriteria criteria)
		throws ApplicationException {
		long start = System.currentTimeMillis();
		List rList = this.cadsr.query(criteria, UMLAssociationMetadata.class.getName());
		UMLAssociationMetadata assocArr[] = new UMLAssociationMetadata[rList.size()];
		// caCORE's toArray(arr) is broken (cacore bug #1382), so need to do
		// this way
		System.arraycopy(rList.toArray(), 0, assocArr, 0, rList.size());

		double duration = (System.currentTimeMillis() - start) / 1000.0;
		LOG.info(proj.getShortName() + "'s association fetch took " + duration + " seconds, and found "
			+ assocArr.length + " associations.");

		return assocArr;
	}


	/**
	 * @return
	 * @throws ApplicationException
	 */
	private UMLClassMetadata[] getProjectClasses(Project proj, DetachedCriteria classCriteria)
		throws ApplicationException {

		long start = System.currentTimeMillis();
		List rList = this.cadsr.query(classCriteria, UMLClassMetadata.class.getName());
		UMLClassMetadata classArr[] = new UMLClassMetadata[rList.size()];
		// caCORE's toArray(arr) is broken (cacore bug #1382), so need to do
		// this way
		System.arraycopy(rList.toArray(), 0, classArr, 0, rList.size());

		double duration = (System.currentTimeMillis() - start) / 1000.0;
		LOG.info(proj.getShortName() + "'s class fetch took " + duration + " seconds, and found " + classArr.length
			+ " classes.");

		return classArr;
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
	public DomainModel createDomainModel(Project proj, String[] packageNames) throws DomainModelGenerationException {
		UMLClassMetadata classArr[] = null;
		UMLAssociationMetadata[] assocArr = null;

		if (packageNames != null && packageNames.length > 0) {
			// build up the OR for all the package names
			Disjunction disjunction = Restrictions.disjunction();
			for (int i = 0; i < packageNames.length; i++) {
				disjunction.add(Restrictions.eq("pack.name", packageNames[i]));
			}

			// get all classes in project (where fqn like packageNames[0].% or
			// packageNames[1].% ....), preloading attributes, semantic metadata
			DetachedCriteria criteria = DetachedCriteria.forClass(UMLClassMetadata.class).createAlias(
				"UMLAttributeMetadataCollection", "atts").createAlias("UMLPackageMetadata", "pack").setFetchMode(
				"atts", FetchMode.JOIN).setFetchMode("atts.semanticMetadataCollection", FetchMode.JOIN).setFetchMode(
				"atts.semanticMetadataCollection", FetchMode.JOIN).setFetchMode("semanticMetadataCollection",
				FetchMode.JOIN).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).add(disjunction)
				.createCriteria("project").add(Restrictions.eq("id", proj.getId()));

			try {
				classArr = getProjectClasses(proj, criteria);
			} catch (ApplicationException e) {
				throw new DomainModelGenerationException("Problem getting project's classes.", e);
			}

			// get all associations in project (where source class fqn like
			// packageNames[0].% or packageNames[1].% ....), preloading source
			// and target classes

			criteria = DetachedCriteria.forClass(UMLAssociationMetadata.class).createAlias("sourceUMLClassMetadata",
				"src").createAlias("src.UMLPackageMetadata", "pack").createAlias("targetUMLClassMetadata", "targ")
				.setFetchMode("src", FetchMode.JOIN).setFetchMode("targ", FetchMode.JOIN).setResultTransformer(
					CriteriaSpecification.DISTINCT_ROOT_ENTITY).add(disjunction).createCriteria("project").add(
					Restrictions.eq("id", proj.getId()));
			try {
				assocArr = getProjectAssociations(proj, criteria);
			} catch (ApplicationException e) {
				throw new DomainModelGenerationException("Problem getting project's associations.", e);
			}

		}
		return createDomainModel(proj, classArr, assocArr);
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
	 * @throws DomainModelGenerationException
	 * @throws RemoteException
	 */
	public DomainModel createDomainModel(final Project proj, UMLClassMetadata[] classes,
		UMLAssociationMetadata[] associations) throws DomainModelGenerationException {

		final DomainModel model = new DomainModel();
		// project
		model.setProjectDescription(proj.getDescription());
		model.setProjectLongName(proj.getLongName());
		model.setProjectShortName(proj.getShortName());
		model.setProjectVersion(proj.getVersion());

		long start = System.currentTimeMillis();
		LOG.info("Beginning processing of classes for project: " + proj.getShortName());
		// classes
		DomainModelExposedUMLClassCollection exposedClasses = new DomainModelExposedUMLClassCollection();
		if (classes != null) {
			List workList = new ArrayList();

			ClassConversionWork[] workers = new ClassConversionWork[classes.length];
			for (int i = 0; i < classes.length; i++) {
				final UMLClassMetadata classMD = classes[i];
				ClassConversionWork work = new ClassConversionWork() {
					public void run() {
						try {
							setUmlClass(convertClass(proj.getShortName(), proj.getVersion(), classMD));
						} catch (ApplicationException e) {
							LOG.error("Error converting class:" + classMD.getFullyQualifiedName(), e);
						}
					}
				};
				workers[i] = work;
				try {
					workList.add(getWorkManager().schedule(work));
				} catch (Exception e) {
					LOG.error("Error sheduling class conversion work", e);
					throw new DomainModelGenerationException("Error sheduling class conversion work", e);
				}
			}
			// wait for work item's to complete
			getWorkManager().waitForAll(workList, WorkManager.INDEFINITE);
			// now that they are done, access all the data from the workers
			UMLClass[] umlClasses = new UMLClass[classes.length];
			for (int i = 0; i < workers.length; i++) {
				ClassConversionWork work = workers[i];
				umlClasses[i] = work.getUmlClass();
				if (umlClasses[i] == null) {
					throw new DomainModelGenerationException("Class converter returned null data!");
				}

			}
			exposedClasses.setUMLClass(umlClasses);
		} else {
			LOG.debug("Class array was null.");
		}
		model.setExposedUMLClassCollection(exposedClasses);
		double duration = (System.currentTimeMillis() - start) / 1000.0;
		LOG.info("Finished class conversion for project: " + proj.getShortName() + " in " + duration + " seconds.");

		start = System.currentTimeMillis();
		LOG.info("Beginning processing of associations for project: " + proj.getShortName());
		// associations
		DomainModelExposedUMLAssociationCollection exposedAssociations = new DomainModelExposedUMLAssociationCollection();
		if (associations != null) {

			List workList = new ArrayList();

			AssociationConversionWork[] workers = new AssociationConversionWork[associations.length];
			for (int i = 0; i < associations.length; i++) {
				final UMLAssociationMetadata assocMD = associations[i];
				AssociationConversionWork work = new AssociationConversionWork() {
					public void run() {
						setUMLAssociation(convertAssociation(assocMD));
					}
				};
				workers[i] = work;
				try {
					workList.add(getWorkManager().schedule(work));
				} catch (Exception e) {
					LOG.error("Error sheduling class conversion work", e);
					throw new DomainModelGenerationException("Error sheduling class conversion work", e);
				}
			}
			// wait for work item's to complete
			getWorkManager().waitForAll(workList, WorkManager.INDEFINITE);
			// now that they are done, access all the data from the workers
			gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation[] umlAssociations = new gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation[associations.length];
			for (int i = 0; i < workers.length; i++) {
				AssociationConversionWork work = workers[i];
				umlAssociations[i] = work.getUMLAssociation();
				if (umlAssociations[i] == null) {
					throw new DomainModelGenerationException("Association converter returned null data!");
				}
			}
			exposedAssociations.setUMLAssociation(umlAssociations);
		} else {
			LOG.debug("Association array was null.");
		}
		model.setExposedUMLAssociationCollection(exposedAssociations);

		duration = (System.currentTimeMillis() - start) / 1000.0;
		LOG.info("Finished association conversion for project: " + proj.getShortName() + " in " + duration
			+ " seconds.");

		return model;
	}


	private gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation convertAssociation(
		UMLAssociationMetadata coreAssociation) {
		LOG.debug("Converting association:" + coreAssociation.getSourceRoleName() + " -> "
			+ coreAssociation.getTargetRoleName());

		gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation converted = new gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation();
		converted.setBidirectional(coreAssociation.getIsBidirectional().booleanValue());

		// process the source
		UMLAssociationSourceUMLAssociationEdge convertedSourceEdge = new UMLAssociationSourceUMLAssociationEdge();
		UMLAssociationEdge sourceEdge = new UMLAssociationEdge();
		sourceEdge.setMaxCardinality(coreAssociation.getSourceHighCardinality().intValue());
		sourceEdge.setMinCardinality(coreAssociation.getSourceLowCardinality().intValue());
		sourceEdge.setRoleName(coreAssociation.getSourceRoleName());
		if (sourceEdge.getRoleName() == null) {
			sourceEdge.setRoleName("");
		}
		sourceEdge.setUMLClassReference(new UMLClassReference(coreAssociation.getSourceUMLClassMetadata().getId()));
		convertedSourceEdge.setUMLAssociationEdge(sourceEdge);
		converted.setSourceUMLAssociationEdge(convertedSourceEdge);

		// process the target
		UMLAssociationTargetUMLAssociationEdge convertedTargetEdge = new UMLAssociationTargetUMLAssociationEdge();
		UMLAssociationEdge targetEdge = new UMLAssociationEdge();
		targetEdge.setMaxCardinality(coreAssociation.getTargetHighCardinality().intValue());
		targetEdge.setMinCardinality(coreAssociation.getTargetLowCardinality().intValue());
		targetEdge.setRoleName(coreAssociation.getTargetRoleName());
		if (targetEdge.getRoleName() == null) {
			targetEdge.setRoleName("");
		}
		targetEdge.setUMLClassReference(new UMLClassReference(coreAssociation.getTargetUMLClassMetadata().getId()));
		convertedTargetEdge.setUMLAssociationEdge(targetEdge);
		converted.setTargetUMLAssociationEdge(convertedTargetEdge);

		return converted;
	}


	private UMLClass convertClass(String projectShortName, String projectVersion, UMLClassMetadata classMetadata)
		throws ApplicationException {
		UMLClass converted = null;
		if (classMetadata != null) {
			converted = new UMLClass();
			converted.setClassName(classMetadata.getName());
			converted.setDescription(classMetadata.getDescription());
			if (converted.getDescription() == null) {
				converted.setDescription("");
			}
			converted.setId(classMetadata.getId());
			converted.setPackageName(CaDSRUtils.getPackageName(classMetadata));
			converted.setProjectName(projectShortName);
			converted.setProjectVersion(projectVersion);

			UMLAttribute[] attributes = createClassAttributes(classMetadata);
			UMLClassUmlAttributeCollection attCol = new UMLClassUmlAttributeCollection();
			attCol.setUMLAttribute(attributes);
			converted.setUmlAttributeCollection(attCol);

			SemanticMetadata[] smArray = semanticMetadataCollectionToArray(classMetadata
				.getSemanticMetadataCollection());
			converted.setSemanticMetadataCollection(new UMLClassSemanticMetadataCollection(smArray));
		}
		return converted;
	}


	private UMLAttribute[] createClassAttributes(UMLClassMetadata classMetadata) throws ApplicationException {

		long start = System.currentTimeMillis();
		DetachedCriteria criteria = DetachedCriteria.forClass(UMLAttributeMetadata.class).setFetchMode(
			"semanticMetadataCollection", FetchMode.JOIN).setResultTransformer(
			CriteriaSpecification.DISTINCT_ROOT_ENTITY).createCriteria("UMLClassMetadata").add(
			Restrictions.eq("id", classMetadata.getId()));

		List rList = cadsr.query(criteria, UMLAttributeMetadata.class.getName());

		UMLAttribute[] attArr = new UMLAttribute[rList.size()];
		int ind = 0;
		for (Iterator resultsIterator = rList.iterator(); resultsIterator.hasNext(); ind++) {
			long attstart = System.currentTimeMillis();
			UMLAttributeMetadata attMD = (UMLAttributeMetadata) resultsIterator.next();
			UMLAttribute converted = new UMLAttribute();
			converted.setDescription(attMD.getDescription());
			converted.setName(attMD.getName());
			// TODO: this makes 2 calls for every attribute... how can we speed
			// this up!?
			if (attMD.getDataElement() != null && attMD.getDataElement().getValueDomain() != null) {
				converted.setValueDomain(attMD.getDataElement().getValueDomain());
			}

			SemanticMetadata[] metadatas = semanticMetadataCollectionToArray(attMD.getSemanticMetadataCollection());
			UMLAttributeSemanticMetadataCollection semCol = new UMLAttributeSemanticMetadataCollection();
			semCol.setSemanticMetadata(metadatas);
			converted.setSemanticMetadataCollection(semCol);

			attArr[ind] = converted;
			LOG.debug("Converted attribute: " + attMD.getName() + " in " + (System.currentTimeMillis() - attstart)
				/ 1000.0 + " seconds.");

		}
		double duration = (System.currentTimeMillis() - start) / 1000.0;
		LOG.info(classMetadata.getFullyQualifiedName() + " attribute conversion took " + duration + " seconds.");

		return attArr;
	}


	private static SemanticMetadata[] semanticMetadataCollectionToArray(Collection semanticMetadata) {
		SemanticMetadata[] smArray = new SemanticMetadata[semanticMetadata.size()];

		// caCORE's toArray(arr) is broken (cacore bug #1382), so need to do
		// this way
		System.arraycopy(semanticMetadata.toArray(), 0, smArray, 0, semanticMetadata.size());

		return smArray;
	}


	public WorkManager getWorkManager() {
		if (this.workManager == null) {
			this.workManager = new WorkManagerImpl(DEFAULT_POOL_SIZE);
		}

		return this.workManager;
	}


	public void setWorkManager(WorkManager workManager) {
		this.workManager = workManager;
	}
}

abstract class ClassConversionWork implements Work {
	private UMLClass umlClass;


	public abstract void run();


	public UMLClass getUmlClass() {
		return this.umlClass;
	}


	public void setUmlClass(UMLClass umlClass) {
		this.umlClass = umlClass;
	}


	public boolean isDaemon() {
		return false;
	}


	public void release() {
		// Do nothing
	}
}

abstract class AssociationConversionWork implements Work {
	private UMLAssociation umlAssociation;


	public abstract void run();


	public UMLAssociation getUMLAssociation() {
		return this.umlAssociation;
	}


	public void setUMLAssociation(UMLAssociation umlAssociation) {
		this.umlAssociation = umlAssociation;
	}


	public boolean isDaemon() {
		return false;
	}


	public void release() {
		// Do nothing
	}
}
