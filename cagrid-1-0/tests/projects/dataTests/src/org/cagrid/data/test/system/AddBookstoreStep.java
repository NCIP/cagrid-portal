package org.cagrid.data.test.system;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;

import org.cagrid.data.test.creation.DataTestCaseInfo;


/**
 * AddBookstoreStep Adds the Bookstore schema to the data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 7, 2006
 * @version $Id: AddBookstoreStep.java,v 1.2 2008-05-22 20:35:30 dervin Exp $
 */
public class AddBookstoreStep extends Step {

    private DataTestCaseInfo serviceInfo;


	public AddBookstoreStep(DataTestCaseInfo info) {
		super();
		this.serviceInfo = info;
	}


	public void runStep() throws Throwable {
		System.out.println("Running step: " + getClass().getName());
		String serviceModelFile = serviceInfo.getDir() + File.separator + IntroduceConstants.INTRODUCE_XML_FILE;
		ServiceDescription desc = null;
		try {
			desc = (ServiceDescription) Utils.deserializeDocument(serviceModelFile, ServiceDescription.class);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Error loading service description: " + ex.getMessage());
		}
		// create a namespace type for the bookstore
		String bookstoreFilename = ".." + File.separator + "data" + File.separator + "test" + File.separator + "resources" + File.separator + "bookstore.xsd";

		// copy the schema to the service's lib directory
		String schemaDir = serviceInfo.getDir() + File.separator + "schema" 
		    + File.separator + serviceInfo.getName();
		File bookstoreXSDDest = new File(schemaDir + File.separator + "bookstore.xsd");
		Utils.copyFile(new File(bookstoreFilename).getAbsoluteFile(), bookstoreXSDDest);

		NamespaceType bookstoreNsType = CommonTools.createNamespaceType(
            bookstoreXSDDest.getAbsolutePath(), new File(schemaDir));
        
        // set the package name of the bookstore namespace type
        bookstoreNsType.setPackageName("org.projectmobius.bookstore");

		// add the namesapce type to the service description
		CommonTools.addNamespace(desc, bookstoreNsType);
		// save the service description back to disk
		try {
			Utils.serializeDocument(serviceModelFile, desc, IntroduceConstants.INTRODUCE_SKELETON_QNAME);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Error storing service description: " + ex.getMessage());
		}
	}
}
