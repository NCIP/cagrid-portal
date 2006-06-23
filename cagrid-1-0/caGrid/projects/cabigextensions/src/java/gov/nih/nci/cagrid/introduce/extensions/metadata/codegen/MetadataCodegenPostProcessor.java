package gov.nih.nci.cagrid.introduce.extensions.metadata.codegen;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.beans.extension.ServiceExtensionDescriptionType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeExceptionsException;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeInputsInput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.resource.ResourcePropertyType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionException;
import gov.nih.nci.cagrid.introduce.extension.CodegenExtensionPostProcessor;
import gov.nih.nci.cagrid.introduce.extensions.metadata.constants.MetadataConstants;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.ServiceMetadataServiceDescription;
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
import gov.nih.nci.cagrid.metadata.service.ServiceServiceContextCollection;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

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
	private static final String FILENAME = "serviceMetadata.xml";
	private static final String MAIN_RF_TYPE = "main";

	protected static Log LOG = LogFactory.getLog(MetadataCodegenPostProcessor.class.getName());


	public void postCodegen(ServiceExtensionDescriptionType desc, ServiceInformation info)
		throws CodegenExtensionException {
		ServiceMetadata metadata = null;

		// read jndi file to determine where to store instance document? or just
		// hard code it to etc/serviceMetadata.xml?
		String filename = info.getBaseDirectory() + File.separator + "etc" + File.separator + FILENAME;

		// look if the file already exists, and load it in, in case other
		// aspects of it (such as cancer center info) are set by something else
		File mdFile = new File(filename);
		if (mdFile.exists() && mdFile.canRead()) {
			try {
				metadata = (ServiceMetadata) Utils.deserializeDocument(filename, ServiceMetadata.class);
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
			Utils.serializeDocument(filename, metadata, MetadataConstants.SERVICE_METADATA_QNAME);
		} catch (Exception e) {
			throw new CodegenExtensionException("Error serializing metadata document.", e);
		}

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


	private void editServiceContext(ServiceType service, ServiceContext serviceContext) {
		serviceContext.setName(service.getName());

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
			output.setDimentionality(1);
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
			param.setDimentionality(1);
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

		// every service desc needs a service
		Service serv = desc.getService();
		if (serv == null) {
			serv = new Service();
			desc.setService(serv);
		}

		// every service needs a context coll
		ServiceServiceContextCollection contCol = serv.getServiceContextCollection();
		if (contCol == null) {
			contCol = new ServiceServiceContextCollection();
			serv.setServiceContextCollection(contCol);
		}
	}
}
