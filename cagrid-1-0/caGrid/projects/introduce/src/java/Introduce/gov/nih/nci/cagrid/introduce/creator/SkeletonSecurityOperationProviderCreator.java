package gov.nih.nci.cagrid.introduce.creator;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeImportInformation;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeProviderInformation;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.info.SpecificServiceInformation;

import java.io.File;

import javax.xml.namespace.QName;


/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 */
public class SkeletonSecurityOperationProviderCreator {

	private static final String SERVICE_SECURITY_WSDL = "ServiceSecurity.wsdl";
	private static final String SERVICE_SECURITY_XSD = "security.xsd";
	private static final String SECURITY_SERVICE_NS = "http://security.introduce.cagrid.nci.nih.gov/ServiceSecurity";
	private static final String SECURITY_SERVICE_PACKAGE = "gov.nih.nci.cagrid.introduce.security.stubs";
	private static final String SECURITY_NS = "gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security";
	private static final String PATH_TO_WSDL = "ext" + File.separator + "xsd";
	private static final String PATH_TO_SCHEMA = "ext" + File.separator + "xsd" + File.separator + "cagrid" + File.separator + "types" + File.separator + "security";

	
	public SkeletonSecurityOperationProviderCreator() {
	}


	public void createSkeleton(SpecificServiceInformation info) throws Exception {
		boolean needToAdd = true;
		if (info.getService().getMethods() != null && info.getService().getMethods().getMethod() != null) {
			MethodType[] methods = info.getService().getMethods().getMethod();
			for (int i = 0; i < methods.length; i++) {
				MethodType method = methods[i];
				if (method.getName().equals("getServiceSecurityMetadata")) {
					needToAdd = false;
				}
			}
		}

		if (needToAdd) {
			// add in the method
			MethodType method = new MethodType();
			method.setName("getServiceSecurityMetadata");

			MethodTypeOutput output = new MethodTypeOutput();
			output.setQName(new QName(SECURITY_NS, "ServiceSecurityMetadata"));
			output.setIsArray(false);
			output.setIsClientHandle(new Boolean(false));
			method.setOutput(output);

			MethodTypeImportInformation ii = new MethodTypeImportInformation();
			ii.setNamespace(SECURITY_SERVICE_NS);
			ii.setPackageName(SECURITY_SERVICE_PACKAGE);
			ii.setPortTypeName("ServiceSecurityPortType");
			ii.setWsdlFile(SERVICE_SECURITY_WSDL);
			ii.setInputMessage(new QName(SECURITY_SERVICE_NS, "GetServiceSecurityMetadataRequest"));
			ii.setOutputMessage(new QName(SECURITY_SERVICE_NS, "GetServiceSecurityMetadataResponse"));
			method.setIsImported(true);
			method.setImportInformation(ii);

			MethodTypeProviderInformation pi = new MethodTypeProviderInformation();
			pi.setProviderClass("gov.nih.nci.cagrid.introduce.security.service.globus.ServiceSecurityProviderImpl");
			method.setIsProvided(true);
			method.setProviderInformation(pi);

			CommonTools.addMethod(info.getService(), method);

			if (CommonTools.getNamespaceType(info.getNamespaces(), SECURITY_NS) == null) {

				String pathToServSchema = info.getBaseDirectory().getAbsolutePath()
					+ File.separator
					+ "schema"
					+ File.separator
					+ info.getIntroduceServiceProperties().getProperty(
						IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME) + File.separator;

				// add in the namespace type
				NamespaceType nsType = CommonTools.createNamespaceType(PATH_TO_SCHEMA + File.separator
					+ SERVICE_SECURITY_XSD);
				nsType.setGenerateStubs(new Boolean(false));
				nsType.setPackageName("gov.nih.nci.cagrid.metadata.security");
				nsType.setLocation("./xsd/" + SERVICE_SECURITY_XSD);
				CommonTools.addNamespace(info.getServiceDescriptor(), nsType);
				
				//should i set the messages to not be generated again....
				//if so add the ns to the excludes list...

				// copy over the wsdl file and the required schema
				Utils.copyFile(new File(PATH_TO_WSDL + File.separator + SERVICE_SECURITY_WSDL), new File(pathToServSchema
					+ SERVICE_SECURITY_WSDL));
				Utils.copyFile(new File(PATH_TO_SCHEMA + File.separator + SERVICE_SECURITY_XSD), new File(
					pathToServSchema + "xsd" + File.separator + SERVICE_SECURITY_XSD));

			}
		}
	}

}