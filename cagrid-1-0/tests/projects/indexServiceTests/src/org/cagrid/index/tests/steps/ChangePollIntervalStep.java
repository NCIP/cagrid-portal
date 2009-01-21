package org.cagrid.index.tests.steps;

import gov.nih.nci.cagrid.common.XMLUtilities;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;
import java.io.FileWriter;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;

public class ChangePollIntervalStep extends Step {
    
    public static final long DEFAULT_POLL_INTERVAL = 5000; // 5000ms == 5 sec
    
    private static final String POLL_INTERVAL_ELEMENT = "PollIntervalMillis";
    
    private ServiceContainer indexServiceContainer = null;
    private long pollIntervalMills;

    public ChangePollIntervalStep(ServiceContainer indexServiceContainer) {
        this(indexServiceContainer, DEFAULT_POLL_INTERVAL);
    }
    
    
    public ChangePollIntervalStep(ServiceContainer indexServiceContainer, long pollIntervalMills) {
        super();
        this.indexServiceContainer = indexServiceContainer;
        this.pollIntervalMills = pollIntervalMills;
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
        Element contentElement = registrationParamsElement.getChild("Content", registrationParamsElement.getNamespace());
        Namespace aggNamespace = rootElement.getNamespace("agg");
        Element aggConfigElement = contentElement.getChild("AggregatorConfig", aggNamespace);
        Element pollTypeElement = aggConfigElement.getChild("GetResourcePropertyPollType", aggConfigElement.getNamespace());
        Element pollIntervalElement = pollTypeElement.getChild(POLL_INTERVAL_ELEMENT, pollTypeElement.getNamespace());
        pollIntervalElement.setText(String.valueOf(pollIntervalMills));
        
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

}
