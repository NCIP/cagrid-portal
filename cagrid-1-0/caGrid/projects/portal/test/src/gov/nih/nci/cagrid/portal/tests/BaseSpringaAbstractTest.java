package gov.nih.nci.cagrid.portal.tests;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;


/**
 *
 * Sets up the spring container for subclasses
 * to use
 *
 *
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 24, 2006
 * Time: 11:06:03 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseSpringaAbstractTest
    extends AbstractDependencyInjectionSpringContextTests {
    protected String[] getConfigLocations() {
        return new String[] {
            "classpath*:/**/applicationContext-data-access.xml",
            "classpath*:/**/applicationContext.xml",
            "classpath*:/**/applicationContext-aggregators.xml",
        };
    }
}
