package gov.nih.nci.cagrid.browser;

import edu.duke.cabig.catrip.gui.common.ServiceMetaDataBean;
import edu.duke.cabig.catrip.gui.discovery.DomainModelMetaDataRegistry;
import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.metadata.MetadataConstants;
import gov.nih.nci.cagrid.metadata.MetadataUtils;
import gov.nih.nci.cagrid.metadata.ResourcePropertyHelper;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;

import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.namespace.QName;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.axis.message.addressing.Address;
import org.apache.axis.message.addressing.EndpointReferenceType;
import org.globus.wsrf.utils.XmlUtils;
import org.w3c.dom.Element;

public class DomainModelTestCase extends TestCase {

    public DomainModelTestCase() {
    }

    public DomainModelTestCase(String arg0) {
	super(arg0);
    }

    public static void main(String[] args) {
	junit.textui.TestRunner.run(suite());
    }

    public void testRetrieveDomainModel() {
	try {
	    String serviceUrl = "http://156.40.132.182:38080/wsrf/services/cagrid/CaBIO";
	    EndpointReferenceType erp = new EndpointReferenceType(new Address(
		    serviceUrl));
	    DomainModel model = MetadataUtils.getDomainModel(erp);
	    StringWriter w = new StringWriter();
	    MetadataUtils.serializeDomainModel(model, w);
	    System.out.println(w.getBuffer());
	} catch (Exception ex) {
	    ex.printStackTrace();
	    fail("Error encountered: " + ex.getMessage());
	}
    }

    
    public void testPopulateDomainModel(){
	try {
	    String serviceUrl = "http://156.40.132.182:38080/wsrf/services/cagrid/CaBIO";
	    EndpointReferenceType erp = new EndpointReferenceType(new Address(
		    serviceUrl));
	    ServiceMetaDataBean svcBean = new ServiceMetaDataBean();
	    svcBean.setServiceName("Some Name");
	    svcBean.setServiceUrl(erp.getAddress().toString());
	    DomainModel model = MetadataUtils.getDomainModel(erp);
	    DomainModelMetaDataRegistry.populateDomainModelMetaData(model, svcBean);
	} catch (Exception ex) {
	    ex.printStackTrace();
	    fail("Error encountered: " + ex.getMessage());
	}
    }
    
    
    
    public static Test suite() {
	TestSuite suite = new TestSuite();
	suite.addTest(new DomainModelTestCase("testRetrieveDomainModel"));
	suite.addTest(new DomainModelTestCase("testPopulateDomainModel"));
	return suite;
    }
}
