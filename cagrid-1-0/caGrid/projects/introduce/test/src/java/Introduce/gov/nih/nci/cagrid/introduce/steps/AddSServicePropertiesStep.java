package gov.nih.nci.cagrid.introduce.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.TestCaseInfo;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.property.ServiceProperties;
import gov.nih.nci.cagrid.introduce.beans.property.ServicePropertiesProperty;
import gov.nih.nci.cagrid.introduce.codegen.SyncTools;
import gov.nih.nci.cagrid.introduce.common.CommonTools;

import java.io.File;

import javax.xml.namespace.QName;

import com.atomicobject.haste.framework.Step;


public class AddSServicePropertiesStep extends Step {
	private TestCaseInfo tci;

	public AddSServicePropertiesStep(TestCaseInfo tci) {
		this.tci = tci;
	}


	public void runStep() throws Throwable {
		System.out.println("Adding a simple method.");

		String pathtobasedir = System.getProperty("basedir");
		System.out.println(pathtobasedir);
		if (pathtobasedir == null) {
			System.err.println("basedir system property not set");
			throw new Exception("basedir system property not set");
		}

		ServiceDescription introService = (ServiceDescription) Utils.deserializeDocument(pathtobasedir + File.separator
			+ tci.getDir() + File.separator + "introduce.xml", ServiceDescription.class);
		ServiceProperties props = introService.getServiceProperties();
		if(props == null){
			props = new ServiceProperties();
			introService.setServiceProperties(props);
		}
		
		ServicePropertiesProperty newProperty1 = new ServicePropertiesProperty();
		newProperty1.setKey("foo");
		
		ServicePropertiesProperty newProperty2 = new ServicePropertiesProperty();
		newProperty2.setKey("bar");
		newProperty2.setKey("barValue");

		// add new method to array in bean
		// this seems to be a wierd way be adding things....
		ServicePropertiesProperty[] propertyTypeArr;
		int newLength = 0;
		if (props.getProperty() != null) {
			newLength = props.getProperty().length + 2;
			propertyTypeArr = new ServicePropertiesProperty[newLength];
			System.arraycopy(props.getProperty(), 0, propertyTypeArr, 0, props.getProperty().length);
		} else {
			newLength = 2;
			propertyTypeArr = new ServicePropertiesProperty[newLength];
		}
		propertyTypeArr[newLength - 2] = newProperty1;
		propertyTypeArr[newLength - 1] = newProperty2;
		props.setProperty(propertyTypeArr);

		Utils.serializeDocument(pathtobasedir + File.separator + tci.getDir() + File.separator + "introduce.xml",
			introService, new QName("gme://gov.nih.nci.cagrid/1/Introduce", "ServiceSkeleton"));

		try {
			SyncTools sync = new SyncTools(new File(pathtobasedir + File.separator + tci.getDir()));
			sync.sync();
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.getMessage());
		}

		String cmd = CommonTools.getAntAllCommand(pathtobasedir + File.separator + tci.getDir());

		Process p = CommonTools.createAndOutputProcess(cmd);
		p.waitFor();
		assertEquals("Checking build status", 0, p.exitValue());
	}

}
