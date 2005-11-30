package gov.nih.nci.cabig.introduce;

import java.io.File;

import org.jdom.Document;
import org.projectmobius.common.XMLUtilities;

import com.atomicobject.haste.framework.Step;

public class AddSimpleMethodStep extends Step {

	public void runStep() throws Throwable {
		System.out.println("Adding a simple method.");
		System.out.println("Removing the service skeleton");
		File f = new File(".");
		System.out.println(f.getAbsolutePath());

		String pathtobasedir = System.getProperty("basedir");
		System.out.println(pathtobasedir);
		if(pathtobasedir == null){
			System.out.println("pathtobasedir system property not set");
			throw new Exception("pathtobasedir system property not set");
		}
		Document methodsDoc = XMLUtilities.fileNameToDocument(pathtobasedir + File.separator + TestCaseInfo.dir + File.separator + "introduceMethods.xml");
		System.out.println(XMLUtilities.formatXML(XMLUtilities.documentToString(methodsDoc)));
	}

}
