package gov.nih.nci.cagrid.introduce.creator;

import java.io.File;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeImportInformation;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeProviderInformation;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.namespace.SchemaElementType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.info.SpecificServiceInformation;

import javax.xml.namespace.QName;

import org.apache.tools.ant.BuildException;

/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * 
 */
public class SkeletonSecurityOperationProviderCreator {

	public SkeletonSecurityOperationProviderCreator() {
	}

	public void createSkeleton(SpecificServiceInformation info)
			throws Exception {
		ServiceType service = info.getService();

		// add in the method
		MethodType method = new MethodType();
		method.setName("getServiceSecurityMetadata");

		MethodTypeOutput output = new MethodTypeOutput();
		output.setQName(new QName(
				"gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security",
				"ServiceSecurityMetadata"));
		output.setIsArray(false);
		output.setIsClientHandle(new Boolean(false));
		method.setOutput(output);

		MethodTypeImportInformation ii = new MethodTypeImportInformation();
		ii
				.setNamespace("http://security.introduce.cagrid.nci.nih.gov/ServiceSecurity");
		ii.setPackageName("gov.nih.nci.cagrid.introduce.security");
		ii.setPortTypeName("ServiceSecurityPortType");
		ii.setWsdlFile("ServiceSecurity.wsdl");
		method.setIsImported(true);
		method.setImportInformation(ii);

		MethodTypeProviderInformation pi = new MethodTypeProviderInformation();
		pi
				.setProviderClass("gov.nih.nci.cagrid.introduce.security.service.ServiceSecurityImpl");
		method.setIsProvided(true);
		method.setProviderInformation(pi);

		CommonTools.addMethod(info.getService(), method);

		if (CommonTools.getNamespaceType(info.getNamespaces(),
				"gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security") == null) {
			// add in the namespace type
			NamespaceType nsType = new NamespaceType();
			nsType.setGenerateStubs(new Boolean(false));
			nsType.setLocation("./ServiceSecurity.xsd");
			nsType
					.setNamespace("gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security");
			nsType.setPackageName("gov.nih.nci.cagrid.metadata.security");
			SchemaElementType[] schemaElements = new SchemaElementType[4];
			SchemaElementType one = new SchemaElementType();
			one.setType("ServiceSecurityMetadata");
			SchemaElementType two = new SchemaElementType();
			two.setType("CommunicationMechanism");
			SchemaElementType three = new SchemaElementType();
			three.setType("Operation");
			SchemaElementType four = new SchemaElementType();
			four.setType("CommunicationStyle");
			schemaElements[0] = one;
			schemaElements[1] = two;
			schemaElements[2] = three;
			schemaElements[3] = four;
			nsType.setSchemaElement(schemaElements);

			CommonTools.addNamespace(info.getServiceDescriptor(), nsType);
			

			// copy over the wsdl file and the required schema
			Utils.copyFile(new File("operationProviders" + File.separator
					+ "ServiceSecurity" + File.separator + "schema"
					+ File.separator + "ServiceSecurity" + File.separator
					+ "ServiceSecurity.wsdl"), new File(info.getBaseDirectory()
					.getAbsolutePath()
					+ File.separator
					+ "schema"
					+ File.separator
					+ info.getIntroduceServiceProperties().getProperty(
							IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME)
					+ File.separator + "ServiceSecurity.wsdl"));
			Utils.copyFile(new File("operationProviders" + File.separator
					+ "ServiceSecurity" + File.separator + "schema"
					+ File.separator + "ServiceSecurity" + File.separator
					+ "ServiceSecurity.xsd"), new File(info.getBaseDirectory()
					.getAbsolutePath()
					+ File.separator
					+ "schema"
					+ File.separator
					+ info.getIntroduceServiceProperties().getProperty(
							IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME)
					+ File.separator + "ServiceSecurity.xsd"));

			// copy over the jars which contain the stubs and the service impl
			Utils.copyFile(new File("operationProviders" + File.separator
					+ "ServiceSecurity" + File.separator + "build"
					+ File.separator + "lib" + File.separator
					+ "ServiceSecurity_stubs.jar"), new File(info.getBaseDirectory()
					.getAbsolutePath()
					+ File.separator
					+ "lib"
					+ File.separator + "ServiceSecurity_stubs.jar"));
			Utils.copyFile(new File("operationProviders" + File.separator
					+ "ServiceSecurity" + File.separator + "build"
					+ File.separator + "lib" + File.separator
					+ "ServiceSecurity-common.jar"), new File(info.getBaseDirectory()
					.getAbsolutePath()
					+ File.separator
					+ "lib"
					+ File.separator + "ServiceSecurity-common.jar"));
			Utils.copyFile(new File("operationProviders" + File.separator
					+ "ServiceSecurity" + File.separator + "build"
					+ File.separator + "lib" + File.separator
					+ "ServiceSecurity-client.jar"), new File(info.getBaseDirectory()
					.getAbsolutePath()
					+ File.separator
					+ "lib"
					+ File.separator + "ServiceSecurity-client.jar"));
			Utils.copyFile(new File("operationProviders" + File.separator
					+ "ServiceSecurity" + File.separator + "build"
					+ File.separator + "lib" + File.separator
					+ "ServiceSecurity-service.jar"), new File(info.getBaseDirectory()
					.getAbsolutePath()
					+ File.separator
					+ "lib"
					+ File.separator + "ServiceSecurity-service.jar"));
			
		}
		// write the modified document back out....
		try {
			Utils.serializeDocument(info.getBaseDirectory()+ File.separator + "introduce.xml", info.getServiceDescriptor(), new QName(
				"gme://gov.nih.nci.cagrid/1/Introduce", "ServiceDescription"));
		} catch (Exception e1) {
			BuildException be = new BuildException(e1.getMessage());
			be.setStackTrace(e1.getStackTrace());
			be.printStackTrace();
			throw be;
		}
	}

}