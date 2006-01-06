package gov.nih.nci.cabig.introduce.steps;

import java.io.File;

import javax.xml.namespace.QName;

import gov.nih.nci.cabig.introduce.TestCaseInfo;
import gov.nih.nci.cagrid.common.CommonTools;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsTypeMethod;

import com.atomicobject.haste.framework.Step;

public class RemoveSimpleMethodStep extends Step {
	private TestCaseInfo tci;
	
	public RemoveSimpleMethodStep(TestCaseInfo tci){
		this.tci = tci;
	}

	public void runStep() throws Throwable {
		System.out.println("Removing a simple method.");

		String pathtobasedir = System.getProperty("basedir");
		System.out.println(pathtobasedir);
		if (pathtobasedir == null) {
			System.err.println("pathtobasedir system property not set");
			throw new Exception("pathtobasedir system property not set");
		}
		
		MethodsType methodsType = (MethodsType) CommonTools
		.deserializeDocument(pathtobasedir
				+ File.separator + tci.getDir() + File.separator
				+ "introduceMethods.xml",
				MethodsType.class);

		methodsType.setMethod(null);
		
		CommonTools
		.serializeDocument(pathtobasedir
				+ File.separator + tci.getDir() + File.separator
				+ "introduceMethods.xml",
				methodsType,
				new QName(
						"gme://gov.nih.nci.cagrid.introduce/1/Methods",
						"methodsType"));

		String cmd = CommonTools.getAntSkeletonResyncCommand(pathtobasedir
				+ File.separator + tci.getDir());

		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();

		assertEquals(0, p.exitValue());
	}

}
