package gov.nih.nci.cagrid.data.system;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.File;

import com.atomicobject.haste.framework.Step;


/**
 * AddBookstoreStep Adds the Bookstore schema to the data service
 * 
 * @author <A HREF="MAILTO:ervin@bmi.osu.edu">David W. Ervin</A> *
 * @created Nov 7, 2006
 * @version $Id: AddBookstoreStep.java,v 1.3 2007-03-02 22:35:00 oster Exp $
 */
public class AddBookstoreStep extends Step {

	private String serviceBaseDir;
	private String serviceName;


	public AddBookstoreStep(String serviceBaseDir, String serviceName) {
		super();
		this.serviceBaseDir = serviceBaseDir;
		this.serviceName = serviceName;
	}


	public void runStep() throws Throwable {
		System.out.println("Running step: " + getClass().getName());
		String serviceModelFile = this.serviceBaseDir + File.separator + IntroduceConstants.INTRODUCE_XML_FILE;
		ServiceDescription desc = null;
		try {
			desc = (ServiceDescription) Utils.deserializeDocument(serviceModelFile, ServiceDescription.class);
		} catch (Exception ex) {
			ex.printStackTrace();
			fail("Error loading service description: " + ex.getMessage());
		}
		// create a namespace type for the bookstore
		String bookstoreFilename = "test" + File.separator + "resources" + File.separator + "bookstore.xsd";

		// copy the schema to the service's lib directory
		String schemaDir = this.serviceBaseDir + File.separator + "schema" + File.separator + this.serviceName;
		File bookstoreXSDDest = new File(schemaDir + File.separator + "bookstore.xsd");
		Utils.copyFile(new File(bookstoreFilename), bookstoreXSDDest);

		NamespaceType bookstoreNsType = CommonTools.createNamespaceType(bookstoreXSDDest.getAbsolutePath(), new File(
			schemaDir));

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
