package gov.nih.nci.cabig.introduce;

import java.io.File;

import org.jdom.Document;
import org.projectmobius.common.XMLUtilities;

import com.atomicobject.haste.framework.Step;

public class AddSimpleMethodStep extends Step {

	public void runStep() throws Throwable {
		System.out.println("Adding a simple method.");
		Document methodsDoc = XMLUtilities.fileNameToDocument(TestCaseInfo.dir + File.separator + "introduceMethods.xml");
		System.out.println(XMLUtilities.formatXML(XMLUtilities.documentToString(methodsDoc)));
	}

}
