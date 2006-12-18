package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.data.ExtensionDataUtils;
import gov.nih.nci.cagrid.data.extension.CQLProcessorConfig;
import gov.nih.nci.cagrid.data.extension.CQLProcessorConfigProperty;
import gov.nih.nci.cagrid.data.extension.Data;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionType;
import gov.nih.nci.cagrid.introduce.beans.extension.ExtensionTypeExtensionData;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.File;
import java.io.FileFilter;
import java.util.Enumeration;
import java.util.Properties;

import com.atomicobject.haste.framework.Step;

/** 
 *  SetQueryProcessorStep
 *  Step to set the service's query processor to my testing one
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A>  * 
 * @created Nov 8, 2006 
 * @version $Id: SetQueryProcessorStep.java,v 1.2 2006-12-18 14:48:47 dervin Exp $ 
 */
public class SetQueryProcessorStep extends Step {
	
	private String serviceDir;
	
	public SetQueryProcessorStep(String serviceDir) {
		this.serviceDir = serviceDir;
	}
	

	public void runStep() throws Throwable {
		System.out.println("Running step: " + getClass().getName());
		String serviceModelFile = serviceDir 
			+ File.separator + IntroduceConstants.INTRODUCE_XML_FILE;
		ServiceDescription desc = null;
		try {
			desc = (ServiceDescription) Utils.deserializeDocument(
				serviceModelFile, ServiceDescription.class);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Error loading service description: " + ex.getMessage());
		}
		if (desc == null) {
			throw new NullPointerException("Service description is NULL!");
		}
		// find the data service extension
		ExtensionType[] extensions = desc.getExtensions().getExtension();
		ExtensionType dataExtension = null;
		for (int i = 0; i < extensions.length; i++) {
			if (extensions[i].getName().equals("data")) {
				dataExtension = extensions[i];
				break;
			}
		}
		if (dataExtension == null) {
			fail("Data service extension not found in service description"); 
		}
		ExtensionTypeExtensionData extData = dataExtension.getExtensionData();
		Data data = ExtensionDataUtils.getExtensionData(extData);
		// replace the properties for the query processor with defaults for testing processor
		TestingCQLQueryProcessor testProc = new TestingCQLQueryProcessor();
		Properties props = testProc.getRequiredParameters();
		CQLProcessorConfigProperty[] configProps = new CQLProcessorConfigProperty[props.size()];
		int propIndex = 0;
		Enumeration propKeys = props.keys();
		while (propKeys.hasMoreElements()) {
			String key = (String) propKeys.nextElement();
			String value = props.getProperty(key);
			configProps[propIndex] = new CQLProcessorConfigProperty(key, value);
			propIndex++;
		}
		CQLProcessorConfig config = new CQLProcessorConfig();
		config.setProperty(configProps);
		data.setCQLProcessorConfig(config);
		ExtensionDataUtils.storeExtensionData(extData, data);
		// set the service property for the new query processor
		CommonTools.setServiceProperty(desc, DataServiceConstants.QUERY_PROCESSOR_CLASS_PROPERTY, 
			TestingCQLQueryProcessor.class.getName(), false);
		// copy the testing jar file to the service
		File buildLibDir = new File("build" + File.separator + "lib");
		File[] testJars = buildLibDir.listFiles(new FileFilter() {
			public boolean accept(File pathname) {
				return (pathname.getName().endsWith("tests.jar"));
			}
		});
		String serviceLibDir = serviceDir + File.separator + "lib";
		for (int i = 0; i < testJars.length; i++) {
			Utils.copyFile(testJars[i], new File(serviceLibDir + File.separator + testJars[i].getName()));
		}
		// serialize the service model back to disk
		Utils.serializeDocument(serviceModelFile, desc, IntroduceConstants.INTRODUCE_SKELETON_QNAME);
	}
}
