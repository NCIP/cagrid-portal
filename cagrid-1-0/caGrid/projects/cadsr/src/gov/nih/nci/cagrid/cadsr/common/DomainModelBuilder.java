package gov.nih.nci.cagrid.cadsr.common;

import gov.nih.nci.cadsr.domain.DataElement;
import gov.nih.nci.cadsr.domain.ValueDomain;
import gov.nih.nci.cadsr.umlproject.domain.AttributeTypeMetadata;
import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.SemanticMetadata;
import gov.nih.nci.cadsr.umlproject.domain.TypeEnumerationMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLAssociationMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cagrid.cadsr.domain.UMLAssociationExclude;
import gov.nih.nci.cagrid.metadata.common.Enumeration;
import gov.nih.nci.cagrid.metadata.common.UMLAttribute;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClass;
import gov.nih.nci.cagrid.metadata.common.UMLClassUmlAttributeCollection;
import gov.nih.nci.cagrid.metadata.common.ValueDomainEnumerationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLAssociationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelExposedUMLClassCollection;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModelUmlGeneralizationCollection;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationSourceUMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLAssociationTargetUMLAssociationEdge;
import gov.nih.nci.cagrid.metadata.dataservice.UMLClassReference;
import gov.nih.nci.cagrid.metadata.dataservice.UMLGeneralization;
import gov.nih.nci.common.util.HQLCriteria;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.impl.work.WorkManagerImpl;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.SimpleExpression;

import commonj.work.Work;
import commonj.work.WorkManager;


/**
 * DomainModelBuilder Builds a DomainModel using a thread pool for concurrent
 * requests to the remote ApplicationService.
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
	private static final String EXCLUDE_WILDCARD = "*";

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
	public DomainModel createDomainModel(Project project) throws DomainModelGenerationException {
		Project proj = findCompleteProject(project);
		// get all classes in project, preloading attributes, semantic
		// metadata (current hibernate we are using doesn't process more
		// than 1 preload, but this should work when we move forward)
		DetachedCriteria criteria = DetachedCriteria.forClass(UMLClassMetadata.class).createAlias(
			"UMLAttributeMetadataCollection", "atts").setFetchMode("atts", FetchMode.JOIN).setFetchMode(
			"atts.semanticMetadataCollection", FetchMode.JOIN).setFetchMode("semanticMetadataCollection",
			FetchMode.JOIN).setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY).createCriteria("project")
			.add(Restrictions.eq("id", proj.getId()));
		UMLClassMetadata classArr[];
		try {
			classArr = getProjectClasses(proj, criteria);
		} catch (ApplicationException e) {
			throw new DomainModelGenerationException("Problem getting project's classes.", e);
		}

		UMLAssociationMetadata[] assocArr;
		try {
			assocArr = getProjectAssociationClosure(proj, classArr, null);
		} catch (ApplicationException e) {
			throw new DomainModelGenerationException("Problem getting project's associations.", e);
		}

		return buildDomainModel(proj, classArr, assocArr);

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
	public DomainModel createDomainModelForPackages(Project project, String[] packageNames)
		throws DomainModelGenerationException {
		Project proj = findCompleteProject(project);
		UMLClassMetadata classArr[] = null;
		UMLAssociationMetadata[] assocArr = null;

		if (packageNames != null && packageNames.length > 0) {
			// build up the OR for all the package names
			Disjunction disjunction = Restrictions.disjunction();
			for (int i = 0; i < packageNames.length; i++) {
				disjunction.add(Restrictions.eq("pack.name", packageNames[i]));
			}

			// get all classes in project (where package name =packageNames[0]
			// or
			// packageNames[1] ...), preloading attributes, semantic metadata
			DetachedCriteria criteria = DetachedCriteria.forClass(UMLClassMetadata.class).createAlias(
				"UMLAttributeMetadataCollection", "atts").createAlias("UMLPackageMetadata", "pack").setFetchMode(
				"atts", FetchMode.JOIN).setFetchMode("atts.semanticMetadataCollection", FetchMode.JOIN).setFetchMode(
				"semanticMetadataCollection", FetchMode.JOIN).setResultTransformer(
				CriteriaSpecification.DISTINCT_ROOT_ENTITY).add(disjunction).createCriteria("project").add(
				Restrictions.eq("id", proj.getId()));

			try {
				classArr = getProjectClasses(proj, criteria);
			} catch (ApplicationException e) {
				throw new DomainModelGenerationException("Problem getting project's classes.", e);
			}

			try {
				assocArr = getProjectAssociationClosure(proj, classArr, null);
			} catch (ApplicationException e) {
				throw new DomainModelGenerationException("Problem getting project's associations.", e);
			}

		}
		return buildDomainModel(proj, classArr, assocArr);
	}


	/**
	 * 
	 * Gets a DomainModel that represents the project and listed classes;
	 * associations will all those between listed classes.
	 * 
	 * 
	 * @param project
	 *            The project to build a domain model for
	 * @param exposedClasses
	 *            fully qualified name of classes to include
	 * @return
	 * @throws DomainModelGenerationException
	 */
	public DomainModel createDomainModelForClasses(Project project, String[] exposedClasses)
		throws DomainModelGenerationException {
		return createDomainModelForClassesWithExcludes(project, exposedClasses, null);
	}


	/**
	 * 
	 * Gets a DomainModel that represents the project and listed classes;
	 * associations will all those between listed classes that are not in the
	 * excludes list.
	 * 
	 * 
	 * @param project
	 *            The project to build a domain model for
	 * @param exposedClasses
	 *            fully qualified name of classes to include
	 * @param excludedAssociations
	 *            associations to not include
	 * @return
	 * @throws DomainModelGenerationException
	 */
	public DomainModel createDomainModelForClassesWithExcludes(Project project, String[] fullClassNames,
		UMLAssociationExclude[] excludedAssociations) throws DomainModelGenerationException {
		Project proj = findCompleteProject(project);
		UMLClassMetadata classArr[] = null;
		UMLAssociationMetadata[] assocArr = null;

		if (fullClassNames != null && fullClassNames.length > 0) {
			// build up the OR for all the class names
			Disjunction disjunction = Restrictions.disjunction();
			for (int i = 0; i < fullClassNames.length; i++) {
				disjunction.add(Restrictions.eq("fullyQualifiedName", fullClassNames[i]));
			}

			// get all classes in project (where fqn in classname[]), preloading
			// attributes, semantic metadata
			DetachedCriteria criteria = DetachedCriteria.forClass(UMLClassMetadata.class).createAlias(
				"UMLAttributeMetadataCollection", "atts").setFetchMode("atts", FetchMode.JOIN).setFetchMode(
				"atts.semanticMetadataCollection", FetchMode.JOIN).setFetchMode("atts.semanticMetadataCollection",
				FetchMode.JOIN).setFetchMode("semanticMetadataCollection", FetchMode.JOIN).setResultTransformer(
				CriteriaSpecification.DISTINCT_ROOT_ENTITY).add(disjunction).createCriteria("project").add(
				Restrictions.eq("id", proj.getId()));

			try {
				classArr = getProjectClasses(proj, criteria);
			} catch (ApplicationException e) {
				throw new DomainModelGenerationException("Problem getting project's classes.", e);
			}

			try {
				assocArr = getProjectAssociationClosure(proj, classArr, excludedAssociations);
			} catch (ApplicationException e) {
				throw new DomainModelGenerationException("Problem getting project's associations.", e);
			}

		}
		return buildDomainModel(proj, classArr, assocArr);
	}


	private String createClassIDFilter(UMLClassMetadata[] classArr) {
		// create a list of class IDs for building closures
		Set idSet = new HashSet();
		if (classArr != null) {
			for (int i = 0; i < classArr.length; i++) {
				idSet.add(classArr[i].getId());
			}
		}

		String classIDFilter = "";
		StringBuffer sb = new StringBuffer();
		// now build the criteria from the id set
		for (Iterator iter = idSet.iterator(); iter.hasNext();) {
			String classID = (String) iter.next();
			sb.append("'" + classID + "'");
			if (iter.hasNext()) {
				sb.append(", ");
			}
		}
		if (idSet.size() > 0) {
			classIDFilter = " IN (" + sb.toString() + ")";
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("Class filter was:" + classIDFilter);
		}

		return classIDFilter;
	}


	/**
	 * Gets all of the associations of this project that are closed over the
	 * classes specified in the classArr, and not present in the excludes list.
	 * 
	 * @param proj
	 * @param classArr
	 * @param excludedAssociations
	 * @return
	 * @throws ApplicationException
	 */
	private UMLAssociationMetadata[] getProjectAssociationClosure(Project proj, UMLClassMetadata[] classArr,
		UMLAssociationExclude[] excludedAssociations) throws ApplicationException {
		if (classArr == null || classArr.length <= 0) {
			return null;
		}

		// get all associations between classes we are exposing
		String classIDFilter = createClassIDFilter(classArr);

		String associationAlias = "assoc";
		String excludesFilter = createAssociationExcludeFilter(excludedAssociations, associationAlias);

		// get all associations in project
		HQLCriteria hql = new HQLCriteria(("SELECT " + associationAlias + ", " + associationAlias + ".sourceRoleName, "
			+ associationAlias + ".sourceUMLClassMetadata.id, " + associationAlias + ".targetRoleName, "
			+ associationAlias + ".targetUMLClassMetadata.id " +

			"FROM UMLAssociationMetadata AS " + associationAlias + " WHERE " + associationAlias + ".project.id='"
			+ proj.getId() + "' AND " + associationAlias + ".sourceUMLClassMetadata.id " + classIDFilter + " AND "
			+ associationAlias + ".targetUMLClassMetadata.id " + classIDFilter + " " + excludesFilter).trim());

		if (LOG.isDebugEnabled()) {
			LOG.debug("Issuing Association query:" + hql.getHqlString());
		}

		// association, src role, src id, tar role, tar id
		long start = System.currentTimeMillis();
		List rList = this.cadsr.query(hql, UMLAssociationMetadata.class.getName());
		// the query incorrectly returns more than one association for the same
		// thing (the association ids are even different), so
		// I build up a unique set based on source and target ids and role names
		// I shouldn't have to do this (its a database view bug), but it creates
		// a lot of unecessaray processing so I'm cutting it out here
		Iterator iterator = rList.iterator();
		Map uniqMap = new HashMap();
		while (iterator.hasNext()) {
			Object[] res = (Object[]) iterator.next();
			UMLAssociationMetadata assoc = (UMLAssociationMetadata) res[0];
			String srcRole = (String) res[1];
			String srcID = (String) res[2];
			String targetRole = (String) res[3];
			String targetID = (String) res[4];
			String createdKey = srcRole + srcID + targetRole + targetID;
			LOG.debug("Created unique key:" + createdKey);
			uniqMap.put(createdKey, assoc);
		}

		Collection uniqList = uniqMap.values();
		LOG.info("Association filtering eliminated " + (rList.size() - uniqList.size())
			+ " associations from returned list of:" + rList.size());
		UMLAssociationMetadata assocArr[] = new UMLAssociationMetadata[uniqList.size()];
		// caCORE's toArray(arr) is broken (cacore bug #1382), so need to do
		// this way
		System.arraycopy(uniqList.toArray(), 0, assocArr, 0, uniqList.size());

		double duration = (System.currentTimeMillis() - start) / 1000.0;
		LOG.info(proj.getShortName() + "'s association fetch took " + duration + " seconds, and found "
			+ assocArr.length + " associations.");

		return assocArr;
	}


	/**
	 * @param excludedAssociations
	 * @return
	 */
	private String createAssociationExcludeFilter(UMLAssociationExclude[] excludedAssociations, String alias) {
		if (excludedAssociations == null || excludedAssociations.length == 0) {
			return "";
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < excludedAssociations.length; i++) {
			UMLAssociationExclude exclude = excludedAssociations[i];

			int filterCount = 0;
			sb.append(" AND NOT(");

			// now only process non wildcards (because this criteria is "not"ed
			// so not filtering on the wildcards makes them be excluded)
			if (!exclude.getSourceClassName().equals(EXCLUDE_WILDCARD)) {
				if (filterCount++ > 0) {
					sb.append(" AND ");
				}
				sb.append(alias + ".sourceUMLClassMetadata.fullyQualifiedName='" + exclude.getSourceClassName() + "'");

			}
			if (!exclude.getTargetClassName().equals(EXCLUDE_WILDCARD)) {
				if (filterCount++ > 0) {
					sb.append(" AND ");
				}
				sb.append(alias + ".targetUMLClassMetadata.fullyQualifiedName='" + exclude.getTargetClassName() + "'");
			}
			if (!exclude.getSourceRoleName().equals(EXCLUDE_WILDCARD)) {
				if (filterCount++ > 0) {
					sb.append(" AND ");
				}
				sb.append(alias + ".sourceRoleName='" + exclude.getSourceRoleName() + "'");
			}
			if (!exclude.getTargetRoleName().equals(EXCLUDE_WILDCARD)) {
				if (filterCount++ > 0) {
					sb.append(" AND ");
				}
				sb.append(alias + ".targetRoleName='" + exclude.getTargetRoleName() + "'");
			}

			// check for all wildcards
			if (filterCount == 0) {
				// just stop processing and create a predicate thats never true,
				// because excluding
				// everything will obviously yeild no results
				return "AND 1=2";
			}

			sb.append(")");
		}

		return sb.toString();
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
	protected DomainModel buildDomainModel(final Project proj, UMLClassMetadata[] classes,
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
						} catch (Exception e) {
							LOG.error("Error converting class:" + classMD.getFullyQualifiedName(), e);
						}
					}
				};
				workers[i] = work;
				try {
					workList.add(getWorkManager().schedule(work));
				} catch (Exception e) {
					LOG.error("Error scheduling class conversion work", e);
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
						try {
							setUMLAssociation(convertAssociation(assocMD));
						} catch (Exception e) {
							LOG.error("Error converting association:" + associationToString(assocMD), e);
						}
					}
				};
				workers[i] = work;
				try {
					workList.add(getWorkManager().schedule(work));
				} catch (Exception e) {
					LOG.error("Error scheduling class conversion work", e);
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

		LOG.info("Beginning processing of generalizations for project: " + proj.getShortName());
		start = System.currentTimeMillis();
		DomainModelUmlGeneralizationCollection genCollection = new DomainModelUmlGeneralizationCollection();
		if (classes != null && classes.length > 0) {
			// build generalizations
			UMLGeneralization[] genArr = buildGeneralizations(classes);
			LOG.info("Found " + genArr.length + " generalizations for project: " + proj.getShortName());
			genCollection.setUMLGeneralization(genArr);
		}
		model.setUmlGeneralizationCollection(genCollection);
		duration = (System.currentTimeMillis() - start) / 1000.0;
		LOG.info("Finished generalization processing for project: " + proj.getShortName() + " in " + duration
			+ " seconds.");

		return model;
	}


	private UMLGeneralization[] buildGeneralizations(UMLClassMetadata[] classes) throws DomainModelGenerationException {
		// get all generalizations between classes we are exposing
		String classIDFilter = createClassIDFilter(classes);

		HQLCriteria hql = new HQLCriteria(
			"SELECT c.id, c.UMLGeneralizationMetadata.superUMLClassMetadata.id FROM UMLClassMetadata AS c WHERE c.id "
				+ classIDFilter);
		LOG.debug("Issuing generialization query with HQL:" + hql.getHqlString());

		try {
			List rList = this.cadsr.query(hql, "UMLClassMetadata");
			UMLGeneralization genArr[] = new UMLGeneralization[rList.size()];
			int ind = 0;
			for (Iterator resultsIterator = rList.iterator(); resultsIterator.hasNext();) {
				Object[] result = (Object[]) resultsIterator.next();
				String subID = (String) result[0];
				String superID = (String) result[1];
				UMLGeneralization gen = new UMLGeneralization(new UMLClassReference(subID), new UMLClassReference(
					superID));
				genArr[ind++] = gen;
			}

			return genArr;
		} catch (Exception e) {
			LOG.error("Error creating Generalizations.", e);
			throw new DomainModelGenerationException("Error creating Generalizations.", e);
		}
	}


	private gov.nih.nci.cagrid.metadata.dataservice.UMLAssociation convertAssociation(
		UMLAssociationMetadata coreAssociation) throws ApplicationException, DomainModelGenerationException {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Converting association:" + associationToString(coreAssociation));
		}

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
		convertedTargetEdge.setUMLAssociationEdge(targetEdge);
		converted.setTargetUMLAssociationEdge(convertedTargetEdge);

		// umlproject.UMLAssociationMetadata is broken so need to issue
		// my own hibernate query to get associated UMLClasses (could have just
		// called getters otherwise)
		setUMLClassReferences(coreAssociation, converted);

		return converted;
	}


	private void setUMLClassReferences(UMLAssociationMetadata coreAssociation, UMLAssociation domainAssociation)
		throws ApplicationException, DomainModelGenerationException {

		SimpleExpression idRes = Restrictions.eq("id", coreAssociation.getId());
		ProjectionList projection = Projections.projectionList().add(Projections.property("sourceUMLClassMetadata.id"))
			.add(Projections.property("targetUMLClassMetadata.id"));
		DetachedCriteria criteria = DetachedCriteria.forClass(UMLAssociationMetadata.class);
		criteria.add(idRes);
		criteria.setProjection(projection);

		long start = System.currentTimeMillis();
		List rList = this.cadsr.query(criteria, UMLAssociationMetadata.class.getName());
		Iterator iterator = rList.iterator();
		if (iterator == null || !iterator.hasNext()) {
			throw new DomainModelGenerationException("Unable to located source and target ids for association!");
		}
		// should have length 2, with src, target
		Object[] ids = (Object[]) iterator.next();
		if (ids == null || ids.length != 2) {
			throw new DomainModelGenerationException("Unexpected result during query for association ids!");
		}

		domainAssociation.getSourceUMLAssociationEdge().getUMLAssociationEdge().setUMLClassReference(
			new UMLClassReference((String) ids[0]));

		domainAssociation.getTargetUMLAssociationEdge().getUMLAssociationEdge().setUMLClassReference(
			new UMLClassReference((String) ids[1]));

		double duration = (System.currentTimeMillis() - start) / 1000.0;
		LOG.info("Association id fetch took " + duration + " seconds.");

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

			gov.nih.nci.cagrid.metadata.common.SemanticMetadata[] smArray = semanticMetadataCollectionToArray(classMetadata
				.getSemanticMetadataCollection());
			converted.setSemanticMetadata(smArray);
		}
		return converted;
	}


	private UMLAttribute[] createClassAttributes(UMLClassMetadata classMetadata) throws ApplicationException {
		long start = System.currentTimeMillis();

		HQLCriteria criteria = new HQLCriteria(
			"SELECT DISTINCT att, att.dataElement.valueDomain, att.attributeTypeMetadata, att.dataElement FROM UMLAttributeMetadata att "
				+ "LEFT JOIN FETCH att.semanticMetadataCollection "
				+ "WHERE att.UMLClassMetadata.id='"
				+ classMetadata.getId() + "'  ORDER BY att.name");

		List rList = cadsr.query(criteria, UMLAttributeMetadata.class.getName());

		Map attMap = new HashMap();
		int ind = 0;
		for (Iterator resultsIterator = rList.iterator(); resultsIterator.hasNext(); ind++) {
			long attstart = System.currentTimeMillis();
			Object[] result = (Object[]) resultsIterator.next();
			UMLAttributeMetadata attMD = (UMLAttributeMetadata) result[0];
			ValueDomain vd = (ValueDomain) result[1];
			AttributeTypeMetadata attTypemd = (AttributeTypeMetadata) result[2];
			DataElement de = (DataElement) result[3];

			// filter out duplicate attributes (caCORE bug in the materialized
			// view?)
			if (attMap.containsKey(de.getPublicID())) {
				continue;
			}

			// build the attribute
			UMLAttribute converted = new UMLAttribute();
			String description = attMD.getDescription();
			if (description == null) {
				description = "";
			}
			converted.setDescription(description);
			converted.setName(attMD.getName());
			if (de.getPublicID() != null) {
				converted.setPublicID(de.getPublicID().longValue());
			}
			if (de.getVersion() != null) {
				converted.setVersion(de.getVersion().floatValue());
			}
			converted.setDataTypeName(attTypemd.getValueDomainDataType());

			// add a value domain
			gov.nih.nci.cagrid.metadata.common.ValueDomain attVD = new gov.nih.nci.cagrid.metadata.common.ValueDomain();
			attVD.setLongName(vd.getLongName());
			attVD.setUnitOfMeasure(vd.getUOMName());
			converted.setValueDomain(attVD);

			// populate vd semantic md
			attVD.setSemanticMetadata(semanticMetadataCollectionToArray(attTypemd.getSemanticMetadataCollection()));

			// populate enumeration
			ValueDomainEnumerationCollection enumCollection = new ValueDomainEnumerationCollection();
			attVD.setEnumerationCollection(enumCollection);

			HQLCriteria enumcriteria = new HQLCriteria("SELECT DISTINCT enum FROM TypeEnumerationMetadata enum "
				+ "LEFT JOIN FETCH enum.semanticMetadataCollection "
				+ "WHERE enum.id in (SELECT e.id FROM TypeEnumerationMetadata e, AttributeTypeMetadata t"
				+ " WHERE t.typeEnumerationCollection.id=e.id AND t.id='" + attTypemd.getId() + "')");

			List enumRList = cadsr.query(enumcriteria, AttributeTypeMetadata.class.getName());

			Iterator typeEnumIter = enumRList.iterator();
			Enumeration enumArr[] = new Enumeration[enumRList.size()];
			int i = 0;
			while (typeEnumIter.hasNext()) {
				TypeEnumerationMetadata typeEnum = (TypeEnumerationMetadata) typeEnumIter.next();
				Enumeration enumer = new Enumeration();
				enumer.setPermissibleValue(typeEnum.getPermissibleValue());
				enumer.setValueMeaning(typeEnum.getValueMeaning());
				enumArr[i++] = enumer;
				// populate enumeration semantic md
				enumer.setSemanticMetadata(semanticMetadataCollectionToArray(typeEnum.getSemanticMetadataCollection()));
			}
			enumCollection.setEnumeration(enumArr);

			// populate att semantic md
			gov.nih.nci.cagrid.metadata.common.SemanticMetadata[] metadatas = semanticMetadataCollectionToArray(attMD
				.getSemanticMetadataCollection());
			converted.setSemanticMetadata(metadatas);

			attMap.put(de.getPublicID(), converted);
			LOG.debug("Converted attribute: " + attMD.getName() + " in " + (System.currentTimeMillis() - attstart)
				/ 1000.0 + " seconds.");

		}

		UMLAttribute[] attArr = new UMLAttribute[attMap.size()];
		attMap.values().toArray(attArr);
		double duration = (System.currentTimeMillis() - start) / 1000.0;
		LOG.info(classMetadata.getFullyQualifiedName() + " attribute conversion took " + duration + " seconds.");

		return attArr;
	}


	private static gov.nih.nci.cagrid.metadata.common.SemanticMetadata[] semanticMetadataCollectionToArray(
		Collection semanticMetadata) {
		gov.nih.nci.cagrid.metadata.common.SemanticMetadata[] smArray = new gov.nih.nci.cagrid.metadata.common.SemanticMetadata[semanticMetadata
			.size()];

		Iterator iter = semanticMetadata.iterator();
		int i = 0;
		while (iter.hasNext()) {
			SemanticMetadata sm = (SemanticMetadata) iter.next();
			gov.nih.nci.cagrid.metadata.common.SemanticMetadata converted = new gov.nih.nci.cagrid.metadata.common.SemanticMetadata();
			converted.setConceptCode(sm.getConceptCode());
			converted.setConceptDefinition(sm.getConceptDefinition());
			converted.setConceptName(sm.getConceptName());
			converted.setOrder(sm.getOrder());
			converted.setOrderLevel(sm.getOrderLevel());
			smArray[i++] = converted;

		}

		return smArray;
	}


	private static String associationToString(UMLAssociationMetadata assoc) {
		return assoc.getSourceRoleName() + "(" + assoc.getSourceLowCardinality() + "..."
			+ assoc.getSourceHighCardinality() + ")"
			+ ((assoc.getIsBidirectional() != null && assoc.getIsBidirectional().booleanValue()) ? "<" : "") + " -->"
			+ assoc.getTargetRoleName() + "(" + assoc.getTargetLowCardinality() + "..."
			+ assoc.getTargetHighCardinality() + ")";

	}


	private static String associationToString(UMLAssociation assoc) {
		UMLAssociationEdge source = null;
		if (assoc.getSourceUMLAssociationEdge() == null
			|| assoc.getSourceUMLAssociationEdge().getUMLAssociationEdge() == null) {
			source = new UMLAssociationEdge();
		} else {
			source = assoc.getSourceUMLAssociationEdge().getUMLAssociationEdge();
		}

		UMLAssociationEdge target = null;
		if (assoc.getTargetUMLAssociationEdge() == null
			|| assoc.getTargetUMLAssociationEdge().getUMLAssociationEdge() == null) {
			target = new UMLAssociationEdge();
		} else {
			target = assoc.getTargetUMLAssociationEdge().getUMLAssociationEdge();
		}

		return source.getRoleName() + "(" + source.getMinCardinality() + "..." + source.getMaxCardinality() + ")"
			+ ((assoc.isBidirectional()) ? "<" : "") + " -->" + target.getRoleName() + "(" + target.getMinCardinality()
			+ "..." + target.getMaxCardinality() + ")";

	}


	private Project findCompleteProject(Project prototype) throws DomainModelGenerationException {
		if (prototype == null) {
			throw new DomainModelGenerationException("Null project not valid.");
		}

		// clear this out and refresh it (in case its stale)
		prototype.setId(null);

		List completeProjects = new ArrayList();
		Iterator projectIter = null;
		Project proj = null;
		try {
			projectIter = this.cadsr.search(Project.class, prototype).iterator();
		} catch (Exception ex) {
			throw new DomainModelGenerationException("Error retrieving complete project: " + ex.getMessage(), ex);
		}
		// should be ONLY ONE project from the caDSR
		while (projectIter.hasNext()) {
			completeProjects.add(projectIter.next());
		}
		if (completeProjects.size() == 1) {
			proj = (Project) completeProjects.get(0);
		} else if (completeProjects.size() == 0) {
			throw new DomainModelGenerationException("No project found in caDSR");
		} else {
			throw new DomainModelGenerationException("More than one project (" + completeProjects.size()
				+ ") found.  Prototype project is ambiguous");
		}

		return proj;
	}


	public synchronized WorkManager getWorkManager() {
		if (this.workManager == null) {
			this.workManager = new WorkManagerImpl(DEFAULT_POOL_SIZE);
		}

		return this.workManager;
	}


	public synchronized void setWorkManager(WorkManager workManager) {
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
