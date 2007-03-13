package gov.nih.nci.cagrid.data.creation.bdt;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.data.extension.ServiceFeatures;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.File;

import com.atomicobject.haste.framework.Step;

/** 
 *  CreateBDTServiceStep
 *  Creates a BDT data service
 * 
 * @author David Ervin
 * 
 * @created Mar 13, 2007 2:53:13 PM
 * @version $Id: CreateBDTServiceStep.java,v 1.1 2007-03-13 19:28:07 dervin Exp $ 
 */
public class CreateBDTServiceStep extends Step {
	private String introduceDir;
	private String serviceName;
	private String serviceNamespace;
	private String servicePackage;
	private String serviceDir;
	
	public CreateBDTServiceStep(
		String introduceDir, String name, String pkg, String namespace, String dir) {
		this.introduceDir = introduceDir;
		this.serviceName = name;
		this.servicePackage = pkg;
		this.serviceNamespace = namespace;
		this.serviceDir = dir;
	}
	
	
	public void runStep() throws Throwable {
		System.out.println("Creating service...");

		String cmd = CommonTools.getAntSkeletonCreationCommand(introduceDir, serviceName, 
			serviceDir, servicePackage, serviceNamespace, "bdt,data");
		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Creating new data service completed successfully", p.exitValue() == 0);
		
		// verify the service model exists
		System.out.println("Verifying the service model file exists");
		File serviceModelFile = new File(serviceDir + File.separator + IntroduceConstants.INTRODUCE_XML_FILE);
		assertTrue("Service model file did not exist: " + serviceModelFile.getAbsolutePath(), serviceModelFile.exists());
		assertTrue("Service model file cannot be read: " + serviceModelFile.getAbsolutePath(), serviceModelFile.canRead());
		
		// deserialize the service model
		System.out.println("Deserializing service description from introduce.xml");
		ServiceDescription serviceDesc = (ServiceDescription) Utils.deserializeDocument(
			serviceModelFile.getAbsolutePath(), ServiceDescription.class);		

		// get the extension data, turn on BDT
		ExtensionType[] extensions = serviceDesc.getExtensions().getExtension();
		ExtensionType dataExtension = null;
		for (int i = 0; i < extensions.length; i++) {
			if (extensions[i].getName().equals("data")) {
				dataExtension = extensions[i];
				break;
			}
		}
		if (dataExtension.getExtensionData() == null) {
			dataExtension.setExtensionData(new ExtensionTypeExtensionData());
		}
		assertNotNull("Data service extension was not found in the service model", dataExtension);
		Data extensionData = ExtensionDataUtils.getExtensionData(dataExtension.getExtensionData());
		ServiceFeatures features = extensionData.getServiceFeatures();
		if (features == null) {
			features = new ServiceFeatures();
			extensionData.setServiceFeatures(features);
		}
		features.setUseBdt(true);
		ExtensionDataUtils.storeExtensionData(dataExtension.getExtensionData(), extensionData);
		Utils.serializeDocument(serviceModelFile.getAbsolutePath(), serviceDesc, IntroduceConstants.INTRODUCE_SKELETON_QNAME);
		
		System.out.println("Invoking post creation processes...");
		cmd = CommonTools.getAntSkeletonPostCreationCommand(introduceDir, serviceName,
			serviceDir, servicePackage, serviceNamespace, "bdt,data");
		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Service post creation process failed", p.exitValue() == 0);

		System.out.println("Building created service...");
		cmd = CommonTools.getAntAllCommand(serviceDir);
		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Build process failed", p.exitValue() == 0);
	}
}
