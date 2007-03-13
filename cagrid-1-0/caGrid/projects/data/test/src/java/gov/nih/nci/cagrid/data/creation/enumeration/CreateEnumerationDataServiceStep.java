package gov.nih.nci.cagrid.data.creation.enumeration;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.creation.WsEnumerationFeatureCreator;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.data.extension.ServiceFeatures;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.extension.ExtensionsLoader;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/** 
 *  CreateEnumerationDataServiceStep
 *  Creates a new caGrid Data Service with WS-Enumeration support enabled
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 30, 2006 
 * @version $Id: CreateEnumerationDataServiceStep.java,v 1.2 2007-03-13 19:27:13 dervin Exp $ 
 */
public class CreateEnumerationDataServiceStep extends Step {
	
	private String introduceBaseDir = null;
	
	public CreateEnumerationDataServiceStep(String introduceDir) {
		this.introduceBaseDir = introduceDir;
	}
	

	public void runStep() throws Throwable {
		System.out.println("Running step: " + getClass().getName());
		
		System.out.println("Initial creation of data service...");
		String cmd = CommonTools.getAntSkeletonCreationCommand(introduceBaseDir,
			CreateEnumerationTests.SERVICE_NAME, CreateEnumerationTests.SERVICE_DIR,
			CreateEnumerationTests.PACKAGE_NAME, CreateEnumerationTests.SERVICE_NAMESPACE,
			"data");
		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Service creation process failed", p.exitValue() == 0);

		// verify the service model exists
		System.out.println("Verifying the service model file exists");
		File serviceModelFile = new File(CreateEnumerationTests.SERVICE_DIR + File.separator + IntroduceConstants.INTRODUCE_XML_FILE);
		assertTrue("Service model file does not exist: " + serviceModelFile.getAbsolutePath(), serviceModelFile.exists());
		assertTrue("Service model file cannot be read: " + serviceModelFile.getAbsolutePath(), serviceModelFile.canRead());
		
		// deserialize the service model
		System.out.println("Deserializing service description from introduce.xml");
		ServiceDescription serviceDesc = (ServiceDescription) Utils.deserializeDocument(
			serviceModelFile.getAbsolutePath(), ServiceDescription.class);		
		
		// add the ws Enumeration extension
		System.out.println("Locating the ws-enumeration extension");
		ExtensionDescription ext = 
			ExtensionsLoader.getInstance().getExtension(WsEnumerationFeatureCreator.WS_ENUM_EXTENSION_NAME);
		assertNotNull("Ws enumeration extension is not available", ext);
		ExtensionType extType = new ExtensionType();
		extType.setName(ext.getServiceExtensionDescription().getName());
		extType.setExtensionType(ext.getExtensionType());
		
		System.out.println("Adding the ws-enumeration extension to the service description");
		ExtensionType[] serviceExtensions = serviceDesc.getExtensions().getExtension();
		ExtensionType[] allExtensions = new ExtensionType[serviceExtensions.length + 1];
		System.arraycopy(serviceExtensions, 0, allExtensions, 0, serviceExtensions.length);
		allExtensions[allExtensions.length - 1] = extType;
		serviceDesc.getExtensions().setExtension(allExtensions);
		
		// verify the data extension is in there
		assertTrue("Service description has no extensions", 
			serviceDesc.getExtensions() != null 
			&& serviceDesc.getExtensions().getExtension() != null
			&& serviceDesc.getExtensions().getExtension().length != 0);
		ExtensionType[] extensions = serviceDesc.getExtensions().getExtension();
		ExtensionType dataExtension = null;
		for (int i = 0; i < extensions.length; i++) {
			if (extensions[i].getName().equals("data")) {
				dataExtension = extensions[i];
				break;
			}
		}
		assertNotNull("Data service extension not found", dataExtension);
		ExtensionTypeExtensionData extData = new ExtensionTypeExtensionData();
		dataExtension.setExtensionData(extData);
		
		// edit the service description here
		// enable the ws-enumeration support feature
		System.out.println("Setting ws-enumeration feature enabled");
		Data data = ExtensionDataUtils.getExtensionData(extData);
		ServiceFeatures features = data.getServiceFeatures();
		if (features == null) {
			features = new ServiceFeatures();
			data.setServiceFeatures(features);
		}
		features.setUseWsEnumeration(true);
		ExtensionDataUtils.storeExtensionData(extData, data);
		
		// serialize the edited model to disk
		System.out.println("Serializing service model to disk");
		Utils.serializeDocument(CreateEnumerationTests.SERVICE_DIR + File.separator + IntroduceConstants.INTRODUCE_XML_FILE,
			serviceDesc, IntroduceConstants.INTRODUCE_SKELETON_QNAME);

		// run post-creation processes
		System.out.println("Executing post-creation command");
		cmd = CommonTools.getAntSkeletonPostCreationCommand(introduceBaseDir, 
			CreateEnumerationTests.SERVICE_NAME, CreateEnumerationTests.SERVICE_DIR, 
			CreateEnumerationTests.PACKAGE_NAME, CreateEnumerationTests.SERVICE_NAMESPACE, 
			"data");
		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Post creation process failed", p.exitValue() == 0);

		// rebuild the code
		System.out.println("Executing build command");
		cmd = CommonTools.getAntAllCommand(CreateEnumerationTests.SERVICE_DIR);
		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Service failed to build", p.exitValue() == 0);
	}
}
