package gov.nih.nci.cagrid.portal;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import java.util.HashSet;


/**
 * Sets up the spring container for subclasses
 * to use
 * <p/>
 * <p/>
 * Created by IntelliJ IDEA.
 * User: kherm
 * Date: Jul 24, 2006
 * Time: 11:06:03 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseSpringAbstractTestCase
        extends AbstractDependencyInjectionSpringContextTests {
    //root index is a collection of indexes that portal aggregates from
    public HashSet rootIndexSet = new HashSet();

    protected void onSetUp() throws Exception {
        super.onSetUp(); //To change body of overridden methods use File | Settings | File Templates.

        rootIndexSet.add(
                "http://cagrid01.bmi.ohio-state.edu:8080/wsrf/services/DefaultIndexService");

        //rootIndexSet.add("http://cagrid04.bmi.ohio-state.edu:7080/wsrf/services/DefaultIndexService");
    }

    protected String[] getConfigLocations() {
        return new String[]{
                "classpath*:/**/applicationContext-data-access.xml",
                "classpath*:/**/applicationContext-data-access-mock.xml",
        };
    }
}
