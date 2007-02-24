package gov.nih.nci.cagrid.introduce.test.unit;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.introduce.IntroduceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.beans.namespace.NamespaceType;
import gov.nih.nci.cagrid.introduce.beans.service.ServiceType;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.info.SchemaInformation;
import gov.nih.nci.cagrid.introduce.info.ServiceInformation;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

public class IntroduceDescriptorToolsTestCase extends TestCase {

	private String baseDirectory;

	private static final String GOLD_RESOURCES = "test" + File.separator
			+ "resources" + File.separator + "gold";

	private static final String NAMESPACE1 = "gme://caCORE.caBIG/3.0/gov.nih.nci.cadsr.domain";

	private static final String NAMESPACE2 = "http://www.w3.org/2001/XMLSchema";

	private static final String SERVICE1 = "HelloWorld";

	private static final String SERVICE2 = "NewService";
	
	private static final QName VALID_QNAME = new QName("gme://caCORE.caBIG/3.0/gov.nih.nci.cadsr.domain","DataElement");

	ServiceInformation info;

	protected void setUp() throws Exception {
		super.setUp();
		baseDirectory = System.getProperty("basedir");
		if (baseDirectory == null) {
			System.err.println("basedir system property not set");
			throw new Exception("basedir system property not set");
		}

		ServiceDescription introService = (ServiceDescription) Utils
				.deserializeDocument(baseDirectory + File.separator
						+ GOLD_RESOURCES + File.separator
						+ "introduceServicesExample.xml",
						ServiceDescription.class);

		File servicePropertiesFile = new File(baseDirectory + File.separator
				+ GOLD_RESOURCES + File.separator
				+ "introduceServicesExample.properties");
		Properties serviceProperties = new Properties();
		serviceProperties.load(new FileInputStream(servicePropertiesFile));
		// have to set the service directory in the service properties
		serviceProperties.setProperty(
				IntroduceConstants.INTRODUCE_SKELETON_DESTINATION_DIR,
				baseDirectory);
		info = new ServiceInformation(introService, serviceProperties,
				new File(baseDirectory + File.separator + GOLD_RESOURCES));

	}

	public void testFindNamespace1() {
		NamespaceType type = CommonTools.getNamespaceType(info.getNamespaces(),NAMESPACE1);
		assertTrue(type != null);
		assertTrue(type.getNamespace().equals(NAMESPACE1));
	}

	public void testFindNamespace2() {
		NamespaceType type = CommonTools.getNamespaceType(info.getNamespaces(),NAMESPACE2);
		assertTrue(type != null);
		assertTrue(type.getNamespace().equals(NAMESPACE2));
	}
	
	
	public void testNotFindNamespace() {
		NamespaceType type = CommonTools.getNamespaceType(info.getNamespaces(),"www.bogusnamespace.com");
		assertTrue(type == null);
	}
	
	public void testFindSchemaInformation(){
		SchemaInformation type = CommonTools.getSchemaInformation(info.getNamespaces(),VALID_QNAME);
		assertTrue(type!=null);
		assertTrue(type.getNamespace().getNamespace().equals(VALID_QNAME.getNamespaceURI()));
		assertTrue(type.getType().getType().equals(VALID_QNAME.getLocalPart()));
	}
	
	public void testNotFindSchemaInformation(){
		SchemaInformation type = CommonTools.getSchemaInformation(info.getNamespaces(),new QName("www.bugusnamespace.com","bogusElement"));
		assertTrue(type==null);
	}

	public void testFindService1() {
		ServiceType type = CommonTools.getService(info.getServices(),(SERVICE1));
		assertTrue(type != null);
		assertTrue(type.getName().equals(SERVICE1));
	}

	public void testFindService2() {
		ServiceType type = CommonTools.getService(info.getServices(),(SERVICE2));
		assertTrue(type != null);
		assertTrue(type.getName().equals(SERVICE2));
	}
	
	public void testNotService2() {
		ServiceType type = CommonTools.getService(info.getServices(),"bogusService");
		assertTrue(type == null);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(IntroduceDescriptorToolsTestCase.class);
	}

}
