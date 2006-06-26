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
import java.io.FileFilter;

import javax.xml.namespace.QName;

import org.apache.tools.ant.BuildException;


/**
 * @author <A HREF="MAILTO:hastings@bmi.osu.edu">Shannon Hastings </A>
 * @author <A HREF="MAILTO:oster@bmi.osu.edu">Scott Oster </A>
 * @author <A HREF="MAILTO:langella@bmi.osu.edu">Stephen Langella </A>
 * 
 */
public class SkeletonSecurityOperationProviderCreator {

	private static final String JAR_PREFIX = "caGrid-introduce-security";
	private static final String SERVICE_SECURITY_WSDL = "ServiceSecurity.wsdl";
	private static final String SERVICE_SECURITY_XSD = "security.xsd";
	private static final String SECURITY_SERVICE_NS = "http://security.introduce.cagrid.nci.nih.gov/ServiceSecurity";
	private static final String SECURITY_NS = "gme://caGrid.caBIG/1.0/gov.nih.nci.cagrid.metadata.security";
	private static final String PATH_TO_BUILD_LIB = "operationProviders" + File.separator + "ServiceSecurity"
		+ File.separator + "build" + File.separator + "lib" + File.separator;
	private static final String PATH_TO_SCHEMA = "operationProviders" + File.separator + "ServiceSecurity"
		+ File.separator + "schema" + File.separator + "ServiceSecurity" + File.separator;


	public SkeletonSecurityOperationProviderCreator() {
	}


	public void createSkeleton(SpecificServiceInformation info) throws Exception {

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
		ii.setPackageName("gov.nih.nci.cagrid.introduce.security");
		ii.setPortTypeName("ServiceSecurityPortType");
		ii.setWsdlFile(SERVICE_SECURITY_WSDL);
		method.setIsImported(true);
		method.setImportInformation(ii);

		MethodTypeProviderInformation pi = new MethodTypeProviderInformation();
		pi.setProviderClass("gov.nih.nci.cagrid.introduce.security.service.globus.ServiceSecurityProviderImpl");
		method.setIsProvided(true);
		method.setProviderInformation(pi);

		CommonTools.addMethod(info.getService(), method);

		if (CommonTools.getNamespaceType(info.getNamespaces(), SECURITY_NS) == null) {

			String pathToServSchema = info.getBaseDirectory().getAbsolutePath() + File.separator + "schema"
				+ File.separator
				+ info.getIntroduceServiceProperties().getProperty(IntroduceConstants.INTRODUCE_SKELETON_SERVICE_NAME)
				+ File.separator;
			String pathToSerLib = info.getBaseDirectory().getAbsolutePath() + File.separator + "lib" + File.separator;

			// add in the namespace type
			NamespaceType nsType = CommonTools.createNamespaceType(PATH_TO_SCHEMA + "xsd" + File.separator
				+ SERVICE_SECURITY_XSD);
			nsType.setGenerateStubs(new Boolean(false));
			nsType.setLocation("./" + "xsd" + File.separator + SERVICE_SECURITY_XSD);
			CommonTools.addNamespace(info.getServiceDescriptor(), nsType);

			// copy over the wsdl file and the required schema
			new File(pathToServSchema + "xsd").mkdirs();
			Utils.copyFile(new File(PATH_TO_SCHEMA + SERVICE_SECURITY_WSDL), new File(pathToServSchema
				+ SERVICE_SECURITY_WSDL));
			Utils.copyFile(new File(PATH_TO_SCHEMA + "xsd" + File.separator + SERVICE_SECURITY_XSD), new File(
				pathToServSchema + "xsd" + File.separator + SERVICE_SECURITY_XSD));

			// copy over the jars which contain the stubs and the service impl
			File libDir = new File(PATH_TO_BUILD_LIB);
			File[] libs = libDir.listFiles(new FileFilter() {
				public boolean accept(File pathname) {
					String name = pathname.getName();
					return (name.endsWith(".jar") && (name.startsWith(JAR_PREFIX)));
				}
			});
			if (libs != null) {
				for (int i = 0; i < libs.length; i++) {
					File outFile = new File(pathToSerLib + libs[i].getName());
					Utils.copyFile(libs[i], outFile);
				}
			}
		}
		// write the modified document back out
		try {
			Utils.serializeDocument(info.getBaseDirectory() + File.separator + "introduce.xml", info
				.getServiceDescriptor(), new QName("gme://gov.nih.nci.cagrid/1/Introduce", "ServiceDescription"));
		} catch (Exception e1) {
			BuildException be = new BuildException(e1.getMessage());
			be.setStackTrace(e1.getStackTrace());
			be.printStackTrace();
			throw be;
		}
	}

}