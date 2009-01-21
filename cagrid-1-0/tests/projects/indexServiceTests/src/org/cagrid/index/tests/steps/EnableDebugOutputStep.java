package org.cagrid.index.tests.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.testing.system.deployment.TomcatServiceContainer;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;

/**
 * TODO: move this functionality to service containers, controlled by service properties
 * @author David
 *
 */
public class EnableDebugOutputStep extends Step {
    
    private TomcatServiceContainer tomcatContainer = null;

    public EnableDebugOutputStep(TomcatServiceContainer tomcatContainer) {
        this.tomcatContainer = tomcatContainer;
    }


    public void runStep() throws Throwable {
        File log4jPropertiesFile = new File(tomcatContainer.getProperties().getContainerDirectory(), "webapps/wsrf/WEB-INF/classes/log4j.properties");
        StringBuffer propertiesContents = Utils.fileToStringBuffer(log4jPropertiesFile);
        replace(propertiesContents, "# log4j.category.org.globus.mds=DEBUG", "log4j.category.org.globus.mds=DEBUG");
        replace(propertiesContents, "# log4j.category.org.globus.wsrf.handlers.MessageLoggingHandler=DEBUG", "log4j.category.org.globus.wsrf.handlers.MessageLoggingHandler=DEBUG");
        propertiesContents.append("\n");
        propertiesContents.append("log4j.category.gov.nih.nci.cagrid.advertisement=DEBUG\n");
        propertiesContents.append("log4j.category.org.globus.wsrf.servicegroup=DEBUG\n");
        Utils.stringBufferToFile(propertiesContents, log4jPropertiesFile.getAbsolutePath());
    }

    
    private void replace(StringBuffer buff, String replaceme, String replacement) {
        int index = buff.indexOf(replaceme);
        buff.replace(index, index + replaceme.length(), replacement);
    }
}
