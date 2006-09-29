package gov.nih.nci.cagrid.cadsr.service;

import gov.nih.nci.cadsr.domain.ClassificationScheme;
import gov.nih.nci.cadsr.domain.Context;
import gov.nih.nci.cadsr.domain.ValueDomain;
import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.SemanticMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLAssociationMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata;
import gov.nih.nci.cagrid.cadsr.common.CaDSRUtils;
import gov.nih.nci.cagrid.cadsr.common.DomainModelBuilder;
import gov.nih.nci.cagrid.cadsr.common.DomainModelGenerationException;
import gov.nih.nci.cagrid.cadsr.domain.UMLAssociation;
import gov.nih.nci.cagrid.cadsr.stubs.types.InvalidProjectException;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.impl.work.WorkManagerImpl;

import commonj.work.WorkManager;

/**
 * gov.nih.nci.cagrid.cadsrI
 * 
 * @created by caGrid toolkit version 1.0
 */
public class CaDSRServiceImpl extends CaDSRServiceImplBase {
	private static final int DEFAULT_POOL_SIZE = 10;

	protected static Log LOG = LogFactory.getLog(CaDSRServiceImpl.class.getName());
	private WorkManager workManager = null;

	public CaDSRServiceImpl() throws RemoteException {

	}

	public gov.nih.nci.cadsr.umlproject.domain.Project[] findAllProjects() throws RemoteException {
		ApplicationService appService = getApplicationService();
		LOG.debug("Using basic search. Retrieving allprojects");

		Project projPrototype = new Project();

		try {
			List resultList = appService.search(Project.class, projPrototype);

			Project arr[] = new Project[resultList.size()];
			// caCORE's toArray(arr) is broken (cacore bug #1382), so need to do
			// this way
			LOG.debug("result count: " + resultList.size());
			System.arraycopy(resultList.toArray(), 0, arr, 0, resultList.size());
			return arr;
		} catch (Exception e) {
			LOG.error("Exception while searching.", e);
			throw new RemoteException(e.getMessage(), e);
		}

	}

	public gov.nih.nci.cadsr.umlproject.domain.Project[] findProjects(java.lang.String context) throws RemoteException {

		ApplicationService appService = getApplicationService();
		LOG.debug("Using basic search. Retrieving all projects under context:" + context);

		Project projPrototype = new Project();
		ClassificationScheme cs = new ClassificationScheme();
		cs.setType("Project");// is this necessary?
		Context ctx = new Context();
		ctx.setName(context);
		cs.setContext(ctx);
		projPrototype.setClassificationScheme(cs);

		try {
			List resultList = appService.search(Project.class, projPrototype);
			Project arr[] = new Project[resultList.size()];

			// caCORE's toArray(arr) is broken (cacore bug #1382), so need to do
			// this way
			LOG.debug("result count: " + resultList.size());
			System.arraycopy(resultList.toArray(), 0, arr, 0, resultList.size());
			return arr;

		} catch (Exception e) {
			LOG.error("Exception while searching.", e);
			throw new RemoteException(e.getMessage(), e);
		}
	}

	public gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata[] findPackagesInProject(gov.nih.nci.cadsr.umlproject.domain.Project project) throws RemoteException, gov.nih.nci.cagrid.cadsr.stubs.types.InvalidProjectException {

		if (project == null) {
			InvalidProjectException fault = new InvalidProjectException();
			fault.setFaultString("Null project not valid.");
			throw fault;
		}

		ApplicationService appService = getApplicationService();
		LOG.debug("Using basic search. Retrieving all packages under project:" + project.getShortName());

		UMLPackageMetadata packagePrototype = new UMLPackageMetadata();
		packagePrototype.setProject(project);

		try {
			List resultList = appService.search(UMLPackageMetadata.class, packagePrototype);
			UMLPackageMetadata arr[] = new UMLPackageMetadata[resultList.size()];

			// caCORE's toArray(arr) is broken (cacore bug #1382), so need to do
			// this way
			LOG.debug("result count: " + resultList.size());
			System.arraycopy(resultList.toArray(), 0, arr, 0, resultList.size());
			return arr;
		} catch (Exception e) {
			LOG.error("Exception while searching.", e);
			throw new RemoteException(e.getMessage(), e);
		}

	}

	public gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata[] findClassesInProject(gov.nih.nci.cadsr.umlproject.domain.Project project) throws RemoteException, gov.nih.nci.cagrid.cadsr.stubs.types.InvalidProjectException {

		if (project == null) {
			InvalidProjectException fault = new InvalidProjectException();
			fault.setFaultString("Null project not valid.");
			throw fault;
		}

		ApplicationService appService = getApplicationService();
		LOG.debug("Using basic search. Retrieving all classes under project:" + project.getShortName());

		UMLClassMetadata classPrototype = new UMLClassMetadata();
		classPrototype.setProject(project);

		try {
			List resultList = appService.search(UMLClassMetadata.class, classPrototype);
			UMLClassMetadata arr[] = new UMLClassMetadata[resultList.size()];

			// caCORE's toArray(arr) is broken (cacore bug #1382), so need to do
			// this way
			LOG.debug("result count: " + resultList.size());
			System.arraycopy(resultList.toArray(), 0, arr, 0, resultList.size());
			return arr;
		} catch (Exception e) {
			LOG.error("Exception while searching.", e);
			throw new RemoteException(e.getMessage(), e);
		}
	}

	public gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata[] findClassesInPackage(gov.nih.nci.cadsr.umlproject.domain.Project project,java.lang.String packageName) throws RemoteException, gov.nih.nci.cagrid.cadsr.stubs.types.InvalidProjectException {

		if (project == null) {
			InvalidProjectException fault = new InvalidProjectException();
			fault.setFaultString("Null project not valid.");
			throw fault;
		}

		ApplicationService appService = getApplicationService();
		LOG.debug("Using basic search. Retrieving all classes under package:" + packageName);

		UMLClassMetadata classPrototype = new UMLClassMetadata();
		UMLPackageMetadata pkg = new UMLPackageMetadata();
		pkg.setName(packageName);
		classPrototype.setUMLPackageMetadata(pkg);
		classPrototype.setProject(project);

		try {
			List resultList = appService.search(UMLClassMetadata.class, classPrototype);
			UMLClassMetadata arr[] = new UMLClassMetadata[resultList.size()];

			// caCORE's toArray(arr) is broken (cacore bug #1382), so need to do
			// this way
			LOG.debug("result count: " + resultList.size());
			System.arraycopy(resultList.toArray(), 0, arr, 0, resultList.size());
			return arr;
		} catch (Exception e) {
			LOG.error("Exception while searching.", e);
			throw new RemoteException(e.getMessage(), e);
		}

	}

	public gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata[] findAttributesInClass(gov.nih.nci.cadsr.umlproject.domain.Project project,gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata clazz) throws RemoteException, gov.nih.nci.cagrid.cadsr.stubs.types.InvalidProjectException {
		if (project == null) {
			InvalidProjectException fault = new InvalidProjectException();
			fault.setFaultString("Null project not valid.");
			throw fault;
		}

		ApplicationService appService = getApplicationService();
		LOG.debug("Using basic search. Retrieving all attributes under class:" + clazz.getName());

		UMLAttributeMetadata attPrototype = new UMLAttributeMetadata();
		attPrototype.setUMLClassMetadata(clazz);
		attPrototype.setProject(project);

		try {
			List resultList = appService.search(UMLAttributeMetadata.class, attPrototype);
			UMLAttributeMetadata arr[] = new UMLAttributeMetadata[resultList.size()];

			// caCORE's toArray(arr) is broken (cacore bug #1382), so need to do
			// this way
			LOG.debug("result count: " + resultList.size());
			System.arraycopy(resultList.toArray(), 0, arr, 0, resultList.size());
			return arr;
		} catch (Exception e) {
			LOG.error("Exception while searching.", e);
			throw new RemoteException(e.getMessage(), e);
		}
	}

	public gov.nih.nci.cadsr.umlproject.domain.SemanticMetadata[] findSemanticMetadataForClass(gov.nih.nci.cadsr.umlproject.domain.Project project,gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata clazz) throws RemoteException, gov.nih.nci.cagrid.cadsr.stubs.types.InvalidProjectException {

		if (project == null) {
			InvalidProjectException fault = new InvalidProjectException();
			fault.setFaultString("Null project not valid.");
			throw fault;
		}

		ApplicationService appService = getApplicationService();
		LOG.debug("Using basic search. Retrieving all semantics for class:" + clazz.getName());

		// associate the class with the project
		clazz.setProject(project);

		try {
			List resultList = appService.search(SemanticMetadata.class, clazz);
			SemanticMetadata arr[] = new SemanticMetadata[resultList.size()];

			// caCORE's toArray(arr) is broken (cacore bug #1382), so need to do
			// this way
			LOG.debug("result count: " + resultList.size());
			System.arraycopy(resultList.toArray(), 0, arr, 0, resultList.size());
			return arr;
		} catch (Exception e) {
			LOG.error("Exception while searching.", e);
			throw new RemoteException(e.getMessage(), e);
		}
	}

	public gov.nih.nci.cadsr.domain.ValueDomain findValueDomainForAttribute(gov.nih.nci.cadsr.umlproject.domain.Project project,gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata attribute) throws RemoteException, gov.nih.nci.cagrid.cadsr.stubs.types.InvalidProjectException {

		if (project == null) {
			InvalidProjectException fault = new InvalidProjectException();
			fault.setFaultString("Null project not valid.");
			throw fault;
		}

		ApplicationService appService = getApplicationService();
		LOG.debug("Using basic search. Retrieving value domain for attribute:" + attribute.getName());

		// associate the class with the project
		attribute.setProject(project);

		try {
			List resultList = appService.search(
				"gov.nih.nci.cadsr.domain.ValueDomain,gov.nih.nci.cadsr.domain.DataElement", attribute);

			if (resultList.size() > 1) {
				LOG.error("findValueDomainForAttribute returned more than one ValueDomain for attribute:"
					+ attribute.getFullyQualifiedName() + " in project:" + project.getShortName());
			}

			if (resultList.size() == 0) {
				return null;
			} else {
				return (ValueDomain) resultList.get(0);
			}

		} catch (Exception e) {
			LOG.error("Exception while searching.", e);
			throw new RemoteException(e.getMessage(), e);
		}

	}

	public gov.nih.nci.cagrid.metadata.dataservice.DomainModel generateDomainModelForProject(gov.nih.nci.cadsr.umlproject.domain.Project project) throws RemoteException, gov.nih.nci.cagrid.cadsr.stubs.types.InvalidProjectException {
		ApplicationService cadsrService = getApplicationService();

		DomainModelBuilder builder = new DomainModelBuilder(cadsrService);
		builder.setWorkManager(getWorkManager());
		DomainModel model;
		try {
			model = builder.createDomainModel(project);
		} catch (DomainModelGenerationException e) {
			throw new RemoteException(e.getMessage(), e);
		}

		return model;
	}

	public gov.nih.nci.cagrid.metadata.dataservice.DomainModel generateDomainModelForPackages(gov.nih.nci.cadsr.umlproject.domain.Project project,java.lang.String[] packageNames) throws RemoteException, gov.nih.nci.cagrid.cadsr.stubs.types.InvalidProjectException {
		ApplicationService cadsrService = getApplicationService();

		DomainModelBuilder builder = new DomainModelBuilder(cadsrService);
		builder.setWorkManager(getWorkManager());
		DomainModel model;
		try {
			model = builder.createDomainModelForPackages(project, packageNames);
		} catch (DomainModelGenerationException e) {
			throw new RemoteException(e.getMessage(), e);
		}
		return model;
	}

	public gov.nih.nci.cagrid.cadsr.domain.UMLAssociation[] findAssociationsForClass(gov.nih.nci.cadsr.umlproject.domain.Project project,gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata clazz) throws RemoteException, gov.nih.nci.cagrid.cadsr.stubs.types.InvalidProjectException {

		if (project == null) {
			InvalidProjectException fault = new InvalidProjectException();
			fault.setFaultString("Null project not valid.");
			throw fault;
		}

		ApplicationService appService = getApplicationService();
		LOG.debug("Using basic search. Retrieving all associations for class:" + clazz.getName());

		UMLAssociationMetadata umlAssocPrototype = new UMLAssociationMetadata();
		umlAssocPrototype.setSourceUMLClassMetadata(clazz);
		umlAssocPrototype.setProject(project);

		try {
			List resultList = appService.search(UMLAssociationMetadata.class, umlAssocPrototype);
			UMLAssociation arr[] = new UMLAssociation[resultList.size()];

			// caCORE's toArray(arr) is broken (cacore bug #1382), so need to do
			// this way
			LOG.debug("result count: " + resultList.size());
			System.arraycopy(resultList.toArray(), 0, arr, 0, resultList.size());
			return arr;
		} catch (Exception e) {
			LOG.error("Exception while searching.", e);
			throw new RemoteException(e.getMessage(), e);
		}

	}

	public gov.nih.nci.cagrid.cadsr.domain.UMLAssociation[] findAssociationsInPackage(gov.nih.nci.cadsr.umlproject.domain.Project project,java.lang.String packageName) throws RemoteException, gov.nih.nci.cagrid.cadsr.stubs.types.InvalidProjectException {

		if (project == null) {
			InvalidProjectException fault = new InvalidProjectException();
			fault.setFaultString("Null project not valid.");
			throw fault;
		}

		ApplicationService appService = getApplicationService();
		LOG.debug("Using basic search. Retrieving all associations for package:" + packageName);

		// associations in the given project
		UMLAssociationMetadata umlAssocPrototype = new UMLAssociationMetadata();
		umlAssocPrototype.setProject(project);

		// classes in the given package
		UMLPackageMetadata packProto = new UMLPackageMetadata();
		packProto.setName(packageName);
		UMLClassMetadata classProto = new UMLClassMetadata();
		classProto.setUMLPackageMetadata(packProto);
		classProto.setProject(project);

		try {
			// associations where the source is our class prototype
			umlAssocPrototype.setSourceUMLClassMetadata(classProto);
			List sourceList = appService.search(UMLAssociationMetadata.class, umlAssocPrototype);
			// associations where the target is our class prototype
			umlAssocPrototype.setSourceUMLClassMetadata(null);
			umlAssocPrototype.setTargetUMLClassMetadata(classProto);
			List targetList = appService.search(UMLAssociationMetadata.class, umlAssocPrototype);

			Map joinedMap = new HashMap(sourceList.size());
			LOG.debug("Source result count: " + sourceList.size());
			for (Iterator resultsIterator = sourceList.iterator(); resultsIterator.hasNext();) {
				UMLAssociationMetadata assoc = (UMLAssociationMetadata) resultsIterator.next();
				joinedMap.put(assoc.getId(), assoc);
			}
			LOG.debug("Target result count: " + targetList.size());
			for (Iterator resultsIterator = targetList.iterator(); resultsIterator.hasNext();) {
				UMLAssociationMetadata assoc = (UMLAssociationMetadata) resultsIterator.next();
				joinedMap.put(assoc.getId(), assoc);
			}

			UMLAssociation arr[] = new UMLAssociation[joinedMap.size()];
			LOG.debug("joined result count: " + joinedMap.size());
			int index = 0;
			for (Iterator resultsIterator = joinedMap.keySet().iterator(); resultsIterator.hasNext();) {
				String assocKey = (String) resultsIterator.next();
				UMLAssociationMetadata assoc = (UMLAssociationMetadata) joinedMap.get(assocKey);
				LOG.debug("target role:" + assoc.getTargetRoleName());
				arr[index++] = CaDSRUtils.convertAssociation(getApplicationService(), assoc);
			}
			return arr;
		} catch (Exception e) {
			LOG.error("Exception while searching.", e);
			throw new RemoteException(e.getMessage(), e);
		}

	}

	public gov.nih.nci.cagrid.cadsr.domain.UMLAssociation[] findAssociationsInProject(gov.nih.nci.cadsr.umlproject.domain.Project project) throws RemoteException, gov.nih.nci.cagrid.cadsr.stubs.types.InvalidProjectException {

		if (project == null) {
			InvalidProjectException fault = new InvalidProjectException();
			fault.setFaultString("Null project not valid.");
			throw fault;
		}

		ApplicationService appService = getApplicationService();
		LOG.debug("Using basic search. Retrieving all associations for project:" + project.getLongName());

		UMLAssociationMetadata umlAssocPrototype = new UMLAssociationMetadata();
		umlAssocPrototype.setProject(project);

		try {
			List resultList = appService.search(UMLAssociationMetadata.class, umlAssocPrototype);
			UMLAssociation arr[] = new UMLAssociation[resultList.size()];
			LOG.debug("result count: " + resultList.size());
			int index = 0;
			for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
				UMLAssociationMetadata assoc = (UMLAssociationMetadata) resultsIterator.next();
				LOG.debug("target role:" + assoc.getTargetRoleName());
				arr[index++] = CaDSRUtils.convertAssociation(getApplicationService(), assoc);
			}
			return arr;
		} catch (Exception e) {
			LOG.error("Exception while searching.", e);
			throw new RemoteException(e.getMessage(), e);
		}
	}

	public gov.nih.nci.cagrid.metadata.dataservice.DomainModel generateDomainModelForClasses(gov.nih.nci.cadsr.umlproject.domain.Project project,java.lang.String[] fullClassNames) throws RemoteException, gov.nih.nci.cagrid.cadsr.stubs.types.InvalidProjectException {
		ApplicationService cadsrService = getApplicationService();

		DomainModelBuilder builder = new DomainModelBuilder(cadsrService);
		builder.setWorkManager(getWorkManager());
		DomainModel model;
		try {
			model = builder.createDomainModelForClasses(project, fullClassNames);
		} catch (DomainModelGenerationException e) {
			throw new RemoteException(e.getMessage(), e);
		}
		return model;
	}

	public gov.nih.nci.cagrid.metadata.dataservice.DomainModel generateDomainModelForClassesWithExcludes(gov.nih.nci.cadsr.umlproject.domain.Project project,java.lang.String[] fullClassNames,gov.nih.nci.cagrid.cadsr.domain.UMLAssociationExclude[] associationExcludes) throws RemoteException, gov.nih.nci.cagrid.cadsr.stubs.types.InvalidProjectException {

		ApplicationService cadsrService = getApplicationService();

		DomainModelBuilder builder = new DomainModelBuilder(cadsrService);
		builder.setWorkManager(getWorkManager());
		DomainModel model;
		try {
			model = builder.createDomainModelForClassesWithExcludes(project, fullClassNames, associationExcludes);
		} catch (DomainModelGenerationException e) {
			throw new RemoteException(e.getMessage(), e);
		}
		return model;
	}

	private ApplicationService getApplicationService() throws RemoteException {
		ServiceConfiguration conf = null;
		try {
			conf = getConfiguration();
		} catch (Exception ex) {
			LOG.error("Error obtaining service configuruation: " + ex.getMessage(), ex);
			throw new RemoteException("Error obtaining service configuruation: " + ex.getMessage(), ex);
		}
		try {
			String serviceURL = conf.getCaCOREServiceURL();
			return ApplicationService.getRemoteInstance(serviceURL);
		} catch (Exception ex) {
			LOG.error("Error obtaining connection to caDSR application service: " + ex.getMessage(), ex);
			throw new RemoteException("Error obtaining connection to caDSR application service: " + ex.getMessage(), ex);
		}

	}

	public WorkManager getWorkManager() {
		if (this.workManager == null) {
			int poolSize = DEFAULT_POOL_SIZE;
			try {
				String poolString = getConfiguration().getThreadPoolSize();
				poolSize = Integer.parseInt(poolString);
			} catch (Exception e) {
				LOG.error("Problem determing pool size, using default(" + poolSize + ").");
			}
			this.workManager = new WorkManagerImpl(poolSize);
		}

		return this.workManager;
	}

	public void setWorkManager(WorkManager workManager) {
		this.workManager = workManager;
	}

	public gov.nih.nci.cagrid.metadata.ServiceMetadata annotateServiceMetadata(gov.nih.nci.cagrid.metadata.ServiceMetadata serviceMetadata) throws RemoteException {
		//TODO: Implement this autogenerated method
		throw new RemoteException("Not yet implemented");
	}

}
