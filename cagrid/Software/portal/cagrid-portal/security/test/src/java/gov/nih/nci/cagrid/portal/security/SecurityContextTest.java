package gov.nih.nci.cagrid.portal.security;

import org.junit.Test;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

/**
 * Test the loading of the spring context file(s)
 * User: kherm
 *
 * @author kherm manav.kher@semanticbits.com
 */
public class SecurityContextTest extends AbstractDependencyInjectionSpringContextTests {


    @Test
    public void testLoad() {
        assertNotNull(getApplicationContext());
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[]{
                "classpath*:applicationContext-security.xml",
        };
    }
}