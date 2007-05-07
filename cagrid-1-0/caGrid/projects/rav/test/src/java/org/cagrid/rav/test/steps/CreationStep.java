/**
 * 
 */
package org.cagrid.rav.test.steps;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.namespace.QName;

import org.cagrid.rav.test.unit.CreationTest;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import com.atomicobject.haste.framework.Step;

/**
 * @author madduri
 * 
 */
public class CreationStep extends Step {

	private String introduceDir;

	private String applicationName = null;

	/**
	 * 
	 */
	public CreationStep(String introduceDir) {
		super();
		this.introduceDir = introduceDir;
		this.applicationName = "sol";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.atomicobject.haste.framework.Step#runStep()
	 */
	public void runStep() throws Throwable {
		System.out.println("Creating RAV Service");
		String cmd = CommonTools.getAntSkeletonCreationCommand(introduceDir,
				CreationTest.SERVICE_NAME, CreationTest.SERVICE_DIR,
				CreationTest.PACKAGE_NAME, CreationTest.SERVICE_NAMESPACE,
				"rav");
		System.out.println("Command: " + cmd);
		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		System.out.println( "Exit Value : " + p.exitValue());
		
		String line ;
		InputStreamReader iStream = new InputStreamReader(p.getErrorStream());
		BufferedReader reader = new BufferedReader(iStream);
		while((line = reader.readLine()) != null) {
			System.out.println(line);
		}
		assertTrue("Creating new rav service failed", p.exitValue() == 0);
		
		addRAVMethod();

		System.out.println("Invoking post creation processes...");
		cmd = CommonTools.getAntSkeletonPostCreationCommand(introduceDir,
				CreationTest.SERVICE_NAME, CreationTest.SERVICE_DIR,
				CreationTest.PACKAGE_NAME, CreationTest.SERVICE_NAMESPACE,
				"bdt");
		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Service post creation process failed", p.exitValue() == 0);

		System.out.println("Building created service...");
		cmd = CommonTools.getAntAllCommand(CreationTest.SERVICE_DIR);
		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertTrue("Build process failed", p.exitValue() == 0);
	}

	private void addRAVMethod() throws Throwable {
		System.out.println("Adding RAV Method");
		File serviceModelFile = new File(CreationTest.SERVICE_DIR
				+ File.separator + IntroduceConstants.INTRODUCE_XML_FILE);
		assertTrue("Service model file did not exist: "
				+ serviceModelFile.getAbsolutePath(), serviceModelFile.exists());
		assertTrue("Service model file cannot be read: "
				+ serviceModelFile.getAbsolutePath(), serviceModelFile
				.canRead());

		// deserialize the service model
		System.out
				.println("Deserializing service description from introduce.xml");
		ServiceDescription serviceDesc = (ServiceDescription) Utils
				.deserializeDocument(serviceModelFile.getAbsolutePath(),
						ServiceDescription.class);

		// get the extensions, verify RAV exists
		ExtensionType[] extensions = serviceDesc.getExtensions().getExtension();
		ExtensionType ravExtension = null;
		for (int i = 0; i < extensions.length; i++) {
			if (extensions[i].getName().equals("bdt")) {
				ravExtension = extensions[i];
				break;
			}
		}
		assertNotNull("RAV extension was not found in the service model",
				ravExtension);

		// create the BDT start
		ServiceType mainService = serviceDesc.getServices().getService(0);
		MethodType appMethod = new MethodType();
		appMethod.setName(this.applicationName);
		appMethod.setDescription("Starts an  application");
		// output of BDT client handle
		MethodTypeOutput ravOutputType = new MethodTypeOutput();
		QName handleQname = new QName("http://foo");
		ravOutputType.setQName(handleQname);
		ravOutputType.setIsArray(false);
		ravOutputType.setIsClientHandle(Boolean.TRUE);
		/*
		 * String clientHandleClass = mainService.getPackageName() +
		 * ".bdt.client." + mainService.getName() + "BulkDataHandlerClient";
		 * bdtHandleOutput.setClientHandleClass(clientHandleClass);
		 * appMethod.setOutput(bdtHandleOutput);
		 *  // add the method to the service mainService.getMethods().setMethod(
		 * (MethodType[]) Utils.appendToArray(
		 * mainService.getMethods().getMethod(), appMethod));
		 */

		// save the model back to disk for the post creation process
		Utils.serializeDocument(serviceModelFile.getAbsolutePath(),
				serviceDesc, IntroduceConstants.INTRODUCE_SKELETON_QNAME);
	}
}
