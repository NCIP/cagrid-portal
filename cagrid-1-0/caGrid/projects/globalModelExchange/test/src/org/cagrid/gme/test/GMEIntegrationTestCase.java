package org.cagrid.gme.test;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;


public abstract class GMEIntegrationTestCase extends AbstractDependencyInjectionSpringContextTests {

    private static final String APPLICATION_CONTEXT_GME_XML = "etc/applicationContext-gme.xml";
    private static final String TEST_APPLICATION_CONTEXT_GME_XML = "test/resources/test-applicationContext-gme.xml";


    @Override
    protected String[] getConfigLocations() {
        // return new String[]{"file:" + APPLICATION_CONTEXT_GME_XML, "file:" +
        // TEST_APPLICATION_CONTEXT_GME_XML};
        return new String[]{"file:" + APPLICATION_CONTEXT_GME_XML};
    }


    @Override
    protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
        super.customizeBeanFactory(beanFactory);
        PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
        FileSystemResource fileSystemResource = new FileSystemResource("etc/gme.properties");
        assertNotNull(fileSystemResource);
        assertTrue(fileSystemResource.exists());
        cfg.setLocation(fileSystemResource);
        // now actually do the replacement
        cfg.postProcessBeanFactory(beanFactory);
    }
}
