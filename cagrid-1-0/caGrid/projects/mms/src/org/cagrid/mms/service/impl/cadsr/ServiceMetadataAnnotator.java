package org.cagrid.mms.service.impl.cadsr;

import gov.nih.nci.cadsr.domain.ClassificationScheme;
import gov.nih.nci.cadsr.domain.Context;
import gov.nih.nci.cadsr.umlproject.domain.Project;
import gov.nih.nci.cadsr.umlproject.domain.UMLClassMetadata;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ServiceMetadata;
import gov.nih.nci.cagrid.metadata.common.SemanticMetadata;
import gov.nih.nci.cagrid.metadata.common.UMLClass;
import gov.nih.nci.cagrid.metadata.service.CaDSRRegistration;
import gov.nih.nci.cagrid.metadata.service.InputParameter;
import gov.nih.nci.cagrid.metadata.service.Operation;
import gov.nih.nci.cagrid.metadata.service.Service;
import gov.nih.nci.cagrid.metadata.service.ServiceContext;
import gov.nih.nci.system.applicationservice.ApplicationException;
import gov.nih.nci.system.applicationservice.ApplicationService;
import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * @author oster
 * 
 */
public class ServiceMetadataAnnotator {
	protected static Log LOG = LogFactory.getLog(ServiceMetadataAnnotator.class.getName());
	private ApplicationService cadsr = null;
//	private String uml2xmlImplClassname = UML2XMLBindingNamingConventionImpl.class.getName();


	public ServiceMetadataAnnotator(ApplicationService cadsr) {
		this.cadsr = cadsr;
	}


	//TODO: deal with Project lookups and namespacetoproject mappings
//	public ServiceMetadataAnnotator(ApplicationService cadsr, String uml2xmlImplClassname) {
//		this.cadsr = cadsr;
//		this.uml2xmlImplClassname = uml2xmlImplClassname;
//	}


	/**
	 * Add caDSR information to model.
	 * 
	 * @param metadata
	 * @throws CaDSRGeneralException
	 */
	public void annotateServiceMetadata(ServiceMetadata metadata) throws CaDSRGeneralException {
		if (metadata == null || metadata.getServiceDescription() == null
			|| metadata.getServiceDescription().getService() == null) {
			return;
		}

		Service service = metadata.getServiceDescription().getService();

		// TODO: how to set caDSR registration?
		CaDSRRegistration caDSRRegistration = service.getCaDSRRegistration();

		// TODO: set/edit service semantic metadata once service's are
		// registered in caDSR
		SemanticMetadata[] semanticMetadatas = service.getSemanticMetadata();

		if (service.getServiceContextCollection() == null
			|| service.getServiceContextCollection().getServiceContext() == null) {
			return;
		}
		ServiceContext[] serviceContexts = service.getServiceContextCollection().getServiceContext();
		for (int i = 0; i < serviceContexts.length; i++) {
			annotateServiceContext(serviceContexts[i]);
		}

	}


	protected void annotateServiceContext(ServiceContext context) throws CaDSRGeneralException {
		if (context == null || context.getOperationCollection() == null
			|| context.getOperationCollection().getOperation() == null) {
			return;
		}
		Operation[] operations = context.getOperationCollection().getOperation();
		for (int i = 0; i < operations.length; i++) {
			annotateOperation(operations[i]);
		}

	}


	protected void annotateOperation(Operation operation) throws CaDSRGeneralException {
		if (operation == null) {
			return;
		}

		// TODO: set/edit operation semantic metadata once services are
		// registered in caDSR
		SemanticMetadata[] semanticMetadatas = operation.getSemanticMetadata();

		// process input
		if (operation.getInputParameterCollection() != null
			&& operation.getInputParameterCollection().getInputParameter() != null) {
			InputParameter[] inputParameters = operation.getInputParameterCollection().getInputParameter();
			for (int i = 0; i < inputParameters.length; i++) {
				InputParameter in = inputParameters[i];
				QName qname = in.getQName();
				UMLClass uml = getUMLClassForQName(qname);
				if (uml != null) {
					LOG.debug("Successfully processed:" + qname);
					in.setUMLClass(uml);
				}

			}
		}

		// process output
		if (operation.getOutput() != null) {
			QName qname = operation.getOutput().getQName();
			UMLClass uml = getUMLClassForQName(qname);
			if (uml != null) {
				LOG.debug("Successfully processed:" + qname);
				operation.getOutput().setUMLClass(uml);
			}
		}
	}


	/**
	 * @param qname
	 * @return
     *      The UML Class matching the QName
	 * @throws CaDSRGeneralException
	 */
	protected UMLClass getUMLClassForQName(QName qname) throws CaDSRGeneralException {
		// look up the UMLClassMetadata we are looking for, based on the QName
		UMLClassMetadata classMetadata = getUMLClassMetadataForQName(qname);
		if (classMetadata == null) {
			return null;
		}

		UMLClass result = null;
		try {
			String shortName = classMetadata.getProject().getShortName();
			String version = classMetadata.getProject().getVersion();
			result = CaDSRUtils.convertClassToUMLClass(cadsr, shortName, version, classMetadata);
		} catch (ApplicationException e) {
			LOG.error("Problem converting class to metadata", e);
		}
		return result;
	}




	/**
	 * 
	 * @param qname
	 * @return
     *      The UMLClassMetadata matching the qname
	 * @throws CaDSRGeneralException
	 */
	protected UMLClassMetadata getUMLClassMetadataForQName(QName qname) throws CaDSRGeneralException {
		UMLClassMetadata prototype = new UMLClassMetadata();
//		UML2XMLBinding binding = getUML2XMLBinding();

//		try {
//			prototype.setName(binding.determineClassName(qname));
//			prototype.setFullyQualifiedName(binding.determinePackageName(qname) + "."
//				+ binding.determineClassName(qname));
//
//			Project proj = new Project();
//			proj.setShortName(binding.determineCaDSRProjectShortName(qname));
//			String version = binding.determineCaDSRProjectVersion(qname);
//			// TODO: caDSR seems to have 3 as version instead of 3.0, so need to
//			// fix that here what is "right"?
//			if (version.endsWith(".0")) {
//				version = version.substring(0, version.length() - 2);
//			}
//			proj.setVersion(version);
//			ClassificationScheme cs = new ClassificationScheme();
//			Context ctx = new Context();
//			ctx.setName(binding.determineCaDSRContext(qname));
//			cs.setContext(ctx);
//			proj.setClassificationScheme(cs);
//
//			prototype.setProject(proj);
//
//		} catch (CaDSRGeneralException e) {
//			LOG.error("Unable to process QName (" + qname + "); skipping because:" + e.getMessage(), e);
//			return null;
//		}
		List rList = null;
		try {
			rList = this.cadsr.search(UMLClassMetadata.class, prototype);
		} catch (ApplicationException e) {
			LOG.error(
				"Unable to locate UMLClassMetadata for QName (" + qname + "); skipping because:" + e.getMessage(), e);
			return null;
		}

		if (rList == null || rList.size() == 0) {
			LOG.error("Unable to locate UMLClassMetadata for QName (" + qname
				+ "); skipping because no results were returned from ApplicationService.");
			return null;
		}

		if (rList.size() > 1) {
			LOG.info("Processing of UMLClassMetadata for QName (" + qname
				+ ") returned more than 1 result, using first.");
		}

		return (UMLClassMetadata) rList.get(0);
	}


	public static void main(String[] args) {
		try {
			JFrame f = new JFrame();
			f.setVisible(true);

			JFileChooser fc = new JFileChooser(".");
			fc.showOpenDialog(f);

			File selectedFile = fc.getSelectedFile();
			ServiceMetadata model = MetadataUtils.deserializeServiceMetadata(new FileReader(selectedFile));
			ApplicationService appService = ApplicationServiceProvider
				.getApplicationServiceFromUrl("http://cadsrapi-prod2.nci.nih.gov/cadsrapi40/");
			ServiceMetadataAnnotator anno = new ServiceMetadataAnnotator(appService);
			anno.annotateServiceMetadata(model);

			File result = new File(".", selectedFile.getName() + "_annotated");
			MetadataUtils.serializeServiceMetadata(model, new FileWriter(result));
			System.exit(0);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
}