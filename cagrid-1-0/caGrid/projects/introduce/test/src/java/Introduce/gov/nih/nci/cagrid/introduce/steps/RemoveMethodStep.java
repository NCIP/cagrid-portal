package gov.nih.nci.cagrid.introduce.steps;

import gov.nih.nci.cagrid.common.CommonTools;
import gov.nih.nci.cagrid.introduce.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.method.MethodType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;

import java.io.File;

import javax.xml.namespace.QName;

import com.atomicobject.haste.framework.Step;

public class RemoveMethodStep extends Step {
	private TestCaseInfo tci;
	private String methodName;

	public RemoveMethodStep(TestCaseInfo tci, String methodName) {
		this.tci = tci;
		this.methodName = methodName;
	}

	public void runStep() throws Throwable {
		System.out.println("Removing a simple method.");

		String pathtobasedir = System.getProperty("basedir");
		System.out.println(pathtobasedir);
		if (pathtobasedir == null) {
			System.err.println("pathtobasedir system property not set");
			throw new Exception("pathtobasedir system property not set");
		}

		ServiceDescription introService = (ServiceDescription) CommonTools
				.deserializeDocument(pathtobasedir
						+ File.separator + tci.getDir() + File.separator
						+ "introduce.xml", ServiceDescription.class);
		MethodsType methodsType = introService.getMethods();
		
		MethodType[] newMethods = new MethodType[methodsType.getMethod().length-1];
		int newMethodsI = 0;
		for(int i=0;i<methodsType.getMethod().length; i++){
			 MethodType method = methodsType.getMethod(i);
			if(!method.getName().equals(methodName)){
				newMethods[newMethodsI]=method;
				newMethodsI++;
			}
		}
		methodsType.setMethod(newMethods);

		CommonTools.serializeDocument(pathtobasedir
				+ File.separator + tci.getDir() + File.separator
				+ "introduce.xml", introService, new QName(
				"gme://gov.nih.nci.cagrid/1/Introduce", "ServiceSkeleton"));

		String cmd = CommonTools.getAntSkeletonResyncCommand(pathtobasedir
				+ File.separator + tci.getDir());

		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();

		assertEquals(0, p.exitValue());
		
		//		 look at the interface to make sure method exists.......
		String serviceInterface = pathtobasedir + File.separator + tci.dir
				+ File.separator + "src" + File.separator
				+ tci.getPackageDir() + "/common/" + tci.getName() + "I.java";
		assertTrue(!StepTools.methodExists(serviceInterface, methodName));
		

		cmd = CommonTools.getAntAllCommand(pathtobasedir + File.separator
				+ tci.getDir());

		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertEquals(0, p.exitValue());
	}

}
