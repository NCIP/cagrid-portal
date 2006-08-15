package gov.nih.nci.cagrid.introduce.creator;

import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.codegen.utils.TemplateUtils;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;
import gov.nih.nci.cagrid.introduce.info.SpecificServiceInformation;
import gov.nih.nci.cagrid.introduce.templates.schema.service.ServiceWSDLTemplate;
import gov.nih.nci.cagrid.introduce.templates.schema.service.ServiceXSDTemplate;

import java.io.File;
import java.io.FileWriter;

import sun.security.action.GetBooleanAction;

/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * 
 */
public class SkeletonSchemaCreator {

	public SkeletonSchemaCreator() {
	}


	public void createSkeleton(File baseDirectory, ServiceInformation info, ServiceType service) throws Exception {
		File schemaDir = new File(baseDirectory.getAbsolutePath() + File.separator + "schema");
		schemaDir.mkdir();

		new File(schemaDir.getAbsolutePath() + File.separator
			+ info.getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME)).mkdirs();

		ServiceWSDLTemplate serviceWSDLT = new ServiceWSDLTemplate();
		String serviceWSDLS = serviceWSDLT.generate(new SpecificServiceInformation(info,service));
		File serviceWSDLF = new File(schemaDir.getAbsolutePath() + File.separator
			+ info.getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME) + File.separator
			+ service.getName() + ".wsdl");
		FileWriter serviceWSDLFW = new FileWriter(serviceWSDLF);
		serviceWSDLFW.write(serviceWSDLS);
		serviceWSDLFW.close();
		
		ServiceXSDTemplate serviceXSDT = new ServiceXSDTemplate();
		String serviceXSDS = serviceXSDT.generate(new SpecificServiceInformation(info,service));
		File serviceXSDF = new File(schemaDir.getAbsolutePath() + File.separator
			+ info.getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME) + File.separator
			+ service.getName() + "Reference.xsd");
		FileWriter serviceXSDFW = new FileWriter(serviceXSDF);
		serviceXSDFW.write(serviceXSDS);
		serviceXSDFW.close();
		
		//add the new service xsd to the namespace list.
		NamespaceType newServiceReferenceSchema = CommonTools.createNamespaceType(info.getBaseDirectory() + File.separator + "schema" + File.separator + info.getServices().getService(0).getName() + File.separator + service.getName() + "Reference.xsd");
		CommonTools.addNamespace(info.getServiceDescriptor(),newServiceReferenceSchema);
		
		
	}

}