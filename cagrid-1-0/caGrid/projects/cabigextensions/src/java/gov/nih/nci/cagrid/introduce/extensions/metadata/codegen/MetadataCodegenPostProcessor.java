package gov.nih.nci.cagrid.introduce.extensions.metadata.codegen;

import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputs;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertiesListType;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.beans.service.ServicesType;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.extensions.metadata.constants.MetadataConstants;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.ServiceMetadataHostingResearchCenter;
import gov.nih.nci.cagrid.metadata.ServiceMetadataServiceDescription;
import gov.nih.nci.cagrid.metadata.common.SemanticMetadata;
import gov.nih.nci.cagrid.metadata.service.ContextProperty;
import gov.nih.nci.cagrid.metadata.service.Fault;
import gov.nih.nci.cagrid.metadata.service.InputParameter;
import gov.nih.nci.cagrid.metadata.service.Operation;
import gov.nih.nci.cagrid.metadata.service.OperationFaultCollection;
import gov.nih.nci.cagrid.metadata.service.OperationInputParameterCollection;
import gov.nih.nci.cagrid.metadata.service.Output;
import gov.nih.nci.cagrid.metadata.service.Service;
import gov.nih.nci.cagrid.metadata.service.ServiceContext;
import gov.nih.nci.cagrid.metadata.service.ServiceContextContextPropertyCollection;
import gov.nih.nci.cagrid.metadata.service.ServiceContextOperationCollection;
import gov.nih.nci.cagrid.metadata.service.ServicePointOfContactCollection;
import gov.nih.nci.cagrid.metadata.service.ServiceServiceContextCollection;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis.utils.ClassUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Creates a metadata instance file. Only creates/modifies the Service portion,
 * will leave any existing other aspects (ResearchCenter) intact.
 * 
 * @author oster
 * 
 */
public class MetadataCodegenPostProcessor implements CodegenExtensionPostProcessor {
	private static final String DEFAULT_FILENAME = "serviceMetadata.xml";
	private static final String MAIN_RF_TYPE = "main";
	private static final String SEMANTIC_METADATA_DEFAULTS_DATA_SERVICE = "default-Service-SemanticMetadata-data.xml";
	private static final String SEMANTIC_METADATA_DEFAULTS_ANALYTICAL_SERVICE = "default-Service-SemanticMetadata-analytical.xml";

	protected static Log LOG = LogFactory.getLog(MetadataCodegenPostProcessor.class.getName());


	public void postCodegen(ServiceExtensionDescriptionType desc, ServiceInformation info)
		throws CodegenExtensionException {
		ServiceMetadata metadata = null;

		String filename = info.getBaseDirectory() + File.separator + "etc" + File.separator + getFilename(info);
		if (filename == null) {
			LOG.error("Unable to locate Service Metadata resource property, skipping metadata instance creation.");
			return;
		}

		// look if the file already exists, and load it in, in case other
		// aspects of it (such as cancer center info) are set by something else
		File mdFile = new File(filename);
		if (mdFile.exists() && mdFile.canRead()) {
			try {
				FileReader reader = new FileReader(mdFile);
				metadata = MetadataUtils.deserializeServiceMetadata(reader);
				reader.close();
			} catch (Exception e) {
				LOG.error("Failed to deserialize existing metadata document!  A new one will be created.", e);
			}
		}

		// create a new model if need be, and initialize it
		if (metadata == null) {
			metadata = new ServiceMetadata();
		}
		initializeModel(metadata);

		// shell it without UML informaiton
		populateService(metadata.getServiceDescription().getService(), info);

		// serialize the model
		try {
			FileWriter writer = new FileWriter(filename);
			MetadataUtils.serializeServiceMetadata(metadata, writer);
			writer.close();
		} catch (Exception e) {
			throw new CodegenExtensionException("Error serializing metadata document.", e);
		}

	}


	/**
	 * @return
	 */
	private String getFilename(ServiceInformation info) {
		ServiceType mainServ = info.getServiceDescriptor().getServices().getService()[0];
		ResourcePropertiesListType resourcePropertiesList = mainServ.getResourcePropertiesList();
		if (resourcePropertiesList != null && resourcePropertiesList.getResourceProperty() != null) {
			ResourcePropertyType[] resourceProperty = resourcePropertiesList.getResourceProperty();
			for (int i = 0; i < resourceProperty.length; i++) {
				ResourcePropertyType rp = resourceProperty[i];
				if (rp.getQName().equals(MetadataConstants.SERVICE_METADATA_QNAME)) {
					String fileLocation = rp.getFileLocation();
					if (fileLocation == null || fileLocation.trim().equals("")) {
						rp.setFileLocation(DEFAULT_FILENAME);
					}

					return rp.getFileLocation();

				}
			}
		}

		return null;
	}


	/**
	 * Reads the service model, and builds the metadata
	 * 
	 * @param metadata
	 * @param info
	 * @throws CodegenExtensionException
	 */
	private void populateService(Service service, ServiceInformation info) throws CodegenExtensionException {
		ServiceType services[] = info.getServiceDescriptor().getServices().getService();

		// we won't set a caDSR registration status

		// initialize the service's semantic metadata
		editServiceSemanticMetadata(service, info);

		ServiceContext[] newServContexts = new ServiceContext[services.length];

		// build a map based on context names (only for lookup)
		Map contextMap = new HashMap();
		ServiceContext[] existingServiceContexts = service.getServiceContextCollection().getServiceContext();
		if (existingServiceContexts != null) {
			for (int i = 0; i < existingServiceContexts.length; i++) {
				ServiceContext context = existingServiceContexts[i];
				contextMap.put(context.getName(), context);
			}
		}

		// create/edit a service context for each service
		for (int i = 0; i < services.length; i++) {
			ServiceType serv = services[i];

			// find the existing context
			ServiceContext currContext = (ServiceContext) contextMap.get(serv.getName());
			// create a new one if necessary
			if (currContext == null) {
				currContext = new ServiceContext();
			}

			// now edit it
			editServiceContext(serv, currContext);
			newServContexts[i] = currContext;

			// use the main service to set some higher level items
			if (serv.getResourceFrameworkType().equals(MAIN_RF_TYPE)) {
				service.setName(serv.getName());

				// set a description
				if (service.getDescription() == null || service.getDescription().trim().equals("")) {
					service.setDescription("The " + service.getName()
						+ " grid service, created with caGrid Introduce, version:"
						+ info.getServiceDescriptor().getIntroduceVersion() + ".");
				}

				// set a version
				if (service.getVersion() == null || service.getVersion().trim().equals("")) {
					// version is introduce's version... should be set elsewhere
					service.setVersion(info.getServiceDescriptor().getIntroduceVersion());
				}
			}
		}

		// replace the old with the new
		service.getServiceContextCollection().setServiceContext(newServContexts);
	}


	/**
	 * TODO: should we move this to the annotateServiceMetadata call?
	 * 
	 * @param service
	 * @param info
	 */
	private void editServiceSemanticMetadata(Service service, ServiceInformation info) {
		// determine if its a data service or analytical service
		// TODO: how are we classifying data services that add other operations?
		// (just data or data and analytical)
		boolean isDataService = isDataService(info);
		LOG.debug("Service " + (isDataService ? "is" : "is not") + " a data service.");
		// deserialize the codes based on the type
		InputStream inputStream = null;
		if (isDataService) {
			inputStream = ClassUtils.getResourceAsStream(getClass(), SEMANTIC_METADATA_DEFAULTS_DATA_SERVICE);
		} else {
			inputStream = ClassUtils.getResourceAsStream(getClass(), SEMANTIC_METADATA_DEFAULTS_ANALYTICAL_SERVICE);
		}

		try {
			// load the appropriate template
			InputStreamReader reader = new InputStreamReader(inputStream);
			ServiceMetadata metadata = MetadataUtils.deserializeServiceMetadata(reader);
			reader.close();

			// set the codes
			service.setSemanticMetadata(metadata.getServiceDescription().getService().getSemanticMetadata());
		} catch (Exception e) {
			LOG.error("Problem setting service semantic metdata; skipping!", e);
		}
	}


	/**
	 * @param info
	 * @return
	 */
	private boolean isDataService(ServiceInformation info) {
		ServicesType services = info.getServices();
		if (services != null && services.getService() != null) {
			ServiceType mainService = null;
			// find the main service
			for (int i = 0; i < services.getService().length; i++) {
				ServiceType serv = services.getService(i);
				if (serv.getResourceFrameworkType().equals(MAIN_RF_TYPE)) {
					mainService = serv;
					break;
				}
			}
			if (mainService != null) {
				MethodsType methods = mainService.getMethods();
				if (methods != null) {
					MethodType[] methodArr = methods.getMethod();
					if (methodArr != null) {
						// walk its methods
						for (int i = 0; i < methodArr.length; i++) {
							MethodType method = methodArr[i];
							// if the method name is data service query
							if (method.getName().equals(DataServiceConstants.QUERY_METHOD_NAME)) {
								MethodTypeInputs inputs = method.getInputs();
								if (inputs != null && inputs.getInput().length == 1) {
									// if it has the right input
									if (inputs.getInput(0).getQName().equals(DataServiceConstants.CQL_QUERY_QNAME)) {
										// if it has the right output
										if (method.getOutput() != null
											&& method.getOutput().getQName().equals(
												DataServiceConstants.CQL_RESULT_COLLECTION_QNAME)) {
											return true;
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}


	private void editServiceContext(ServiceType service, ServiceContext serviceContext) {
		serviceContext.setName(service.getName());

		// set a description (for xsd validation reasons)
		if (serviceContext.getDescription() == null || serviceContext.getDescription().trim().equals("")) {
			serviceContext.setDescription("");
		}

		// make context properties for RPs
		editServiceContextProperties(service.getResourcePropertiesList().getResourceProperty(), serviceContext);

		// make operations
		editOperations(service.getMethods().getMethod(), serviceContext);

	}


	private void editOperations(MethodType methods[], ServiceContext serviceContext) {
		if (methods == null || methods.length == 0) {
			serviceContext.setOperationCollection(new ServiceContextOperationCollection());
			return;
		}

		Operation[] newOperations = new Operation[methods.length];

		if (serviceContext.getOperationCollection() == null) {
			serviceContext.setOperationCollection(new ServiceContextOperationCollection());
		}

		// build a map based on operation names (only for lookup)
		Map opMap = new HashMap();
		Operation[] existingOperations = serviceContext.getOperationCollection().getOperation();
		if (existingOperations != null) {
			for (int i = 0; i < existingOperations.length; i++) {
				Operation op = existingOperations[i];
				opMap.put(op.getName(), op);
			}
		}

		for (int i = 0; i < methods.length; i++) {
			MethodType method = methods[i];
			// find the existing op
			Operation currOp = (Operation) opMap.get(method.getName());
			// create a new one if necessary
			if (currOp == null) {
				currOp = new Operation();
			}

			// edit the operation
			editOperation(method, currOp);
			newOperations[i] = currOp;

		}
		serviceContext.getOperationCollection().setOperation(newOperations);

	}


	private void editOperation(MethodType method, Operation operation) {
		operation.setName(method.getName());
		// set a description (for xsd validation reasons)
		if (operation.getDescription() == null || operation.getDescription().trim().equals("")) {
			operation.setDescription("");
		}

		// OUTPUT
		MethodTypeOutput methOut = method.getOutput();
		if (methOut.getQName().toString().equals("void")) {
			operation.setOutput(null);
		} else {
			// we create a new one, because we don't currently make the UML
			// info, and can't know if its up to date or not
			Output output = new Output();
			output.setIsArray(methOut.isIsArray());
			// this is here for expansion, but we only support 1 dim arrays
			output.setDimensionality(1);
			output.setQName(methOut.getQName());
			operation.setOutput(output);
		}

		// FAULTS
		if (method.getExceptions() == null) {
			editFaults(null, operation);
		} else {
			editFaults(method.getExceptions().getException(), operation);
		}

		// INPUTS
		if (method.getInputs() == null) {
			editOperationInputs(null, operation);
		} else {
			editOperationInputs(method.getInputs().getInput(), operation);
		}

		// SEMANTIC METADATA
		if (operation.getSemanticMetadata() == null) {
			operation.setSemanticMetadata(new SemanticMetadata[]{});
		}

	}


	private void editOperationInputs(MethodTypeInputsInput[] inputs, Operation operation) {
		if (inputs == null || inputs.length == 0) {
			operation.setInputParameterCollection(new OperationInputParameterCollection());
			return;
		}

		InputParameter[] newInputs = new InputParameter[inputs.length];
		if (operation.getInputParameterCollection() == null) {
			operation.setInputParameterCollection(new OperationInputParameterCollection());
		}

		// build a map based on fault names (only for lookup)
		Map inputMap = new HashMap();
		InputParameter[] existingInputs = operation.getInputParameterCollection().getInputParameter();
		if (existingInputs != null) {
			for (int i = 0; i < existingInputs.length; i++) {
				InputParameter input = existingInputs[i];
				inputMap.put(input.getName(), input);
			}
		}

		// for each input, build/edit the param
		for (int i = 0; i < inputs.length; i++) {
			MethodTypeInputsInput input = inputs[i];
			String name = input.getName();

			// find the existing
			InputParameter param = (InputParameter) inputMap.get(name);
			// create a new one if necessary
			if (param == null) {
				param = new InputParameter();
			}
			param.setName(name);
			// we only support 1 dim arrays
			param.setDimensionality(1);
			param.setIndex(i);
			param.setIsArray(input.isIsArray());
			// param.setIsRequired(???)
			param.setQName(input.getQName());

			// clear this out, because we don't set it, and don't know if its
			// still valid
			param.setUMLClass(null);
			newInputs[i] = param;
		}

		operation.getInputParameterCollection().setInputParameter(newInputs);
	}


	private void editFaults(MethodTypeExceptionsException[] exceptions, Operation operation) {
		if (exceptions == null || exceptions.length == 0) {
			operation.setFaultCollection(new OperationFaultCollection());
			return;
		}

		if (operation.getFaultCollection() == null) {
			operation.setFaultCollection(new OperationFaultCollection());
		}
		Fault[] newFaults = new Fault[exceptions.length];

		// build a map based on fault names (only for lookup)
		Map faultMap = new HashMap();
		Fault[] existingFaults = operation.getFaultCollection().getFault();
		if (existingFaults != null) {
			for (int i = 0; i < existingFaults.length; i++) {
				Fault fault = existingFaults[i];
				faultMap.put(fault.getName(), fault);
			}
		}

		// for each exception, build/edit the fault

		for (int i = 0; i < exceptions.length; i++) {
			MethodTypeExceptionsException exception = exceptions[i];
			String name = exception.getName();

			// find the existing context
			Fault currFault = (Fault) faultMap.get(name);
			// create a new one if necessary
			if (currFault == null) {
				currFault = new Fault();
			}
			currFault.setName(name);
			// set a description (for xsd validation reasons)
			if (currFault.getDescription() == null || currFault.getDescription().trim().equals("")) {
				currFault.setDescription("");
			}
			newFaults[i] = currFault;
		}

		operation.getFaultCollection().setFault(newFaults);
	}


	private void editServiceContextProperties(ResourcePropertyType[] resourcePropertys, ServiceContext serviceContext) {
		if (resourcePropertys == null || resourcePropertys.length == 0) {
			serviceContext.setContextPropertyCollection(new ServiceContextContextPropertyCollection());
			return;
		}

		ContextProperty[] newProps = new ContextProperty[resourcePropertys.length];

		if (serviceContext.getContextPropertyCollection() == null) {
			serviceContext.setContextPropertyCollection(new ServiceContextContextPropertyCollection());
		}

		// build a map based on rps names (only for lookup)
		Map propMap = new HashMap();
		ContextProperty[] existingProps = serviceContext.getContextPropertyCollection().getContextProperty();
		if (existingProps != null) {
			for (int i = 0; i < existingProps.length; i++) {
				ContextProperty prop = existingProps[i];
				propMap.put(prop.getName(), prop);
			}
		}

		// for each RP, build/edit the contextprop

		for (int i = 0; i < resourcePropertys.length; i++) {
			ResourcePropertyType rp = resourcePropertys[i];
			QName name = rp.getQName();

			// find the existing context
			ContextProperty currProp = (ContextProperty) propMap.get(name.toString());
			// create a new one if necessary
			if (currProp == null) {
				currProp = new ContextProperty();
			}
			currProp.setName(name.toString());
			// set a description (for xsd validation reasons)
			if (currProp.getDescription() == null || currProp.getDescription().trim().equals("")) {
				currProp.setDescription("");
			}

			newProps[i] = currProp;

		}
		serviceContext.getContextPropertyCollection().setContextProperty(newProps);
	}


	/**
	 * bootstrap the necessary fields as needed, to avoid null checks
	 * everywhere.
	 * 
	 * @param metadata
	 */
	private static void initializeModel(ServiceMetadata metadata) {
		// every model needs a service desc
		ServiceMetadataServiceDescription desc = metadata.getServiceDescription();
		if (desc == null) {
			desc = new ServiceMetadataServiceDescription();
			metadata.setServiceDescription(desc);
		}

		// every model needs a hosting center (container)
		ServiceMetadataHostingResearchCenter hostingResearchCenter = metadata.getHostingResearchCenter();
		if (hostingResearchCenter == null) {
			hostingResearchCenter = new ServiceMetadataHostingResearchCenter();
			metadata.setHostingResearchCenter(hostingResearchCenter);
		}

		// every service desc needs a service
		Service serv = desc.getService();
		if (serv == null) {
			serv = new Service();
			desc.setService(serv);
		}

		// every service needs a context collection
		ServicePointOfContactCollection pointOfContactCollection = serv.getPointOfContactCollection();
		if (pointOfContactCollection == null) {
			pointOfContactCollection = new ServicePointOfContactCollection();
			serv.setPointOfContactCollection(pointOfContactCollection);
		}

		// every service needs a context coll
		ServiceServiceContextCollection contCol = serv.getServiceContextCollection();
		if (contCol == null) {
			contCol = new ServiceServiceContextCollection();
			serv.setServiceContextCollection(contCol);
		}
	}
}
