package gov.nih.nci.cagrid.browser;

import javax.xml.namespace.QName;

import gov.nih.nci.cagrid.metadata.MetadataConstants;
import gov.nih.nci.cagrid.metadata.ResourcePropertyHelper;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.w3c.dom.Element;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class DomainModelTestCase extends TestCase {

	public DomainModelTestCase() {
	}

	public DomainModelTestCase(String arg0) {
		super(arg0);
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}
	
	public void testRetrieveDomainModel(){
		try{
			String serviceUrl = "http://cagrid05.bmi.ohio-state.edu:8080/wsrf/services/cagrid/CaDSR";
			EndpointReferenceType erp = new EndpointReferenceType(new Address(serviceUrl));
			Element resourceProperty = ResourcePropertyHelper.getResourceProperty(erp, new QName(MetadataConstants.CAGRID_DATA_MD_NAMESPACE, "DomainModel"));
			
		}catch(Exception ex){
			ex.printStackTrace();
			fail("Error encountered: " + ex.getMessage());
		}
	}

	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new DomainModelTestCase("testRetrieveDomainModel"));
		return suite;
	}
}
