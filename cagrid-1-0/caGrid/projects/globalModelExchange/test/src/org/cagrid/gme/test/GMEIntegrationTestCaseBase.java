package org.cagrid.gme.test;

import org.springframework.test.annotation.AbstractAnnotationAwareTransactionalTests;


public abstract class GMEIntegrationTestCaseBase extends AbstractAnnotationAwareTransactionalTests {

    private static final String APPLICATION_CONTEXT_GME_XML = "etc/applicationContext-gme.xml";
    private static final String TEST_APPLICATION_CONTEXT_GME_XML = "test-applicationContext-gme.xml";


    @Override
    protected String[] getConfigLocations() {
        return new String[]{"file:" + APPLICATION_CONTEXT_GME_XML, "classpath*:" + TEST_APPLICATION_CONTEXT_GME_XML};
    }

    // Really I'd like to do something like this to add the properties, but this
    // is called before the beans are loaded, so has no effect (I can't find a
    // place to override which works)
    // @Override
    // protected void customizeBeanFactory(DefaultListableBeanFactory
    // beanFactory) {
    // super.customizeBeanFactory(beanFactory);
    // PropertyPlaceholderConfigurer cfg = new PropertyPlaceholderConfigurer();
    // FileSystemResource fileSystemResource = new
    // FileSystemResource("etc/gme.properties");
    // assertNotNull(fileSystemResource);
    // assertTrue(fileSystemResource.exists());
    // cfg.setLocation(fileSystemResource);
    // // now actually do the replacement
    // cfg.postProcessBeanFactory(beanFactory);
    //        
    // beanFactory.addBeanPostProcessor(beanPostProcessor)
    // }
}
