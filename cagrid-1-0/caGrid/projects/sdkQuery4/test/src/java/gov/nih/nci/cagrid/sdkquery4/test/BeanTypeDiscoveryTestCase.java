package gov.nih.nci.cagrid.sdkquery4.test;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.metadata.dataservice.DomainModel;
import gov.nih.nci.cagrid.sdkquery4.beans.domaininfo.DomainTypesInformation;
import gov.nih.nci.cagrid.sdkquery4.style.beanmap.BeanTypeDiscoveryEvent;
import gov.nih.nci.cagrid.sdkquery4.style.beanmap.BeanTypeDiscoveryEventListener;
import gov.nih.nci.cagrid.sdkquery4.style.beanmap.BeanTypeDiscoveryMapper;
import gov.nih.nci.cagrid.sdkquery4.style.beanmap.DomainTypesInformationUtil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.apache.log4j.Logger;

import sun.net.www.content.audio.x_aiff;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/** 
 *  BeanTypeDiscoveryTest
 *  Test case for the bean type discovery utility
 * 
 * @author David Ervin
 * 
 * @created Jan 15, 2008 1:33:01 PM
 * @version $Id: BeanTypeDiscoveryTestCase.java,v 1.1 2008-01-16 17:05:54 dervin Exp $ 
 */
public class BeanTypeDiscoveryTestCase extends TestCase {

    public static final String BEANS_JAR_FILENAME = "ext/lib/sdk/remote-client/lib/example40-beans.jar";
    public static final String DOMAIN_MODEL_FILENAME = "test/resources/sdkExampleDomainModel.xml";
    public static final String GOLD_DOMAIN_TYPES_FILENAME = "test/resources/goldExampleDomainTypes.xml";

    private static final Logger LOG = Logger.getLogger(BeanTypeDiscoveryTestCase.class);
    
    private File beansJar = null;
    private DomainModel model = null;

    public BeanTypeDiscoveryTestCase() {
        super();
    }
    
    
    public void setUp() {
        beansJar = new File(BEANS_JAR_FILENAME);
        assertTrue("Beans jar (" + beansJar.getAbsolutePath() + ") did not exist", beansJar.exists());
        assertTrue("Beans jar (" + beansJar.getAbsolutePath() + ") could not be read", beansJar.canRead());
        LOG.debug("Using beans jar " + beansJar.getAbsolutePath());
        try {
            File modelFile = new File(DOMAIN_MODEL_FILENAME);
            LOG.debug("Using domain model from " + modelFile.getAbsolutePath());
            FileReader modelReader = new FileReader(modelFile);
            model = (DomainModel) Utils.deserializeObject(modelReader, DomainModel.class);
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error loading domain model: " + ex.getMessage());
        }
    }
    
    
    public void testTypeInfoDiscovery() {
        BeanTypeDiscoveryMapper mapper = new BeanTypeDiscoveryMapper(beansJar, model);
        mapper.addBeanTypeDiscoveryEventListener(new BeanTypeDiscoveryEventListener() {
            public void typeDiscoveryBegins(BeanTypeDiscoveryEvent e) {
                LOG.debug("Discovering information for " + e.getBeanClassname() + " (" + e.getCurrentBean() + "/" + e.getTotalBeans() + ")");
            }
        });
        
        DomainTypesInformation typesInformation = null;
        try {
            typesInformation = mapper.discoverTypesInformation();
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error processing domain types information: " + ex.getMessage());
        }        
        assertNotNull("Types information document was null", typesInformation);
        
        DomainTypesInformation goldTypes = null;
        try {
            File goldTypesFile = new File(GOLD_DOMAIN_TYPES_FILENAME);
            LOG.debug("Using gold domain types from " + goldTypesFile.getAbsolutePath());
            FileReader reader = new FileReader(goldTypesFile);
            goldTypes = DomainTypesInformationUtil.deserializeDomainTypesInformation(reader);
            reader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            fail("Error deserializing gold domain types information document: " + ex.getMessage());
        }
        
        assertEquals("Processed types information did not match expected", goldTypes, typesInformation);
    }
    
    
    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        TestResult result = runner.doRun(new TestSuite(BeanTypeDiscoveryTestCase.class));
        System.exit(result.errorCount() + result.failureCount());
    }
}
