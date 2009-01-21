package org.cagrid.index.tests.steps;

import java.io.File;
import java.io.FileWriter;

import org.jdom.Document;
import org.jdom.Element;

import gov.nih.nci.cagrid.common.XMLUtilities;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.haste.Step;

public class ChangeInitialTerminationTimeStep extends Step {
    
    public static final long DEFAULT_TIME = 5000; // 5000ms == 5 sec
    // note that the config file keeps this value in seconds, so convert accordingly!
    
    private static final String TERMINATION_TIME_ELEMENT = "InitialTerminationTime";
    
    private ServiceContainer indexServiceContainer = null;
    private long terminationTimeMills;

    public ChangeInitialTerminationTimeStep(ServiceContainer indexServiceContainer) {
        this(indexServiceContainer, DEFAULT_TIME);
    }
    
    
    public ChangeInitialTerminationTimeStep(ServiceContainer indexServiceContainer, long terminationTimeMills) {
        super();
        this.indexServiceContainer = indexServiceContainer;
        this.terminationTimeMills = terminationTimeMills;
    }


    public void runStep() throws Throwable {
        // get the config file to edit
        File configFile = new File(indexServiceContainer.getProperties().getContainerDirectory(), 
            "webapps" + File.separator + "wsrf" + File.separator + "WEB-INF" + File.separator
            + "etc" + File.separator + "globus_wsrf_mds_index"
            + File.separator + "container-registration.xml");
        assertTrue("Config file " + configFile.getAbsolutePath() + " could not be found", configFile.exists());
        
        // load the config as a DOM
        Document configDoc = null;
        try {
            configDoc = XMLUtilities.fileNameToDocument(configFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            fail("Problem loading Index Service config (" + configFile.getAbsolutePath() + "):" + e.getMessage());
        }
        
        // edit the config
        Element rootElement = configDoc.getRootElement();
        Element registrationParamsElement = rootElement.getChild(
            "ServiceGroupRegistrationParameters", rootElement.getNamespace());
        Element terminationTimeElem = registrationParamsElement.getChild(
            TERMINATION_TIME_ELEMENT, registrationParamsElement.getNamespace());
        if (terminationTimeElem == null) {
            terminationTimeElem = new Element(TERMINATION_TIME_ELEMENT, registrationParamsElement.getNamespace());
            registrationParamsElement.addContent(terminationTimeElem);
        }
        terminationTimeElem.setText(String.valueOf(getTerminationSeconds()));
        
        // save the config
        try {
            FileWriter fw = new FileWriter(configFile);
            fw.write(XMLUtilities.formatXML(XMLUtilities.documentToString(configDoc)));
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
            fail("Problem writting out config:" + configFile.getAbsolutePath());
        }
    }
    
    
    private long getTerminationSeconds() {
        return (long) Math.ceil(terminationTimeMills / 1000d);
    }

}
