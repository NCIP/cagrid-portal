package org.cagrid.mms.service.globus.resource;

import java.io.File;

import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.cagrid.mms.domain.ModelSourceMetadata;
import org.cagrid.mms.service.impl.MMS;
import org.cagrid.mms.service.impl.MMSGeneralException;
import org.cagrid.mms.stubs.MetadataModelServiceResourceProperties;
import org.globus.wsrf.ResourceException;
import org.globus.wsrf.config.ContainerConfig;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;


/**
 * The implementation of this MetadataModelServiceResource type.
 * 
 * @created by Introduce Toolkit version 1.3
 */
public class MetadataModelServiceResource extends MetadataModelServiceResourceBase {

    protected static Log LOG = LogFactory.getLog(MetadataModelServiceResource.class.getName());

    private static final String MMS_BEAN_NAME = "mms";
    private MMS mms;


    public MMS getMms() {
        return mms;
    }


    @Override
    public void initialize(Object resourceBean, QName resourceElementQName, Object id) throws ResourceException {

        // load the implementation from a spring bean
        String mmsConfigurationFile = null;
        try {

            // load the configuration file from our extended resource
            // configuration
            mmsConfigurationFile = ContainerConfig.getBaseDirectory() + File.separator
                + ((MetadataModelServiceResourceConfigurationExtension) getConfiguration()).getMmsConfigurationFile();

            FileSystemResource confResource = new FileSystemResource(mmsConfigurationFile);

            XmlBeanFactory factory = new XmlBeanFactory(confResource);
            // PropertyPlaceholderConfigurer cfg = new
            // PropertyPlaceholderConfigurer();
            // cfg.setLocation(mmsPropertiesResource);
            // cfg.postProcessBeanFactory(factory);

            this.mms = (MMS) factory.getBean(MMS_BEAN_NAME, MMS.class);

        } catch (Exception e) {
            throw new ResourceException("Problem loading configuration file:" + e.getMessage(), e);
        }

        super.initialize(resourceBean, resourceElementQName, id);

    }


    @Override
    protected void populateResourceProperties() {
        // allow the super to load some from file
        super.populateResourceProperties();

        // set the resource's ModelSourceMetadata from the Spring-loaded
        // implementation
        try {
            ((MetadataModelServiceResourceProperties) this.getResourceBean())
                .setModelSourceMetadata((ModelSourceMetadata) this.mms.getModelSourceMetadata());
        } catch (MMSGeneralException e) {
            LOG.error("Unable to set Model Source Metadata!", e);
        }
    }
}
