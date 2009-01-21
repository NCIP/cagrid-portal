package org.cagrid.index.tests.steps;

import gov.nih.nci.cagrid.common.XMLUtilities;
import gov.nih.nci.cagrid.testing.system.deployment.ServiceContainer;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;
import java.io.FileWriter;

import org.jdom.Document;
import org.jdom.Element;

public class ChangeRefreshIntervalStep extends Step {
    
    public static final long DEFAULT_REFRESH_INTERVAL = 5000; // 5000ms == 5 seconds
    // note that the config file keeps this value in seconds, so convert accordingly!
    
    private static final String REFRESH_INTERVAL_ELEM = "RefreshIntervalSecs";
    
    private ServiceContainer indexServiceContainer = null;
    private long refreshIntervalMills;

    /**
     * Creates a change refresh interval step using the default refresh interval
     * 
     * @param indexServiceContainer
     *      The service container into which the Globus index service is deployed
     * 
     * @param indexServiceContainer
     */
    public ChangeRefreshIntervalStep(ServiceContainer indexServiceContainer) {
        this(indexServiceContainer, DEFAULT_REFRESH_INTERVAL);
    }
    
    
    /**
     * Creates a change refresh interval step
     * 
     * @param indexServiceContainer
     *      The service container into which the Globus index service is deployed
     * @param refreshIntervalMills
     *      The number of milliseconds between refreshes to set
     */
    public ChangeRefreshIntervalStep(ServiceContainer indexServiceContainer, long refreshIntervalMills) {
        super();
        this.indexServiceContainer = indexServiceContainer;
        this.refreshIntervalMills = refreshIntervalMills;
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
        Element refreshIntervalElem = registrationParamsElement.getChild(
            REFRESH_INTERVAL_ELEM, registrationParamsElement.getNamespace());
        refreshIntervalElem.setText(String.valueOf(getRefreshSeconds()));
        
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
    
    
    private long getRefreshSeconds() {
        return (long) Math.ceil(refreshIntervalMills / 1000d);
    }
}
