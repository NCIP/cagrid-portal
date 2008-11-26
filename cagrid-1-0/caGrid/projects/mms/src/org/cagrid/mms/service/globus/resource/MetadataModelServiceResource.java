package org.cagrid.mms.service.globus.resource;

import javax.xml.namespace.QName;

import org.cagrid.mms.domain.ModelSourceMetadata;
import org.cagrid.mms.service.impl.MMS;
import org.cagrid.mms.stubs.MetadataModelServiceResourceProperties;
import org.globus.wsrf.ResourceException;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;


/**
 * The implementation of this MetadataModelServiceResource type.
 * 
 * @created by Introduce Toolkit version 1.3
 */
public class MetadataModelServiceResource extends MetadataModelServiceResourceBase {

    private static final String MMS_BEAN_NAME = "mms";
    private MMS mms;


    public MMS getMms() {
        return mms;
    }


    @Override
    public void initialize(Object resourceBean, QName resourceElementQName, Object id) throws ResourceException {
        super.initialize(resourceBean, resourceElementQName, id);

        // load the implementation from a spring bean
        String mmsConfigurationFile = null;
        try {
            // TODO: this causes a cycle; need a different way to find the
            // config (could just manually look up the value in JNDI)
            
            // mmsConfigurationFile =
            // MetadataModelServiceConfiguration.getConfiguration().getMmsConfigurationFile();
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

    }


    @Override
    protected void populateResourceProperties() {
        // allow the super to load some from file
        super.populateResourceProperties();

        // set the resource's ModelSourceMetadata from the Spring-loaded
        // implementation
        ((MetadataModelServiceResourceProperties) this.getResourceBean())
            .setModelSourceMetadata((ModelSourceMetadata) this.mms.getModelSourceMetadata());
    }

}
