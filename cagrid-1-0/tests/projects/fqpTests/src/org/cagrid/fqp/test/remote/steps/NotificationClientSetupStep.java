package org.cagrid.fqp.test.remote.steps;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import gov.nih.nci.cagrid.testing.system.haste.Step;

public class NotificationClientSetupStep extends Step {
    public static final String GLOBUS_LOCATION = "GLOBUS_LOCATION";

    private static Log LOG = LogFactory.getLog(NotificationClientSetupStep.class);
    
    private String customGlobusLocation = null;
    
    public NotificationClientSetupStep() {
        
    }
    
    
    public NotificationClientSetupStep(String customGlobusLocation) {
        this.customGlobusLocation = customGlobusLocation;
    }
    

    public void runStep() throws Throwable {
        String location = null;
        if (customGlobusLocation != null) {
            LOG.debug("Using custom globus location of " + customGlobusLocation);
            location = customGlobusLocation;
        } else {
            location = System.getenv(GLOBUS_LOCATION);
            LOG.debug("Using globus location environment variable; set to " + location);
            assertNotNull(location);
        }
        setGlobusLocationProperty(location);
    }
    
    
    private void setGlobusLocationProperty(String location) {
        System.setProperty(GLOBUS_LOCATION, location);
    }
}
