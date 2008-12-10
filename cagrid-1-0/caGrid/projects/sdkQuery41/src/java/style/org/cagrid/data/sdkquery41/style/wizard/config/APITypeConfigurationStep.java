package org.cagrid.data.sdkquery41.style.wizard.config;

import gov.nih.nci.cagrid.data.DataServiceConstants;
import gov.nih.nci.cagrid.introduce.beans.ServiceDescription;
import gov.nih.nci.cagrid.introduce.common.CommonTools;
import gov.nih.nci.cagrid.introduce.common.ServiceInformation;

import org.cagrid.data.sdkquery41.processor.SDK41QueryProcessor;

public class APITypeConfigurationStep extends AbstractStyleConfigurationStep {
    
    private ApiType apiType;
    private String hostname;
    private int portNumber;
    private boolean useHttps;

    public APITypeConfigurationStep(ServiceInformation serviceInfo) {
        super(serviceInfo);
    }


    public void applyConfiguration() throws Exception {
        // set service properties
        ServiceDescription desc = getServiceInformation().getServiceDescriptor();
        CommonTools.setServiceProperty(desc, 
            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + SDK41QueryProcessor.PROPERTY_USE_LOCAL_API, 
            String.valueOf(apiType == ApiType.LOCAL_API), false);
        CommonTools.setServiceProperty(desc,
            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + SDK41QueryProcessor.PROPERTY_HOST_NAME,
            hostname, false);
        CommonTools.setServiceProperty(desc,
            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + SDK41QueryProcessor.PROPERTY_HOST_PORT,
            String.valueOf(portNumber), false);
        CommonTools.setServiceProperty(desc,
            DataServiceConstants.QUERY_PROCESSOR_CONFIG_PREFIX + SDK41QueryProcessor.DEFAULT_HOST_HTTPS,
            String.valueOf(useHttps), false);
    }
    
        
    public void setApiType(ApiType apiType) {
        this.apiType = apiType;
    }


    public void setHostname(String hostname) {
        this.hostname = hostname;
    }


    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }


    public void setUseHttps(boolean useHttps) {
        this.useHttps = useHttps;
    }
    
    
    public static enum ApiType {
        LOCAL_API, REMOTE_API
    }
}
