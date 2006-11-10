package gov.nih.nci.cagrid.portal;

import org.apache.axis.message.addressing.EndpointReferenceType;
import org.apache.axis.types.URI;
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
public abstract class BaseSpringAbstractTest
        extends AbstractDependencyInjectionSpringContextTests {
    //root index is a collection of indexes that portal aggregates from
    public HashSet rootIndexSet = new HashSet();
    public EndpointReferenceType validServiceEPR;

    protected void onSetUp() throws Exception {
        super.onSetUp(); //To change body of overridden methods use File | Settings | File Templates.

        setAutowireMode(AUTOWIRE_BY_NAME);
        rootIndexSet.add(
                "http://cagrid01.bmi.ohio-state.edu:8080/wsrf/services/DefaultIndexService");

        //rootIndexSet.add("http://cagrid04.bmi.ohio-state.edu:7080/wsrf/services/DefaultIndexService");
        validServiceEPR = new EndpointReferenceType(new URI("http://cagrid04.bmi.ohio-state.edu:7080/wsrf/services/cagrid/CaDSRService"));
    }

    protected String[] getConfigLocations() {
        return new String[]{
                "classpath*:/**/applicationContext-utils.xml",
                "classpath*:/**/applicationContext-data-access.xml",
                "classpath*:/**/applicationContext-data-access-mock.xml",
        };
    }
}
