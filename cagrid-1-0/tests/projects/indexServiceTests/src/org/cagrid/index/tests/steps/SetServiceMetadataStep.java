package org.cagrid.index.tests.steps;

import gov.nih.nci.cagrid.common.Utils;
import gov.nih.nci.cagrid.testing.system.haste.Step;

import java.io.File;

/**
 * SetServiceMetadataStep
 * Sets the service metadata document of a service
 * @author David
 */
public class SetServiceMetadataStep extends Step {
    public static final String INTRODUCE_SERVICEMETADATA_FILENAME = "serviceMetadata.xml";
    
    private File serviceDir = null;
    private StringBuffer metadata = null;
    
    public SetServiceMetadataStep(File serviceDir, StringBuffer metadata) {
        this.serviceDir = serviceDir;
        this.metadata = metadata;
    }
    

    public void runStep() throws Throwable {
        File serviceMetadata = new File(serviceDir, "etc" + File.separator + INTRODUCE_SERVICEMETADATA_FILENAME);
        if (serviceMetadata.exists()) {
            serviceMetadata.delete();
        }
        Utils.stringBufferToFile(metadata, serviceMetadata.getAbsolutePath());
    }
}
