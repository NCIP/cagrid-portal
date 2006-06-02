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
import gov.nih.nci.cagrid.cadsr.common.CaDSRServiceI;
import gov.nih.nci.cagrid.cadsr.common.CaDSRUtils;
import gov.nih.nci.cagrid.cadsr.domain.UMLAssociation;
import gov.nih.nci.system.applicationservice.ApplicationService;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.InitialContext;

import org.apache.axis.MessageContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.globus.wsrf.Constants;


/**
 * gov.nih.nci.cagrid.cadsrI
 * 
 * @created by caGrid toolkit version 1.0
 */
public class CaDSRServiceImpl implements CaDSRServiceI {

	protected static Log LOG = LogFactory.getLog(CaDSRServiceImpl.class.getName());
	private ServiceConfiguration configuration;


	public CaDSRServiceImpl() {

	}


	public gov.nih.nci.cadsr.umlproject.domain.Project[] findAllProjects() throws RemoteException {
		try {
			ApplicationService appService = getApplicationService();
			LOG.debug("Using basic search. Retrieving allprojects");

			Project projPrototype = new Project();

			try {
				List resultList = appService.search(Project.class, projPrototype);

				Project arr[] = new Project[resultList.size()];
				LOG.debug("result count: " + resultList.size());
				int index = 0;
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
					Project project = (Project) resultsIterator.next();
					LOG.debug("project name:" + project.getLongName());
					arr[index++] = project;
				}
				return arr;
			} catch (Exception e) {
				LOG.error("Exception while searching.", e);
				throw new RemoteException(e.getMessage(), e);
			}

		} catch (Exception e) {
			LOG.error("Exception while searching.", e);
			throw new RemoteException(e.getMessage(), e);
		}
	}


	public gov.nih.nci.cadsr.umlproject.domain.Project[] findProjects(java.lang.String context) throws RemoteException {
		try {
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
				LOG.debug("result count: " + resultList.size());
				int index = 0;
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
					Project project = (Project) resultsIterator.next();
					LOG.debug("project name:" + project.getLongName());
					arr[index++] = project;
				}
				return arr;
			} catch (Exception e) {
				LOG.error("Exception while searching.", e);
				throw new RemoteException(e.getMessage(), e);
			}

		} catch (Exception e) {
			LOG.error("Exception while searching.", e);
			throw new RemoteException(e.getMessage(), e);
		}
	}


	public gov.nih.nci.cadsr.umlproject.domain.UMLPackageMetadata[] findPackagesInProject(
		gov.nih.nci.cadsr.umlproject.domain.Project project) throws RemoteException {
		try {
			ApplicationService appService = getApplicationService();
			LOG.debug("Using basic search. Retrieving all packages under project:" + project.getShortName());

			UMLPackageMetadata packagePrototype = new UMLPackageMetadata();
			packagePrototype.setProject(project);

			try {
				List resultList = appService.search(UMLPackageMetadata.class, packagePrototype);
				UMLPackageMetadata arr[] = new UMLPackageMetadata[resultList.size()];
				LOG.debug("result count: " + resultList.size());
				int index = 0;
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
					UMLPackageMetadata pack = (UMLPackageMetadata) resultsIterator.next();
					LOG.debug("package name:" + pack.getName());
					arr[index++] = pack;
				}
				return arr;
			} catch (Exception e) {
				LOG.error("Exception while searching.", e);
				throw new RemoteException(e.getMessage(), e);
			}

		} catch (Exception e) {
			LOG.error("Exception while searching.", e);
			throw new RemoteException(e.getMessage(), e);
		}
	}


	public gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata[] findClassesInProject(
		gov.nih.nci.cadsr.umlproject.domain.Project project) throws RemoteException {
		try {
			ApplicationService appService = getApplicationService();
			LOG.debug("Using basic search. Retrieving all classes under project:" + project.getShortName());

			UMLClassMetadata classPrototype = new UMLClassMetadata();
			classPrototype.setProject(project);

			try {
				List resultList = appService.search(UMLClassMetadata.class, classPrototype);
				UMLClassMetadata arr[] = new UMLClassMetadata[resultList.size()];
				LOG.debug("result count: " + resultList.size());
				int index = 0;
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
					UMLClassMetadata pack = (UMLClassMetadata) resultsIterator.next();
					LOG.debug("class name:" + pack.getName());
					arr[index++] = pack;
				}
				return arr;
			} catch (Exception e) {
				LOG.error("Exception while searching.", e);
				throw new RemoteException(e.getMessage(), e);
			}

		} catch (Exception e) {
			LOG.error("Exception while searching.", e);
			throw new RemoteException(e.getMessage(), e);
		}
	}


	public gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata[] findClassesInPackage(
		gov.nih.nci.cadsr.umlproject.domain.Project project, java.lang.String packageName) throws RemoteException {
		try {
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
				LOG.debug("result count: " + resultList.size());
				int index = 0;
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
					UMLClassMetadata clazz = (UMLClassMetadata) resultsIterator.next();
					LOG.debug("class name:" + clazz.getName());
					arr[index++] = clazz;
				}
				return arr;
			} catch (Exception e) {
				LOG.error("Exception while searching.", e);
				throw new RemoteException(e.getMessage(), e);
			}

		} catch (Exception e) {
			LOG.error("Exception while searching.", e);
			throw new RemoteException(e.getMessage(), e);
		}
	}


	public gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata[] findAttributesInClass(
		gov.nih.nci.cadsr.umlproject.domain.Project project, gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata clazz)
		throws RemoteException {
		try {
			ApplicationService appService = getApplicationService();
			LOG.debug("Using basic search. Retrieving all attributes under class:" + clazz.getName());

			UMLAttributeMetadata attPrototype = new UMLAttributeMetadata();
			attPrototype.setUMLClassMetadata(clazz);
			attPrototype.setProject(project);

			try {
				List resultList = appService.search(UMLAttributeMetadata.class, attPrototype);
				UMLAttributeMetadata arr[] = new UMLAttributeMetadata[resultList.size()];
				LOG.debug("result count: " + resultList.size());
				int index = 0;
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
					UMLAttributeMetadata att = (UMLAttributeMetadata) resultsIterator.next();

					// TODO: remove when this hackaround for cacore bug is not
					// needed
					String attName = att.getName();
					int ind = attName.indexOf(":");
					if (ind >= 0) {
						att.setName(attName.substring(ind + 1));
					}
					// END HACK

					LOG.debug("attribute name:" + att.getName());
					arr[index++] = att;
				}
				return arr;
			} catch (Exception e) {
				LOG.error("Exception while searching.", e);
				throw new RemoteException(e.getMessage(), e);
			}

		} catch (Exception e) {
			LOG.error("Exception while searching.", e);
			throw new RemoteException(e.getMessage(), e);
		}
	}


	public gov.nih.nci.cadsr.umlproject.domain.SemanticMetadata[] findSemanticMetadataForClass(
		gov.nih.nci.cadsr.umlproject.domain.Project project, gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata clazz)
		throws RemoteException {
		try {
			ApplicationService appService = getApplicationService();
			LOG.debug("Using basic search. Retrieving all semantics for class:" + clazz.getName());

			// associate the class with the project
			clazz.setProject(project);

			try {
				List resultList = appService.search(SemanticMetadata.class, clazz);
				SemanticMetadata arr[] = new SemanticMetadata[resultList.size()];
				LOG.debug("result count: " + resultList.size());
				int index = 0;
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
					SemanticMetadata sem = (SemanticMetadata) resultsIterator.next();
					LOG.debug("semantic concept code:" + sem.getConceptCode());
					arr[index++] = sem;
				}
				return arr;
			} catch (Exception e) {
				LOG.error("Exception while searching.", e);
				throw new RemoteException(e.getMessage(), e);
			}

		} catch (Exception e) {
			LOG.error("Exception while searching.", e);
			throw new RemoteException(e.getMessage(), e);
		}
	}


	public gov.nih.nci.cadsr.domain.ValueDomain findValueDomainForAttribute(
		gov.nih.nci.cadsr.umlproject.domain.Project project,
		gov.nih.nci.cadsr.umlproject.domain.UMLAttributeMetadata attribute) throws RemoteException {
		try {
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

		} catch (Exception e) {
			LOG.error("Exception while searching.", e);
			throw new RemoteException(e.getMessage(), e);
		}
	}


	private ApplicationService getApplicationService() throws Exception {
		String serviceURL = getConfiguration().getCaCOREServiceURL();
		return ApplicationService.getRemoteInstance(serviceURL);
	}


	public ServiceConfiguration getConfiguration() throws Exception {
		if (this.configuration != null) {
			return this.configuration;
		}
		MessageContext ctx = MessageContext.getCurrentContext();

		String servicePath = ctx.getTargetService();

		String jndiName = Constants.JNDI_SERVICES_BASE_NAME + servicePath + "/serviceconfiguration";
		try {
			javax.naming.Context initialContext = new InitialContext();
			this.configuration = (ServiceConfiguration) initialContext.lookup(jndiName);
		} catch (Exception e) {
			throw new Exception("Unable to instantiate service configuration.", e);
		}

		return this.configuration;
	}


	public gov.nih.nci.cagrid.metadata.dataservice.DomainModel generateDomainModelForProject(
		gov.nih.nci.cadsr.umlproject.domain.Project project) throws RemoteException {
		ApplicationService cadsrService = null;
		try {
			cadsrService = getApplicationService();
		} catch (Exception ex) {
			LOG.error("Error obtaining connection to caDSR application service: " + ex.getMessage(), ex);
		}
		DomainModelBuilder builder = new DomainModelBuilder(cadsrService);
		return builder.getDomainModel(project);
	}


	public gov.nih.nci.cagrid.metadata.dataservice.DomainModel generateDomainModelForPackages(
		gov.nih.nci.cadsr.umlproject.domain.Project project, java.lang.String[] packageNames) throws RemoteException {
		ApplicationService cadsrService = null;
		try {
			cadsrService = getApplicationService();
		} catch (Exception ex) {
			LOG.error("Error obtaining connection to caDSR application service: " + ex.getMessage(), ex);
		}
		DomainModelBuilder builder = new DomainModelBuilder(cadsrService);
		return builder.getDomainModel(project, packageNames);
	}


	public gov.nih.nci.cagrid.metadata.dataservice.DomainModel generateDomainModelForClasses(
		gov.nih.nci.cadsr.umlproject.domain.Project project,
		gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata[] classes,
		gov.nih.nci.cagrid.cadsr.domain.UMLAssociation[] associations) throws RemoteException {
		ApplicationService cadsrService = null;
		try {
			cadsrService = getApplicationService();
		} catch (Exception ex) {
			LOG.error("Error obtaining connection to caDSR application service: " + ex.getMessage(), ex);
		}
		DomainModelBuilder builder = new DomainModelBuilder(cadsrService);
		return builder.getDomainModel(project, classes, associations);
	}


	public gov.nih.nci.cagrid.cadsr.domain.UMLAssociation[] findAssociationsForClass(
		gov.nih.nci.cadsr.umlproject.domain.Project project, gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata clazz)
		throws RemoteException {

		try {
			ApplicationService appService = getApplicationService();
			LOG.debug("Using basic search. Retrieving all associations for class:" + clazz.getName());

			UMLAssociationMetadata umlAssocPrototype = new UMLAssociationMetadata();
			umlAssocPrototype.setSourceUMLClassMetadata(clazz);
			umlAssocPrototype.setProject(project);

			try {
				List resultList = appService.search(UMLAssociationMetadata.class, umlAssocPrototype);
				UMLAssociation arr[] = new UMLAssociation[resultList.size()];
				LOG.debug("result count: " + resultList.size());
				int index = 0;
				for (Iterator resultsIterator = resultList.iterator(); resultsIterator.hasNext();) {
					UMLAssociationMetadata assoc = (UMLAssociationMetadata) resultsIterator.next();
					LOG.debug("target role:" + assoc.getTargetRoleName());
					arr[index++] = CaDSRUtils.convertAssociation(assoc);
				}
				return arr;
			} catch (Exception e) {
				LOG.error("Exception while searching.", e);
				throw new RemoteException(e.getMessage(), e);
			}

		} catch (Exception e) {
			LOG.error("Exception while searching.", e);
			throw new RemoteException(e.getMessage(), e);
		}

	}


	public gov.nih.nci.cagrid.cadsr.domain.UMLAssociation[] findAssociationsInPackage(
		gov.nih.nci.cadsr.umlproject.domain.Project project, java.lang.String packageName) throws RemoteException {
		try {
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
					arr[index++] = CaDSRUtils.convertAssociation(assoc);
				}
				return arr;
			} catch (Exception e) {
				LOG.error("Exception while searching.", e);
				throw new RemoteException(e.getMessage(), e);
			}

		} catch (Exception e) {
			LOG.error("Exception while searching.", e);
			throw new RemoteException(e.getMessage(), e);
		}
	}


	public gov.nih.nci.cagrid.cadsr.domain.UMLAssociation[] findAssociationsInProject(
		gov.nih.nci.cadsr.umlproject.domain.Project project) throws RemoteException {
		try {
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
					arr[index++] = CaDSRUtils.convertAssociation(assoc);
				}
				return arr;
			} catch (Exception e) {
				LOG.error("Exception while searching.", e);
				throw new RemoteException(e.getMessage(), e);
			}

		} catch (Exception e) {
			LOG.error("Exception while searching.", e);
			throw new RemoteException(e.getMessage(), e);
		}
	}

}
