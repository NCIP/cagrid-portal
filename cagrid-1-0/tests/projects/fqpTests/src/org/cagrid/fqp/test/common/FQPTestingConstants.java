package org.cagrid.fqp.test.common;

import java.io.File;

public interface FQPTestingConstants {

    public static final String CLIENT_WSDD = "/resources/wsdd/client-config.wsdd";
    public static final String QUERIES_LOCATION = 
        "resources" + File.separator + "queries" + File.separator;
    public static final String GOLD_LOCATION = 
        "resources" + File.separator + "gold" + File.separator;
    
    /**
     * Controled by a property in jndi-config.xml
     */
    public static final long RESOURCE_SWEEPER_DELAY = 2000;
    
    /**
     * Number of times to try the isProcessingComplete() method
     */
    public static final int PROCESSING_WAIT_RETRIES = 20;
    
    /**
     * ms delay between successive calls to isProcessingComplete()
     */
    public static final long PROCESSING_RETRY_DELAY = 500;
    
    /**
     * System property which points to the FQP service directory
     */
    public static final String FQP_DIR_PROPERTY = "fqp.service.dir";
    
    /**
     * System property which points to the Trnsfer service directory
     */
    public static final String TRANSFER_SERVICE_DIR_PROPERTY = "transfer.service.dir";
    
    /**
     * System property which points to the CDS service directory
     */
    public static final String CDS_SERVICE_DIR_PROPERTY = "cds.service.dir";
}
