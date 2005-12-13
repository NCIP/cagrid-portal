package gov.nih.nci.cabig.introduce.steps;

import gov.nih.nci.cabig.introduce.TestCaseInfo;
import gov.nih.nci.cagrid.common.CommonTools;

import java.io.File;
import java.io.FileWriter;

import org.jdom.Document;
import org.jdom.Element;
import org.projectmobius.common.XMLUtilities;

import com.atomicobject.haste.framework.Step;

public class AddSimpleMethodStep extends Step {
	private TestCaseInfo tci;
	
	public AddSimpleMethodStep(TestCaseInfo tci){
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

		Document methodsDoc = XMLUtilities.fileNameToDocument(pathtobasedir
				+ File.separator + tci.getDir() + File.separator
				+ "introduceMethods.xml");

		Element root = methodsDoc.getRootElement();
		Element method = new Element("method");
		method.setAttribute("name", "simpleMethod");
		root.addContent(method);
		Element output = new Element("output");
		output.setAttribute("className","void");
		method.addContent(output);

		FileWriter fw = new FileWriter(pathtobasedir + File.separator
				+ tci.getDir() + File.separator + "introduceMethods.xml");
		fw.write(XMLUtilities.formatXML(XMLUtilities
				.documentToString(methodsDoc)));
		fw.close();

		String cmd = CommonTools.getAntSkeletonResyncCommand(pathtobasedir
				+ File.separator + tci.getDir());

		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();

		assertEquals(0, p.exitValue());
	}

}
