package gov.nih.nci.cagrid.portal.portlet;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * Base class for all integration test
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class PortletIntegrationTestBase extends AbstractDependencyInjectionSpringContextTests {

    @Override
    protected String[] getConfigLocations() {
        return new String[]{
                "classpath*:applicationContext-portlets-test.xml"

        };
    }
}
