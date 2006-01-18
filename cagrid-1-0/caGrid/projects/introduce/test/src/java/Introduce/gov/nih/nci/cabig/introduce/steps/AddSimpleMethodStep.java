package gov.nih.nci.cabig.introduce.steps;

import gov.nih.nci.cabig.introduce.TestCaseInfo;
import gov.nih.nci.cagrid.common.CommonTools;
import gov.nih.nci.cagrid.introduce.beans.method.MethodTypeOutput;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsType;
import gov.nih.nci.cagrid.introduce.beans.method.MethodsTypeMethod;

import java.io.File;
import java.io.FileWriter;

import javax.xml.namespace.QName;

import org.jdom.Document;
import org.jdom.Element;
import org.projectmobius.common.XMLUtilities;

import com.atomicobject.haste.framework.Step;


public class AddSimpleMethodStep extends Step {
	private TestCaseInfo tci;


	public AddSimpleMethodStep(TestCaseInfo tci) {
		this.tci = tci;
	}


	public void runStep() throws Throwable {
		System.out.println("Adding a simple method.");

		String pathtobasedir = System.getProperty("basedir");
		System.out.println(pathtobasedir);
		if (pathtobasedir == null) {
			System.err.println("pathtobasedir system property not set");
			throw new Exception("pathtobasedir system property not set");
		}

		MethodsType methodsType = (MethodsType) CommonTools.deserializeDocument(pathtobasedir + File.separator
			+ tci.getDir() + File.separator + "introduceMethods.xml", MethodsType.class);

		MethodsTypeMethod method = new MethodsTypeMethod();
		method.setName("newMethod");
		MethodTypeOutput output = new MethodTypeOutput();
		output.setClassName("void");
		method.setOutput(output);

		// add new method to array in bean
		// this seems to be a wierd way be adding things....
		MethodsTypeMethod[] newMethods;
		int newLength = 0;
		if (methodsType.getMethod() != null) {
			newLength = methodsType.getMethod().length + 1;
			newMethods = new MethodsTypeMethod[newLength];
			System.arraycopy(methodsType.getMethod(), 0, newMethods, 0, methodsType.getMethod().length);
		} else {
			newLength = 1;
			newMethods = new MethodsTypeMethod[newLength];
		}
		newMethods[newLength - 1] = method;
		methodsType.setMethod(newMethods);

		CommonTools.serializeDocument(pathtobasedir + File.separator + tci.getDir() + File.separator
			+ "introduceMethods.xml", methodsType, new QName("gme://gov.nih.nci.cagrid.introduce/1/Methods",
			"methodsType"));

		String cmd = CommonTools.getAntSkeletonResyncCommand(pathtobasedir + File.separator + tci.getDir());

		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();

		assertEquals(0, p.exitValue());

		cmd = CommonTools.getAntAllCommand(pathtobasedir + File.separator + tci.getDir());

		p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertEquals(0, p.exitValue());
	}

}
